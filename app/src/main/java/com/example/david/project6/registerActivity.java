/*
This allows the user to register in Firebase.
 */
package com.example.david.project6;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class registerActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String email, password;
    DatabaseReference mDatabase;
    EditText userT, passwordT;

    public registerActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity_layout);
        userT = findViewById(R.id.UsernameT);
        passwordT = findViewById(R.id.PasswordT);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    // creates user and checks if it is succesfull in the onComplete
    public void create_user(final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // toast it, add user to firebase and go to the next activity
                            Toast.makeText(registerActivity.this,
                                    "Authentication succes.", Toast.LENGTH_SHORT).show();
                            add_user_firebase();
                            logged_in();
                        } else {

                            // If sign in fails, display a message to the user.
                            Toast.makeText(registerActivity.this,
                                    "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void add_user_firebase(){
        FirebaseUser user = mAuth.getCurrentUser();
        String Uid = user.getUid();
        mDatabase.child("users").child(Uid).setValue(new UserClass(email, 1, null));
    }
    private void logged_in() {
        Intent intent = new Intent(this, loggedActivity.class);
        // starts the new activity

        startActivity(intent);
        finish();
    }

    // checks if both fields are filled in correct, if correct it creates the user
    public void register(View view) {
        email = userT.getText().toString();
        password = passwordT.getText().toString();
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            if (password.length() >6 ){
                create_user(email,password);
            }
            else{
                passwordT.setHint("needs at least 6 characters");
                passwordT.setText("");
            }
        }
        else{
            userT.setHint("fill in a valid email");
            userT.setText("");
        }
    }
}

