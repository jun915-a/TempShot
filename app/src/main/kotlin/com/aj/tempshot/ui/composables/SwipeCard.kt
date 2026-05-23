package com.aj.tempshot.ui.composables

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Composable
fun SwipeCard(
    modifier: Modifier = Modifier,
    onSwipeRight: () -> Unit,
    onSwipeLeft: () -> Unit,
    onSwipeDown: () -> Unit,
    content: @Composable () -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableFloatStateOf(0f) }

    val animatedOffsetX by animateDpAsState(targetValue = (offsetX / 10).dp)
    val animatedOffsetY by animateDpAsState(targetValue = (offsetY / 10).dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .offset(x = animatedOffsetX, y = animatedOffsetY)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                        isDragging = 1f
                    },
                    onDragEnd = {
                        when {
                            abs(offsetX) > 200 -> {
                                if (offsetX > 0) {
                                    onSwipeRight()
                                } else {
                                    onSwipeLeft()
                                }
                            }
                            offsetY > 200 -> {
                                onSwipeDown()
                            }
                        }
                        offsetX = 0f
                        offsetY = 0f
                        isDragging = 0f
                    }
                )
            }
            .background(Color.White, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
