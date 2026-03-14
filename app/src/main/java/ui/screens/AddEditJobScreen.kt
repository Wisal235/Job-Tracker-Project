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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.wali235.jobtracker.data.model.JobEntity
import ca.wali235.jobtracker.ui.theme.LightBackground
import ca.wali235.jobtracker.ui.theme.PrimaryGreen
import ca.wali235.jobtracker.viewmodel.JobViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditJobScreen(
    viewModel: JobViewModel,
    onBackClick: () -> Unit
) {
    val clientName = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }
    val location = remember { mutableStateOf("") }
    val jobType = remember { mutableStateOf("") }
    val status = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Job") },
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

            OutlinedTextField(
                value = status.value,
                onValueChange = { status.value = it },
                label = { Text("Status") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Button(
                onClick = {
                    if (
                        clientName.value.isNotBlank() &&
                        phone.value.isNotBlank() &&
                        location.value.isNotBlank() &&
                        jobType.value.isNotBlank() &&
                        status.value.isNotBlank()
                    ) {
                        viewModel.addJob(
                            JobEntity(
                                clientName = clientName.value,
                                phone = phone.value,
                                location = location.value,
                                jobType = jobType.value,
                                status = status.value
                            )
                        )
                        onBackClick()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text("Save Job")
            }
        }
    }
}