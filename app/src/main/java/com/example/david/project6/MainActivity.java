package com.example.david.project6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        // set a listener that checks if the user is already logged in
        setListener();
    }

    // goes to the next intent
    public void register(View view) {
        Intent intent = new Intent(this, registerActivity.class);
        // starts the new activity
        startActivity(intent);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void setListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    // if the user is singed in

                    Toast.makeText(getApplicationContext(), "logging in again",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,
                            loggedActivity.class);

                    // starts the new activity
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    // prompt the user to register or login
    public void login(View view) {
        Intent intent = new Intent(this, inlogActivity.class);
        // starts the new activity
        startActivity(intent);
    }
}
