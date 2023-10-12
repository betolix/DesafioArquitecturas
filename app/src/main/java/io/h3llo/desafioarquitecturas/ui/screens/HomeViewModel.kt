package io.h3llo.desafioarquitecturas.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.h3llo.desafioarquitecturas.data.Movie
import io.h3llo.desafioarquitecturas.data.local.MoviesDao
import io.h3llo.desafioarquitecturas.data.local.toMovie
import io.h3llo.desafioarquitecturas.data.remote.MoviesService
import io.h3llo.desafioarquitecturas.data.remote.ServerMovie
import io.h3llo.desafioarquitecturas.data.remote.toLocalMovie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeViewModel(private val dao: MoviesDao) : ViewModel() {

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
        viewModelScope.launch{
            val isDbEmpty = dao.count() == 0
            if (isDbEmpty){
                _state.value = UiState(loading = true)
                //delay(2000)

                dao.insertAll(
                    Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(MoviesService::class.java)
                        .getMovies()
                        .results
                        .map{ it.toLocalMovie() }
                )
            }


            _state.value = UiState(
                loading = false,
                movies = dao.getMovies().map { it.toMovie() }
            )

        }

    }

    fun onMovieClick(movie: Movie) {
        val movies = _state.value.movies.toMutableList()
        movies.replaceAll { if (it.id == movie.id) movie.copy(favorite = !movie.favorite) else it }

        _state.value = _state.value.copy(movies = movies )

    }

    data class UiState(
        val loading: Boolean = false,
        val movies: List<Movie> = emptyList()
    )
}