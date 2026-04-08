package com.example.myapplication.model

import com.google.firebase.firestore.DocumentId
import java.util.Date

data class Showtime(
    @DocumentId val id: String = "",
    val movieId: String = "",
    val theaterId: String = "",
    val startTime: Date? = null,
    val price: Double = 0.0,
    val availableSeats: List<String> = emptyList()
)
