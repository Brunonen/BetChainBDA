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
import org.web3j.abi.datatypes.Utf8String;
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
    private static final String BINARY = "6060604052604051620028533803806200285383398101604052808051820191906020018051906020019091908051820191906020018051820191905050600034841415156200004e57600080fd5b600060068190555060a0604051908101604052803373ffffffffffffffffffffffffffffffffffffffff16815260200160011515815260200160001515815260200160001515815260200160006003811115620000a757fe5b815250600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060208201518160000160146101000a81548160ff02191690831515021790555060408201518160000160156101000a81548160ff02191690831515021790555060608201518160000160166101000a81548160ff02191690831515021790555060808201518160000160176101000a81548160ff02191690836003811115620001b557fe5b0217905550905050600e8054806001018281620001d391906200054e565b9160005260206000209001600033909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550506006600081548092919060010191905055506001600581905550600060048190555083600b819055508460009080519060200190620002639291906200057d565b508360018190555033600c60016101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000600c60156101000a81548160ff02191690836004811115620002cc57fe5b0217905550600060078190555060016008819055506000600981905550600090505b8251811015620005435760a06040519081016040528084838151811015156200031357fe5b9060200190602002015173ffffffffffffffffffffffffffffffffffffffff16815260200160001515815260200160001515815260200160001515815260200183838151811015156200036257fe5b9060200190602002015160038111156200037857fe5b815250600d600085848151811015156200038e57fe5b9060200190602002015173ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060208201518160000160146101000a81548160ff02191690831515021790555060408201518160000160156101000a81548160ff02191690831515021790555060608201518160000160166101000a81548160ff02191690831515021790555060808201518160000160176101000a81548160ff021916908360038111156200049e57fe5b0217905550905050600e8054806001018281620004bc91906200054e565b916000526020600020900160008584815181101515620004d857fe5b90602001906020020151909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550506006600081548092919060010191905055508080600101915050620002ee565b50505050506200062c565b815481835581811511620005785781836000526020600020918201910162000577919062000604565b5b505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620005c057805160ff1916838001178555620005f1565b82800160010185558215620005f1579182015b82811115620005f0578251825591602001919060010190620005d3565b5b50905062000600919062000604565b5090565b6200062991905b80821115620006255760008160009055506001016200060b565b5090565b90565b612217806200063c6000396000f300606060405260043610610112576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806309e69ede1461011757806330bfcb5f146101c6578063362f04c0146101e85780633912253e146102115780634b9f5c981461029f57806354a32272146102dc57806354ffbddb146103095780635d98230f1461036c5780637956d375146103fa5780638cda68d8146104315780638f6deb381461045e57806399823ace14610487578063b110c731146104be578063b2114ca114610557578063bf3cff3714610580578063c4e23a5c146105ad578063c80c28a2146105da578063e32a28b014610603578063e8ac03a81461062c578063fa6776db14610669575b600080fd5b341561012257600080fd5b61014e600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050610696565b604051808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018515151515815260200184151515158152602001831515151581526020018260038111156101ae57fe5b60ff1681526020019550505050505060405180910390f35b6101ce610720565b604051808215151515815260200191505060405180910390f35b34156101f357600080fd5b6101fb610a52565b6040518082815260200191505060405180910390f35b341561021c57600080fd5b610224610a58565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610264578082015181840152602081019050610249565b50505050905090810190601f1680156102915780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34156102aa57600080fd5b6102c260048080351515906020019091905050610af6565b604051808215151515815260200191505060405180910390f35b34156102e757600080fd5b6102ef6113c3565b604051808215151515815260200191505060405180910390f35b341561031457600080fd5b61032a60048080359060200190919050506113d6565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b341561037757600080fd5b61037f611415565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156103bf5780820151818401526020810190506103a4565b50505050905090810190601f1680156103ec5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b341561040557600080fd5b61040d6114bd565b6040518082600481111561041d57fe5b60ff16815260200191505060405180910390f35b341561043c57600080fd5b6104446114d4565b604051808215151515815260200191505060405180910390f35b341561046957600080fd5b610471611884565b6040518082815260200191505060405180910390f35b341561049257600080fd5b61049a61188e565b604051808260048111156104aa57fe5b60ff16815260200191505060405180910390f35b34156104c957600080fd5b6104df60048080359060200190919050506118a1565b604051808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200185151515158152602001841515151581526020018315151515815260200182600381111561053f57fe5b60ff1681526020019550505050505060405180910390f35b341561056257600080fd5b61056a611b81565b6040518082815260200191505060405180910390f35b341561058b57600080fd5b610593611b87565b604051808215151515815260200191505060405180910390f35b34156105b857600080fd5b6105c0611b9e565b604051808215151515815260200191505060405180910390f35b34156105e557600080fd5b6105ed611eed565b6040518082815260200191505060405180910390f35b341561060e57600080fd5b610616611ef7565b6040518082815260200191505060405180910390f35b341561063757600080fd5b61064f60048080351515906020019091905050611f01565b604051808215151515815260200191505060405180910390f35b341561067457600080fd5b61067c61205d565b604051808215151515815260200191505060405180910390f35b600d6020528060005260406000206000915090508060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16908060000160149054906101000a900460ff16908060000160159054906101000a900460ff16908060000160169054906101000a900460ff16908060000160179054906101000a900460ff16905085565b600061072b3361212b565b151561073657600080fd5b60011515600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160149054906101000a900460ff1615151415151561079957600080fd5b6003808111156107a557fe5b600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff16600381111561080057fe5b14151561082b57600154341015151561081857600080fd5b600154600b600082825401925050819055505b6002600381111561083857fe5b600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff16600381111561089357fe5b14156108ac576007600081548092919060010191905055505b600160038111156108b957fe5b600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff16600381111561091457fe5b141561092d576008600081548092919060010191905055505b60038081111561093957fe5b600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff16600381111561099457fe5b14156109ad576009600081548092919060010191905055505b6001600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160146101000a81548160ff0219169083151502179055506005600081548092919060010191905055506006546005541415610a4b576001600c60156101000a81548160ff02191690836004811115610a4557fe5b02179055505b6001905090565b60065481565b60008054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610aee5780601f10610ac357610100808354040283529160200191610aee565b820191906000526020600020905b815481529060010190602001808311610ad157829003601f168201915b505050505081565b6000806000806000610b073361212b565b1515610b1257600080fd5b60026004811115610b1f57fe5b600c60159054906101000a900460ff166004811115610b3a57fe5b141515610b4657600080fd5b60011515600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160149054906101000a900460ff161515141515610ba857600080fd5b60001515600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160159054906101000a900460ff161515141515610c0a57600080fd5b60009350600854600754101515610c25576007549350610c2b565b60085493505b600380811115610c3757fe5b600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff166003811115610c9257fe5b141515610cce578515610cb657600360008154809291906001019190505550610cc9565b6002600081548092919060010191905055505b610d15565b8515610cf65760095484811515610ce157fe5b04600360008282540192505081905550610d14565b60095484811515610d0357fe5b046002600082825401925050819055505b5b6004600081548092919060010191905055506001600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160156101000a81548160ff02191690831515021790555060055460045414156113b5576002546003541415610e93576000600381905550600060028190555060006004819055506001600c60156101000a81548160ff02191690836004811115610dd157fe5b0217905550600092505b600e80549050831015610e8a576000600d6000600e86815481101515610dfd57fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160156101000a81548160ff0219169083151502179055508280600101935050610ddb565b600194506113ba565b6002546003541115610ebf576001600c60006101000a81548160ff021916908315150217905550610edb565b6000600c60006101000a81548160ff0219169083151502179055505b6003600c60156101000a81548160ff02191690836004811115610efa57fe5b021790555060009150600c60009054906101000a900460ff1615610f44576008543073ffffffffffffffffffffffffffffffffffffffff1631811515610f3c57fe5b049150610f6c565b6007543073ffffffffffffffffffffffffffffffffffffffff1631811515610f6857fe5b0491505b600090505b600e805490508110156113b457600c60009054906101000a900460ff16156111f45760016003811115610fa057fe5b600d6000600e84815481101515610fb357fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff16600381111561103557fe5b148061117957506000600381111561104957fe5b600d6000600e8481548110151561105c57fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff1660038111156110de57fe5b148015611178575060011515600d6000600e848154811015156110fd57fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160149054906101000a900460ff161515145b5b156111ef57600e8181548110151561118d57fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166108fc839081150290604051600060405180830381858888f19350505050505b6113a7565b6002600381111561120157fe5b600d6000600e8481548110151561121457fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff16600381111561129657fe5b148015611330575060011515600d6000600e848154811015156112b557fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160149054906101000a900460ff161515145b156113a657600e8181548110151561134457fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166108fc839081150290604051600060405180830381858888f19350505050505b5b8080600101915050610f71565b5b600194505b50505050919050565b600c60009054906101000a900460ff1681565b600e818154811015156113e557fe5b90600052602060002090016000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b61141d6121d7565b60008054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156114b35780601f10611488576101008083540402835291602001916114b3565b820191906000526020600020905b81548152906001019060200180831161149657829003601f168201915b5050505050905090565b6000600c60159054906101000a900460ff16905090565b60006114df3361212b565b15156114ea57600080fd5b600060048111156114f757fe5b600c60159054906101000a900460ff16600481111561151257fe5b14151561151e57600080fd5b60038081111561152a57fe5b600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff16600381111561158557fe5b1415151561159257600080fd5b6001600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160146101000a81548160ff021916908315150217905515156115f757600080fd5b6001543073ffffffffffffffffffffffffffffffffffffffff16311015151561161f57600080fd5b6002600381111561162c57fe5b600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff16600381111561168757fe5b14156116b35760076000815480929190600190039190505550600154600b600082825403925050819055505b600160038111156116c057fe5b600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff16600381111561171b57fe5b14156117475760086000815480929190600190039190505550600154600b600082825403925050819055505b60038081111561175357fe5b600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff1660038111156117ae57fe5b14156117c857600960008154809291906001900391905055505b6000600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160146101000a81548160ff0219169083151502179055503373ffffffffffffffffffffffffffffffffffffffff166108fc6001549081150290604051600060405180830381858888f1935050505015156118695760009050611881565b60056000815480929190600190039190505550600190505b90565b6000600b54905090565b600c60159054906101000a900460ff1681565b6000806000806000600d6000600e888154811015156118bc57fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600d6000600e8981548110151561195957fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160149054906101000a900460ff16600d6000600e8a8154811015156119e357fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160159054906101000a900460ff16600d6000600e8b815481101515611a6d57fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160169054906101000a900460ff16600d6000600e8c815481101515611af757fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff169450945094509450945091939590929450565b60015481565b6000600c60009054906101000a900460ff16905090565b600080611baa3361212b565b1515611bb557600080fd5b60006004811115611bc257fe5b600c60159054906101000a900460ff166004811115611bdd57fe5b1480611c0e575060016004811115611bf157fe5b600c60159054906101000a900460ff166004811115611c0c57fe5b145b80611c3e575060026004811115611c2157fe5b600c60159054906101000a900460ff166004811115611c3c57fe5b145b1515611c4957600080fd5b60001515600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160169054906101000a900460ff161515141515611cab57600080fd5b60011515600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160149054906101000a900460ff161515141515611d0d57600080fd5b600a600081548092919060010191905055506001600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160166101000a81548160ff021916908315150217905550600554600a541415611ee5576004600c60156101000a81548160ff02191690836004811115611da557fe5b0217905550600090505b600e80549050811015611ee457600380811115611dc857fe5b600d6000600e84815481101515611ddb57fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160179054906101000a900460ff166003811115611e5d57fe5b141515611ed757600e81815481101515611e7357fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166108fc6001549081150290604051600060405180830381858888f19350505050505b8080600101915050611daf565b5b600191505090565b6000600654905090565b6000600154905090565b6000600c60019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515611f5f57600080fd5b60016004811115611f6c57fe5b600c60159054906101000a900460ff166004811115611f8757fe5b141515611f9357600080fd5b8115611fb057600360008154809291906001019190505550611fc3565b6002600081548092919060010191905055505b6001600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160156101000a81548160ff0219169083151502179055506004600081548092919060010191905055506002600c60156101000a81548160ff0219169083600481111561204f57fe5b021790555060019050919050565b6000600c60019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156120bb57600080fd5b600060048111156120c857fe5b600c60159054906101000a900460ff1660048111156120e357fe5b1415156120ef57600080fd5b600060075411151561210057600080fd5b6001600c60156101000a81548160ff0219169083600481111561211f57fe5b02179055506001905090565b60008073ffffffffffffffffffffffffffffffffffffffff16600d60008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161415156121cd57600190506121d2565b600090505b919050565b6020604051908101604052806000815250905600a165627a7a723058200c5cb2636f6a74a87c13a220e2dd15ea29be8a91f40bd51c425b5a5bfabef1bf0029";

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

    public RemoteCall<String> betConditions() {
        final Function function = new Function("betConditions", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
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

    public RemoteCall<String> getBetConditions() {
        final Function function = new Function("getBetConditions", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
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

    public static RemoteCall<BetChainBetContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, String _betConditions, BigInteger _betEntryFee, List<String> _participantAddresses, List<BigInteger> _participantRoles) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_betConditions), 
                new org.web3j.abi.datatypes.generated.Uint256(_betEntryFee), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.Utils.typeMap(_participantAddresses, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint8>(
                        org.web3j.abi.Utils.typeMap(_participantRoles, org.web3j.abi.datatypes.generated.Uint8.class))));
        return deployRemoteCall(BetChainBetContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor, initialWeiValue);
    }

    public static RemoteCall<BetChainBetContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, String _betConditions, BigInteger _betEntryFee, List<String> _participantAddresses, List<BigInteger> _participantRoles) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_betConditions), 
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
