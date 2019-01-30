package com.proact.poject.serku.proact

import com.proact.poject.serku.proact.di.baseApiModule
import com.proact.poject.serku.proact.di.projectApiModule
import com.proact.poject.serku.proact.di.userApiModule
import com.proact.poject.serku.proact.di.viewModelsModule
import com.proact.poject.serku.proact.viewmodels.ProjectViewModel
import com.proact.poject.serku.proact.viewmodels.UserViewModel
import org.junit.Before
import org.junit.Test
import org.koin.standalone.StandAloneContext
import org.koin.standalone.inject
import org.koin.test.KoinTest
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.koin.standalone.StandAloneContext.stopKoin

class ViewModelsTest : KoinTest {
    private val userViewModel: UserViewModel by inject()
    private val projectViewModel: ProjectViewModel by inject()

    @Before
    fun setUp() {
        StandAloneContext.startKoin(listOf(baseApiModule, userApiModule, projectApiModule, viewModelsModule))
    }

    @Test
    fun testViewModel() {
        assertThat(userViewModel)
            .isNotNull()

        assertThat(projectViewModel)
            .isNotNull()
    }

    @After
    fun setDown() {
        stopKoin()
    }
}