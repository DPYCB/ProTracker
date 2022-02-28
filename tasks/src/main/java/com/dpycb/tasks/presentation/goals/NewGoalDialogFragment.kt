package com.dpycb.tasks.presentation.goals

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.dpycb.tasks.R
import com.dpycb.tasks.data.GoalStatus
import com.dpycb.tasks.databinding.EditGoalDialogBinding
import com.dpycb.utils.view.viewBinding

class NewGoalDialogFragment : DialogFragment(R.layout.edit_goal_dialog) {
    companion object {
        const val EDIT_GOAL_REQUEST = "editGoalRequest"
        const val TAG = "NewGoalDialogFragment"
        const val BUNDLE_GOAL_ID = "GoalId"
        const val BUNDLE_GOAL_NAME = "GoalName"
        const val BUNDLE_GOAL_WEIGHT = "GoalWeight"
        const val BUNDLE_GOAL_STATUS = "GoalStatus"
        fun create(goalId: Int, goalName: String, goalWeight: Int, goalStatus: String): NewGoalDialogFragment {
            return NewGoalDialogFragment().apply {
                arguments = bundleOf(
                    BUNDLE_GOAL_ID to goalId,
                    BUNDLE_GOAL_NAME to goalName,
                    BUNDLE_GOAL_WEIGHT to goalWeight,
                    BUNDLE_GOAL_STATUS to goalStatus
                )
            }
        }
    }

    private val binding by viewBinding(EditGoalDialogBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val setGoalName = arguments?.getString(BUNDLE_GOAL_NAME) ?: ""
            val setGoalWeight = arguments?.getInt(BUNDLE_GOAL_WEIGHT) ?: 1
            val goalId = arguments?.getInt(BUNDLE_GOAL_ID) ?: Int.MAX_VALUE
            val setGoalStatus = arguments?.getString(BUNDLE_GOAL_STATUS) ?: GoalStatus.NOT_STARTED.name
            goalNameEdit.setText(setGoalName)
            weightSlider.value = setGoalWeight.toFloat()
            statusSpinner.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                GoalStatus.values().map(GoalStatus::statusName),
            )
            statusSpinner.setSelection(GoalStatus.valueOf(setGoalStatus).ordinal)

            btnApply.setOnClickListener {
                setFragmentResult(
                    EDIT_GOAL_REQUEST, bundleOf(
                        BUNDLE_GOAL_ID to goalId,
                        BUNDLE_GOAL_NAME to goalNameEdit.text.toString(),
                        BUNDLE_GOAL_WEIGHT to weightSlider.value.toInt(),
                        BUNDLE_GOAL_STATUS to getSelectedItem(statusSpinner.selectedItem.toString())
                    )
                )
                dismiss()
            }
        }
    }

    private fun getSelectedItem(selectedItem: String): String {
        return GoalStatus.values().find { it.statusName == selectedItem }?.name
            ?: GoalStatus.NOT_STARTED.name
    }
}