package bda.hslu.ch.betchain.BlockChainFunctions;

import android.os.AsyncTask;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import bda.hslu.ch.betchain.BetChainBetContract;
import bda.hslu.ch.betchain.DTO.Participant;
import bda.hslu.ch.betchain.WebRequestException;

/**
 * Created by ssj10 on 28/03/2018.
 */

public abstract class BlockChainFunctions extends AsyncTask<Object, Void, Object>  {

    private static final String BLOCKCHAIN_URL = "http://10.0.2.2:7545";
    private static BigInteger GAS_PRICE = new BigInteger("100000");
    private static BigInteger GAS_LIMIT = new BigInteger("4000000");

    private static final String BRUNO_P_KEY = "c87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3";
    //private static final String BRUNO_P_KEY = "cdfccc4c39b60a7b591eac331dc9860a5ba643ef5d7e09cdfb86a91e7c14464c";

    public static Exception mException;
    public abstract void onSuccess(Object result);
    public abstract void onFailure(Object result);

    public BlockChainFunctions(){

    }

    @Override
    protected Object doInBackground(Object... params) {
        try {
            String method = (String) params[0];
            switch (method) {
                    //return getUserFriendList();


                case "getContractInfo":
                    getSmartContractInfo((String) params[1]);
                    break;

            }
        }catch(Exception e){
            mException = e;
        }
        return null;
    }

    private static void getSmartContractInfo(String contractAddress) throws Exception{
        Web3j web3 = Web3jFactory.build(new HttpService(BLOCKCHAIN_URL));  // defaults to http://localhost:8545/
        Credentials credentials = Credentials.create(BRUNO_P_KEY);


        BetChainBetContract contract = BetChainBetContract.load(
                contractAddress, web3, credentials, GAS_PRICE, GAS_LIMIT);

        System.out.println(contract.getBetPrizePool().send());
        System.out.println(contract.getBetEntryFee().send());
        System.out.println(contract.getBetConditions().send());
        int numberOfParts = contract.getNumberOfParticipants().send().intValue();
        System.out.println(numberOfParts);

        for(int i = 0; i < numberOfParts; i++){
            System.out.println(contract.getParticipantInfo(BigInteger.valueOf(i)).send());
        }


    }



    private static byte[] stringToBytes32(String string) {
        byte[] byteValue = string.getBytes();
        byte[] byteValueLen32 = new byte[32];
        System.arraycopy(byteValue, 0, byteValueLen32, 0, byteValue.length);
        return byteValueLen32;
    }

    protected void onPostExecute(Object result) {
        if (mException == null) {
            onSuccess(result);
        } else {
            onFailure(mException);
        }

    }
}
