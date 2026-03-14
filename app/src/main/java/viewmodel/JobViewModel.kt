package ca.wali235.jobtracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.wali235.jobtracker.data.model.JobEntity
import ca.wali235.jobtracker.data.repository.JobRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class JobViewModel(
    private val repository: JobRepository
) : ViewModel() {

    val jobs = repository.getAllJobs()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getJobById(id: Int): JobEntity? {
        return jobs.value.find { it.id == id }
    }

    fun addJob(job: JobEntity) {
        viewModelScope.launch {
            repository.insertJob(job)
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