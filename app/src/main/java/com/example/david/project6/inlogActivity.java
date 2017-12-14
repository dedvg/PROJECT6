package com.example.david.project6;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import static android.content.ContentValues.TAG;



public class inlogActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String email, password;
    EditText userT, passwordT;


    public inlogActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inlog);

        // initialize the edittexts and the Firebase instance
        userT = findViewById(R.id.UsernameT);
        passwordT = findViewById(R.id.PasswordT);
        mAuth = FirebaseAuth.getInstance();

    }

    // login the user with use of email and password and go to the next activity
    public void login_user(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            go_to_logged();
                        }
                        else {

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(inlogActivity.this, "Authentication failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
            });
    }

    // go to the next activity if login was succesfull
    private void go_to_logged() {
        Intent intent = new Intent(this, loggedActivity.class);
        Toast.makeText(inlogActivity.this, "login succesfull",
                Toast.LENGTH_SHORT).show();

        // starts the new activity
        startActivity(intent);
    }

    // checks if the user has put an email and password
    public void login(View view) {
        email = userT.getText().toString();
        password = passwordT.getText().toString();

        // first checks email then password
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            if (password.length() >5 ){
                login_user(email,password);

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

