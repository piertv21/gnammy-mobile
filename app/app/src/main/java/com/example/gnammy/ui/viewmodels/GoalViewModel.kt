package com.example.gnammy.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gnammy.data.local.entities.GnamGoal
import com.example.gnammy.data.local.entities.UserGoal
import com.example.gnammy.data.repository.GoalRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class UserGoalsState(val goals: List<UserGoal> = emptyList())
data class GnamGoalsState(val goals: List<GnamGoal> = emptyList())

class GoalViewModel(private val repository: GoalRepository) : ViewModel() {

    val gnamGoalsState = repository.gnamGoals.map { GnamGoalsState(goals = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = GnamGoalsState(emptyList())
    )

    val userGoalsState = repository.userGoals.map { UserGoalsState(goals = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = UserGoalsState(emptyList())
    )

    var goalsPreview = listOf<UserGoal>()

    fun fetchGoalsNonBlocking(userId: String) {
        viewModelScope.launch {
            repository.fetchGoals(userId)
        }
    }

    fun getGoalsPreview(userId: String) {
        viewModelScope.launch {
            goalsPreview = run { repository.getGoalsPreview(userId) }
        }
    }
}
