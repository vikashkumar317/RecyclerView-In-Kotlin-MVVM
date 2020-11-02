package com.example.recyclerviewinkotlin.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.recyclerviewinkotlin.db.userdata.Task
import com.example.recyclerviewinkotlin.ui.fragment.MapFragment
import com.example.recyclerviewinkotlin.ui.fragment.TaskInfoFragment

class TaskTabLayoutAdapter(var fragmentManager: FragmentManager, var itemTask: Task): FragmentPagerAdapter(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    // return total count of tabs
    override fun getCount() = 2

    // return Fragment instance according to tab selected
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0    -> TaskInfoFragment.newInstance(itemTask)
            1    -> MapFragment.newInstance(itemTask)
            else -> TaskInfoFragment.newInstance(itemTask)
        }
    }

    // return title of tabs
    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0    -> "Task Info"
            1    -> "Map"
            else -> "Task Info"
        }
    }
}