package com.example.recyclerviewinkotlin.db.userdata

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Task (val fleet_id: Int, val job_address: String, val job_time: String, val job_latitude: String, val job_longitude: String): Parcelable,
    Serializable