package com.dpycb.tasks.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.dpycb.tasks.R
import com.dpycb.tasks.data.Goal
import com.dpycb.tasks.data.GoalStatus
import com.dpycb.tasks.data.Task
import com.dpycb.tasks.databinding.TaskDetailsFragmentBinding
import com.dpycb.tasks.presentation.goals.GoalsAdapter
import com.dpycb.tasks.presentation.goals.NewGoalDialogFragment
import com.dpycb.utils.di.DaggerViewModelFactory
import com.dpycb.utils.formatDateToString
import com.dpycb.utils.view.viewBinding
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TaskDetailsDialogFragment : DialogFragment(R.layout.task_details_fragment) {
    companion object {
        const val TAG = "TaskDetailsFragment"
        private const val BUNDLE_TASK_ID = "BundleTaskId"
        fun create(taskId: Long): TaskDetailsDialogFragment {
            return TaskDetailsDialogFragment().apply {
                arguments = bundleOf(
                    BUNDLE_TASK_ID to taskId
                )
            }
        }
    }

    private val binding by viewBinding(TaskDetailsFragmentBinding::bind)
    private val compositeDisposable = CompositeDisposable()
    private val adapter = GoalsAdapter(::editGoal) {}
    private var taskId = 0L

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory
    private val viewModel: TaskDetailsViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.goalsList.adapter = adapter
        taskId = arguments?.getLong(BUNDLE_TASK_ID) ?: 0L

        setFragmentResultListener(NewGoalDialogFragment.EDIT_GOAL_REQUEST) { requestKey, bundle ->
            val goalId = bundle.getInt(NewGoalDialogFragment.BUNDLE_GOAL_ID)
            val goalName = bundle.getString(NewGoalDialogFragment.BUNDLE_GOAL_NAME) ?: ""
            val goalWeight = bundle.getInt(NewGoalDialogFragment.BUNDLE_GOAL_WEIGHT)
            val goalStatus = bundle.getString(NewGoalDialogFragment.BUNDLE_GOAL_STATUS)
                ?: GoalStatus.NOT_STARTED.name
            viewModel.editGoal(
                taskId,
                Goal(
                    uid = goalId,
                    name = goalName,
                    weight = goalWeight,
                    status = GoalStatus.valueOf(goalStatus)
                )
            )
        }

        viewModel
            .getTaskFlow(taskId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::updateTaskInfo)
            .let(compositeDisposable::add)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
    }

    private fun updateTaskInfo(task: Task) {
        binding.apply {
            taskName.text = task.name
            startDate.text = formatDateToString(task.startDate)
            endDate.text = formatDateToString(task.endDate)
            progress.text = "${task.progress} %"
        }
        adapter.submitList(task.goals)
    }

    private fun editGoal(goalId: Int) {
        Maybe.just(true)
            .subscribeOn(Schedulers.io())
            .flatMap {
                Maybe.just(viewModel.getGoalById(taskId, goalId))
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { currentGoal ->
                NewGoalDialogFragment.create(
                    goalId,
                    currentGoal?.name ?: "",
                    currentGoal?.weight ?: 1,
                    currentGoal?.status?.name ?: GoalStatus.NOT_STARTED.name
                ).show(parentFragmentManager, NewGoalDialogFragment.TAG)
            }
            .let(compositeDisposable::add)
    }
}