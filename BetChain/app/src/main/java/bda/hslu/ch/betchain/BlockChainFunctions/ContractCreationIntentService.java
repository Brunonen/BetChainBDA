package bda.hslu.ch.betchain.BlockChainFunctions;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import bda.hslu.ch.betchain.BetChainBetContract;
import bda.hslu.ch.betchain.DTO.BetRole;
import bda.hslu.ch.betchain.DTO.Participant;
import bda.hslu.ch.betchain.Database.SQLWrapper;
import bda.hslu.ch.betchain.MainActivity;
import bda.hslu.ch.betchain.WebFunctions.BetFunctions;

/**
 * Created by ssj10 on 29/03/2018.
 */

public class ContractCreationIntentService extends IntentService {

    private static final String BLOCKCHAIN_URL = "http://10.0.2.2:7545";
    //private static final String BLOCKCHAIN_URL = "https://ropsten.infura.io/";
    private static final String BRUNO_P_KEY = "c87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3";
    //private static final String BRUNO_P_KEY = "cdfccc4c39b60a7b591eac331dc9860a5ba643ef5d7e09cdfb86a91e7c14464c";
    private static BigInteger GAS_PRICE = new BigInteger("320000");
    private static BigInteger GAS_LIMIT = new BigInteger("4100000");
    private static int POLL_TIME = 5000; //5 seconds
    private static int MAXIMUM_CREATION_TIMEOUT = 600000; //10 Minutes

    public ContractCreationIntentService() {
        super("ContractCreationIntent");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent){
        String betEntryFees = intent.getExtras().getString("betEntryFee");
        String betConditions = intent.getExtras().getString("betConditions");
        String betTitle = intent.getExtras().getString("betTitle");
        String user_P_Key = intent.getExtras().getString("pKey");
        List<Participant> participants = (List<Participant>) intent.getExtras().getSerializable("participants");
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

        BigDecimal eth = Convert.toWei(Float.valueOf(betEntryFees).toString(), Convert.Unit.ETHER);
        Web3j web3 = Web3jFactory.build(new HttpService(BLOCKCHAIN_URL));  // defaults to http://localhost:8545/

        //REPLACE WITH CREDENTIALS FROM DATABASE!
        Credentials credentials = Credentials.create(user_P_Key);

        try {

            /*BetChainBetContract contract = BetChainBetContract.deploy(
                    web3, credentials,
                    GAS_PRICE, GAS_LIMIT, eth.toBigInteger(),
                    stringToBytes32(betConditions), eth.toBigInteger(), participantAddresses, particpantRoles).send();  // constructor params
            */
            RawTransaction raw = getContractTransaction( web3, credentials,
                    GAS_PRICE, GAS_LIMIT, eth.toBigInteger(),
                    stringToBytes32(betConditions), eth.toBigInteger(), participantAddresses, particpantRoles);

            byte[] signedMessage = TransactionEncoder.signMessage(raw, credentials);
            String hexValue = Numeric.toHexString(signedMessage);

            EthSendTransaction ethSendTransaction =
                    web3.ethSendRawTransaction(hexValue).sendAsync().get();

            if(ethSendTransaction.getTransactionHash() != null) {
                String transactionHash = ethSendTransaction.getTransactionHash();
                System.out.println(transactionHash);
                int betID = BetFunctions.createBet(betTitle, transactionHash, participants);

                int passedTime = 0;

                //Wait for Bet to be deployed in order to get the Contract address! (Wait a maximum of 10 Minutes!
                EthGetTransactionReceipt receipt = web3.ethGetTransactionReceipt(transactionHash).send();
                System.out.println(receipt.getTransactionReceipt());
                if(receipt.getTransactionReceipt() == null) {
                    System.out.println("in here! ");
                    while (receipt.getTransactionReceipt() == null && passedTime <= MAXIMUM_CREATION_TIMEOUT) {
                        System.out.println("still in here! ");
                        SystemClock.sleep(POLL_TIME);
                        passedTime += POLL_TIME;
                        receipt = web3.ethGetTransactionReceipt(transactionHash).send();
                    }
                }

                //If we got an address in time, we update it on the Database. If not, we try to update it when synchronizing.
                if(passedTime < MAXIMUM_CREATION_TIMEOUT) {
                    receipt = web3.ethGetTransactionReceipt(transactionHash).send();

                    if(receipt.getTransactionReceipt() != null) {
                        String contractAddress = receipt.getTransactionReceipt().getContractAddress();

                        BetFunctions.updateBetContractAddress(betID, contractAddress);
                    }
                }

            }else {
                throw new Exception("Creation was rejected!");
            }

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

    public static RawTransaction getContractTransaction(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, byte[] _betConditions, BigInteger _betEntryFee, List<String> _participantAddresses, List<BigInteger> _participantRoles) throws ExecutionException, InterruptedException {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_betConditions),
                new org.web3j.abi.datatypes.generated.Uint256(_betEntryFee),
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.Utils.typeMap(_participantAddresses, org.web3j.abi.datatypes.Address.class)),
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint8>(
                        org.web3j.abi.Utils.typeMap(_participantRoles, org.web3j.abi.datatypes.generated.Uint8.class))));

        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount( credentials.getAddress()
                , DefaultBlockParameterName.LATEST).sendAsync().get();

        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        return RawTransaction.createContractTransaction(nonce, GAS_PRICE, GAS_LIMIT, initialWeiValue, BetChainBetContract.getBinary() + encodedConstructor );

    }


}
