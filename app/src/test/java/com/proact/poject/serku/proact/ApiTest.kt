package com.proact.poject.serku.proact

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.proact.poject.serku.proact.di.baseApiModule
import com.proact.poject.serku.proact.di.projectApiModule
import com.proact.poject.serku.proact.di.userApiModule
import com.proact.poject.serku.proact.repositories.UserRepository
import org.junit.Rule
import org.junit.Test
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.mockito.junit.MockitoJUnit

class ApiTest : KoinTest {
    @get:Rule
    val mockitoRule = MockitoJUnit.rule()!!

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    
    private val userRepository: UserRepository by inject()

    @Test
    fun isRegisteredTest() {
        startKoin(listOf(baseApiModule, userApiModule, projectApiModule))

        val liveData = userRepository.isRegistered.testObserver()
        userRepository.isUserRegistered("romen1@ya.ru")
        assertThat(liveData.observedValues.first())
            .isEqualTo(true)
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
}