package com.dpycb.protracker.presentation

import android.app.Activity
import android.content.Context
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.dpycb.protracker.R
import com.dpycb.protracker.Utils
import com.dpycb.protracker.data.Task
import com.dpycb.protracker.databinding.TaskDetailsFragmentBinding
import com.dpycb.protracker.utils.viewBinding
import dagger.android.support.AndroidSupportInjection
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

    @Inject
    lateinit var viewModel: TaskDetailsViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onStart() {
        super.onStart()
        val taskId = arguments?.getLong(BUNDLE_TASK_ID) ?: 0L
        viewModel
            .getTaskFlow(taskId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::updateTaskInfo)
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
}