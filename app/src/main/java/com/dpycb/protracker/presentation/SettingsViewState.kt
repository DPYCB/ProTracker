package com.dpycb.protracker.presentation

data class SettingsViewState(
    val iconResId: Int,
    val text: String,
    val onClick: () -> Unit
)