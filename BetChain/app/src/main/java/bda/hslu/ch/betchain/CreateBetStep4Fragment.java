package bda.hslu.ch.betchain;

import android.provider.Telephony;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.ArrayList;

import bda.hslu.ch.betchain.Adapters.CustomAdapterParticipantInfo;
import bda.hslu.ch.betchain.DTO.BetRole;
import bda.hslu.ch.betchain.DTO.Participant;


public class CreateBetStep4Fragment extends Fragment {

    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_create_bet_step4, container, false);

        MainActivity activity = (MainActivity) getActivity();

        TextView betTitle = (TextView) rootView.findViewById(R.id.inputBetTitleConfirmation);
        betTitle.setText(activity.getBetCreationBetTitle());

        TextView betConditions = (TextView) rootView.findViewById(R.id.inputBetConditionsConfirmation);
        betConditions.setText(activity.getBetCreationBetConditions());

        EditText betEntryFees = (EditText) rootView.findViewById(R.id.inputEntryFeeConfirmation);
        betEntryFees.setText(String.valueOf(activity.getBetCreationBetEntryFee()));

        final List<Participant> participantList = activity.getBetCreationParticipants();

        /*
        Participant kay = new Participant("Kay Hartmann", "0x627306090abaB3A6e1400e9345bC60c78a8BEf57", true, false, BetRole.OWNER);
        kay.setProfilePicture(R.drawable.kay);

        Participant bruno = new Participant("Bruno Fischlin", "0xf17f52151EbEF6C7334FAD080c5704D77216b732", false, false, BetRole.OPPOSER);
        bruno.setProfilePicture(R.drawable.bruno);

        Participant damir = new Participant("Damir Hodzic", "0xC5fdf4076b8F3A5357c5E395ab970B5B54098Fef", false, false, BetRole.OPPOSER);
        damir.setProfilePicture(R.drawable.damir);

        Participant alex = new Participant("Alex Neher", "0x821aEa9a577a9b44299B9c15c88cf3087F3b5544", false, false, BetRole.SUPPORTER);
        alex.setProfilePicture(R.drawable.alex);

        Participant suki = new Participant("Suki Kasipillai", "0x0d1d4e623D10F9FBA5Db95830F7d3839406C6AF2", false, false, BetRole.NOTAR);
        suki.setProfilePicture(R.drawable.suki);

        List<Participant> participants = new ArrayList<Participant>();


        participants.add(kay);
        participants.add(alex);

        participants.add(bruno);
        participants.add(damir);

        participants.add(suki);
    */

        ListView betParticipantListView = (ListView) rootView.findViewById(R.id.betParticipantList);
        CustomAdapterParticipantInfo adapter = new CustomAdapterParticipantInfo (activity, participantList);
        betParticipantListView.setAdapter(adapter);

        return rootView;
    }

    public void goToStep4(View v) {
        EditText betConditions = (EditText) rootView.findViewById(R.id.betConditions);
        EditText betEntryFees = (EditText) rootView.findViewById(R.id.betEntryFee);

        //Fragment step2 = new CreateBetStep2Fragment();

        if(!betConditions.getText().equals("")){

        }else{
            Toast.makeText(this.getActivity() ,"Please enter Conditions for your bet!",  Toast.LENGTH_SHORT).show();
        }
    }

    public void backToStep2(View v){

    }
}