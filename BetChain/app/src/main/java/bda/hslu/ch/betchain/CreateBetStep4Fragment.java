package bda.hslu.ch.betchain;

import android.app.AlertDialog;
import android.app.IntentService;
import android.content.DialogInterface;
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

import org.web3j.utils.Convert;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
        betEntryFees.setText(String.valueOf(new BigDecimal(String.valueOf(activity.getBetCreationBetEntryFee()))));
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

                                   final BigDecimal est = getEstimatedContractCost(participantList, betConditions.getText().toString(), Float.valueOf(betEntryFee));

                                   AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                                   builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                       public void onClick(DialogInterface dialog, int id) {

                                           try {
                                               String transactionHash = BlockChainFunctions.createContract(betConditions.getText().toString(), new BigDecimal(String.valueOf(Float.valueOf(betEntryFees.getText().toString()))).floatValue(), participantList);
                                               int betID = BetFunctions.createBet(betTitle.getText().toString(), transactionHash, participantList);
                                               Intent betCreationIntent = new Intent(activity, ContractCreationIntentService.class);
                                               betCreationIntent.putExtra("transactionHash", transactionHash);
                                               betCreationIntent.putExtra("betID", betID);

                                               activity.startService(betCreationIntent);
                                               activity.resetBetCreationInfo();

                                               Toast.makeText(activity, "Request submitted! Please keep in mind that changes might take a few minutes to take effect on the Blockchain!" , Toast.LENGTH_LONG).show();
                                               activity.changeFragment(new MyBetsFragment());

                                           } catch (WebRequestException e) {
                                               e.printStackTrace();
                                               Toast.makeText(activity, e.getMessage() , Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                   });
                                   builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                       public void onClick(DialogInterface dialog, int id) {
                                           //Do nothing
                                       }
                                   });

                                   builder.setMessage("Releasing a contract onto the Blockchain will result in costs on your behalf. \n\nEstimated: " + est.toString() + " Eth\n\nAre you sure you want to continue? ")
                                           .setTitle("Notice");


                                   AlertDialog dialog = builder.create();
                                   dialog.show();



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
                       e.printStackTrace();
                       Toast.makeText(activity, "Entry needs to be a valid value" , Toast.LENGTH_SHORT).show();
                   }

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

    //Calculate Estimated Ether Cost
    private BigDecimal getEstimatedContractCost(List<Participant> participantList, String betConditions, float betEntryFees) throws InterruptedException, ExecutionException, IOException {
        List<String> participantAddresses = new ArrayList<String>();
        List<BigInteger> particpantRoles = new ArrayList<BigInteger>();

        //The contract needs two arrays, one with the addresses and one with the roles of the Participants, since a contract
        //can not possibly know our Class participant.
        for(Participant p : participantList){
            if(p.getBetRole() != BetRole.OWNER){
                participantAddresses.add(p.getAddress());
                System.out.println(p.getAddress());
                particpantRoles.add(BigInteger.valueOf(p.getBetRole().ordinal()));
            }
        }

        BigDecimal eth = Convert.toWei(String.valueOf(new BigDecimal(String.valueOf(betEntryFees)).floatValue()), Convert.Unit.ETHER);

        BigInteger estimatedGas = BlockChainFunctions.getContractEstimate(eth.toBigInteger(), betConditions, eth.toBigInteger(), participantAddresses, particpantRoles).multiply(BlockChainFunctions.getGasPrice());

        return Convert.fromWei(String.valueOf(estimatedGas), Convert.Unit.ETHER);
    }

}