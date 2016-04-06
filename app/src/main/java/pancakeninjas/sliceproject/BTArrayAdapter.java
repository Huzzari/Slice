package pancakeninjas.sliceproject;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by darkhobbo on 4/6/2016.
 */
public class BTArrayAdapter extends ArrayAdapter<BluetoothDevice> {

    public BTArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public BTArrayAdapter(Context context, int resource, BluetoothDevice[] items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.btlist, null);
        }

        BluetoothDevice device = getItem(position);

        if (device != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.deviceName);

            if (tt1 != null) {
                tt1.setText(device.getName());
            }
        }

        return v;
    }

}
