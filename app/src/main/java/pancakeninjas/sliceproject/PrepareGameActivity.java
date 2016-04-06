package pancakeninjas.sliceproject;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by darkhobbo on 4/5/2016.
 */
public class PrepareGameActivity extends AppCompatActivity {

    public BluetoothAdapter bluetoothAdapter;
    private BluetoothServerSocket serverSocket;
    private static final UUID SECURE_UUID = UUID.fromString("01010101-123124151-123151");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prepare_game_activity);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter==null){
            Log.d("BluetoothAdapter","Bluetooth not supported");
        }
        else{
            Intent discoverableIntent = new Intent(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION);
            startActivity(discoverableIntent);
            Log.d("Bluetooth", "Discoverable");

            AcceptThread();
            run();
        }
    }

    private void AcceptThread(){
        BluetoothServerSocket tmp = null;
        try{
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("Test1", SECURE_UUID);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        serverSocket = tmp;
    }
    private void run(){
        BluetoothSocket socket = null;
        while(true){
            try{
                socket = serverSocket.accept();
                Log.d("MSG", "LISTENING");
            }
            catch (IOException e){
                e.printStackTrace();
                break;
            }
            if(socket != null){
                Log.d("MSG", "DONE");
                try{
                    // Manage socket here then close.
                    // Make a call to a manage method i write.
                    serverSocket.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
