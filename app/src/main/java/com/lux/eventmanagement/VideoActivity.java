package com.lux.eventmanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {
    VideoView video;
    String video_url ;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        video = (VideoView)findViewById(R.id.video);
        Intent i = VideoActivity.this.getIntent();
        video_url = i.getStringExtra("video");
        pd = new ProgressDialog(VideoActivity.this);
        pd.setMessage("Buffering video please wait...");
        pd.show();

        Uri uri = Uri.parse(video_url);
        video.setVideoURI(uri);
        video.start();

        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //close the progress dialog when buffering is done
                pd.dismiss();
            }
        });
    }
}