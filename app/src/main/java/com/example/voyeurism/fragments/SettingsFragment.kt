package com.example.voyeurism.fragments

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.voyeurism.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val s = this.activity
        s!!.actionBar?.title = "Settings"
        val themeListPreference = findPreference<ListPreference>(getString(R.string.pref_key_night))
      //  themeListPreference?.onPreferenceChangeListener = darkModeChangeListener
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

}