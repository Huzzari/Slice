package pancakeninjas.sliceproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragHowToBoss extends Fragment{

    private ConnectActivity connectActivity;
    private Button rdyBtn, cancelBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.frag_how_to_boss, container, false);

        connectActivity = (ConnectActivity)getActivity();

        rdyBtn = (Button)view.findViewById(R.id.readyBtn);
        cancelBtn = (Button)view.findViewById(R.id.cancelBtn);

        rdyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectActivity.enemyReady) {
                    rdyBtn.setBackgroundColor(Color.parseColor("#33cc33"));
                    // Say Im ready and starting game.
                    connectActivity.sendReady();
                    connectActivity.startGame();
                } else {
                    rdyBtn.setBackgroundColor(Color.parseColor("#33cc33"));
                    connectActivity.areYouReady();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectActivity.sendCancel();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.detach(fm.findFragmentByTag("howToBossFrag"));
                ft.commit();
                connectActivity.finish();
            }
        });

        return view;
    }
}
