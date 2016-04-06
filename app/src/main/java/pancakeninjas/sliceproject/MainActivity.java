package pancakeninjas.sliceproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick_Connect(View v){

        Intent newActivity1 = new Intent(MainActivity.this, ConnectActivity.class);
        startActivity(newActivity1);
    }

    public void onClick_Play(View v){

        Intent newActivity1 = new Intent(MainActivity.this, GameActivity.class);
        startActivity(newActivity1);
    }
}
