package com.example.lab_7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Me extends AppCompatActivity {

    Button butt_ex, butt_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        butt_ex = findViewById(R.id.butt_ex);
        butt_main = findViewById(R.id.butt_main);

        butt_ex.setOnClickListener(view -> finish());

        butt_main.setOnClickListener(view -> {
            Intent intent = new Intent(Me.this, Main.class);
            startActivity(intent);
        });
    }
}