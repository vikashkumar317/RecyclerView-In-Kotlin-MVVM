package com.example.recyclerviewinkotlin.db.repository

import androidx.lifecycle.MutableLiveData
import com.example.recyclerviewinkotlin.db.network.MyApi
import com.example.recyclerviewinkotlin.db.network.MyGoogleApi
import com.example.recyclerviewinkotlin.db.userdata.Data
import com.example.recyclerviewinkotlin.db.userdata.GoogleMapData
import com.example.recyclerviewinkotlin.db.userdata.RequestBody
import com.example.recyclerviewinkotlin.db.userdata.ResponseData
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RouteRepository {

    // LiveData is of abstract type and hence create instance of MutableLiveData
    fun getRouteData(url: String): MutableLiveData<GoogleMapData> {
        var routeData =  MutableLiveData<GoogleMapData>()

        // call getRouteData() method
        MyGoogleApi().getRouteData(url).enqueue(object: Callback<GoogleMapData> {

                override fun onResponse(call: Call<GoogleMapData>, response: Response<GoogleMapData>) {
                    routeData.value = response.body()!!
                }

                override fun onFailure(call: Call<GoogleMapData>, t: Throwable) { }
            })

        return  routeData;
    }

    // getInstance return instance of RouteRepository
    companion object {
        private var INSTANCE: RouteRepository? = null

        fun getInstance() = INSTANCE ?: RouteRepository().also { INSTANCE = it }
    }

}