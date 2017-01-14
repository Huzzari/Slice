package pancakeninjas.sliceproject;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragGameBossActivity.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragGameBossActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragGameBossActivity extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public ConnectActivity connectActivity;
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
    LinearLayout cubes, scoreZone, scoreTop, scoreBottom, divideBottom, halfBottom;
    TextView score, endScore, endText;
    RelativeLayout mainLayout;
    ImageView yCube, bCube, rCube, gCube, explosionView;
    AnimationDrawable explode;


    private OnFragmentInteractionListener mListener;

    public FragGameBossActivity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragGameBossActivity.
     */
    // TODO: Rename and change types and number of parameters
    public static FragGameBossActivity newInstance(String param1, String param2) {
        FragGameBossActivity fragment = new FragGameBossActivity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_game_boss, container, false);
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
        blueButton = (Button) view.findViewById(R.id.bButton);
        redButton = (Button) view.findViewById(R.id.rButton);
        yellowButton = (Button) view.findViewById(R.id.yButton);
        greenButton = (Button) view.findViewById(R.id.gButton);
        yCube = (ImageView)view.findViewById(R.id.yCube);
        gCube = (ImageView)view.findViewById(R.id.gCube);
        bCube = (ImageView)view.findViewById(R.id.bCube);
        rCube = (ImageView)view.findViewById(R.id.rCube);
        endText = (TextView)view.findViewById(R.id.endRoundText);
        explosionView = (ImageView) view.findViewById(R.id.explosionView);


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

        count=0;
        count2=0;
        playerScore = 0;
        endPoint = scoreBottom.getY();
        Log.d("endPoint", "end Point = " + endPoint);
        Log.d("endPoint", "end Point = " + scoreBottom.getY());
        getScreenSize();

        return view;

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
            }, 200);
            Log.d("Explosion", "after animation");
            explode.stop();
            Log.d("Explosion", "after stop()");

        } catch (Exception e) {
            // TODO: handle exception
        }

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
        }, Constants.delay);
    }


    //makes a cube fall
    //TODO: change View v to the message being passed
    public void dropCube(View v){

        count++;
        if(count< Constants.cap)
        {
            buttonDisable();
        }
        else{
            blueButton.setEnabled(false);
            redButton.setEnabled(false);
            yellowButton.setEnabled(false);
            greenButton.setEnabled(false);
            endText.setVisibility(View.VISIBLE);
        }

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
                connectActivity.sendYellow();
                break;
            case R.id.bButton:
                //create new imageView for the cube
                newCube = new ImageView(v.getContext());
                newCube.setImageResource(R.drawable.brick48blue);
                //color is a copy of the yellow cube that stays on screen (yCube)
                colour = bCube;
                connectActivity.sendBlue();
                break;
            case R.id.gButton:
                //create new imageView for the cube
                newCube = new ImageView(v.getContext());
                newCube.setImageResource(R.drawable.brick48green);
                //color is a copy of the yellow cube that stays on screen (yCube)
                colour = gCube;
                connectActivity.sendGreen();
                break;
            case R.id.rButton:
                //create new imageView for the cube
                newCube = new ImageView(v.getContext());
                newCube.setImageResource(R.drawable.brick48red);
                //color is a copy of the yellow cube that stays on screen (yCube)
                colour = rCube;
                connectActivity.sendRed();
                break;
            default:
                newCube = new ImageView(v.getContext());
                newCube.setImageResource(R.drawable.brick48yellow);
                //color is a copy of the yellow cube that stays on screen (yCube)
                colour = yCube;
                connectActivity.sendYellow();
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
        anim = ObjectAnimator.ofFloat(newCube, "Y", 0, halfBottom.getY()).setDuration(Constants.speed);
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
                if(!deleteCubeArray.isEmpty()){

                    ImageView temp = deleteCubeArray.get(0);
                    mainLayout.removeView(temp);
                    deleteCubeArray.remove(0);
                    Log.d("animationEnd","delete from deleteCubeArray");
                }
                else {//if the cube reached the red zone
                    //delete the cube
                    deleteCube();
                }
                Log.d("animationListener","Animation End");
                count2++;

                if(count2 >= Constants.cap){
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
        animation.setDuration(Constants.speed);
        animation.setFillAfter(false);

        Log.d("moveCube", "Test5");
        newCube.startAnimation(animation);
        Log.d("moveCube", "Test6");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
