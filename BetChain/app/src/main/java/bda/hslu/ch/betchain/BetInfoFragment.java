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

import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import bda.hslu.ch.betchain.Adapters.CustomAdapterParticipantInfo;
import bda.hslu.ch.betchain.BlockChainFunctions.BlockChainFunctions;
import bda.hslu.ch.betchain.DTO.AppUser;
import bda.hslu.ch.betchain.DTO.Bet;
import bda.hslu.ch.betchain.DTO.BetRole;
import bda.hslu.ch.betchain.DTO.BetState;
import bda.hslu.ch.betchain.DTO.CurrencySelector;
import bda.hslu.ch.betchain.DTO.Participant;
import bda.hslu.ch.betchain.Database.DBSessionSingleton;
import bda.hslu.ch.betchain.Database.SQLWrapper;
import bda.hslu.ch.betchain.WebFunctions.BetFunctions;
import bda.hslu.ch.betchain.WebFunctions.CurrencyExchangeAPI;
import bda.hslu.ch.betchain.WebFunctions.CurrencyExchangerSingleton;


public class BetInfoFragment extends Fragment {

    private View rootView;
    private Participant loggedInUser;
    private Participant betOwner;
    private int opposerCount;
    private int abortCount;
    private String warningMessage ="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_bet_info, container, false);
        final MainActivity activity = (MainActivity) getActivity();
        final Bet selectedBetInfo;
        try {
            selectedBetInfo = BlockChainFunctions.getBetInfoFromBlockchain(activity.getSelectedBet());
            final AppUser loggedInUserInfo = AppUser.getLoggedInUserObject();
            System.out.println(selectedBetInfo.getBetEntryFee());

            TextView betTitle = (TextView) rootView.findViewById(R.id.betInfoBetTitle);
            betTitle.setText(selectedBetInfo.getBetTitle());

            EditText betConditions = (EditText) rootView.findViewById(R.id.betInfoBetConditions);
            betConditions.setText(selectedBetInfo.getBetConditions());

            EditText betEntryFee = (EditText) rootView.findViewById(R.id.betInfoBetEntryFee);
            betEntryFee.setText(CurrencyExchangerSingleton.getInstance().exchangeCurrency(String.valueOf(new BigDecimal(String.valueOf(selectedBetInfo.getBetEntryFee()))), CurrencySelector.ETH, loggedInUserInfo.getPrefferedCurrency()) + " " + loggedInUserInfo.getPrefferedCurrency().toString());

            EditText betPrizePool = (EditText) rootView.findViewById(R.id.betInfoBetPrizePool);
            betPrizePool.setText(CurrencyExchangerSingleton.getInstance().exchangeCurrency(String.valueOf(new BigDecimal(String.valueOf(selectedBetInfo.getBetPrizePool()))), CurrencySelector.ETH, loggedInUserInfo.getPrefferedCurrency()) + " " +loggedInUserInfo.getPrefferedCurrency().toString());

            EditText betStatus = (EditText) rootView.findViewById(R.id.betInfoBetStatus);
            betStatus.setText(selectedBetInfo.getBetState().toString());

            //Bind all  Buttons
            Button acceptBetButton = (Button) rootView.findViewById(R.id.buttonBetInfoAcceptBet);
            Button retreatFromBetButton = (Button) rootView.findViewById(R.id.buttonBetInfoRetreat);
            Button startBetButton = (Button) rootView.findViewById(R.id.buttonBetInfoStartBet);
            Button startVoteButton = (Button) rootView.findViewById(R.id.buttonBetInfoStartVote);
            Button betSuccessButton = (Button) rootView.findViewById(R.id.buttonBetInfoBetSuccess);
            Button betFailureButton = (Button) rootView.findViewById(R.id.buttonBetInfoBetFailure);
            Button abortBetButton = (Button) rootView.findViewById(R.id.buttonAbortBet);

            //Genreate contrainers and get LoggedIn UserInfo
            betOwner = new Participant();

            loggedInUser = new Participant();

            loggedInUser.setUsername(loggedInUserInfo.getUsername());
            loggedInUser.setAddress(loggedInUserInfo.getPublicAddress());
            final String loggedInPKey = loggedInUserInfo.getPrivateKey();
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

            //Per default disable and Hide all Buttons
            disableAllButtons();

            //Now "Switch" the entire Screen by the State the selected Bet is in
            if(selectedBetInfo.getBetState() == BetState.PENDING){
                //Set Flags if the Participants has Accepted the Bet or not
                for(int i = 0; i < selectedBetInfo.getParticipants().size(); i++) {
                    if(selectedBetInfo.getParticipants().get(i).hasBetAccepted()){
                        selectedBetInfo.getParticipants().get(i).setInfoIcon(R.drawable.ic_check_black_24dp);
                    }else{
                        selectedBetInfo.getParticipants().get(i).setInfoIcon(R.drawable.ic_close_black_24dp);
                    }
                }

                //Check if the User calling the Information is also its owner (Different Actions)
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
                //Set InfoIcon of Participant so it's visible if they voted or not
                for(int i = 0; i < selectedBetInfo.getParticipants().size(); i++) {
                    if(selectedBetInfo.getParticipants().get(i).hasVoted()){
                        selectedBetInfo.getParticipants().get(i).setInfoIcon(R.drawable.ic_mode_comment_black_24dp);
                    }
                }

                //Participants other then the Owner can Vote in this State.
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
                //Make Participants who won the Bet visible by an Icon
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

            //If the Bet has already commenced, filter out Participants that have to accepted the Bet
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

            //If the Bet has commenced and the Participant which is logged in hasn't accepted the bet, disable all Actions
            if(selectedBetInfo.getBetState() != BetState.PENDING) {
                if (!loggedInUser.hasBetAccepted()) {
                    disableAllButtons();
                }
            }

            //Check if user has enough blanace to commit change. If not set warning message.
            BigDecimal accBalance = BlockChainFunctions.getAccountBalance();
            BigDecimal changeMaxCost = new BigDecimal(BlockChainFunctions.getGasLimitChange()).multiply(new BigDecimal(BlockChainFunctions.getGasPrice()));
            if(accBalance.max(Convert.fromWei(changeMaxCost.toString(),Convert.Unit.ETHER)) != accBalance){
                warningMessage = "\n\nYour account has a low Ether Balance. Changes might not be able to be commited to the Blockchain!";
            }

            //Klick Listeners for The Bet Actions
            acceptBetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if(!loggedInPKey.equals("")) {
                        if (!loggedInUser.hasBetAccepted()) {

                            if(BlockChainFunctions.acceptBet(selectedBetInfo.getBetAddress(), loggedInUser.getBetRole(), selectedBetInfo.getBetEntryFee())){
                                Toast.makeText(activity, "Request submitted! Please keep in mind that changes might take a few minutes to take effect on the Blockchain"+warningMessage, Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(activity, "The Request was rejected! Please check if your Account has enough Balance to make changes on the Blockchain", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(activity, "You have already accepted this bet!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(activity, "Your Account does not have a private Key set! You need this information in order to interact and make changes on contracts!!", Toast.LENGTH_LONG).show();
                    }
                }
            });

            retreatFromBetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if(loggedInUser.getBetRole() != BetRole.NOTAR){
                        if(!loggedInPKey.equals("")) {
                            if (loggedInUser.hasBetAccepted()) {
                                Toast.makeText(activity, "Request submitted! Please keep in mind that changes might take a few minutes to take effect on the Blockchain!"+warningMessage, Toast.LENGTH_LONG).show();
                                BlockChainFunctions.retreatFromBet(selectedBetInfo.getBetAddress());
                            } else {
                                Toast.makeText(activity, "You can not retreat from a bet you haven't accepted!", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(activity, "Your Account does not have a private Key set! You need this information in order to interact and make changes on contracts!!", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(activity, "Request submitted! Please keep in mind that changes might take a few minutes to take effect on the Blockchain!"+warningMessage , Toast.LENGTH_LONG).show();
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
                            Toast.makeText(activity, "Request submitted! Please keep in mind that changes might take a few minutes to take effect on the Blockchain!"+warningMessage , Toast.LENGTH_LONG).show();
                            BlockChainFunctions.startVoting(selectedBetInfo.getBetAddress(), true);
                        }
                    });
                    builder.setNegativeButton("Bet Failure", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(activity, "Request submitted! Please keep in mind that changes might take a few minutes to take effect on the Blockchain!"+warningMessage , Toast.LENGTH_LONG).show();
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
                    if(!loggedInPKey.equals("")) {
                        Toast.makeText(activity, "Request submitted! Please keep in mind that changes might take a few minutes to take effect on the Blockchain!"+warningMessage , Toast.LENGTH_LONG).show();
                        BlockChainFunctions.vote(selectedBetInfo.getBetAddress(), true);
                    }else{
                        Toast.makeText(activity, "Your Account does not have a private Key set! You need this information in order to interact and make changes on contracts!!", Toast.LENGTH_LONG).show();
                    }
                }
            });

            betFailureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if(!loggedInPKey.equals("")) {
                        Toast.makeText(activity, "Request submitted! Please keep in mind that changes might take a few minutes to take effect on the Blockchain!"+warningMessage , Toast.LENGTH_LONG).show();
                        BlockChainFunctions.vote(selectedBetInfo.getBetAddress(), false);
                    }else{
                        Toast.makeText(activity, "Your Account does not have a private Key set! You need this information in order to interact and make changes on contracts!!", Toast.LENGTH_LONG).show();
                    }
                }
            });

            abortBetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if(!loggedInUser.isAbortVoted()) {
                        if(BlockChainFunctions.abort(selectedBetInfo.getBetAddress())) {
                            Toast.makeText(activity, "Abort submitted! Bet will be aborted if all accepted Participants agreed to abort this bet"+warningMessage, Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(activity, "The Request was rejected! Please check if your Account has enough Balance to make changes on the Blockchain", Toast.LENGTH_LONG).show();
                        }
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

    /***
     * Disables all Buttons of the Layout and makes them Invisible
     */
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



}