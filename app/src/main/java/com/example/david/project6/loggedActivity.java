package com.example.david.project6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class loggedActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    FirebaseAuth authTest;
    FirebaseAuth.AuthStateListener authListenerTest;
    DatabaseReference mDatabase;
    EditText countryEd, capitalEd;
    Integer score;
    String capitalStr, username, CountryGues, Capitalgues, countrySTR;
    JSONObject results;
    UserClass countries_found;
    TextView TitleTXT, currentTXT, exampleTXT;
    Button gues_count_btn, gues_capt_btn;
    UserClass to_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);
        countryEd = (EditText) findViewById(R.id.countryED);
        capitalEd = (EditText) findViewById(R.id.capitalED);
        gues_count_btn = findViewById(R.id.countryBTN);
        gues_capt_btn = findViewById(R.id.capitalBTN);
        score = 0;
        TitleTXT = findViewById(R.id.TitleTXT);
        currentTXT =  findViewById(R.id.currentTXT);
        exampleTXT =findViewById(R.id.exampleTXT);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        authTest = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        capital_invis();



        volley();

    }

    public void volley() {

        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String mJSONURLString = "http://www.geognos.com/api/en/countries/info/all.json";
        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                mJSONURLString,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // Process the JSON
                        try {
                            results = response.getJSONObject("Results");
                        } catch (JSONException e) {
                            Toast.makeText(loggedActivity.this, "No connection please restart the app", Toast.LENGTH_SHORT).show();
                            TitleTXT.setText("check conection and restart app");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred

                    }
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }


    public void get_user() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = authTest.getCurrentUser();
                System.out.println(user.getUid());
                to_change = dataSnapshot.child("users").child(user.getUid()).getValue(UserClass.class);

                List<String> countries = new ArrayList<>();
                if(to_change.Countries == null){
                    Toast.makeText(loggedActivity.this, "ADDED " + countrySTR, Toast.LENGTH_SHORT).show();


                }
                else {
                    countries = to_change.Countries;
                    Toast.makeText(loggedActivity.this, "ADDED 2 " + countrySTR, Toast.LENGTH_SHORT).show();

                }

                if (countries.contains(countrySTR)  ){
                    Toast.makeText(loggedActivity.this, "YOU ALREADY HAVE THIS COUNTRY", Toast.LENGTH_SHORT).show();
                }
                else {
                    countries.add(countrySTR);
                    Toast.makeText(loggedActivity.this, "you added " + countrySTR, Toast.LENGTH_SHORT).show();
                }

                to_change.Score = countries.size();
                to_change.username = username;
                mDatabase.child("users").child(user.getUid()).setValue(to_change);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(loggedActivity.this, " not showing user",
                        Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addListenerForSingleValueEvent(postListener);
    }



    public void country_gues(View view) {
        CountryGues = countryEd.getText().toString().toUpperCase();
        //  make sure the length is no more then 3
        if (CountryGues.length() < 3){
            try {
                countrySTR = results.getJSONObject(CountryGues).getString("Name");

                TitleTXT.setText("Gues the capital of " + countrySTR);
                countryEd.setText("");
                exampleTXT.setText("Example Amsterdam (with correct grammar)");
                capital_vis();
            } catch (JSONException e) {

                TitleTXT.setText("Gues the capital again! of: " + countrySTR);
            }
        }
    }

    public void capital_gues(View view) {
        String capital = capitalEd.getText().toString();
        if (CountryGues != ""){
            try {
                Capitalgues = results.getJSONObject(CountryGues).getJSONObject("Capital").getString("Name");


            } catch (JSONException e) {
                currentTXT.setText("Gues again!");


                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(loggedActivity.this, " pick a valid country first",
                    Toast.LENGTH_SHORT).show();
        }
        if (Capitalgues != ""){
            if (Capitalgues.equals(capital)){
                currentTXT.setText("Correct, gues another Country!");
                get_user();
                capital_invis();
            }
            else{
                currentTXT.setText("GUES AGAIN!");
            }
        }
    }

    public void capital_vis(){
        capitalEd.setVisibility(View.VISIBLE);
        gues_capt_btn.setVisibility(View.VISIBLE);
        gues_count_btn.setVisibility(View.INVISIBLE);
        countryEd.setVisibility(View.INVISIBLE);
    }
    public void capital_invis(){
        capitalEd.setVisibility(View.INVISIBLE);
        gues_capt_btn.setVisibility(View.INVISIBLE);
        gues_count_btn.setVisibility(View.VISIBLE);
        countryEd.setVisibility(View.VISIBLE);
    }

    public void compare(View view) {
        Intent intent = new Intent(this, CompareActivity.class);
        // starts the new activity

        intent.putExtra("username", username);

        startActivity(intent);
        finish();
    }
}
