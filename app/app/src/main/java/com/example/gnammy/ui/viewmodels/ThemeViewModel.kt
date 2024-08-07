package com.example.gnammy.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gnammy.data.repository.ThemeRepository
import com.example.gnammy.ui.theme.Themes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ThemeViewModel(private val themeRepository: ThemeRepository) : ViewModel() {

    private val _theme = MutableStateFlow(Themes.Auto)
    val theme: StateFlow<Themes> = _theme

    init {
        viewModelScope.launch {
            themeRepository.getThemePreference().collect {
                _theme.value = it
            }
        }
    }

    fun selectTheme(themePreference: Themes) {
        _theme.value = themePreference
        viewModelScope.launch {
            themeRepository.saveThemePreference(themePreference)
        }
    }
}
