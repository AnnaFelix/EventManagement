package com.lux.eventmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lux.eventmanagement.R;

import java.util.List;


public class MyEntryListAdapter extends RecyclerView.Adapter<MyEntryListAdapter.ViewHolder> {

    private static List<EntryDetails> dataSet;
    Context mcontext;

    public MyEntryListAdapter(Context mcontext
            , List<EntryDetails> os_versions) {
        dataSet = os_versions;
        this.mcontext = mcontext;
    }


    @Override
    public MyEntryListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
// create a new view
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.layout_entry_row, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MyEntryListAdapter.ViewHolder viewHolder, int i) {
        /*final EntryDetails margs = dataSet.get(i);
        viewHolder.tvVersionName.setText(""+dataSet.get(i).name);
        viewHolder.textViewType.setText(""+dataSet.get(i).authername);

        Glide.with(mcontext)
                .load(dataSet.get(i).image)
                *//*.placeholder(R.drawable.frame_foodbg)
                .error(R.drawable.frame_foodbg)*//*
                .into(viewHolder.imageViewRecipe);

        viewHolder.imageViewRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*RecipeDetailsFragment mRecipeDetailsFragment = new RecipeDetailsFragment();
                Bundle args = new Bundle();
                args.putSerializable("RecipesData", margs);
                mRecipeDetailsFragment.setArguments(args);
                HomeActivity.mLoadFragmentListner.onLoadFragment(mRecipeDetailsFragment);*//*
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvVersionName;
        public TextView textViewType,title;
        public ImageView imageViewRecipe;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

           /* tvVersionName = (TextView) itemLayoutView
                    .findViewById(R.id.textViewMainName);
            textViewType = (TextView) itemLayoutView
                    .findViewById(R.id.textViewType);
            imageViewRecipe = (ImageView) itemLayoutView
                    .findViewById(R.id.imageViewRecipe);*/
        }

    }
}