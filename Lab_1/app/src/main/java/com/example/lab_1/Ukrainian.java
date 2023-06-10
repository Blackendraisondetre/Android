package com.example.lab_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class Ukrainian extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cipher_ukr);
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

        Map<Integer, String> UkrainianAlphabet = new HashMap<Integer, String>();

        UkrainianAlphabet.put(1, "а");
        UkrainianAlphabet.put(2, "б");
        UkrainianAlphabet.put(3, "в");
        UkrainianAlphabet.put(4, "г");
        UkrainianAlphabet.put(5, "ґ");
        UkrainianAlphabet.put(6, "д");
        UkrainianAlphabet.put(7, "е");
        UkrainianAlphabet.put(8, "є");
        UkrainianAlphabet.put(9, "ж");
        UkrainianAlphabet.put(10, "з");
        UkrainianAlphabet.put(11, "и");
        UkrainianAlphabet.put(12, "і");
        UkrainianAlphabet.put(13, "ї");
        UkrainianAlphabet.put(14, "й");
        UkrainianAlphabet.put(15, "к");
        UkrainianAlphabet.put(16, "л");
        UkrainianAlphabet.put(17, "м");
        UkrainianAlphabet.put(18, "н");
        UkrainianAlphabet.put(19, "о");
        UkrainianAlphabet.put(20, "п");
        UkrainianAlphabet.put(21, "р");
        UkrainianAlphabet.put(22, "с");
        UkrainianAlphabet.put(23, "т");
        UkrainianAlphabet.put(24, "у");
        UkrainianAlphabet.put(25, "ф");
        UkrainianAlphabet.put(26, "х");
        UkrainianAlphabet.put(27, "ц");
        UkrainianAlphabet.put(28, "ч");
        UkrainianAlphabet.put(29, "ш");
        UkrainianAlphabet.put(30, "щ");
        UkrainianAlphabet.put(31, "ь");
        UkrainianAlphabet.put(32, "ю");
        UkrainianAlphabet.put(33, "я");


        int shift = Integer.parseInt(rot.getText().toString());
        while (shift > 33) {
            if (shift > 33) {
                shift -= 33;
            }
        }

        switch (view.getId()) {
            case R.id.encr:
                char[] originalLetters = plain_1.getText().toString().toLowerCase().toCharArray();
                char[] cipherLetters = new char[originalLetters.length];
                String s;
                int key;
                int space = 32;

                for (int i = 0; i < originalLetters.length; i++) {
                    if(originalLetters[i] != (char)space)  {
                        for (Map.Entry<Integer, String> item : UkrainianAlphabet.entrySet()) {
                            if (String.valueOf(originalLetters[i]).equals(item.getValue())) {
                                key = item.getKey() + shift;
                                if (key>33){
                                    key =  key - 33;
                                    s = (UkrainianAlphabet.get(key));
                                    cipherLetters[i] = s.charAt(0);
                                } else if (key<=33) {
                                    s = (UkrainianAlphabet.get(key));
                                    cipherLetters[i] = s.charAt(0);
                                }
                            }

                        }
                    }else if(originalLetters[i] == (char)space) {
                        cipherLetters[i] = originalLetters[i];
                    }
                }

                cipher.setText("");

                for (int i = 0; i < cipherLetters.length; i++) {
                    cipher.setText(cipher.getText().toString() + cipherLetters[i]);
                }

                break;

                case R.id.decr:
                char[] encryptedLetters = plain_1.getText().toString().toLowerCase().toCharArray();
                char[] decryptedLetters = new char[encryptedLetters.length];
                String stillas;
                int stillakey;
                int stillaspace = 32;

                for(int i = 0; i < encryptedLetters.length; i++){
                    if(encryptedLetters[i] != (char)stillaspace){
                        for (Map.Entry<Integer, String> item : UkrainianAlphabet.entrySet()) {
                            if (String.valueOf(encryptedLetters[i]).equals(item.getValue())) {
                                stillakey = item.getKey() - shift;
                                if (stillakey<1){
                                    stillakey =  33 + stillakey;
                                    stillas = (UkrainianAlphabet.get(stillakey));
                                    decryptedLetters[i] = stillas.charAt(0);
                                } else if (stillakey>=1) {
                                    stillas = (UkrainianAlphabet.get(stillakey));
                                    decryptedLetters[i] = stillas.charAt(0);
                                }
                            }

                        }
                    } else if (encryptedLetters[i] == (char)stillaspace) {
                        decryptedLetters[i] = encryptedLetters[i];
                    }
                }

                    plain_2.setText("");

                for (int i = 0; i < decryptedLetters.length; i++) {
                    plain_2.setText(plain_2.getText().toString() + decryptedLetters[i]);
                }
                break;
        }
    }


}