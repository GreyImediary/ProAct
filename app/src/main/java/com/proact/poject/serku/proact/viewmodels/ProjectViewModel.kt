package com.proact.poject.serku.proact.viewmodels

import androidx.lifecycle.ViewModel
import com.proact.poject.serku.proact.repositories.ProjectRepository
import java.util.*

class ProjectViewModel(private val projectRepository: ProjectRepository) : ViewModel() {
    val currentProject = projectRepository.currentProject
    val isProjectCreated = projectRepository.isProjectCreated
    val isStatusUpdated = projectRepository.isStatusUpdated
    val loadingStatus = projectRepository.loadingStatus
    val projects = projectRepository.projects

    fun createProject(title: String,
                      description: String,
                      deadlineDate: Calendar,
                      curatorId: Int,
                      members: String,
                      tags: String) = projectRepository.createProject(title, description, deadlineDate, curatorId, members, tags)

    fun updateStatus() = projectRepository.updateStatus()

    fun getProjectById(id: Int) = projectRepository.getProjectById(id)

    fun getProjectsByStatus(status: Int) = projectRepository.getProjectByStatus(status)

    fun getNextProjects(status: Int) = projectRepository.getNextProjects(status)

    override fun onCleared() {
        projectRepository.clearDisposable()
        super.onCleared()
    }
}