package com.example.camera.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileNotFoundException

fun uriToBitmap(imageUri: Uri, contentResolver: ContentResolver): Bitmap {
    val bitmap = when {
        Build.VERSION.SDK_INT < 28 -> {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
        }
        else -> {
            val source = ImageDecoder.createSource(contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        }
    }
    return bitmap
}

fun saveImageToStorage(
    imageUri: Uri,
    contentResolver: ContentResolver,
    name: String = "IMG_${SystemClock.uptimeMillis()}"
): Uri {
    val bitmap = uriToBitmap(imageUri, contentResolver)

    val values = ContentValues()
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    values.put(MediaStore.Images.Media.DISPLAY_NAME, name)

    val savedImageUri =
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    val outputStream = savedImageUri?.let { contentResolver.openOutputStream(it) }
        ?: throw FileNotFoundException()

    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream.close()

    return savedImageUri
}

fun getFileFromUri(context: Context, uri: Uri): File? {
    val inputStream = context.contentResolver.openInputStream(uri) ?: return null
    val tempFile = File.createTempFile("temp_", ".jpg", context.cacheDir)
    tempFile.outputStream().use { inputStream.copyTo(it) }
    return tempFile
}

fun getImageMultipartBody(context: Context, uri: Uri, partName: String): MultipartBody.Part? {
    val file = getFileFromUri(context, uri) ?: return null
    val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
    return MultipartBody.Part.createFormData(partName, file.name, requestFile)
}