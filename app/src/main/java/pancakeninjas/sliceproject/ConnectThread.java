package pancakeninjas.sliceproject;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class ConnectThread extends Thread {
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
            try {
                mmSocket.close();
            }
            catch (IOException closeException) {
                closeException.printStackTrace();
            }
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
        ConnectedThread connectedThread = new ConnectedThread(bluetoothSocket);
        connectedThread.start();
        connectedThread.write("First message".getBytes());
    }
}
