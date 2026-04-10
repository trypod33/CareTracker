package com.caretracker.ui.carecircle

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class CareCirclePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2
    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> MembersFragment()
        1 -> DoctorsFragment()
        else -> MembersFragment()
    }
}
