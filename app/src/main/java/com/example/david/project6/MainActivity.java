package com.example.david.project6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void register(View view) {
        Intent intent = new Intent(this, registerActivity.class);
        // starts the new activity
        startActivity(intent);

    }

    public void login(View view) {
        Intent intent = new Intent(this, inlogActivity.class);
        // starts the new activity
        startActivity(intent);
    }
}