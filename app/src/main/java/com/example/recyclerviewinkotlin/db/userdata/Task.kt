package com.example.recyclerviewinkotlin.db.userdata

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Task (val fleet_id: Int, val job_address: String, val job_time: String): Parcelable