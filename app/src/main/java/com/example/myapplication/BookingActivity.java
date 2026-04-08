package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class BookingActivity extends AppCompatActivity {

    private TextView tvMovieTitle, tvTotalPrice;
    private CheckBox cbA1, cbA2, cbB1;
    private Button btnConfirm;
    
    private String movieId;
    private String movieTitle;
    private double pricePerSeat = 150000.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // Lấy thông tin phim từ Intent
        movieId = getIntent().getStringExtra("movieId");
        movieTitle = getIntent().getStringExtra("movieTitle");

        tvMovieTitle = findViewById(R.id.tvBookingMovieTitle);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        cbA1 = findViewById(R.id.cbSeatA1);
        cbA2 = findViewById(R.id.cbSeatA2);
        cbB1 = findViewById(R.id.cbSeatB1);
        btnConfirm = findViewById(R.id.btnConfirmBooking);

        tvMovieTitle.setText("Đặt vé phim: " + movieTitle);

        // Xử lý tính tiền khi chọn ghế
        cbA1.setOnCheckedChangeListener((v, isChecked) -> updateTotalPrice());
        cbA2.setOnCheckedChangeListener((v, isChecked) -> updateTotalPrice());
        cbB1.setOnCheckedChangeListener((v, isChecked) -> updateTotalPrice());

        btnConfirm.setOnClickListener(v -> saveTicketToFirebase());
    }

    private void updateTotalPrice() {
        int count = 0;
        if (cbA1.isChecked()) count++;
        if (cbA2.isChecked()) count++;
        if (cbB1.isChecked()) count++;
        
        double total = count * pricePerSeat;
        tvTotalPrice.setText("Tổng tiền: " + String.format("%,.0f", total) + "đ");
    }

    private void saveTicketToFirebase() {
        List<String> selectedSeats = new ArrayList<>();
        if (cbA1.isChecked()) selectedSeats.add("A1");
        if (cbA2.isChecked()) selectedSeats.add("A2");
        if (cbB1.isChecked()) selectedSeats.add("B1");

        if (selectedSeats.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ít nhất 1 ghế", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        double total = selectedSeats.size() * pricePerSeat;

        // Tạo đối tượng Ticket
        Ticket ticket = new Ticket("", userId, movieId, movieTitle, selectedSeats, total, null);

        // Lưu lên Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tickets")
                .add(ticket)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(BookingActivity.this, "Đặt vé thành công!", Toast.LENGTH_LONG).show();
                    finish(); // Quay lại màn hình trước
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(BookingActivity.this, "Lỗi đặt vé: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
