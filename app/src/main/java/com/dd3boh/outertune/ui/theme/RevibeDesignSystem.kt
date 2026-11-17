/*
 * Copyright (C) 2026 REVIBE contributors
 *
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.dd3boh.outertune.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * REVIBE's design tokens.
 *
 * These are deliberately independent from the upstream app's material defaults: a dark canvas,
 * restrained violet accent, quiet separators and generous type. Screens keep their existing
 * behaviour and callbacks; only the visual language is centralised here.
 */
object RevibeTokens {
    val Accent = Color(0xFFB69CFF)
    val AccentBright = Color(0xFFD6C7FF)
    val AccentDeep = Color(0xFF6E56CF)
    val AccentInk = Color(0xFF1A1230)

    val Canvas = Color(0xFF08080B)
    val Surface = Color(0xFF0E0E13)
    val SurfaceRaised = Color(0xFF15151C)
    val SurfaceOverlay = Color(0xFF1C1C25)
    val OutlineSoft = Color(0xFF2A2A35)

    val TextPrimary = Color(0xFFF6F3FF)
    val TextSecondary = Color(0xFFB7B2C7)
    val TextMuted = Color(0xFF7E798D)

    val Success = Color(0xFF7FD1A6)
    val Warning = Color(0xFFE7B980)
    val Danger = Color(0xFFF2838F)

    object Shapes {
        val Chip = RoundedCornerShape(50)
        val Small = RoundedCornerShape(12.dp)
        val Button = RoundedCornerShape(18.dp)
        val Card = RoundedCornerShape(24.dp)
        val Panel = RoundedCornerShape(28.dp)
        val Artwork = RoundedCornerShape(22.dp)
        val Navigation = RoundedCornerShape(26.dp)
    }

    object Spacing {
        val XS = 4.dp
        val S = 8.dp
        val M = 12.dp
        val L = 18.dp
        val XL = 24.dp
    }

    object Motion {
        const val FAST = 180
        const val NORMAL = 300
        const val SLOW = 520
    }
}

/** REVIBE type scale: confident headings, readable body, deliberate tracking on small caps. */
val RevibeTypography = Typography(
    displayLarge = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold, fontSize = 54.sp, lineHeight = 60.sp, letterSpacing = (-1.5).sp),
    displayMedium = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold, fontSize = 44.sp, lineHeight = 50.sp, letterSpacing = (-1.0).sp),
    displaySmall = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold, fontSize = 36.sp, lineHeight = 42.sp, letterSpacing = (-0.5).sp),
    headlineLarge = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold, fontSize = 32.sp, lineHeight = 38.sp, letterSpacing = (-0.4).sp),
    headlineMedium = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.SemiBold, fontSize = 28.sp, lineHeight = 34.sp, letterSpacing = (-0.2).sp),
    headlineSmall = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.SemiBold, fontSize = 24.sp, lineHeight = 30.sp),
    titleLarge = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.SemiBold, fontSize = 21.sp, lineHeight = 27.sp),
    titleMedium = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.SemiBold, fontSize = 17.sp, lineHeight = 23.sp, letterSpacing = 0.1.sp),
    titleSmall = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Medium, fontSize = 15.sp, lineHeight = 21.sp, letterSpacing = 0.1.sp),
    bodyLarge = TextStyle(fontFamily = FontFamily.Default, fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontFamily = FontFamily.Default, fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall = TextStyle(fontFamily = FontFamily.Default, fontSize = 12.sp, lineHeight = 17.sp),
    labelLarge = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.3.sp),
    labelMedium = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp),
    labelSmall = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 15.sp, letterSpacing = 0.8.sp),
)

val RevibeShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp),
)

/** A compact waveform mark used wherever the brand needs to be recognised without a logo file. */
@Composable
fun RevibeWaveMark(
    modifier: Modifier = Modifier,
    color: Color = RevibeTokens.AccentBright,
    barCount: Int = 7,
) {
    androidx.compose.foundation.Canvas(modifier) {
        val barWidth = 3.dp.toPx()
        val gap = 3.dp.toPx()
        val totalWidth = barCount * barWidth + (barCount - 1) * gap
        val startX = (size.width - totalWidth) / 2f
        val heights = floatArrayOf(0.36f, 0.62f, 0.9f, 0.54f, 1f, 0.7f, 0.44f)
        for (i in 0 until barCount) {
            val barHeight = size.height * heights[i % heights.size]
            drawRoundRect(
                color = color,
                topLeft = Offset(startX + i * (barWidth + gap), (size.height - barHeight) / 2f),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(barWidth / 2f),
            )
        }
    }
}

/** The shared REVIBE surface. Cards across the app use one shape, one border and one padding. */
@Composable
fun RevibeCard(
    modifier: Modifier = Modifier,
    accent: Boolean = false,
    onClick: (() -> Unit)? = null,
    animateIn: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RevibeTokens.Shapes.Card
    val backgroundBrush = if (accent) {
        Brush.linearGradient(
            listOf(
                RevibeTokens.AccentDeep.copy(alpha = 0.34f),
                RevibeTokens.SurfaceRaised,
                RevibeTokens.Surface,
            )
        )
    } else {
        null
    }

    Box(
        modifier = modifier
            .then(if (animateIn) Modifier.revibeEnter() else Modifier)
            .clip(shape)
            .then(
                if (backgroundBrush != null) Modifier.background(backgroundBrush)
                else Modifier.background(RevibeTokens.SurfaceRaised)
            )
            .border(1.dp, Color.White.copy(alpha = 0.07f), shape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(RevibeTokens.Spacing.L)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(RevibeTokens.Spacing.S),
            content = content,
        )
    }
}

/** Section heading with a short REVIBE rule underneath. */
@Composable
fun RevibeSectionTitle(
    title: String,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = RevibeTypography.titleLarge,
                color = RevibeTokens.TextPrimary,
            )
            Spacer(Modifier.height(6.dp))
            Box(
                Modifier
                    .width(28.dp)
                    .height(2.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(RevibeTokens.Accent)
            )
        }
        if (action != null) action()
    }
}

/** Consistent empty state used by library, search and list screens. */
@Composable
fun RevibeEmptyState(
    icon: ImageVector?,
    title: String,
    message: String? = null,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    animateIn: Boolean = true,
) {
    Column(
        modifier = modifier
            .then(if (animateIn) Modifier.revibeEnter() else Modifier)
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RevibeTokens.Shapes.Card)
                .background(RevibeTokens.SurfaceRaised)
                .border(1.dp, Color.White.copy(alpha = 0.07f), RevibeTokens.Shapes.Card),
            contentAlignment = Alignment.Center,
        ) {
            if (icon != null) {
                Icon(icon, contentDescription = null, tint = RevibeTokens.AccentBright, modifier = Modifier.size(32.dp))
            } else {
                RevibeWaveMark(Modifier.size(34.dp))
            }
        }
        Spacer(Modifier.height(18.dp))
        Text(title, style = RevibeTypography.titleLarge, color = RevibeTokens.TextPrimary)
        if (message != null) {
            Spacer(Modifier.height(8.dp))
            Text(message, style = RevibeTypography.bodyMedium, color = RevibeTokens.TextSecondary)
        }
        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .clip(RevibeTokens.Shapes.Chip)
                    .background(RevibeTokens.Accent.copy(alpha = 0.18f))
                    .border(1.dp, RevibeTokens.Accent.copy(alpha = 0.35f), RevibeTokens.Shapes.Chip)
                    .clickable(onClick = onAction)
                    .padding(horizontal = 20.dp, vertical = 10.dp),
            ) {
                Text(actionLabel, style = RevibeTypography.labelLarge, color = RevibeTokens.AccentBright)
            }
        }
    }
}

/** Consistent loading state for pages whose data has not arrived yet. */
@Composable
fun RevibeLoadingState(
    label: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator(color = RevibeTokens.AccentBright, strokeWidth = 2.dp)
        Spacer(Modifier.height(16.dp))
        Text(label, style = RevibeTypography.bodyMedium, color = RevibeTokens.TextSecondary)
    }
}
