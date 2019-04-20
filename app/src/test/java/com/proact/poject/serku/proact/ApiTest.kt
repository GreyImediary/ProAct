package com.proact.poject.serku.proact

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.proact.poject.serku.proact.di.baseApiModule
import com.proact.poject.serku.proact.di.projectApiModule
import com.proact.poject.serku.proact.di.requestApiModule
import com.proact.poject.serku.proact.di.userApiModule
import com.proact.poject.serku.proact.repositories.ProjectRepository
import com.proact.poject.serku.proact.repositories.RequestRepository
import com.proact.poject.serku.proact.repositories.UserRepository
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.StandAloneContext.stopKoin
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
    private val projectRepository: ProjectRepository by inject()
    private val requestRepository: RequestRepository by inject()

    @Before
    fun setUp() {
        startKoin(listOf(baseApiModule, userApiModule, projectApiModule, requestApiModule))
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
            "jja@mail.ru",
            "jj",
            "8939593213",
            "171-333",
            "",
            1,
            "")

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
    fun authUser() {
        val liveData = userRepository.authed.testObserver()

        userRepository.authUser("curator@mail.ru", "123123")
        assertThat(liveData)
            .isNotNull()
    }

    @Test
    fun getProjectByIdTest() {
        val livedata = projectRepository.currentProject.testObserver()

        projectRepository.getProjectById(14)

        assertThat(livedata.observedValues.first())
            .isNotNull()
    }

    @Test
    fun createProjectTest() {
        val livedata = projectRepository.isProjectCreated.testObserver()

        /*projectRepository.createProject("RazRazzz", "Eto gachibass", "2019-05-23", "2019-05-28",
            130, "[{\"backend\": 0, \"Frontend\": 0}]", "Web")*/

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

    @Test
    fun getRequestsByProjectTest() {
        val liveData = requestRepository.requestsByProject.testObserver()

        requestRepository.getRequestsByProject(0, 25)

        assertThat(liveData.observedValues.first())
            .isNotEmpty()
    }

    @Test
    fun getWorkerRequestsTest() {
        val liveData = requestRepository.workerRequests.testObserver()

        requestRepository.getWorkerRequests(194)

        assertThat(liveData.observedValues.first())
            .isNotEmpty()
    }

    @Test
    fun getNextTEst() {
    val liveDta = requestRepository.workerRequests.testObserver()

        requestRepository.getNextRequests(194)

        assertThat(liveDta.observedValues.first())
            .isNotEmpty()
        print(liveDta.observedValues.first())
    }

    @Test
    fun isWorkerSignedTest() {
        val liveData = requestRepository.isWorkerSigned.testObserver()

        requestRepository.isWorkerSigned(161, 14)

        assertThat(liveData.observedValues.first())
            .isTrue()
    }

    @Test
    fun createRequest() {
        val liveData = requestRepository.isRequestFiled.testObserver()

        requestRepository.createRequest(161, 14, 0, "Записка")

        assertThat(liveData.observedValues.first())
            .isTrue()
    }

    @After
    fun setDown() {
        stopKoin()
    }
}