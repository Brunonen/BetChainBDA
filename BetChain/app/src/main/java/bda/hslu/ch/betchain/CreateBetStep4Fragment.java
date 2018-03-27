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

import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;

import bda.hslu.ch.betchain.Adapters.CustomAdapterParticipantInfo;
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
                               BigDecimal eth = Convert.toWei(Float.valueOf(betEntryFees.getText().toString()).toString(), Convert.Unit.ETHER);
                               Web3j web3 = Web3jFactory.build(new HttpService("http://10.0.2.2:7545"));  // defaults to http://localhost:8545/
                               Credentials credentials = Credentials.create("c87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3");

                               BetChainBetContract contract = BetChainBetContract.deploy(
                                       web3, credentials,
                                       GAS_PRICE, GAS_LIMIT,
                                       stringToBytes32(betConditions.getText().toString()), eth.toBigInteger()).send();  // constructor params

                               System.out.println(contract.getContractAddress());
                               contract.addBetOpposer("0xf17f52151EbEF6C7334FAD080c5704D77216b732").send();
                               contract.acceptBet(Convert.toWei(Float.valueOf(betEntryFees.getText().toString()).toString(), Convert.Unit.ETHER).toBigInteger()).send();
                               BigInteger numberOFParticipants = contract.getNumberOfParticipants().send();
                               System.out.println(numberOFParticipants);
                               for(int i = 0; i < numberOFParticipants.intValue(); i++){
                                   System.out.println(contract.getParticipantInfo(BigInteger.valueOf(i)).send());
                               }
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