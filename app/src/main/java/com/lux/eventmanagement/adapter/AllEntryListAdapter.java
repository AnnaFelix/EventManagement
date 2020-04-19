package com.lux.eventmanagement.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lux.eventmanagement.EntryDetailsActivity;
import com.lux.eventmanagement.R;

import java.util.List;



public class AllEntryListAdapter extends RecyclerView.Adapter<AllEntryListAdapter.ViewHolder> {

    private static List<EntryDetails> dataSet;
    Context mcontext;

    public AllEntryListAdapter(Context mcontext
            , List<EntryDetails> os_versions) {
        dataSet = os_versions;
        this.mcontext = mcontext;
    }


    @Override
    public AllEntryListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
// create a new view
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.layout_entry_row, null);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AllEntryListAdapter.ViewHolder viewHolder, int i) {

        final EntryDetails margs = dataSet.get(i);
        final String username  =  (dataSet.get(i).getUser()!= null)? ""+dataSet.get(i).getUser().getName():" ";
        final String description  =  (dataSet.get(i).getDescription()!= null)? ""+dataSet.get(i).description: "";
        final String title  =  (dataSet.get(i).title!= null)?dataSet.get(i).title: "";
        final Uri imageurl  =  (dataSet.get(i).getImage()!= null)? Uri.parse(dataSet.get(i).getImage()): null;

        viewHolder.name.setText(""+username);
        viewHolder.followers.setText(""+dataSet.get(i).getUser().email);
        viewHolder.description.setText(description);
        viewHolder.title.setText(""+title);



        Glide.with(mcontext)
                .load(imageurl)
                .placeholder(R.drawable.img)
                .into(viewHolder.thumbnail);


         viewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Let's share..."+username +" has uploded new entry. For more details please contact us!");
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, String.valueOf(imageurl));
                intent.setType("text/plain");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                mcontext.startActivity(Intent.createChooser(intent, "Send To"));

            }
        });
        viewHolder.viewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mcontext, EntryDetailsActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("EntryData", margs);
                mIntent.putExtras(mBundle);
                mcontext.startActivity(mIntent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

         TextView name;
         TextView followers,title,description;
         ImageView userimg,thumbnail;
         Button share,viewData;
         CardView cardView;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            name = (TextView) itemLayoutView
                    .findViewById(R.id.textViewMainName);
            followers = (TextView) itemLayoutView
                    .findViewById(R.id.textViewType);
            title = (TextView) itemLayoutView
                    .findViewById(R.id.title);
            description = (TextView) itemLayoutView
                    .findViewById(R.id.description);
            userimg = (ImageView) itemLayoutView
                    .findViewById(R.id.imageViewRecipe);
            thumbnail = (ImageView) itemLayoutView
                    .findViewById(R.id.thumbnail);
            share = (Button) itemLayoutView
                    .findViewById(R.id.share);
            viewData = (Button) itemLayoutView
                    .findViewById(R.id.view);
            cardView = (CardView) itemLayoutView
                    .findViewById(R.id.cardView);
        }

    }
 /*   public Bitmap ViewShot(View v) {
        int height = v.getHeight();
        int width = v.getWidth();
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas (b);
        v.layout(0, 0 , v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }*/
}