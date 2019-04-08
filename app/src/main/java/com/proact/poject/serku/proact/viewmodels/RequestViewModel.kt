package com.proact.poject.serku.proact.viewmodels

import androidx.lifecycle.ViewModel
import com.proact.poject.serku.proact.repositories.RequestRepository

class RequestViewModel(private val requestRepository: RequestRepository) : ViewModel() {
    val isWorkerSigned = requestRepository.isWorkerSigned
    val isRequestFiled = requestRepository.isRequestFiled
    val workerRequests = requestRepository.workerRequests
    val requestsByProject = requestRepository.requestsByProject

    fun createRequest(
        workerId: Int,
        projectId: Int,
        teamNumber: Int,
        role: String
    ) = requestRepository.createRequest(workerId, projectId, teamNumber, role)

    fun isWorkerSigned(workerId: Int, projectId: Int) =
        requestRepository.isWorkerSigned(workerId, projectId)

    fun getWorkerRequests(workerId: Int) = requestRepository.getWorkerRequests(workerId)

    fun getRequestsByProject(requestStatus: Int, projectId: Int) =
        requestRepository.getRequestsByProject(requestStatus, projectId)

    fun getNextRequests(workerId: Int) = requestRepository.getNextRequests(workerId)

    override fun onCleared() {
        requestRepository.clearDisposable()
        super.onCleared()
    }
}