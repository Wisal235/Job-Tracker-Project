package ca.wali235.jobtracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.wali235.jobtracker.data.model.JobEntity
import ca.wali235.jobtracker.data.repository.JobRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class JobViewModel(
    private val repository: JobRepository
) : ViewModel() {

    // current logged in user id
    private val _userId = MutableStateFlow(0)

    val jobs: StateFlow<List<JobEntity>> = _userId
        .flatMapLatest { id -> repository.getJobsByUser(id) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setUser(userId: Int) {
        _userId.value = userId
    }

    fun getJobById(id: Int): JobEntity? {
        return jobs.value.find { it.id == id }
    }

    fun addJob(job: JobEntity) {
        viewModelScope.launch {
            repository.insertJob(job.copy(userId = _userId.value))
        }
    }

    fun deleteJob(job: JobEntity) {
        viewModelScope.launch {
            repository.deleteJob(job)
        }
    }

    fun updateJob(job: JobEntity) {
        viewModelScope.launch {
            repository.updateJob(job)
        }
    }
}