package bda.hslu.ch.betchain;

import android.graphics.Color;
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

import bda.hslu.ch.betchain.Adapters.CustomAdapterParticipantInfo;
import bda.hslu.ch.betchain.DTO.Bet;
import bda.hslu.ch.betchain.DTO.BetRole;
import bda.hslu.ch.betchain.DTO.BetState;
import bda.hslu.ch.betchain.DTO.Participant;
import bda.hslu.ch.betchain.WebFunctions.BetFunctions;


public class BetInfoFragment extends Fragment {

    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_bet_info, container, false);
        MainActivity activity = (MainActivity) getActivity();
        Bet selectedBetInfo = BetFunctions.getBetFromAddress(activity.getSelectedBetAddress());
        selectedBetInfo.setParticipants(addProfilePictures(selectedBetInfo.getParticipants()));

        TextView betTitle = (TextView) rootView.findViewById(R.id.betInfoBetTitle);
        betTitle.setText(selectedBetInfo.getBetTitle());

        EditText betConditions = (EditText) rootView.findViewById(R.id.betInfoBetConditions);
        betConditions.setText(selectedBetInfo.getBetConditions());

        EditText betEntryFee = (EditText) rootView.findViewById(R.id.betInfoBetEntryFee);
        betEntryFee.setText(String.valueOf(selectedBetInfo.getBetEntryFee()) + " Eth");

        EditText betPrizePool = (EditText) rootView.findViewById(R.id.betInfoBetPrizePool);
        betPrizePool.setText(String.valueOf(selectedBetInfo.getBetPrizePool()) + " Eth");

        EditText betStatus = (EditText) rootView.findViewById(R.id.betInfoBetStatus);
        betStatus.setText(selectedBetInfo.getBetState().toString());

        Button acceptBetButton = (Button) rootView.findViewById(R.id.buttonBetInfoAcceptBet);
        Button retreatFromBetButton = (Button) rootView.findViewById(R.id.buttonBetInfoRetreat);
        Button startBetButton = (Button) rootView.findViewById(R.id.buttonBetInfoStartBet);
        Button startVoteButton = (Button) rootView.findViewById(R.id.buttonBetInfoStartVote);
        Button betSuccessButton = (Button) rootView.findViewById(R.id.buttonBetInfoBetSuccess);
        Button betFailureButton = (Button) rootView.findViewById(R.id.buttonBetInfoBetFailure);

        Participant betOwner = new Participant();
        Participant loggedInUser = new Participant();

        for(Participant p : selectedBetInfo.getParticipants()){
            if(p.getBetRole() == BetRole.OWNER){
                betOwner = p;
            }
            if(p.getUsername() == "Kay Hartmann"){
                loggedInUser = p;
            }
        }

        disableAllButtons();

        if(selectedBetInfo.getBetState() == BetState.PENDING){
            for(int i = 0; i < selectedBetInfo.getParticipants().size(); i++) {
                if(selectedBetInfo.getParticipants().get(i).hasBetAccepted()){
                    selectedBetInfo.getParticipants().get(i).setInfoIcon(R.drawable.ic_check_black_24dp);
                }else{
                    selectedBetInfo.getParticipants().get(i).setInfoIcon(R.drawable.ic_close_black_24dp);
                }
            }

            if(betOwner.getUsername() == loggedInUser.getUsername()){
                startBetButton.setVisibility(View.VISIBLE);
                startBetButton.setEnabled(true);
            }else{
                if(!loggedInUser.hasBetAccepted()){
                    acceptBetButton.setEnabled(true);
                    acceptBetButton.setVisibility(View.VISIBLE);
                    retreatFromBetButton.setEnabled(false);
                    retreatFromBetButton.setVisibility(View.VISIBLE);
                }else{
                    retreatFromBetButton.setEnabled(true);
                    retreatFromBetButton.setVisibility(View.VISIBLE);
                    acceptBetButton.setEnabled(false);
                    acceptBetButton.setVisibility(View.VISIBLE);
                }
            }
        }

        if(selectedBetInfo.getBetState() == BetState.LOCKED){
            if(betOwner.getUsername() == loggedInUser.getUsername()){
                startVoteButton.setVisibility(View.VISIBLE);
                startVoteButton.setEnabled(true);
            }
        }

        if(selectedBetInfo.getBetState() == BetState.EVALUATION){
            for(int i = 0; i < selectedBetInfo.getParticipants().size(); i++) {
                if(selectedBetInfo.getParticipants().get(i).hasVoted()){
                    selectedBetInfo.getParticipants().get(i).setInfoIcon(R.drawable.ic_mode_comment_black_24dp);
                }
            }

            if(betOwner.getUsername() != loggedInUser.getUsername()){
                if(!loggedInUser.hasVoted()){
                    betSuccessButton.setEnabled(true);
                    betSuccessButton.setVisibility(View.VISIBLE);
                    betFailureButton.setEnabled(true);
                    betFailureButton.setVisibility(View.VISIBLE);
                }
            }
        }

        if(selectedBetInfo.getBetState() == BetState.CONCLUDED){
            for(int i = 0; i < selectedBetInfo.getParticipants().size(); i++){
                if(selectedBetInfo.isBetSuccessful() && selectedBetInfo.getParticipants().get(i).getBetRole() == BetRole.SUPPORTER){
                    selectedBetInfo.getParticipants().get(i).setInfoIcon(R.drawable.ic_menu_createbet);
                }
                else if(!selectedBetInfo.isBetSuccessful() && selectedBetInfo.getParticipants().get(i).getBetRole() == BetRole.OPPOSER){
                    selectedBetInfo.getParticipants().get(i).setInfoIcon(R.drawable.ic_menu_createbet);
                }
            }
        }

        if(selectedBetInfo.getBetState() == BetState.ABORTED){
            betStatus.setTextColor(Color.RED);
        }




        ListView betParticipantListView = (ListView) rootView.findViewById(R.id.betInfoBetParticpantsList);
        CustomAdapterParticipantInfo adapter = new CustomAdapterParticipantInfo (activity, selectedBetInfo.getParticipants());
        betParticipantListView.setAdapter(adapter);


        return rootView;
    }

    private void disableAllButtons(){
        Button acceptBetButton = (Button) rootView.findViewById(R.id.buttonBetInfoAcceptBet);
        Button retreatFromBetButton = (Button) rootView.findViewById(R.id.buttonBetInfoRetreat);
        Button startBetButton = (Button) rootView.findViewById(R.id.buttonBetInfoStartBet);
        Button startVoteButton = (Button) rootView.findViewById(R.id.buttonBetInfoStartVote);
        Button betSuccessButton = (Button) rootView.findViewById(R.id.buttonBetInfoBetSuccess);
        Button betFailureButton = (Button) rootView.findViewById(R.id.buttonBetInfoBetFailure);

        acceptBetButton.setVisibility(View.INVISIBLE);
        retreatFromBetButton.setVisibility(View.INVISIBLE);
        startBetButton.setVisibility(View.INVISIBLE);
        startVoteButton.setVisibility(View.INVISIBLE);
        betSuccessButton.setVisibility(View.INVISIBLE);
        betFailureButton.setVisibility(View.INVISIBLE);

        acceptBetButton.setEnabled(false);
        retreatFromBetButton.setEnabled(false);
        startBetButton.setEnabled(false);
        startVoteButton.setEnabled(false);
        betSuccessButton.setEnabled(false);
        betFailureButton.setEnabled(false);
    }

    private List<Participant> addProfilePictures(List<Participant> betParticipants){
        for(int i = 0; i < betParticipants.size(); i++){
            if(betParticipants.get(i).getUsername() == "Kay Hartmann"){
                betParticipants.get(i).setProfilePicture(R.drawable.kay);
            }
            if(betParticipants.get(i).getUsername() == "Bruno Fischlin"){
                betParticipants.get(i).setProfilePicture(R.drawable.bruno);
            }
            if(betParticipants.get(i).getUsername() == "Damir Hodzic"){
                betParticipants.get(i).setProfilePicture(R.drawable.damir);
            }
            if(betParticipants.get(i).getUsername() == "Alex Neher"){
                betParticipants.get(i).setProfilePicture(R.drawable.alex);
            }
            if(betParticipants.get(i).getUsername() == "Suki Kasipillai"){
                betParticipants.get(i).setProfilePicture(R.drawable.suki);
            }
        }

        return betParticipants;

    }
}