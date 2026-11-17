/*
 * Copyright (C) 2024 z-huang/InnerTune
 * Copyright (C) 2025 OuterTune Project
 * Copyright (C) 2026 REVIBE contributors
 *
 * SPDX-License-Identifier: GPL-3.0-only
 */
package com.dd3boh.outertune.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dd3boh.outertune.ui.theme.RevibeEmptyState
import com.dd3boh.outertune.ui.theme.RevibeTokens
import com.dd3boh.outertune.ui.theme.RevibeTypography

/**
 * Shared empty state. Both overloads now render the same REVIBE visual language: a quiet raised
 * tile for the glyph, a confident title and restrained secondary text. No behaviour is changed.
 */
@Composable
fun EmptyPlaceholder(
    @DrawableRes icon: Int,
    text: String,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 40.dp),
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RevibeTokens.Shapes.Card)
                .background(RevibeTokens.SurfaceRaised)
                .border(1.dp, Color.White.copy(alpha = 0.07f), RevibeTokens.Shapes.Card),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                colorFilter = ColorFilter.tint(RevibeTokens.AccentBright),
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(Modifier.height(18.dp))

        androidx.compose.material3.Text(
            text = text,
            style = RevibeTypography.titleMedium,
            color = RevibeTokens.TextPrimary,
        )
    }
}

@Composable
fun EmptyPlaceholder(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
) {
    RevibeEmptyState(
        icon = icon,
        title = text,
        modifier = modifier,
    )
}
