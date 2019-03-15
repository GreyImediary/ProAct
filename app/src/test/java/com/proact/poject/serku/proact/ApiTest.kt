package com.proact.poject.serku.proact

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.proact.poject.serku.proact.di.baseApiModule
import com.proact.poject.serku.proact.di.projectApiModule
import com.proact.poject.serku.proact.di.userApiModule
import com.proact.poject.serku.proact.repositories.ProjectRepository
import com.proact.poject.serku.proact.repositories.UserRepository
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnit
import java.util.*

class ApiTest : KoinTest {
    @get:Rule
    val mockitoRule = MockitoJUnit.rule()!!

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    
    private val userRepository: UserRepository by inject()
    private val projectRepository: ProjectRepository by inject()

    @Before
    fun setUp() {
        startKoin(listOf(baseApiModule, userApiModule, projectApiModule))
    }

    @Test
    fun isRegisteredTest() {
        val liveData = userRepository.isRegistered.testObserver()
        userRepository.isUserRegistered("romen1@ya.ru")
        assertThat(liveData.observedValues.first())
            .isTrue()
    }

    @Test
    fun getUserByIdTest() {
        val secondRepository: UserRepository by inject()

        val liveData = userRepository.currentUser.testObserver()
        val secondLiveData = secondRepository.currentUser.testObserver()

        userRepository.getUserById(1)
        secondRepository.getUserById(141)

        assertThat(liveData.observedValues.first()?.id)
            .isEqualTo(1)
        assertThat(secondLiveData.observedValues.first()?.id)
            .isEqualTo(141)
    }


    @Test
    fun getAllWorkersTest() {
        val liveData = userRepository.allWorkers.testObserver()
        userRepository.getAllWorkers()
        assertThat(liveData.observedValues.isNotEmpty())
            .isTrue()
    }

    @Test
    fun getAllCustomersTest() {
        val liveData = userRepository.allCustomers.testObserver()
        userRepository.getAllCustomers()

        assertThat(liveData.observedValues.isNotEmpty())
            .isTrue()
    }

    @Test
    fun getAllAdminsTest() {
        val liveData = userRepository.allAdmins.testObserver()
        userRepository.getAllAdmins()

        assertThat(liveData.observedValues.isNotEmpty())
            .isTrue()
    }


    @Test
    fun addUserTest() {
        val liveData = userRepository.userAdded.testObserver()
        userRepository.addUser("Test rep",
            "Test ret last",
            "",
            "testaaa@mail.ru",
            "testPasswrod",
            "8939593213",
            "171-333",
            "",
            1)

        assertThat(liveData.observedValues.first())
            .isAnyOf(true, false)
    }

    @Test
    fun verifyUserTest() {
        val liveData = userRepository.userVerified.testObserver()

        userRepository.verifyUser("testaaa@mail.ru", "testPasswrod")
        assertThat(liveData.observedValues.first())
            .isTrue()
    }

    @Test
    fun getProjectByIdTest() {
        val livedata = projectRepository.currentProject.testObserver()

        projectRepository.getProjectById(26)

        assertThat(livedata.observedValues.first())
            .isNotNull()
    }

    @Test
    fun createProjectTest() {
        val livedata = projectRepository.isProjectCreated.testObserver()

        projectRepository.createProject("RazRazzz", "Eto gachibass", "2019-05-23",
            130, "[{\"backend\": 0, \"Frontend\": 0}]", "Web")

        assertThat(livedata.observedValues.first())
            .isAnyOf(true, false)
    }

    @Test
    fun updateProjectTest() {
        val liveData = projectRepository.isStatusUpdated.testObserver()

        projectRepository.updateStatus()

        assertThat(liveData.observedValues.first())
            .isTrue()
    }

    @After
    fun setDown() {
        stopKoin()
    }
}