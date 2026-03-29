package ca.wali235.jobtracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.wali235.jobtracker.ui.theme.*
import ca.wali235.jobtracker.viewmodel.JobViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobListScreen(
    viewModel: JobViewModel,
    onAddJobClick: () -> Unit,
    onJobClick: (Int) -> Unit,
    onFollowUpClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val jobs by viewModel.jobs.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Job Tracker") },
                actions = {
                    IconButton(onClick = onFollowUpClick) {
                        Icon(Icons.Default.Notifications, contentDescription = null)
                    }
                    IconButton(onClick = onLogoutClick) {
                        Icon(Icons.Default.Logout, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LightBackground
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddJobClick,
                containerColor = PrimaryGreen
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        },
        containerColor = LightBackground
    ) { paddingValues ->

        if (jobs.isEmpty()) {
            // show empty state when no jobs
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "💼", fontSize = 56.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No jobs yet!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tap + to add your first job",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightBackground)
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(jobs) { job ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onJobClick(job.id) },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBackground)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = job.clientName,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = job.location,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                            Row(modifier = Modifier.padding(top = 12.dp)) {
                                AssistChip(
                                    onClick = { onJobClick(job.id) },
                                    label = { Text(job.status) },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = getStatusColor(job.status)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun getStatusColor(status: String) = when (status) {
    "Lead" -> LeadColor
    "Quote" -> QuoteColor
    "Active" -> ActiveColor
    "Done" -> DoneColor
    else -> LeadColor
}