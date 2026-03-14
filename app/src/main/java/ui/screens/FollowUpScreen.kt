package ca.wali235.jobtracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.wali235.jobtracker.ui.theme.LightBackground
import ca.wali235.jobtracker.viewmodel.JobViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowUpScreen(
    viewModel: JobViewModel,
    onBackClick: () -> Unit
) {
    val followUps = remember {
        listOf("Hassan - Today", "Ahmed - Tomorrow", "Omar - Feb 24")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Follow Up") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
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
            items(followUps) { item ->
                Card {
                    Text(
                        text = item,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}