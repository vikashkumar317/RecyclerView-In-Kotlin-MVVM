package com.example.recyclerviewinkotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclerviewinkotlin.db.repository.TaskRepository
import com.example.recyclerviewinkotlin.db.userdata.Data
import com.example.recyclerviewinkotlin.db.userdata.RequestBody

class TaskViewModel: ViewModel() {
    var taskData = MutableLiveData<Data>()

    fun getTask(requestBody: RequestBody){
        taskData = TaskRepository.getInstance().getTask(requestBody)
    }

}