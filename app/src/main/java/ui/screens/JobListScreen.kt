package ca.wali235.jobtracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.wali235.jobtracker.ui.theme.ActiveColor
import ca.wali235.jobtracker.ui.theme.CardBackground
import ca.wali235.jobtracker.ui.theme.DoneColor
import ca.wali235.jobtracker.ui.theme.LeadColor
import ca.wali235.jobtracker.ui.theme.LightBackground
import ca.wali235.jobtracker.ui.theme.PrimaryGreen
import ca.wali235.jobtracker.ui.theme.QuoteColor
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
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = job.clientName,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = job.location,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 6.dp)
                        )

                        Row(
                            modifier = Modifier.padding(top = 12.dp)
                        ) {
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

@Composable
fun getStatusColor(status: String) = when (status) {
    "Lead" -> LeadColor
    "Quote" -> QuoteColor
    "Active" -> ActiveColor
    "Done" -> DoneColor
    else -> LeadColor
}