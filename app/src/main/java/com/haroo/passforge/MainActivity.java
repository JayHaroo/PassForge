package com.haroo.passforge;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    int passLen = 0;
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

        CheckBox customChar = findViewById(R.id.customChar);
        TextInputLayout customCharSet = findViewById(R.id.customCharField);
        customChar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customCharSet.setEnabled(customChar.isChecked());
            }
        });
    }

    public String getSaltString() {
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        RadioButton size12 = findViewById(R.id.radioButton1);
        RadioButton size16 = findViewById(R.id.radioButton2);
        RadioButton size18 = findViewById(R.id.radioButton3);

        if(size12.isChecked()) {
            passLen = 12;
        } else if(size16.isChecked()) {
            passLen = 16;
        } else if(size18.isChecked()) {
            passLen = 18;
        }


        CheckBox customChar = findViewById(R.id.customChar);
        TextInputLayout customCharSet = findViewById(R.id.customCharField);
        CheckBox symbolsBox = findViewById(R.id.symbolsBox);
        CheckBox numbersBox = findViewById(R.id.numbersBox);

        String customSALTCHARS = "";
        String SALTCHARS = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
        String SALTCHARSsym = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz-+=_':/.?";
        String SALTCHARSnum = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789";
        String SALTCHARSmix = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0-1+2=3.4?5/6(7:89";

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
            }else if(customChar.isChecked()) {
                customCharSet.isEnabled();
                customSALTCHARS = customCharSet.getEditText().getText().toString();

                if(!customSALTCHARS.isEmpty()) {
                    index = (int) (rnd.nextFloat() * customSALTCHARS.length());
                    salt.append(customSALTCHARS.charAt(index));
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Please enter a custom character set", Toast.LENGTH_SHORT);
                    toast.show();
                    index = (int) (rnd.nextFloat() * SALTCHARS.length());
                    salt.append(SALTCHARS.charAt(index));
                }

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

    public void copyPassword(View view) {
        TextView textView = findViewById(R.id.generatedText);
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        String label = "Generated Password";
        String text = textView.getText().toString();
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
    }

    public void clearPassword(View view) {
        password = "Password Generated";
        TextView generatedText = findViewById(R.id.generatedText);
        generatedText.setText(password);
    }

    public void openPassManager(View view) {
        Password_Manager pm = new Password_Manager();
        startActivity(new Intent(this, Password_Manager.class));
        if(password != "Password Generated"){
            pm.addPasswordToJSON(this, password);
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "Please generate a password first", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}