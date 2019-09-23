package com.test.shooting;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;

import java.util.Random;

public class shoot1Activity extends AppCompatActivity {

    private Scene scene;
    private Camera camera;
    private ModelRenderable bulletRenderable;
    private boolean shouldStartTimer=true;
    private int balloonsLeft=20;
    private Point point;
    private TextView balloonleftTxt;
    private SoundPool soundPool;
    private int sound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoot1);

        CustomArFragment arFragment= (CustomArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

        scene=arFragment.getArSceneView().getScene();
        camera=scene.getCamera();

        addBalloonsToScene();
    }

    private void addBalloonsToScene() {
        ModelRenderable
                .builder()
                .setSource(this, Uri.parse("balloon.sfb"))
                .build()
                .thenAccept(renderable ->{
                    for (int i=0; i<20; i++){
                        Node node=new Node();
                        node.setRenderable(renderable);
                        scene.addChild(node);

                        Random random=new Random();
                        int x=random.nextInt(10);
                        int z=random.nextInt(10);
                        int y=random.nextInt(20);

                        //z=-z;

                        node.setWorldPosition(new Vector3(
                                (float)x,
                                y/10f,
                                (float)z
                        ));
                    }
                });
    }
}
