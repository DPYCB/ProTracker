package com.dpycb.tasks.presentation.goals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dpycb.tasks.R
import com.dpycb.tasks.data.Goal
import com.dpycb.tasks.databinding.NewGoalItemBinding

class GoalsAdapter(
    private val editGoal: (Int) -> Unit,
    private val addGoal: () -> Unit
) : ListAdapter<Goal, NewGoalViewHolder>(
    object : DiffUtil.ItemCallback<Goal>() {
        override fun areItemsTheSame(oldItem: Goal, newItem: Goal): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: Goal, newItem: Goal): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
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

    fun bind(item: Goal) {
        binding.apply {
            if (item.uid == Int.MAX_VALUE) {
                root.setOnClickListener { addGoal() }
            } else {
                root.setOnClickListener { editGoal(item.uid) }
            }
            goalName.text = item.name
            weight.text = item.weight.toString()
            weight.isVisible = item.weight in (1..10)
            statusLabel.text = item.status.statusName
            statusLabel.isVisible = item.weight in (1..10)
        }
    }
}