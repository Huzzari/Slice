package pancakeninjas.sliceproject;

public interface Constants {
    // Message types sent from the BluetoothChatService Handler
    int MESSAGE_STATE_CHANGE = 1;
    int MESSAGE_READ = 2;
    int MESSAGE_WRITE = 3;
    int MESSAGE_DEVICE_NAME = 4;
    int MESSAGE_TOAST = 5;
    int speed = 2000;
    int speed2 = 1500;
    int speed3 = 1000;
    int delay = 300;
    int delay2 = 750;
    int delay3 = 1000;
    int cap = 20;

    // Key names received from the BluetoothChatService Handler
    String DEVICE_NAME = "device_name";
    String TOAST = "toast";
}

