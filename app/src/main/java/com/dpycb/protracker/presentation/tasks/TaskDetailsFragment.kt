package com.dpycb.protracker.presentation.tasks

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResultListener
import com.dpycb.protracker.R
import com.dpycb.protracker.data.GoalStatus
import com.dpycb.protracker.data.Task
import com.dpycb.protracker.databinding.TaskDetailsFragmentBinding
import com.dpycb.protracker.utils.Utils
import com.dpycb.protracker.utils.viewBinding
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TaskDetailsFragment : DialogFragment(R.layout.task_details_fragment) {
    companion object {
        const val TAG = "TaskDetailsFragment"
        private const val BUNDLE_TASK_ID = "BundleTaskId"
        fun create(taskId: Long): TaskDetailsFragment {
            return TaskDetailsFragment().apply {
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
    lateinit var viewModel: TaskDetailsViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            goalsList.adapter = adapter
        }
        taskId = arguments?.getLong(BUNDLE_TASK_ID) ?: 0L

        setFragmentResultListener(NewGoalDialogFragment.EDIT_GOAL_REQUEST) { requestKey, bundle ->
            val goalId = bundle.getInt(NewGoalDialogFragment.BUNDLE_GOAL_ID)
            val goalName = bundle.getString(NewGoalDialogFragment.BUNDLE_GOAL_NAME) ?: ""
            val goalWeight = bundle.getInt(NewGoalDialogFragment.BUNDLE_GOAL_WEIGHT)
            val goalStatus = bundle.getString(NewGoalDialogFragment.BUNDLE_GOAL_STATUS)
                ?: GoalStatus.NOT_STARTED.name
            Maybe.just(true)
                .subscribeOn(Schedulers.io())
                .subscribe{
                    viewModel.editGoal(
                        taskId,
                        GoalViewState(
                            id = goalId,
                            name = goalName,
                            weight = goalWeight,
                            status = GoalStatus.valueOf(goalStatus)
                        )
                    )
                }.let(compositeDisposable::add)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel
            .getTaskFlow(taskId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::updateTaskInfo)
            .let(compositeDisposable::add)

        viewModel
            .getGoalsFlow(taskId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(adapter::submitList)
            .let(compositeDisposable::add)
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()
    }

    private fun updateTaskInfo(task: Task) {
        binding.apply {
            taskName.text = task.name
            startDate.text = Utils.formatDateToString(task.startDate)
            endDate.text = Utils.formatDateToString(task.endDate)
            progress.text = "${task.progress} %"
        }
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