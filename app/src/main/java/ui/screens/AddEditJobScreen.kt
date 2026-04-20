package ca.wali235.jobtracker.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ca.wali235.jobtracker.data.model.JobEntity
import ca.wali235.jobtracker.ui.theme.*
import ca.wali235.jobtracker.viewmodel.JobViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditJobScreen(
    viewModel: JobViewModel,
    jobId: Int = -1,
    onBackClick: () -> Unit
) {
    val existingJob = if (jobId != -1) viewModel.getJobById(jobId) else null
    val isEditing = existingJob != null

    // all the fields use mutableStateOf so typing updates the screen
    val clientName = remember { mutableStateOf(existingJob?.clientName ?: "") }
    val phone = remember { mutableStateOf(existingJob?.phone ?: "") }
    val location = remember { mutableStateOf(existingJob?.location ?: "") }
    val jobType = remember { mutableStateOf(existingJob?.jobType ?: "") }
    val status = remember { mutableStateOf(existingJob?.status ?: "Lead") }
    val followUpDate = remember { mutableStateOf(existingJob?.followUpDate ?: "") }

    // list of status choices that appear in the dropdown
    val statusOptions = listOf("Lead", "Quote", "Active", "Done")
    var statusExpanded by remember { mutableStateOf(false) }

    // setup android date picker for follow up date
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            followUpDate.value = "$day/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditing) "Edit Job" else "Add New Job",
                        color = PrimaryGreen
                    )
                },
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
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // green header card so the screen feels welcoming
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryGreen)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (isEditing) "Edit your job" else "Create new job",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Fill in the details below",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            // client name field with person icon on the left
            PrettyField(
                value = clientName.value,
                onValueChange = { clientName.value = it },
                label = "Client Name",
                leadingIcon = Icons.Default.Person
            )

            // phone field with phone icon
            PrettyField(
                value = phone.value,
                onValueChange = { phone.value = it },
                label = "Phone Number",
                leadingIcon = Icons.Default.Phone
            )

            // location field with pin icon
            PrettyField(
                value = location.value,
                onValueChange = { location.value = it },
                label = "Location",
                leadingIcon = Icons.Default.LocationOn
            )

            // job type field with briefcase icon
            PrettyField(
                value = jobType.value,
                onValueChange = { jobType.value = it },
                label = "Job Type",
                leadingIcon = Icons.Default.Work
            )

            // status dropdown - we keep the dropdown but color it pretty
            ExposedDropdownMenuBox(
                expanded = statusExpanded,
                onExpandedChange = { statusExpanded = !statusExpanded }
            ) {
                OutlinedTextField(
                    value = status.value,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Status") },
                    // small colored circle shows which status is picked
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(
                                    color = getStatusColor(status.value),
                                    shape = CircleShape
                                )
                        )
                    },
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryGreen,
                        focusedLabelColor = PrimaryGreen
                    )
                )
                // the open dropdown with each option
                ExposedDropdownMenu(
                    expanded = statusExpanded,
                    onDismissRequest = { statusExpanded = false }
                ) {
                    statusOptions.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                // each item has its own color dot so its easy to see
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .background(
                                                color = getStatusColor(option),
                                                shape = CircleShape
                                            )
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(option)
                                    if (option == status.value) {
                                        // show a check when this is the one picked
                                        Spacer(modifier = Modifier.weight(1f))
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            tint = PrimaryGreen,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            },
                            onClick = {
                                status.value = option
                                statusExpanded = false
                            }
                        )
                    }
                }
            }

            // follow up date field with calendar icon
            OutlinedTextField(
                value = followUpDate.value,
                onValueChange = {},
                readOnly = true,
                label = { Text("Follow Up Date") },
                leadingIcon = {
                    Icon(
                        Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = PrimaryGreen
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryGreen,
                    focusedLabelColor = PrimaryGreen
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // big save button with save icon
            Button(
                onClick = {
                    if (clientName.value.isNotBlank() &&
                        phone.value.isNotBlank() &&
                        location.value.isNotBlank() &&
                        jobType.value.isNotBlank()
                    ) {
                        if (isEditing) {
                            viewModel.updateJob(
                                existingJob!!.copy(
                                    clientName = clientName.value,
                                    phone = phone.value,
                                    location = location.value,
                                    jobType = jobType.value,
                                    status = status.value,
                                    followUpDate = followUpDate.value
                                )
                            )
                        } else {
                            viewModel.addJob(
                                JobEntity(
                                    clientName = clientName.value,
                                    phone = phone.value,
                                    location = location.value,
                                    jobType = jobType.value,
                                    status = status.value,
                                    followUpDate = followUpDate.value
                                )
                            )
                        }
                        onBackClick()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Icon(Icons.Default.Save, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isEditing) "Update Job" else "Save Job",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// helper so all text fields look the same with icon and rounded corners
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PrettyField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = PrimaryGreen
            )
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryGreen,
            focusedLabelColor = PrimaryGreen
        )
    )
}
