package com.proact.poject.serku.proact.api

import com.proact.poject.serku.proact.data.Response
import com.proact.poject.serku.proact.data.Tag
import com.proact.poject.serku.proact.data.User
import io.reactivex.Observable
import retrofit2.http.*

interface UserApi {
    @GET("user/get.php")
    fun getUserById(@Query("id") id: Int): Observable<User>

    @GET("user/get.php")
    fun getUserByEmail(@Query("email") email: String): Observable<User>

    @GET("workers/get.php")
    fun getAllWorkers(): Observable<List<User>>

    @GET("curators/get.php")
    fun getAllCustomers(): Observable<List<User>>

    @GET("admins/get.php")
    fun getAllAdmins(): Observable<List<User>>

    @POST("user/isregistered.php")
    @FormUrlEncoded
    fun isUserRegistered(@Field("email") email: String): Observable<Response>

    @POST("user/verify.php")
    @FormUrlEncoded
    fun verifyUser(
        @Field("email") email: String,
        @Field("pass") password: String,
        @Field("api_key") apiKey: String = "android"
    ): Observable<Response>

    @POST("user/auth.php")
    @FormUrlEncoded
    fun authUser(
        @Field("email") email: String,
        @Field("pass") password: String
    ): Observable<String>

    @POST("user/add.php")
    @FormUrlEncoded
    fun addUser(
        @Field("name") name: String,
        @Field("surname") surname: String,
        @Field("middlename") middleName: String,
        @Field("email") email: String,
        @Field("pass") password: String,
        @Field("tel") phone: String,
        @Field("std_group") studentGroup: String,
        @Field("description") description: String,
        @Field("usergroup") userGroup: Int,
        @Field("avatar") avatar: String,
        @Field("api_key") apiKey: String = "android"
    ): Observable<Response>

    @GET("tags/")
    fun fetchTags(@Header("X-Auth-Token") token: String): Observable<List<Tag>>

}