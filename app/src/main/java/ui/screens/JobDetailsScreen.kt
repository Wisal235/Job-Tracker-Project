package ca.wali235.jobtracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.wali235.jobtracker.ui.theme.LightBackground
import ca.wali235.jobtracker.ui.theme.PrimaryGreen
import ca.wali235.jobtracker.viewmodel.JobViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailsScreen(
    viewModel: JobViewModel,
    jobId: Int,
    onBackClick: () -> Unit
) {
    val job = viewModel.getJobById(jobId)

    if (job == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Job Details") },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null)
                        }
                    }
                )
            },
            containerColor = LightBackground
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightBackground)
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text("Job not found")
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Job Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        containerColor = LightBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LightBackground)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Client: ${job.clientName}")
            Text("Phone: ${job.phone}")
            Text("Location: ${job.location}")
            Text("Job Type: ${job.jobType}")
            Text("Status: ${job.status}")

            Button(
                onClick = {
                    viewModel.updateJob(job.copy(status = "Done"))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text("Mark as Done")
            }

            OutlinedButton(
                onClick = {
                    viewModel.deleteJob(job)
                    onBackClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Delete Job")
            }
        }
    }
}