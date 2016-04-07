package pancakeninjas.sliceproject;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    int width, height, half;
    int count = 1;
    int speed = 2000;
    TranslateAnimation animation;
    ObjectAnimator anim;
    ImageView cube1, cube2;
    ArrayList<ImageView> cubeArray;
    ArrayList<ImageView> deleteCubeArray;
    ImageView colour;
    LinearLayout cubes, scoreZone, scoreTop, scoreBottom;
    TextView score;
    RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        cube1 = (ImageView) findViewById(R.id.bCube);
        Log.d("Test", "Test1");
        getScreenSize();

        cubeArray = new ArrayList<ImageView>();
        deleteCubeArray = new ArrayList<ImageView>();
        cubes = (LinearLayout) findViewById(R.id.cubeLayout);
        mainLayout = (RelativeLayout) findViewById(R.id.mainRel);
        scoreZone = (LinearLayout) findViewById(R.id.scoreField);
        scoreTop = (LinearLayout) findViewById(R.id.scoreLineTop);
        scoreBottom = (LinearLayout) findViewById(R.id.scoreLineBottom);
        score = (TextView) findViewById(R.id.scoreText);

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

    public void stopCube(){

        if(!cubeArray.isEmpty()){
            ImageView temp = cubeArray.get(0);
            deleteCubeArray.add(temp);
            cubeArray.remove(0);
            mainLayout.removeView(temp);
        }
    }

    public void testClick(View v){
        Log.d("Test","inTestClick");
        checkIfScore();
    }

    public void deleteCube(){

        if(!cubeArray.isEmpty()){

            ImageView temp = cubeArray.get(0);
            temp.clearAnimation();
            mainLayout.removeView(temp);
            cubeArray.remove(0);
            Log.d("deleteCube","not empty");
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

    public void checkIfScore(){

        //int scoreTemp = Integer.parseInt(score.getText().toString());
        if(!cubeArray.isEmpty()) {
            ImageView temp = cubeArray.get(0);

            Log.d("Test","Cube = " + temp.getY());
            Log.d("Test", "Top = " + scoreTop.getY() + "Bottom = " + scoreBottom.getY());

            if ((temp.getY() > scoreTop.getY()) && (temp.getY() < scoreBottom.getY())) {
                Log.d("CheckIfScore", "score!");
                stopCube();
                //scoreTemp++;
                score.setText("SCORE");
                Log.d("CheckIfScore", "score end");
            } else {
                Log.d("CheckIfScore", "oops, you missed!");
                //scoreTemp--;
                score.setText("MISS");
                Log.d("CheckIfScore", "miss end");
            }
            Log.d("deleteCube", "not empty");
        }
    }



    public void yellowCube(View v){
        count++;
        Log.d("moveCube", "count = " + count);
        int[] location = new int[2];
        cube1.getLocationOnScreen(location);
        Log.d("moveCube", "Test2");

        ImageView newCube = new ImageView(this);
        newCube.setImageResource(R.drawable.brick48yellow);

        colour = (ImageView) findViewById(R.id.yCube);
        newCube.setX(colour.getX() + cubes.getX());
        newCube.setY(0);
        //newCube.setId(count);
        cubeArray.add(newCube);
        mainLayout.addView(newCube);

        Log.d("moveCube", "Test3");

        //LinearInterpolator linInt = new LinearInterpolator();

        anim = ObjectAnimator.ofFloat(newCube, "Y", 0, height).setDuration(2000);
        anim.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO Auto-generated method stub
                if(!deleteCubeArray.isEmpty()){
                    ImageView temp = deleteCubeArray.get(0);
                    mainLayout.removeView(temp);
                    deleteCubeArray.remove(0);
                    Log.d("animationEnd","delete from deleteCubeArray");
                }
                else {
                    deleteCube();
                }
                Log.d("animationListener","Animation End");
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
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
