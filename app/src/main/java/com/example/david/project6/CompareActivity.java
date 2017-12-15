/*
This Activity allows the user to search a users score and countries.
It returns a list with countries that where guessed correctly and the score.
 */


package com.example.david.project6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;



public class CompareActivity extends AppCompatActivity {
    TextView score_v;
    EditText user_ed;
    List<String> ListText;
    String email, username;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        ListText = new ArrayList<>();
        user_ed = findViewById(R.id.userED);
        score_v = findViewById(R.id.scoreTXT);
        list = findViewById(R.id.listView);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

    }
    public void getScore(){
        FirebaseDatabase.getInstance().getReference().child("users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            UserClass user = snapshot.getValue(UserClass.class);
                            if (user.username.equals(email)){
                                score_v.setText(email + "'s score  = " + user.Score);
                                ListText = user.Countries;
                                ArrayAdapter<String> theAdapter = new ArrayAdapter<String>(CompareActivity.this, android.R.layout.simple_list_item_1, ListText);
                                list.setAdapter(theAdapter);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public void go_back(View view) {
        Intent intent = new Intent(this, loggedActivity.class);
        // starts the new activity

        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }

    public void check_score(View view) {
        email = user_ed.getText().toString();

        // make sure the length is at least 5
        if (email.length() > 5){
            getScore();
        }
    }
}

