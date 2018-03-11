package bda.hslu.ch.betchain;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class CreateBetStep2Fragment extends Fragment {

    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_create_bet_step2, container, false);

        final Button goToStep3 = (Button) rootView.findViewById(R.id.button_goToStep3);
        goToStep3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                EditText betConditions = (EditText) rootView.findViewById(R.id.betConditions);
                EditText betEntryFees = (EditText) rootView.findViewById(R.id.betEntryFee);
                MainActivity activity = (MainActivity) getActivity();
                //Fragment step2 = new CreateBetStep2Fragment();

                if(!betConditions.getText().equals("")){
                    Fragment step3 = new CreateBetStep3Fragment();

                    activity.changeFragment(step3);
                }else{
                    Toast.makeText(activity ,"Please enter Conditions for your bet!",  Toast.LENGTH_SHORT).show();
                }
            }
        });


        return rootView;
    }

    public void goToStep3(View v) {

    }

    public void backToStep1(View v){

    }
}