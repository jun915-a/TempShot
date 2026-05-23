package com.aj.tempshot.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.aj.tempshot.ui.composables.ActionButtons
import com.aj.tempshot.ui.composables.MemoInput
import com.aj.tempshot.ui.composables.SwipeCard
import com.aj.tempshot.viewmodel.MainViewModel

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val currentImage by viewModel.currentImage.collectAsStateWithLifecycle()
    val memo by viewModel.memo.collectAsStateWithLifecycle()
    val unorganizedCount by viewModel.unorganizedCount.collectAsStateWithLifecycle()
    val swipeAction by viewModel.swipeAction.collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = androidx.compose.foundation.background(
                        color = Color(0xFFFAFAFE)
                    ).brush
                ),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "TempShot",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Card(
                    modifier = Modifier
                        .background(Color.Transparent)
                        .padding(horizontal = 12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "整理中: $unorganizedCount 件",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(12.dp, 8.dp)
                    )
                }
            }

            if (currentImage == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✨ すべての画像が整理されました！",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(420.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 8.dp
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        SwipeCard(
                            modifier = Modifier.fillMaxSize(),
                            onSwipeRight = {
                                viewModel.markAsOrganized()
                            },
                            onSwipeLeft = {
                                viewModel.markAsTemporary(viewModel.defaultExpiryDays)
                            },
                            onSwipeDown = {
                                viewModel.deleteImage()
                            },
                            swipeAction = swipeAction
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
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        MemoInput(
                            value = memo,
                            onValueChange = { viewModel.updateMemo(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            }

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
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun TestImagePlaceholder(imagePath: String) {
    val colors = listOf(
        Color(0xFF5E5CE6),
        Color(0xFF03DAC6),
        Color(0xFFFF6B9D),
        Color(0xFFFFA726)
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
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "📸",
                fontSize = 64.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "テスト画像",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = imagePath,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}
