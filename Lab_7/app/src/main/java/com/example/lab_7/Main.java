package com.example.lab_7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FilenameUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Main extends AppCompatActivity {

    Button butt_enc, butt_dec, butt_ex, butt_iv, butt_me;
    EditText pass_edit,iv_edit;

    private static final int FIRST_REQUEST_CODE = 1337;
    private static final int SECOND_REQUEST_CODE = 228;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        butt_enc = findViewById(R.id.butt_enc);
        butt_dec = findViewById(R.id.butt_dec);
        butt_iv = findViewById(R.id.butt_iv);
        butt_ex = findViewById(R.id.butt_ex);
        butt_me = findViewById(R.id.butt_me);
        pass_edit = findViewById(R.id.password);
        iv_edit = findViewById(R.id.iv);

        butt_ex.setOnClickListener(view -> finish());

        butt_me.setOnClickListener(view -> {
            Intent intent = new Intent(Main.this, Me.class);
            startActivity(intent);
        });

        butt_iv.setOnClickListener(view -> {
            SecureRandom secureRandom = new SecureRandom();
            byte[] iv = new byte[16];
            secureRandom.nextBytes(iv);

            String base64IV = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                byte[] truncatedIV = Arrays.copyOf(iv, 16); // Truncate the IV to 16 bytes
                //Toast.makeText(this, "The length is " + truncatedIV.length +"and the value is = "+ Arrays.toString(truncatedIV), Toast.LENGTH_SHORT).show();
                base64IV = Base64.getEncoder().encodeToString(truncatedIV);

            }
            iv_edit.setText(base64IV);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Toast.makeText(this, "The length is pivo" + Base64.getDecoder().decode(iv_edit.getText().toString()).length +"and the value is = "+ Arrays.toString(Base64.getDecoder().decode(base64IV)), Toast.LENGTH_SHORT).show();
            }
        });


        butt_enc.setOnClickListener(view -> {
            String password = pass_edit.getText().toString();
            String iv = iv_edit.getText().toString();

            // Validate key length
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (Base64.getDecoder().decode(iv).length != 16) {
                    // Show an error message indicating invalid key size
                    Toast.makeText(this, "Length of IV must be 16 bytes length", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            // Hash the key
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hashedKey = md.digest(password.getBytes());
                SecretKeySpec secretKey = new SecretKeySpec(hashedKey, "AES");

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.putExtra("password", password);
                intent.putExtra("iv", iv);
                startActivityForResult(intent, FIRST_REQUEST_CODE);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        });


        butt_dec.setOnClickListener(view -> {
            String password = pass_edit.getText().toString();
            String iv = iv_edit.getText().toString();

            // Validate key length
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (Base64.getDecoder().decode(iv).length != 16) {
                    // Show an error message indicating invalid key size
                    Toast.makeText(this, "Length of IV must be 16 bytes length", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            // Hash the key
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hashedKey = md.digest(password.getBytes());
                SecretKeySpec secretKey = new SecretKeySpec(hashedKey, "AES");

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.putExtra("password", password);
                intent.putExtra("iv", iv);
                startActivityForResult(intent, SECOND_REQUEST_CODE);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        });


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == FIRST_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                try {
                    ContentResolver contentResolver = getContentResolver();
                    InputStream inputStream = contentResolver.openInputStream(uri);

                    // Pass the inputStream to the encryptInputStream method
                    String password = pass_edit.getText().toString();
                    String iv = iv_edit.getText().toString();
                    encryptInputStream(inputStream, password, iv, uri);

                    // Close the inputStream
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        else if (requestCode == SECOND_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                try {
                    ContentResolver contentResolver = getContentResolver();
                    InputStream inputStream = contentResolver.openInputStream(uri);

                    // Pass the inputStream to the decryptInputStream method
                    String password = pass_edit.getText().toString();
                    String iv = iv_edit.getText().toString();
                    decryptInputStream(inputStream, password);

                    // Close the inputStream
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }



    private void encryptInputStream(InputStream inputStream, String password, String iv, Uri uri) {
        try {
            if (inputStream != null) {
                // Read the input stream content
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteStream.write(buffer, 0, bytesRead);
                }
                byte[] fileContent = byteStream.toByteArray();

                // Perform additional error checks
                if (fileContent.length == 0) {
                    throw new IllegalArgumentException("Input stream content is empty");
                }

                // Generate a random IV
                byte[] ivBytes = new byte[16];
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ivBytes = Base64.getDecoder().decode(iv);
                }

                // Derive the AES key from the password using PBKDF2
                SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
                KeySpec spec = new PBEKeySpec(password.toCharArray(), ivBytes, 65536, 256); // Adjust iterations and key size as needed
                SecretKey secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

                // Initialize the cipher with AES/CBC/PKCS5Padding
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(ivBytes));

                // Encrypt the file content
                byte[] encryptedContent = cipher.doFinal(fileContent);
                System.out.println("The encrypted content = " + Arrays.toString(encryptedContent));

                byte[] combinedContent = new byte[ivBytes.length + encryptedContent.length];
                System.arraycopy(ivBytes, 0, combinedContent, 0, ivBytes.length);
                System.arraycopy(encryptedContent, 0, combinedContent, ivBytes.length, encryptedContent.length);

                // Write the encrypted content to a file
                File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "encrypted.bin");
                FileOutputStream outputStream = new FileOutputStream(outputFile);
                outputStream.write(combinedContent);
                outputStream.close();

                System.out.println("Encryption completed. Encrypted file path: " + outputFile.getAbsolutePath());
            } else {
                throw new IllegalArgumentException("Input stream is null");
            }
        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException |
                 InvalidKeyException | InvalidAlgorithmParameterException |
                 IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    private void decryptInputStream(InputStream inputStream, String password) {
        try {
            if (inputStream != null) {
                // Read the input stream content
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteStream.write(buffer, 0, bytesRead);
                }
                byte[] fileContent = byteStream.toByteArray();

                // Perform additional error checks
                if (fileContent.length == 0) {
                    throw new IllegalArgumentException("Input stream content is empty");
                }

                // Extract IV from the encrypted file
                byte[] iv = new byte[16];
                byte[] encryptedContent = new byte[fileContent.length - iv.length];
                System.arraycopy(fileContent, 0, iv, 0, iv.length);
                System.arraycopy(fileContent, iv.length, encryptedContent, 0, encryptedContent.length);

                // Derive the AES key from the password using PBKDF2
                SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
                KeySpec spec = new PBEKeySpec(password.toCharArray(), iv, 65536, 256); // Adjust iterations and key size as needed
                SecretKey secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

                // Initialize the cipher with AES/CBC/PKCS5Padding
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

                // Decrypt the encrypted content
                byte[] decryptedContent = cipher.doFinal(encryptedContent);
                System.out.println("The decrypted content: " + Arrays.toString(decryptedContent));

                // TODO: Handle the decrypted content as desired (e.g., save it, display it, etc.)

                // Write the decrypted content to a file
                File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "decrypted.txt");
                FileOutputStream outputStream = new FileOutputStream(outputFile);
                outputStream.write(decryptedContent);
                outputStream.close();

                System.out.println("Decryption completed. Decrypted file path: " + outputFile.getAbsolutePath());
            } else {
                throw new IllegalArgumentException("Input stream is null");
            }
        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException |
                 InvalidKeyException | InvalidAlgorithmParameterException |
                 IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }







}