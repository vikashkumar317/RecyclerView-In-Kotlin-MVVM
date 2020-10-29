package com.example.recyclerviewinkotlin.ui.viewholder

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewinkotlin.BR
import com.example.recyclerviewinkotlin.db.userdata.Task
import com.example.recyclerviewinkotlin.viewmodel.TaskViewModel

class TaskViewHolder (val dataBinding: ViewDataBinding): RecyclerView.ViewHolder(dataBinding.root){

    fun setup(itemTask: Task){
        // firstly we are setting the itemTask variable which is defined in the databinding scope of the item_task.xml layout,
        dataBinding.setVariable(BR.itemTask, itemTask)

        // then calling executePendingBindings over dataBinding object will evaluate the pending bindings,
        // updates any view that is expression bound to modified variables
        dataBinding.executePendingBindings()
    }
}