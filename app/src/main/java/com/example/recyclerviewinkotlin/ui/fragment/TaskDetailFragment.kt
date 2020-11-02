package com.example.recyclerviewinkotlin.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.recyclerviewinkotlin.R
import com.example.recyclerviewinkotlin.ui.adapter.TaskTabLayoutAdapter
import com.google.android.material.tabs.TabLayout

class TaskDetailFragment : Fragment() {
    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // use the by navArgs() property delegate to access arguments.
        val args: TaskDetailFragmentArgs by navArgs()
        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.viewPager)

        // add Adapter to viewpager
        viewPager!!.adapter = TaskTabLayoutAdapter(childFragmentManager, args.itemTask)

        // set viewPager with tabLayout
        tabLayout!!.setupWithViewPager(viewPager)
    }
}