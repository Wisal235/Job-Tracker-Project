package ca.wali235.jobtracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ca.wali235.jobtracker.data.model.JobEntity
import ca.wali235.jobtracker.data.model.UserEntity

@Database(
    entities = [JobEntity::class, UserEntity::class],
    version = 2,
    exportSchema = false
)
abstract class JobDatabase : RoomDatabase() {

    abstract fun jobDao(): JobDao
    abstract fun userDao(): UserDao
}