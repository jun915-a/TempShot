package com.aj.tempshot.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.aj.tempshot.domain.model.Image
import com.aj.tempshot.ui.composables.ActionButtons
import com.aj.tempshot.ui.composables.ExpiryDurationButtons
import com.aj.tempshot.ui.composables.MemoInput
import com.aj.tempshot.ui.composables.SwipeCard
import com.aj.tempshot.viewmodel.MainViewModel

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val currentImage by viewModel.currentImage.collectAsStateWithLifecycle()
    val memo by viewModel.memo.collectAsStateWithLifecycle()
    val unorganizedCount by viewModel.unorganizedCount.collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "TempShot",
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "整理中: $unorganizedCount 件",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            if (currentImage == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "すべての画像が整理されました！",
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                SwipeCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    onSwipeRight = {
                        viewModel.markAsOrganized()
                    },
                    onSwipeLeft = {
                        viewModel.markAsTemporary(viewModel.defaultExpiryDays)
                    },
                    onSwipeDown = {
                        viewModel.deleteImage()
                    }
                ) {
                    currentImage?.let { image ->
                        if (image.imagePath.startsWith("test_image_")) {
                            TestImagePlaceholder(imagePath = image.imagePath)
                        } else {
                            AsyncImage(
                                model = image.imagePath,
                                contentDescription = "Current Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                MemoInput(
                    value = memo,
                    onValueChange = { viewModel.updateMemo(it) },
                    modifier = Modifier.fillMaxWidth()
                )

                ActionButtons(
                    onOrganize = {
                        viewModel.markAsOrganized()
                    },
                    onTemporary = {
                        viewModel.markAsTemporary(viewModel.defaultExpiryDays)
                    },
                    onDelete = {
                        viewModel.deleteImage()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }

    }
}

@Composable
fun TestImagePlaceholder(imagePath: String) {
    val colors = listOf(
        Color(0xFF6200EE),
        Color(0xFF03DAC6),
        Color(0xFFFFA726),
        Color(0xFFEF5350)
    )
    val colorIndex = imagePath.takeLast(1).toIntOrNull()?.rem(colors.size) ?: 0

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors[colorIndex]),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "テスト画像",
                style = MaterialTheme.typography.displaySmall,
                color = Color.White
            )
            Text(
                text = imagePath,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
