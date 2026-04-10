package com.caretracker

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.caretracker.databinding.ActivityMainBinding
import com.caretracker.ui.calendar.CalendarFragment
import com.caretracker.ui.carecircle.CareCircleFragment
import com.caretracker.ui.dashboard.DashboardFragment
import com.caretracker.ui.habits.HabitsFragment
import com.caretracker.ui.health.HealthFragment
import com.caretracker.ui.medications.MedicationsFragment
import com.caretracker.ui.reports.ReportsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.nav_open, R.string.nav_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            replaceFragment(DashboardFragment(), "Dashboard")
            binding.navView.setCheckedItem(R.id.nav_dashboard)
        }

        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard   -> replaceFragment(DashboardFragment(),   "Dashboard")
                R.id.nav_habits      -> replaceFragment(HabitsFragment(),      "Habits")
                R.id.nav_health      -> replaceFragment(HealthFragment(),      "Health")
                R.id.nav_medications -> replaceFragment(MedicationsFragment(), "Medications")
                R.id.nav_calendar    -> replaceFragment(CalendarFragment(),    "Calendar")
                R.id.nav_care_circle -> replaceFragment(CareCircleFragment(),  "Care Circle")
                R.id.nav_reports     -> replaceFragment(ReportsFragment(),     "Reports")
                else                 -> replaceFragment(DashboardFragment(),   "Dashboard")
            }
            binding.drawerLayout.closeDrawers()
            true
        }
    }

    private fun replaceFragment(fragment: Fragment, pageTitle: String) {
        binding.tvPageTitle.text = pageTitle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
