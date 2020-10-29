package com.example.recyclerviewinkotlin.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewinkotlin.databinding.ItemTaskBinding
import com.example.recyclerviewinkotlin.db.userdata.Task
import com.example.recyclerviewinkotlin.ui.fragment.HomeFragmentDirections
import com.example.recyclerviewinkotlin.ui.viewholder.TaskViewHolder
import com.example.recyclerviewinkotlin.viewmodel.TaskViewModel

class TaskAdapter (val taskViewModel: TaskViewModel, val v: View): RecyclerView.Adapter<TaskViewHolder>(){
    var taskList: List<List<Task>> = emptyList()

    // binding the data for the layout item_task.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ItemTaskBinding.inflate(inflater, parent, false)
        return TaskViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.setup(taskList[position][0])

        // upon clicking on the particular task, it navigate to TaskDetailFragment and also passes the detail info of that task
        holder.itemView.setOnClickListener{
            val action = HomeFragmentDirections.actionHomeFragmentToTaskDetailFragment(taskList[position][0])
            v.findNavController().navigate(action)
        }
    }

    // return the size of taskList
    override fun getItemCount() = taskList.size

    // to update the list inside our adapter
    fun updateTaskList(taskList: List<List<Task>>){
        this.taskList = taskList
        notifyDataSetChanged()
    }
}