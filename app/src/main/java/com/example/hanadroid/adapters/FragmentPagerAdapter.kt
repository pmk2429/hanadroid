package com.example.hanadroid.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hanadroid.ui.fragments.FirstTabFragment
import com.example.hanadroid.ui.fragments.SecondTabFragment
import com.example.hanadroid.ui.fragments.ThirdTabFragment

class FragmentAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FirstTabFragment.newInstance()
            1 -> SecondTabFragment.newInstance()
            2 -> ThirdTabFragment.newInstance()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
