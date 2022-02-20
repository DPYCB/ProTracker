package com.dpycb.protracker.presentation.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.dpycb.protracker.R
import com.dpycb.protracker.databinding.SettingsFragmentBinding
import com.dpycb.protracker.utils.viewBinding
import dagger.android.support.AndroidSupportInjection

class SettingsFragment: Fragment(R.layout.settings_fragment) {
    private val binding by viewBinding(SettingsFragmentBinding::bind)
    private val adapter = SettingsAdapter()

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.settingsList.adapter = adapter
        adapter.submitList(initSettingsList())
    }

    private fun initSettingsList(): List<SettingsViewState> {
        return listOf(
            SettingsViewState(
                R.drawable.ic_palette,
                "Внешний вид",
                ::onThemeSwitchClicked
            ),
            SettingsViewState(
                R.drawable.ic_info,
                "О приложении",
                ::onAboutClicked
            )
        )
    }

    private fun onAboutClicked() {
        Toast.makeText(requireContext(), "Приложение мужчин честной судьбы", Toast.LENGTH_LONG).show()
    }

    private fun onThemeSwitchClicked() {
        val nightMode = AppCompatDelegate.getDefaultNightMode()
        val requiredNightMode = when (nightMode) {
            AppCompatDelegate.MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_NO
            else -> AppCompatDelegate.MODE_NIGHT_YES
        }
        AppCompatDelegate.setDefaultNightMode(requiredNightMode)
    }
}