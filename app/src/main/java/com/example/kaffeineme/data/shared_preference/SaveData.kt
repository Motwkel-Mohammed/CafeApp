package com.example.kaffeineme.data.shared_preference

import android.content.Context

class SaveData(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("File", Context.MODE_PRIVATE)

    fun setDarkModeState(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("Dark", state)
        editor.apply()
    }

    fun loadDarkState(): Boolean {
        return sharedPreferences.getBoolean("Dark", false)
    }

    fun setArabicLanguageState(state: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("Arabic", state)
        editor.apply()
    }

    fun loadArabicLanguageState(): Int {
        return sharedPreferences.getInt("Arabic", 1)
    }

    fun setSavePasswordState(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("SavePassword", state)
        editor.apply()
    }

    fun loadSavePasswordState(): Boolean {
        return sharedPreferences.getBoolean("SavePassword", false)
    }
}
