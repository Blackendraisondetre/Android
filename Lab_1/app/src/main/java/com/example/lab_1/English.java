package com.example.lab_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class English extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cipher_eng);
    }

    public void Goto_main(View v){

        Intent intent = new Intent(this, Main.class);
        startActivity(intent);

    }

    public void End(View v){

        finish();

    }

    public void toEncrypt(View view) {

        EditText plain_1 = findViewById(R.id.plain_1);
        TextView cipher = findViewById(R.id.cipher);
        TextView plain_2 = findViewById(R.id.plain_2);
        EditText rot = findViewById(R.id.rot);
        Button encr = findViewById(R.id.encr);
        Button decr = findViewById(R.id.decr);

        int shift = Integer.parseInt(rot.getText().toString());
        while (shift > 26) {
            if (shift > 26) {
                shift -= 26;
            }
        }

        switch(view.getId()) {
            case R.id.encr:
                char[] originalLetters = plain_1.getText().toString().toLowerCase().toCharArray();
                int[] cipherLetters = new int[originalLetters.length];

                for (int i = 0; i < originalLetters.length; i++) {
                    cipherLetters[i] = (int) originalLetters[i];
                    if((cipherLetters[i] + shift) > 122 && cipherLetters[i] != 32){
                        cipherLetters[i] = 97 + (cipherLetters[i] + shift - 122 - 1);
                    } else if (cipherLetters[i] < 122 && cipherLetters[i] != 32) {
                        cipherLetters[i] += shift;
                    }
                }

                cipher.setText("");

                for (int i = 0; i < cipherLetters.length; i++) {
                    cipher.setText(cipher.getText().toString() + ((char) cipherLetters[i]));
                }

                break;
            case R.id.decr:
                char[] encryptedLetters = plain_1.getText().toString().toLowerCase().toCharArray();
                int[] decipheredLetters = new int[encryptedLetters.length];

                for (int i = 0; i < encryptedLetters.length; i++) {
                    decipheredLetters[i] = (int)encryptedLetters[i];
                    if((decipheredLetters[i] - shift) < 97 && decipheredLetters[i] != 32){
                        decipheredLetters[i] = 122 + decipheredLetters[i] - 97 - shift + 1;
                    }else if(decipheredLetters[i] > 97 && decipheredLetters[i] != 32){
                        decipheredLetters[i] -= shift;
                    }

                }

                plain_2.setText("");

                for (int i = 0; i < decipheredLetters.length; i++) {
                    plain_2.setText(plain_2.getText().toString() + ((char) decipheredLetters[i]));
                }
                break;
            default:
                break;
        }
    }




}