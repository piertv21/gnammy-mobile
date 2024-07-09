package com.example.gnammy.ui.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gnammy.data.local.entities.Gnam
import com.example.gnammy.data.repository.GnamRepository
import com.example.gnammy.data.repository.LikedGnamRepository
import com.example.gnammy.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


data class GnamsState(val gnams: List<Gnam> = emptyList())

class GnamViewModel(
    private val repository: GnamRepository,
    private val likedGnamRepository: LikedGnamRepository
) : ViewModel() {

    val state = repository.gnams.map { GnamsState(gnams = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = GnamsState(emptyList())
    )

    val timelineState = repository.timeline.map { GnamsState(gnams = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = GnamsState(emptyList())
    )

    val likedGnamsState = likedGnamRepository.likedGnams.map { GnamsState(gnams = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = GnamsState(emptyList())
    )

    private val _gnamToBeFetched = MutableStateFlow<Gnam?>(null)
    val gnamToBeFetched: StateFlow<Gnam?> = _gnamToBeFetched.asStateFlow()

    fun fetchGnam(gnamId: String) {
        viewModelScope.launch {
            repository.fetchGnam(gnamId)
        }
    }

    fun fetchGnamTimeline() {
        viewModelScope.launch {
            repository.fetchGnamTimeline()
        }
    }

    fun removeFromTimeline(gnam: Gnam, liked: Boolean) {
        viewModelScope.launch {
            repository.removeFromTimeline(gnam, liked)
        }
    }

    suspend fun publishGnam(
        context: Context,
        currentUserId: String,
        title: String,
        shortDescription: String,
        ingredientsAndRecipe: String,
        imageUri: Uri
    ): Result<String> {
        return repository.publishGnam(
            context,
            currentUserId,
            title,
            shortDescription,
            ingredientsAndRecipe,
            imageUri
        )
    }

    fun syncSavedGnam(userId: String) {
        viewModelScope.launch {
            likedGnamRepository.syncSavedGnam(userId)
        }
    }

    fun removeGnamFromSaved(gnam: Gnam, loggedUserId: String) {
        viewModelScope.launch {
            likedGnamRepository.removeGnamFromSaved(gnam, loggedUserId)
        }
    }

    fun addCurrentUserGnams(userId: String) {
        viewModelScope.launch {
            repository.addCurrentUserGnams(userId)
        }
    }

    suspend fun getGnamData(gnamId: String) {
        _gnamToBeFetched.value = repository.getGnamData(gnamId)
    }
}
