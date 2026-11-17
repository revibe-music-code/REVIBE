/*
 * Copyright (C) 2026 REVIBE contributors
 *
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.dd3boh.outertune.ui.theme

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import com.dd3boh.outertune.constants.AnimationsKey
import com.dd3boh.outertune.constants.RevibeMicroMotionKey
import com.dd3boh.outertune.constants.RevibePlayerMotionKey
import com.dd3boh.outertune.constants.RevibeReduceMotionKey
import com.dd3boh.outertune.constants.RevibeTransitionsKey
import com.dd3boh.outertune.constants.RevibeWelcomeMotionKey
import com.dd3boh.outertune.ui.utils.Animations
import com.dd3boh.outertune.utils.rememberPreference

/**
 * REVIBE's motion, in one place.
 *
 * [Animations] stays the one master rule: Android's Remove animations and the app's own Animations
 * switch still stop every movement. What this adds is a second, finer layer, read from the six rows
 * of "Animações e efeitos", so a listener can keep navigation fades but drop decorative player
 * movement, or keep the presentation but ask for shorter, quieter motion everywhere.
 *
 * Nothing here starts a clock of its own. Every duration is a normal Compose [AnimationSpec], so
 * the app's [Animations.recomposer] paces it, and [Animations.off] turns it into an instant change.
 */
data class RevibeMotionConfig(
    val general: Boolean,
    val transitions: Boolean,
    val micro: Boolean,
    val player: Boolean,
    val welcome: Boolean,
    val reduce: Boolean,
) {
    /** Master rule plus the general row. False means "change state at once", never "lose a feature". */
    val allowed: Boolean get() = general && !Animations.off

    val transitionsEnabled: Boolean get() = allowed && transitions
    val microEnabled: Boolean get() = allowed && micro
    val playerEnabled: Boolean get() = allowed && player
    val welcomeEnabled: Boolean get() = allowed && welcome

    /** Reduced movement keeps feedback, but nothing decorative and nothing that travels far. */
    val reduced: Boolean get() = allowed && reduce

    /** Duration of one movement, shortened rather than removed when movement is reduced. */
    fun duration(base: Int): Int = when {
        !allowed -> 0
        reduce -> (base * 0.4f).toInt().coerceAtLeast(1)
        else -> base
    }

    /** The shared tween. Duration comes from here so no screen has its own idea of "smooth". */
    fun <T> spec(base: Int, easing: Easing = FastOutSlowInEasing): FiniteAnimationSpec<T> =
        tween(durationMillis = duration(base), easing = easing)

    fun navSpec(): FiniteAnimationSpec<IntOffset> = spec(RevibeTokens.Motion.NORMAL)

    fun fadeSpec(): FiniteAnimationSpec<Float> = spec(RevibeTokens.Motion.FAST)

    /** With screen transitions off there is no fade either: the new screen is simply there. */
    fun screenFadeSpec(): FiniteAnimationSpec<Float> = if (transitionsEnabled) fadeSpec() else spec(0)
}

/** The motion settings as one value, so the rows and everything they govern read the same truth. */
@Composable
fun rememberRevibeMotionConfig(): RevibeMotionConfig {
    val (general, _) = rememberPreference(AnimationsKey, defaultValue = Animations.SWITCH_DEFAULT)
    val (transitions, _) = rememberPreference(RevibeTransitionsKey, defaultValue = true)
    val (micro, _) = rememberPreference(RevibeMicroMotionKey, defaultValue = true)
    val (player, _) = rememberPreference(RevibePlayerMotionKey, defaultValue = true)
    val (welcome, _) = rememberPreference(RevibeWelcomeMotionKey, defaultValue = true)
    val (reduce, _) = rememberPreference(RevibeReduceMotionKey, defaultValue = false)

    return remember(general, transitions, micro, player, welcome, reduce) {
        RevibeMotionConfig(
            general = general,
            transitions = transitions,
            micro = micro,
            player = player,
            welcome = welcome,
            reduce = reduce,
        )
    }
}

/** Every screen under [OuterTuneTheme] sees the same motion rules. */
val LocalRevibeMotion = compositionLocalOf {
    RevibeMotionConfig(
        general = true,
        transitions = true,
        micro = true,
        player = true,
        welcome = true,
        reduce = false,
    )
}

/**
 * Feedback for a press, driven by the interaction source the clickable already owns. No separate
 * gesture detector, so it cannot swallow a tap or change what the control does.
 */
@Composable
fun Modifier.revibePress(
    source: MutableInteractionSource,
    enabled: Boolean = true,
    config: RevibeMotionConfig = LocalRevibeMotion.current,
    scaleDown: Float = 0.96f,
): Modifier {
    val pressed by source.collectIsPressedAsState()
    val target = if (enabled && pressed && config.microEnabled) scaleDown else 1f
    val scale by animateFloatAsState(
        targetValue = target,
        animationSpec = config.spec(RevibeTokens.Motion.FAST),
        label = "revibePress",
    )
    return this.graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
}

/** A clickable with the REVIBE press built in. Used where we own the clickable. */
@Composable
fun Modifier.revibeClickable(
    onClick: () -> Unit,
    enabled: Boolean = true,
    config: RevibeMotionConfig = LocalRevibeMotion.current,
    scaleDown: Float = 0.96f,
): Modifier {
    val source = remember { MutableInteractionSource() }
    return this
        .clickable(
            interactionSource = source,
            indication = null,
            enabled = enabled,
            onClick = onClick,
        )
        .revibePress(source = source, enabled = enabled, config = config, scaleDown = scaleDown)
}

/**
 * A small, one-shot entrance for content that has just appeared: cards, result rows and empty
 * states. It runs once per composition of the item, never on every frame and never in a loop.
 */
@Composable
fun Modifier.revibeEnter(
    index: Int = 0,
    config: RevibeMotionConfig = LocalRevibeMotion.current,
): Modifier {
    val shouldMove = config.microEnabled
    val shown = remember { mutableStateOf(!shouldMove) }
    LaunchedEffect(shouldMove) {
        if (shouldMove) shown.value = true
    }
    val progress by animateFloatAsState(
        targetValue = if (shown.value) 1f else 0f,
        animationSpec = config.spec(RevibeTokens.Motion.NORMAL + (index.coerceAtMost(6) * 20)),
        label = "revibeEnter",
    )
    return this.graphicsLayer {
        alpha = progress
        translationY = if (config.reduced) 0f else (1f - progress) * 10f
    }
}

/**
 * One step of the welcome presentation: fade and a little scale or rise, then still. [visible]
 * comes from the presentation's stages; with animations off, or reduced, the content is simply
 * there.
 */
@Composable
fun Modifier.revibeReveal(
    visible: Boolean,
    config: RevibeMotionConfig = LocalRevibeMotion.current,
    delayExtra: Int = 0,
): Modifier {
    val progress by animateFloatAsState(
        targetValue = if (!config.welcomeEnabled || !config.allowed || visible) 1f else 0f,
        animationSpec = config.spec(RevibeTokens.Motion.NORMAL + delayExtra),
        label = "revibeReveal",
    )
    return this.graphicsLayer {
        alpha = progress
        val scale = if (config.reduced) 1f else 0.95f + 0.05f * progress
        scaleX = scale
        scaleY = scale
        translationY = if (config.reduced) 0f else (1f - progress) * 12f
    }
}
