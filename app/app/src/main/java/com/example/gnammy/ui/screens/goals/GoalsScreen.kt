package com.example.gnammy.ui.screens.goals

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gnammy.R
import com.example.gnammy.ui.composables.GnamGoal
import com.example.gnammy.ui.composables.UserGoal
import com.example.gnammy.ui.viewmodels.GnamGoalsState
import com.example.gnammy.ui.viewmodels.GoalViewModel
import com.example.gnammy.ui.viewmodels.UserGoalsState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GoalsScreen(
    goalsViewModel: GoalViewModel
) {
    var isLoadingUserGoals by remember { mutableStateOf(true) }
    var isLoadingGnamGoals by remember { mutableStateOf(true) }

    val userGoalsState: UserGoalsState by goalsViewModel.userGoalsState.collectAsStateWithLifecycle()
    val gnamGoalsState: GnamGoalsState by goalsViewModel.gnamGoalsState.collectAsStateWithLifecycle()

    LaunchedEffect(userGoalsState.goals.isNotEmpty()) {
        isLoadingUserGoals = false
    }

    LaunchedEffect(gnamGoalsState.goals.isNotEmpty()) {
        isLoadingGnamGoals = false
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            Text(
                text = stringResource(R.string.goals_users),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        }
        item {
            if (isLoadingUserGoals) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                if (userGoalsState.goals.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Nessun obiettivo utente raggiunto.")
                    }
                } else {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        userGoalsState.goals.forEach { goal ->
                            UserGoal(
                                goal,
                                Modifier
                                    .fillMaxWidth(1 / 2f)
                                    .aspectRatio(2f)
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = stringResource(R.string.goals_gnam),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        item {
            if (isLoadingGnamGoals) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                if (gnamGoalsState.goals.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Nessun obiettivo gnam raggiunto.")
                    }
                } else {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    ) {
                        gnamGoalsState.goals.forEach { goal ->
                            GnamGoal(goal)
                        }
                    }
                }
            }
        }
    }
}
