package com.example.myapplication.model

import com.google.firebase.firestore.DocumentId

data class Movie(
    @DocumentId val id: String = "",
    val title: String = "",
    val description: String = "",
    val posterUrl: String = "",
    val duration: Int = 0,
    val rating: Double = 0.0,
    val genre: String = "",
    val isShowing: Boolean = true
)
