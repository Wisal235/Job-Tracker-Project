package ca.wali235.jobtracker.data.repository

import ca.wali235.jobtracker.data.local.UserDao
import ca.wali235.jobtracker.data.model.UserEntity

class UserRepository(
    private val userDao: UserDao
) {
    suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }

    suspend fun loginUser(email: String, password: String): UserEntity? {
        return userDao.loginUser(email, password)
    }
}