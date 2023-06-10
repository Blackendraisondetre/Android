package com.example.lab_5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main extends AppCompatActivity {


    Button butt_h1, butt_h2, comp;
    TextView hash1, hash2, ans;

    RadioGroup radioGroup;
    RadioButton radioBinary, radioOctal, radioHex;
    String text1, text2, path1, path2;

    private static final int FILE_FIRST_REQUEST_CODE = 15;
    private static final int FILE_SECOND_REQUEST_CODE = 17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        }

        butt_h1 = findViewById(R.id.butt_h1);
        butt_h2 = findViewById(R.id.butt_h2);
        comp = findViewById(R.id.comp);

        hash1 = findViewById(R.id.hash1);
        hash2 = findViewById(R.id.hash2);
        ans = findViewById(R.id.ans);

        radioGroup = findViewById(R.id.radioGroup);
        radioBinary = findViewById(R.id.radioBinary);
        radioOctal = findViewById(R.id.radioOctal);
        radioHex = findViewById(R.id.radioHex);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            updateHashTextRepresentation();
        });

        butt_h1.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, FILE_FIRST_REQUEST_CODE);
        });

        butt_h2.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, FILE_SECOND_REQUEST_CODE);
        });

        comp.setOnClickListener(v -> {
            if (hash1.getText() != null && hash2.getText() !=null){
                if(hash1.getText().toString().equals(hash2.getText().toString())){
                    ans.setText("Files are identical");
                } else{
                    ans.setText("Files are different");
                }
            } else{
                ans.setText("Only one file selected!");
            }
        });

        updateHashTextRepresentation();
    }

    private void updateHashTextRepresentation() {
        String selectedHashText_1 = "";
        String selectedHashText_2 = "";
        String hash1Text = hash1.getText().toString();
        String hash2Text = hash2.getText().toString();

        if (radioBinary.isChecked()) {
            selectedHashText_1 = convertToBinary(hash1Text);
            selectedHashText_2 = convertToBinary(hash2Text);
        } else if (radioOctal.isChecked()) {
            selectedHashText_1 = convertToOctal(hash1Text);
            selectedHashText_2 = convertToOctal(hash2Text);
        } else if (radioHex.isChecked()) {
            selectedHashText_1 = hash1Text;
            selectedHashText_2 = hash2Text;
        }

        hash1.setText(selectedHashText_1);
        hash2.setText(selectedHashText_2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_FIRST_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                text1 = readTextFromFile(uri, hash1);
                updateHashTextRepresentation();
            }
        }

        if (requestCode == FILE_SECOND_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                text2 = readTextFromFile(uri, hash2);
                updateHashTextRepresentation();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private String readTextFromFile(Uri uri, TextView textView) {
        byte[] digest = null;
        try {
            byte fileData;
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            InputStream inputStream = getContentResolver().openInputStream(uri);
            DataInputStream dis = new DataInputStream(inputStream);

            while(dis.available() > 0){
                fileData = dis.readByte();
                messageDigest.update(fileData);
            }
            dis.close();
            inputStream.close();
            digest = messageDigest.digest();
            textView.setText(byteArrayToHex(digest));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return byteArrayToHex(digest);
    }


    public static String byteArrayToHex(byte[] a){
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    private String convertToBinary(String hashText) {
        try {
            BigInteger hashValue = new BigInteger(hashText, 16);
            return hashValue.toString(2);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String convertToOctal(String hashText) {
        try {
            BigInteger hashValue = new BigInteger(hashText, 16);
            return hashValue.toString(8);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void Goto_me(View v){

        Intent intent = new Intent(this, About.class);
        startActivity(intent);

    }
    public void End(View v){

        finish();

    }



}