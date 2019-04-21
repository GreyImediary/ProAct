package com.proact.poject.serku.proact.viewmodels

import androidx.lifecycle.ViewModel
import com.proact.poject.serku.proact.repositories.UserRepository

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    val isRegistered = userRepository.isRegistered
    val userAdded = userRepository.userAdded
    val userVerified = userRepository.userVerified
    val authed = userRepository.authed
    val currentUser = userRepository.currentUser
    val allWorkers = userRepository.allWorkers
    val allCustomers = userRepository.allCustomers
    val allAdmins = userRepository.allAdmins
    val tags = userRepository.tags

    fun getUserById(id: Int) = userRepository.getUserById(id)

    fun getUserByEmail(email: String) = userRepository.getUserByEmail(email)

    fun getAllWorkers() = userRepository.getAllWorkers()

    fun getAllCustomers() = userRepository.getAllCustomers()

    fun getAllAdmins() = userRepository.getAllAdmins()

    fun isUserRegistered(email: String) = userRepository.isUserRegistered(email)

    fun verifyUser(email: String, password: String) = userRepository.verifyUser(email, password)

    fun authUser(email: String, password: String) = userRepository.authUser(email, password)

    fun fetchTags(token: String) = userRepository.fetchTags(token)

    fun addUser(name: String,
                surname: String,
                middleName: String = "",
                email: String,
                password: String,
                phone: String = "",
                studentGroup: String = "",
                description: String = "",
                userGroup: Int,
                avatar: String = ""
                ) = userRepository.addUser(name, surname, middleName, email, password, phone, studentGroup, description, userGroup, avatar)

    override fun onCleared() {
        userRepository.clearDisposable()
        super.onCleared()
    }
}