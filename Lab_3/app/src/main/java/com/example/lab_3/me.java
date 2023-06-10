package com.example.lab_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class me extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_me);
    }

    public void Goto_main(View v){

        Intent intent = new Intent(this, Main.class);
        startActivity(intent);

    }

    public void End(View v){

        finish();

    }


}