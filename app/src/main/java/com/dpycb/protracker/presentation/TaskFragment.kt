package com.dpycb.protracker.presentation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import com.dpycb.protracker.R
import com.dpycb.protracker.databinding.TaskFragmentBinding
import com.dpycb.protracker.utils.viewBinding
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TaskFragment : Fragment(R.layout.task_fragment) {
    private val binding by viewBinding(TaskFragmentBinding::bind)
    private val compositeDisposable = CompositeDisposable()

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = (requireActivity() as MainActivity)
            .getActivityComponent()
            .viewModelFactory.create(TaskFragmentViewModel::class.java)
        binding.apply {
            btnAddTask.setOnClickListener { addTask(viewModel) }
            btnShowTask.setOnClickListener {
                val task = viewModel.getRandomTask()
                val label = task?.uid?.toString() ?: "Null recieved"
                resultLabel.text = label
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun addTask(viewModel: TaskFragmentViewModel) {
        Flowable
            .just(true)
            .subscribeOn(Schedulers.io())
            .subscribe {
                viewModel.addRandomTask()
            }
            .let(compositeDisposable::add)
    }
}