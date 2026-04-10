package com.caretracker.ui.carecircle

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.caretracker.data.models.Doctor
import com.caretracker.databinding.ItemDoctorBinding

class DoctorAdapter(
    private val onClick: (Doctor) -> Unit,
    private val onDelete: (Doctor) -> Unit
) : ListAdapter<Doctor, DoctorAdapter.DoctorViewHolder>(DiffCallback) {

    inner class DoctorViewHolder(private val binding: ItemDoctorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(doctor: Doctor) {
            binding.tvDoctorName.text = doctor.name
            binding.tvDoctorSpecialty.text = doctor.specialty.ifEmpty { "General Practice" }
            binding.tvDoctorPhone.text = doctor.phone.ifEmpty { "No phone on file" }

            // Short tap — edit
            binding.root.setOnClickListener { onClick(doctor) }

            // Long press — Edit / Delete menu
            binding.root.setOnLongClickListener { view ->
                showMenu(view.context, doctor)
                true
            }
        }
    }

    private fun showMenu(context: Context, doctor: Doctor) {
        AlertDialog.Builder(context)
            .setTitle(doctor.name)
            .setItems(arrayOf("\u270F\uFE0F  Edit", "\uD83D\uDDD1\uFE0F  Delete")) { _, which ->
                when (which) {
                    0 -> onClick(doctor)
                    1 -> AlertDialog.Builder(context)
                        .setTitle("Delete \"${doctor.name}\"?")
                        .setMessage("This will permanently delete this doctor and remove all patient links. This cannot be undone.")
                        .setPositiveButton("Delete") { _, _ -> onDelete(doctor) }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
            }
            .show()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DoctorViewHolder(
            ItemDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object DiffCallback : DiffUtil.ItemCallback<Doctor>() {
        override fun areItemsTheSame(a: Doctor, b: Doctor) = a.id == b.id
        override fun areContentsTheSame(a: Doctor, b: Doctor) = a == b
    }
}
