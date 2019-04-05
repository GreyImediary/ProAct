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
    val curatorActiveProjects = MutableLiveData<MutableList<Project>>()
    val curatorFinishedProjects = MutableLiveData<MutableList<Project>>()
    val curatorRequestProjects = MutableLiveData<MutableList<Project>>()
    val loadingStatus = MutableLiveData<Boolean>()
    val activeUserProjects = MutableLiveData<List<Project>>()
    val finishedUserProjects = MutableLiveData<List<Project>>()

    private val allProjectsPages = mutableMapOf(
        "page" to 1.0,
        "allPages" to 0.0,
        "perPage" to 3.0
    )

    private val allCuratorActiveProjectsPages = mutableMapOf(
        "page" to 1.0,
        "allPages" to 0.0,
        "perPage" to 3.0
    )

    private val allCuratorFinishedProjectsPages = mutableMapOf(
        "page" to 1.0,
        "allPages" to 0.0,
        "perPage" to 3.0
    )

    private val allCuratorRequestProjectsPages = mutableMapOf(
        "page" to 1.0,
        "allPages" to 0.0,
        "perPage" to 3.0
    )

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
        val subscription = projectApi.getProjectsByStatus(
            status,
            allProjectsPages["perPage"]!!.toInt(),
            allProjectsPages["page"]!!
        )
            .subscribeOn(Schedulers.io())
            .map {
                allProjectsPages["allPages"] = it["pages"] as Double
                allProjectsPages["page"] = it["page"] as Double

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
                            if (!containsAll(it)) {
                                addAll(it)
                            }
                        }

                        projects.postValue(list)
                    }
                }
            )

        disposable.add(subscription)
    }

    fun getCuratorActiveProjects(curatorId: Int) =
        getCuratorProject(curatorActiveProjects, allCuratorActiveProjectsPages, curatorId, 1)

    fun getCuratorFinishedProjects(curatorId: Int) {
        getCuratorProject(curatorFinishedProjects, allCuratorFinishedProjectsPages, curatorId, 2)
    }

    fun getCuratorRequestProjects(curatorId: Int) =
        getCuratorProject(curatorRequestProjects, allCuratorRequestProjectsPages, curatorId, 30)

    private fun getCuratorProject(
        curatorsProjects: MutableLiveData<MutableList<Project>>,
        pages: MutableMap<String, Double>,
        curatorId: Int,
        projectStatus: Int

    ) {
        loadingStatus.postValue(true)
        val subscription =
            projectApi.getCuratorsProjects(
                curatorId,
                projectStatus,
                pages["perPage"]!!.toInt(),
                pages["page"]!!
            )
                .subscribeOn(Schedulers.io())
                .map {
                    pages["allPages"] = it["pages"] as Double
                    pages["page"] = it["page"] as Double

                    var projectList = emptyList<AnyMap>()

                    if (it["data"] != null) {
                        projectList = it["data"] as List<AnyMap>
                    }

                    projectList.map { rowProject -> getProjectFromMap(rowProject) }
                }
                .doOnComplete { loadingStatus.postValue(false) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onError = { Log.e("PR-getCuratorsProjects", it.message) },
                    onNext = {
                        if (curatorsProjects.value == null) {
                            curatorsProjects.postValue(it as MutableList<Project>)
                        } else {
                            val list = curatorsProjects.value?.apply {
                                if (!containsAll(it)) {
                                    addAll(it)
                                }
                            }

                            curatorsProjects.postValue(list)
                        }
                    }
                )

        disposable.add(subscription)
    }

    fun getActiveUserProject(userId: Int) = getUserProject(activeUserProjects, userId, active = true)

    fun getFinishedUserProject(userId: Int) = getUserProject(finishedUserProjects, userId, finished = true)

    private fun getUserProject(
        userProjects: MutableLiveData<List<Project>>,
        userId: Int,
        active: Boolean = false,
        finished: Boolean = false
    ) {
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
                    if (active) {
                        userProjects.postValue(it["active"])
                    } else if (finished) {
                        userProjects.postValue(it["finished"])
                    }
                },
                onError = { Log.e("PR-getUsersProject", it.message) }
            )

        disposable.add(subscription)
    }

    fun getNextProjects(status: Int) {
        if (allProjectsPages["page"] != allProjectsPages["allPages"]) {
            allProjectsPages["page"] = allProjectsPages["page"]!! + 1
            getProjectByStatus(status)
        }
    }

    fun getNextCuratorActiveProjects(curatorId: Int) {
        val page = allCuratorActiveProjectsPages["page"]
        val allPages = allCuratorActiveProjectsPages["allPages"]
        if (page != allPages) {
            allCuratorActiveProjectsPages["page"] = page!! + 1
            getCuratorActiveProjects(curatorId)
        }
    }

    fun getNextCuratorFinishedProjects(curatorId: Int) {
        val page = allCuratorFinishedProjectsPages["page"]
        val allPages = allCuratorFinishedProjectsPages["allPages"]
        if (page != allPages) {
            allCuratorFinishedProjectsPages["page"] = page!! + 1
            getCuratorActiveProjects(curatorId)
        }
    }

    fun getNextCuratorRequestProjects(curatorId: Int) {
        val page = allCuratorRequestProjectsPages["page"]
        val allPages = allCuratorRequestProjectsPages["allPages"]
        if (page != allPages) {
            allCuratorRequestProjectsPages["page"] = page!! + 1
            getCuratorActiveProjects(curatorId)
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