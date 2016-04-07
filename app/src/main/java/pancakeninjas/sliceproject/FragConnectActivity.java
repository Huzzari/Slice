package pancakeninjas.sliceproject;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by darkhobbo on 4/7/2016.
 */
public class FragConnectActivity extends Fragment {
    public ConnectActivity connectActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.frag_connect, container, false);

        connectActivity = (ConnectActivity)getActivity();

        connectActivity.btView = (ListView)view.findViewById(R.id.listView);
        connectActivity.test1 = (TextView)view.findViewById(R.id.textView2);

        //connectActivity.setup();

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

        view.findViewById(R.id.enableBluetoothBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectActivity.checkEnabled(v);
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



        return view;
    }


}
