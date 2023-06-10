package com.example.lab_4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Main extends AppCompatActivity {


    Button buttonToHash;
    EditText editText;
    TextView textViewResultHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        buttonToHash = findViewById(R.id.butt_hash);
        editText = findViewById(R.id.plain);
        textViewResultHash = findViewById(R.id.ans);

    }

    public void hashing(View v){

        String data = editText.getText().toString();
        try {
            byte[] dataEditText = data.getBytes("UTF-8");
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] digest = messageDigest.digest(dataEditText);
            textViewResultHash.setText(byteArrayToHex(digest));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String byteArrayToHex(byte[] a){
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }


    public void Goto_me(View v){

        Intent intent = new Intent(this, me.class);
        startActivity(intent);

    }

    public void End(View v){

        this.finish();

        System.exit(0);

    }


}