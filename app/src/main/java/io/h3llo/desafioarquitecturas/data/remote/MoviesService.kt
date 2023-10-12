package io.h3llo.desafioarquitecturas.data.remote

import retrofit2.http.GET

interface MoviesService {

    @GET("discover/movie?api_key=2daaecb9ca6189631bfadd5744bf366c")
    suspend fun getMovies(): MovieResult
}