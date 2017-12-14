package com.example.david.project6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    // initialize the necesary variables for firebase
    FirebaseAuth authTest;
    DatabaseReference mDatabase;

    // initialize the api result
    JSONObject results;

    // initialize the layout
    EditText countryEd, capitalEd;
    String  CountryGues, Capitalgues, countrySTR;
    TextView TitleTXT, currentTXT, exampleTXT;
    Button gues_count_btn, gues_capt_btn;

    // initialize the userclass
    UserClass to_change;

    // set everything that was initialized and do a volley for the countries and capitals
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);
        countryEd = (EditText) findViewById(R.id.countryED);
        capitalEd = (EditText) findViewById(R.id.capitalED);
        gues_count_btn = findViewById(R.id.countryBTN);
        gues_capt_btn = findViewById(R.id.capitalBTN);
        TitleTXT = findViewById(R.id.TitleTXT);
        currentTXT =  findViewById(R.id.currentTXT);
        exampleTXT =findViewById(R.id.exampleTXT);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        authTest = FirebaseAuth.getInstance();

        // makes sure the user starts with guessing an countrycode
        capital_invis();

        // get a jsonobject with countries and capitals
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

                            // safe the JSON
                            results = response.getJSONObject("Results");
                        } catch (JSONException e) {
                            // if this shows something changed in the JSON
                            Toast.makeText(loggedActivity.this,
                                    "problem with accesing the JSON",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // prompt the user to start again
                        Toast.makeText(loggedActivity.this,
                                "No connection please restart the app with internet acces",
                                Toast.LENGTH_SHORT).show();
                        TitleTXT.setText("check conection and restart app");
                    }
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    // add the country to the user in firebase
    public void add_country_firebase() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // get the userclass from firebase (from the current user)
                FirebaseUser user = authTest.getCurrentUser();
                to_change = dataSnapshot.child("users").child(user.getUid()).getValue(UserClass.class);

                // get the countries list and make sure it is not empty
                List<String> countries = new ArrayList<>();
                if(to_change.Countries != null){
                    countries = to_change.Countries;
                }

                // check if the user already has added the country if not add it to his list
                if (countries.contains(countrySTR)  ){
                    Toast.makeText(loggedActivity.this, "YOU ALREADY HAVE THIS COUNTRY",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    countries.add(countrySTR);
                    Toast.makeText(loggedActivity.this, "you added " + countrySTR,
                            Toast.LENGTH_SHORT).show();
                }

                // calculate the new score and add the country if correct
                to_change.Score = countries.size();
                to_change.Countries = countries;
                mDatabase.child("users").child(user.getUid()).setValue(to_change);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // if not possible toast it
                Toast.makeText(loggedActivity.this, " error in adding",
                        Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addListenerForSingleValueEvent(postListener);
    }

    public void country_gues(View view) {
        CountryGues = countryEd.getText().toString().toUpperCase();
        //  make sure the length is no more then 3 (because it i a countrycode)
        if (CountryGues.length() < 3){

            // check if the countrycode exists
            try {
                countrySTR = results.getJSONObject(CountryGues).getString("Name");
                capital_vis();
            } catch (JSONException e) {

                currentTXT.setText("Gues the countrycode again");
            }
        }
    }

    public void capital_gues(View view) {
        String capital = capitalEd.getText().toString();

        // get the correct capital and compare it with the userinput
        try {
            Capitalgues = results.getJSONObject(CountryGues).getJSONObject("Capital").getString("Name");

            // if the capital guessed is indeed belonging to the country add it to your firebase
            if (Capitalgues.equals(capital)){
                add_country_firebase();

                // set layout correct for another country guess
                capital_invis();
            }
            else{
                currentTXT.setText("Gues the capital of " + countrySTR + " again!");
            }
        } catch (JSONException e) {

            // if this happens the JSON is changed
            currentTXT.setText("something went wrong please logout");
        }
    }

    // sets the layout correct for guessing the capital
    public void capital_vis(){
        capitalEd.setVisibility(View.VISIBLE);
        gues_capt_btn.setVisibility(View.VISIBLE);
        capitalEd.setText("");
        gues_count_btn.setVisibility(View.INVISIBLE);
        countryEd.setVisibility(View.INVISIBLE);
        currentTXT.setText("The capital of " + countrySTR + " is?");
        exampleTXT.setText("Example Amsterdam (with correct grammar)");
    }

    // sets the layout correct for guessing the countrycode
    public void capital_invis(){
        capitalEd.setVisibility(View.INVISIBLE);
        gues_capt_btn.setVisibility(View.INVISIBLE);
        gues_count_btn.setVisibility(View.VISIBLE);
        countryEd.setVisibility(View.VISIBLE);
        countryEd.setText("");
        currentTXT.setText("Correct, gues another Country!");
        exampleTXT.setText("Example BD (for Bangladesh)");
    }

    // go to the activiy to compare with other users
    public void compare(View view) {
        Intent intent = new Intent(this, CompareActivity.class);

        // starts the new activity
        startActivity(intent);
        finish();
    }

    // logout the user
    public void logout_user(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        // starts the new activity
        startActivity(intent);
        finish();
    }
}
