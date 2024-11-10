package com.haroo.passforge;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.Random;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private int passLen = 12;
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public String getSaltString() {
        CheckBox symbolsBox = findViewById(R.id.symbolsBox);
        CheckBox numbersBox = findViewById(R.id.numbersBox);
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String SALTCHARSsym = "ABCDEFGHIJKLMNOPQRSTUVWXYZ-+=_':/.?";
        String SALTCHARSnum = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String SALTCHARSmix = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-+=_':/.?";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        String saltStr = "";
        int index;

        while (salt.length() < passLen) { // length of the random string.
            if(symbolsBox.isChecked()) {
                index = (int) (rnd.nextFloat() * SALTCHARSsym.length());
                salt.append(SALTCHARSsym.charAt(index));
            } else if(numbersBox.isChecked()) {
                index = (int) (rnd.nextFloat() * SALTCHARSnum.length());
                salt.append(SALTCHARSnum.charAt(index));
            } else if (symbolsBox.isChecked() && numbersBox.isChecked()) {
                index = (int) (rnd.nextFloat() * SALTCHARSmix.length());
                salt.append(SALTCHARSmix.charAt(index));
            }else{
                index = (int) (rnd.nextFloat() * SALTCHARS.length());
                salt.append(SALTCHARS.charAt(index));
            }
            saltStr = salt.toString();
        }

        return saltStr;
    }

    public void generatePassword(View view) {
        password = getSaltString();
        TextView generatedText = findViewById(R.id.generatedText);
        generatedText.setText(password);
    }

    public void clearPassword(View view) {
        password = "Password Generated";
        TextView generatedText = findViewById(R.id.generatedText);
        generatedText.setText(password);
    }
}