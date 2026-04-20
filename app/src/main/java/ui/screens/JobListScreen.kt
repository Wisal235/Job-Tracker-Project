package ca.wali235.jobtracker.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobListScreen(
    viewModel: JobViewModel,
    onAddJobClick: () -> Unit,
    onJobClick: (Int) -> Unit
) {
    val jobs by viewModel.jobs.collectAsState()

    // state for search bar text
    var searchQuery by remember { mutableStateOf("") }

    // state for filter chip (All, Lead, Quote, Active, Done)
    var selectedFilter by remember { mutableStateOf("All") }

    // filter jobs based on search text and selected status
    val filteredJobs = remember(jobs, searchQuery, selectedFilter) {
        jobs.filter { job ->
            val matchesSearch = searchQuery.isEmpty() ||
                    job.clientName.contains(searchQuery, ignoreCase = true) ||
                    job.location.contains(searchQuery, ignoreCase = true) ||
                    job.jobType.contains(searchQuery, ignoreCase = true)
            val matchesFilter = selectedFilter == "All" || job.status == selectedFilter
            matchesSearch && matchesFilter
        }
    }

    // count jobs for the stats row
    val totalJobs = jobs.size
    val activeJobs = jobs.count { it.status in listOf("Lead", "Quote", "Active") }
    val followUpCount = jobs.count { it.followUpDate.isNotEmpty() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // greeting with two lines
                    Column {
                        Text(
                            text = "Your Jobs",
                            style = MaterialTheme.typography.titleLarge,
                            color = PrimaryGreen
                        )
                        Text(
                            text = "Welcome back 👋",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LightBackground
                )
            )
        },
        floatingActionButton = {
            // extended button has text next to the plus
            ExtendedFloatingActionButton(
                onClick = onAddJobClick,
                containerColor = PrimaryGreen,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Add Job") }
            )
        },
        containerColor = LightBackground
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // animated empty state - shows with fade + slide when no jobs
            AnimatedVisibility(
                visible = jobs.isEmpty(),
                enter = fadeIn(animationSpec = tween(500)) +
                        slideInVertically(animationSpec = tween(500)),
                exit = fadeOut() + slideOutVertically()
            ) {
                EmptyState()
            }

            // main content shows when there is at least one job
            AnimatedVisibility(
                visible = jobs.isNotEmpty(),
                enter = fadeIn(animationSpec = tween(400)),
                exit = fadeOut()
            ) {
                Column(modifier = Modifier.fillMaxSize()) {

                    // stats row with 3 small cards
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            modifier = Modifier.weight(1f),
                            count = totalJobs,
                            label = "Total",
                            color = PrimaryGreen
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            count = activeJobs,
                            label = "Active",
                            color = ActiveColor
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            count = followUpCount,
                            label = "Follow-ups",
                            color = LeadColor
                        )
                    }

                    // search bar to find jobs by name or location
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        placeholder = { Text("Search clients or locations") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null)
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryGreen,
                            focusedLeadingIconColor = PrimaryGreen
                        )
                    )

                    // filter chips for job status
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val filters = listOf("All", "Lead", "Quote", "Active", "Done")
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

                    // animated no-match state when search/filter has no results
                    AnimatedVisibility(
                        visible = filteredJobs.isEmpty(),
                        enter = fadeIn(animationSpec = tween(300)) +
                                slideInVertically(animationSpec = tween(300)),
                        exit = fadeOut()
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "🔍", fontSize = 48.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "No jobs match",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = PrimaryGreen
                                )
                                Text(
                                    text = "Try a different search or filter",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    // animated list - shows when there are matching jobs
                    AnimatedVisibility(
                        visible = filteredJobs.isNotEmpty(),
                        enter = fadeIn(animationSpec = tween(300)),
                        exit = fadeOut()
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 8.dp,
                                bottom = 80.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(filteredJobs) { job ->
                                JobCard(job = job, onClick = { onJobClick(job.id) })
                            }
                        }
                    }
                }
            }
        }
    }
}

// small card for the stats row at top of screen
@Composable
private fun StatCard(
    modifier: Modifier,
    count: Int,
    label: String,
    color: Color
) {
    Card(
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count.toString(),
                color = color,
                style = MaterialTheme.typography.headlineMedium,
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

// card for one job in the list
@Composable
private fun JobCard(job: JobEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // colored strip on left to show job status
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(getStatusColor(job.status))
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            ) {
                // first row with client name and status badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = job.clientName,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextDark,
                        modifier = Modifier.weight(1f)
                    )
                    StatusBadge(status = job.status)
                }
                Spacer(modifier = Modifier.height(8.dp))
                // location row with pin icon
                InfoRow(icon = Icons.Default.LocationOn, text = job.location)
                Spacer(modifier = Modifier.height(4.dp))
                // job type row with briefcase icon
                InfoRow(icon = Icons.Default.Work, text = job.jobType)
            }
        }
    }
}

// small colored badge that shows the job status
@Composable
private fun StatusBadge(status: String) {
    // color changes with smooth animation when status updates
    val color by animateColorAsState(
        targetValue = getStatusColor(status),
        animationSpec = tween(400),
        label = "statusColor"
    )
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text = status,
            style = MaterialTheme.typography.labelMedium,
            color = color,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

// small row with an icon and gray text
@Composable
private fun InfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

// message that shows when the user has no jobs at all
@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "💼", fontSize = 72.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No jobs yet!",
                style = MaterialTheme.typography.headlineMedium,
                color = PrimaryGreen
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tap the + button to add your first job",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

// helper function that returns the right color for each job status
// we use this in JobCard and StatusBadge
@Composable
fun getStatusColor(status: String): Color {
    return when (status) {
        "Lead" -> LeadColor
        "Quote" -> QuoteColor
        "Active" -> ActiveColor
        "Done" -> DoneColor
        else -> Color.Gray
    }
}