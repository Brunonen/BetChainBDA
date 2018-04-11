package bda.hslu.ch.betchain;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.3.1.
 */
public class BetChainBetContract extends Contract {
    private static final String BINARY = "6060604052604051620025573803806200255783398101604052808051906020019091908051906020019091908051820191906020018051820191905050600034841415156200004e57600080fd5b600060068190555060a0604051908101604052803373ffffffffffffffffffffffffffffffffffffffff16815260200160011515815260200160001515815260200160001515815260200160006003811115620000a757fe5b815250600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060208201518160000160146101000a81548160ff02191690831515021790555060408201518160000160156101000a81548160ff02191690831515021790555060608201518160000160166101000a81548160ff02191690831515021790555060808201518160000160176101000a81548160ff02191690836003811115620001b557fe5b0217905550905050600e8054806001018281620001d3919062000540565b9160005260206000209001600033909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550506006600081548092919060010191905055506001600581905550600060048190555083600b8190555084600081600019169055508360018190555033600c60016101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000600c60156101000a81548160ff02191690836004811115620002be57fe5b0217905550600060078190555060016008819055506000600981905550600090505b8251811015620005355760a06040519081016040528084838151811015156200030557fe5b9060200190602002015173ffffffffffffffffffffffffffffffffffffffff16815260200160001515815260200160001515815260200160001515815260200183838151811015156200035457fe5b9060200190602002015160038111156200036a57fe5b815250600d600085848151811015156200038057fe5b9060200190602002015173ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060208201518160000160146101000a81548160ff02191690831515021790555060408201518160000160156101000a81548160ff02191690831515021790555060608201518160000160166101000a81548160ff02191690831515021790555060808201518160000160176101000a81548160ff021916908360038111156200049057fe5b0217905550905050600e8054806001018281620004ae919062000540565b916000526020600020900160008584815181101515620004ca57fe5b90602001906020020151909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550506006600081548092919060010191905055508080600101915050620002e0565b505050505062000597565b8154818355818115116200056a578183600052602060002091820191016200056991906200056f565b5b505050565b6200059491905b808211156200059057600081600090555060010162000576565b5090565b90565b611fb080620005a76000396000f300606060405260043610610112576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806309e69ede1461011757806330bfcb5f146101c6578063362f04c0146101e85780633912253e146102115780634b9f5c981461024257806354a322721461027f57806354ffbddb146102ac5780635d98230f1461030f5780637956d375146103405780638cda68d8146103775780638f6deb38146103a457806399823ace146103cd578063b110c73114610404578063b2114ca11461049d578063bf3cff37146104c6578063c4e23a5c146104f3578063c80c28a214610520578063e32a28b014610549578063e8ac03a814610572578063fa6776db146105af575b600080fd5b341561012257600080fd5b61014e600480803573ffffffffffffffffffffffffffffffffffffffff169060200190919050506105dc565b604051808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018515151515815260200184151515158152602001831515151581526020018260038111156101ae57fe5b60ff1681526020019550505050505060405180910390f35b6101ce610666565b604051808215151515815260200191505060405180910390f35b34156101f357600080fd5b6101fb610998565b6040518082815260200191505060405180910390f35b341561021c57600080fd5b61022461099e565b60405180826000191660001916815260200191505060405180910390f35b341561024d57600080fd5b610265600480803515159060200190919050506109a4565b604051808215151515815260200191505060405180910390f35b341561028a57600080fd5b610292611271565b604051808215151515815260200191505060405180910390f35b34156102b757600080fd5b6102cd6004808035906020019091905050611284565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b341561031a57600080fd5b6103226112c3565b60405180826000191660001916815260200191505060405180910390f35b341561034b57600080fd5b6103536112cc565b6040518082600481111561036357fe5b60ff16815260200191505060405180910390f35b341561038257600080fd5b61038a6112e3565b604051808215151515815260200191505060405180910390f35b34156103af57600080fd5b6103b7611693565b6040518082815260200191505060405180910390f35b34156103d857600080fd5b6103e061169d565b604051808260048111156103f057fe5b60ff16815260200191505060405180910390f35b341561040f57600080fd5b61042560048080359060200190919050506116b0565b604051808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200185151515158152602001841515151581526020018315151515815260200182600381111561048557fe5b60ff1681526020019550505050505060405180910390f35b34156104a857600080fd5b6104b0611990565b6040518082815260200191505060405180910390f35b34156104d157600080fd5b6104d9611996565b604051808215151515815260200191505060405180910390f35b34156104fe57600080fd5b6105066119ad565b604051808215151515815260200191505060405180910390f35b341561052b57600080fd5b610533611c9a565b6040518082815260200191505060405180910390f35b341561055457600080fd5b61055c611ca4565b6040518082815260200191505060405180910390f35b341561057d57600080fd5b61059560048080351515906020019091905050611cae565b604051808215151515815260200191505060405180910390f35b34156105ba57600080fd5b6105c2611e0a565b604051808215151515815260200191505060405180910390f35b600d6020528060005260406000206000915090508060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16908060000160149054906101000a900460ff16908060000160159054906101000a900460ff16908060000160169054906101000a900460ff16908060000160179054906101000a900460ff16905085565b600061067133611ed8565b151561067c57600080fd5b60011515600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160149054906101000a900460ff161515141515156106df57600080fd5b6003808111156106eb57fe5b600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff16600381111561074657fe5b14151561077157600154341015151561075e57600080fd5b600154600b600082825401925050819055505b6002600381111561077e57fe5b600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff1660038111156107d957fe5b14156107f2576007600081548092919060010191905055505b600160038111156107ff57fe5b600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff16600381111561085a57fe5b1415610873576008600081548092919060010191905055505b60038081111561087f57fe5b600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff1660038111156108da57fe5b14156108f3576009600081548092919060010191905055505b6001600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160146101000a81548160ff0219169083151502179055506005600081548092919060010191905055506006546005541415610991576001600c60156101000a81548160ff0219169083600481111561098b57fe5b02179055505b6001905090565b60065481565b60005481565b60008060008060006109b533611ed8565b15156109c057600080fd5b600260048111156109cd57fe5b600c60159054906101000a900460ff1660048111156109e857fe5b1415156109f457600080fd5b60011515600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160149054906101000a900460ff161515141515610a5657600080fd5b60001515600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160159054906101000a900460ff161515141515610ab857600080fd5b60009350600854600754101515610ad3576007549350610ad9565b60085493505b600380811115610ae557fe5b600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff166003811115610b4057fe5b141515610b7c578515610b6457600360008154809291906001019190505550610b77565b6002600081548092919060010191905055505b610bc3565b8515610ba45760095484811515610b8f57fe5b04600360008282540192505081905550610bc2565b60095484811515610bb157fe5b046002600082825401925050819055505b5b6004600081548092919060010191905055506001600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160156101000a81548160ff0219169083151502179055506005546004541415611263576002546003541415610d41576000600381905550600060028190555060006004819055506001600c60156101000a81548160ff02191690836004811115610c7f57fe5b0217905550600092505b600e80549050831015610d38576000600d6000600e86815481101515610cab57fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160156101000a81548160ff0219169083151502179055508280600101935050610c89565b60019450611268565b6002546003541115610d6d576001600c60006101000a81548160ff021916908315150217905550610d89565b6000600c60006101000a81548160ff0219169083151502179055505b6003600c60156101000a81548160ff02191690836004811115610da857fe5b021790555060009150600c60009054906101000a900460ff1615610df2576008543073ffffffffffffffffffffffffffffffffffffffff1631811515610dea57fe5b049150610e1a565b6007543073ffffffffffffffffffffffffffffffffffffffff1631811515610e1657fe5b0491505b600090505b600e8054905081101561126257600c60009054906101000a900460ff16156110a25760016003811115610e4e57fe5b600d6000600e84815481101515610e6157fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff166003811115610ee357fe5b1480611027575060006003811115610ef757fe5b600d6000600e84815481101515610f0a57fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff166003811115610f8c57fe5b148015611026575060011515600d6000600e84815481101515610fab57fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160149054906101000a900460ff161515145b5b1561109d57600e8181548110151561103b57fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166108fc839081150290604051600060405180830381858888f19350505050505b611255565b600260038111156110af57fe5b600d6000600e848154811015156110c257fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff16600381111561114457fe5b1480156111de575060011515600d6000600e8481548110151561116357fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160149054906101000a900460ff161515145b1561125457600e818154811015156111f257fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166108fc839081150290604051600060405180830381858888f19350505050505b5b8080600101915050610e1f565b5b600194505b50505050919050565b600c60009054906101000a900460ff1681565b600e8181548110151561129357fe5b90600052602060002090016000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60008054905090565b6000600c60159054906101000a900460ff16905090565b60006112ee33611ed8565b15156112f957600080fd5b6000600481111561130657fe5b600c60159054906101000a900460ff16600481111561132157fe5b14151561132d57600080fd5b60038081111561133957fe5b600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff16600381111561139457fe5b141515156113a157600080fd5b6001600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160146101000a81548160ff0219169083151502179055151561140657600080fd5b6001543073ffffffffffffffffffffffffffffffffffffffff16311015151561142e57600080fd5b6002600381111561143b57fe5b600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff16600381111561149657fe5b14156114c25760076000815480929190600190039190505550600154600b600082825403925050819055505b600160038111156114cf57fe5b600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff16600381111561152a57fe5b14156115565760086000815480929190600190039190505550600154600b600082825403925050819055505b60038081111561156257fe5b600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff1660038111156115bd57fe5b14156115d757600960008154809291906001900391905055505b6000600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160146101000a81548160ff0219169083151502179055503373ffffffffffffffffffffffffffffffffffffffff166108fc6001549081150290604051600060405180830381858888f1935050505015156116785760009050611690565b60056000815480929190600190039190505550600190505b90565b6000600b54905090565b600c60159054906101000a900460ff1681565b6000806000806000600d6000600e888154811015156116cb57fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600d6000600e8981548110151561176857fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160149054906101000a900460ff16600d6000600e8a8154811015156117f257fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160159054906101000a900460ff16600d6000600e8b81548110151561187c57fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160169054906101000a900460ff16600d6000600e8c81548110151561190657fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff169450945094509450945091939590929450565b60015481565b6000600c60009054906101000a900460ff16905090565b6000806119b933611ed8565b15156119c457600080fd5b600060048111156119d157fe5b600c60159054906101000a900460ff1660048111156119ec57fe5b1480611a1d575060016004811115611a0057fe5b600c60159054906101000a900460ff166004811115611a1b57fe5b145b80611a4d575060026004811115611a3057fe5b600c60159054906101000a900460ff166004811115611a4b57fe5b145b1515611a5857600080fd5b60001515600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160169054906101000a900460ff161515141515611aba57600080fd5b600a600081548092919060010191905055506001600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160166101000a81548160ff021916908315150217905550600554600a541415611c92576004600c60156101000a81548160ff02191690836004811115611b5257fe5b0217905550600090505b600e80549050811015611c9157600380811115611b7557fe5b600d6000600e84815481101515611b8857fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff166003811115611c0a57fe5b141515611c8457600e81815481101515611c2057fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166108fc6001549081150290604051600060405180830381858888f19350505050505b8080600101915050611b5c565b5b600191505090565b6000600654905090565b6000600154905090565b6000600c60019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515611d0c57600080fd5b60016004811115611d1957fe5b600c60159054906101000a900460ff166004811115611d3457fe5b141515611d4057600080fd5b8115611d5d57600360008154809291906001019190505550611d70565b6002600081548092919060010191905055505b6001600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160156101000a81548160ff0219169083151502179055506004600081548092919060010191905055506002600c60156101000a81548160ff02191690836004811115611dfc57fe5b021790555060019050919050565b6000600c60019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515611e6857600080fd5b60006004811115611e7557fe5b600c60159054906101000a900460ff166004811115611e9057fe5b141515611e9c57600080fd5b6000600754111515611ead57600080fd5b6001600c60156101000a81548160ff02191690836004811115611ecc57fe5b02179055506001905090565b60008073ffffffffffffffffffffffffffffffffffffffff16600d60008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16141515611f7a5760019050611f7f565b600090505b9190505600a165627a7a72305820b35db07c14daccf50178702c1473d2016b875036ef44f21cb0a6571af6d5f1b70029";

    protected BetChainBetContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected BetChainBetContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<Tuple5<String, Boolean, Boolean, Boolean, BigInteger>> participants(String param0) {
        final Function function = new Function("participants", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bool>() {}, new TypeReference<Bool>() {}, new TypeReference<Bool>() {}, new TypeReference<Uint8>() {}));
        return new RemoteCall<Tuple5<String, Boolean, Boolean, Boolean, BigInteger>>(
                new Callable<Tuple5<String, Boolean, Boolean, Boolean, BigInteger>>() {
                    @Override
                    public Tuple5<String, Boolean, Boolean, Boolean, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<String, Boolean, Boolean, Boolean, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (Boolean) results.get(1).getValue(), 
                                (Boolean) results.get(2).getValue(), 
                                (Boolean) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue());
                    }
                });
    }

    public RemoteCall<TransactionReceipt> acceptBet(BigInteger weiValue) {
        final Function function = new Function(
                "acceptBet", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<BigInteger> participantCount() {
        final Function function = new Function("participantCount", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<byte[]> betConditions() {
        final Function function = new Function("betConditions", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteCall<TransactionReceipt> vote(Boolean betSuccessfull) {
        final Function function = new Function(
                "vote", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Bool(betSuccessfull)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> isBetSuccessfull() {
        final Function function = new Function("isBetSuccessfull", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<String> participantAddresses(BigInteger param0) {
        final Function function = new Function("participantAddresses", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<byte[]> getBetConditions() {
        final Function function = new Function("getBetConditions", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteCall<BigInteger> getBetState() {
        final Function function = new Function("getBetState", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> retreatFromBet() {
        final Function function = new Function(
                "retreatFromBet", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> getBetPrizePool() {
        final Function function = new Function("getBetPrizePool", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> currentBetState() {
        final Function function = new Function("currentBetState", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Tuple5<String, Boolean, Boolean, Boolean, BigInteger>> getParticipantInfo(BigInteger index) {
        final Function function = new Function("getParticipantInfo", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bool>() {}, new TypeReference<Bool>() {}, new TypeReference<Bool>() {}, new TypeReference<Uint8>() {}));
        return new RemoteCall<Tuple5<String, Boolean, Boolean, Boolean, BigInteger>>(
                new Callable<Tuple5<String, Boolean, Boolean, Boolean, BigInteger>>() {
                    @Override
                    public Tuple5<String, Boolean, Boolean, Boolean, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<String, Boolean, Boolean, Boolean, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (Boolean) results.get(1).getValue(), 
                                (Boolean) results.get(2).getValue(), 
                                (Boolean) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue());
                    }
                });
    }

    public RemoteCall<BigInteger> betEntryFee() {
        final Function function = new Function("betEntryFee", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Boolean> isBetSuccessfullI() {
        final Function function = new Function("isBetSuccessfullI", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> abortBet() {
        final Function function = new Function(
                "abortBet", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> getNumberOfParticipants() {
        final Function function = new Function("getNumberOfParticipants", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getBetEntryFee() {
        final Function function = new Function("getBetEntryFee", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> startVote(Boolean betSuccessfull) {
        final Function function = new Function(
                "startVote", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Bool(betSuccessfull)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> startBet() {
        final Function function = new Function(
                "startBet", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<BetChainBetContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, byte[] _betConditions, BigInteger _betEntryFee, List<String> _participantAddresses, List<BigInteger> _participantRoles) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_betConditions), 
                new org.web3j.abi.datatypes.generated.Uint256(_betEntryFee), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.Utils.typeMap(_participantAddresses, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint8>(
                        org.web3j.abi.Utils.typeMap(_participantRoles, org.web3j.abi.datatypes.generated.Uint8.class))));
        return deployRemoteCall(BetChainBetContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor, initialWeiValue);
    }

    public static RemoteCall<BetChainBetContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, byte[] _betConditions, BigInteger _betEntryFee, List<String> _participantAddresses, List<BigInteger> _participantRoles) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_betConditions), 
                new org.web3j.abi.datatypes.generated.Uint256(_betEntryFee), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.Utils.typeMap(_participantAddresses, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint8>(
                        org.web3j.abi.Utils.typeMap(_participantRoles, org.web3j.abi.datatypes.generated.Uint8.class))));
        return deployRemoteCall(BetChainBetContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, initialWeiValue);
    }

    public static BetChainBetContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new BetChainBetContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static BetChainBetContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new BetChainBetContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static String getBinary(){
        return BINARY;
    }
}
