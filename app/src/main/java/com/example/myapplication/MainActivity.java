package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.MovieAdapter;
import com.example.myapplication.model.Movie;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvMovies;
    private MovieAdapter adapter;
    private List<Movie> movieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Thiết lập giao diện RecyclerView
        rvMovies = findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        
        // 2. Xử lý khi nhấn nút Đặt vé trong Adapter
        adapter = new MovieAdapter(movieList, movie -> {
            Intent intent = new Intent(MainActivity.this, BookingActivity.class);
            intent.putExtra("movieId", movie.getId());
            intent.putExtra("movieTitle", movie.getTitle());
            startActivity(intent);
        });
        rvMovies.setAdapter(adapter);

        // 3. Lấy dữ liệu từ Firestore
        fetchMovies();
    }

    private void fetchMovies() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("movies")
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    movieList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Movie movie = document.toObject(Movie.class);
                        // Gán ID từ document vào object movie
                        movie = new Movie(document.getId(), movie.getTitle(), movie.getDescription(), 
                                        movie.getPosterUrl(), movie.getDuration(), movie.getRating(), 
                                        movie.getGenre(), movie.isShowing());
                        movieList.add(movie);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.w("MainActivity", "Error getting documents.", task.getException());
                    Toast.makeText(this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
            });
    }
}
