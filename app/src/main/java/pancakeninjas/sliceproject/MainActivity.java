package pancakeninjas.sliceproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton connectToFriendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        connectToFriendBtn = (ImageButton)findViewById(R.id.connectToFriendBtn);
        //old on click listener
        /*
        connectToFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConnectActivity.class);
                startActivity(intent);
            }
        });
        */

        connectToFriendBtn.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    connectToFriendBtn.setColorFilter(Color.argb(104, 0, 0, 0));
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    connectToFriendBtn.setColorFilter(Color.argb(0, 0, 0, 0));
                    Intent intent = new Intent(MainActivity.this, ConnectActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }
}
