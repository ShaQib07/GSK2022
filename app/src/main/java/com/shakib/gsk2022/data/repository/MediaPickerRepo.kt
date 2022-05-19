package com.shakib.gsk2022.data.repository

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.shakib.gsk2022.data.model.Image
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class MediaPickerRepo @Inject constructor(@ApplicationContext private val context: Context) {
    suspend fun fetchAllImages(): Flow<MutableList<Image>> {
        return withContext(Dispatchers.IO) {
            try {
                val galleryImages = mutableListOf<Image>()
                val columns =
                    arrayOf(
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.DATA
                    )
                val orderBy = MediaStore.Images.Media.DATE_TAKEN

                context.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                    null, null, "$orderBy DESC"
                )?.use { cursor ->
                    while (cursor.moveToNext()) {
                        val uri = ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                        )
                        val title =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                        val path =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                        val image = Image(uri, title, path)
                        Timber.d("XYZ | uri - $uri | title - $title | path - $path")
                        galleryImages.add(image)
                    }
                    flow { emit(galleryImages) }
                }
                    ?: flow { emit(mutableListOf()) }
            } catch (e: Exception) {
                Timber.e("XYZ | Error - ${e.message}")
                flow { emit(mutableListOf()) }
            }
        }
    }
}