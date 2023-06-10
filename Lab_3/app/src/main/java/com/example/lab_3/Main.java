package com.example.lab_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void Goto_web(View v){

        Intent intent = new Intent(this, webber.class);
        startActivity(intent);

    }

    public void End(View v){

        finish();

    }



}