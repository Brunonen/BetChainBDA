package bda.hslu.ch.betchain;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import bda.hslu.ch.betchain.BlockChainFunctions.BlockChainFunctions;
import bda.hslu.ch.betchain.DTO.Bet;
import bda.hslu.ch.betchain.DTO.BetRole;
import bda.hslu.ch.betchain.DTO.BetState;
import bda.hslu.ch.betchain.DTO.Participant;
import bda.hslu.ch.betchain.Database.DBSessionSingleton;
import bda.hslu.ch.betchain.Database.SQLWrapper;
import bda.hslu.ch.betchain.WebFunctions.BetFunctions;


public class BetInfoFragment extends Fragment {

    private View rootView;
    private Participant loggedInUser;
    private Participant betOwner;
    private int opposerCount;
    private int abortCount;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_bet_info, container, false);
        final MainActivity activity = (MainActivity) getActivity();
        final Bet selectedBetInfo;
        try {
            selectedBetInfo = BlockChainFunctions.getBetInfoFromBlockchain(activity.getSelectedBet());

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
            Button abortBetButton = (Button) rootView.findViewById(R.id.buttonAbortBet);

            betOwner = new Participant();
            final String[] loggedInUserInfo = getUserInfo();
            loggedInUser = new Participant();

            loggedInUser.setUsername(loggedInUserInfo[0]);
            loggedInUser.setAddress(loggedInUserInfo[3]);
            opposerCount = 0;
            abortCount = 0;

            //Loop through all participants to find the user and which of those is the logged in User.
            for(Participant p : selectedBetInfo.getParticipants()){
                if(p.getBetRole() == BetRole.OWNER){
                    betOwner = p;
                }

                if(p.getBetRole() == BetRole.OPPOSER && p.hasBetAccepted()){
                    opposerCount++;
                }

                if(p.getUsername().equals(loggedInUser.getUsername())){
                    loggedInUser = p;
                }

                if(p.isAbortVoted()){
                    abortCount++;
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

                if(betOwner.getUsername().equals(loggedInUser.getUsername()) || betOwner.getAddress().equals(loggedInUser.getAddress())){
                    startBetButton.setVisibility(View.VISIBLE);
                    startBetButton.setEnabled(true);
                }else{
                    acceptBetButton.setEnabled(true);
                    acceptBetButton.setVisibility(View.VISIBLE);
                    retreatFromBetButton.setEnabled(true);
                    retreatFromBetButton.setVisibility(View.VISIBLE);

                }

                abortBetButton.setVisibility(View.VISIBLE);
                abortBetButton.setEnabled(true);
            }

            if(selectedBetInfo.getBetState() == BetState.LOCKED){
                if(betOwner.getUsername() == loggedInUser.getUsername()){
                    startVoteButton.setVisibility(View.VISIBLE);
                    startVoteButton.setEnabled(true);
                }

                abortBetButton.setVisibility(View.VISIBLE);
                abortBetButton.setEnabled(true);
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

                abortBetButton.setVisibility(View.VISIBLE);
                abortBetButton.setEnabled(true);


            }

            if(selectedBetInfo.getBetState() == BetState.CONCLUDED){
                for(int i = 0; i < selectedBetInfo.getParticipants().size(); i++){
                    if(selectedBetInfo.isBetSuccessful() && (selectedBetInfo.getParticipants().get(i).getBetRole() == BetRole.SUPPORTER || selectedBetInfo.getParticipants().get(i).getBetRole() == BetRole.OWNER)){
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

            if(selectedBetInfo.getBetState() != BetState.PENDING){
                List<Participant> acceptedParticipants = selectedBetInfo.getParticipants();

                //Remove participants which haven't accepted the bet
                for(int i = 0; i < acceptedParticipants.size(); i++){
                    if(!acceptedParticipants.get(i).hasBetAccepted()){
                        acceptedParticipants.remove(i);
                        i = 0;
                    }
                }

                selectedBetInfo.setParticipants(acceptedParticipants);
            }

            if(loggedInUser.isAbortVoted()) {
                abortBetButton.setText(abortCount + "/" + selectedBetInfo.getParticipants().size() + " aborted");
                abortBetButton.setEnabled(false);
            }

            CustomAdapterParticipantInfo adapter = new CustomAdapterParticipantInfo (activity, selectedBetInfo.getParticipants());
            betParticipantListView.setAdapter(adapter);

            if(selectedBetInfo.getBetState() != BetState.PENDING) {
                if (!loggedInUser.hasBetAccepted()) {
                    disableAllButtons();
                }
            }

            acceptBetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if(!loggedInUser.hasBetAccepted()) {
                        Toast.makeText(activity, "Request submitted! Please keep in mind that changes might take a few minutes to take effect on the Blockchain" , Toast.LENGTH_LONG).show();
                        BlockChainFunctions.acceptBet(selectedBetInfo.getBetAddress(), loggedInUser.getBetRole(), selectedBetInfo.getBetEntryFee());
                    }else{
                        Toast.makeText(activity, "You have already accepted this bet!" , Toast.LENGTH_SHORT).show();
                    }
                }
            });

            retreatFromBetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if(loggedInUser.getBetRole() != BetRole.NOTAR){
                        if(loggedInUser.hasBetAccepted()) {
                            Toast.makeText(activity, "Request submitted! Please keep in mind that changes might take a few minutes to take effect on the Blockchain!" , Toast.LENGTH_LONG).show();
                            BlockChainFunctions.retreatFromBet(selectedBetInfo.getBetAddress());
                        }else{
                            Toast.makeText(activity, "You can not retreat from a bet you haven't accepted!" , Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(activity, "Notars can not retreat from a bet, seeing as they do not have to pay any fees." , Toast.LENGTH_SHORT).show();
                    }
                }
            });

            startBetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if(opposerCount > 0){
                        Toast.makeText(activity, "Request submitted! Please keep in mind that changes might take a few minutes to take effect on the Blockchain!" , Toast.LENGTH_LONG).show();
                        BlockChainFunctions.startBet(selectedBetInfo.getBetAddress());
                    }else{
                        Toast.makeText(activity, "Atleast one Opposer must accept the Bet, in Order for it to be startet!" , Toast.LENGTH_SHORT).show();
                    }
                }
            });

            startVoteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                    builder.setPositiveButton("Bet Success", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            BlockChainFunctions.startVoting(selectedBetInfo.getBetAddress(), true);
                        }
                    });
                    builder.setNegativeButton("Bet Failure", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            BlockChainFunctions.startVoting(selectedBetInfo.getBetAddress(), false);
                        }
                    });

                    builder.setMessage("Please Select how the bet concluded")
                            .setTitle("Vote Confirmation");


                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            betSuccessButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    BlockChainFunctions.vote(selectedBetInfo.getBetAddress(), true);
                }
            });

            betFailureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    BlockChainFunctions.vote(selectedBetInfo.getBetAddress(), false);
                }
            });

            abortBetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if(!loggedInUser.isAbortVoted()) {
                        BlockChainFunctions.abort(selectedBetInfo.getBetAddress());
                        Toast.makeText(activity, "Abort submitted! Bet will be aborted if all accepted Participants agreed to abort this bet" , Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(activity, "You have already submitted an abort request!" , Toast.LENGTH_SHORT).show();
                    }

                }
            });

        } catch (Exception e) {
            Toast.makeText(activity, e.getMessage() , Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    private void disableAllButtons(){
        Button acceptBetButton = (Button) rootView.findViewById(R.id.buttonBetInfoAcceptBet);
        Button retreatFromBetButton = (Button) rootView.findViewById(R.id.buttonBetInfoRetreat);
        Button startBetButton = (Button) rootView.findViewById(R.id.buttonBetInfoStartBet);
        Button startVoteButton = (Button) rootView.findViewById(R.id.buttonBetInfoStartVote);
        Button betSuccessButton = (Button) rootView.findViewById(R.id.buttonBetInfoBetSuccess);
        Button betFailureButton = (Button) rootView.findViewById(R.id.buttonBetInfoBetFailure);
        Button abortBet = (Button) rootView.findViewById(R.id.buttonAbortBet);

        acceptBetButton.setVisibility(View.INVISIBLE);
        retreatFromBetButton.setVisibility(View.INVISIBLE);
        startBetButton.setVisibility(View.INVISIBLE);
        startVoteButton.setVisibility(View.INVISIBLE);
        betSuccessButton.setVisibility(View.INVISIBLE);
        betFailureButton.setVisibility(View.INVISIBLE);
        abortBet.setVisibility(View.INVISIBLE);

        acceptBetButton.setEnabled(false);
        retreatFromBetButton.setEnabled(false);
        startBetButton.setEnabled(false);
        startVoteButton.setEnabled(false);
        betSuccessButton.setEnabled(false);
        betFailureButton.setEnabled(false);
        abortBet.setEnabled(false);
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

    private String[] getUserInfo(){
        String[] returnString;
        MainActivity activity = (MainActivity) getActivity();
        SQLWrapper db = DBSessionSingleton.getInstance().getDbUtil();
        returnString = db.getLoggedInUserInfo();
        return returnString;
    }


}