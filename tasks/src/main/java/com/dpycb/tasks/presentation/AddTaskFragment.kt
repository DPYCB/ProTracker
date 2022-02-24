package com.dpycb.tasks.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.dpycb.tasks.R
import com.dpycb.tasks.data.Goal
import com.dpycb.tasks.data.GoalStatus
import com.dpycb.tasks.databinding.AddTaskFragmentBinding
import com.dpycb.tasks.presentation.goals.GoalsAdapter
import com.dpycb.tasks.presentation.goals.NewGoalDialogFragment
import com.dpycb.utils.Utils
import com.dpycb.utils.di.DaggerViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddTaskFragment : BottomSheetDialogFragment() {
    companion object {
        const val TAG = "AddTaskBottomSheet"
        const val NEW_GOAL_ID = Int.MAX_VALUE
    }

    private var binding: AddTaskFragmentBinding? = null
    private val compositeDisposable = CompositeDisposable()
    private val adapter = GoalsAdapter(
        ::editGoal,
        ::addGoal
    )
    private var startDate = 0L
    private var endDate = 0L

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory
    private val viewModel by viewModels<AddTaskViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_task_fragment, container)
        binding = AddTaskFragmentBinding.bind(view)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            goalsList.adapter = adapter
            btnAddTask.setOnClickListener { addNewTask() }
        }
        initDatePickers()
        setFragmentResultListener(NewGoalDialogFragment.EDIT_GOAL_REQUEST, ::onFragmentResult)
    }

    override fun onStart() {
        super.onStart()
        viewModel
            .getGoalsFlow()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(adapter::submitList)
            .let(compositeDisposable::add)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        compositeDisposable.dispose()
    }

    private fun onFragmentResult(requestCode: String, bundle: Bundle) {
        val goalId = bundle.getInt(NewGoalDialogFragment.BUNDLE_GOAL_ID, NEW_GOAL_ID)
        val goalName = bundle.getString(NewGoalDialogFragment.BUNDLE_GOAL_NAME) ?: ""
        val goalWeight = bundle.getInt(NewGoalDialogFragment.BUNDLE_GOAL_WEIGHT)
        val goalStatusString = bundle.getString(NewGoalDialogFragment.BUNDLE_GOAL_STATUS)
            ?: GoalStatus.NOT_STARTED.name
        val goalStatus = GoalStatus.valueOf(goalStatusString)
        when (goalId == NEW_GOAL_ID) {
            true -> viewModel.addNewGoal(goalName, goalWeight, goalStatus)
            else -> viewModel.editGoal(
                Goal(
                    uid = goalId,
                    name = goalName,
                    weight = goalWeight,
                    status = goalStatus
                )
            )
        }
    }

    private fun initDatePickers() {
        binding?.apply {
            startDateEdit.setOnClickListener {
                val picker = getDatePicker()
                picker.addOnPositiveButtonClickListener { date ->
                    startDate = date
                    startDateEdit.text = Utils.formatDateToString(date)
                }
                picker.show(parentFragmentManager, "DATE_PICKER")
            }
            endDateEdit.setOnClickListener {
                val picker = getDatePicker()
                picker.addOnPositiveButtonClickListener { date ->
                    endDate = date
                    endDateEdit.text = Utils.formatDateToString(date)
                }
                picker.show(parentFragmentManager, "DATE_PICKER")
            }
        }
    }

    private fun getDatePicker(): MaterialDatePicker<Long> {
        return MaterialDatePicker.Builder
            .datePicker()
            .setTitleText("Выберите дату")
            .build()
    }

    private fun editGoal(goalId: Int) {
        val currentGoal = viewModel.getGoalById(goalId)
        NewGoalDialogFragment.create(
            goalId,
            currentGoal?.name ?: "",
            currentGoal?.weight ?: 1,
            currentGoal?.status?.name ?: GoalStatus.NOT_STARTED.name
        ).show(parentFragmentManager, NewGoalDialogFragment.TAG)
    }

    private fun addGoal() {
        NewGoalDialogFragment.create(
            NEW_GOAL_ID,
            "",
            1,
            GoalStatus.NOT_STARTED.name
        ).show(parentFragmentManager, NewGoalDialogFragment.TAG)
    }

    private fun addNewTask() {
        val (name, startDateText, endDateText) = binding?.let {
            listOf(
                it.taskNameEdit.text,
                it.startDateEdit.text,
                it.endDateEdit.text
            ).map(CharSequence::toString)
        } ?: listOf()

        binding?.apply {
            val hasEmptyInputs = name.isEmpty()
                    || startDateText.isEmpty()
                    || endDateText.isEmpty()
            val hasNoGoals = goalsList.adapter?.itemCount ?: 0 < 1
            val hasIncorrectDate = startDate > endDate
            val toastText = when {
                hasEmptyInputs -> "Все поля должны быть заполнены!"
                hasIncorrectDate -> "Неверная дата!"
                hasNoGoals -> "Необходимо добавить цели!"
                else -> null
            }
            toastText?.let {
                Toast.makeText(
                    requireContext(),
                    it,
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
        }
        viewModel.addNewTask(name, startDate, endDate)
        dismiss()
    }
}