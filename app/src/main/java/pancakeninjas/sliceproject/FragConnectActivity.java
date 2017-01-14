package pancakeninjas.sliceproject;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by darkhobbo on 4/7/2016.
 */
public class FragConnectActivity extends Fragment {
    public ConnectActivity connectActivity;
    public ImageButton button1, button2, button3;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.frag_connect, container, false);

        button1 = (ImageButton) view.findViewById(R.id.pairDevBtn);
        button2 = (ImageButton) view.findViewById(R.id.becomeDiscoverableBtn);
        button3 = (ImageButton) view.findViewById(R.id.discoverDeviceBtn);

        connectActivity = (ConnectActivity)getActivity();

        connectActivity.btView = (ListView)view.findViewById(R.id.listView);
        connectActivity.test1 = (TextView)view.findViewById(R.id.textView2);

        //connectActivity.setup();

        button1.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    button1.setColorFilter(Color.argb(104, 0, 0, 0));
                    connectActivity.pairedDevices(button1);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    button1.setColorFilter(Color.argb(0, 0, 0, 0));
                    return true;
                }
                return false;
            }
        });

        button2.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    button2.setColorFilter(Color.argb(104, 0, 0, 0));
                    connectActivity.pairedDevices(button1);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    button2.setColorFilter(Color.argb(0, 0, 0, 0));
                    connectActivity.becomeDiscoverable(button2);
                    return true;
                }
                return false;
            }
        });

        button3.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    button3.setColorFilter(Color.argb(104, 0, 0, 0));
                    connectActivity.pairedDevices(button1);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    button3.setColorFilter(Color.argb(0, 0, 0, 0));
                    connectActivity.discover(button3);
                    return true;
                }
                return false;
            }
        });

        /*
        view.findViewById(R.id.pairDevBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectActivity.pairedDevices(v);
            }
        });

        view.findViewById(R.id.becomeDiscoverableBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectActivity.becomeDiscoverable(v);
            }
        });

        view.findViewById(R.id.discoverDeviceBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectActivity.discover(v);
            }
        });


        view.findViewById(R.id.sendMessageBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectActivity.sendMsg(v);
            }
        });

        view.findViewById(R.id.enableBluetoothBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectActivity.checkEnabled(v);
            }
        });

        */

        return view;
    }


}
