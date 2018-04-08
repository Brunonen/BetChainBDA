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
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple4;
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
    private static final String BINARY = "606060405260405162001ccf38038062001ccf83398101604052808051906020019091908051906020019091908051820191906020018051820191905050600034841415156200004e57600080fd5b6001600381905550600060028190555060006004819055506080604051908101604052803373ffffffffffffffffffffffffffffffffffffffff16815260200160011515815260200160001515815260200160006003811115620000ae57fe5b815250600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060208201518160000160146101000a81548160ff02191690831515021790555060408201518160000160156101000a81548160ff02191690831515021790555060608201518160000160166101000a81548160ff021916908360038111156200019c57fe5b021790555090505060088054806001018281620001ba919062000536565b9160005260206000209001600033909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505060046000815480929190600101919050555084600081600019169055508360018190555060036000815480929190600101919050555033600660016101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000600660156101000a81548160ff02191690836004811115620002a057fe5b02179055506000600581905550600090505b82518110156200052b576080604051908101604052808483815181101515620002d757fe5b9060200190602002015173ffffffffffffffffffffffffffffffffffffffff16815260200160001515815260200160001515815260200183838151811015156200031d57fe5b9060200190602002015160038111156200033357fe5b8152506007600085848151811015156200034957fe5b9060200190602002015173ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060208201518160000160146101000a81548160ff02191690831515021790555060408201518160000160156101000a81548160ff02191690831515021790555060608201518160000160166101000a81548160ff021916908360038111156200043957fe5b02179055509050506008805480600101828162000457919062000536565b9160005260206000209001600085848151811015156200047357fe5b90602001906020020151909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505060046000815480929190600101919050555060026003811115620004de57fe5b8282815181101515620004ed57fe5b9060200190602002015160038111156200050357fe5b14156200051d576005600081548092919060010191905055505b8080600101915050620002b2565b50505050506200058d565b81548183558181151162000560578183600052602060002091820191016200055f919062000565565b5b505050565b6200058a91905b80821115620005865760008160009055506001016200056c565b5090565b90565b611732806200059d6000396000f300606060405260043610610112576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806309e69ede1461011757806330bfcb5f146101bb578063362f04c0146101dd5780633912253e146102065780634b29fd2e1461023757806354a322721461027057806354ffbddb1461029d578063558a5ec6146103005780635d98230f1461033957806377e58a161461036a5780637956d375146103a35780638cda68d8146103da5780638f6deb381461040757806399823ace14610430578063b110c73114610467578063b2114ca1146104f5578063bf3cff371461051e578063c80c28a21461054b578063e32a28b014610574578063fa6776db1461059d575b600080fd5b341561012257600080fd5b61014e600480803573ffffffffffffffffffffffffffffffffffffffff169060200190919050506105ca565b604051808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200184151515158152602001831515151581526020018260038111156101a457fe5b60ff16815260200194505050505060405180910390f35b6101c3610641565b604051808215151515815260200191505060405180910390f35b34156101e857600080fd5b6101f061081c565b6040518082815260200191505060405180910390f35b341561021157600080fd5b610219610822565b60405180826000191660001916815260200191505060405180910390f35b341561024257600080fd5b61026e600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050610828565b005b341561027b57600080fd5b610283610a87565b604051808215151515815260200191505060405180910390f35b34156102a857600080fd5b6102be6004808035906020019091905050610a9a565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b341561030b57600080fd5b610337600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050610ad9565b005b341561034457600080fd5b61034c610d38565b60405180826000191660001916815260200191505060405180910390f35b341561037557600080fd5b6103a1600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050610d41565b005b34156103ae57600080fd5b6103b6610f9f565b604051808260048111156103c657fe5b60ff16815260200191505060405180910390f35b34156103e557600080fd5b6103ed610fb6565b604051808215151515815260200191505060405180910390f35b341561041257600080fd5b61041a61122c565b6040518082815260200191505060405180910390f35b341561043b57600080fd5b61044361124b565b6040518082600481111561045357fe5b60ff16815260200191505060405180910390f35b341561047257600080fd5b610488600480803590602001909190505061125e565b604051808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200184151515158152602001831515151581526020018260038111156104de57fe5b60ff16815260200194505050505060405180910390f35b341561050057600080fd5b6105086114ae565b6040518082815260200191505060405180910390f35b341561052957600080fd5b6105316114b4565b604051808215151515815260200191505060405180910390f35b341561055657600080fd5b61055e6114cb565b6040518082815260200191505060405180910390f35b341561057f57600080fd5b6105876114d5565b6040518082815260200191505060405180910390f35b34156105a857600080fd5b6105b06114df565b604051808215151515815260200191505060405180910390f35b60076020528060005260406000206000915090508060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16908060000160149054906101000a900460ff16908060000160159054906101000a900460ff16908060000160169054906101000a900460ff16905084565b600061064c33611609565b151561065757600080fd5b60011515600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160149054906101000a900460ff161515141515156106ba57600080fd5b6003808111156106c657fe5b600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160169054906101000a900460ff16600381111561072157fe5b141515610739576001543414151561073857600080fd5b5b6002600381111561074657fe5b600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160169054906101000a900460ff1660038111156107a157fe5b14156107ba576005600081548092919060010191905055505b6001600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160146101000a81548160ff0219169083151502179055506001905090565b60045481565b60005481565b600660019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561088457600080fd5b6000600481111561089157fe5b600660159054906101000a900460ff1660048111156108ac57fe5b1415156108b857600080fd5b600015156108c582611609565b15151415156108d357600080fd5b6080604051908101604052808273ffffffffffffffffffffffffffffffffffffffff1681526020016000151581526020016000151581526020016001600381111561091a57fe5b815250600760008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060208201518160000160146101000a81548160ff02191690831515021790555060408201518160000160156101000a81548160ff02191690831515021790555060608201518160000160166101000a81548160ff02191690836003811115610a0757fe5b021790555090505060088054806001018281610a2391906116b5565b9160005260206000209001600083909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505060046000815480929190600101919050555050565b600660009054906101000a900460ff1681565b600881815481101515610aa957fe5b90600052602060002090016000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600660019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610b3557600080fd5b60006004811115610b4257fe5b600660159054906101000a900460ff166004811115610b5d57fe5b141515610b6957600080fd5b60001515610b7682611609565b1515141515610b8457600080fd5b6080604051908101604052808273ffffffffffffffffffffffffffffffffffffffff16815260200160001515815260200160001515815260200160026003811115610bcb57fe5b815250600760008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060208201518160000160146101000a81548160ff02191690831515021790555060408201518160000160156101000a81548160ff02191690831515021790555060608201518160000160166101000a81548160ff02191690836003811115610cb857fe5b021790555090505060088054806001018281610cd491906116b5565b9160005260206000209001600083909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505060046000815480929190600101919050555050565b60008054905090565b600660019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610d9d57600080fd5b60006004811115610daa57fe5b600660159054906101000a900460ff166004811115610dc557fe5b141515610dd157600080fd5b60001515610dde82611609565b1515141515610dec57600080fd5b6080604051908101604052808273ffffffffffffffffffffffffffffffffffffffff168152602001600015158152602001600015158152602001600380811115610e3257fe5b815250600760008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060208201518160000160146101000a81548160ff02191690831515021790555060408201518160000160156101000a81548160ff02191690831515021790555060608201518160000160166101000a81548160ff02191690836003811115610f1f57fe5b021790555090505060088054806001018281610f3b91906116b5565b9160005260206000209001600083909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505060046000815480929190600101919050555050565b6000600660159054906101000a900460ff16905090565b6000610fc133611609565b1515610fcc57600080fd5b60006004811115610fd957fe5b600660159054906101000a900460ff166004811115610ff457fe5b14151561100057600080fd5b60038081111561100c57fe5b600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160169054906101000a900460ff16600381111561106757fe5b1415151561107457600080fd5b6001600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160146101000a81548160ff021916908315150217905515156110d957600080fd5b6001543073ffffffffffffffffffffffffffffffffffffffff16311015151561110157600080fd5b6002600381111561110e57fe5b600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160169054906101000a900460ff16600381111561116957fe5b141561118357600560008154809291906001900391905055505b6000600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160146101000a81548160ff0219169083151502179055503373ffffffffffffffffffffffffffffffffffffffff166108fc6001549081150290604051600060405180830381858888f1935050505015156112245760009050611229565b600190505b90565b60003073ffffffffffffffffffffffffffffffffffffffff1631905090565b600660159054906101000a900460ff1681565b6000806000806007600060088781548110151561127757fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff166007600060088881548110151561131457fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160149054906101000a900460ff166007600060088981548110151561139e57fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160159054906101000a900460ff166007600060088a81548110151561142857fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160169054906101000a900460ff1693509350935093509193509193565b60015481565b6000600660009054906101000a900460ff16905090565b6000600454905090565b6000600154905090565b6000600660019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561153d57600080fd5b600660019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561159957600080fd5b600060048111156115a657fe5b600660159054906101000a900460ff1660048111156115c157fe5b1415156115cd57600080fd5b60006005541115156115de57600080fd5b6001600660156101000a81548160ff021916908360048111156115fd57fe5b02179055506001905090565b60008073ffffffffffffffffffffffffffffffffffffffff16600760008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161415156116ab57600190506116b0565b600090505b919050565b8154818355818115116116dc578183600052602060002091820191016116db91906116e1565b5b505050565b61170391905b808211156116ff5760008160009055506001016116e7565b5090565b905600a165627a7a7230582016509670b8b724e5aa6456900d12d9648c8c2874e4d2d0e26a10c97aac98ad390029";

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

    public RemoteCall<Tuple4<String, Boolean, Boolean, BigInteger>> participants(String param0) {
        final Function function = new Function("participants", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)), 
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

    public RemoteCall<String> participantAddresses(BigInteger param0) {
        final Function function = new Function("participantAddresses", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
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
