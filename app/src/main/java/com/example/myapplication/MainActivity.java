package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.util.FirebaseSeed;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.example.myapplication.model.Movie;
import com.example.myapplication.adapter.MovieAdapter;

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

        // 1. CHẠY DÒNG NÀY ĐỂ TỰ ĐỘNG TẠO DỮ LIỆU TRÊN FIREBASE (Chỉ cần chạy 1 lần)
        // Sau khi dữ liệu đã lên Firebase, bạn có thể comment dòng này lại.
        FirebaseSeed.INSTANCE.initData();
        Toast.makeText(this, "Đang tải dữ liệu...", Toast.LENGTH_SHORT).show();

        // 2. Thiết lập giao diện
        rvMovies = findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new MovieAdapter(movieList, movie -> {
            // Sau này sẽ code chức năng chuyển sang màn hình đặt vé ở đây
            Toast.makeText(MainActivity.this, "Chọn phim: " + movie.getTitle(), Toast.LENGTH_SHORT).show();
        });
        rvMovies.setAdapter(adapter);

        // 3. Lấy dữ liệu từ Firestore và hiển thị
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
