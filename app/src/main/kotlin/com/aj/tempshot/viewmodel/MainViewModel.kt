package com.aj.tempshot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aj.tempshot.domain.model.Image
import com.aj.tempshot.domain.repository.IImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: IImageRepository
) : ViewModel() {

    private val _currentImage = MutableStateFlow<Image?>(null)
    val currentImage: StateFlow<Image?> = _currentImage.asStateFlow()

    private val _unorganizedCount = MutableStateFlow(0)
    val unorganizedCount: StateFlow<Int> = _unorganizedCount.asStateFlow()

    private val _memo = MutableStateFlow("")
    val memo: StateFlow<String> = _memo.asStateFlow()

    private val testMode = true // ダミー画像表示用
    val defaultExpiryDays = 3 // 一時保存のデフォルト期間（日数）

    init {
        viewModelScope.launch {
            if (testMode) {
                loadTestImages()
            } else {
                repository.registerScreenshotsIfNotExists()
                loadNextImage()
                observeUnorganizedCount()
            }
        }
    }

    private suspend fun loadTestImages() {
        val testImages = listOf(
            Image(
                imagePath = "test_image_1",
                memo = "サンプル画像 1",
                isOrganized = false
            ),
            Image(
                imagePath = "test_image_2",
                memo = "サンプル画像 2",
                isOrganized = false
            ),
            Image(
                imagePath = "test_image_3",
                memo = "サンプル画像 3",
                isOrganized = false
            )
        )
        testImages.forEach { repository.saveImage(it) }
        loadNextImage()
        observeUnorganizedCount()
    }

    private fun loadNextImage() {
        viewModelScope.launch {
            repository.getNextUnorganized().collect { image ->
                _currentImage.value = image
                _memo.value = image?.memo ?: ""
            }
        }
    }

    private fun observeUnorganizedCount() {
        viewModelScope.launch {
            repository.getUnorganizedCount().collect { count ->
                _unorganizedCount.value = count
            }
        }
    }

    fun updateMemo(text: String) {
        _memo.value = text
    }

    fun saveMemo() {
        val image = _currentImage.value ?: return
        viewModelScope.launch {
            repository.updateMemo(image.imagePath, _memo.value)
        }
    }

    fun markAsOrganized() {
        val image = _currentImage.value ?: return
        viewModelScope.launch {
            saveMemo()
            repository.markAsOrganized(image.imagePath)
            loadNextImage()
        }
    }

    fun markAsTemporary(expiryDays: Int) {
        val image = _currentImage.value ?: return
        viewModelScope.launch {
            saveMemo()
            repository.markAsTemporary(image.imagePath, expiryDays)
            loadNextImage()
        }
    }

    fun deleteImage() {
        val image = _currentImage.value ?: return
        viewModelScope.launch {
            repository.deleteImage(image.imagePath)
            loadNextImage()
        }
    }
}
