package com.example.myapplication.model

import com.google.firebase.firestore.DocumentId

data class Theater(
    @DocumentId val id: String = "",
    val name: String = "",
    val location: String = "",
    val capacity: Int = 0
)
