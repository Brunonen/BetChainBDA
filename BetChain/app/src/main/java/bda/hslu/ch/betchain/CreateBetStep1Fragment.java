package bda.hslu.ch.betchain;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class CreateBetStep1Fragment extends Fragment {

    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_create_bet_step1, container, false);

        final Button startBetCreation = (Button) rootView.findViewById(R.id.createBetButtonStart);
        startBetCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                TextView betTitle = (TextView) rootView.findViewById(R.id.inputBetTitle);

                MainActivity activity = (MainActivity) getActivity();
                if (betTitle.getText() != "" && betTitle.getText().length() >= 8) {


                    Fragment step2 = new CreateBetStep2Fragment();

                    activity.changeFragment(step2);
                } else {
                    Toast.makeText(activity, "Please enter a Bet Title that is atleast 8 Characters long.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

}