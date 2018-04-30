package bda.hslu.ch.betchain.BlockChainFunctions;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

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
 * Created by Bruno Fischlin on 29/03/2018.
 */

public class ContractCreationIntentService extends IntentService {

    //private static final String BLOCKCHAIN_URL = "http://10.0.2.2:7545";
    private static final String BLOCKCHAIN_URL = "https://ropsten.infura.io/";
    private static BigInteger GAS_PRICE = new BigInteger("200000");
    private static BigInteger GAS_LIMIT = new BigInteger("50000000");
    private static int POLL_TIME = 5000; //5 seconds
    private static int MAXIMUM_CREATION_TIMEOUT = 600000; //10 Minutes
    private NotificationManager notificationManager;

    public ContractCreationIntentService() {
        super("ContractCreationIntent");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent){
        String transactionHash = intent.getExtras().getString("transactionHash");
        int betID = intent.getExtras().getInt("betID");

        try {
                Web3j web3 = Web3jFactory.build(new HttpService(BLOCKCHAIN_URL));
                int passedTime = 0;

                //Wait for Bet to be deployed in order to get the Contract address! (Wait a maximum of 10 Minutes!)
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
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction("bda.hslu.ch.betchain.BlockChainFunctions.ContractCreationIntentService");
                        sendBroadcast(broadcastIntent);
                    }
                }


        }catch(Exception ex){
            System.out.println(ex.getMessage() );
            ex.printStackTrace();
        }
    }

}
