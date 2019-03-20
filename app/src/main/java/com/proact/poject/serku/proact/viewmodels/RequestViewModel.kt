package com.proact.poject.serku.proact.viewmodels

import androidx.lifecycle.ViewModel
import com.proact.poject.serku.proact.repositories.RequestRepository

class RequestViewModel(private val requestRepository: RequestRepository) : ViewModel() {
    val isWorkerSigned = requestRepository.isWorkerSigned
    val isRequestFiled = requestRepository.isRequestFiled
    val workerequests = requestRepository.workerRequests
    val requestsByProject = requestRepository.requestsByProject

    fun createRequest(
        workerId: Int,
        projectId: Int,
        teamNumber: Int,
        role: String
    ) = requestRepository.createRequest(workerId, projectId, teamNumber, role)

    fun isWorkerSigned(workerId: Int, projectId: Int) =
        requestRepository.isWorkerSigned(workerId, projectId)

    fun getWorkerRequests(workerId: Int) = requestRepository.getWokerRequests(workerId)

    fun getRequestsByProject(requestStatus: Int, projectId: Int) =
        requestRepository.getRequestsByProject(requestStatus, projectId)

    override fun onCleared() {
        requestRepository.clearDisposable()
        super.onCleared()
    }
}