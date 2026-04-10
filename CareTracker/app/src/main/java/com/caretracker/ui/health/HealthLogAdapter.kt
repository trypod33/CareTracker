package com.caretracker.ui.health

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.caretracker.data.models.HealthLog
import com.caretracker.databinding.ItemHealthLogBinding
import java.text.SimpleDateFormat
import java.util.*

class HealthLogAdapter(private val onClick: (HealthLog) -> Unit) :
    ListAdapter<HealthLog, HealthLogAdapter.HealthLogViewHolder>(DiffCallback) {

    private val dateFormat = SimpleDateFormat("MMM dd, yyyy  h:mm a", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthLogViewHolder {
        return HealthLogViewHolder(
            ItemHealthLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HealthLogViewHolder, position: Int) {
        val log = getItem(position)
        holder.binding.tvLogValue.text = log.value
        holder.binding.tvLogTimestamp.text = dateFormat.format(Date(log.timestamp))
        holder.binding.tvLogNotes.text = log.notes ?: ""
        holder.itemView.setOnClickListener { onClick(log) }
    }

    class HealthLogViewHolder(val binding: ItemHealthLogBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object DiffCallback : DiffUtil.ItemCallback<HealthLog>() {
        override fun areItemsTheSame(oldItem: HealthLog, newItem: HealthLog) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: HealthLog, newItem: HealthLog) =
            oldItem == newItem
    }
}
