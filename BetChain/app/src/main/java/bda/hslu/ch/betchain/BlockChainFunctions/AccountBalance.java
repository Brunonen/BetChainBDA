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

import bda.hslu.ch.betchain.Database.DBSessionSingleton;
import bda.hslu.ch.betchain.Database.SQLWrapper;
import bda.hslu.ch.betchain.MainActivity;

import static java.lang.System.err;

/**
 * Created by Kay on 26/03/2018.
 */

public class AccountBalance {
    private String testnet = "https://ropsten.infura.io/";
    private BigDecimal eth;

    public AccountBalance() {


    }

    public BigDecimal getAccountBalance() throws Exception {
        Web3j web3 = Web3jFactory.build(new HttpService(testnet));

        EthGetBalance ethGetBalance = web3.ethGetBalance(DBSessionSingleton.getInstance().getDbUtil().getLoggedInUserInfo()[3], DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger wei = ethGetBalance.getBalance();
        eth = Convert.fromWei(wei.toString(), Convert.Unit.ETHER);

        return eth;

    }
}
