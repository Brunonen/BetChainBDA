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

import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

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


public class CreateBetStep4Fragment extends Fragment {

    private View rootView;
    private static BigInteger GAS_PRICE = new BigInteger("100000");
    private static BigInteger GAS_LIMIT = new BigInteger("4000000");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_create_bet_step4, container, false);

        final MainActivity activity = (MainActivity) getActivity();

        TextView betTitle = (TextView) rootView.findViewById(R.id.inputBetTitleConfirmation);
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

                   if(!betConditions.getText().equals("")){
                       try {
                           if (Float.valueOf(betEntryFees.getText().toString()) >= 0) {

                               Intent betCreation = new Intent(activity, ContractCreationIntentService.class);
                               betCreation.putExtra("betConditions", betConditions.getText().toString());
                               betCreation.putExtra("betEntryFee", betEntryFees.getText().toString());
                               betCreation.putExtra("participants" , (Serializable) participantList);
                               betCreation.setAction("bda.hslu.ch.betchain.BlockChainFunctions.ContractCreationIntentService");
                               activity.sendBroadcast(betCreation);
                               activity.startService(betCreation);

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
                           } else {
                               Toast.makeText(activity, "Entry Fee can not be less than 0!", Toast.LENGTH_SHORT).show();

                           }
                       }catch(Exception ex){
                           System.out.println(ex.getMessage());
                       }
                   }else{
                       Toast.makeText(activity ,"Please enter Conditions for your bet!",  Toast.LENGTH_SHORT).show();
                   }


               }
           });

        ListView betParticipantListView = (ListView) rootView.findViewById(R.id.betParticipantList);
        CustomAdapterParticipantInfo adapter = new CustomAdapterParticipantInfo (activity, participantList);
        betParticipantListView.setAdapter(adapter);

        return rootView;
    }

    public static byte[] stringToBytes32(String string) {
        byte[] byteValue = string.getBytes();
        byte[] byteValueLen32 = new byte[32];
        System.arraycopy(byteValue, 0, byteValueLen32, 0, byteValue.length);
        return byteValueLen32;
    }
}