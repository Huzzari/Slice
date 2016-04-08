package pancakeninjas.sliceproject;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.graphics.Point;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by darkhobbo on 4/7/2016.
 */
public class FragGamePlayerActivity extends Fragment{

    public ConnectActivity connectActivity;
    int width, height, half, playerScore;
    int count = 0;
    int count2 = 0;
    int speed = 2000;
    int cap = 30;
    int delay = 1000;
    float endPoint;
    TranslateAnimation animation;
    ObjectAnimator anim;
    ImageView cube1;
    Button blueButton, redButton, greenButton, yellowButton;
    ArrayList<ImageView> cubeArray;
    ArrayList<ImageView> deleteCubeArray;
    ImageView colour;
    LinearLayout cubes, scoreZone, scoreTop, scoreBottom, divideBottom;
    TextView score;
    RelativeLayout mainLayout;
    ImageView yCube, bCube, rCube, gCube;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.frag_game_player, container, false);

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
        score = (TextView) view.findViewById(R.id.scoreText);
        blueButton = (Button) view.findViewById(R.id.bButton);
        redButton = (Button) view.findViewById(R.id.rButton);
        yellowButton = (Button) view.findViewById(R.id.yButton);
        greenButton = (Button) view.findViewById(R.id.gButton);
        yCube = (ImageView)view.findViewById(R.id.yCube);
        gCube = (ImageView)view.findViewById(R.id.gCube);
        bCube = (ImageView)view.findViewById(R.id.bCube);
        rCube = (ImageView)view.findViewById(R.id.rCube);


        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropCube(v);
            }
        });

        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropCube(v);
            }
        });

        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("yellowClick", v.toString());
                dropCube(v);
            }
        });

        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropCube(v);
            }
        });

        playerScore = 0;
        endPoint = scoreBottom.getY();
        Log.d("endPoint", "end Point = " + endPoint);
        Log.d("endPoint", "end Point = " + scoreBottom.getY());
        getScreenSize();

        return view;
    }


    //used for when the user collects a cube before the animation has ended
    public void stopCube(){
        //if the arrayList isnt empty
        if(!cubeArray.isEmpty()){
            ImageView temp = cubeArray.get(0);
            //add cube to deleteCubeArray
            deleteCubeArray.add(temp);
            //remove the cube from cubeArray and delete from the screen
            cubeArray.remove(0);
            mainLayout.removeView(temp);
        }
    }

    //delete a cube that has ended its animation
    public void deleteCube(){
        //check to make sure the arrayList isnt empty first
        if(!cubeArray.isEmpty()){
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

    public void checkIfScore(){
        //if there are cubes in cubeArray
        if(!cubeArray.isEmpty()) {
            //copy the first cube
            ImageView temp = cubeArray.get(0);

            Log.d("Test","Cube = " + temp.getY());
            Log.d("Test", "Top = " + scoreTop.getY() + "Bottom = " + scoreBottom.getY());

            //if the cube is in the score zone
            if ((temp.getY() > scoreTop.getY()) && (temp.getY() < scoreBottom.getY())) {
                Log.d("CheckIfScore", "score!");
                //remove cube from screen and place it into the deleteCubeArray
                stopCube();
                //increase player score
                playerScore += 10;
                score.setText(""+playerScore);
                Log.d("CheckIfScore", "score end");
            } else {//cube is not in the score zone
                Log.d("CheckIfScore", "oops, you missed!");
                //decrease player score
                playerScore-= 5;
                score.setText(""+playerScore);
                Log.d("CheckIfScore", "miss end");
            }
            Log.d("deleteCube", "not empty");
        }
    }

    public void buttonDisable(){
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
        }, delay);
    }


    //makes a cube fall
    //TODO: change View v to the message being passed
    public void dropCube(View v){

        buttonDisable();
        count++;
        Log.d("moveCube", "count = " + count);
        //int[] location = new int[2];
        //cube1.getLocationOnScreen(location);
        Log.d("moveCube", "Test2");
        ImageView newCube;
        switch(v.getId()){
            case R.id.yButton:
                //create new imageView for the cube
                newCube = new ImageView(v.getContext());
                newCube.setImageResource(R.drawable.brick48yellow);
                //color is a copy of the yellow cube that stays on screen (yCube)
                colour = yCube;
                Log.d("dropCube", colour.toString());
                break;
            case R.id.bButton:
                //create new imageView for the cube
                newCube = new ImageView(v.getContext());
                newCube.setImageResource(R.drawable.brick48blue);
                //color is a copy of the yellow cube that stays on screen (yCube)
                colour = bCube;
                break;
            case R.id.gButton:
                //create new imageView for the cube
                newCube = new ImageView(v.getContext());
                newCube.setImageResource(R.drawable.brick48green);
                //color is a copy of the yellow cube that stays on screen (yCube)
                colour = gCube;
                break;
            case R.id.rButton:
                //create new imageView for the cube
                newCube = new ImageView(v.getContext());
                newCube.setImageResource(R.drawable.brick48red);
                //color is a copy of the yellow cube that stays on screen (yCube)
                colour = rCube;
                break;
            default:
                newCube = new ImageView(v.getContext());
                newCube.setImageResource(R.drawable.brick48yellow);
                //color is a copy of the yellow cube that stays on screen (yCube)
                colour = yCube;
                break;
        }
        //set X and Y of cube
        Log.d("dropCube", newCube.toString());
        newCube.setX(colour.getX() + cubes.getX());
        newCube.setY(0);
        //newCube.setId(count);
        //add the cube to the arraylist and add to the screen
        cubeArray.add(newCube);
        mainLayout.addView(newCube);

        Log.d("moveCube", "Test3");

        //LinearInterpolator linInt = new LinearInterpolator();

        //animator for the cube
        anim = ObjectAnimator.ofFloat(newCube, "Y", 0, scoreBottom.getY()).setDuration(2000);
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
                if(!deleteCubeArray.isEmpty()){

                    ImageView temp = deleteCubeArray.get(0);
                    mainLayout.removeView(temp);
                    deleteCubeArray.remove(0);
                    Log.d("animationEnd","delete from deleteCubeArray");
                }
                else {//if the cube reached the red zone
                    //decrease player score
                    playerScore-= 5;
                    score.setText(""+playerScore);
                    //delete the cube
                    deleteCube();
                }
                Log.d("animationListener","Animation End");
                count2++;

                if(count2 >= cap){
                    //end of game
                    //TODO: end of game
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub
                Log.d("animationListener","animation cancel");
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
        // newCube.setId(count);
        cubeArray.add(newCube);
        mainLayout.addView(newCube);

        Log.d("moveCube", "Test3");

        Log.d("moveCube", "Test4");
        animation.setDuration(speed);
        animation.setFillAfter(false);

        Log.d("moveCube", "Test5");
        newCube.startAnimation(animation);
        Log.d("moveCube", "Test6");
    }
}
