package com.proact.poject.serku.proact.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("message")
    @Expose
    val message: String
)