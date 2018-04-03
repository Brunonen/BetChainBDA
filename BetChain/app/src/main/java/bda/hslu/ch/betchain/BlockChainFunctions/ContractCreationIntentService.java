package bda.hslu.ch.betchain.BlockChainFunctions;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import bda.hslu.ch.betchain.BetChainBetContract;
import bda.hslu.ch.betchain.DTO.BetRole;
import bda.hslu.ch.betchain.DTO.Participant;

/**
 * Created by ssj10 on 29/03/2018.
 */

public class ContractCreationIntentService extends IntentService {

    private static final String BLOCKCHAIN_URL = "http://10.0.2.2:7545";
    //private static final String BLOCKCHAIN_URL = "https://ropsten.infura.io/";
    private static final String BRUNO_P_KEY = "c87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3";
    //private static final String BRUNO_P_KEY = "cdfccc4c39b60a7b591eac331dc9860a5ba643ef5d7e09cdfb86a91e7c14464c";
    private static BigInteger GAS_PRICE = new BigInteger("250000");
    private static BigInteger GAS_LIMIT = new BigInteger("5000000");

    public ContractCreationIntentService() {
        super("ContractCreationIntent");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent){
        String betEntryFees = intent.getExtras().getString("betEntryFee");
        String betConditions = intent.getExtras().getString("betConditions");
        List<Participant> participants = (List<Participant>) intent.getExtras().getSerializable("participants");
        List<String> participantAddresses = new ArrayList<String>();
        List<BigInteger> particpantRoles = new ArrayList<BigInteger>();

        for(Participant p : participants){
            if(p.getBetRole() != BetRole.OWNER){
                participantAddresses.add(p.getAddress());
                System.out.println(p.getAddress());
                particpantRoles.add(BigInteger.valueOf(p.getBetRole().ordinal()));
            }
        }

        BigDecimal eth = Convert.toWei(Float.valueOf(betEntryFees).toString(), Convert.Unit.ETHER);
        Web3j web3 = Web3jFactory.build(new HttpService(BLOCKCHAIN_URL));  // defaults to http://localhost:8545/
        Credentials credentials = Credentials.create(BRUNO_P_KEY);
        System.out.println("Starting Contract creation");
        try {
            BetChainBetContract contract = BetChainBetContract.deploy(
                    web3, credentials,
                    GAS_PRICE, GAS_LIMIT, eth.toBigInteger(),
                    stringToBytes32(betConditions), eth.toBigInteger(), participantAddresses, particpantRoles).send();  // constructor params

            System.out.println(contract.getContractAddress());


        }catch(Exception ex){
            System.out.println(ex.getMessage() );
            ex.printStackTrace();
        }
    }

    private static byte[] stringToBytes32(String string) {
        byte[] byteValue = string.getBytes();
        byte[] byteValueLen32 = new byte[32];
        System.arraycopy(byteValue, 0, byteValueLen32, 0, byteValue.length);
        return byteValueLen32;
    }
}
