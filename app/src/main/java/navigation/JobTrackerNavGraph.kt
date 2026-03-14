package ca.wali235.jobtracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ca.wali235.jobtracker.ui.screens.AddEditJobScreen
import ca.wali235.jobtracker.ui.screens.FollowUpScreen
import ca.wali235.jobtracker.ui.screens.JobDetailsScreen
import ca.wali235.jobtracker.ui.screens.JobListScreen
import ca.wali235.jobtracker.ui.screens.LoginScreen
import ca.wali235.jobtracker.ui.screens.SignUpScreen
import ca.wali235.jobtracker.viewmodel.AuthViewModel
import ca.wali235.jobtracker.viewmodel.JobViewModel

@Composable
fun JobTrackerNavGraph(
    jobViewModel: JobViewModel,
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Login.route
    ) {
        composable(Routes.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginClick = {
                    navController.navigate(Routes.JobList.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigate(Routes.SignUp.route)
                }
            )
        }

        composable(Routes.SignUp.route) {
            SignUpScreen(
                authViewModel = authViewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                onSignUpSuccess = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.SignUp.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.JobList.route) {
            JobListScreen(
                viewModel = jobViewModel,
                onAddJobClick = {
                    navController.navigate(Routes.AddEditJob.route)
                },
                onJobClick = { jobId ->
                    navController.navigate(Routes.JobDetails.createRoute(jobId))
                },
                onFollowUpClick = {
                    navController.navigate(Routes.FollowUp.route)
                },
                onLogoutClick = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.JobList.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.AddEditJob.route) {
            AddEditJobScreen(
                viewModel = jobViewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.JobDetails.route) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId")?.toIntOrNull() ?: 0

            JobDetailsScreen(
                viewModel = jobViewModel,
                jobId = jobId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.FollowUp.route) {
            FollowUpScreen(
                viewModel = jobViewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}