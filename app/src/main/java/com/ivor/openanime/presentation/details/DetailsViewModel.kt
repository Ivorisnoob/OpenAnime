package com.ivor.openanime.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivor.openanime.data.remote.model.AnimeDetailsDto
import com.ivor.openanime.domain.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: AnimeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val animeId: Int = checkNotNull(savedStateHandle["animeId"])
    
    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    init {
        loadDetails()
    }

    fun loadDetails() {
        viewModelScope.launch {
            _uiState.value = DetailsUiState.Loading
            repository.getAnimeDetails(animeId)
                .onSuccess { details ->
                    _uiState.value = DetailsUiState.Success(details)
                }
                .onFailure { exception ->
                    _uiState.value = DetailsUiState.Error(exception.message ?: "Unknown error")
                }
        }
    }
}

sealed interface DetailsUiState {
    data object Loading : DetailsUiState
    data class Success(val details: AnimeDetailsDto) : DetailsUiState
    data class Error(val message: String) : DetailsUiState
}
