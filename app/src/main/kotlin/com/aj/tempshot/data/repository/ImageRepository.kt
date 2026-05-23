package com.aj.tempshot.data.repository

import com.aj.tempshot.data.local.ImageProvider
import com.aj.tempshot.data.local.dao.ImageMemoDao
import com.aj.tempshot.data.local.entity.ImageMemoEntity
import com.aj.tempshot.domain.model.Image
import com.aj.tempshot.domain.repository.IImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ImageRepository @Inject constructor(
    private val dao: ImageMemoDao,
    private val imageProvider: ImageProvider
) : IImageRepository {

    override suspend fun saveImage(image: Image) {
        dao.insert(image.toEntity())
    }

    override suspend fun updateMemo(imagePath: String, memo: String) {
        val entity = dao.getByPath(imagePath)
        entity?.let {
            dao.update(it.copy(memo = memo, updatedAt = System.currentTimeMillis()))
        }
    }

    override suspend fun markAsOrganized(imagePath: String) {
        val entity = dao.getByPath(imagePath)
        entity?.let {
            dao.update(it.copy(isOrganized = true, updatedAt = System.currentTimeMillis()))
        }
    }

    override suspend fun markAsTemporary(imagePath: String, expiryDays: Int) {
        val entity = dao.getByPath(imagePath) ?: ImageMemoEntity(imagePath = imagePath)
        val expiryTime = System.currentTimeMillis() + (expiryDays * 24 * 60 * 60 * 1000L)
        dao.insert(entity.copy(expiryDate = expiryTime, updatedAt = System.currentTimeMillis()))
    }

    override fun getNextUnorganized(): Flow<Image?> {
        return dao.getNextUnorganized().map { it?.toDomain() }
    }

    override fun getAllOrganized(): Flow<List<Image>> {
        return dao.getAllOrganized().map { list -> list.map { it.toDomain() } }
    }

    override fun searchByMemo(keyword: String): Flow<List<Image>> {
        return dao.searchByMemo(keyword).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun deleteImage(imagePath: String) {
        val entity = dao.getByPath(imagePath)
        entity?.let { dao.delete(it) }
    }

    override suspend fun getExpiredImages(): List<Image> {
        return dao.getExpiredImages(System.currentTimeMillis()).map { it.toDomain() }
    }

    override fun getUnorganizedCount(): Flow<Int> {
        return dao.getUnorganizedCount()
    }

    override suspend fun registerScreenshotsIfNotExists(): Int {
        val allImages = dao.getAll()
        val savedPaths = allImages.map { it.imagePath }.toSet()
        val newScreenshots = imageProvider.getScreenshotsNotInDatabase(savedPaths)

        newScreenshots.forEach { image ->
            dao.insert(image.toEntity())
        }

        return newScreenshots.size
    }

    private fun Image.toEntity() = ImageMemoEntity(
        imagePath = imagePath,
        memo = memo,
        expiryDate = expiryDate,
        isOrganized = isOrganized,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    private fun ImageMemoEntity.toDomain() = Image(
        imagePath = imagePath,
        memo = memo,
        expiryDate = expiryDate,
        isOrganized = isOrganized,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
