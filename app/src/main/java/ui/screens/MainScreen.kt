package ca.wali235.jobtracker.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ca.wali235.jobtracker.ui.theme.LightBackground
import ca.wali235.jobtracker.ui.theme.PrimaryGreen
import ca.wali235.jobtracker.viewmodel.AuthViewModel
import ca.wali235.jobtracker.viewmodel.JobViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    jobViewModel: JobViewModel,
    authViewModel: AuthViewModel,
    onAddJobClick: () -> Unit,
    onJobClick: (Int) -> Unit,
    onLogoutClick: () -> Unit
) {
    // which tab is currently selected (0 = jobs, 1 = follow ups, 2 = profile)
    var selectedTab by remember { mutableStateOf(0) }

    // colors used by all 3 tab items
    val itemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = Color.White,
        selectedTextColor = PrimaryGreen,
        indicatorColor = PrimaryGreen,
        unselectedIconColor = Color.Gray,
        unselectedTextColor = Color.Gray
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                // jobs tab
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Work, contentDescription = null) },
                    label = { Text("Jobs") },
                    colors = itemColors
                )
                // follow ups tab
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Notifications, contentDescription = null) },
                    label = { Text("Follow Ups") },
                    colors = itemColors
                )
                // profile tab
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Profile") },
                    colors = itemColors
                )
            }
        },
        containerColor = LightBackground
    ) { paddingValues ->
        // show the right screen based on which tab is open
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                0 -> JobListScreen(
                    viewModel = jobViewModel,
                    onAddJobClick = onAddJobClick,
                    onJobClick = onJobClick
                )
                1 -> FollowUpScreen(
                    viewModel = jobViewModel,
                    onBackClick = { selectedTab = 0 }
                )
                2 -> ProfileScreen(
                    jobViewModel = jobViewModel,
                    authViewModel = authViewModel,
                    onLogoutClick = onLogoutClick
                )
            }
        }
    }
}