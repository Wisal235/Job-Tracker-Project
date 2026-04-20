package ca.wali235.jobtracker.navigation

sealed class Routes(val route: String) {
    object Landing : Routes("landing")
    object Login : Routes("login")
    object SignUp : Routes("signup")
    object Main : Routes("main")
    object AddEditJob : Routes("add_edit_job")
    object EditJob : Routes("edit_job/{jobId}") {
        fun createRoute(jobId: Int) = "edit_job/$jobId"
    }
    object JobDetails : Routes("job_details/{jobId}") {
        fun createRoute(jobId: Int) = "job_details/$jobId"
    }
}