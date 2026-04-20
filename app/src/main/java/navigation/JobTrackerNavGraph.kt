package ca.wali235.jobtracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ca.wali235.jobtracker.ui.screens.*
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
        startDestination = Routes.Landing.route
    ) {
        composable(Routes.Landing.route) {
            LandingScreen(
                onGetStarted = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.Landing.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginClick = {
                    // go to Main wrapper now (with bottom nav) instead of JobList directly
                    navController.navigate(Routes.Main.route) {
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
                onBackClick = { navController.popBackStack() },
                onSignUpSuccess = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.SignUp.route) { inclusive = true }
                    }
                }
            )
        }

        // main screen with bottom nav - holds jobs / follow ups / profile tabs
        composable(Routes.Main.route) {
            // set current user once we land in the main area
            LaunchedEffect(Unit) {
                jobViewModel.setUser(authViewModel.currentUserId.value)
            }
            MainScreen(
                jobViewModel = jobViewModel,
                authViewModel = authViewModel,
                onAddJobClick = {
                    navController.navigate(Routes.AddEditJob.route)
                },
                onJobClick = { jobId ->
                    navController.navigate(Routes.JobDetails.createRoute(jobId))
                },
                onLogoutClick = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.Main.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.AddEditJob.route) {
            AddEditJobScreen(
                viewModel = jobViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.EditJob.route) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId")?.toIntOrNull() ?: -1
            AddEditJobScreen(
                viewModel = jobViewModel,
                jobId = jobId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.JobDetails.route) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId")?.toIntOrNull() ?: 0
            JobDetailsScreen(
                viewModel = jobViewModel,
                jobId = jobId,
                onBackClick = { navController.popBackStack() },
                onEditClick = { id ->
                    navController.navigate(Routes.EditJob.createRoute(id))
                }
            )
        }
    }
}