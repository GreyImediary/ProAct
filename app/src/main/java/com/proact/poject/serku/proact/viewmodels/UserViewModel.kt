package com.proact.poject.serku.proact.viewmodels

import androidx.lifecycle.ViewModel
import com.proact.poject.serku.proact.repositories.UserRepository

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    val isRegistered = userRepository.isRegistered
    val userAdded = userRepository.userAdded
    val userVirified = userRepository.userVerified
    val currentUser = userRepository.currentUser
    val allWorkers = userRepository.allWorkers
    val allCustomers = userRepository.allCustomers
    val allAdmins = userRepository.allAdmins

    fun getUserById(id: Int) = userRepository.getUserById(id)

    fun getAllWorkers() = userRepository.getAllWorkers()

    fun getAllCustomers() = userRepository.getAllCustomers()

    fun getAllAdmins() = userRepository.getAllAdmins()

    fun isUserRegistered(email: String) = userRepository.isUserRegistered(email)

    fun verifyUser(email: String, password: String) = userRepository.verifyUser(email, password)

    fun addUser(name: String,
                surname: String,
                middleName: String = "",
                email: String,
                password: String,
                phone: String = "",
                studentGroup: String = "",
                description: String = "",
                userGroup: Int
                ) = userRepository.addUser(name, surname, middleName, email, password, phone, studentGroup, description, userGroup)

    override fun onCleared() {
        userRepository.clearDisposable()
        super.onCleared()
    }
}