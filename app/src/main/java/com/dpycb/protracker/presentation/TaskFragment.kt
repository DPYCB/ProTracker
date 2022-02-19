package com.dpycb.protracker.presentation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.dpycb.protracker.R
import com.dpycb.protracker.databinding.TaskFragmentBinding
import com.dpycb.protracker.utils.viewBinding
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TaskFragment : Fragment(R.layout.task_fragment) {
    private val binding by viewBinding(TaskFragmentBinding::bind)
    private val compositeDisposable = CompositeDisposable()
    private val adapter = TasksAdapter(::showTaskDetails)

    @Inject
    lateinit var viewModel: TaskFragmentViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnAddTask.setOnClickListener { addTask() }
            btnAddTask.setOnLongClickListener { removeAllTasks() }
            tasksList.adapter = adapter
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel
            .getTasksFlow()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(adapter::submitList)
            .let(compositeDisposable::add)

        viewModel
            .getTotalProgressFlow()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(binding.tasksProgress::setText)
            .let(compositeDisposable::add)
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    private fun removeAllTasks(): Boolean {
        Maybe.just(true)
            .subscribeOn(Schedulers.io())
            .subscribe { viewModel.removeAllTasks() }
            .let(compositeDisposable::add)
        return true
    }

    private fun showTaskDetails(taskId: Long) {
        TaskDetailsFragment.create(taskId).show(parentFragmentManager, TaskDetailsFragment.TAG)
    }

    private fun addTask() {
        AddTaskBottomSheet().show(parentFragmentManager, AddTaskBottomSheet.TAG)
    }
}