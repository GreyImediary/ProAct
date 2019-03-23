package com.proact.poject.serku.proact.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("surname")
    @Expose
    val surname: String,

    @SerializedName("middle_name")
    @Expose
    val middleName: String?,

    @SerializedName("email")
    @Expose
    val email: String,

    @SerializedName("phone")
    @Expose
    val phone: String?,

    @SerializedName("stdgroup")
    @Expose
    val studentGroup: String?,

    @SerializedName("description")
    @Expose
    val description: String?,

    @SerializedName("avatar")
    @Expose
    val avatarUrl: String?,

    @SerializedName("usergroup")
    @Expose
    val userGroup: Int,

    @SerializedName("active_projects")
    @Expose
    val ActiveProject: String? = null,

    @SerializedName("finished_projects")
    @Expose
    val finishedProjects: String? = null
)