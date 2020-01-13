package com.nihalismail.ar_vein_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.ExternalTexture;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;

import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {

    private  ViewRenderable viewRenderable;
    private float HEIGHT= 1.25f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ARCore requires camera permission to operate.
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            CameraPermissionHelper.requestCameraPermission(this);
            return;
        }

        // inflate the layout
        View myLayout = LayoutInflater.from(this).inflate(R.layout.ar_video_layout,null);
        VideoView  videoView  = findViewById(R.id.arVideoView);
        videoView.setVideoURI(Uri.parse("https://archive.org/download/Popeye_forPresident/Popeye_forPresident_512kb.mp4"));
        LinearLayout linearLayout  =  (LinearLayout) myLayout.findViewById(R.id.ar_layout);

        //mediaPlayer.setLooping(true);
        ViewRenderable.builder()
                .setView(this, linearLayout)
                .build()
                .thenAccept(renderable -> viewRenderable = renderable);

        ArFragment arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

        AnchorNode anchorNode = new AnchorNode();

        if(!videoView.isPlaying()){
            videoView.start();
            anchorNode.setRenderable(viewRenderable);
        }
        else{
            anchorNode.setRenderable(viewRenderable);
        }

        float width = 1;
        float height  = 1;

        anchorNode.setLocalScale(new Vector3(HEIGHT *(width/height),HEIGHT,1.0f));

        arFragment.getArSceneView().getScene().addChild(anchorNode);
    }
}
