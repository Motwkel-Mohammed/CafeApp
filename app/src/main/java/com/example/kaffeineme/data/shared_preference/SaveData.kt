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
}