package com.example.myapplication.util

import android.util.Log
import com.example.myapplication.model.User
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseSeed {
    private const val TAG = "FirebaseSeed"

    fun initData() {
        val db = FirebaseFirestore.getInstance()
        Log.d(TAG, "Bắt đầu khởi tạo dữ liệu mẫu...")

        // 1. Tạo User mẫu
        val sampleUser = User(
            uid = "user_demo_123",
            email = "demo@gmail.com",
            fullName = "Người Dùng Thử",
            fcmToken = ""
        )
        db.collection("users").document(sampleUser.uid).set(sampleUser)
            .addOnSuccessListener { Log.d(TAG, "Tạo User thành công!") }
            .addOnFailureListener { e -> Log.e(TAG, "Lỗi tạo User: ${e.message}") }

        // 2. Tạo danh sách phim mẫu
        val movies = listOf(
            hashMapOf(
                "title" to "Avengers: Endgame",
                "description" to "Sau những sự kiện thảm khốc của Avengers: Infinity War, vũ trụ đang bị tàn phá.",
                "posterUrl" to "https://image.tmdb.org/t/p/w500/or06vSaeW1uGjH6mYq0oYv0p96L.jpg",
                "duration" to 181,
                "genre" to "Hành động, Viễn tưởng",
                "rating" to 4.8,
                "isShowing" to true
            ),
            hashMapOf(
                "title" to "Spider-Man: No Way Home",
                "description" to "Lần đầu tiên trong lịch sử điện ảnh của Người Nhện, danh tính của người anh hùng bị lộ.",
                "posterUrl" to "https://image.tmdb.org/t/p/w500/1g0dhvRzfwvS6zDhu7Yp.jpg",
                "duration" to 148,
                "genre" to "Hành động, Phiêu lưu",
                "rating" to 4.7,
                "isShowing" to true
            )
        )

        for (movieData in movies) {
            db.collection("movies").add(movieData)
                .addOnSuccessListener { movieRef ->
                    Log.d(TAG, "Thêm phim thành công: ${movieData["title"]}")
                    
                    // 3. Tạo rạp và lịch chiếu mẫu
                    val theater = hashMapOf(
                        "name" to "CGV Vincom Center",
                        "address" to "72 Lê Thánh Tôn, Quận 1, TP.HCM",
                        "location" to "Tầng 3"
                    )
                    db.collection("theaters").add(theater).addOnSuccessListener { theaterRef ->
                        val showtime = hashMapOf(
                            "movieId" to movieRef.id,
                            "theaterId" to theaterRef.id,
                            "startTime" to "20:00 - 30/12/2024",
                            "price" to 150000.0,
                            "availableSeats" to listOf("A1", "A2", "B1", "B2")
                        )
                        db.collection("showtimes").add(showtime)
                            .addOnSuccessListener { Log.d(TAG, "Tạo lịch chiếu thành công cho phim!") }
                    }
                }
                .addOnFailureListener { e -> 
                    Log.e(TAG, "Lỗi khi thêm phim: ${e.message}")
                }
        }
    }
}
