package pancakeninjas.sliceproject;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

/**
 * Created by darkhobbo on 4/7/2016.
 */
public class FragGamePlayerActivity extends Fragment{

    private ConnectActivity connectActivity;
    int width, height, half, playerScore;
    int count, count2;
    float endPoint;
    TranslateAnimation animation;
    ObjectAnimator anim;
    ImageView cube1;
    Button blueButton, redButton, greenButton, yellowButton;
    ArrayList<ImageView> cubeArray;
    ArrayList<ImageView> deleteCubeArray;
    ImageView colour;
    LinearLayout cubes, scoreZone, scoreZone2, scoreTop, scoreBottom, divideBottom, halfBottom;
    TextView score, endScore, endText;
    RelativeLayout mainLayout;
    ImageView yCube, bCube, rCube, gCube, explosionView;
    private View view;
    int countEnd=0;
    AnimationDrawable explode;
    MediaPlayer mp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.frag_game_player, container, false);
        Log.d("onCreateView", "TEST");
        connectActivity = (ConnectActivity)getActivity();

        //arrayLists for storing cubes
        cubeArray = new ArrayList<ImageView>();
        deleteCubeArray = new ArrayList<ImageView>();
        //copies of UI elements
        //cube1 = (ImageView) view.findViewById(R.id.bCube);
        cubes = (LinearLayout) view.findViewById(R.id.cubeLayout);
        mainLayout = (RelativeLayout) view.findViewById(R.id.mainRel);
        scoreZone = (LinearLayout) view.findViewById(R.id.scoreField);
        scoreTop = (LinearLayout) view.findViewById(R.id.scoreLineTop);
        scoreBottom = (LinearLayout) view.findViewById(R.id.scoreLineBottom);
        divideBottom = (LinearLayout) view.findViewById(R.id.divider1);
        halfBottom = (LinearLayout) view.findViewById(R.id.halfBottom);

        score = (TextView) view.findViewById(R.id.scoreText);
        endText = (TextView) view.findViewById(R.id.endRoundText);
        endScore = (TextView) view.findViewById(R.id.endScoretext);

        yCube = (ImageView)view.findViewById(R.id.yCube);
        gCube = (ImageView)view.findViewById(R.id.gCube);
        bCube = (ImageView)view.findViewById(R.id.bCube);
        rCube = (ImageView)view.findViewById(R.id.rCube);
        explosionView = (ImageView) view.findViewById(R.id.explosionView);

        mp = MediaPlayer.create(view.getContext(), R.raw.gamemusic);
        mp.setLooping(true);
        //mp.start();
        count=0;
        count2=0;
        playerScore = 0;
        endPoint = scoreBottom.getY();
        Log.d("endPoint", "end Point = " + endPoint);
        Log.d("endPoint", "end Point = " + scoreBottom.getY());
        getScreenSize();

        return view;
    }

    public void setListeners(){


    }

    public void explosion(){

        try {
            BitmapDrawable frame1 = (BitmapDrawable) getResources().getDrawable(
                    R.drawable.brickexplode1);
            BitmapDrawable frame2 = (BitmapDrawable) getResources().getDrawable(
                    R.drawable.brickexplode2);
            BitmapDrawable frame3 = (BitmapDrawable) getResources().getDrawable(
                    R.drawable.brickexplode3);
            BitmapDrawable frame4 = (BitmapDrawable) getResources().getDrawable(
                    R.drawable.brickexplode4);

            explode = new AnimationDrawable();
            explode.addFrame(frame1, 50);
            explode.addFrame(frame2, 50);
            explode.addFrame(frame3, 50);
            explode.addFrame(frame4, 50);
            explode.setOneShot(true);
            explosionView.setBackground(explode);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

                public void run() {

                    explode.start();

                }
            }, 0);
            Log.d("Explosion", "after animation");
            explode.stop();
            Log.d("Explosion", "after stop()");
        } catch (Exception e) {
            // TODO: handle exception
        }

    }


    //used for when the user collects a cube before the animation has ended
    public void stopCube(View v){
        //if the arrayList isnt empty
        if(!cubeArray.isEmpty()){
            //connectActivity.sendStopCube();
            ImageView temp = (ImageView) v;
            //add cube to deleteCubeArray
            deleteCubeArray.add(temp);
            //set up explosion position
            explosionView.setX(temp.getX());
            explosionView.setY(temp.getY());
            //remove the cube from cubeArray and delete from the screen
            mainLayout.removeView(temp);
            //play explosion
            explosion();
        }
    }

    public void gameEnd(){
        endText.setVisibility(View.VISIBLE);
        endScore.setText("Score: " + playerScore);
        endScore.setVisibility(View.VISIBLE);

    }

    //delete a cube that has ended its animation
    public void deleteCube(){
        //check to make sure the arrayList isnt empty first
        if(!cubeArray.isEmpty()){
            //connectActivity.sendDeleteCube();
            //remove from the arrayList and screen
            ImageView temp = cubeArray.get(0);
            mainLayout.removeView(temp);
            cubeArray.remove(0);
            Log.d("deleteCube","not empty");


        }
    }

    //gets the size of the screen
    public void getScreenSize(){

        width=0;
        height=0;

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        half = width/2;

        Log.d("Height"," " + height);
        Log.d("width"," " + width);

    }

    public void checkIfScore(View v){
        //if there are cubes in cubeArray
        if(!cubeArray.isEmpty()) {
            //copy the first cube
            ImageView temp = (ImageView) v;

            //Log.d("Test","Cube = " + temp.getY());
            //Log.d("Test", "Top = " + scoreTop.getY() + "Bottom = " + scoreBottom.getY());

            //if the cube is in the score zone
            if ((temp.getY() > scoreTop.getY()) && (temp.getY() < scoreBottom.getY())) {
                Log.d("CheckIfScore", "Full score!");
                //remove cube from screen and place it into the deleteCubeArray
                stopCube(v);
                //increase player score
                playerScore += 10;
                score.setText("Score: " + playerScore);
            }
            else if((temp.getY() > scoreBottom.getY()) && (temp.getY() < halfBottom.getY())){
                Log.d("CheckIfScore", "Half score!");
                //remove cube from screen and place it into the deleteCubeArray
                stopCube(v);
                //increase player score
                playerScore += 5;
                score.setText("Score: "+playerScore);
            }
            else{//cube is not in the score zone
                Log.d("CheckIfScore", "oops, you missed!");
                //decrease player score
                playerScore-= 5;
                score.setText("Score: "+playerScore);
            }
        }
    }

    /*public void buttonDisable(){
        blueButton.setEnabled(false);
        redButton.setEnabled(false);
        yellowButton.setEnabled(false);
        greenButton.setEnabled(false);

        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        blueButton.setEnabled(true);
                        redButton.setEnabled(true);
                        yellowButton.setEnabled(true);
                        greenButton.setEnabled(true);
                    }
                });
            }
        }, Constants.delay);
    }*/


    //makes a cube fall
    //TODO: change View v to the message being passed
    public void dropCube(String cube){

        //buttonDisable();
        count++;
        Log.d("moveCube", "count = " + count);
        //int[] location = new int[2];
        //cube1.getLocationOnScreen(location);
        Log.d("moveCube", "Test2");
        ImageView newCube;
        switch(cube){
            case "yellow":
                //create new imageView for the cube
                newCube = new ImageView(view.getContext());
                newCube.setImageResource(R.drawable.brick48yellow);
                //color is a copy of the yellow cube that stays on screen (yCube)
                colour = yCube;
                Log.d("dropCube", colour.toString());
                break;
            case "blue":
                //create new imageView for the cube
                newCube = new ImageView(view.getContext());
                newCube.setImageResource(R.drawable.brick48blue);
                //color is a copy of the yellow cube that stays on screen (yCube)
                colour = bCube;
                break;
            case "green":
                //create new imageView for the cube
                newCube = new ImageView(view.getContext());
                newCube.setImageResource(R.drawable.brick48green);
                //color is a copy of the yellow cube that stays on screen (yCube)
                colour = gCube;
                break;
            case "red":
                //create new imageView for the cube
                newCube = new ImageView(view.getContext());
                newCube.setImageResource(R.drawable.brick48red);
                //color is a copy of the yellow cube that stays on screen (yCube)
                colour = rCube;
                break;
            default:
                newCube = new ImageView(view.getContext());
                newCube.setImageResource(R.drawable.brick48yellow);
                //color is a copy of the yellow cube that stays on screen (yCube)
                colour = yCube;
                break;
        }
        //set X and Y of cube
        Log.d("dropCube", newCube.toString());
        newCube.setX(colour.getX() + cubes.getX());
        newCube.setY(0);

        //set on click listener
        newCube.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("OnTouchTest", "onTouch WORKING!!");
                checkIfScore(v);
                return false;
            }
        });

        /*
        newCube.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //do stuff
                Log.d("OnClickTest", "onClick WORKING!!");
                checkIfScore();
            }
        });
        */

        //newCube.setId(count);
        //add the cube to the arraylist and add to the screen
        cubeArray.add(newCube);
        mainLayout.addView(newCube);

        Log.d("moveCube", "Test3");

        //LinearInterpolator linInt = new LinearInterpolator();

        //animator for the cube
        anim = ObjectAnimator.ofFloat(newCube, "Y", 0, halfBottom.getY()).setDuration(Constants.speed);
        Log.d("dropCube","anim interpolator = " + anim.getInterpolator());
        anim.setInterpolator(new LinearInterpolator());
        //animator listener
        anim.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //if the cube has been scored (in the deleteCube arraylist)
                /*
                if (!deleteCubeArray.isEmpty()) {

                    ImageView temp = deleteCubeArray.get(0);
                    mainLayout.removeView(temp);
                    deleteCubeArray.remove(0);
                    Log.d("animationEnd", "delete from deleteCubeArray");
                } else {//if the cube reached the red zone
                    //decrease player score
                    playerScore -= 5;
                    score.setText("" + playerScore);
                    //delete the cube
                    deleteCube();

                }
                */
                //if we have caught a cube previous to animation ending
                if(!deleteCubeArray.isEmpty()){

                    ImageView temp = cubeArray.get(0);
                    //if the animation belongs to a caught cube
                    if(deleteCubeArray.contains(temp)){
                        //remove cube from arrayLists (cube has already been scored)
                        cubeArray.remove(temp);
                        deleteCubeArray.remove(temp);
                    }
                    else{//the animation belongs to a cube that reached the end
                        //remove from screen
                        mainLayout.removeView(temp);
                        playerScore-=5;
                        score.setText("Score: " + playerScore);
                        cubeArray.remove(0);
                    }
                }
                else{//no cubes previously caught and cube reached bottom
                    //decrease player score
                    playerScore -= 5;
                    score.setText("" + playerScore);
                    //delete the cube
                    deleteCube();
                }

                Log.d("animationListener", "Animation End");
                countEnd++;

                if (countEnd >= Constants.cap) {
                    //end of game
                    //TODO: end of game
                    gameEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub
                Log.d("animationListener", "animation cancel");
            }
        });


        Log.d("moveCube", "Test5");
        anim.start();
        Log.d("moveCube", "Test6");
    }//end of yellowCube

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //old version of the cube fall for safety
    public void greenCubeOld(View v){
        count++;
        Log.d("moveCube", "count = " + count);
        int[] location = new int[2];
        cube1.getLocationOnScreen(location);
        Log.d("moveCube", "Test2");

        animation = new TranslateAnimation(0, 0, 0, height);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d("Animation Listener", "Animation Start");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("Animation Listener", "Animation End");
                deleteCube();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ImageView newCube = new ImageView(v.getContext());
        newCube.setImageResource(R.drawable.brick48green);

        colour = (ImageView) v.findViewById(R.id.gCube);

        //newCube.setX(colour.getX()+cubes.getX());
        newCube.setY(0);
        newCube.setId(count);
        cubeArray.add(newCube);
        mainLayout.addView(newCube);

        Log.d("moveCube", "Test3");

        Log.d("moveCube", "Test4");
        animation.setDuration(Constants.speed);
        animation.setFillAfter(false);

        Log.d("moveCube", "Test5");
        newCube.startAnimation(animation);
        Log.d("moveCube", "Test6");
    }



    @Override
    public void onPause(){
        super.onPause();
        //TODO fix the music stop
        //mp.stop();
    }
}
