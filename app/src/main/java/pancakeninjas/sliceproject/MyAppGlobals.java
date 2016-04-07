package pancakeninjas.sliceproject;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MyAppGlobals extends Application {

    private final Handler mHandler = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.d("MESSAGE", readMessage);
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
            return true;
        }
    });

    public Handler getHandler(){
        return mHandler;
    }
}
