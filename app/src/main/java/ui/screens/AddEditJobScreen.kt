package ca.wali235.jobtracker.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ca.wali235.jobtracker.data.model.JobEntity
import ca.wali235.jobtracker.ui.theme.LightBackground
import ca.wali235.jobtracker.ui.theme.PrimaryGreen
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

    val clientName = remember { mutableStateOf(existingJob?.clientName ?: "") }
    val phone = remember { mutableStateOf(existingJob?.phone ?: "") }
    val location = remember { mutableStateOf(existingJob?.location ?: "") }
    val jobType = remember { mutableStateOf(existingJob?.jobType ?: "") }
    val status = remember { mutableStateOf(existingJob?.status ?: "Lead") }
    val followUpDate = remember { mutableStateOf(existingJob?.followUpDate ?: "") }

    val statusOptions = listOf("Lead", "Quote", "Active", "Done")
    var statusExpanded by remember { mutableStateOf(false) }

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
                title = { Text(if (isEditing) "Edit Job" else "Add Job") },
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
            OutlinedTextField(
                value = clientName.value,
                onValueChange = { clientName.value = it },
                label = { Text("Client Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
            OutlinedTextField(
                value = phone.value,
                onValueChange = { phone.value = it },
                label = { Text("Phone") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
            OutlinedTextField(
                value = location.value,
                onValueChange = { location.value = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
            OutlinedTextField(
                value = jobType.value,
                onValueChange = { jobType.value = it },
                label = { Text("Job Type") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            // Status Dropdown
            ExposedDropdownMenuBox(
                expanded = statusExpanded,
                onExpandedChange = { statusExpanded = !statusExpanded }
            ) {
                OutlinedTextField(
                    value = status.value,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Status") },
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(16.dp)
                )
                ExposedDropdownMenu(
                    expanded = statusExpanded,
                    onDismissRequest = { statusExpanded = false }
                ) {
                    statusOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                status.value = option
                                statusExpanded = false
                            }
                        )
                    }
                }
            }

            // Follow Up Date Picker
            OutlinedTextField(
                value = followUpDate.value,
                onValueChange = {},
                readOnly = true,
                label = { Text("Follow Up Date") },
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

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
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text(if (isEditing) "Update Job" else "Save Job")
            }
        }
    }
}