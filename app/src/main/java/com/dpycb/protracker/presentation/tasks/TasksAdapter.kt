package com.dpycb.protracker.presentation.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dpycb.protracker.R
import com.dpycb.protracker.databinding.TaskItemViewBinding

class TasksAdapter(
    private val onItemClick: (Long) -> Unit,
    private val onItemLongClick: (Long) -> Boolean
) : ListAdapter<TaskViewState, TaskItemViewHolder>(
    object : DiffUtil.ItemCallback<TaskViewState>() {
        override fun areItemsTheSame(oldItem: TaskViewState, newItem: TaskViewState): Boolean {
            return oldItem.taskId == newItem.taskId
        }

        override fun areContentsTheSame(oldItem: TaskViewState, newItem: TaskViewState): Boolean {
            return oldItem.title == newItem.title
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemViewHolder {
        return TaskItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.task_item_view, parent, false),
            onItemClick,
            onItemLongClick
        )
    }

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class TaskItemViewHolder(
    private val view: View,
    private val onItemClick: (Long) -> Unit,
    private val onItemLongClick: (Long) -> Boolean
) : RecyclerView.ViewHolder(view) {
    private val binding = TaskItemViewBinding.bind(itemView)

    fun bind(item: TaskViewState) {
        binding.apply {
            root.setOnClickListener { onItemClick(item.taskId) }
            root.setOnLongClickListener { onItemLongClick(item.taskId) }
            taskProgress.text = item.progress
            taskTitle.text = item.title
            taskEndDate.text = item.endDate
        }
    }
}