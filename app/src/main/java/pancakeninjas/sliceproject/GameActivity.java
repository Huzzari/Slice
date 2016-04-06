package pancakeninjas.sliceproject;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    int width, height, half;
    int count = 1;
    int speed = 2000;
    TranslateAnimation animation;
    ImageView cube1, cube2;
    ArrayList<ImageView> cubeArray;
    ImageView colour;
    LinearLayout cubes;

    RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        cube1 = (ImageView) findViewById(R.id.bCube);
        Log.d("Test", "Test1");
        getScreenSize();

        cubeArray = new ArrayList<ImageView>();
        cubes = (LinearLayout) findViewById(R.id.cubeLayout);
        mainLayout = (RelativeLayout) findViewById(R.id.mainRel);

        Log.d("Test", "Test2");
    }

    public void moveCube2(View v){

        Log.d("moveCube", "Test1");
        int[] location = new int[2];
        cube2.getLocationOnScreen(location);
        Log.d("moveCube", "Test2");


        Log.d("moveCube", "Test3");


        Log.d("moveCube", "Test4");
        animation.setDuration(speed);
        animation.setFillAfter(false);

        Log.d("moveCube", "Test5");
        cube2.startAnimation(animation);
        Log.d("moveCube", "Test6");


    }

    public void stopCube(View v){

        if(!cubeArray.isEmpty()){

            ImageView temp = cubeArray.get(0);
            temp.clearAnimation();

        }
    }

    public void deleteCube(){

        if(!cubeArray.isEmpty()){

            ImageView temp = cubeArray.get(0);
            temp.clearAnimation();
            mainLayout.removeView(temp);
            cubeArray.remove(0);

        }
    }


    public void getScreenSize(){

        width=0;
        height=0;

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        half = width/2;

        Log.d("Height"," " + height);
        Log.d("width"," " + width);

    }

    public void yellowCube(View v){
        count++;
        Log.d("moveCube", "count = " + count);
        int[] location = new int[2];
        cube1.getLocationOnScreen(location);
        Log.d("moveCube", "Test2");

        animation = new TranslateAnimation(0, 0, 0, height);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d("Animation Listener","Animation Start");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("Animation Listener","Animation End");
                deleteCube();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ImageView newCube = new ImageView(this);
        newCube.setImageResource(R.drawable.brick48yellow);

        colour = (ImageView) findViewById(R.id.yCube);
        newCube.setX(colour.getX()+cubes.getX());
        newCube.setY(0);
        //newCube.setId(count);
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

    public void blueCube(View v){
        count++;
        Log.d("moveCube", "count = " + count);
        int[] location = new int[2];
        cube1.getLocationOnScreen(location);
        Log.d("moveCube", "Test2");

        animation = new TranslateAnimation(0, 0, 0, height);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d("Animation Listener","Animation Start");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("Animation Listener","Animation End");
                deleteCube();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ImageView newCube = new ImageView(this);
        newCube.setImageResource(R.drawable.brick48blue);

        colour = (ImageView) findViewById(R.id.bCube);
        newCube.setX(colour.getX()+cubes.getX());
        newCube.setY(0);
        //newCube.setId(count);
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


    public void redCube(View v){
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
        ImageView newCube = new ImageView(this);
        newCube.setImageResource(R.drawable.brick48red);

        colour = (ImageView) findViewById(R.id.rCube);
        newCube.setX(colour.getX()+cubes.getX());
        newCube.setY(0);
        //newCube.setId(count);
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

    public void greenCube(View v){
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
        ImageView newCube = new ImageView(this);
        newCube.setImageResource(R.drawable.brick48green);

        colour = (ImageView) findViewById(R.id.gCube);

        newCube.setX(colour.getX()+cubes.getX());
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
