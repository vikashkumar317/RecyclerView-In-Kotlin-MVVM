package com.example.recyclerviewinkotlin.db.network

import com.example.recyclerviewinkotlin.db.userdata.GoogleMapData
import com.example.recyclerviewinkotlin.db.userdata.RequestBody
import com.example.recyclerviewinkotlin.db.userdata.ResponseData
import com.google.android.gms.maps.model.LatLng
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface MyGoogleApi {
    // GET request using retrofit
    @GET
    fun getRouteData(@Url url: String): Call<GoogleMapData>

    // now instance of MyGoogleApi can be created by MyGoogleApi() or MyGoogleApi.invoke()
    companion object{
        operator fun invoke() : MyGoogleApi {
            return Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/directions/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyGoogleApi::class.java)
        }
    }
}