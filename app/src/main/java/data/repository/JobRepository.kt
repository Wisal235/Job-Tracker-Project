package ca.wali235.jobtracker.data.repository

import ca.wali235.jobtracker.data.local.JobDao
import ca.wali235.jobtracker.data.model.JobEntity
import kotlinx.coroutines.flow.Flow

class JobRepository(
    private val jobDao: JobDao
) {
    fun getJobsByUser(userId: Int): Flow<List<JobEntity>> {
        return jobDao.getJobsByUser(userId)
    }

    suspend fun insertJob(job: JobEntity) {
        jobDao.insertJob(job)
    }

    suspend fun deleteJob(job: JobEntity) {
        jobDao.deleteJob(job)
    }

    suspend fun updateJob(job: JobEntity) {
        jobDao.updateJob(job)
    }
}