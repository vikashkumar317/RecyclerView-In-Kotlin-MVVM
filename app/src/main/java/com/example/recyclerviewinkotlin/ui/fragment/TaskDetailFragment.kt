package com.example.recyclerviewinkotlin.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.recyclerviewinkotlin.R
import com.example.recyclerviewinkotlin.databinding.FragmentTaskDetailBinding
import com.example.recyclerviewinkotlin.db.userdata.Task

class TaskDetailFragment : Fragment() {
    lateinit var fragmentTaskDetailBinding: FragmentTaskDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Call the static inflate() method included in the generated binding class.
        // This creates an instance of the binding class for the fragment to use
        fragmentTaskDetailBinding = FragmentTaskDetailBinding.inflate(inflater, container, false)

        // Get a reference to the root view and return
        return fragmentTaskDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // use the by navArgs() property delegate to access arguments.
        val args: TaskDetailFragmentArgs by navArgs()

        // use dataBinding to set the views
        fragmentTaskDetailBinding.itemTask = args.itemTask
    }
}