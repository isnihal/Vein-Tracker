package com.nihalismail.ar_vein_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.ExternalTexture;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;

public class MainActivity extends AppCompatActivity {

    private  ModelRenderable videoRenderable;
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

        ExternalTexture externalTexture = new ExternalTexture();
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.video);
        mediaPlayer.setSurface(externalTexture.getSurface());
        mediaPlayer.setLooping(true);
        ModelRenderable
                .builder()
                .setSource(this,R.raw.video_screen)
                .build()
                .thenAccept(modelRenderable -> {
                    videoRenderable = modelRenderable;
                    videoRenderable.getMaterial().setExternalTexture("videoTexture",externalTexture);
                    videoRenderable.getMaterial().setFloat4("keyColor",new Color(0.01843f,1.0f,0.098f));
                });

        ArFragment arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            AnchorNode anchorNode = new AnchorNode(hitResult.createAnchor());

            if(!mediaPlayer.isPlaying()){
                mediaPlayer.start();

                externalTexture.getSurfaceTexture().setOnFrameAvailableListener(surfaceTexture -> {
                    anchorNode.setRenderable(videoRenderable);
                    externalTexture.getSurfaceTexture().setOnFrameAvailableListener(null);
                });
            }
            else{
                anchorNode.setRenderable(videoRenderable);
            }

            float width = mediaPlayer.getVideoWidth();
            float height  = mediaPlayer.getVideoHeight();

            anchorNode.setLocalScale(new Vector3(HEIGHT *(width/height),HEIGHT,1.0f));

            arFragment.getArSceneView().getScene().addChild(anchorNode);
        });
    }
}
