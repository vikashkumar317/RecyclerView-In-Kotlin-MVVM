package com.example.recyclerviewinkotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclerviewinkotlin.db.repository.RouteRepository
import com.example.recyclerviewinkotlin.db.userdata.GoogleMapData
import com.google.android.gms.maps.model.LatLng
import org.json.JSONObject
import java.util.*

class RouteViewModel: ViewModel() {

    fun getRouteData(url: String): MutableLiveData<GoogleMapData> {
        return RouteRepository.getInstance().getRouteData(url)
    }
}