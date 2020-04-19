package com.lux.eventmanagement.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lux.eventmanagement.R;
import com.lux.eventmanagement.Utils;
import com.lux.eventmanagement.adapter.EntryDetails;
import com.lux.eventmanagement.adapter.LocationTrack;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class AddEventDetailsFragment extends Fragment   {

    private static final String TAG = "ggg >.>";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private final int PICK_IMAGE_REQUEST = 10; //71 img
    private ImageView imageView,imgView; //img
    private ImageView videoViewSelect; //img
    private Uri filePath; //img
    String imageUrlFinal;
    String videourlFinal;


    //Firebase
    FirebaseStorage storage; //= FirebaseStorage.getInstance();
    StorageReference storageReference;  //= storage.getReference();
    //getReferenceFromUrl("gs://food4all-a993a.appspot.com");


    FirebaseFirestore db;
    int pos;
    ScrollableNumberPicker num;
    TimePickerDialog picker_T;
    DatePickerDialog picker;
    TextView tvw, tvw1;
    Context mContext;
    int mHour;
    int mMinute;
    ProfileUserData mprofileUserData;
    String savedDocumentId = "";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText E555_S, E5555_C;

    EditText description, titleMain, locationtxt;
    private Button btnGet, time_buttn, save;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
    double longitudeVal ;
    double latitudeVal ;
    private Bitmap bitmap;
    private File destination,destinationvideoPath = null;
    private InputStream inputStreamImg;
    private String imgPath, videoPath = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mContext = getActivity();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_addevent, container, false);


        description = view.findViewById(R.id.description);
        titleMain = view.findViewById(R.id.title);
        locationtxt = view.findViewById(R.id.locationtxt);

        mprofileUserData = Utils.userDetailsFromPreference(getActivity());
        //Initialize Views

        imageView = (ImageView) view.findViewById(R.id.imgViewSelect);
        imgView = (ImageView) view.findViewById(R.id.imgView);
        videoViewSelect = (ImageView) view.findViewById(R.id.videoViewSelect);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!titleMain.getText().toString().isEmpty()) {
                    selectImage();
                }else{
                    Toast.makeText(getActivity(),"Mention Name",Toast.LENGTH_LONG).show();
                }

            }
        });
        videoViewSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!titleMain.getText().toString().isEmpty()) {
                    selectVideo();
                }else{
                    Toast.makeText(getActivity(),"Mention Name",Toast.LENGTH_LONG).show();
                }

            }
        });
        save = (Button) view.findViewById(R.id.saveBtn);
        save.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (!titleMain.getText().toString().isEmpty()) {

                    String des = "" + description.getText().toString();
                    String tle = titleMain.getText().toString();
                    ProfileUserData user = Utils.userDetailsFromPreference(getActivity());

                    LatLng latlong = getLocationFromAddress(locationtxt.getText().toString());
                    if (latlong != null) {
                        longitudeVal = latlong.longitude;
                        latitudeVal = latlong.latitude;
                    } else {
                        longitudeVal = 0.0;
                        latitudeVal = 0.0;
                    }
                    if (latlong == null && !(locationtxt.getText().toString().isEmpty())) {
                        Toast.makeText(getActivity(),
                                "Invalid Location!",
                                Toast.LENGTH_SHORT).show();  //I have add User Profile data so that we will get whole user details

                    } else {
                        Log.e("aa",imageUrlFinal+" "+videourlFinal);
                        EntryDetails details = new EntryDetails(Utils.getUserGmail(getActivity()), des, imageUrlFinal, tle, longitudeVal, latitudeVal, user, null, "0", videourlFinal);
                        save.setText("Uploading...");
                        //notifyDataSetChanged();
                        saveData(details);
                    }
                }else {
                    Toast.makeText(getActivity(),
                            "Please enter data!",
                            Toast.LENGTH_SHORT).show();  //I have add User Profile data so that we will get whole user details

                }
            }
        });

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }



        DocumentReference washingtonRef = db.collection("cities").document("DC");

// Set the "isCapital" field of the city 'DC'
        washingtonRef
                .update("capital", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

        return view;
    }


    private void saveData(EntryDetails details) {
        if(!titleMain.getText().toString().isEmpty()) {
            db.collection("events")
                    .add(details)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            save.setText("Done");
                            getActivity().onBackPressed();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                            save.setText("Error!");
                        }
                    });
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Utils.PICK_IMAGE_CAMERA) {
            try {
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                Log.e("Activity", "Pick from Camera::>>> ");

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(Environment.getExternalStorageDirectory() + "/" +
                        getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imgPath = destination.getAbsolutePath();
                imgView.setImageBitmap(bitmap);
                uploadImage(imgPath);
                //MyProfileFragment.mImageSelectionCompleteListner.onImageSelectionComplete(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == Utils.PICK_IMAGE_GALLERY) {
            try {
                Uri selectedImage = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                Log.e("Activity", "Pick from Gallery::>>> ");

                imgPath = Utils.getRealPathFromURI(getActivity(), selectedImage);
                destination = new File(imgPath.toString());
                Log.e("Activity", "Pick from Gallery::>>> ");
                // MyProfileFragment.mImageSelectionCompleteListner.onImageSelectionComplete(bitmap);
                imgView.setImageBitmap(bitmap);
                uploadImage(imgPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(requestCode == Utils.PICK_VIDEO_CAMERA){
            try {
                Uri selectedImage = data.getData();

                videoPath = Utils.getRealPathFromURI(getActivity(), selectedImage);
                destinationvideoPath = new File(videoPath.toString());
                Log.e("Activity", "Pick from Gallery::>>> ");
                // MyProfileFragment.mImageSelectionCompleteListner.onImageSelectionComplete(bitmap);
                //imageview.setImageBitmap(bitmap);
                uploadVideo(videoPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(String pathh) {
        final String name = "image"+titleMain.getText().toString()+new Random().nextInt(100);

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference uploadeRef = storageRef.child(name);
        File file = new File(pathh);
        Uri fileUri = Uri.fromFile(file);

        progressDialog = new ProgressDialog(getActivity(),
                ProgressDialog.THEME_HOLO_DARK);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        uploadeRef.putFile(fileUri).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception exception) {
                progressDialog.cancel();
                Toast.makeText(getActivity(),
                        "Upload failed",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.cancel();
                //add file name to firestore database
                Toast.makeText(getActivity(),
                        "File has been uploaded to cloud storage",
                        Toast.LENGTH_SHORT).show();
                getImage(name);


            }
        });
    }
    private void uploadVideo(String pathh) {
        final String name = "video"+titleMain.getText().toString()+new Random().nextInt(100);
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference uploadeRef = storageRef.child(name);
        File file = new File(pathh);
        Uri fileUri = Uri.fromFile(file);

        progressDialog = new ProgressDialog(getActivity(),
                ProgressDialog.THEME_HOLO_DARK);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        uploadeRef.putFile(fileUri).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception exception) {
                progressDialog.cancel();
                Toast.makeText(getActivity(),
                        "Upload failed",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.cancel();
                //add file name to firestore database
                Toast.makeText(getActivity(),
                        "File has been uploaded to cloud storage",
                        Toast.LENGTH_SHORT).show();
                getVideo(name);


            }
        });
    }
    private void getImage(final String name){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // StorageReference storageRef = storage.getReferenceFromUrl("gs://tutsplus-firebase.appspot.com").child(name);

        /*     final long ONE_MEGABYTE = 1024 * 1024;
            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Log.e("aaa "+name,"bitmap "+bitmap);
                }
            });*/
        StorageReference storageRef = storage.getReference();
        storageRef.child(name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Log.e("aaa "+name,"uri "+uri);
                imageUrlFinal = uri.toString();
                Glide.with(getActivity())
                        .load(imageUrlFinal)
                        .asBitmap()

                        .into(imgView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.e("aaimagea "+name,"exception "+exception);
            }
        });



    }
    private void getVideo(final String name){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // StorageReference storageRef = storage.getReferenceFromUrl("gs://tutsplus-firebase.appspot.com").child(name);

        /*     final long ONE_MEGABYTE = 1024 * 1024;
            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Log.e("aaa "+name,"bitmap "+bitmap);
                }
            });*/
        StorageReference storageRef = storage.getReference();
        storageRef.child(name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Log.e("aaa video "+name,"uri "+uri);
                videourlFinal = uri.toString();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.e("aavideoea "+name,"exception "+exception);
            }
        });

    }
    ProgressDialog progressDialog;
    private void selectImage() {
        try {
            PackageManager pm = getActivity().getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getActivity().getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, Utils.PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, Utils.PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(getActivity(), "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Camera ", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }
    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (ActivityCompat.checkSelfPermission(getContext(),permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            locationTrack.stopListener();
        }catch (Exception e){

        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    public LatLng getLocationFromAddress( String strAddress) {

        Geocoder coder = new Geocoder(getActivity());
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            Log.e("mmmmmm",""+address);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }
    private void selectVideo() {
        try {
            PackageManager pm = getActivity().getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getActivity().getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = { "Choose From Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent intent = new Intent (Intent.ACTION_PICK,MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("video/*");
                            startActivityForResult(Intent.createChooser(intent,"Select Video"),Utils.PICK_VIDEO_CAMERA);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(getActivity(), "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Camera ", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}





