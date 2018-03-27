package bda.hslu.ch.betchain;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

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
    private static final String BINARY = "606060405234156200001057600080fd5b60405160408062001e928339810160405280805190602001909190805190602001909190505060016003819055506000600281905550600060048190555060a0604051908101604052803373ffffffffffffffffffffffffffffffffffffffff1681526020016000151581526020016000151581526020016004548152602001600160038111156200009e57fe5b815250600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060208201518160000160146101000a81548160ff02191690831515021790555060408201518160000160156101000a81548160ff0219169083151502179055506060820151816001015560808201518160020160006101000a81548160ff021916908360038111156200019657fe5b021790555090505060088054806001018281620001b4919062000386565b9160005260206000209060030201600060a0604051908101604052803373ffffffffffffffffffffffffffffffffffffffff1681526020016000151581526020016000151581526020016004548152602001600060038111156200021457fe5b815250909190915060008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060208201518160000160146101000a81548160ff02191690831515021790555060408201518160000160156101000a81548160ff0219169083151502179055506060820151816001015560808201518160020160006101000a81548160ff02191690836003811115620002d257fe5b021790555050505060046000815480929190600101919050555081600081600019169055508060018190555060036000815480929190600101919050555033600660016101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000600660156101000a81548160ff021916908360048111156200037157fe5b02179055506000600581905550505062000448565b815481835581811511620003b657600302816003028360005260206000209182019101620003b59190620003bb565b5b505050565b6200044591905b808211156200044157600080820160006101000a81549073ffffffffffffffffffffffffffffffffffffffff02191690556000820160146101000a81549060ff02191690556000820160156101000a81549060ff021916905560018201600090556002820160006101000a81549060ff021916905550600301620003c2565b5090565b90565b611a3a80620004586000396000f300606060405260043610610112576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806309e69ede1461011757806330bfcb5f146101c2578063362f04c0146101e45780633912253e1461020d5780634b29fd2e1461023e57806354a3227214610277578063558a5ec6146102a45780635d98230f146102dd57806377e58a161461030e5780637956d375146103475780638cda68d81461037e5780638f6deb38146103ab57806399823ace146103d4578063b110c7311461040b578063b2114ca114610499578063bf3cff37146104c2578063c80c28a2146104ef578063e32a28b014610518578063f617873d14610541578063fa6776db146105d6575b600080fd5b341561012257600080fd5b61014e600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050610603565b604051808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200185151515158152602001841515151581526020018381526020018260038111156101aa57fe5b60ff1681526020019550505050505060405180910390f35b6101ca610680565b604051808215151515815260200191505060405180910390f35b34156101ef57600080fd5b6101f76108c2565b6040518082815260200191505060405180910390f35b341561021857600080fd5b6102206108c8565b60405180826000191660001916815260200191505060405180910390f35b341561024957600080fd5b610275600480803573ffffffffffffffffffffffffffffffffffffffff169060200190919050506108ce565b005b341561028257600080fd5b61028a610c14565b604051808215151515815260200191505060405180910390f35b34156102af57600080fd5b6102db600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050610c27565b005b34156102e857600080fd5b6102f0610f6d565b60405180826000191660001916815260200191505060405180910390f35b341561031957600080fd5b610345600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050610f76565b005b341561035257600080fd5b61035a6112ba565b6040518082600481111561036a57fe5b60ff16815260200191505060405180910390f35b341561038957600080fd5b6103916112d1565b604051808215151515815260200191505060405180910390f35b34156103b657600080fd5b6103be6115ad565b6040518082815260200191505060405180910390f35b34156103df57600080fd5b6103e76115cc565b604051808260048111156103f757fe5b60ff16815260200191505060405180910390f35b341561041657600080fd5b61042c60048080359060200190919050506115df565b604051808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001841515151581526020018315151515815260200182600381111561048257fe5b60ff16815260200194505050505060405180910390f35b34156104a457600080fd5b6104ac6116bf565b6040518082815260200191505060405180910390f35b34156104cd57600080fd5b6104d56116c5565b604051808215151515815260200191505060405180910390f35b34156104fa57600080fd5b6105026116dc565b6040518082815260200191505060405180910390f35b341561052357600080fd5b61052b6116e6565b6040518082815260200191505060405180910390f35b341561054c57600080fd5b61056260048080359060200190919050506116f0565b604051808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200185151515158152602001841515151581526020018381526020018260038111156105be57fe5b60ff1681526020019550505050505060405180910390f35b34156105e157600080fd5b6105e961177c565b604051808215151515815260200191505060405180910390f35b60076020528060005260406000206000915090508060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16908060000160149054906101000a900460ff16908060000160159054906101000a900460ff16908060010154908060020160009054906101000a900460ff16905085565b600060011515600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160149054906101000a900460ff161515141515156106e557600080fd5b6003808111156106f157fe5b600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060020160009054906101000a900460ff16600381111561074c57fe5b141515610764576001543414151561076357600080fd5b5b6002600381111561077157fe5b600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060020160009054906101000a900460ff1660038111156107cc57fe5b14156107e5576005600081548092919060010191905055505b6001600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160146101000a81548160ff02191690831515021790555060016008600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206001015481548110151561089357fe5b906000526020600020906003020160000160146101000a81548160ff0219169083151502179055506001905090565b60045481565b60005481565b600660019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561092a57600080fd5b6000600481111561093757fe5b600660159054906101000a900460ff16600481111561095257fe5b14151561095e57600080fd5b6000151561096b826118a6565b151514151561097957600080fd5b60a0604051908101604052808273ffffffffffffffffffffffffffffffffffffffff1681526020016000151581526020016000151581526020016004548152602001600160038111156109c857fe5b815250600760008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060208201518160000160146101000a81548160ff02191690831515021790555060408201518160000160156101000a81548160ff0219169083151502179055506060820151816001015560808201518160020160006101000a81548160ff02191690836003811115610abf57fe5b021790555090505060088054806001018281610adb9190611952565b9160005260206000209060030201600060a0604051908101604052808573ffffffffffffffffffffffffffffffffffffffff168152602001600015158152602001600015158152602001600454815260200160016003811115610b3a57fe5b815250909190915060008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060208201518160000160146101000a81548160ff02191690831515021790555060408201518160000160156101000a81548160ff0219169083151502179055506060820151816001015560808201518160020160006101000a81548160ff02191690836003811115610bf757fe5b021790555050505060046000815480929190600101919050555050565b600660009054906101000a900460ff1681565b600660019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610c8357600080fd5b60006004811115610c9057fe5b600660159054906101000a900460ff166004811115610cab57fe5b141515610cb757600080fd5b60001515610cc4826118a6565b1515141515610cd257600080fd5b60a0604051908101604052808273ffffffffffffffffffffffffffffffffffffffff168152602001600015158152602001600015158152602001600454815260200160026003811115610d2157fe5b815250600760008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060208201518160000160146101000a81548160ff02191690831515021790555060408201518160000160156101000a81548160ff0219169083151502179055506060820151816001015560808201518160020160006101000a81548160ff02191690836003811115610e1857fe5b021790555090505060088054806001018281610e349190611952565b9160005260206000209060030201600060a0604051908101604052808573ffffffffffffffffffffffffffffffffffffffff168152602001600015158152602001600015158152602001600454815260200160026003811115610e9357fe5b815250909190915060008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060208201518160000160146101000a81548160ff02191690831515021790555060408201518160000160156101000a81548160ff0219169083151502179055506060820151816001015560808201518160020160006101000a81548160ff02191690836003811115610f5057fe5b021790555050505060046000815480929190600101919050555050565b60008054905090565b600660019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610fd257600080fd5b60006004811115610fdf57fe5b600660159054906101000a900460ff166004811115610ffa57fe5b14151561100657600080fd5b60001515611013826118a6565b151514151561102157600080fd5b60a0604051908101604052808273ffffffffffffffffffffffffffffffffffffffff168152602001600015158152602001600015158152602001600454815260200160038081111561106f57fe5b815250600760008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060208201518160000160146101000a81548160ff02191690831515021790555060408201518160000160156101000a81548160ff0219169083151502179055506060820151816001015560808201518160020160006101000a81548160ff0219169083600381111561116657fe5b0217905550905050600880548060010182816111829190611952565b9160005260206000209060030201600060a0604051908101604052808573ffffffffffffffffffffffffffffffffffffffff16815260200160001515815260200160001515815260200160045481526020016003808111156111e057fe5b815250909190915060008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060208201518160000160146101000a81548160ff02191690831515021790555060408201518160000160156101000a81548160ff0219169083151502179055506060820151816001015560808201518160020160006101000a81548160ff0219169083600381111561129d57fe5b021790555050505060046000815480929190600101919050555050565b6000600660159054906101000a900460ff16905090565b60008060048111156112df57fe5b600660159054906101000a900460ff1660048111156112fa57fe5b14151561130657600080fd5b60038081111561131257fe5b600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060020160009054906101000a900460ff16600381111561136d57fe5b1415151561137a57600080fd5b6001600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160146101000a81548160ff021916908315150217905515156113df57600080fd5b6001543073ffffffffffffffffffffffffffffffffffffffff16311015151561140757600080fd5b6002600381111561141457fe5b600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060020160009054906101000a900460ff16600381111561146f57fe5b141561148957600560008154809291906001900391905055505b6000600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160146101000a81548160ff02191690831515021790555060006008600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206001015481548110151561153757fe5b906000526020600020906003020160000160146101000a81548160ff0219169083151502179055503373ffffffffffffffffffffffffffffffffffffffff166108fc6001549081150290604051600060405180830381858888f1935050505015156115a557600090506115aa565b600190505b90565b60003073ffffffffffffffffffffffffffffffffffffffff1631905090565b600660159054906101000a900460ff1681565b6000806000806008858154811015156115f457fe5b906000526020600020906003020160000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1660088681548110151561163557fe5b906000526020600020906003020160000160149054906101000a900460ff1660088781548110151561166357fe5b906000526020600020906003020160000160159054906101000a900460ff1660088881548110151561169157fe5b906000526020600020906003020160020160009054906101000a900460ff1693509350935093509193509193565b60015481565b6000600660009054906101000a900460ff16905090565b6000600454905090565b6000600154905090565b6008818154811015156116ff57fe5b90600052602060002090600302016000915090508060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16908060000160149054906101000a900460ff16908060000160159054906101000a900460ff16908060010154908060020160009054906101000a900460ff16905085565b6000600660019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156117da57600080fd5b600660019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561183657600080fd5b6000600481111561184357fe5b600660159054906101000a900460ff16600481111561185e57fe5b14151561186a57600080fd5b600060055411151561187b57600080fd5b6001600660156101000a81548160ff0219169083600481111561189a57fe5b02179055506001905090565b60008073ffffffffffffffffffffffffffffffffffffffff16600760008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16141515611948576001905061194d565b600090505b919050565b81548183558181151161197f5760030281600302836000526020600020918201910161197e9190611984565b5b505050565b611a0b91905b80821115611a0757600080820160006101000a81549073ffffffffffffffffffffffffffffffffffffffff02191690556000820160146101000a81549060ff02191690556000820160156101000a81549060ff021916905560018201600090556002820160006101000a81549060ff02191690555060030161198a565b5090565b905600a165627a7a72305820f7608664e27736b7e8afc6533945ed511267dabdf0cb6f437b0fb2c96094c4d00029";

    protected BetChainBetContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected BetChainBetContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<BailEventResponse> getBailEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Bail", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<BailEventResponse> responses = new ArrayList<BailEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BailEventResponse typedResponse = new BailEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._to = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<BailEventResponse> bailEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Bail", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, BailEventResponse>() {
            @Override
            public BailEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                BailEventResponse typedResponse = new BailEventResponse();
                typedResponse.log = log;
                typedResponse._to = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public List<BetStartedEventResponse> getBetStartedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("BetStarted", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList());
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<BetStartedEventResponse> responses = new ArrayList<BetStartedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BetStartedEventResponse typedResponse = new BetStartedEventResponse();
            typedResponse.log = eventValues.getLog();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<BetStartedEventResponse> betStartedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("BetStarted", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList());
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, BetStartedEventResponse>() {
            @Override
            public BetStartedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                BetStartedEventResponse typedResponse = new BetStartedEventResponse();
                typedResponse.log = log;
                return typedResponse;
            }
        });
    }

    public List<BetAbortedEventResponse> getBetAbortedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("BetAborted", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList());
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<BetAbortedEventResponse> responses = new ArrayList<BetAbortedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BetAbortedEventResponse typedResponse = new BetAbortedEventResponse();
            typedResponse.log = eventValues.getLog();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<BetAbortedEventResponse> betAbortedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("BetAborted", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList());
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, BetAbortedEventResponse>() {
            @Override
            public BetAbortedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                BetAbortedEventResponse typedResponse = new BetAbortedEventResponse();
                typedResponse.log = log;
                return typedResponse;
            }
        });
    }

    public RemoteCall<Tuple5<String, Boolean, Boolean, BigInteger, BigInteger>> participants(String param0) {
        final Function function = new Function("participants", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bool>() {}, new TypeReference<Bool>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint8>() {}));
        return new RemoteCall<Tuple5<String, Boolean, Boolean, BigInteger, BigInteger>>(
                new Callable<Tuple5<String, Boolean, Boolean, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple5<String, Boolean, Boolean, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<String, Boolean, Boolean, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (Boolean) results.get(1).getValue(), 
                                (Boolean) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
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

    public RemoteCall<TransactionReceipt> addBetSupporter(String pIdentifier) {
        final Function function = new Function(
                "addBetSupporter", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(pIdentifier)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> isBetSuccessfull() {
        final Function function = new Function("isBetSuccessfull", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> addBetOpposer(String pIdentifier) {
        final Function function = new Function(
                "addBetOpposer", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(pIdentifier)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<byte[]> getBetConditions() {
        final Function function = new Function("getBetConditions", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteCall<TransactionReceipt> addBetNotar(String pIdentifier) {
        final Function function = new Function(
                "addBetNotar", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(pIdentifier)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
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

    public RemoteCall<Tuple4<String, Boolean, Boolean, BigInteger>> getParticipantInfo(BigInteger index) {
        final Function function = new Function("getParticipantInfo", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bool>() {}, new TypeReference<Bool>() {}, new TypeReference<Uint8>() {}));
        return new RemoteCall<Tuple4<String, Boolean, Boolean, BigInteger>>(
                new Callable<Tuple4<String, Boolean, Boolean, BigInteger>>() {
                    @Override
                    public Tuple4<String, Boolean, Boolean, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<String, Boolean, Boolean, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (Boolean) results.get(1).getValue(), 
                                (Boolean) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue());
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

    public RemoteCall<Tuple5<String, Boolean, Boolean, BigInteger, BigInteger>> iterateableParticipants(BigInteger param0) {
        final Function function = new Function("iterateableParticipants", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bool>() {}, new TypeReference<Bool>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint8>() {}));
        return new RemoteCall<Tuple5<String, Boolean, Boolean, BigInteger, BigInteger>>(
                new Callable<Tuple5<String, Boolean, Boolean, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple5<String, Boolean, Boolean, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<String, Boolean, Boolean, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (Boolean) results.get(1).getValue(), 
                                (Boolean) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue());
                    }
                });
    }

    public RemoteCall<TransactionReceipt> startBet() {
        final Function function = new Function(
                "startBet", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<BetChainBetContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, byte[] _betConditions, BigInteger _betEntryFee) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_betConditions), 
                new org.web3j.abi.datatypes.generated.Uint256(_betEntryFee)));
        return deployRemoteCall(BetChainBetContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<BetChainBetContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, byte[] _betConditions, BigInteger _betEntryFee) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_betConditions), 
                new org.web3j.abi.datatypes.generated.Uint256(_betEntryFee)));
        return deployRemoteCall(BetChainBetContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static BetChainBetContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new BetChainBetContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static BetChainBetContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new BetChainBetContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class BailEventResponse {
        public Log log;

        public String _to;

        public BigInteger _amount;
    }

    public static class BetStartedEventResponse {
        public Log log;
    }

    public static class BetAbortedEventResponse {
        public Log log;
    }
}
