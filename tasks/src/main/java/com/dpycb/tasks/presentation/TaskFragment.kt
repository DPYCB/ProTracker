package com.dpycb.tasks.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dpycb.tasks.R
import com.dpycb.tasks.databinding.TaskFragmentBinding
import com.dpycb.utils.di.DaggerViewModelFactory
import com.dpycb.utils.view.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TaskFragment : Fragment(R.layout.task_fragment) {
    private val binding by viewBinding(TaskFragmentBinding::bind)
    private val compositeDisposable = CompositeDisposable()
    private val adapter = TasksAdapter(
        ::showTaskDetails,
        ::removeTask
    )

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory
    private val viewModel: TaskFragmentViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnAddTask.setOnClickListener { addTask() }
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

    private fun showTaskDetails(taskId: Long) {
        TaskDetailsDialogFragment.create(taskId).show(parentFragmentManager, TaskDetailsDialogFragment.TAG)
    }

    private fun removeTask(taskId: Long): Boolean {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Точно хочешь удалить задачу, ленивая ты жопа?)")
            .setPositiveButton("Да") { _, _ ->
                viewModel.removeTask(taskId)
            }
            .setNegativeButton("Нет") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
        return true
    }

    private fun addTask() {
        AddTaskBSFragment().show(parentFragmentManager, AddTaskBSFragment.TAG)
    }
}