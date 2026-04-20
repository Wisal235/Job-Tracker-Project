package ca.wali235.jobtracker.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.wali235.jobtracker.data.model.UserEntity
import ca.wali235.jobtracker.data.repository.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: UserRepository
) : ViewModel() {

    val message = mutableStateOf("")
    val isSuccess = mutableStateOf(false)
    val currentUserId = mutableStateOf(0) // save logged in user id
    val currentUserEmail = mutableStateOf("") // save logged in user email

    fun signUp(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (email.isBlank() || password.isBlank()) {
                message.value = "Please fill in all fields"
                isSuccess.value = false
                return@launch
            }

            val existingUser = repository.getUserByEmail(email)
            if (existingUser != null) {
                message.value = "This email is already registered"
                isSuccess.value = false
                return@launch
            }

            repository.insertUser(UserEntity(email = email, password = password))
            message.value = "Account created successfully"
            isSuccess.value = true
            onSuccess()
        }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (email.isBlank() || password.isBlank()) {
                message.value = "Please enter email and password"
                isSuccess.value = false
                return@launch
            }

            val userByEmail = repository.getUserByEmail(email)
            if (userByEmail == null) {
                message.value = "This email is not registered. Please sign up first"
                isSuccess.value = false
                return@launch
            }

            val loggedInUser = repository.loginUser(email, password)
            if (loggedInUser == null) {
                message.value = "Email or password is incorrect"
                isSuccess.value = false
                return@launch
            }

            currentUserId.value = loggedInUser.id // save user id on login
            currentUserEmail.value = loggedInUser.email // save email on login
            message.value = ""
            isSuccess.value = false
            onSuccess()
        }
    }

    fun logout() {
        // clear user info when logging out
        currentUserId.value = 0
        currentUserEmail.value = ""
    }

    fun clearMessage() {
        message.value = ""
        isSuccess.value = false
    }
}