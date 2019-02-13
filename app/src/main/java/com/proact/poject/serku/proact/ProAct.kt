package com.proact.poject.serku.proact

import android.app.Application
import com.proact.poject.serku.proact.di.baseApiModule
import com.proact.poject.serku.proact.di.projectApiModule
import com.proact.poject.serku.proact.di.userApiModule
import com.proact.poject.serku.proact.di.viewModelsModule
import org.koin.android.ext.android.startKoin

class ProAct : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(baseApiModule, userApiModule, projectApiModule, viewModelsModule))
    }
}