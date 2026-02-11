package com.ivor.openanime.presentation.watch_history

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivor.openanime.data.remote.model.AnimeDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

data class WatchHistoryUiState(
    val history: List<AnimeDto> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class WatchHistoryViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val json: Json
) : ViewModel() {

    private val _uiState = MutableStateFlow(WatchHistoryUiState())
    val uiState: StateFlow<WatchHistoryUiState> = _uiState.asStateFlow()

    private val HISTORY_KEY = "watch_history_list"

    init {
        loadHistory()
    }

    private fun loadHistory() {
        _uiState.update { it.copy(isLoading = true) }
        val historyJson = sharedPreferences.getString(HISTORY_KEY, "[]") ?: "[]"
        try {
            val historyList = json.decodeFromString<List<AnimeDto>>(historyJson)
            _uiState.update { it.copy(history = historyList, isLoading = false) }
        } catch (e: Exception) {
            _uiState.update { it.copy(history = emptyList(), isLoading = false) }
        }
    }

    fun clearHistory() {
        _uiState.update { it.copy(history = emptyList()) }
        sharedPreferences.edit().remove(HISTORY_KEY).apply()
    }
    
    fun removeFromHistory(animeId: Int) {
        val currentList = _uiState.value.history.toMutableList()
        currentList.removeIf { it.id == animeId }
        _uiState.update { it.copy(history = currentList) }
        saveHistory(currentList)
    }
    
    private fun saveHistory(list: List<AnimeDto>) {
        viewModelScope.launch {
            val jsonString = json.encodeToString(list)
            sharedPreferences.edit().putString(HISTORY_KEY, jsonString).apply()
        }
    }
}
