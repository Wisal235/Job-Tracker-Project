package ca.wali235.jobtracker.navigation

sealed class Routes(val route: String) {
    object Login : Routes("login")
    object SignUp : Routes("signup")
    object JobList : Routes("job_list")
    object AddEditJob : Routes("add_edit_job")
    object JobDetails : Routes("job_details/{jobId}") {
        fun createRoute(jobId: Int) = "job_details/$jobId"
    }
    object FollowUp : Routes("follow_up")
}