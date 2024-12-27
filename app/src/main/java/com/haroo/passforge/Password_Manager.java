package com.haroo.passforge;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Password_Manager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_manager);
        Button deleteBtn = findViewById(R.id.deleteBtn);

        deleteBtn.setOnClickListener(view -> {
                    String fileName = "passwords.json";
                    try {
                        FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
                        fos.write("[]".getBytes());
                        fos.close();
                        finish();
                        startActivity(getIntent());
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        ListView listView = findViewById(R.id.listPass);

        // Load JSON data
        String jsonData = loadJSONFromFile();
        if (jsonData != null) {
            ArrayList<String> passwords = parseJSON(jsonData);

            // Use ArrayAdapter for a simple layout
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, passwords);
            listView.setAdapter(adapter);
        }
    }

    // Load JSON from internal file
    private String loadJSONFromFile() {
        String json = null;
        String fileName = "passwords.json";
        try {
            FileInputStream fis = openFileInput(fileName);
            int size = fis.available();
            byte[] buffer = new byte[size];
            fis.read(buffer);
            fis.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            Log.e("Password_Manager", "Error reading JSON file", ex);
        }
        return json;
    }

    // Parse JSON data
    private ArrayList<String> parseJSON(String jsonData) {
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                list.add(obj.getString("password"));
            }
        } catch (JSONException e) {
            Log.e("Password_Manager", "Error parsing JSON", e);
        }
        return list;
    }

    public static void addPasswordToJSON(Context context, String password) {
        String fileName = "passwords.json";
        try {
            JSONArray jsonArray;

            // Read the existing JSON data
            String existingData = readJSONFromFile(context, fileName);
            if (existingData != null && !existingData.isEmpty()) {
                jsonArray = new JSONArray(existingData);
            } else {
                jsonArray = new JSONArray();
            }

            // Add the new password as a JSON object
            JSONObject newPassword = new JSONObject();
            newPassword.put("password", password);
            jsonArray.put(newPassword);

            // Write the updated JSON array to the file
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(jsonArray.toString().getBytes());
            fos.close();

            Log.d("Password_Manager", "Password added successfully to " + fileName);

        } catch (Exception e) {
            Log.e("Password_Manager", "Error writing to JSON file", e);
        }
    }

    private static String readJSONFromFile(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            int size = fis.available();
            byte[] buffer = new byte[size];
            fis.read(buffer);
            fis.close();
            return new String(buffer, "UTF-8");
        } catch (IOException e) {
            Log.e("Password_Manager", "Error reading JSON file", e);
            return null;
        }
    }

}
