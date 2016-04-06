package pancakeninjas.sliceproject;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by greg on 4/6/2016.
 */
public class AcceptThread extends Thread {
    private final BluetoothServerSocket mmServerSocket;
    private static final UUID SECURE_UUID = UUID.fromString("01010101-123124151-123151");


    public AcceptThread() {
        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code
            tmp = BluetoothAdapter.getDefaultAdapter().listenUsingRfcommWithServiceRecord("Test1", SECURE_UUID);
        } catch (IOException e) { }
        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned
        while (true) {
            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                break;
            }
            // If a connection was accepted
            if (socket != null) {
                try{
                    Log.d("MSG", "SOCKET ACCEPTED.");
                    // Do work to manage the connection (in a separate thread)
                    //manageConnectedSocket(socket);
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
}