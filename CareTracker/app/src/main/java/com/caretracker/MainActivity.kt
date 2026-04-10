package com.caretracker

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.caretracker.databinding.ActivityMainBinding
import com.caretracker.ui.calendar.CalendarFragment
import com.caretracker.ui.dashboard.DashboardFragment
import com.caretracker.ui.habits.HabitsFragment
import com.caretracker.ui.health.HealthFragment
import com.caretracker.ui.medications.MedicationsFragment
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

        if (savedInstanceState == null) replaceFragment(DashboardFragment())

        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.dashboard -> replaceFragment(DashboardFragment())
                R.id.calendar -> replaceFragment(CalendarFragment())
                R.id.medications -> replaceFragment(MedicationsFragment())
                R.id.health -> replaceFragment(HealthFragment())
                R.id.habits -> replaceFragment(HabitsFragment())
                else -> replaceFragment(DashboardFragment())
            }
            binding.drawerLayout.closeDrawers()
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
