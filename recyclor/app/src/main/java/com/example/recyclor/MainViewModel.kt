package com.example.recyclor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel (application: Application) : AndroidViewModel(application) {
    private val dataStore = DataStoreManager(application)

    val getTheme = dataStore.uiMode

    fun setTheme(isDarkMode : Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.setTheme(isDarkMode)
        }
    }
}
