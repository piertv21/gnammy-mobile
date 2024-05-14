package com.example.gnammy.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gnammy.data.database.Place
import com.example.gnammy.data.repositories.PlacesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class PlacesState(val places: List<Place>)

class PlacesViewModel(
    private val repository: PlacesRepository
) : ViewModel() {
    val state = repository.places.map { PlacesState(places = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = PlacesState(emptyList())
    )

    fun addPlace(place: Place) = viewModelScope.launch {
        repository.upsert(place)
    }

    fun deletePlace(place: Place) = viewModelScope.launch {
        repository.delete(place)
    }
}
