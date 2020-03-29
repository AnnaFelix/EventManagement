package com.lux.eventmanagement;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lux.eventmanagement.fragments.ProfileUserData;

import java.lang.reflect.Type;


public class Utils {

    public static final int TAG_MAINKIST = 0;
    public static final int TAG_MYLIST = 1;
    public static final int  TAG_PROFILE = 2;
    public static final int NOTIFICATION = 3;
    public static final int SETTING = 4;

    public static final int TAG_ADD_NEWRECIPE = 18;
    public static final int PICK_IMAGE_CAMERA = 22;
    public static final int PICK_IMAGE_GALLERY = 11;
    public static final int PERMISSION_CAMERA = 15;



    public static void onSaveUSerID(Context context, ProfileUserData user){
        //save user data
        Gson gson = new Gson();
        String hashMapString = gson.toJson(user);

        //save in shared prefs
        SharedPreferences prefs = context.getSharedPreferences("user", context.MODE_PRIVATE);
        prefs.edit().putString("userdata", hashMapString).apply();
        String storedHashMapString = prefs.getString("userdata", "oopsDintWork");
        Log.e("aaaaaaaaaaaaa", "storedHashMapString "+storedHashMapString +getUserID(context));

    }
    public static String getUserID(Context context){
        SharedPreferences mPrefs = context.getSharedPreferences("user", Context.MODE_PRIVATE); //add key
        SharedPreferences.Editor prefsEditor = mPrefs.edit();

        String data = mPrefs.getString("userdata", null);
        Gson gson = new Gson();
        Type type = new TypeToken<ProfileUserData>() {}.getType();
        ProfileUserData str = gson.fromJson(data, type);
        String val = (str.getId()!=null)?str.getId(): "";
        return  val;

    }
    public static String getUserGmail(Context context){
        SharedPreferences mPrefs = context.getSharedPreferences("user", Context.MODE_PRIVATE); //add key
        SharedPreferences.Editor prefsEditor = mPrefs.edit();

        String data = mPrefs.getString("userdata", null);
        Gson gson = new Gson();
        Type type = new TypeToken<ProfileUserData>() {}.getType();
        ProfileUserData str = gson.fromJson(data, type);
        String val = (str.getEmail()!=null)?str.getEmail(): "";
        return  val;

    }



    public static void onSaveUSerProfile(Context context, ProfileUserData user){
        //save user data
        Gson gson = new Gson();
        String hashMapString = gson.toJson(user);

        //save in shared prefs
        SharedPreferences prefs = context.getSharedPreferences("userprofile", context.MODE_PRIVATE);
        prefs.edit().putString("userprofiledata", hashMapString).apply();
        String storedHashMapString = prefs.getString("userprofiledata", "oopsDintWork");
        Log.e("aaaaaaaaaaaaa", "userprofiledata "+storedHashMapString );

    }
    

    public static void getUserDetailsFromServer(final Context context, FirebaseFirestore db, final OnActionComplete onActionComplete){
        String userId = getUserID(context);
        DocumentReference docRef = db.collection("users").document(userId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ProfileUserData profileUserData = documentSnapshot.toObject(ProfileUserData.class);
                onSaveUSerProfile(context,profileUserData);
                onActionComplete.onAction(true);
            }
        });

    }

    public  static void logoutUser(Context context){
        SharedPreferences settings = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        settings.edit().clear().commit();
        SharedPreferences userProfile = context.getSharedPreferences("userprofile", Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }

    public  static ProfileUserData userDetailsFromPreference(Context context){
        SharedPreferences mPrefs = context.getSharedPreferences("userprofile", Context.MODE_PRIVATE); //add key

        String data = mPrefs.getString("userprofiledata", null);
        Gson gson = new Gson();
        Type type = new TypeToken<ProfileUserData>() {}.getType();
        ProfileUserData str = gson.fromJson(data, type);

        return str;
    }
    public interface OnActionComplete {
        void onAction(boolean actionDone);
    }
    public static String getRealPathFromURI(Activity mContext, Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = mContext.managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}
