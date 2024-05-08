package com.objectorientedoleg.githubrepos.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.shimmer(
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
): Modifier = composed {
    val shimmerColors = remember {
        listOf(
            Color.White.copy(alpha = 0.3f),
            Color.White.copy(alpha = 0.5f),
            Color.White.copy(alpha = 1.0f),
            Color.White.copy(alpha = 0.5f),
            Color.White.copy(alpha = 0.3f),
        )
    }
    val transition = rememberInfiniteTransition(label = "transition")
    val animation = transition.animateFloat(
        initialValue = 0f,
        targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "animation",
    )
    background(
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(x = animation.value - widthOfShadowBrush, y = 0.0f),
            end = Offset(x = animation.value, y = angleOfAxisY),
        ),
    )
}