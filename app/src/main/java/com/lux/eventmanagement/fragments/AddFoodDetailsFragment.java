package com.lux.eventmanagement.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lux.eventmanagement.R;
import com.lux.eventmanagement.Utils;
import com.lux.eventmanagement.adapter.EntryDetails;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;





public class AddFoodDetailsFragment extends Fragment {

    private static final String TAG = "Rudddddddddddd >.>";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private final int PICK_IMAGE_REQUEST = 10; //71 img
    private Button btnChoose, btnUpload; //img
    private ImageView imageView; //img
    private Uri filePath; //img
    String imageUrl;


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

    EditText description, title;
    private Button btnGet, time_buttn, save;

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
        View view = inflater.inflate(R.layout.layout_addfood, container, false);


        description = view.findViewById(R.id.description);
        title = view.findViewById(R.id.title);





        mprofileUserData = Utils.userDetailsFromPreference(getActivity());



        //Initialize Views
        btnChoose = (Button) view.findViewById(R.id.btnChoose);
        btnUpload = (Button) view.findViewById(R.id.btnUpload);
        imageView = (ImageView) view.findViewById(R.id.imgView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!title.getText().toString().isEmpty()) {
                    selectImage();
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

                String des = ""+description.getText().toString();
                String tle = title.getText().toString();
                ProfileUserData user = Utils.userDetailsFromPreference(getActivity());


                //I have add User Profile data so that we will get whole user details
                EntryDetails details = new EntryDetails(  Utils.getUserGmail(getActivity()),des,  imageUrl  ,  tle,  user);
                save.setText("clicked");
                //notifyDataSetChanged();
                saveData(details);
            }
        });

        return view;
    }


    private void saveData(EntryDetails details) {
        db.collection("events")
                .add(details)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }



    private Bitmap bitmap;
    private File destination = null;
    private InputStream inputStreamImg;
    private String imgPath = null;

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
                // imageview.setImageBitmap(bitmap);
                //MyProfileFragment.mImageSelectionCompleteListner.onImageSelectionComplete(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == Utils.PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                Log.e("Activity", "Pick from Gallery::>>> ");

                imgPath = Utils.getRealPathFromURI(getActivity(), selectedImage);
                destination = new File(imgPath.toString());
                Log.e("Activity", "Pick from Gallery::>>> ");
                // MyProfileFragment.mImageSelectionCompleteListner.onImageSelectionComplete(bitmap);
                //imageview.setImageBitmap(bitmap);
                uploadImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference uploadeRef = storageRef.child(title.getText().toString());
        File file = new File(imgPath);
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
                getImage(title.getText().toString());


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
                imageUrl = uri.toString();
                Glide.with(getActivity())
                        .load(imageUrl)
                        .asBitmap()

                        .into(imageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
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



}





