package com.example.recyclor

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ui_mode_preference")

class DataStoreManager(context: Context) {

    private val dataStore = context.dataStore

    suspend fun setTheme(isDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[UI_MODE_KEY] = isDarkMode
        }
    }

    val uiMode: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            val uiMode = preferences[UI_MODE_KEY] ?: false
            uiMode
        }

    companion object {
        private val UI_MODE_KEY = booleanPreferencesKey("ui_mode")
    }
}