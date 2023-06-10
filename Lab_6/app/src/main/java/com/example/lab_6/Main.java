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
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Main extends AppCompatActivity {

    Button butt_log,butt_reg,butt_ex;
    EditText username, password;

    public static String byteArrayToHex(byte[] a){
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        butt_log = findViewById(R.id.butt_log);
        butt_reg = findViewById(R.id.butt_reg);
        butt_ex = findViewById(R.id.butt_ex);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);



        butt_ex.setOnClickListener(view -> finish());

        butt_log.setOnClickListener(view -> {
            String usernameInput = username.getText().toString();
            String passwordInput = password.getText().toString();

            DBH dbh = new DBH(Main.this);
            List<userModel> userList = dbh.getUser();

            boolean isSuccess = false;
            for (userModel user : userList) {
                if (user.getName().equals(usernameInput)) {
                    String salt = user.getSalt();
                    String storedPassword = user.getPass();

                    byte[] saltBytes = new byte[0];
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        saltBytes = Base64.getDecoder().decode(salt);
                    }
                    byte[] passwordBytes = passwordInput.getBytes();

                    int minLength = Math.min(passwordBytes.length, saltBytes.length);
                    byte[] passWithSaltBytes = new byte[minLength];
                    for (int i = 0; i < minLength; i++) {
                        passWithSaltBytes[i] = (byte) (passwordBytes[i] ^ saltBytes[i]);
                    }

                    try {
                        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                        byte[] hashBytes = messageDigest.digest(passWithSaltBytes);
                        String hashedPassword = byteArrayToHex(hashBytes);

                        if (hashedPassword.equals(storedPassword)) {
                            isSuccess = true;
                            break;
                        }
                    } catch (NoSuchAlgorithmException e) {
                        Toast.makeText(Main.this, "No such algorithm", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            if (isSuccess) {

                //Toast.makeText(Main.this, "Login successful", Toast.LENGTH_SHORT).show();
                Toast.makeText(Main.this, userList.toString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Main.this, Inner.class);
                intent.putExtra("username", usernameInput);
                startActivity(intent);
            } else {
                Toast.makeText(Main.this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        });

        butt_reg.setOnClickListener(view -> {

            String user_name = username.getText().toString();
            String user_pass = password.getText().toString();

            byte[] digest = null;
            byte[] pass_bytes = user_pass.getBytes();

            SecureRandom random = new SecureRandom();
            byte[] sold;
            sold = new byte[256];
            random.nextBytes(sold);

            byte[] pass_sold = new byte[pass_bytes.length];
            int z;
            for (int i = 0; i < pass_bytes.length; i++) {
                z = pass_bytes[i] ^ sold[i];
                pass_sold[i] = (byte) z;
            }

            String sold64 = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                sold64 = Base64.getEncoder().encodeToString(sold);
            }
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                digest = messageDigest.digest(pass_sold);
            } catch (Exception NoSuchAlgorithmException) {
                Toast.makeText(this, "No such alhorithm", Toast.LENGTH_SHORT).show();
            }

            String fin_user_pass = byteArrayToHex(digest);
            String salt = sold64;


            userModel usrmd = new userModel(1, user_name, fin_user_pass, salt);


            DBH dbh = new DBH(Main.this);
            boolean suc = dbh.addUser(usrmd);
            Toast.makeText(Main.this, "Success = " + suc, Toast.LENGTH_SHORT).show();

        });




    }






}