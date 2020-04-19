package com.lux.eventmanagement;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lux.eventmanagement.adapter.AllEntryListAdapter;
import com.lux.eventmanagement.adapter.CommentListAdapter;
import com.lux.eventmanagement.adapter.EntryDetails;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.lux.eventmanagement.adapter.LocationTrack;
import com.lux.eventmanagement.adapter.UserCommentList;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class EntryDetailsActivity extends AppCompatActivity {

    TextView title, details, addcomment;
    ImageView RecDImg,video;
    private GoogleMap  mMap;
    Button buttonviewlocaion;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
    EntryDetails mEntryDetails;
    List<UserCommentList> mUserCommentListMain;
    private RecyclerView recyclerView;
    private CommentListAdapter mAdapter;
    FirebaseFirestore db;
    RatingBar ratingBar;
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

        db = FirebaseFirestore.getInstance();
        title = findViewById(R.id.title);
        video = findViewById(R.id.video);
        buttonviewlocaion = findViewById(R.id.buttonviewlocaion);
        addcomment = findViewById(R.id.addcomment);
        recyclerView = findViewById(R.id.reviewlist);
        RecDImg = findViewById(R.id.RecDImg);
        title.setText(mEntryDetails.getTitle());
        title.setFocusable(false);
        details = findViewById(R.id.details);
        details.setText(mEntryDetails.getDescription());
        details.setFocusable(false);
        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setRating((Float.parseFloat( mEntryDetails.getRate())));
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
               // Toast.makeText(EntryDetailsActivity.this, ratingx+"lll"+rating, Toast.LENGTH_LONG).show();
                DocumentReference washingtonRef = db.collection("events").document(mEntryDetails.getId());
                washingtonRef
                        .update("rate", ""+ratingBar.getRating())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("ll", "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("ll", "Error updating document", e);
                            }
                        });

            }
        });


        // Load the image using Glide
        Glide.with(getApplicationContext())
                .load(mEntryDetails.getImage())
                .into(RecDImg);

        getUserComment();
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
        addcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText taskEditText = new EditText(EntryDetailsActivity.this);
                AlertDialog dialog = new AlertDialog.Builder(EntryDetailsActivity.this)
                        .setTitle("Enter Comment")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                UserCommentList mm = new UserCommentList(mEntryDetails.getUser(),mEntryDetails.getId(),task);
                            onUpdate(mm);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEntryDetails.getVideo()!= null) {
                    Intent i = new Intent(EntryDetailsActivity.this, VideoActivity.class);
                    i.putExtra("video", mEntryDetails.getVideo());
                    startActivity(i);
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
    public void  onUpdate(UserCommentList mUserComment) {
        db.collection("reviewtable")
                .add(mUserComment)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("dd", "DocumentSnapshot written with ID: " + documentReference.getId());

                        getUserComment();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("dd", "Error adding document", e);
                    }
                });
    }
    private void onCreateAdapter(List<UserCommentList> mEntryDetails) {

        mAdapter = new CommentListAdapter( mEntryDetails);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }
    ProgressDialog dialog;
    private void getUserComment() {
         dialog = ProgressDialog.show(EntryDetailsActivity.this, "",
                "Loading. Please wait...", true);
        db.collection("reviewtable")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            mUserCommentListMain = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e("annnnnnnnnnnnavvvvvv", document.getId() + " => " + document.getData());

                                UserCommentList mUserCommentList = document.toObject(UserCommentList.class);
                                //if(mUserCommentList.geteventID().equalsIgnoreCase(mEntryDetails.getId())) {
                                    mUserCommentListMain.add(mUserCommentList);
                                //}

                            }
                            onCreateAdapter(mUserCommentListMain);
                            dialog.dismiss();
                        } else {
                            Log.w("no", "Error getting documents.", task.getException());
                            dialog.dismiss();
                        }
                    }
                });
    }

public  void  playvideo(){

}


}