package com.test.shooting;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.ar.core.Anchor;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.SkeletonNode;
//import com.google.ar.sceneform.animation.ModelAnimator;
import com.google.ar.sceneform.collision.Ray;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.AnimationData;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.ArFragment;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;

public class ShootingActivityAnimation extends AppCompatActivity {

    private Scene scene;
    private Camera camera;
    private ModelRenderable bulletRenderable;
    private boolean shouldStartTimer=true;
    private int balloonsLeft=20;
    private Point point;
    private TextView balloonleftTxt;
    private SoundPool soundPool;
    private int sound;
    private int airGun;
    private int birdSound;
    protected String timeInfo;
    //private ModelAnimator modelAnimator;
    private int i=0;
    private ImageView gunImage;

    //Bitmap bitmap= BitmapFactory.decodeFile("drawable/gun.jpg");

//    private RelativeLayout myLayout=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        gunImage=(ImageView)findViewById(R.id.gun);
        //gunImage.setImageResource(R.drawable.gun);
        //gunImage.setImageBitmap(BitmapFactory.decodeFile("drawable/gun.jpg"));
        Display display=getWindowManager().getDefaultDisplay();
        point=new Point();
        display.getRealSize(point);

        setContentView(R.layout.activity_shooting_animation);

        loadSoundPool();
        balloonleftTxt=findViewById(R.id.balloonsCntTxt);

        CustomArFragment arFragment=
                (CustomArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

        scene=arFragment.getArSceneView().getScene();
        camera=scene.getCamera();

        addBalloonsToScene();
        //addMoveToScene();
        buildBulletModel();

        Button shoot=findViewById(R.id.shootButton);


//        myLayout=(RelativeLayout) findViewById(R.id.shootingLayout);
//        myLayout.setOnClickListener(v->{
//            Toast.makeText(ShootingActivity.this,"Shoot",Toast.LENGTH_SHORT).show();
//            if(shouldStartTimer){
//                startTimer();
//                shouldStartTimer=false;
//            }
//
//            shoot();
//        });



        shoot.setOnClickListener(v-> {

                if(shouldStartTimer){
                    startTimer();
                    shouldStartTimer=false;
                }

                shoot();

        });



    }

    private void loadSoundPool() {

        AudioAttributes audioAttributes=new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();

        soundPool=new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();

        sound=soundPool.load(this,R.raw.blop_sound,1);
        airGun=soundPool.load(this,R.raw.air_gun_shot,2);
        birdSound=soundPool.load(this,R.raw.bird_chirping_2,3);



    }

    private void shoot(){

        Ray ray=camera.screenPointToRay(point.x/2f,point.y/2f);
        Node node=new Node();
        node.setRenderable(bulletRenderable);
        scene.addChild(node);
        soundPool.play(airGun,1f,1f,1,0,1f);

        new Thread(()->{

            for (int i=0; i<200; i++){

                int finalI1 = i;
                runOnUiThread(()->{

                    Vector3 vector3=ray.getPoint(finalI1 *0.1f);
                    node.setWorldPosition(vector3);

                    Node nodeInContact=scene.overlapTest(node);

                    if (nodeInContact!=null){

                        balloonsLeft--;
                        balloonleftTxt.setText("Monsters Left:"+balloonsLeft);
                        scene.removeChild(nodeInContact);

                        soundPool.play(sound,1f,1f,1,0,1f);


                    }


                });
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            runOnUiThread(()->scene.removeChild(node));


        }).start();

    }

    private void startTimer(){
        TextView timer=findViewById(R.id.timerText);
        new Thread(()->{
            int seconds=0;
            int minitesPassed=0;
            int secondsPassed=0;

            while (balloonsLeft>0){

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                seconds++;
                int currentMinitesPassed = seconds/60;
                int currentSecondsPassed=seconds%60;

                runOnUiThread(()->timer.setText(currentMinitesPassed +":"+currentSecondsPassed));

                minitesPassed=currentMinitesPassed;
                secondsPassed=currentSecondsPassed;
            }
            timeInfo=minitesPassed+":"+secondsPassed;
            //Toast.makeText(ShootingActivity.this,"Congrats!",Toast.LENGTH_SHORT).show();
            Intent inToleader=new Intent(ShootingActivityAnimation.this,GameResultActivity.class);
            inToleader.putExtra("EXTRA_MESSAGE",timeInfo);
            startActivity(inToleader);
            finish();
        }).start();
    }

    private void buildBulletModel() {

        Texture
                .builder()
                .setSource(this, R.drawable.texture)
                .build()
                .thenAccept(texture -> {
                    MaterialFactory
                            .makeOpaqueWithTexture(this, texture)
                            .thenAccept(material -> {
                                  bulletRenderable=ShapeFactory
                                          .makeSphere(0.01f,
                                                  new Vector3(0f,0f,0f)
                                                  ,material);
                    });
        });
    }

    private void addBalloonsToScene() {
        ModelRenderable
                .builder()
                .setSource(this, Uri.parse("bird.sfb"))
                .build()
                .thenAccept(renderable ->{
                    ArrayList<Node> nodes=new ArrayList<>(20);
                    for (int i=0; i<20; i++){
                        nodes.add(i,new Node());

                        //AnchorNode anchorNode=new AnchorNode(anchor);
                        //SkeletonNode skeletonNode=new SkeletonNode();

                        nodes.get(i).setRenderable(renderable);
                        scene.addChild(nodes.get(i));
                        //animateModel(renderable);


                        Random random=new Random();
                        int x=random.nextInt(10);
                        int z=random.nextInt(10);
                        int y=random.nextInt(20);

                        z=-z;

                        nodes.get(i).setWorldPosition(new Vector3(
                                (float)x,
                                y/10f,
                                (float)z
                        ));
                        nodes.get(i).setWorldRotation(Quaternion.axisAngle(new Vector3(0, 1, 0), 90));



                    }
                    new Thread(()->{
                        while(true){

                            Random time=new Random();
                            try {
                                Thread.sleep(time.nextInt(2000));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            soundPool.play(birdSound,1f,1f,2,0,1f);
                            for (int i=0;i<5;i++){
                                runOnUiThread(()->{
                                    Random random=new Random();
                                    int x=random.nextInt(10);
                                    int z=random.nextInt(10);
                                    int y=random.nextInt(20);

                                    z=-z;

                                    nodes.get(random.nextInt(20)).setWorldPosition(new Vector3(
                                            (float)x,
                                            y/10f,
                                            (float)z
                                    ));
                                    nodes.get(random.nextInt(20)).setWorldRotation(Quaternion.axisAngle(new Vector3(0, 1, 0), 90));


                                });
                            };

                        }


                    }).start();


                });
    }

    /*private void addMoveToScene() {
        ModelRenderable
                .builder()
                .setSource(this, Uri.parse("balloon.sfb"))
                .build()
                .thenAccept(renderable -> {

                    new Thread(()->{
                        runOnUiThread(()->{
                            Node node=new Node();
                            Random random=new Random();

                            int z=random.nextInt(10);
                            int y=random.nextInt(20);


                            z=-z;
                            int x=random.nextInt(10);
                            while(true){
                                if (x>=10){
                                    x=0;
                                }
                                node.setWorldPosition(new Vector3(
                                        (float)x,
                                        y/10f,
                                        (float)z
                                ));
                                try {
                                    Thread.sleep(random.nextInt(x*100));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                x=x+1;
                                node.setWorldPosition(new Vector3(
                                        (float)x,
                                        y/10f,
                                        (float)z
                                ));
                            }




                        });
                    }).start();



                });
    }*/




//    private void animateModel(ModelRenderable modelRenderable){
//        if (modelAnimator!=null&& modelAnimator.isRunning()){
//            modelAnimator.end();
//
//            int animationCount=modelRenderable.getAnimationDataCount();
//
//            if (i==animationCount)
//                i=0;
//            AnimationData animationData=modelRenderable.getAnimationData(i);
//
//            modelAnimator =new ModelAnimator(animationData,modelRenderable);
//            modelAnimator.start();
//            i++;
//        }
//    }

   // @Override
    //protected void onStart(){
     //   super.onStart();
        //mFirebaseAuth.addAuthStateListener(mAuthStateListener);
   // }
}
