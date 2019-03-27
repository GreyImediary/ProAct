package com.proact.poject.serku.proact.di

import com.google.gson.Gson
import com.proact.poject.serku.proact.api.ProjectApi
import com.proact.poject.serku.proact.api.RequestsApi
import com.proact.poject.serku.proact.api.UserApi
import com.proact.poject.serku.proact.repositories.UserRepository
import com.proact.poject.serku.proact.di.ApiProperties.BASE_URL
import com.proact.poject.serku.proact.repositories.ProjectRepository
import com.proact.poject.serku.proact.repositories.RequestRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import org.koin.experimental.builder.create
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiProperties {
    const val BASE_URL = "BASE_URL"
}

fun createGson() = Gson()

fun createBaseUrl() = "http://new.std-247.ist.mospolytech.ru/api/"

fun createLogging() = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

fun createClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

fun createGsonConverter(): GsonConverterFactory = GsonConverterFactory.create()

fun createRxJava2CallAdapter(): RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

inline fun <reified T> createApi(
    baseUrl: String,
    client: OkHttpClient,
    converterFactory: GsonConverterFactory,
    rxJava2CallAdapterFactory: RxJava2CallAdapterFactory
): T =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(converterFactory)
        .addCallAdapterFactory(rxJava2CallAdapterFactory).build().create(T::class.java)

val baseApiModule = module {
    single(BASE_URL) { createBaseUrl() }
    single { createGson() }
    single { createLogging() }
    single { createClient(get()) }
    single { createGsonConverter() }
    single { createRxJava2CallAdapter() }
}

val userApiModule = module {
    single { createApi<UserApi>(get(BASE_URL), get(), get(), get()) }
    factory { create<UserRepository>() }
}

val projectApiModule = module {
    single { createApi<ProjectApi>(get(BASE_URL), get(), get(), get()) }
    factory { create<ProjectRepository>() }
}

val requestApiModule = module {
    single { createApi<RequestsApi>(get(BASE_URL), get(), get(), get()) }
    factory { create<RequestRepository>() }
}

