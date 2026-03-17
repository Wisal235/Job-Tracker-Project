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

    fun signUp(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (email.isBlank() || password.isBlank()) {
                message.value = "Please fill in all fields"
                return@launch
            }

            val existingUser = repository.getUserByEmail(email)
            if (existingUser != null) {
                message.value = "This email is already registered"
                return@launch
            }

            repository.insertUser(
                UserEntity(
                    email = email,
                    password = password
                )
            )

            message.value = "Account created successfully"
            onSuccess()
        }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (email.isBlank() || password.isBlank()) {
                message.value = "Please enter email and password"
                return@launch
            }

            val userByEmail = repository.getUserByEmail(email)
            if (userByEmail == null) {
                message.value = "This email is not registered. Please sign up first"
                return@launch
            }

            val loggedInUser = repository.loginUser(email, password)
            if (loggedInUser == null) {
                message.value = "Email or password is incorrect"
                return@launch
            }

            message.value = ""
            onSuccess()
        }
    }
}