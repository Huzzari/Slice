package pancakeninjas.sliceproject;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class ConnectActivity extends AppCompatActivity {

    private static final UUID SECURE_UUID = UUID.fromString("38df9e29-55e9-481b-8a4b-948714886462");
    public static final int  MY_PERMISSIONS_REQUEST_LOCATION = 3;

    // Handler stuff, will move after.
    public Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message message){
            switch (message.what) {
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) message.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, message.arg1);
                    // RIGHT HERE.
                    Log.d("MESSAGE", readMessage);
                    processMessage(readMessage);
                    break;
                /*
                case Constants.MESSAGE_STATE_CHANGE:

                    break;

                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;

                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != ctx) {
                        Toast.makeText(ctx, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != ctx) {
                        Toast.makeText(ctx, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;*/
            }
        }
    };

    // HELLO WORLD!
    //HI!

    String deviceName, macAddress;
    public BluetoothAdapter bluetooth;
    final int REQUEST_DISCOVERABLE = 12346;
    final int ENABLE_BLUETOOTH = 12345;
    TextView test1;
    ArrayAdapter<BluetoothDevice> btArrayAdapter;
    ListView btView;
    private Context ctx;
    private FragGamePlayerActivity fragGamePlayerActivity;
    private FragGameBossActivity fragGameBossActivity;
    private boolean master = false;
    private boolean imboss = false;
    protected boolean ready = false;
    protected boolean enemyReady = false;

    protected ConnectedThread connectedThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        ctx = this;

        if(Build.VERSION.SDK_INT < 23){
            Log.d("BuildVersion", "VERSION less than 23");
            setup();
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
                setup();
            }
        }
    }

    public void setup(){
        bluetooth = BluetoothAdapter.getDefaultAdapter();
        if (bluetooth == null) {
            // Device does not support Bluetooth
            Log.d("onCreate", "Device does not support bluetooth");
        }
        else
            checkEnabled(test1);
    }

    public void postSetup(){
        btArrayAdapter = new BTArrayAdapter(this, R.layout.btlist);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(bluetoothReceiver, filter);

        AcceptThread acceptThread = new AcceptThread(SECURE_UUID);
        acceptThread.start();
        manageStart();
    }

    public void checkEnabled(View v){
        if (bluetooth.isEnabled()) {
            deviceName = bluetooth.getName();
            macAddress = bluetooth.getAddress();
            postSetup();

        } else {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
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
                    setup();

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
        Intent discoverable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
        startActivityForResult(discoverable, REQUEST_DISCOVERABLE);
        test1.setText("Friends should be able to find me now.");
    }

    public void discover(View v){
        if (bluetooth.isEnabled()) {
            if(bluetooth.isDiscovering())
                bluetooth.cancelDiscovery();
            Log.d("discover", "Started discovering.");
            test1.setText("Looking for friends...");
            btArrayAdapter.clear();
            btView.setAdapter(btArrayAdapter);
            btView.setOnItemClickListener(discoverClick);
            bluetooth.startDiscovery();
            //IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            //registerReceiver(deviceFoundRcvr, filter);
        }
    }

    public void manageStart(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        FragConnectActivity frg = new FragConnectActivity();
        ft.add(R.id.fragmentContainer, frg, "manageFragment");
        ft.commit();
    }

    public void sendMsg(View v){
        if(connectedThread != null)
            connectedThread.write("First message".getBytes());
        else
            Log.d("SendMsg", "Not connected.");
    }

    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d("bluetoothReceiver", "Intent received");
            switch(intent.getAction()){
                case BluetoothDevice.ACTION_FOUND:
                    try{
                        Log.d("bluetoothReceiver", "Device discovered.");
                        //get device name
                        String deviceName = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
                        //get device
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        if(device == null)
                            Log.d("bluetoothReceier", "NULL DEVICE RECEIVED");

                        Log.d("bluetoothReceiver", deviceName);
                        Log.d("bluetoothReceiver", device.toString());
                        // do whatever with the device
                        btArrayAdapter.add(device);
                        test1.setText("Found Device! Click on a device to start a game.");
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
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
            btView.setAdapter(btArrayAdapter);
            btView.setOnItemClickListener(null);
            Log.d("pairedDevices", "3");
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                Log.d("pairedDevices", "before arrayAdapter");
                btArrayAdapter.add(device);
                Log.d("pairedDevices", "after arrayAdapter");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("onActivityResult", "RequestCode: " + requestCode);

        if (requestCode == ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
                String deviceName = bluetooth.getName();
                String macAddress = bluetooth.getAddress();
                // do something bluetoothy 
                Log.d("onActivityResult","bluetooth enabled");
                postSetup();
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
        unregisterReceiver(bluetoothReceiver);
        if(connectedThread!=null)
        {
            connectedThread.cancel();
            connectedThread = null;
        }
        super.onPause();
    }

    private AdapterView.OnItemClickListener discoverClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            BluetoothDevice clickedDevice = btArrayAdapter.getItem(position);
            Log.d("DeviceClicked", "Device clicked: " + clickedDevice.getName());
            ConnectThread connectThread = new ConnectThread(clickedDevice, SECURE_UUID);
            connectThread.start();
        }
    };

    private AdapterView.OnItemClickListener pairedClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("PAIRED_CLICK", "Paired item was clicked.");
        }
    };

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);

                    // Send the obtained bytes to the UI activity
                    handler.obtainMessage(Constants.MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                }
                catch (IOException e) {
                    Log.d("ConnectedThread", "Socket closed.");
                    //e.printStackTrace();
                    master = false;
                    if(connectedThread!=null){
                        connectedThread.cancel();
                        connectedThread = null;
                    }
                    finish();
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            }
            catch (IOException e) {
                Log.d("ConnectedThread", "Not connected.");
                //e.printStackTrace();
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
                master = false;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private BluetoothAdapter bluetoothAdapter;

        public ConnectThread(BluetoothDevice device, UUID uuid) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(uuid);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            mmSocket = tmp;
        }

        public void run() {
            Log.d("ConnectThread", "Inside run");
            // Cancel discovery because it will slow down the connection
            bluetoothAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                Log.d("ConnectThread", "Before connect.");
                mmSocket.connect();
                Log.d("ConnectThread", "After connect.");

            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                Log.d("ConnectThread", "Unable to connect.");
                connectException.printStackTrace();
                if(connectedThread!=null)
                    connectedThread.cancel();

                return;
            }

            // Do work to manage the connection (in a separate thread)
            Log.d("CONNECTION", "DEVICES ARE CONNECTED.");
            manageConnectedSocket(mmSocket);
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void manageConnectedSocket(BluetoothSocket bluetoothSocket){
            Log.d("CONNECTION", "I AM SLAVE");
            connectedThread = new ConnectedThread(bluetoothSocket);
            connectedThread.start();
        }
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread(UUID uuid) {
            // Use a temporary object that is later assigned to mmServerSocket,
            // because mmServerSocket is final
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = BluetoothAdapter.getDefaultAdapter().listenUsingRfcommWithServiceRecord("Test1", uuid);
            } catch (IOException e) {
                e.printStackTrace();}
            mmServerSocket = tmp;
        }

        public void run() {
            Log.d("MSG", "Listening for connection.");
            BluetoothSocket socket;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                Log.d("MSG", "Waiting for connection.");
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    try{
                        Log.d("MSG", "SOCKET ACCEPTED.");
                        // Do work to manage the connection (in a separate thread)
                        manageConnectedSocket(socket);
                        mmServerSocket.close();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        /** Will cancel the listening socket, and cause the thread to finish */
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {e.printStackTrace(); }
        }

        public void manageConnectedSocket(BluetoothSocket bluetoothSocket){
            Log.d("CONNECTION", "I AM MASTER");
            master = true;
            connectedThread = new ConnectedThread(bluetoothSocket);
            connectedThread.start();
            setupGame();
        }
    }

    public void gameStart(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        FragHowToPlay fragHowToPlay = new FragHowToPlay();
        ft.replace(R.id.fragmentContainer, fragHowToPlay, "howToPlayFrag");

        ft.commit();
    }

    public void bossStart() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        FragHowToBoss fragHowToBoss = new FragHowToBoss();
        ft.replace(R.id.fragmentContainer, fragHowToBoss, "howToBossFrag");

        ft.commit();
    }

    public void setupGame(){
        // I am master, I am setting up game.
        requestRoll();
    }

    public void startGame(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(imboss){
            Log.d("startGame", "If Setting frags");
            FragGameBossActivity frg = new FragGameBossActivity();
            ft.replace(R.id.fragmentContainer, frg, "bossFragment");
            fragGameBossActivity = frg;
        }
        else
        {
            Log.d("startGame", "Else Setting frags");
            FragGamePlayerActivity frg = new FragGamePlayerActivity();
            ft.replace(R.id.fragmentContainer, frg, "playFragment");
            fragGamePlayerActivity = frg;
        }
        Log.d("startGame", "Before commit");
        try{
            ft.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Log.d("startGame", "After commit");
    }

    public void requestRoll(){
        connectedThread.write("req,roll".getBytes());
    }

    public void sendRoll(){
        connectedThread.write(("res,rollnum," + new Random().nextInt(100)).getBytes());
    }

    public void areYouReady(){
        connectedThread.write("req,ready".getBytes());
    }

    public void sendReady(){
        connectedThread.write("res,ready".getBytes());
    }

    public void sendDeleteCube(){
        connectedThread.write("req,deletecube".getBytes());
    }

    public void sendStopCube(){
        connectedThread.write("req,stopcube".getBytes());
    }

    public void sendBlue(){
        connectedThread.write("req,blue".getBytes());
    }

    public void sendRed(){
        connectedThread.write("req,red".getBytes());
    }

    public void sendYellow(){
        connectedThread.write("req,yellow".getBytes());
    }

    public void sendGreen(){
        connectedThread.write("req,green".getBytes());
    }

    public void sendCancel(){
        connectedThread.write("req,cancel".getBytes());
    }

    public void killFrag(){
        if(imboss){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.detach(fm.findFragmentByTag("howToBossFrag"));
            ft.commit();
        }
        else{
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.detach(fm.findFragmentByTag("howToPlayFrag"));
            ft.commit();
        }
    }

    public void compareRoll(int clientRoll){
        Log.d("compareRoll", "Client Roll: " + clientRoll);
        int myRoll = new Random().nextInt(100);
        Log.d("compareRoll", "My Roll: " + myRoll);
        if(myRoll >= clientRoll){
            // I go first.
            Log.d("compareRoll", "I go first.");
            connectedThread.write("res,imboss".getBytes());
            imboss = true;
            bossStart();
        }
        else{
            Log.d("compareRoll", "I go second");
            connectedThread.write("res,implayer".getBytes());
            // OPEN PLAYER FRAGMENT.
            gameStart();
        }
    }

    public void processMessage(String message){
        String[] tokenized = message.split(",");
        switch(tokenized[0]){
            case "req":
                switch (tokenized[1]){
                    case "blue":
                        // CALL SEND BLUE HERE.
                        Log.d("processMessage", "Request for Blue Cube.");
                        fragGamePlayerActivity.dropCube("blue");
                        break;
                    case "red":
                        // CALL SEND RED HERE.
                        Log.d("processMessage", "Request for Red Cube.");
                        fragGamePlayerActivity.dropCube("red");
                        break;
                    case "green":
                        // CALL SEND GREEN HERE.
                        Log.d("processMessage", "Request for Green Cube.");
                        fragGamePlayerActivity.dropCube("green");
                        break;
                    case "yellow":
                        // CALL SEND YELLOW HERE.
                        Log.d("processMessage", "Request for Yellow Cube.");
                        fragGamePlayerActivity.dropCube("yellow");
                        break;
                    case "roll":
                        // CALL SEND ROLL HERE.
                        Log.d("processMessage", "Request for Roll.");
                        sendRoll();
                        break;
                    case "state":
                        // CALL SEND STATE HERE.
                        Log.d("processMessage", "Request for State.");
                        break;
                    case "youhost":
                        // BECOME HOST HERE.
                        Log.d("processMessage", "Request to become host");
                        break;
                    case "deletecube":
                        Log.d("processMessage", "Request to delete cube");
                        fragGameBossActivity.deleteCube();
                        break;
                    case "stopcube":
                        Log.d("processMessage", "Request to stop cube");
                        fragGameBossActivity.stopCube();
                        break;
                    case "ready":
                        Log.d("processMessage", "Request if ready");
                        enemyReady = true;
                        break;
                    default:
                        Log.d("processMessage", "req Broken");
                        Log.d("processMessage", message);
                }
                break;
            case "res":
                switch (tokenized[1]){
                    case "rollnum":
                        // CALL ROLL NUMBER HERE.
                        Log.d("processMessage", "Received roll");
                        compareRoll(Integer.parseInt(tokenized[2]));
                        break;
                    case "ready":
                        Log.d("processMessage", "Received ready state");
                        startGame();
                        break;
                    case "gamestart":
                        Log.d("processMessage", "Received game start");
                        break;
                    case "cancel":
                        Log.d("processMessage", "Received cancel response");
                        killFrag();
                        finish();
                        break;
                    case "gamefinished":
                        Log.d("processMessage", "Received game finished.");
                        break;
                    case "imboss":
                        Log.d("processMessage", "Received imboss response, therefore I am player.");
                        gameStart();
                        break;
                    case "implayer":
                        Log.d("processMessage", "Received implayer response, therefore I am boss.");
                        imboss = true;
                        bossStart();
                        break;
                    default:
                        Log.d("processMessage", "res Broken");
                }
                break;
            default:
                Log.d("processMessage", "How did we get here?");
                Log.d("processMessage", "Message: " + message);
        }
    }
}
