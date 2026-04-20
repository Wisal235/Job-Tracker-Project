package ca.wali235.jobtracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ca.wali235.jobtracker.ui.theme.*
import ca.wali235.jobtracker.viewmodel.JobViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailsScreen(
    viewModel: JobViewModel,
    jobId: Int,
    onBackClick: () -> Unit,
    onEditClick: (Int) -> Unit
) {
    val job = viewModel.getJobById(jobId)

    // if the job is missing for some reason just show a nice small message
    if (job == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Job Details", color = PrimaryGreen) },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = PrimaryGreen
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = LightBackground)
                )
            },
            containerColor = LightBackground
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Job not found", color = Color.Gray)
            }
        }
        return
    }

    // pick the color that matches this job status
    val statusColor = getStatusColor(job.status)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Job Details", color = PrimaryGreen) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = PrimaryGreen
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LightBackground)
            )
        },
        containerColor = LightBackground
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LightBackground)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // big colored hero card at top with client name and status
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = statusColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                ) {
                    // small status chip on top of the name
                    Text(
                        text = job.status.uppercase(),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    // big client name text
                    Text(
                        text = job.clientName,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    // the job type below the name so user sees what kind of job
                    Text(
                        text = job.jobType,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            // small title above the info rows
            Text(
                text = "Job Information",
                style = MaterialTheme.typography.titleMedium,
                color = TextDark,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )

            // each row is a little card with icon label and value
            DetailRow(
                icon = Icons.Default.Person,
                label = "Client",
                value = job.clientName
            )
            DetailRow(
                icon = Icons.Default.Phone,
                label = "Phone",
                value = job.phone
            )
            DetailRow(
                icon = Icons.Default.LocationOn,
                label = "Location",
                value = job.location
            )
            DetailRow(
                icon = Icons.Default.Work,
                label = "Job Type",
                value = job.jobType
            )
            // only show follow up if the user picked a date
            if (job.followUpDate.isNotBlank()) {
                DetailRow(
                    icon = Icons.Default.CalendarMonth,
                    label = "Follow Up Date",
                    value = job.followUpDate
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // edit button in primary green
            Button(
                onClick = { onEditClick(job.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit Job", style = MaterialTheme.typography.labelLarge)
            }

            // mark as done only shows if job is not done yet
            if (job.status != "Done") {
                Button(
                    onClick = { viewModel.updateJob(job.copy(status = "Done")) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ActiveColor)
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Mark as Done", style = MaterialTheme.typography.labelLarge)
                }
            }

            // delete button in red so its clear its dangerous
            OutlinedButton(
                onClick = {
                    viewModel.deleteJob(job)
                    onBackClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFD32F2F)
                )
            ) {
                Icon(Icons.Default.Delete, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Delete Job", style = MaterialTheme.typography.labelLarge)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// small card that shows one info row with icon label and value
@Composable
private fun DetailRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // colored square with the icon inside to look like a badge
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = PrimaryGreen.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                // small gray label on top
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(2.dp))
                // the real value below in dark text
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextDark,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
