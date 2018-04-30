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

import bda.hslu.ch.betchain.DTO.AppUser;
import bda.hslu.ch.betchain.Database.DBSessionSingleton;
import bda.hslu.ch.betchain.Database.SQLWrapper;


public class CreateBetStep1Fragment extends Fragment {

    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_create_bet_step1, container, false);
        final MainActivity activity = (MainActivity) getActivity();

        TextView betTitle = (TextView) rootView.findViewById(R.id.inputBetTitle);
        betTitle.setText(activity.getBetCreationBetTitle());

        final AppUser loggedInUserInfo = AppUser.getLoggedInUserObject();

        final Button startBetCreation = (Button) rootView.findViewById(R.id.createBetButtonStart);
        startBetCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                TextView betTitle = (TextView) rootView.findViewById(R.id.inputBetTitle);

                MainActivity activity = (MainActivity) getActivity();

                //Failsafe Check if user has his account linked (Public and Private Key accessible). Otherwise he can't create bets!
                if(!loggedInUserInfo.getPublicAddress().equals("")) {
                    if (!loggedInUserInfo.getPrivateKey().equals("")) {

                        if (betTitle.getText() != "" && betTitle.getText().length() >= 8) {

                            activity.setBetCreationBetTitle(betTitle.getText().toString());
                            Fragment step2 = new CreateBetStep2Fragment();

                            activity.changeFragment(step2);
                        } else {
                            Toast.makeText(activity, "Please enter a Bet Title that is atleast 8 Characters long.", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(activity, "Your Account does not have a private Key set! You need this information in order to create contracts!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(activity, "Your Account is not linked to any Ethereum Account!", Toast.LENGTH_SHORT).show();
                }
            }
        });



        //Check if the logged in user has the required Information provided, in order to create a Bet!
        if(!loggedInUserInfo.getPublicAddress().equals("")) {
            if (loggedInUserInfo.getPrivateKey().equals("")) {
                Toast.makeText(activity, "Your Account does not have a private Key set! You need this information in order to create contracts!", Toast.LENGTH_SHORT).show();
            }
        }else{
            NoUserAddressDialog noUserAddressDialog = new NoUserAddressDialog();
            noUserAddressDialog.show(activity.getSupportFragmentManager(), "No Ethereum Wallet found");
        }

        return rootView;


    }

}