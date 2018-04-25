package bda.hslu.ch.betchain.BlockChainFunctions;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import bda.hslu.ch.betchain.BetChainBetContract;
import bda.hslu.ch.betchain.DTO.Bet;
import bda.hslu.ch.betchain.DTO.BetRole;
import bda.hslu.ch.betchain.DTO.BetState;
import bda.hslu.ch.betchain.DTO.Participant;
import bda.hslu.ch.betchain.Database.DBSessionSingleton;
import bda.hslu.ch.betchain.Database.SQLWrapper;
import bda.hslu.ch.betchain.MainActivity;
import bda.hslu.ch.betchain.WebFunctions.UserFunctions;
import bda.hslu.ch.betchain.WebRequestException;

/**
 * Created by Bruno Fischlin on 28/03/2018.
 */

public class BlockChainFunctions {

    private static final String BLOCKCHAIN_URL = "http://10.0.2.2:7545";
    private static BigInteger GAS_PRICE = new BigInteger("2000000000");
    private static BigInteger GAS_LIMIT = new BigInteger("4000000");
    private static Web3j web3 = Web3jFactory.build(new HttpService(BLOCKCHAIN_URL));

    public static Bet getBetInfoFromBlockchain(Bet betInfoWeb) throws Exception{
        try {

            Credentials credentials = Credentials.create(getUserInfo()[2]);

            BetChainBetContract contract = BetChainBetContract.load(betInfoWeb.getBetAddress(), web3, credentials, GAS_PRICE, GAS_LIMIT);

            betInfoWeb.setBetConditions(contract.getBetConditions().send());
            betInfoWeb.setBetEntryFee(Convert.fromWei(Float.valueOf(contract.getBetEntryFee().send().floatValue()).toString(), Convert.Unit.ETHER).floatValue());
            betInfoWeb.setBetSuccessful(contract.isBetSuccessfull().send());
            int numberOfParticipants = contract.getNumberOfParticipants().send().intValue();
            List<Participant> betParticipants = new ArrayList<Participant>();
            for(int i = 0; i < numberOfParticipants; i++){
                Tuple5<String, Boolean, Boolean, Boolean, BigInteger> partInfo = contract.getParticipantInfo(BigInteger.valueOf(i)).send();
                Participant tmpPart = new Participant();
                tmpPart.setAddress(partInfo.getValue1());
                tmpPart.setBetAccept(partInfo.getValue2());
                tmpPart.setBetVoted(partInfo.getValue3());
                tmpPart.setAbortVoted(partInfo.getValue4());
                tmpPart.setBetRole(BetRole.valueOfInt(partInfo.getValue5().intValue()));

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

    public static Bet getContractMetaInfoFromBlockchain(Bet betInfo){
        try {

            //REPLACE WITH CREDENTIALS FROM DATABASE!
            Credentials credentials = Credentials.create(getUserInfo()[2]);

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

    public static String createContract(String betConditions, float betEntryFees, List<Participant> participants) throws WebRequestException{
        List<String> participantAddresses = new ArrayList<String>();
        List<BigInteger> particpantRoles = new ArrayList<BigInteger>();

        //The contract needs two arrays, one with the addresses and one with the roles of the Participants, since a contract
        //can not possibly know our Class participant.
        for(Participant p : participants){
            if(p.getBetRole() != BetRole.OWNER){
                participantAddresses.add(p.getAddress());
                System.out.println(p.getAddress());
                particpantRoles.add(BigInteger.valueOf(p.getBetRole().ordinal()));
            }
        }

        BigDecimal eth = Convert.toWei(String.valueOf(betEntryFees).toString(), Convert.Unit.ETHER);
        Web3j web3 = Web3jFactory.build(new HttpService(BLOCKCHAIN_URL));  // defaults to http://localhost:8545/

        //REPLACE WITH CREDENTIALS FROM DATABASE!
        Credentials credentials = Credentials.create(getUserInfo()[2]);

        try {
            RawTransaction raw = getContractTransaction( web3, credentials,
                    GAS_PRICE, GAS_LIMIT, eth.toBigInteger(),
                    betConditions, eth.toBigInteger(), participantAddresses, particpantRoles);

            byte[] signedMessage = TransactionEncoder.signMessage(raw, credentials);
            String hexValue = Numeric.toHexString(signedMessage);

            EthSendTransaction ethSendTransaction =
                    web3.ethSendRawTransaction(hexValue).sendAsync().get();


            if(ethSendTransaction.getTransactionHash() != null) {
                return ethSendTransaction.getTransactionHash();
            }else{
                throw new Exception("Contract creation rejected");
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new WebRequestException(e.getMessage());
        }

    }

    public static boolean acceptBet(String betAddress, BetRole participantRole, float entryFee)  {
        //REPLACE WITH CREDENTIALS FROM DATABASE!
        Credentials credentials = Credentials.create(getUserInfo()[2]);

        BetChainBetContract contract = BetChainBetContract.load(betAddress, web3, credentials, GAS_PRICE, GAS_LIMIT);
        BigDecimal eth;

        if(participantRole != BetRole.NOTAR) {
            //We use String.valueOF because it Rounds the values, and we also round it when creating the contract.
            //The conctract rejects the Transaction if the Amount is not exact. So to much detail is not viable.
            eth = Convert.toWei(String.valueOf(entryFee).toString(), Convert.Unit.ETHER);
        }else{
            eth = Convert.toWei(String.valueOf(0.0f).toString(), Convert.Unit.ETHER);
        }

        try {
            contract.acceptBet(eth.toBigInteger()).sendAsync();
        }catch(Exception e){
            e.printStackTrace();
        }

        return true;

    }

    public static boolean retreatFromBet(String betAddress)  {

        //REPLACE WITH CREDENTIALS FROM DATABASE!
        Credentials credentials = Credentials.create(getUserInfo()[2]);

        BetChainBetContract contract = BetChainBetContract.load(betAddress, web3, credentials, GAS_PRICE, GAS_LIMIT);

        try {
            contract.retreatFromBet().sendAsync();
        }catch(Exception e){
            e.printStackTrace();
        }

        return true;

    }

    public static boolean startBet(String betAddress)  {

        Credentials credentials = Credentials.create(getUserInfo()[2]);

        BetChainBetContract contract = BetChainBetContract.load(betAddress, web3, credentials, GAS_PRICE, GAS_LIMIT);

        try {
            contract.startBet().sendAsync();
        }catch(Exception e){
            e.printStackTrace();
        }

        return true;

    }

    public static boolean startVoting(String betAddress, boolean wasBetSuccessfull){

        Credentials credentials = Credentials.create(getUserInfo()[2]);

        BetChainBetContract contract = BetChainBetContract.load(betAddress, web3, credentials, GAS_PRICE, GAS_LIMIT);

        try {
            contract.startVote(wasBetSuccessfull).sendAsync();
        }catch(Exception e){
            e.printStackTrace();
        }

        return true;
    }

    public static boolean vote(String betAddress, boolean wasBetSuccessfull){

        Credentials credentials = Credentials.create(getUserInfo()[2]);

        BetChainBetContract contract = BetChainBetContract.load(betAddress, web3, credentials, GAS_PRICE, GAS_LIMIT);

        try {
            contract.vote(wasBetSuccessfull).sendAsync();
        }catch(Exception e){
            e.printStackTrace();
        }

        return true;
    }

    public static boolean abort(String betAddress){

        Credentials credentials = Credentials.create(getUserInfo()[2]);

        BetChainBetContract contract = BetChainBetContract.load(betAddress, web3, credentials, GAS_PRICE, GAS_LIMIT);

        try {
            contract.abortBet().sendAsync();
        }catch(Exception e){
            e.printStackTrace();
        }

        return true;
    }

    public static BigDecimal getAccountBalance() throws Exception {
        Web3j web3 = Web3jFactory.build(new HttpService(BLOCKCHAIN_URL));

        EthGetBalance ethGetBalance = web3.ethGetBalance(getUserInfo()[3], DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger wei = ethGetBalance.getBalance();
        BigDecimal eth = Convert.fromWei(wei.toString(), Convert.Unit.ETHER);

        return eth;

    }

    public String[] createNewEthereumWallet(Context context, String password) {
        File file = context.getFilesDir();
        BigInteger privateKey;
        String[] adresses = new String[2];
        try {
            String walletFileName = WalletUtils.generateLightNewWalletFile(password, file);
            File testFile = new File(file + "/" + walletFileName);
            Credentials credentials = WalletUtils.loadCredentials(password, testFile);
            ECKeyPair ecKeyPair = credentials.getEcKeyPair();
            privateKey = ecKeyPair.getPrivateKey();

            adresses[0] = privateKey.toString(16);
            adresses[1] = credentials.getAddress();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return adresses;
    }


    private static byte[] stringToBytes32(String string) {
        /*byte[] byteValue = string.getBytes();
        byte[] byteValueLen32 = new byte[32];
        System.arraycopy(byteValue, 0, byteValueLen32, 0, byteValue.length);
        return byteValueLen32;*/

        char[] chars = string.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++)
        {
            hex.append(Integer.toHexString((int) chars[i]));
        }

        return Numeric.hexStringToByteArray(hex.toString() + TextUtils.join("", Collections.nCopies(32 - (hex.length()/2), "00")));
    }

    private static String[] getUserInfo(){
        String[] returnString;
        SQLWrapper db = DBSessionSingleton.getInstance().getDbUtil();
        returnString = db.getLoggedInUserInfo();
        return returnString;
    }

    public static RawTransaction getContractTransaction(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, String _betConditions, BigInteger _betEntryFee, List<String> _participantAddresses, List<BigInteger> _participantRoles) throws ExecutionException, InterruptedException {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_betConditions),
                new org.web3j.abi.datatypes.generated.Uint256(_betEntryFee),
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.Utils.typeMap(_participantAddresses, org.web3j.abi.datatypes.Address.class)),
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint8>(
                        org.web3j.abi.Utils.typeMap(_participantRoles, org.web3j.abi.datatypes.generated.Uint8.class))));

        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount( credentials.getAddress()
                , DefaultBlockParameterName.LATEST).sendAsync().get();

        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        try {
            System.out.println("estimate:" + web3.ethEstimateGas(new Transaction(credentials.getAddress(), nonce, gasPrice, gasLimit, null, initialWeiValue, BetChainBetContract.getBinary() + encodedConstructor)).send().getAmountUsed());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return RawTransaction.createContractTransaction(nonce, GAS_PRICE, GAS_LIMIT, initialWeiValue, BetChainBetContract.getBinary() + encodedConstructor );

    }

    public static BigInteger getContractEstimate(BigInteger initialWeiValue, String _betConditions, BigInteger _betEntryFee, List<String> _participantAddresses, List<BigInteger> _participantRoles) throws ExecutionException, InterruptedException, IOException {
        Web3j web3 = Web3jFactory.build(new HttpService(BLOCKCHAIN_URL));  // defaults to http://localhost:8545/

        //REPLACE WITH CREDENTIALS FROM DATABASE!
        Credentials credentials = Credentials.create(getUserInfo()[2]);

        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_betConditions),
                new org.web3j.abi.datatypes.generated.Uint256(_betEntryFee),
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.Utils.typeMap(_participantAddresses, org.web3j.abi.datatypes.Address.class)),
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint8>(
                        org.web3j.abi.Utils.typeMap(_participantRoles, org.web3j.abi.datatypes.generated.Uint8.class))));

        EthGetTransactionCount ethGetTransactionCount = web3.ethGetTransactionCount( credentials.getAddress()
                , DefaultBlockParameterName.LATEST).sendAsync().get();

        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        return web3.ethEstimateGas(new Transaction(credentials.getAddress(), nonce, GAS_PRICE, GAS_LIMIT, null, initialWeiValue, BetChainBetContract.getBinary() + encodedConstructor)).send().getAmountUsed();


    }

    public static BigInteger getGasPrice(){
        return GAS_PRICE;
    }

    public static BigInteger getGasLimit(){
        return GAS_LIMIT;
    }


}
