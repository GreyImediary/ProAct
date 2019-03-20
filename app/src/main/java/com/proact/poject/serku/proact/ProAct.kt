package com.proact.poject.serku.proact

import android.app.Application
import com.proact.poject.serku.proact.di.*
import org.koin.android.ext.android.startKoin

class ProAct : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(
            baseApiModule,
            userApiModule,
            projectApiModule,
            viewModelsModule,
            requestApiModule))
    }
}