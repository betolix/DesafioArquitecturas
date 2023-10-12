package io.h3llo.desafioarquitecturas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.room.Room
import io.h3llo.desafioarquitecturas.data.local.MoviesDatabase
import io.h3llo.desafioarquitecturas.ui.screens.Home

class MainActivity : ComponentActivity() {

    lateinit var db : MoviesDatabase

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(
            applicationContext,
            MoviesDatabase:: class.java, "movies-db"
        ).build()

        setContent {
            Home(db.moviesDao())
        }
    }
}
