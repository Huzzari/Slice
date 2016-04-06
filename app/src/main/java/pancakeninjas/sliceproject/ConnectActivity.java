package pancakeninjas.sliceproject;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Permission;
import java.util.Set;

public class ConnectActivity extends AppCompatActivity {

    public static final int  MY_PERMISSIONS_REQUEST_LOCATION = 3;

    String deviceName, macAddress;
    BluetoothAdapter bluetooth;
    final int REQUEST_DISCOVERABLE = 12346;
    final int ENABLE_BLUETOOTH = 12345;
    TextView test1;
    ArrayAdapter<String> btArrayAdapter;
    ListView btView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        if(Build.VERSION.SDK_INT < 23){
            Log.d("BuildVersion", "VERSION less than 23");
            bluetooth = BluetoothAdapter.getDefaultAdapter();
            if (bluetooth == null) {
                // Device does not support Bluetooth
                Log.d("onCreate", "Device does not support bluetooth");
            }

            btArrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1);


            btView = (ListView) findViewById(R.id.listView);
            test1 = (TextView) findViewById(R.id.textView2);

            checkEnabled(test1);

            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            this.registerReceiver(bluetoothReceiver, filter);
        }
        else{
            Log.d("BuildVersion", "VERSION 23");
            if(ContextCompat.checkSelfPermission(ConnectActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(ConnectActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                Log.d("DEBUG", "PERMISSION REQ -> ACCESS_FINE/COARSE_LOCATION -> PERMISSION_NOT_GRANTED");

                if((ActivityCompat.shouldShowRequestPermissionRationale(ConnectActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION))||
                        (ActivityCompat.shouldShowRequestPermissionRationale(ConnectActivity.this,
                                Manifest.permission.ACCESS_COARSE_LOCATION))){
                    Log.d("DEBUG", "PERMISSION REQ  -> should Show Request Permission Rationale");
                    ActivityCompat.requestPermissions(ConnectActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }
                else{
                    Log.d("DEBUG", "PERMISSION REQ  -> No explanation needed");

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(ConnectActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);

                }
            }
            else{
                Log.d("DEBUG", "PERMISSION REQ  -> *** ACCESS_FINE/COARSE_LOCATION -> PERMISSION_GRANTED ****");

                bluetooth = BluetoothAdapter.getDefaultAdapter();
                if (bluetooth == null) {
                    // Device does not support Bluetooth
                    Log.d("onCreate", "Device does not support bluetooth");
                }

                btArrayAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1);


                btView = (ListView) findViewById(R.id.listView);
                test1 = (TextView) findViewById(R.id.textView2);

                checkEnabled(test1);

                IntentFilter filter = new IntentFilter();
                filter.addAction(BluetoothDevice.ACTION_FOUND);
                filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                this.registerReceiver(bluetoothReceiver, filter);
            }
        }
    }

    public void checkEnabled(View v){

        if (bluetooth.isEnabled()) {
            deviceName = bluetooth.getName();
            macAddress = bluetooth.getAddress();
        } else {
            Intent enableBluetooth =
                    new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, ENABLE_BLUETOOTH);
        }

    }//end of checkEnabled


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("DEBUG", "onRequestPermissionsResult -> requestCode = " + requestCode + ",permissions = " + permissions + "grantResults = " + grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d("DEBUG", "PERMISSION REQ  -> requestCode + ->*** ACCESS_FINE/CORSE_LOCATION -> PERMISSION_GRANTED ****");

                    bluetooth = BluetoothAdapter.getDefaultAdapter();
                    if (bluetooth == null) {
                        // Device does not support Bluetooth
                        Log.d("onCreate", "Device does not support bluetooth");
                    }

                    btArrayAdapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_list_item_1);


                    btView = (ListView) findViewById(R.id.listView);
                    test1 = (TextView) findViewById(R.id.textView2);

                    checkEnabled(test1);

                    IntentFilter filter = new IntentFilter();
                    filter.addAction(BluetoothDevice.ACTION_FOUND);
                    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                    this.registerReceiver(bluetoothReceiver, filter);

                } else {
                    Log.d("DEBUG", "PERMISSION REQ  -> ACCESS DENIED");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    finish();
                }

                return;

            default:


                // other 'case' lines to check for other
                // permissions this app might request
        }
    }


    public void becomeDiscoverable(View v){
        Intent discoverable = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
        startActivityForResult(discoverable, REQUEST_DISCOVERABLE);
    }

    public void discover(View v){
        if (bluetooth.isEnabled()) {
            if(bluetooth.isDiscovering())
                bluetooth.cancelDiscovery();
            Log.d("discover", "Started discovering.");
            bluetooth.startDiscovery();
            //IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            //registerReceiver(deviceFoundRcvr, filter);
        }
    }

    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            btArrayAdapter.clear();
            Log.d("bluetoothReceiver", "Intent received");
            switch(intent.getAction()){
                case BluetoothDevice.ACTION_FOUND:
                    Log.d("bluetoothReceiver", "Device discovered.");
                    //get device name
                    String deviceName = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
                    //get device
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    // do whatever with the device
                    btArrayAdapter.add(deviceName);
                    test1.setText(deviceName);
                    btView.setAdapter(btArrayAdapter);
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Log.d("bluetoothReceiver", "Finished discovering.");
                    break;
                default:
                    Log.d("switch", "How did we get here?");
            }
        }
    };


    public void pairedDevices(View v) {
        Log.d("pairedDevices", "1");
        Set<BluetoothDevice> pairedDevices = bluetooth.getBondedDevices();
        Log.d("pairedDevices", "2");
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            //ArrayAdapter<String> pairedArrayAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
            btArrayAdapter.clear();
            Log.d("pairedDevices", "3");
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                Log.d("pairedDevices", "before arrayAdapter");
                btArrayAdapter.add(device.getName());
                Log.d("pairedDevices", "after arrayAdapter");
                btView.setAdapter(btArrayAdapter);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("onActivityResult", "RequestCode: " + requestCode);

        if (requestCode == ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                BluetoothAdapter bluetooth =
                        BluetoothAdapter.getDefaultAdapter();
                String deviceName = bluetooth.getName();
                String macAddress = bluetooth.getAddress();
                // do something bluetoothy 
                Log.d("onActivityResult","bluetooth enabled");
            }
            else{
                Log.d("onActivityResult","bluetooth cancelled");
                //cancelled.
            }
        }

        if (requestCode == REQUEST_DISCOVERABLE) {
            if (resultCode == RESULT_CANCELED) {
                // go without Bluetooth?
                Log.d("onActivityResult","discoverable cancelled");
            } else {
                int howLong = resultCode;
                // we'll be discoverable for howLong seconds
                Log.d("onActivityResult","discoverable enabled");
            }
        }
    }

    public void onResume(){
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(bluetoothReceiver, filter);
    }

    public void onPause(){
        super.onPause();
        unregisterReceiver(bluetoothReceiver);
    }
}
