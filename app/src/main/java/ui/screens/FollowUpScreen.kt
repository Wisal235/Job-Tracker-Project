package ca.wali235.jobtracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.wali235.jobtracker.data.model.JobEntity
import ca.wali235.jobtracker.ui.theme.*
import ca.wali235.jobtracker.viewmodel.JobViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowUpScreen(
    viewModel: JobViewModel,
    onBackClick: () -> Unit  // we keep this so MainScreen keeps working but we dont use it now
) {
    val jobs by viewModel.jobs.collectAsState()

    // only keep jobs that have a follow up date set
    val followUpJobs = jobs.filter { it.followUpDate.isNotBlank() }

    // which chip is picked at top
    var selectedFilter by remember { mutableStateOf("All") }

    // count jobs for the summary cards
    val upcomingCount = followUpJobs.count { isUpcoming(it.followUpDate) && it.status != "Done" }
    val overdueCount = followUpJobs.count { isOverdue(it.followUpDate) && it.status != "Done" }

    // apply the chip filter
    val filteredJobs = when (selectedFilter) {
        "Upcoming" -> followUpJobs.filter { isUpcoming(it.followUpDate) && it.status != "Done" }
        "Overdue" -> followUpJobs.filter { isOverdue(it.followUpDate) && it.status != "Done" }
        else -> followUpJobs
    }

    // sort so older dates show first (overdue at top then upcoming)
    val visibleJobs = filteredJobs.sortedBy {
        parseFollowUpDate(it.followUpDate)?.timeInMillis ?: Long.MAX_VALUE
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Follow Ups", color = PrimaryGreen) },
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
        ) {

            // top row with 2 summary cards (upcoming + overdue)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SummaryCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Schedule,
                    count = upcomingCount,
                    label = "Upcoming",
                    color = PrimaryGreen
                )
                SummaryCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Warning,
                    count = overdueCount,
                    label = "Overdue",
                    color = Color(0xFFD32F2F)
                )
            }

            // filter chips for All / Upcoming / Overdue
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filters = listOf("All", "Upcoming", "Overdue")
                items(filters) { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryGreen,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            // the list or a pretty empty state
            if (visibleJobs.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "📅", fontSize = 64.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (followUpJobs.isEmpty())
                                "No follow-ups yet"
                            else
                                "Nothing in this filter",
                            style = MaterialTheme.typography.titleMedium,
                            color = PrimaryGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (followUpJobs.isEmpty())
                                "Add a follow-up date when creating a job"
                            else
                                "Try a different filter",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(visibleJobs) { job ->
                        FollowUpCard(job = job)
                    }
                }
            }
        }
    }
}

// small summary card at the top row showing count with icon and label
@Composable
private fun SummaryCard(
    modifier: Modifier,
    icon: ImageVector,
    count: Int,
    label: String,
    color: Color
) {
    Card(
        modifier = modifier.height(90.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // colored icon box on the left
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = color.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = color,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
            }
        }
    }
}

// card for one follow up item in the list
@Composable
private fun FollowUpCard(job: JobEntity) {
    val overdue = isOverdue(job.followUpDate) && job.status != "Done"
    val statusColor = getStatusColor(job.status)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // colored strip on left that matches status
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(statusColor)
            )
            // calendar icon in a rounded box
            Box(
                modifier = Modifier
                    .padding(start = 14.dp, top = 16.dp, bottom = 16.dp)
                    .size(50.dp)
                    .background(
                        color = PrimaryGreen.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(26.dp)
                )
            }
            // main column with name status and date
            Column(
                modifier = Modifier
                    .padding(14.dp)
                    .weight(1f)
            ) {
                // top row with client name and status badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = job.clientName,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextDark,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                    // small status chip on the right
                    Box(
                        modifier = Modifier
                            .background(
                                color = statusColor.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = job.status,
                            style = MaterialTheme.typography.labelMedium,
                            color = statusColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                // follow up date row with optional overdue chip
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "📅 ${job.followUpDate}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    if (overdue) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color(0xFFD32F2F).copy(alpha = 0.15f),
                                    shape = CircleShape
                                )
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "OVERDUE",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFD32F2F),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

// try to read date string like 15/4/2026 into a Calendar object
private fun parseFollowUpDate(dateStr: String): Calendar? {
    return try {
        val parts = dateStr.split("/")
        Calendar.getInstance().apply {
            set(parts[2].toInt(), parts[1].toInt() - 1, parts[0].toInt(), 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
    } catch (e: Exception) {
        null
    }
}

// today at midnight so we can compare dates fairly
private fun todayStart(): Calendar {
    return Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}

// true when date is in the past
private fun isOverdue(dateStr: String): Boolean {
    val date = parseFollowUpDate(dateStr) ?: return false
    return date.before(todayStart())
}

// true when date is today or later
private fun isUpcoming(dateStr: String): Boolean {
    val date = parseFollowUpDate(dateStr) ?: return false
    return !date.before(todayStart())
}