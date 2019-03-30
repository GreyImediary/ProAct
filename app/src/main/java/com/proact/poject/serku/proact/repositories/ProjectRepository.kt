package com.proact.poject.serku.proact.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.proact.poject.serku.proact.AnyMap
import com.proact.poject.serku.proact.api.ProjectApi
import com.proact.poject.serku.proact.api.UserApi
import com.proact.poject.serku.proact.data.MemberOfProject
import com.proact.poject.serku.proact.data.Project
import com.proact.poject.serku.proact.data.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.Calendar.*
import kotlin.collections.HashMap

class ProjectRepository(
    private val projectApi: ProjectApi,
    private val userApi: UserApi,
    private val gson: Gson
) {
    val currentProject = MutableLiveData<Project>()
    val isProjectCreated = MutableLiveData<Boolean>()
    val isStatusUpdated = MutableLiveData<Boolean>()
    val projects = MutableLiveData<MutableList<Project>>()
    val loadingStatus = MutableLiveData<Boolean>()
    val userProjects = MutableLiveData<HashMap<String, List<Project>>>()
    private var page = 1.0
    private var allPages = 0.0
    private val perPage = 3
    private val disposable = CompositeDisposable()

    fun createProject(
        title: String,
        description: String,
        deadlineDate: String,
        curatorId: Int,
        members: String,
        tags: String
    ) {
        val subsciption =
            projectApi.createProject(title, description, deadlineDate, curatorId, members, tags)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = { isProjectCreated.postValue(it.message == "true") },
                    onError = { Log.e("PR-createProject", it.message) }
                )
        disposable.add(subsciption)
    }

    fun updateStatus() {
        val subscription = projectApi.updateStatus()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { isStatusUpdated.postValue(it.message == "true") },
                onError = { Log.e("PR-updateStatus", it.message) }
            )

        disposable.add(subscription)
    }

    fun getProjectById(id: Int) {
        loadingStatus.postValue(true)
        val subscription = projectApi.getPojectById(id)
            .subscribeOn(Schedulers.io())
            .map {
                getProjectFromMap(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { loadingStatus.postValue(false) }
            .subscribeBy(
                onError = { Log.e("PR-getProjectById", it.message) },
                onNext = {
                    currentProject.postValue(it)
                }
            )
        disposable.add(subscription)
    }

    fun getProjectByStatus(status: Int) {
        loadingStatus.postValue(true)
        val subscription = projectApi.getProjectsByStatus(status, perPage, page)
            .subscribeOn(Schedulers.io())
            .map {
                allPages = it["pages"] as Double
                page = it["page"] as Double

                var projectList = emptyList<AnyMap>()

                if (it["data"] != null) {
                    projectList = it["data"] as List<AnyMap>
                }

                projectList.map { rowProject -> getProjectFromMap(rowProject) }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { loadingStatus.postValue(false) }
            .subscribeBy(
                onError = { Log.e("PR-getProjectByStatus", it.message) },
                onNext = {
                    if (projects.value == null) {
                        projects.postValue(it as MutableList<Project>)
                    } else {
                        val list = projects.value?.apply {
                            addAll(it)
                        }

                        projects.postValue(list)
                    }
                }
            )

        disposable.add(subscription)
    }

    fun getUsersProject(userId: Int) {
        val subscription = projectApi.getUserProjects(userId)
            .subscribeOn(Schedulers.io())
            .map {
                var activeProjects = emptyList<Project>()
                var finishedProjects = emptyList<Project>()

                if (it["active_projects"] != null) {
                    activeProjects = (it["active_projects"] as List<AnyMap>).map { rowProject ->
                        getProjectFromMap(rowProject)
                    }
                }

                if (it["finished_projects"] != null) {
                    finishedProjects = (it["finished_projects"] as List<AnyMap>).map { rowProject ->
                        getProjectFromMap(rowProject)
                    }
                }

                HashMap<String, List<Project>>().apply {
                    put("active", activeProjects)
                    put("finished", finishedProjects)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    userProjects.postValue(it)
                },
                onError = { Log.e("PR-getUsersProject", it.message) }
            )

        disposable.add(subscription)
    }

    fun getNextProjects(status: Int) {
        if (page != allPages) {
            page += 1
            getProjectByStatus(status)
        }
    }

    fun clearDisposable() = disposable.clear()

    private fun getProjectFromMap(anyMap: AnyMap): Project {
        val projectId = (anyMap["id"] as String).toInt()
        val title = anyMap["title"] as String
        val description = anyMap["description"] as String
        val teams = parseTeams(anyMap["members"] as List<AnyMap>)
        val signingDeadline = fromStringToDate(anyMap["deadline"] as String)
        val projectDeadline = fromStringToDate(anyMap["finish_date"] as String)
        val curator = gson.fromJson(gson.toJsonTree(anyMap["curator"]), User::class.java)
        val tags = (anyMap["tags"] as String).split(",").toMutableList()
        val status = (anyMap["status"] as String).toInt()
        val adminComment = anyMap["adm_comment"] as String

        return Project(
            projectId,
            title,
            description,
            teams,
            signingDeadline,
            projectDeadline,
            curator,
            tags,
            status,
            adminComment
        )
    }

    private fun parseTeams(teams: List<AnyMap>): MutableList<MutableList<MemberOfProject>> {
        val teamList = mutableListOf<MutableList<MemberOfProject>>()
        val defUser = User(0, "", "", "", "", "", "", "", "", -1)

        teams.forEach { map ->
            val membersOfProject = mutableListOf<MemberOfProject>()

            for ((spec, member) in map) {
                when (member) {
                    is Double -> membersOfProject.add(MemberOfProject(spec, defUser))
                    !is Double -> {
                        val memberUser = gson.fromJson(gson.toJsonTree(member), User::class.java)
                        membersOfProject.add(MemberOfProject(spec, memberUser))
                    }
                }
            }

            teamList.add(membersOfProject)
        }

        return teamList
    }

    private fun fromStringToDate(rawDate: String) = Calendar.getInstance().also { date ->
        val splittedDate = rawDate.split("-")
        date[YEAR] = splittedDate[0].toInt()
        date[MONTH] = splittedDate[1].toInt()
        date[DAY_OF_MONTH] = splittedDate[2].toInt()
        date[HOUR_OF_DAY] = 0
        date[MINUTE] = 0
        date[SECOND] = 0
    }
}