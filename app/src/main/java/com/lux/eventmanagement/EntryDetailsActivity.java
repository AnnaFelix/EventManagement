package com.lux.eventmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lux.eventmanagement.adapter.EntryDetails;


public class EntryDetailsActivity extends AppCompatActivity {

    TextView title, details;
    ImageView RecDImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ;
        setContentView(R.layout.layout_fooddetails);

        Intent i = EntryDetailsActivity.this.getIntent();
        EntryDetails mEntryDetails = (EntryDetails) i.getSerializableExtra("EntryData");
        //StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("food");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Details");
        }

        title = findViewById(R.id.title);
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