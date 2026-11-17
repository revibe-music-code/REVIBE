/*
 * Copyright (C) 2026 REVIBE contributors
 *
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.dd3boh.outertune.ui.screens.settings.fragments

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessibilityNew
import androidx.compose.material.icons.rounded.Animation
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material.icons.rounded.SwipeLeft
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material.icons.rounded.WavingHand
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dd3boh.outertune.R
import com.dd3boh.outertune.constants.AnimationsKey
import com.dd3boh.outertune.constants.RevibeMicroMotionKey
import com.dd3boh.outertune.constants.RevibePlayerMotionKey
import com.dd3boh.outertune.constants.RevibeReduceMotionKey
import com.dd3boh.outertune.constants.RevibeTransitionsKey
import com.dd3boh.outertune.constants.RevibeWelcomeMotionKey
import com.dd3boh.outertune.ui.component.PreferenceEntry
import com.dd3boh.outertune.ui.component.SwitchPreference
import com.dd3boh.outertune.ui.screens.LocalRevibeWelcome
import com.dd3boh.outertune.ui.screens.settings.settingRow
import com.dd3boh.outertune.ui.utils.Animations
import com.dd3boh.outertune.utils.rememberPreference

/**
 * "Animações e efeitos". Six persistent choices and one action.
 *
 * The general row writes the same key as the older Animations switch in Look and feel, so there is
 * still one master rule (Animations): Android's Remove animations, or this row being off, stops all
 * movement everywhere. The other rows only decide which movements are worth keeping.
 */
@Composable
fun RevibeAnimationsFrag() {
    val (animations, onAnimationsChange) = rememberPreference(AnimationsKey, defaultValue = Animations.SWITCH_DEFAULT)
    val (transitions, onTransitionsChange) = rememberPreference(RevibeTransitionsKey, defaultValue = true)
    val (micro, onMicroChange) = rememberPreference(RevibeMicroMotionKey, defaultValue = true)
    val (player, onPlayerChange) = rememberPreference(RevibePlayerMotionKey, defaultValue = true)
    val (welcome, onWelcomeChange) = rememberPreference(RevibeWelcomeMotionKey, defaultValue = true)
    val (reduce, onReduceChange) = rememberPreference(RevibeReduceMotionKey, defaultValue = false)

    // Read here, not inside the click lambda: reading a CompositionLocal is a composable call.
    val welcomeController = LocalRevibeWelcome.current

    // Android's own setting cannot be undone from inside the app. Keep the choice that was made,
    // show the row held off, and never pretend a tap changed anything.
    val heldOff = Animations.offInSystem

    SwitchPreference(
        modifier = Modifier.settingRow(R.string.revibe_animations_general_title),
        title = { Text(stringResource(R.string.revibe_animations_general_title)) },
        description = stringResource(R.string.revibe_animations_general_description),
        icon = { Icon(Icons.Rounded.Animation, null) },
        checked = animations && !heldOff,
        isEnabled = !heldOff,
        onCheckedChange = onAnimationsChange,
    )

    SwitchPreference(
        modifier = Modifier.settingRow(R.string.revibe_animations_transitions_title),
        title = { Text(stringResource(R.string.revibe_animations_transitions_title)) },
        description = stringResource(R.string.revibe_animations_transitions_description),
        icon = { Icon(Icons.Rounded.SwipeLeft, null) },
        checked = transitions,
        onCheckedChange = onTransitionsChange,
    )

    SwitchPreference(
        modifier = Modifier.settingRow(R.string.revibe_animations_micro_title),
        title = { Text(stringResource(R.string.revibe_animations_micro_title)) },
        description = stringResource(R.string.revibe_animations_micro_description),
        icon = { Icon(Icons.Rounded.TouchApp, null) },
        checked = micro,
        onCheckedChange = onMicroChange,
    )

    SwitchPreference(
        modifier = Modifier.settingRow(R.string.revibe_animations_player_title),
        title = { Text(stringResource(R.string.revibe_animations_player_title)) },
        description = stringResource(R.string.revibe_animations_player_description),
        icon = { Icon(Icons.Rounded.PlayArrow, null) },
        checked = player,
        onCheckedChange = onPlayerChange,
    )

    SwitchPreference(
        modifier = Modifier.settingRow(R.string.revibe_animations_welcome_title),
        title = { Text(stringResource(R.string.revibe_animations_welcome_title)) },
        description = stringResource(R.string.revibe_animations_welcome_description),
        icon = { Icon(Icons.Rounded.WavingHand, null) },
        checked = welcome,
        onCheckedChange = onWelcomeChange,
    )

    SwitchPreference(
        modifier = Modifier.settingRow(R.string.revibe_animations_reduce_title),
        title = { Text(stringResource(R.string.revibe_animations_reduce_title)) },
        description = stringResource(R.string.revibe_animations_reduce_description),
        icon = { Icon(Icons.Rounded.AccessibilityNew, null) },
        checked = reduce,
        onCheckedChange = onReduceChange,
    )

    PreferenceEntry(
        modifier = Modifier.settingRow(R.string.revibe_animations_replay_title),
        title = { Text(stringResource(R.string.revibe_animations_replay_title)) },
        description = stringResource(R.string.revibe_animations_replay_description),
        icon = { Icon(Icons.Rounded.Replay, null) },
        onClick = { welcomeController.show() },
    )
}
