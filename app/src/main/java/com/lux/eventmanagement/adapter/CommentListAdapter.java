package com.lux.eventmanagement.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lux.eventmanagement.R;


import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {

    private static List<UserCommentList> dataSet;

    public CommentListAdapter(List<UserCommentList> mUserCommentList) {

        dataSet = mUserCommentList;
    }


    @Override
    public CommentListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
// create a new view
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.custom_usercommentlist_main, null);

        // create ViewHolder

        CommentListAdapter.ViewHolder viewHolder = new CommentListAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommentListAdapter.ViewHolder viewHolder, int i) {

        viewHolder.tv_name.setText(""+dataSet.get(i).user.name);
       // viewHolder.iconView.
        viewHolder.tv_comment.setText(""+dataSet.get(i).usercomment);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name,tv_comment;
        public ImageView icon_user;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            tv_name = (TextView) itemLayoutView
                    .findViewById(R.id.tv_name);
            tv_comment = (TextView) itemLayoutView
                    .findViewById(R.id.tv_comment);
            icon_user = (ImageView) itemLayoutView
                    .findViewById(R.id.icon_user);

            itemLayoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });


        }

    }
}