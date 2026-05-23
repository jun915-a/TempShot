package com.aj.tempshot.domain.model

data class Image(
    val imagePath: String,
    val memo: String = "",
    val expiryDate: Long? = null,
    val isOrganized: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
