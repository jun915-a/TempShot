package com.aj.tempshot.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "image_memo")
data class ImageMemoEntity(
    @PrimaryKey
    val imagePath: String,
    val memo: String = "",
    val expiryDate: Long? = null,
    val isOrganized: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
