package bda.hslu.ch.betchain;

import android.provider.Telephony;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.ArrayList;

import bda.hslu.ch.betchain.Adapters.CustomAdapterParticipantInfo;
import bda.hslu.ch.betchain.DTO.BetRole;
import bda.hslu.ch.betchain.DTO.Participant;


public class CreateBetStep3Fragment extends Fragment {

    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_create_bet_step3, container, false);

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

        List<Participant> betSupporters = new ArrayList<Participant>();
        List<Participant> betOpposers = new ArrayList<Participant>();
        List<Participant> notars = new ArrayList<Participant>();;

        betSupporters.add(kay);
        betSupporters.add(alex);

        betOpposers.add(bruno);
        betOpposers.add(damir);

        notars.add(suki);


        MainActivity activity = (MainActivity) getActivity();
        ListView betSupporterList = (ListView) rootView.findViewById(R.id.betSupporterList);
        CustomAdapterParticipantInfo adapter = new CustomAdapterParticipantInfo (activity, betSupporters);
        betSupporterList.setAdapter(adapter);

        ListView betOpposerList = (ListView) rootView.findViewById(R.id.betOpposerList);
        adapter = new CustomAdapterParticipantInfo (activity, betOpposers);
        betOpposerList.setAdapter(adapter);

        ListView betNotarList = (ListView) rootView.findViewById(R.id.betNotarList);
        adapter = new CustomAdapterParticipantInfo (activity, notars);
        betNotarList.setAdapter(adapter);

        final Button goToStep4 = (Button) rootView.findViewById(R.id.button_goToStep4);
        goToStep4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                MainActivity activity = (MainActivity) getActivity();
                //Fragment step2 = new CreateBetStep2Fragment();

                Fragment step4 = new CreateBetStep4Fragment();

                activity.changeFragment(step4);

            }
        });

        return rootView;
    }



}