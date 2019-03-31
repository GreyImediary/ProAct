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
    val workerRequests = MutableLiveData<List<Request>>()
    val requestsByProject = MutableLiveData<List<Request>>()

    private val dispodable = CompositeDisposable()

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

    fun getWokerRequests(workerId: Int) {
        /*val subscription = requestsApi.getWorkerRequests(workerId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { workerRequests.postValue(it) },
                onError = { Log.e("RR-getWorkerRequests", it.message) }
            )

        dispodable.add(subscription)*/
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
                onError = { Log.e("RR-getRequestsByProject", it.message) }
            )

        dispodable.add(subscription)
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
        val comment = request["comment"] as String

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