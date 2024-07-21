package com.example.gnammy.utils

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface

interface CameraLauncher {
    val capturedImageUri: Uri
    fun captureImage()
}

@Composable
fun rememberCameraLauncher(onPictureTaken: (imageUri: Uri) -> Unit = {}) : CameraLauncher {
    val ctx = LocalContext.current
    val imageUri = remember {
        val imageFile = File.createTempFile("tmp_image", ".jpg", ctx.externalCacheDir)
        FileProvider.getUriForFile(ctx, ctx.packageName + ".provider", imageFile)
    }
    var capturedImageUri by remember { mutableStateOf(Uri.EMPTY) }
    val cameraActivityLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { pictureTaken ->
            if (pictureTaken) {
                capturedImageUri = imageUri
                handleImageRotation(ctx, capturedImageUri)
                onPictureTaken(capturedImageUri)
            }
        }
    val cameraLauncher by remember {
        derivedStateOf {
            object : CameraLauncher {
                override val capturedImageUri = capturedImageUri
                override fun captureImage() = cameraActivityLauncher.launch((imageUri))
            }
        }
    }
    return cameraLauncher
}

private fun handleImageRotation(context: Context, imageUri: Uri) {
    val inputStream = context.contentResolver.openInputStream(imageUri)
    val exif = inputStream?.let { ExifInterface(it) }
    val orientation = exif?.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    )
    val bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(imageUri))
    val rotatedBitmap = when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
        else -> bitmap
    }
    val outputStream = context.contentResolver.openOutputStream(imageUri)
    if (outputStream != null) {
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    }
    outputStream?.close()
}

private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
}
