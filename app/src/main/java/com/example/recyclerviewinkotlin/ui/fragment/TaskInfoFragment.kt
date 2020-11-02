package com.example.recyclerviewinkotlin.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.recyclerviewinkotlin.R
import com.example.recyclerviewinkotlin.databinding.FragmentTaskInfoBinding
import com.example.recyclerviewinkotlin.db.userdata.Task

class TaskInfoFragment() : Fragment() {
    lateinit var itemTask: Task
    lateinit var fragmentTaskInfoBinding: FragmentTaskInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            itemTask = it.getParcelable("my_task")!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Call the static inflate() method included in the generated binding class.
        // This creates an instance of the binding class for the fragment to use
        fragmentTaskInfoBinding =  FragmentTaskInfoBinding.inflate(inflater, container, false)

        // Get a reference to the root view and return
        return fragmentTaskInfoBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // use dataBinding to set the views
        fragmentTaskInfoBinding.itemTask = itemTask
    }

    companion object {
        private const val MY_TASK = "my_task"

        @JvmStatic
        fun newInstance(task: Task): TaskInfoFragment{
            val fragment = TaskInfoFragment()
            var  args = Bundle()
            args.putSerializable(MY_TASK, task)
            fragment.arguments = args
            return fragment
        }
    }
}