package com.example.lab_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Main extends AppCompatActivity {

    //private Button butt_eng, butt_ukr, butt_end, butt_me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);



    }

    public void Goto_eng(View v){

        Intent intent = new Intent(this, English.class);
        startActivity(intent);

    }

    public void Goto_ukr(View v){

        Intent intent = new Intent(this, Ukrainian.class);
        startActivity(intent);

    }

    public void Goto_me(View v){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void End(View v){

        finish();

    }


}