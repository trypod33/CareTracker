package com.caretracker.ui.habits

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.caretracker.R
import com.caretracker.data.entities.MoodJournalEntity

class JournalAdapter : ListAdapter<MoodJournalEntity, JournalAdapter.VH>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<MoodJournalEntity>() {
            override fun areItemsTheSame(a: MoodJournalEntity, b: MoodJournalEntity) = a.id == b.id
            override fun areContentsTheSame(a: MoodJournalEntity, b: MoodJournalEntity) = a == b
        }
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvJournalDate)
        val tvMood: TextView = view.findViewById(R.id.tvJournalMood)
        val tvContent: TextView = view.findViewById(R.id.tvJournalContent)
        val tvTags: TextView = view.findViewById(R.id.tvJournalTags)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_journal_entry, parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val entry = getItem(position)
        holder.tvDate.text = entry.entryDate
        holder.tvMood.text = moodEmoji(entry.moodScore)
        holder.tvContent.text = entry.content ?: ""
        holder.tvTags.text = entry.tags?.split(",")?.joinToString(" ") { "#${it.trim()}" } ?: ""
        holder.tvTags.visibility = if (entry.tags.isNullOrBlank()) View.GONE else View.VISIBLE
    }

    private fun moodEmoji(score: Int?) = when (score) {
        1 -> "😢"; 2 -> "😕"; 3 -> "😐"; 4 -> "🙂"; 5 -> "😄"; else -> ""
    }
}
