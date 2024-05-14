package com.example.gnammy.data.repositories

import android.content.ContentResolver
import android.net.Uri
import com.example.camera.utils.saveImageToStorage
import com.example.gnammy.data.database.Place
import com.example.gnammy.data.database.PlacesDAO
import kotlinx.coroutines.flow.Flow

class PlacesRepository(
    private val placesDAO: PlacesDAO,
    private val contentResolver: ContentResolver
) {
    val places: Flow<List<Place>> = placesDAO.getAll()

    suspend fun upsert(place: Place) {
        if (place.imageUri?.isNotEmpty() == true) {
            val imageUri = saveImageToStorage(
                Uri.parse(place.imageUri),
                contentResolver,
                "TravelDiary_Place${place.name}"
            )
            placesDAO.upsert(place.copy(imageUri = imageUri.toString()))
        } else {
            placesDAO.upsert(place)
        }
    }

    suspend fun delete(place: Place) = placesDAO.delete(place)
}
