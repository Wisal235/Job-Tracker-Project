package ca.wali235.jobtracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ca.wali235.jobtracker.data.repository.JobRepository

class JobViewModelFactory(
    private val repository: JobRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return JobViewModel(repository) as T
    }
}