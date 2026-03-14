package ca.wali235.jobtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import ca.wali235.jobtracker.data.local.JobDatabase
import ca.wali235.jobtracker.data.repository.JobRepository
import ca.wali235.jobtracker.data.repository.UserRepository
import ca.wali235.jobtracker.navigation.JobTrackerNavGraph
import ca.wali235.jobtracker.ui.theme.JobTrackerTheme
import ca.wali235.jobtracker.viewmodel.AuthViewModel
import ca.wali235.jobtracker.viewmodel.AuthViewModelFactory
import ca.wali235.jobtracker.viewmodel.JobViewModel
import ca.wali235.jobtracker.viewmodel.JobViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            JobDatabase::class.java,
            "job_database"
        )
            .fallbackToDestructiveMigration()
            .build()

        val jobRepository = JobRepository(database.jobDao())
        val userRepository = UserRepository(database.userDao())

        val jobViewModel: JobViewModel = ViewModelProvider(
            this,
            JobViewModelFactory(jobRepository)
        )[JobViewModel::class.java]

        val authViewModel: AuthViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(userRepository)
        )[AuthViewModel::class.java]

        setContent {
            JobTrackerTheme {
                JobTrackerNavGraph(
                    jobViewModel = jobViewModel,
                    authViewModel = authViewModel
                )
            }
        }
    }
}