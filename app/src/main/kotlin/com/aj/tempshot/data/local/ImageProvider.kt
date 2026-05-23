package com.aj.tempshot.data.local

import android.content.Context
import android.provider.MediaStore
import com.aj.tempshot.domain.model.Image
import javax.inject.Inject

class ImageProvider @Inject constructor(
    private val context: Context
) {
    fun getScreenshotsNotInDatabase(savedPaths: Set<String>): List<Image> {
        val images = mutableListOf<Image>()
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_TAKEN
        )

        val selection = "${MediaStore.Images.Media.DATA} LIKE ?"
        val selectionArgs = arrayOf("%Screenshots%")
        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        context.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)?.use { cursor ->
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

            while (cursor.moveToNext()) {
                val imagePath = cursor.getString(dataColumn)
                val dateTime = cursor.getLong(dateColumn)

                if (imagePath !in savedPaths) {
                    images.add(
                        Image(
                            imagePath = imagePath,
                            createdAt = if (dateTime > 0) dateTime else System.currentTimeMillis()
                        )
                    )
                }
            }
        }

        return images
    }

    fun getAllScreenshots(): List<Image> {
        val images = mutableListOf<Image>()
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_TAKEN
        )

        val selection = "${MediaStore.Images.Media.DATA} LIKE ?"
        val selectionArgs = arrayOf("%Screenshots%")
        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        context.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)?.use { cursor ->
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

            while (cursor.moveToNext()) {
                val imagePath = cursor.getString(dataColumn)
                val dateTime = cursor.getLong(dateColumn)

                images.add(
                    Image(
                        imagePath = imagePath,
                        createdAt = if (dateTime > 0) dateTime else System.currentTimeMillis()
                    )
                )
            }
        }

        return images
    }
}
