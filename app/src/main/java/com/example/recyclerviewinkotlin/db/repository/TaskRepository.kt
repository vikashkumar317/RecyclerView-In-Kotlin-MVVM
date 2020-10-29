package com.example.recyclerviewinkotlin.db.repository

import androidx.lifecycle.MutableLiveData
import com.example.recyclerviewinkotlin.db.network.MyApi
import com.example.recyclerviewinkotlin.db.userdata.Data
import com.example.recyclerviewinkotlin.db.userdata.RequestBody
import com.example.recyclerviewinkotlin.db.userdata.ResponseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository {

    // LiveData is of abstract type and hence create instance of MutableLiveData
    fun getTask(requestBody: RequestBody): MutableLiveData<Data> {
        val taskData =  MutableLiveData<Data>()

        // call getTask() method
        MyApi().getTask(requestBody)
            .enqueue(object: Callback<ResponseData> {

                // On Success
                override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                    if(response.body()!!.status == 200)
                        taskData.value = response.body()!!.data
                }

                // On Failure
                override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                    // TO DO
                }
            })

        // return list of tasks
        return  taskData;
    }

    // getInstance return instance of TaskRepository
    companion object {
        private var INSTANCE: TaskRepository? = null

        fun getInstance() = INSTANCE ?: TaskRepository().also { INSTANCE = it }
    }

}