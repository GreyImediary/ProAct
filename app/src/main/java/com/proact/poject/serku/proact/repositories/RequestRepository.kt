package com.proact.poject.serku.proact.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.proact.poject.serku.proact.AnyMap
import com.proact.poject.serku.proact.api.RequestsApi
import com.proact.poject.serku.proact.data.Request
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class RequestRepository(private val requestsApi: RequestsApi) {
    val isWorkerSigned = MutableLiveData<Boolean>()
    val isRequestFiled = MutableLiveData<Boolean>()
    val isStatusUpdated = MutableLiveData<Boolean>()
    val workerRequests = MutableLiveData<MutableList<Request>>()
    val requestsByProject = MutableLiveData<List<Request>>()
    val loadingStatus = MutableLiveData<Boolean>()

    private val dispodable = CompositeDisposable()

    private var page = 1.0
    private var allPages = 0.0
    private val perPage = 3


    fun createRequest(
        workerId: Int,
        projectId: Int,
        teamNumber: Int,
        role: String
    ) {
        val subscription = requestsApi.createRequest(workerId, projectId, teamNumber, role)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { isRequestFiled.postValue(it.message == "true") },
                onError = { Log.e("RR-createRequest", it.message) }
            )

        dispodable.add(subscription)
    }

    fun isWorkerSigned(workerId: Int, projectId: Int) {
        val subscription = requestsApi.isWorkerSigned(workerId, projectId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { isWorkerSigned.postValue(it.message == "true") },
                onError = { Log.e("RR-isWorkerSigned", it.message) }
            )

        dispodable.add(subscription)
    }

    fun updateRequestStatus(requestId: Int, status: Int) {
        val subscription = requestsApi.updateRequestStatus(requestId, status)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { isStatusUpdated.postValue(it.message == "true") },
                onError = { Log.e("RR-uodateRequestStatus", it.message) }
            )

        dispodable.add(subscription)
    }

    fun getWorkerRequests(workerId: Int) {
        loadingStatus.postValue(true)
        val subscription = requestsApi.getWorkerRequests(workerId, page, perPage)
            .subscribeOn(Schedulers.io())
            .map {
                allPages = it["pages"] as Double
                page = it["page"] as Double

                var requestList = emptyList<AnyMap>()

                if (it["data"] != null) {
                    requestList = it["data"] as List<AnyMap>
                }

                requestList.map { rowRequest -> getRequestFromMap(rowRequest) }
            }
            .doOnComplete { loadingStatus.postValue(false) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (workerRequests.value == null) {
                        workerRequests.postValue(it.toMutableList())
                    } else {
                        val list = workerRequests.value?.apply {
                            if (!containsAll(it)) {
                                addAll(it)
                            }
                        }
                        workerRequests.postValue(list)
                    }
                },
                onError = { Log.e("RR-getWorkerRequests", it.message) }
            )

        dispodable.add(subscription)
    }

    fun getRequestsByProject(requestStatus: Int, projectId: Int) {
        val subscription = requestsApi.getRequestsByProject(requestStatus, projectId)
            .subscribeOn(Schedulers.io())
            .map {
                it.map { requestMap -> getRequestFromMap(requestMap) }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { requestsByProject.postValue(it) },
                onError = {
                    Log.e("RR-getRequestsByProject", it.message)
                    requestsByProject.postValue(emptyList())
                }
            )

        dispodable.add(subscription)
    }

    fun getNextRequests(workerId: Int) {
        if (page != allPages) {
            page += 1
            getWorkerRequests(workerId)
        }
    }

    private fun getRequestFromMap(request: AnyMap): Request {
        val id = (request["id"] as String).toInt()

        var workerId = -1
        var workerName = ""

        if (request["worker_id"] !is String) {
            val worker = request["worker_id"] as AnyMap

            workerId = (worker["id"] as String).toInt()
            workerName = "${worker["surname"] as String} ${worker["name"] as String}"
        }

        var projectId = -1
        var projectTitle = ""

        if (request["project_id"] !is String) {
            val project = request["project_id"] as AnyMap

            projectId = (project["id"] as String).toInt()
            projectTitle = project["title"] as String
        }

        val team = (request["team"] as String).toInt() + 1
        val role = request["role"] as String
        val status = (request["status"] as String).toInt()
        val comment = if (request["comment"] != null) request["comment"] as String else ""

        return Request(
            id,
            workerId,
            workerName,
            projectId,
            projectTitle,
            team,
            role,
            status,
            comment)

    }

    fun clearDisposable() {
        dispodable.dispose()
    }
}