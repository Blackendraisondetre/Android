package com.example.lab_6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Inner extends AppCompatActivity {

    Button butt_change,butt_main,butt_ex;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner);
        butt_change = findViewById(R.id.butt_change);
        butt_main = findViewById(R.id.butt_main);
        butt_ex = findViewById(R.id.butt_ex);
        password = findViewById(R.id.password);

        butt_ex.setOnClickListener(view -> finish());

        butt_main.setOnClickListener(view -> {
            Intent intent = new Intent(Inner.this, Main.class);
            startActivity(intent);
        });

        butt_change.setOnClickListener(view -> {
            String newPassword = password.getText().toString();
            updatePassword(newPassword);
        });

    }

    private void updatePassword(String newPassword) {
        // Get the current user's username (you may need to pass it from the Main activity or retrieve it from the shared preferences, etc.)
        String username = getIntent().getStringExtra("username");

        // Create an instance of the DBH class
        DBH dbh = new DBH(Inner.this);

        // Retrieve the user from the database based on the username
        userModel user = dbh.getUserByUsername(username);

        // Generate the salted hash of the new password
        String salt = user.getSalt();
        String hashedPassword = generateHashedPassword(newPassword, salt);

        // Update the user's password in the database
        boolean isSuccess = dbh.updateUserPassword(username, hashedPassword);

        if (isSuccess) {
            // Password update was successful
            Toast.makeText(Inner.this, "Password updated", Toast.LENGTH_SHORT).show();
        } else {
            // Password update failed
            Toast.makeText(Inner.this, "Password update failed", Toast.LENGTH_SHORT).show();
        }
    }

    private String generateHashedPassword(String password, String salt) {
        try {
            byte[] saltBytes = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                saltBytes = Base64.getDecoder().decode(salt);
            }
            byte[] passwordBytes = password.getBytes();

            byte[] passWithSaltBytes = new byte[Math.min(passwordBytes.length, saltBytes.length)];
            for (int i = 0; i < passWithSaltBytes.length; i++) {
                passWithSaltBytes[i] = (byte) (passwordBytes[i] ^ saltBytes[i]);
            }

            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] digest = messageDigest.digest(passWithSaltBytes);

            return byteArrayToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }



}