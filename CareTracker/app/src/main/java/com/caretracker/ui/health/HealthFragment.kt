package com.caretracker.ui.health

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.caretracker.databinding.FragmentHealthBinding
import com.caretracker.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HealthFragment : Fragment() {

    private var _binding: FragmentHealthBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HealthViewModel by viewModels()
    private lateinit var adapter: HealthEntryAdapter

    @Inject lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHealthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val todayLabel = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(Date())
        binding.tvTodayDate.text = todayLabel

        adapter = HealthEntryAdapter(
            onEdit   = { entry -> openLog(entry.id) },
            onDelete = { entry -> viewModel.delete(entry) }
        )
        binding.rvEntryHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEntryHistory.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.entries.collectLatest { entries ->
                adapter.submitList(entries)

                // Today's readings
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val todayEntry = entries.firstOrNull { it.date == today }

                // Blood Sugar card
                if (todayEntry?.bloodSugar != null) {
                    binding.tvBsTodayValue.text = "${todayEntry.bloodSugar} mg/dL"
                    binding.tvBsNoReadings.visibility = View.GONE
                } else {
                    binding.tvBsTodayValue.text = ""
                    binding.tvBsNoReadings.visibility = View.VISIBLE
                }

                // Blood Pressure card
                if (todayEntry?.bpSystolic != null && todayEntry.bpDiastolic != null) {
                    binding.tvBpTodayValue.text = "${todayEntry.bpSystolic}/${todayEntry.bpDiastolic}"
                    binding.tvBpNoReadings.visibility = View.GONE
                } else {
                    binding.tvBpTodayValue.text = ""
                    binding.tvBpNoReadings.visibility = View.VISIBLE
                }

                // Insights strip
                updateInsights(entries)

                binding.tvEmptyHistory.visibility =
                    if (entries.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        binding.btnAddBsReading.setOnClickListener { openLog(0L) }
        binding.btnAddBpReading.setOnClickListener { openLog(0L) }
        binding.fabAddHealth.setOnClickListener   { openLog(0L) }
    }

    private fun openLog(entryId: Long) {
        startActivity(
            Intent(requireContext(), HealthLogActivity::class.java)
                .putExtra("ENTRY_ID", entryId)
        )
    }

    private fun updateInsights(entries: List<com.caretracker.data.models.DailyHealthEntry>) {
        if (entries.isEmpty()) {
            binding.layoutInsights.visibility = View.GONE
            return
        }
        binding.layoutInsights.visibility = View.VISIBLE
        val recent = entries.take(7)
        val insights = mutableListOf<String>()

        // Mood insight
        val avgMood = recent.mapNotNull { it.mood }.average()
        if (!avgMood.isNaN()) {
            if (avgMood >= 7) insights.add("😊 Your mood has been great this week — keep up whatever you're doing!")
            else if (avgMood <= 4) insights.add("⚠️ Your mood has been low lately. Consider talking to someone you trust.")
        }

        // Sleep insight
        val avgSleep = recent.mapNotNull { it.sleepHours }.average()
        if (!avgSleep.isNaN() && avgSleep < 7) {
            insights.add("⚠️ You've averaged ${"%.1f".format(avgSleep)} hours of sleep. Prioritizing rest can drastically improve your health.")
        }

        // BP insight
        val highBp = recent.firstOrNull { (it.bpSystolic ?: 0) > 130 }
        if (highBp != null) {
            insights.add("🚨 Elevated blood pressure detected on ${highBp.date}. Consult your doctor if this persists.")
        }

        // Steps insight
        val avgSteps = recent.mapNotNull { it.steps }.average()
        if (!avgSteps.isNaN() && avgSteps >= 8000) {
            insights.add("🏃 You're hitting your step goals! Great work staying active.")
        }

        binding.tvInsight1.text = insights.getOrElse(0) { "💡 Log health data daily to unlock personalized insights." }
        binding.tvInsight2.text = insights.getOrElse(1) { "" }
        binding.tvInsight2.visibility = if (insights.size > 1) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
