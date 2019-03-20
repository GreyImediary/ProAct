package com.proact.poject.serku.proact.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Request(

    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("worker_id")
    @Expose
    val workerId: Int,

    @SerializedName("project_id")
    @Expose
    val projectId: Int,

    @SerializedName("team")
    @Expose
    val teamNumber: Int,

    @SerializedName("role")
    @Expose
    val projectRole: String,

    @SerializedName("status")
    @Expose
    val requestStatus: Int,

    @SerializedName("comment")
    @Expose
    val comment: String
)