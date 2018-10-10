package com.life.shelter.people.homeless;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class FAQ extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        VideoView vviw= findViewById(R.id.videoView);
        MediaController mediaController=new MediaController(this);
        mediaController.setAnchorView(vviw);
        Uri uri=Uri.parse("android.resource://com.life.shelter.people.homeless/"+R.raw.test);
        vviw.setMediaController(mediaController);
        vviw.setVideoURI(uri);
        vviw.requestFocus();
        vviw.start();

    }
}
