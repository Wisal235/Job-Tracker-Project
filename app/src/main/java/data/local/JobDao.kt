package ca.wali235.jobtracker.data.local

import androidx.room.*
import ca.wali235.jobtracker.data.model.JobEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {

    @Query("SELECT * FROM jobs")
    fun getAllJobs(): Flow<List<JobEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(job: JobEntity)

    @Delete
    suspend fun deleteJob(job: JobEntity)

    @Update
    suspend fun updateJob(job: JobEntity)
}