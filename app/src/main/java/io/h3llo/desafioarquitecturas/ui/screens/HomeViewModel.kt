package io.h3llo.desafioarquitecturas.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.h3llo.desafioarquitecturas.data.Movie
import io.h3llo.desafioarquitecturas.data.MoviesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MoviesRepository) : ViewModel() {

    // Google recommended WAY
    /*
    /*
    var state by mutableStateOf(UiState())
        private set */
    // LiveData-Way
    /*
    private val _state = MutableLiveData(UiState())
    val state: LiveData<UiState> = _state */
    */
    // StateFlow-Way
    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state


    init{
        viewModelScope.launch {
            _state.value = UiState(loading = true)
            repository.requestMovies()

            repository.movies.collect{
                _state.value = UiState(movies = it)
            }
        }
    }

    fun onMovieClick(movie: Movie) {
        /*
        val movies = _state.value.movies.toMutableList()
        movies.replaceAll { if (it.id == movie.id) movie.copy(favorite = !movie.favorite) else it }
        _state.value = _state.value.copy(movies = movies )
        */
        viewModelScope.launch{
            repository.updateMovie(movie.copy(favorite = !movie.favorite))
        }

    }

    data class UiState(
        val loading: Boolean = false,
        val movies: List<Movie> = emptyList()
    )
}