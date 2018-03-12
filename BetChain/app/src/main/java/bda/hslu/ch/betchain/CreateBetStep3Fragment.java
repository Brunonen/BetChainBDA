package bda.hslu.ch.betchain;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import bda.hslu.ch.betchain.DTO.Friend;
import bda.hslu.ch.betchain.WebFunctions.FriendFunctions;


public class CreateBetStep3Fragment extends Fragment {

    private View rootView;
    private List<Participant> betParticipants = new ArrayList<Participant>();

    private List<Participant> betSupporters = new ArrayList<Participant>();
    private List<Participant> betOpposers = new ArrayList<Participant>();
    private List<Participant> notars = new ArrayList<Participant>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_create_bet_step3, container, false);

        MainActivity activity = (MainActivity) getActivity();

        betParticipants = activity.getBetCreationParticipants();


        if(betParticipants.size() > 0){
            for(Participant p : betParticipants){
                switch(p.getBetRole()){
                    case OWNER:     betSupporters.add(p);
                                    break;
                    case SUPPORTER: betSupporters.add(p);
                                    break;
                    case OPPOSER:   betOpposers.add(p);
                                    break;
                    case NOTAR:     notars.add(p);
                                    break;

                }
            }
        }



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


        betSupporters.add(kay);
        betSupporters.add(alex);

        betOpposers.add(bruno);
        betOpposers.add(damir);

        notars.add(suki);

*/
        final ListView betSupporterList = (ListView) rootView.findViewById(R.id.betSupporterList);
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

                List<Participant> betParticipants = new ArrayList<Participant>();

                List<Participant> currentBetSupporters = getParticipantsFromListView((ListView) rootView.findViewById(R.id.betSupporterList));
                for(Participant p : currentBetSupporters){
                    betParticipants.add(p);
                }

                List<Participant> currentBetOpposers = getParticipantsFromListView((ListView) rootView.findViewById(R.id.betOpposerList));
                for(Participant p : currentBetOpposers){
                    betParticipants.add(p);
                }

                List<Participant> currentBetNotars = getParticipantsFromListView((ListView) rootView.findViewById(R.id.betNotarList));
                for(Participant p : currentBetNotars){
                    betParticipants.add(p);
                }

                if(currentBetSupporters.size() > 0 && currentBetOpposers.size() > 0){

                    activity.setBetCreationParticipants(betParticipants);
                    Fragment step4 = new CreateBetStep4Fragment();

                    activity.changeFragment(step4);
                }else{
                    Toast.makeText(activity ,"A bet must at least have 1 Supporter and 1 Opposer",  Toast.LENGTH_SHORT).show();
                }



            }
        });

        final Button addBetSupporter = (Button) rootView.findViewById(R.id.buttonAddBetSupporter);
        goToStep4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                MainActivity activity = (MainActivity) getActivity();

                final List<Friend> userFriends = FriendFunctions.getUserFriendList();
                CharSequence friends[] = new CharSequence[userFriends.size()];
                for(int i = 0; i < userFriends.size(); i++){
                    friends[i] = userFriends.get(i).getUsername();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Choose Participant");
                builder.setItems(friends, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Friend userToAdd = userFriends.get(which);
                        //String username, String address, boolean betAccepted, boolean voted, BetRole betRole) {
                        Participant participantToAdd = new Participant(userToAdd.getUsername(), userToAdd.getAddress(), false, false, BetRole.SUPPORTER);
                        participantToAdd.setProfilePicture(userToAdd.getProfilePicture());

                        betSupporters.add(participantToAdd);
                        updateParticipantLists();
                    }
                });
                builder.show();
            }
        });

        return rootView;
    }


    private List<Participant> getParticipantsFromListView(ListView listView){

        List<Participant> participantList = new ArrayList<Participant>();

        ListView participantListView = listView;
        CustomAdapterParticipantInfo adapter = (CustomAdapterParticipantInfo) participantListView.getAdapter();
        if(adapter.getCount() > 0){
            for(int i = 0; i < adapter.getCount(); i++){
                participantList.add(adapter.getItem(i));
            }
        }

        return participantList;
    }

    private void updateParticipantLists(){
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
    }


}