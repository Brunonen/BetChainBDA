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
                case "createSmartContract":
                    createSmartContract((String) params[1], (String) params[2], (List<Participant>) params[3]);
                    break;

                    //return getUserFriendList();


                case "getContractInfo":
                    getSmartContractInfo((String) params[1]);
                    break;



                case "removeFriend":
                    //return removeFriend(params[1]);
            }
        }catch(Exception e){
            mException = e;
        }
        return null;
    }

    private static void getSmartContractInfo(String contractAddress) throws Exception{
        Web3j web3 = Web3jFactory.build(new HttpService(BLOCKCHAIN_URL));  // defaults to http://localhost:8545/
        Credentials credentials = Credentials.create("c87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3");


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

    private static boolean createSmartContract(String betConditions, String betEntryFees, List<Participant> participants) throws Exception {
        BigDecimal eth = Convert.toWei(Float.valueOf(betEntryFees).toString(), Convert.Unit.ETHER);
        Web3j web3 = Web3jFactory.build(new HttpService(BLOCKCHAIN_URL));  // defaults to http://localhost:8545/
        Credentials credentials = Credentials.create("c87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3");

        BetChainBetContract contract = BetChainBetContract.deploy(
                web3, credentials,
                GAS_PRICE, GAS_LIMIT,
                stringToBytes32(betConditions), eth.toBigInteger()).send();  // constructor params

        System.out.println(contract.getContractAddress());
        contract.acceptBet(Convert.toWei(Float.valueOf(betEntryFees).toString(), Convert.Unit.ETHER).toBigInteger()).send();

        for(Participant p : participants){
            System.out.println(p.getUsername() + " Address: " + p.getAddress());
            switch(p.getBetRole()){
                case OWNER:     break;
                case SUPPORTER: contract.addBetSupporter(p.getAddress().toString()).send();
                                System.out.println("Adding Supporter");
                                break;
                case OPPOSER: contract.addBetOpposer(p.getAddress().toString()).send();
                                System.out.println("Adding Oppser");
                                break;
                case NOTAR: contract.addBetNotar(p.getAddress()).send();
                                System.out.println("Adding Notar");
                                break;
            }
        }

        return true;
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
