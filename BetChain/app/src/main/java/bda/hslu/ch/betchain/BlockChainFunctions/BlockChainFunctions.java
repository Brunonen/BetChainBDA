package bda.hslu.ch.betchain.BlockChainFunctions;

import android.os.AsyncTask;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import bda.hslu.ch.betchain.BetChainBetContract;
import bda.hslu.ch.betchain.DTO.Bet;
import bda.hslu.ch.betchain.DTO.BetRole;
import bda.hslu.ch.betchain.DTO.BetState;
import bda.hslu.ch.betchain.DTO.Participant;
import bda.hslu.ch.betchain.WebFunctions.UserFunctions;
import bda.hslu.ch.betchain.WebRequestException;

/**
 * Created by ssj10 on 28/03/2018.
 */

public class BlockChainFunctions {

    private static final String BLOCKCHAIN_URL = "http://10.0.2.2:7545";
    private static BigInteger GAS_PRICE = new BigInteger("100000");
    private static BigInteger GAS_LIMIT = new BigInteger("4000000");

    public static Bet getBetInfoFromBlockchain(Bet betInfoWeb, String user_P_Key) throws Exception{
        try {
            Web3j web3 = Web3jFactory.build(new HttpService(BLOCKCHAIN_URL));  // defaults to http://localhost:8545/

            //REPLACE WITH CREDENTIALS FROM DATABASE!
            Credentials credentials = Credentials.create(user_P_Key);



            BetChainBetContract contract = BetChainBetContract.load(betInfoWeb.getBetAddress(), web3, credentials, GAS_PRICE, GAS_LIMIT);

            betInfoWeb.setBetConditions(new String(contract.getBetConditions().send(), "UTF-8"));
            betInfoWeb.setBetEntryFee(Convert.fromWei(Float.valueOf(contract.getBetEntryFee().send().floatValue()).toString(), Convert.Unit.ETHER).floatValue());
            int numberOfParticipants = contract.getNumberOfParticipants().send().intValue();
            List<Participant> betParticipants = new ArrayList<Participant>();
            for(int i = 0; i < numberOfParticipants; i++){
                Tuple4<String, Boolean, Boolean, BigInteger> partInfo = contract.getParticipantInfo(BigInteger.valueOf(i)).send();
                Participant tmpPart = new Participant();
                tmpPart.setAddress(partInfo.getValue1());
                tmpPart.setBetAccept(partInfo.getValue2());
                tmpPart.setBetVoted(partInfo.getValue3());
                tmpPart.setBetRole(BetRole.valueOfInt(partInfo.getValue4().intValue()));

                try {
                    tmpPart.setUsername(UserFunctions.getUserByQR(tmpPart.getAddress()).getUsername());
                }catch(WebRequestException e){
                    tmpPart.setUsername(partInfo.getValue1());
                }

                betParticipants.add(tmpPart);
            }

            betInfoWeb.setParticipants(betParticipants);

            return betInfoWeb;

        }catch(Exception e){
            e.printStackTrace();
            throw new Exception("Error while loading Bet information from Blockchain. Please try again later");
        }

    }

    public static Bet getContractMetaInfoFromBlockchain(Bet betInfo, String user_P_Key){
        try {
            Web3j web3 = Web3jFactory.build(new HttpService(BLOCKCHAIN_URL));  // defaults to http://localhost:8545/

            //REPLACE WITH CREDENTIALS FROM DATABASE!
            Credentials credentials = Credentials.create(user_P_Key);

            BetChainBetContract contract = BetChainBetContract.load(betInfo.getBetAddress(), web3, credentials, GAS_PRICE, GAS_LIMIT);

            BigInteger prizePool = contract.getBetPrizePool().send();
            BigInteger betState = contract.getBetState().send();

            BigDecimal eth = Convert.fromWei(Float.valueOf(prizePool.floatValue()).toString(), Convert.Unit.ETHER);
            betInfo.setBetPrizePool(eth.floatValue());
            betInfo.setBetState(BetState.valueOfInt(betState.intValue()));

            return betInfo;
        }catch(Exception e){
            return betInfo;
        }
    }



    private static byte[] stringToBytes32(String string) {
        byte[] byteValue = string.getBytes();
        byte[] byteValueLen32 = new byte[32];
        System.arraycopy(byteValue, 0, byteValueLen32, 0, byteValue.length);
        return byteValueLen32;
    }


}
