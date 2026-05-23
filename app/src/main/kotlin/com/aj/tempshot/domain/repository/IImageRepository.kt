package com.aj.tempshot.domain.repository

import com.aj.tempshot.domain.model.Image
import kotlinx.coroutines.flow.Flow

interface IImageRepository {
    suspend fun saveImage(image: Image)
    suspend fun updateMemo(imagePath: String, memo: String)
    suspend fun markAsOrganized(imagePath: String)
    suspend fun markAsTemporary(imagePath: String, expiryDays: Int)
    fun getNextUnorganized(): Flow<Image?>
    fun getAllOrganized(): Flow<List<Image>>
    fun searchByMemo(keyword: String): Flow<List<Image>>
    suspend fun deleteImage(imagePath: String)
    suspend fun getExpiredImages(): List<Image>
    fun getUnorganizedCount(): Flow<Int>
    suspend fun registerScreenshotsIfNotExists(): Int
}
