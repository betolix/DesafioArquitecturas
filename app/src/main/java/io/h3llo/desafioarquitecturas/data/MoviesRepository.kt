package io.h3llo.desafioarquitecturas.data

import io.h3llo.desafioarquitecturas.data.local.LocalDataSource
import io.h3llo.desafioarquitecturas.data.local.MoviesDao
import io.h3llo.desafioarquitecturas.data.local.toLocalMovie
import io.h3llo.desafioarquitecturas.data.local.toMovie
import io.h3llo.desafioarquitecturas.data.remote.MoviesService
import io.h3llo.desafioarquitecturas.data.remote.RemoteDataSource
import io.h3llo.desafioarquitecturas.data.remote.toMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class MoviesRepository (
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
){
    val movies: Flow<List<Movie>> = localDataSource.movies

    suspend fun updateMovie(movie: Movie) {
        localDataSource.updateMovie(movie)
    }

    suspend fun requestMovies() {
        val isDbEmpty = localDataSource.count() == 0
        if (isDbEmpty) {
            localDataSource.insertAll(remoteDataSource.getMovies())
        }
    }




}