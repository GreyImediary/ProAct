package com.proact.poject.serku.proact.viewmodels

import androidx.lifecycle.ViewModel
import com.proact.poject.serku.proact.repositories.ProjectRepository

class ProjectViewModel(private val projectRepository: ProjectRepository) : ViewModel() {
    val currentProject = projectRepository.currentProject
    val isProjectCreated = projectRepository.isProjectCreated
    val isStatusUpdated = projectRepository.isStatusUpdated
    val loadingStatus = projectRepository.loadingStatus
    val projects = projectRepository.projects
    val curatorActiveProjects = projectRepository.curatorActiveProjects
    val curatorFinishedProjects = projectRepository.curatorFinishedProjects
    val curatoRequestProjects = projectRepository.curatorRequestProjects
    val activeUserProjects = projectRepository.activeUserProjects
    val finishedUserProjects = projectRepository.finishedUserProjects
    val tagProjects = projectRepository.tagProjects

    fun createProject(
        token: String,
        title: String,
        description: String,
        deadlineDate: String,
        finishDate: String,
        curatorId: Int,
        members: String,
        tags: String
    ) = projectRepository.createProject(
        token,
        title,
        description,
        deadlineDate,
        finishDate,
        curatorId,
        members,
        tags
    )

    fun updateStatus() = projectRepository.updateProjectsStatus()

    fun getProjectById(id: Int) = projectRepository.getProjectById(id)

    fun getProjectsByStatus(status: Int) = projectRepository.getProjectByStatus(status)

    fun getProjectsByTag(tag: String) = projectRepository.getProjectsByTag(tag)

    fun getActiveUserProjects(userInt: Int) = projectRepository.getActiveUserProject(userInt)

    fun getFinishedUserProjects(userInt: Int) = projectRepository.getFinishedUserProject(userInt)

    fun getCuratorActiveProjects(curatorId: Int) =
        projectRepository.getCuratorActiveProjects(curatorId)

    fun getCuratorFinishedProjects(curatorId: Int) =
        projectRepository.getCuratorFinishedProjects(curatorId)

    fun getCuratorRequestProjects(curatorId: Int) =
        projectRepository.getCuratorRequestProjects(curatorId)

    override fun onCleared() {
        projectRepository.clearDisposable()
        super.onCleared()
    }
}