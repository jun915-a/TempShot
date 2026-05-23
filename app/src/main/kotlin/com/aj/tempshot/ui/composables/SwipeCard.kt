package com.aj.tempshot.ui.composables

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    val animatedOffsetX by animateDpAsState(targetValue = (offsetX / 10).dp)
    val animatedOffsetY by animateDpAsState(targetValue = (offsetY / 10).dp)
    val rotation by animateFloatAsState(targetValue = offsetX / 25)
    val alpha by animateFloatAsState(
        targetValue = (1f - (abs(offsetX) / 1000f)).coerceIn(0f, 1f)
    )
    val scale by animateFloatAsState(
        targetValue = (1f - (abs(offsetX) / 3000f)).coerceIn(0.8f, 1f)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .offset(x = animatedOffsetX, y = animatedOffsetY)
            .scale(scale)
            .rotate(rotation)
            .alpha(alpha)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x * 1.5f
                        offsetY += dragAmount.y * 1.5f
                    },
                    onDragEnd = {
                        val threshold = 150f
                        when {
                            abs(offsetX) > threshold && abs(offsetX) > abs(offsetY) -> {
                                if (offsetX > 0) {
                                    onSwipeRight()
                                } else {
                                    onSwipeLeft()
                                }
                            }
                            offsetY > threshold -> {
                                onSwipeDown()
                            }
                        }
                        offsetX = 0f
                        offsetY = 0f
                    }
                )
            }
            .background(Color.White, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        content()

        val threshold = 100f
        if (offsetX > threshold) {
            SwipeLabel(
                text = "整理済み ✓",
                color = Color(0xFF4CAF50),
                alpha = (offsetX - threshold) / 100f
            )
        } else if (offsetX < -threshold) {
            SwipeLabel(
                text = "3日後に削除 ⏳",
                color = Color(0xFFFFA726),
                alpha = (abs(offsetX) - threshold) / 100f
            )
        } else if (offsetY > threshold) {
            SwipeLabel(
                text = "削除 🗑️",
                color = Color(0xFFEF5350),
                alpha = (offsetY - threshold) / 100f
            )
        }
    }
}

@Composable
private fun SwipeLabel(
    text: String,
    color: Color,
    alpha: Float
) {
    Text(
        text = text,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = color.copy(alpha = alpha.coerceIn(0f, 1f)),
        textAlign = TextAlign.Center,
        modifier = Modifier.alpha(alpha.coerceIn(0f, 1f))
    )
}
