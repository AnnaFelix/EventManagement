package com.lux.eventmanagement.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lux.eventmanagement.HomeMainActivity;
import com.lux.eventmanagement.R;
import com.lux.eventmanagement.Utils;


public class ProviderProfile extends Fragment {//get the spinner from the xml.
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText editTextName;
    EditText editTextEmailAddress;
    EditText editTextPhonenumber;
    EditText editTextAddress;
    EditText editTextAboutme;
    Button button;
    Button buttonsave;
    FirebaseFirestore db;


    Context mContext;
    //user details
    ProfileUserData mprofileUserData;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
        ////////////////////////////////////////////////////////////
        mContext = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_activity_jehan, container, false);
        editTextName = (EditText)view.findViewById(R.id.editTextName);
        editTextEmailAddress = (EditText)view.findViewById(R.id.editTextEmailAddress);
        editTextPhonenumber = (EditText)view.findViewById(R.id.editTextPhonenumber);
        editTextAddress = (EditText)view.findViewById(R.id.editTextAddress);
        editTextAboutme = (EditText)view.findViewById(R.id.editTextAboutme);
        button = (Button)view.findViewById(R.id.button);
        buttonsave = (Button)view.findViewById(R.id.buttonsave);


        mprofileUserData = Utils.userDetailsFromPreference(getActivity());
        if(mprofileUserData!= null) {
            editTextAddress.setText(mprofileUserData.getAddress());
            editTextName.setText(mprofileUserData.getName());
            editTextAboutme.setText(mprofileUserData.getAboutme());
            //editTextْUserTyp.setText(mprofileUserData.getUserType());
            editTextPhonenumber.setText(mprofileUserData.getPhonenumber());
        }

        editTextEmailAddress.setText(Utils.getUserGmail(getActivity()));

        editTextEmailAddress.setFocusable(false);
        //add details
        db = FirebaseFirestore.getInstance();
        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String profileid = Utils.getUserID(getActivity());
                ProfileUserData user = new ProfileUserData(profileid,editTextAddress.getText().toString(),
                        editTextName.getText().toString(),editTextAboutme.getText().toString(),
                        editTextPhonenumber.getText().toString(),editTextEmailAddress.getText().toString());
                saveDetails(user);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextAddress.setText(mprofileUserData.getAddress());
                editTextName.setText(mprofileUserData.getName());
                editTextAboutme.setText(mprofileUserData.getAboutme());
                editTextPhonenumber.setText(mprofileUserData.getPhonenumber());
                editTextEmailAddress.setText(mprofileUserData.getAddress());

            }
        });

        return view;
    }


    private void saveDetails(final ProfileUserData user){
        db.collection("users").document(user.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        DocumentReference washingtonRef = db.collection("users").document(user.getId());

                        washingtonRef
                                .set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("aaa", "DocumentSnapshot successfully updated!");
                                        Toast.makeText(getActivity(),"Uploaded succssfuly", Toast.LENGTH_SHORT).show();

                                        Utils.getUserDetailsFromServer(getActivity(), db, HomeMainActivity.mOnActionComplete);

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("aaa", "Error updating document", e);

                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Err", "Error deleting document", e);
                        Toast.makeText(getActivity(),"uploaded unsuccessfully", Toast.LENGTH_SHORT).show();
                    }
                });



    }



    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(boolean profilesaved);
    }


}

