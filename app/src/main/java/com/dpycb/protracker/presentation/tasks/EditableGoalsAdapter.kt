package com.dpycb.protracker.presentation.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dpycb.protracker.R
import com.dpycb.protracker.databinding.NewGoalItemBinding

class GoalsAdapter(
    private val editGoal: (Int) -> Unit,
    private val addGoal: () -> Unit
): ListAdapter<GoalViewState, NewGoalViewHolder>(
    object : DiffUtil.ItemCallback<GoalViewState>() {
        override fun areItemsTheSame(oldItem: GoalViewState, newItem: GoalViewState): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GoalViewState, newItem: GoalViewState): Boolean {
            return oldItem.name == newItem.name
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewGoalViewHolder {
        return NewGoalViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.new_goal_item, parent, false),
            editGoal,
            addGoal
        )
    }

    override fun onBindViewHolder(holder: NewGoalViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class NewGoalViewHolder(
   view: View,
    private val editGoal: (Int) -> Unit,
    private val addGoal: () -> Unit
): RecyclerView.ViewHolder(view) {
    private val binding = NewGoalItemBinding.bind(view)

    fun bind(item: GoalViewState) {
        binding.apply {
            if (item.id == Int.MAX_VALUE) {
                root.setOnClickListener { addGoal() }
            }
            else {
                root.setOnClickListener {  editGoal(item.id) }
            }
            goalName.text = item.name
            weight.text = item.weight.toString()
            weight.isVisible = item.weight in (1..10)
            statusLabel.text = item.status.name
        }
    }
}