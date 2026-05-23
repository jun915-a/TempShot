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
import androidx.compose.runtime.LaunchedEffect
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
import com.aj.tempshot.viewmodel.SwipeAction
import kotlin.math.abs

@Composable
fun SwipeCard(
    modifier: Modifier = Modifier,
    onSwipeRight: () -> Unit,
    onSwipeLeft: () -> Unit,
    onSwipeDown: () -> Unit,
    swipeAction: SwipeAction? = null,
    content: @Composable () -> Unit
) {
    var dragOffsetX by remember { mutableFloatStateOf(0f) }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }
    var animationOffsetX by remember { mutableFloatStateOf(0f) }
    var animationOffsetY by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(swipeAction) {
        if (swipeAction != null) {
            animationOffsetX = when (swipeAction) {
                SwipeAction.RIGHT -> 500f
                SwipeAction.LEFT -> -500f
                SwipeAction.DOWN -> 0f
            }
            animationOffsetY = when (swipeAction) {
                SwipeAction.DOWN -> 500f
                else -> 0f
            }
        }
    }

    val animatedOffsetX by animateDpAsState(targetValue = (animationOffsetX / 10).dp)
    val animatedOffsetY by animateDpAsState(targetValue = (animationOffsetY / 10).dp)
    val rotation by animateFloatAsState(targetValue = animationOffsetX / 25)
    val alpha by animateFloatAsState(
        targetValue = (1f - (abs(animationOffsetX) / 1000f)).coerceIn(0f, 1f)
    )
    val scale by animateFloatAsState(
        targetValue = (1f - (abs(animationOffsetX) / 3000f)).coerceIn(0.8f, 1f)
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
                        dragOffsetX += dragAmount.x * 1.5f
                        dragOffsetY += dragAmount.y * 1.5f
                    },
                    onDragEnd = {
                        val threshold = 150f
                        when {
                            abs(dragOffsetX) > threshold && abs(dragOffsetX) > abs(dragOffsetY) -> {
                                if (dragOffsetX > 0) {
                                    onSwipeRight()
                                } else {
                                    onSwipeLeft()
                                }
                            }
                            dragOffsetY > threshold -> {
                                onSwipeDown()
                            }
                            else -> {
                                animationOffsetX = 0f
                                animationOffsetY = 0f
                            }
                        }
                        dragOffsetX = 0f
                        dragOffsetY = 0f
                    }
                )
            }
            .background(Color.White, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        content()

        val threshold = 100f
        if (dragOffsetX > threshold) {
            SwipeLabel(
                text = "整理済み ✓",
                color = Color(0xFF4CAF50),
                alpha = (dragOffsetX - threshold) / 100f
            )
        } else if (dragOffsetX < -threshold) {
            SwipeLabel(
                text = "3日後に削除 ⏳",
                color = Color(0xFFFFA726),
                alpha = (abs(dragOffsetX) - threshold) / 100f
            )
        } else if (dragOffsetY > threshold) {
            SwipeLabel(
                text = "削除 🗑️",
                color = Color(0xFFEF5350),
                alpha = (dragOffsetY - threshold) / 100f
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
