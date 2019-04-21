package com.proact.poject.serku.proact.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Tag(

    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("category")
    @Expose
    val category: String,

    @SerializedName("value")
    @Expose
    val value: String
)