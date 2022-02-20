package com.dpycb.protracker.presentation.settings

data class SettingsViewState(
    val iconResId: Int,
    val text: String,
    val onClick: () -> Unit
)