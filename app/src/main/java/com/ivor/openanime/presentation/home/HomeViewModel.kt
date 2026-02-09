package com.ivor.openanime.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivor.openanime.data.remote.model.AnimeDto
import com.ivor.openanime.domain.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AnimeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadPopularAnime()
    }

    fun loadPopularAnime() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            repository.getPopularAnime(page = 1)
                .onSuccess { animeList ->
                    _uiState.value = HomeUiState.Success(animeList)
                }
                .onFailure { exception ->
                    _uiState.value = HomeUiState.Error(exception.message ?: "Unknown error")
                }
        }
    }
}

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val animeList: List<AnimeDto>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
