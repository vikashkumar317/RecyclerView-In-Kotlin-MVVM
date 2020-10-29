package com.example.recyclerviewinkotlin.db.network

import com.example.recyclerviewinkotlin.db.userdata.RequestBody
import com.example.recyclerviewinkotlin.db.userdata.ResponseData
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MyApi {

    // POST request using retrofit
    @POST("view_tasks_for_date")
    fun getTask(@Body requestBody: RequestBody): Call<ResponseData>

    // now instance of MyApi can be created by MyApi() or MyApi.invoke()
    companion object{
        operator fun invoke() : MyApi {
            return Retrofit.Builder()
                .baseUrl("https://api.tookanapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }

}