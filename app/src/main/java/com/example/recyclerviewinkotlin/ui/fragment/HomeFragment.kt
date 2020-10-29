package com.example.recyclerviewinkotlin.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerviewinkotlin.R
import com.example.recyclerviewinkotlin.databinding.FragmentHomeBinding
import com.example.recyclerviewinkotlin.db.userdata.RequestBody
import com.example.recyclerviewinkotlin.db.userdata.Task
import com.example.recyclerviewinkotlin.ui.adapter.TaskAdapter
import com.example.recyclerviewinkotlin.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    lateinit var fragmentHomeBinding: FragmentHomeBinding
    lateinit var adapter: TaskAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Initialising data binding for our fragment class together with the viewmodel object
        // which lies inside the layout as a variable of data binding object.
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            viewmodel = ViewModelProvider(this@HomeFragment).get(TaskViewModel::class.java)
            setLifecycleOwner(viewLifecycleOwner)
        }

        // Get a reference to the root view and return
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // create request body instance
        var requestbody = RequestBody("5b6262c2a80b5f021c162e3b1d17244f1decc4fb22c10b22591a02c2c9fb","2020-10-28", "v2", "home_refresh_swipe")

        // call getTask() method of TaskViewModel
        fragmentHomeBinding.viewmodel?.getTask(requestbody)

        if (fragmentHomeBinding.viewmodel != null) {
            // make adapter instance
            adapter = TaskAdapter(fragmentHomeBinding.viewmodel!!, view)

            // Align tasks vertically
            rvTask.layoutManager = LinearLayoutManager(activity)

            // seprate each task by a line
            rvTask.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager(activity).orientation))
            rvTask.adapter = adapter
        }

        // notify adapter upon detecting any update in task list
        fragmentHomeBinding.viewmodel?.taskData?.observe(viewLifecycleOwner, Observer {
            adapter.updateTaskList(it.tasks)
        })
    }
}