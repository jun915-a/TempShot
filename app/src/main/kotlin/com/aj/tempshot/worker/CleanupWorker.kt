package com.aj.tempshot.worker

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.aj.tempshot.domain.repository.IImageRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File

@HiltWorker
class CleanupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: IImageRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val expiredImages = repository.getExpiredImages()

            expiredImages.forEach { image ->
                try {
                    deleteImageFile(image.imagePath)
                    repository.deleteImage(image.imagePath)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    private fun deleteImageFile(imagePath: String) {
        val file = File(imagePath)
        if (file.exists()) {
            file.delete()
        }

        val uri = Uri.parse(imagePath)
        if (uri.scheme == "content") {
            try {
                applicationContext.contentResolver.delete(uri, null, null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
