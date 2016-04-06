package pancakeninjas.sliceproject;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.UUID;

/**
 * Created by darkhobbo on 4/5/2016.
 */
public class PrepareGameActivity extends AppCompatActivity {

    public BluetoothAdapter bluetoothAdapter;
    private BluetoothServerSocket serverSocket;
    private static final UUID SECURE_UUID = UUID.randomUUID();

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
        }
    }

    private void AcceptThread(){
        BluetoothServerSocket tmp = null;
        try{
            //tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("HELLO", SECURE_UUID);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
