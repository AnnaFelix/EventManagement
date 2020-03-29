package com.lux.eventmanagement.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.lux.eventmanagement.R;
import com.lux.eventmanagement.adapter.AllEntryListAdapter;
import com.lux.eventmanagement.adapter.EntryDetails;
import com.lux.eventmanagement.adapter.UserLocalDataList;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;



public class HomeListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private RecyclerView recyclerView;
    private AllEntryListAdapter mAdapter;
    Context mContext;
    private GridLayoutManager lLayout;

    FirebaseFirestore db;
    public List<EntryDetails> mEntryDetails;
    ProgressDialog progressDialog;

    public HomeListFragment() {
        // Required empty public constructor
    }


            @Override
            public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        ////////////////////////////////////////////////////////////
        Log.e("aaaaaaaaaaaaa", "onCreate " );
        mContext = getActivity();
        db = FirebaseFirestore.getInstance();
        mEntryDetails = new ArrayList<>();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //onSetContent();
        recyclerView = (RecyclerView) view.findViewById(R.id.mainrecipielist);
        Log.e("aaaaaaaaaaaaa", "onCreateView " );
        getListItems();



        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        void onFragmentInteraction(Uri uri);
    }

    private void onCreateAdapter(List<EntryDetails> mEntryDetails) {

        mAdapter = new AllEntryListAdapter(getActivity(), mEntryDetails);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }


    private void getListItems() {
        CollectionReference usersCollectionRef = db.collection("event");
        //Log.d("aaaaaa",  " => " + usersCollectionRef.getId());

        db.collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e("annnnnnnnnnnnavvvvvv", document.getId() + " => " + document.getData());

                                EntryDetails mEntryDetailsLocal = document.toObject(EntryDetails.class);
                                mEntryDetails.add(mEntryDetailsLocal);
                            }

                            onCreateAdapter(mEntryDetails);

                        } else {
                            Log.w("no", "Error getting documents.", task.getException());
                             getOfflineData(true);

                        }
                    }
                });
    }

    private void getOfflineData(boolean isDataOnlineAvailable) {
        if (isDataOnlineAvailable) {
            Gson gson = new Gson();
            String json = null;
            try {
                InputStream inputStream = mContext.getAssets().open("entries.json");
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                inputStream.close();
                json = new String(buffer, "UTF-8");

            } catch (IOException e) {
                e.printStackTrace();

            }
            String list = json;

            UserLocalDataList eventResponseList = gson.fromJson(list, UserLocalDataList.class);
            mEntryDetails = eventResponseList.getData();
            //Log.e("aaaaaaaaaaaaa", "e " + list);
            onCreateAdapter(mEntryDetails);
        }
    }


}
