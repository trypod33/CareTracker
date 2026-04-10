package com.caretracker.ui.people

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.caretracker.data.models.Person
import com.caretracker.databinding.ItemPersonBinding

class PeopleAdapter(
    private val onClick: (Person) -> Unit,
    private val onDelete: (Person) -> Unit
) : ListAdapter<Person, PeopleAdapter.PersonViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        return PersonViewHolder(ItemPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = getItem(position)
        holder.binding.tvName.text = person.name
        holder.binding.tvRole.text = person.role
        holder.binding.tvAvatar.text = person.avatar

        // Short tap — edit
        holder.itemView.setOnClickListener { onClick(person) }

        // Long press — Edit / Delete menu
        holder.itemView.setOnLongClickListener { view ->
            AlertDialog.Builder(view.context)
                .setTitle(person.name)
                .setItems(arrayOf("\u270F\uFE0F  Edit", "\uD83D\uDDD1\uFE0F  Delete")) { _, which ->
                    when (which) {
                        0 -> onClick(person)
                        1 -> AlertDialog.Builder(view.context)
                            .setTitle("Delete \"${person.name}\"?")
                            .setMessage("This will permanently remove this person and all their associated data. This cannot be undone.")
                            .setPositiveButton("Delete") { _, _ -> onDelete(person) }
                            .setNegativeButton("Cancel", null)
                            .show()
                    }
                }
                .show()
            true
        }
    }

    class PersonViewHolder(val binding: ItemPersonBinding) : RecyclerView.ViewHolder(binding.root)

    companion object DiffCallback : DiffUtil.ItemCallback<Person>() {
        override fun areItemsTheSame(oldItem: Person, newItem: Person) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Person, newItem: Person) = oldItem == newItem
    }
}
