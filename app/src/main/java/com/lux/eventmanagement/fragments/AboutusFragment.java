package com.lux.eventmanagement.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.lux.eventmanagement.R;
import com.lux.eventmanagement.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class AboutusFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Context mContext;

    private ArrayList<Integer> images;
    private BitmapFactory.Options options;
    private ViewPager viewPager;
    private View btnNext, btnPrev;
    private TextView text_aboutus;
    private FragmentStatePagerAdapter adapter;
    private final static int[] resourceIDs = new int[]{R.drawable.aa, R.drawable.bb,
            R.drawable.cc};

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
        View view = inflater.inflate(R.layout.fragment_aboutus, container, false);
        //onSetContent();
        //getListItems();


        images = new ArrayList<>();

        //find view by id
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        btnNext = view.findViewById(R.id.next);
        btnPrev = view.findViewById(R.id.prev);
        text_aboutus = view.findViewById(R.id.text_aboutus);


        btnPrev.setOnClickListener(onClickListener(0));
        btnNext.setOnClickListener(onClickListener(1));
        text_aboutus.setText("Food should be available to everyone. No one should sleep hungry. " +
                "his application is designed to allow people to help needy people by providing them quality food and motivate them to " +
                "avoid wastage of food. Anyone can use this platform to provide food to those people who are not able to buy food. " +
                "Also, anyone can volunteer to help in delivering the food. Any Non-Profit Organization or other organization which works" +
                " for needy people can contact to register themselves with us in order to get quality food. " +
                "Some people are waiting for your help, Lets do something for them.");

        setImagesData();

        // init viewpager adapter and attach
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), images);
        viewPager.setAdapter(adapter);
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
        inflateThumbnails();
        return view;
    }

    private View.OnClickListener onClickListener(final int i) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i > 0) {
                    //next page
                    if (viewPager.getCurrentItem() < viewPager.getAdapter().getCount() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    }
                } else {
                    //previous page
                    if (viewPager.getCurrentItem() > 0) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                    }
                }
            }
        };
    }

    private void setImagesData() {
        for (int i = 0; i < resourceIDs.length; i++) {
            images.add(resourceIDs[i]);
        }
    }

    private void inflateThumbnails() {
        for (int i = 0; i < images.size(); i++) {
            View imageLayout = getLayoutInflater().inflate(R.layout.item_image, null);
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.img_thumb);
            imageView.setOnClickListener(onChagePageClickListener(i));
            options = new BitmapFactory.Options();
            options.inSampleSize = 3;
            options.inDither = false;
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), images.get(i), options );
            imageView.setImageBitmap(bitmap);
            //set to image view
            imageView.setImageBitmap(bitmap);
            //add imageview
        }
    }

    private View.OnClickListener onChagePageClickListener(final int i) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(i);
            }
        };
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
        void onFragmentInteraction(Uri uri);
    }

    class SliderTimer extends TimerTask {

        @Override
        public void run() {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (viewPager.getCurrentItem() < images.size() - 1) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }
    }
}
