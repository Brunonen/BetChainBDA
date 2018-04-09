package bda.hslu.ch.betchain.BlockChainFunctions;

import android.app.WallpaperManager;

import org.web3j.abi.datatypes.Uint;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import bda.hslu.ch.betchain.Database.SQLWrapper;
import bda.hslu.ch.betchain.MainActivity;

import static java.lang.System.err;

/**
 * Created by Kay on 26/03/2018.
 */

public class AccountBalance {
    private String testnet = "https://ropsten.infura.io/";
    private String walletAddress = "0x6F52912815A67C51BDFdB851ad34F46C7FF4Af8E";
    private BigDecimal eth;

    public AccountBalance() {


    }

    public BigDecimal getAccountBalance(){
        String[] userInfos;
        SQLWrapper db = new SQLWrapper();
        userInfo = db.getLoggedInUserInfo();
        Web3j web3 = Web3jFactory.build(new HttpService(testnet));
        try {
            EthGetBalance ethGetBalance = web3.ethGetBalance(walletAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
            BigInteger wei = ethGetBalance.getBalance();
            eth = Convert.fromWei(wei.toString(), Convert.Unit.ETHER);

        }catch (ExecutionException e){


        }catch (InterruptedException ex){

        }

        return eth;

    }

    public void sendEtherToAccount(){

        Web3j web3 = Web3jFactory.build(new HttpService(testnet));

        Credentials credentials = Credentials.create("b633809e1974f424ffc2888a792c97d9b4ea3070bbfce3e219a7e510df3f7aa1");

        try {
            TransactionReceipt transactionReceipt = Transfer.sendFunds(
                    web3, credentials, "0x33Cb1dfB36d0949127EEB70E0792745154e0f01e",
                    BigDecimal.valueOf(0.1), Convert.Unit.ETHER)
                    .send();

            }catch (Exception e){

        }

/*
        transactionObject.setFrom(from);
        transactionObject.setTo(to);
        transactionObject.setValue(Convert.toWei("1", Convert.Unit.ETHER).toString());
        transactionObject.setGas("21000");
        web3.ethSendTransaction(org.web3j.protocol.core.methods.request.Transaction.)
        web3.ethSendTransaction(transactionObject.);*/

    }
}
