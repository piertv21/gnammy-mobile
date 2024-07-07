package com.example.gnammy.ui.screens.goals

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gnammy.ui.composables.GnamGoal
import com.example.gnammy.ui.composables.UserGoal
import com.example.gnammy.ui.viewmodels.GnamGoalsState
import com.example.gnammy.ui.viewmodels.GoalViewModel
import com.example.gnammy.ui.viewmodels.UserGoalsState
import com.example.gnammy.ui.viewmodels.UserViewModel
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GoalsScreen(
    goalsViewModel: GoalViewModel,
    userViewModel: UserViewModel
) {
    val userGoalsState: UserGoalsState by goalsViewModel.userGoalsState.collectAsStateWithLifecycle()
    val gnamGoalsState: GnamGoalsState by goalsViewModel.gnamGoalsState.collectAsStateWithLifecycle()

    runBlocking {
        val loggedUserId = userViewModel.getLoggedUserId()
        goalsViewModel.fetchGoals(loggedUserId)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        item {
            Text(
                text = "Utente",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            )
        }

        item {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                userGoalsState.goals.forEach { goal ->
                    UserGoal(goal)
                }
            }
        }

        item {
            Text(
                text = "Ricette",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 8.dp)
            )
        }

        item {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                gnamGoalsState.goals.forEach { goal ->
                    GnamGoal(goal)
                }
            }
        }
    }
}