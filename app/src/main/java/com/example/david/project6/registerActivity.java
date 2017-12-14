package com.example.david.project6;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
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
        Intent intent = getIntent();

        userT = findViewById(R.id.UsernameT);
        passwordT = findViewById(R.id.PasswordT);

        mAuth = FirebaseAuth.getInstance();
        password = "meer dan 6 chars";
        email = "lol@live.nl";


    }





    public void create_user(final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, set a UserClass in firebase
                            Toast.makeText(registerActivity.this, "Authentication succes.", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            List<String> countries = new ArrayList<String>();
                            countries.add("FREE POINT");
                            mDatabase.child("users").child(user.getUid()).setValue(new UserClass(email, 1, countries));

                            

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(registerActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
    public void add_tolist() {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        UserClass current_user = new UserClass(email, 0, null);
        List<String> Countries = new ArrayList<String>();

        Countries.add("Holland");
        current_user.Countries = Countries;
        List<UserClass> userlist = new ArrayList<UserClass>();
        userlist.add(current_user);

        mDatabase.child("userlist").setValue(userlist);
        get_list();


    }

    public void get_list(){
        FirebaseDatabase.getInstance().getReference().child("users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            UserClass user = snapshot.getValue(UserClass.class);
                            System.out.println(user.username);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

    }

    private void updateUI(FirebaseUser user) {
        System.out.println("it came close");
    }

    public void register(View view) {
        email = userT.getText().toString();
        password = passwordT.getText().toString();

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            if (password.length() >6 ){
                create_user(email,password);
                add_tolist();

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

