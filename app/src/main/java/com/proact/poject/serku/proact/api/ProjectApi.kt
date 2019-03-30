package com.proact.poject.serku.proact.api

import com.proact.poject.serku.proact.AnyMap
import com.proact.poject.serku.proact.data.Response
import io.reactivex.Observable
import retrofit2.http.*

interface ProjectApi {
    @GET("projects/get.php")
    fun getPojectById(@Query("id") id: Int): Observable<AnyMap>

    @GET("projects/get.php")
    fun getProjectsByStatus(@Query("status") status: Int,
                            @Query("per_page") perPgae: Int,
                            @Query("page") page: Double): Observable<AnyMap>

    @GET("projects/get.php")
    fun getUserProjects(@Query("user") userId: Int): Observable<AnyMap>


    @POST("projects/create.php")
    @FormUrlEncoded
    fun createProject(
        @Field("title") title: String,
        @Field("description") description: String,
        @Field("deadline") deadline: String,
        @Field("curator") curatorId: Int,
        @Field("members") members: String,
        @Field("tags") tags: String
    ): Observable<Response>

    @POST("projects/updateStatus.php")
    @FormUrlEncoded
    fun updateStatus(@Field("api_key") apiKey: String = "android"): Observable<Response>

}