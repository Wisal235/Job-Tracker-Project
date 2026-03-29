package ca.wali235.jobtracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jobs")
data class JobEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int = 0, // link job to user
    val clientName: String,
    val location: String,
    val phone: String,
    val jobType: String,
    val status: String,
    val followUpDate: String = ""
)