package com.lux.eventmanagement;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.lux.eventmanagement.adapter.EntryDetails;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.lux.eventmanagement.adapter.LocationTrack;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class EntryDetailsActivity extends AppCompatActivity {

    TextView title, details;
    ImageView RecDImg;
    private GoogleMap  mMap;
    Button buttonviewlocaion;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
    EntryDetails mEntryDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_eventdetails);
        Intent i = EntryDetailsActivity.this.getIntent();
        mEntryDetails = (EntryDetails) i.getSerializableExtra("EntryData");
        //StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("food");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Details");
        }

        title = findViewById(R.id.title);
        buttonviewlocaion = findViewById(R.id.buttonviewlocaion);
        RecDImg = findViewById(R.id.RecDImg);
        title.setText(mEntryDetails.getTitle());
        title.setFocusable(false);
        details = findViewById(R.id.details);
        details.setText(mEntryDetails.getDescription());
        details.setFocusable(false);
        // Load the image using Glide
        Glide.with(getApplicationContext())
                .load(mEntryDetails.getImage())
                .into(RecDImg);


        buttonviewlocaion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(""+mEntryDetails.getLatitude()).equalsIgnoreCase("0.0") ){
                    Intent i = new Intent(EntryDetailsActivity.this, MapsActivity.class);
                    i.putExtra("latitude", mEntryDetails.getLatitude());
                    i.putExtra("longitude", mEntryDetails.getLongitude());
                    startActivity(i);
                }else {
                    Toast.makeText(EntryDetailsActivity.this,  "No Location shared!", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }





    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


}