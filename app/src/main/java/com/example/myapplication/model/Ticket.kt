package com.example.myapplication.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Ticket(
    @DocumentId val id: String = "",
    val userId: String = "",
    val movieId: String = "",
    val movieTitle: String = "",
    val seats: List<String> = emptyList(),
    val totalPrice: Double = 0.0,
    @ServerTimestamp val bookingDate: Date? = null
)
