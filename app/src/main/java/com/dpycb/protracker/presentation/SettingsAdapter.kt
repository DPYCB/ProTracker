package com.dpycb.protracker.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dpycb.protracker.R
import com.dpycb.protracker.databinding.SettingsItemBinding

class SettingsAdapter: ListAdapter<SettingsViewState, SettingsItemViewHolder>(
    object : DiffUtil.ItemCallback<SettingsViewState>() {
        override fun areItemsTheSame(
            oldItem: SettingsViewState,
            newItem: SettingsViewState
        ) =  oldItem.iconResId == newItem.iconResId

        override fun areContentsTheSame(
            oldItem: SettingsViewState,
            newItem: SettingsViewState
        ) =  oldItem.text == newItem.text

    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsItemViewHolder {
        return SettingsItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.settings_item, parent, false),
        )
    }

    override fun onBindViewHolder(holder: SettingsItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class SettingsItemViewHolder(private val view: View, ) : RecyclerView.ViewHolder(view) {
    private val binding = SettingsItemBinding.bind(itemView)

    fun bind(item: SettingsViewState) {
        binding.apply {
            root.setOnClickListener { item.onClick() }
            itemIcon.setImageResource(item.iconResId)
            itemLabel.text = item.text
        }
    }
}