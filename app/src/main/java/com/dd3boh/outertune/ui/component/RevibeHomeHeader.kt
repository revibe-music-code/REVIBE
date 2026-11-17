/*
 * Copyright (C) 2024 z-huang/InnerTune
 * Copyright (C) 2025 OuterTune Project
 * Copyright (C) 2026 REVIBE contributors
 *
 * SPDX-License-Identifier: GPL-3.0-only
 */
package com.dd3boh.outertune.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dd3boh.outertune.ui.theme.RevibeTokens
import com.dd3boh.outertune.ui.theme.RevibeTypography
import com.dd3boh.outertune.ui.theme.RevibeWaveMark

/**
 * REVIBE's home hero. The Home rows and their playback callbacks are untouched; this is only the
 * first card in the list, now built from the shared REVIBE design tokens instead of the upstream
 * header visual.
 */
@Composable
fun RevibeHomeHeader(modifier: Modifier = Modifier) {
    val shape = RevibeTokens.Shapes.Panel

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(
                        RevibeTokens.AccentDeep.copy(alpha = 0.42f),
                        RevibeTokens.SurfaceRaised,
                        RevibeTokens.Surface,
                    )
                )
            )
            .border(1.dp, Color.White.copy(alpha = 0.09f), shape)
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(RevibeTokens.Accent.copy(alpha = 0.16f))
                        .border(1.dp, RevibeTokens.Accent.copy(alpha = 0.30f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    RevibeWaveMark(Modifier.size(26.dp))
                }
                Spacer(Modifier.size(12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "REVIBE",
                        style = RevibeTypography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.4.sp,
                        color = RevibeTokens.TextPrimary,
                    )
                    Text(
                        text = "MUSIC, RECONNECTED",
                        style = RevibeTypography.labelSmall,
                        letterSpacing = 1.4.sp,
                        color = RevibeTokens.AccentBright,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Feel the music again.",
                    style = RevibeTypography.headlineMedium,
                    color = RevibeTokens.TextPrimary,
                )
                Text(
                    text = "Your listening, your rhythm.",
                    style = RevibeTypography.bodyMedium,
                    color = RevibeTokens.TextSecondary,
                )
            }

            Box(
                modifier = Modifier
                    .clip(RevibeTokens.Shapes.Chip)
                    .background(RevibeTokens.Accent.copy(alpha = 0.12f))
                    .border(1.dp, RevibeTokens.Accent.copy(alpha = 0.24f), RevibeTokens.Shapes.Chip)
                    .padding(horizontal = 12.dp, vertical = 5.dp)
            ) {
                Text(
                    text = "YOUR SPACE  ·  VOL. 01",
                    style = RevibeTypography.labelSmall,
                    letterSpacing = 1.1.sp,
                    color = RevibeTokens.AccentBright,
                )
            }
        }
    }
}
