package bda.hslu.ch.betchain;

import android.app.IntentService;
import android.content.Intent;
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

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;

import bda.hslu.ch.betchain.Adapters.CustomAdapterParticipantInfo;
import bda.hslu.ch.betchain.BlockChainFunctions.BlockChainFunctions;
import bda.hslu.ch.betchain.BlockChainFunctions.ContractCreationIntentService;
import bda.hslu.ch.betchain.DTO.BetRole;
import bda.hslu.ch.betchain.DTO.Participant;
import bda.hslu.ch.betchain.Database.DBSessionSingleton;
import bda.hslu.ch.betchain.Database.SQLWrapper;
import bda.hslu.ch.betchain.WebFunctions.BetFunctions;


public class CreateBetStep4Fragment extends Fragment {

    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_create_bet_step4, container, false);

        final MainActivity activity = (MainActivity) getActivity();

        final TextView betTitle = (TextView) rootView.findViewById(R.id.inputBetTitleConfirmation);
        betTitle.setText(activity.getBetCreationBetTitle());

        final TextView betConditions = (TextView) rootView.findViewById(R.id.inputBetConditionsConfirmation);
        betConditions.setText(activity.getBetCreationBetConditions());

        final EditText betEntryFees = (EditText) rootView.findViewById(R.id.inputEntryFeeConfirmation);
        betEntryFees.setText(String.valueOf(activity.getBetCreationBetEntryFee()));

        final List<Participant> participantList = activity.getBetCreationParticipants();

        Button confirmCreation = (Button) rootView.findViewById(R.id.button_confirmBetCreation);

        confirmCreation.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(final View v) {

                   try{
                       float betEntryFee = Float.valueOf(betEntryFees.getText().toString());
                       String inputError = BetFunctions.checkIfBetInputsAreValid(betTitle.getText().toString(), betConditions.getText().toString(), participantList, betEntryFee);


                       if(inputError == "") {

                           String[] loggedInUserInfo = getUserInfo();

                           //Check if the logged in user has the required Information provided, in order to create a Bet!
                           if(!loggedInUserInfo[3].equals("")) {
                               if(!loggedInUserInfo[2].equals("")) {
                                    Intent betCreation = new Intent(activity, ContractCreationIntentService.class);
                                    betCreation.putExtra("betConditions", betConditions.getText().toString());
                                    betCreation.putExtra("betEntryFee", betEntryFees.getText().toString());
                                    betCreation.putExtra("participants", (Serializable) participantList);
                                    betCreation.putExtra("betTitle", betTitle.getText().toString());
                                    betCreation.putExtra("pKey", getUserInfo()[2]);
                                    betCreation.setAction("bda.hslu.ch.betchain.BlockChainFunctions.ContractCreationIntentService");

                                    activity.sendBroadcast(betCreation);
                                    activity.startService(betCreation);

                                    activity.resetBetCreationInfo();

                                }else{
                                   Toast.makeText(activity, "Your account has no private key set! The app needs this information in order to deploy a contract onto the blockchain!" , Toast.LENGTH_SHORT).show();
                               }
                           }else{
                               Toast.makeText(activity, "Your account needs to be connected to an ethereum account in order to create a bet!" , Toast.LENGTH_SHORT).show();

                           }

                           //Change Fragment to myBets after succeffull deplyoment. (Display loading Screen for the duration)
                           //activity.changeFragment(new MyBetsFragment());

                       }else{
                           Toast.makeText(activity, inputError , Toast.LENGTH_SHORT).show();
                       }
                   }catch(Exception e) {
                       Toast.makeText(activity, "Entry needs to be a valid value" , Toast.LENGTH_SHORT).show();
                   }


                               /*
                               BlockChainFunctions smartContract = new BlockChainFunctions() {
                                   @Override
                                   public void onSuccess(Object result) {
                                       Toast.makeText(activity, "Your Bet has been successfully created!", Toast.LENGTH_SHORT).show();
                                   }

                                   @Override
                                   public void onFailure(Object result) {
                                       Exception exec = (Exception) result;
                                       Toast.makeText(activity, exec.getMessage(), Toast.LENGTH_SHORT).show();
                                       exec.printStackTrace();
                                   }
                               };

                               smartContract.execute("createSmartContract", betConditions.getText().toString(), betEntryFees.getText().toString(), participantList);
                                */
                               //System.out.println(contract.getContractAddress());



               }
           });

        ListView betParticipantListView = (ListView) rootView.findViewById(R.id.betParticipantList);
        CustomAdapterParticipantInfo adapter = new CustomAdapterParticipantInfo (activity, participantList);
        betParticipantListView.setAdapter(adapter);

        return rootView;
    }

    private String[] getUserInfo(){
        String[] returnString;
        MainActivity activity = (MainActivity) getActivity();
        SQLWrapper db = DBSessionSingleton.getInstance().getDbUtil();
        returnString = db.getLoggedInUserInfo();
        return returnString;
    }

}