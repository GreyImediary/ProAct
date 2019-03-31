package com.proact.poject.serku.proact.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Request(
    val id: Int,
    val wokerId: Int,
    val workerName: String,
    val projectId: Int,
    val projectTitle: String,
    val teamNumber: Int,
    val projectRole: String,
    val requestStatus: Int,
    val comment: String
)