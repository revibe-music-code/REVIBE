/*
 * Copyright (C) 2026 InterTune
 * Copyright (C) 2026 REVIBE contributors
 *
 * SPDX-License-Identifier: GPL-3.0
 */

package com.dd3boh.outertune.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dd3boh.outertune.LocalNetworkConnected
import com.dd3boh.outertune.R
import com.dd3boh.outertune.ui.theme.RevibeTokens
import com.dd3boh.outertune.ui.theme.RevibeTypography

/**
 * In place of a page's loading shimmer once its request has failed. Kept as a single reusable
 * component so every error screen shares the REVIBE language, while retry still calls [onRetry].
 */
@Composable
fun LoadFailed(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val connected = LocalNetworkConnected.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
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
            Icon(
                imageVector = Icons.Rounded.CloudOff,
                contentDescription = null,
                tint = RevibeTokens.AccentBright,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(Modifier.height(18.dp))
        Text(
            text = stringResource(if (connected) R.string.error_unknown else R.string.error_no_internet),
            style = RevibeTypography.titleMedium,
            color = RevibeTokens.TextPrimary,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .clip(RevibeTokens.Shapes.Chip)
                .background(RevibeTokens.Accent.copy(alpha = 0.16f))
                .border(1.dp, RevibeTokens.Accent.copy(alpha = 0.34f), RevibeTokens.Shapes.Chip)
                .clickable(onClick = onRetry)
                .padding(horizontal = 24.dp, vertical = 10.dp),
        ) {
            Text(
                text = stringResource(R.string.retry),
                style = RevibeTypography.labelLarge,
                color = RevibeTokens.AccentBright,
            )
        }
    }
}
