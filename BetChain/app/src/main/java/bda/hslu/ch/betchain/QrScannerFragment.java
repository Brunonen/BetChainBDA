package bda.hslu.ch.betchain;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class QrScannerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_qr_scanner, container, false);


        /*Button registerButton = (Button) root.findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                Fragment registerUserFragment = new RegisterUserFragment();
                MainActivity activity = (MainActivity) getActivity();
                activity.changeFragment(registerUserFragment);


            }
        });*/


        return root;
    }
}