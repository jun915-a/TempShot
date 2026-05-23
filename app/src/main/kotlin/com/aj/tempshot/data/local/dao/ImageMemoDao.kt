package com.aj.tempshot.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aj.tempshot.data.local.entity.ImageMemoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageMemoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ImageMemoEntity)

    @Update
    suspend fun update(entity: ImageMemoEntity)

    @Delete
    suspend fun delete(entity: ImageMemoEntity)

    @Query("SELECT * FROM image_memo WHERE imagePath = :imagePath")
    suspend fun getByPath(imagePath: String): ImageMemoEntity?

    @Query("SELECT * FROM image_memo WHERE isOrganized = 0 ORDER BY createdAt DESC LIMIT 1")
    fun getNextUnorganized(): Flow<ImageMemoEntity?>

    @Query("SELECT * FROM image_memo WHERE isOrganized = 1 ORDER BY createdAt DESC")
    fun getAllOrganized(): Flow<List<ImageMemoEntity>>

    @Query("SELECT * FROM image_memo WHERE memo LIKE '%' || :keyword || '%' ORDER BY createdAt DESC")
    fun searchByMemo(keyword: String): Flow<List<ImageMemoEntity>>

    @Query("SELECT * FROM image_memo WHERE expiryDate IS NOT NULL AND expiryDate < :currentTime AND isOrganized = 0")
    suspend fun getExpiredImages(currentTime: Long): List<ImageMemoEntity>

    @Query("DELETE FROM image_memo WHERE imagePath IN (:paths)")
    suspend fun deleteByPaths(paths: List<String>)

    @Query("SELECT COUNT(*) FROM image_memo WHERE isOrganized = 0")
    fun getUnorganizedCount(): Flow<Int>

    @Query("SELECT * FROM image_memo")
    suspend fun getAll(): List<ImageMemoEntity>
}
