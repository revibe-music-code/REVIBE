/*
 * Copyright (C) 2026 REVIBE contributors
 *
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.dd3boh.outertune.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dd3boh.outertune.R
import com.dd3boh.outertune.ui.theme.LocalRevibeMotion
import com.dd3boh.outertune.ui.theme.RevibeTokens
import com.dd3boh.outertune.ui.theme.RevibeTypography
import com.dd3boh.outertune.ui.theme.RevibeWaveMark
import com.dd3boh.outertune.ui.theme.revibeClickable
import com.dd3boh.outertune.ui.theme.revibeReveal
import kotlinx.coroutines.delay

/**
 * The controller lives in the activity, because the presentation is a full-window overlay and not
 * a navigation destination: it must sit above the wizard and above every other dialog, and it must
 * not add a route that Back could land on. Settings holds this same controller to offer a replay.
 */
class RevibeWelcomeController internal constructor(internal val state: MutableState<Boolean>) {
    val showing: Boolean get() = state.value

    fun show() {
        state.value = true
    }

    fun dismiss() {
        state.value = false
    }
}

/** A quiet default for previews and tests that compose a row outside the activity. */
val LocalRevibeWelcome = compositionLocalOf<RevibeWelcomeController> {
    RevibeWelcomeController(mutableStateOf(false))
}

@Composable
fun rememberRevibeWelcomeController(): RevibeWelcomeController {
    val state = rememberSaveable { mutableStateOf(false) }
    return remember(state) { RevibeWelcomeController(state) }
}

/**
 * The first-open presentation.
 *
 * One short sequence of stages, then still: nothing repeats, nothing waits on a timer while the
 * listener is using the app, and every stage becomes a plain state when animations are off or
 * movement is reduced. The Continue button is available as soon as the first stage has landed, so
 * no animation is ever required to get into the app.
 */
@Composable
fun RevibeWelcomeScreen(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val motion = LocalRevibeMotion.current
    var stage by rememberSaveable { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        if (!motion.welcomeEnabled || !motion.allowed || motion.reduced) {
            stage = 4
        } else {
            delay(120)
            stage = 1
            delay(180)
            stage = 2
            delay(220)
            stage = 3
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            // Swallow taps meant for whatever is underneath: the wizard is already mounted behind
            // this overlay and must not answer until the presentation has been dismissed.
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {},
            )
            .background(RevibeTokens.Canvas)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        RevibeTokens.AccentDeep.copy(alpha = 0.22f),
                        Color.Transparent,
                    ),
                    radius = 900f,
                )
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                // Large system text, or a phone held sideways: the words still fit, the column
                // just scrolls, so the Continue button is never unreachable.
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(132.dp)
                    .clip(RevibeTokens.Shapes.Card)
                    .background(RevibeTokens.SurfaceOverlay.copy(alpha = 0.75f))
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RevibeTokens.Shapes.Card)
                    .revibeReveal(visible = stage >= 1, config = motion),
                contentAlignment = Alignment.Center,
            ) {
                RevibeWaveMark(modifier = Modifier.size(64.dp))
            }

            Spacer(Modifier.height(30.dp))

            Text(
                text = stringResource(R.string.revibe_name),
                style = RevibeTypography.displayMedium.copy(
                    color = RevibeTokens.TextPrimary,
                    letterSpacing = 10.sp,
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.revibeReveal(visible = stage >= 2, config = motion),
            )

            Spacer(Modifier.height(28.dp))

            Text(
                text = stringResource(R.string.revibe_welcome_title),
                style = RevibeTypography.headlineSmall.copy(color = RevibeTokens.AccentBright),
                textAlign = TextAlign.Center,
                modifier = Modifier.revibeReveal(
                    visible = stage >= 3,
                    config = motion,
                    delayExtra = 40,
                ),
            )

            Spacer(Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.revibe_welcome_subtitle),
                style = RevibeTypography.bodyLarge.copy(color = RevibeTokens.TextSecondary),
                textAlign = TextAlign.Center,
                modifier = Modifier.revibeReveal(
                    visible = stage >= 3,
                    config = motion,
                    delayExtra = 80,
                ),
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.revibe_welcome_thanks),
                style = RevibeTypography.bodyMedium.copy(color = RevibeTokens.TextMuted),
                textAlign = TextAlign.Center,
                modifier = Modifier.revibeReveal(
                    visible = stage >= 3,
                    config = motion,
                    delayExtra = 120,
                ),
            )

            Box(
                modifier = Modifier
                    .padding(top = 36.dp)
                    .clip(RevibeTokens.Shapes.Button)
                    .background(RevibeTokens.Accent)
                    .revibeReveal(
                        visible = stage >= 3,
                        config = motion,
                        delayExtra = 160,
                    )
                    .revibeClickable(onClick = onContinue, config = motion)
                    .padding(horizontal = 30.dp, vertical = 14.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.revibe_welcome_cta),
                    style = RevibeTypography.labelLarge.copy(color = RevibeTokens.AccentInk),
                )
            }
        }
    }
}
