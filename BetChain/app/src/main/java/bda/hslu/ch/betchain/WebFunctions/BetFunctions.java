package bda.hslu.ch.betchain.WebFunctions;

import android.os.AsyncTask;
import android.provider.Telephony;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.Tuple;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import bda.hslu.ch.betchain.BetChainBetContract;
import bda.hslu.ch.betchain.BlockChainFunctions.BlockChainFunctions;
import bda.hslu.ch.betchain.CallAPI;
import bda.hslu.ch.betchain.DTO.Bet;
import bda.hslu.ch.betchain.DTO.BetRole;
import bda.hslu.ch.betchain.DTO.BetState;
import bda.hslu.ch.betchain.DTO.Participant;
import bda.hslu.ch.betchain.R;
import bda.hslu.ch.betchain.WebRequestException;

/**
 * Created by Bruno Fischlin on 15/03/2018.
 */

public abstract class BetFunctions extends AsyncTask<String, Void, Object> {

    private static final String SERVER_URL = "http://blockchaincontracts.enterpriselab.ch/index.php";
    private static final String API_KEY = "sdkajdkaj2";

    //private static final String BLOCKCHAIN_URL = "http://10.0.2.2:7545";
    private static final String BLOCKCHAIN_URL = "https://ropsten.infura.io/";

    private static BigInteger GAS_PRICE = new BigInteger("320000");
    private static BigInteger GAS_LIMIT = new BigInteger("4100000");

    public static Exception mException;
    public abstract void onSuccess(Object result);
    public abstract void onFailure(Object result);

    public BetFunctions(){

    }

    /***
     * Method to create a Bet on the Database of the Web-Server.
     * @param betTitle          : Title of the Bet
     * @param transactionHash   : Transaction Hash on the Blockchain
     * @param participants      : List with Participants which should be in the bet
     * @return                  : Returns the ID of the Bet which was added. (Needed for Update purposes)
     * @throws WebRequestException  : If something goes wrong it will throw a WebRequest Exception with the message of the server.
     */
    public static int createBet(String betTitle, String transactionHash, List<Participant> participants) throws WebRequestException {

        try {
            JSONObject requestParams = new JSONObject();
            requestParams.put("method", "createBet");
            requestParams.put("betTitle", betTitle);
            requestParams.put("transactionHash", transactionHash);

            JSONObject participantsJSON = new JSONObject();

            //Parse Particpants into JSON Object with username as key
            for(Participant p : participants){
                participantsJSON.put(p.getUsername(), p.getAddress());
            }

            requestParams.put("participants", participantsJSON);

            //System.out.println(response);

            JSONObject jsonResponse = new JSONObject(CallAPI.makePOSTRequestToServer(SERVER_URL, requestParams.toString()));

            System.out.println(jsonResponse);

            if (jsonResponse.getInt("is_error") == 0) {
                return jsonResponse.getInt("data");
            }else{
                throw new WebRequestException(jsonResponse.getString("message"));
            }

        }catch(JSONException exec){
            throw new WebRequestException(exec.getMessage());
        }

    }

    public static boolean updateBetContractAddress(int betID, String contractAddress) throws WebRequestException {

        try {
            JSONObject requestParams = new JSONObject();
            requestParams.put("method", "updateBetContractAddress");
            requestParams.put("betID", betID);
            requestParams.put("contractAddress", contractAddress);

            //System.out.println(response);

            JSONObject jsonResponse = new JSONObject(CallAPI.makePOSTRequestToServer(SERVER_URL, requestParams.toString()));

            if (jsonResponse.getInt("is_error") == 0) {
                return true;
            }else{
                throw new WebRequestException(jsonResponse.getString("message"));
            }

        }catch(JSONException exec){
            throw new WebRequestException(exec.getMessage());
        }

    }

    public static List<Bet> getUserBets() throws WebRequestException {
        List<Bet> betList = new ArrayList<Bet>();

        betList = getUserBetsFromWebServer();

        try {
            Web3j web3 = Web3jFactory.build(new HttpService(BLOCKCHAIN_URL));  // defaults to http://localhost:8545/

            for (int i = 0; i < betList.size(); i++) {
                //Check if a Contract address has been added to the Database yet. If not try to synchronize!

                if(betList.get(i).getBetAddress().length() > 0) {
                    //GEt Meta info from Blockchain
                    betList.set(i, BlockChainFunctions.getContractMetaInfoFromBlockchain(betList.get(i)));
                }else{
                    //Synchronize Bet information from Blockchain to Database.
                    EthGetTransactionReceipt receipt = web3.ethGetTransactionReceipt(betList.get(i).getBetTransactionHash()).send();

                    if(receipt.getTransactionReceipt() != null) {
                        String betAddress = receipt.getTransactionReceipt().getContractAddress();

                        //If the contract has been deployed now and we got an address, update the address on the Database and get the Meta Info from the Blockchain
                        if (!betAddress.equals("")) {
                            updateBetContractAddress(betList.get(i).getBetID(), betAddress);
                            betList.get(i).setBetAddress(betAddress);
                            betList.set(i, BlockChainFunctions.getContractMetaInfoFromBlockchain(betList.get(i)));
                        } else {
                            betList.get(i).setBetState(BetState.NOTDEPLOYED);
                        }
                    }
                }

            }
        }catch(Exception  e){
            e.printStackTrace();
            return betList;
        }

        return betList;
    }

    public static List<Bet> getUserBetsFromWebServer() throws WebRequestException{
        List<Bet> betList = new ArrayList<Bet>();

        try {
            JSONObject requestParams = new JSONObject();
            requestParams.put("method", "getUserBets");
            //ADD FILTER PARAMETER ONCE IMPLEMENTED

            //System.out.println(response);

            JSONObject jsonResponse = new JSONObject(CallAPI.makePOSTRequestToServer(SERVER_URL, requestParams.toString()));

            if (jsonResponse.getInt("is_error") == 0) {
                JSONArray betJSONS = jsonResponse.getJSONArray("data");
                return parseDataResponseToBet(betJSONS);
            }else{
                throw new WebRequestException(jsonResponse.getString("message"));
            }

        }catch(JSONException exec){
            throw new WebRequestException(exec.getMessage());
        }


    }

    private static List<Bet> parseDataResponseToBet(JSONArray data) {
        List<Bet> betList = new ArrayList<Bet>();

        try {
            for (int i = 0; i < data.length(); i++) {

                JSONObject betO = data.getJSONObject(i);
                String betAddress = betO.getString("contractAddress");

                //Somehow the JSON lietereally writes null into the string
                if(betAddress.equals("null")){
                    betAddress = "";
                }

                String betTitle = betO.getString("title");
                String betTxHash = betO.getString("txHash");
                int betID = betO.getInt("id");

                //BetState betState, String betTitle, String betConditions, float betEntryFee, float betPrizePool, List<Participant> participants, String betAddress, boolean betSuccessful
                Bet tmpBet = new Bet(BetState.NOTDEPLOYED, betTitle, "", 0.0f, 0.0f, null, betAddress, false);
                tmpBet.setBetTransactionHash(betTxHash);
                tmpBet.setBetID(betID);
                betList.add(tmpBet);


            }

            return betList;
        } catch(JSONException exec) {
            return betList;
        }
    }

    public static String checkIfBetInputsAreValid(String betTitle, String betConditions, List<Participant> participants, float entryFee){
        if(betTitle.length() < 8) return "Bet Title can not be less than 8 characters long";
        if(betTitle.length() > 45) return "Bet Title can not be longer than 45 characters!";
        if(betConditions.length() < 8) return "Bet Conditions must be at least 8 characters long";
        if(betConditions.length() > 200) return "Bet conditions can not be longer than 200 characters";

        for(Participant p : participants){
            if(p.getAddress() == null) return "Participant error";
            if(p.getAddress() == "") return "Participant error";
            if(p.getAddress().length() != 42) return "Participant error";
            if(p.getBetRole() == null) return "Participant error";
            if(p.getUsername() == null) return "Participant error";
            if(p.getUsername() == "") return "Participant error";
        }

        if(entryFee < 0) return "Bet Entry fee must be bigger or equal to 0";

        return "";
    }

    @Override
    protected Object doInBackground(String... params) {
        mException = null;
        try {
            switch (params[0]) {
                case "getUserBets":
                    return getUserBets();
                default:
                    return null;
            }
        }catch(WebRequestException e){
            mException = e;
        }
        return null;
    }


    protected void onPostExecute(Object result) {
        if (mException == null) {
            onSuccess(result);
        } else {
            onFailure(mException);
        }

    }
}
