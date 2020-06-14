package com.example.root.myapplication;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.16.
 */
@SuppressWarnings("rawtypes")
public class CovidSafe_sol_CovidSafe extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50600380546001600160a01b031916331790556110ad806100326000396000f3fe608060405234801561001057600080fd5b50600436106100935760003560e01c80636d26fa3b116100665780636d26fa3b146100ea578063781e4bef146100f25780638da5cb5b1461011857806391b678d71461013c578063f2c298be146102ea57610093565b806311db785a146100985780631865c57d146100b25780634c0a162d146100bc5780635ef5f948146100c4575b600080fd5b6100a061038e565b60408051918252519081900360200190f35b6100ba610394565b005b6100a06104d9565b6100ba600480360360408110156100da57600080fd5b508035906020013560ff166104df565b6100a061059f565b6100ba6004803603602081101561010857600080fd5b50356001600160a01b03166105a5565b610120610638565b604080516001600160a01b039092168252519081900360200190f35b6100ba6004803603606081101561015257600080fd5b810190602081018135600160201b81111561016c57600080fd5b82018360208201111561017e57600080fd5b803590602001918460018302840111600160201b8311171561019f57600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295949360208101935035915050600160201b8111156101f157600080fd5b82018360208201111561020357600080fd5b803590602001918460018302840111600160201b8311171561022457600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295949360208101935035915050600160201b81111561027657600080fd5b82018360208201111561028857600080fd5b803590602001918460018302840111600160201b831117156102a957600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610647945050505050565b6100ba6004803603602081101561030057600080fd5b810190602081018135600160201b81111561031a57600080fd5b82018360208201111561032c57600080fd5b803590602001918460018302840111600160201b8311171561034d57600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295506107f4945050505050565b60005481565b3360009081526008602052604090205460ff166103fd57604080516020808252600a9082015269796f7520617265206f6b60b01b8183015290517f657c8f8737e140ab246227a836d782705d68d909f2a19553fdeddf29d7b0a0919181900360600190a16104d7565b3360009081526008602052604090205460ff1660011415610468577f657c8f8737e140ab246227a836d782705d68d909f2a19553fdeddf29d7b0a09160405180806020018281038252602c815260200180611026602c913960400191505060405180910390a16104d7565b3360009081526008602052604090205460ff16600214156104d7576040805160208082526010908201526f796f752061726520706f73697469766560801b8183015290517f657c8f8737e140ab246227a836d782705d68d909f2a19553fdeddf29d7b0a0919181900360600190a15b565b60015481565b6003546001600160a01b031633146104f657600080fd5b8060ff16600114801561050a575060025482145b1561059b576000828152600960205260409020805460019091015461053b916001600160a01b039081169116610a07565b600280546001019055604080516020808252601390820152726c696e6b6564207375636365737366756c6c7960681b8183015290517f1f2fa6e389d837c2b0b31b0a34df1ab3647861fcce3bb470a588c55f113a6adc9181900360600190a15b5050565b60025481565b6003546001600160a01b031633146105bc57600080fd5b6001600160a01b038116600090815260086020908152604091829020805460ff1916600217905581518181526010918101919091526f736574207375636365737366756c6c7960801b8183015290517f87be5773378512d22edc06a49b8674602b78ec292ba1aea47ead2767f19305219181900360600190a150565b6003546001600160a01b031681565b3360009081526005602052604090205460ff1660011461066657600080fd5b600061067184610b3c565b90506001600160a01b03811615610795576001600160a01b038116600090815260076020908152604080832033845260010190915290205460ff16610722576106bd8184846001610bad565b604080516020808252601c908201527f43616c6c207375636365737366756c6c792072656769737465726564000000008183015290517f1ef29226a71ebfcaaebab6685fa018130205c48de9409c293d986311a6d1caf59181900360600190a1610790565b61072f8184846002610bad565b604080516020808252601d908201527f43616c6c207375636365737366756c6c792061636b6e6f776c656765640000008183015290517f1ef29226a71ebfcaaebab6685fa018130205c48de9409c293d986311a6d1caf59181900360600190a15b6107ee565b6040805160208082526015908201527411195d9a58d9481b9bdd081c9959da5cdd195c9959605a1b8183015290517f1ef29226a71ebfcaaebab6685fa018130205c48de9409c293d986311a6d1caf59181900360600190a15b50505050565b60006001600160a01b03166004826040518082805190602001908083835b602083106108315780518252601f199092019160209182019101610812565b51815160209384036101000a60001901801990921691161790529201948552506040519384900301909220546001600160a01b031692909214915061099a9050576004816040518082805190602001908083835b602083106108a45780518252601f199092019160209182019101610885565b51815160209384036101000a60001901801990921691161790529201948552506040519384900301909220546001600160a01b031633141591506109499050576040805160208082526019908201527f44657669636520616c72656164792072656769737465726564000000000000008183015290517f043a7333b00239b949a1ee3b1542cd8e3bb7cda5b41d97a661780dfa1bf891d49181900360600190a1610995565b7f043a7333b00239b949a1ee3b1542cd8e3bb7cda5b41d97a661780dfa1bf891d46040518080602001828103825260268152602001806110526026913960400191505060405180910390a15b610a04565b6109a381610df1565b604080516020808252601e908201527f446576696365207375636365737366756c6c79207265676973746572656400008183015290517f043a7333b00239b949a1ee3b1542cd8e3bb7cda5b41d97a661780dfa1bf891d49181900360600190a15b50565b6001600160a01b0380831660009081526006602090815260408083209385168352600193840190915290205460ff1614610aa3576001600160a01b03808316600081815260066020818152604080842095871680855260018781018452918520805460ff191683179055928252600286018054808301825590855291842090910180546001600160a01b03191690921790915591905281540190555b6001600160a01b0380821660009081526006602090815260408083209386168352600193840190915290205460ff161461059b576001600160a01b0390811660008181526006602081815260408084209690951680845260018088018352958420805460ff19168717905591815260028601805480870182559084529083200180546001600160a01b0319169091179055528154019055565b60006004826040518082805190602001908083835b60208310610b705780518252601f199092019160209182019101610b51565b51815160209384036101000a60001901801990921691161790529201948552506040519384900301909220546001600160a01b0316949350505050565b8060ff1660011415610c54573360009081526007602090815260408083206001600160a01b0388168452600190810183529220805460ff1916831781558551610bfd939190910191860190610f8a565b503360009081526007602090815260408083206001600160a01b038816845260010182529091208351610c3892600290920191850190610f8a565b50336000908152600760205260409020805460010190556107ee565b6001600160a01b038416600081815260076020818152604080842033855260018082018452828620805460ff191690559383528054600019908101909155818520958552948301825292839020820180548451600261010095831615959095029096011692909204601f8101829004820285018201909352828452610de793879387939192830182828015610d2a5780601f10610cff57610100808354040283529160200191610d2a565b820191906000526020600020905b815481529060010190602001808311610d0d57829003601f168201915b50503360009081526007602090815260408083206001600160a01b038f16845260019081018352928190206002908101805483519581161561010002600019011691909104601f8101849004840285018401909252818452929550919350909150830182828015610ddc5780601f10610db157610100808354040283529160200191610ddc565b820191906000526020600020905b815481529060010190602001808311610dbf57829003601f168201915b505050505088610e95565b6107ee8433610a07565b336004826040518082805190602001908083835b60208310610e245780518252601f199092019160209182019101610e05565b51815160209384036101000a60001901801990921691161790529201948552506040805194859003820190942080546001600160a01b0319166001600160a01b039690961695909517909455505033600090815260059092528120805460ff19166001908117909155815401905550565b6040805160c0810182526001600160a01b0380841682523360208084019182528385018a815260608501899052608085018a905260a085018890526001805460009081526009845296909620855181549086166001600160a01b03199182161782559351968101805497909516969093169590951790925592518051929392610f249260028501920190610f8a565b5060608201518051610f40916003840191602090910190610f8a565b5060808201518051610f5c916004840191602090910190610f8a565b5060a08201518051610f78916005840191602090910190610f8a565b50506001805481019055505050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610fcb57805160ff1916838001178555610ff8565b82800160010185558215610ff8579182015b82811115610ff8578251825591602001919060010190610fdd565b50611004929150611008565b5090565b61102291905b80821115611004576000815560010161100e565b9056fe416c6572742120536f6d656f6e6520796f7520636f6e746163746564207475726e656420706f7369746976654465766963652072656769737465726564207769746820616e6f746865722061646472657373a2646970667358221220cfff2eaeb3bb0100f1485c04935ee5512b82168892144b08dc5a785a5b543e2064736f6c63430006090033";

    public static final String FUNC_GETSTATE = "getState";

    public static final String FUNC_LISTENER = "listener";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_REGISTER = "register";

    public static final String FUNC_SET_POSITIVE = "set_positive";

    public static final String FUNC_TOTAL_ACCOUNT = "total_account";

    public static final String FUNC_TOTAL_CHECK = "total_check";

    public static final String FUNC_TOTAL_CHECKED = "total_checked";

    public static final String FUNC_UPDATE_LINKING = "update_linking";

    public static final Event DISTANCE_LOG_EVENT = new Event("distance_log", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event LINKING_LOG_EVENT = new Event("linking_log", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    public static final Event LISTENER_LOG_EVENT = new Event("listener_log", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    public static final Event POSITIVE_LOG_EVENT = new Event("positive_log", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    public static final Event REGISTER_LOG_EVENT = new Event("register_log", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    public static final Event STATE_LOG_EVENT = new Event("state_log", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected CovidSafe_sol_CovidSafe(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected CovidSafe_sol_CovidSafe(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected CovidSafe_sol_CovidSafe(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected CovidSafe_sol_CovidSafe(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<Distance_logEventResponse> getDistance_logEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(DISTANCE_LOG_EVENT, transactionReceipt);
        ArrayList<Distance_logEventResponse> responses = new ArrayList<Distance_logEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            Distance_logEventResponse typedResponse = new Distance_logEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.lat1 = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.lon1 = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.lat2 = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.lon2 = (String) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<Distance_logEventResponse> distance_logEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, Distance_logEventResponse>() {
            @Override
            public Distance_logEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(DISTANCE_LOG_EVENT, log);
                Distance_logEventResponse typedResponse = new Distance_logEventResponse();
                typedResponse.log = log;
                typedResponse.lat1 = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.lon1 = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.lat2 = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.lon2 = (String) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<Distance_logEventResponse> distance_logEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DISTANCE_LOG_EVENT));
        return distance_logEventFlowable(filter);
    }

    public List<Linking_logEventResponse> getLinking_logEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(LINKING_LOG_EVENT, transactionReceipt);
        ArrayList<Linking_logEventResponse> responses = new ArrayList<Linking_logEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            Linking_logEventResponse typedResponse = new Linking_logEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.status = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<Linking_logEventResponse> linking_logEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, Linking_logEventResponse>() {
            @Override
            public Linking_logEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(LINKING_LOG_EVENT, log);
                Linking_logEventResponse typedResponse = new Linking_logEventResponse();
                typedResponse.log = log;
                typedResponse.status = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<Linking_logEventResponse> linking_logEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LINKING_LOG_EVENT));
        return linking_logEventFlowable(filter);
    }

    public List<Listener_logEventResponse> getListener_logEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(LISTENER_LOG_EVENT, transactionReceipt);
        ArrayList<Listener_logEventResponse> responses = new ArrayList<Listener_logEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            Listener_logEventResponse typedResponse = new Listener_logEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.status = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<Listener_logEventResponse> listener_logEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, Listener_logEventResponse>() {
            @Override
            public Listener_logEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(LISTENER_LOG_EVENT, log);
                Listener_logEventResponse typedResponse = new Listener_logEventResponse();
                typedResponse.log = log;
                typedResponse.status = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<Listener_logEventResponse> listener_logEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LISTENER_LOG_EVENT));
        return listener_logEventFlowable(filter);
    }

    public List<Positive_logEventResponse> getPositive_logEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(POSITIVE_LOG_EVENT, transactionReceipt);
        ArrayList<Positive_logEventResponse> responses = new ArrayList<Positive_logEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            Positive_logEventResponse typedResponse = new Positive_logEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.status = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<Positive_logEventResponse> positive_logEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, Positive_logEventResponse>() {
            @Override
            public Positive_logEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(POSITIVE_LOG_EVENT, log);
                Positive_logEventResponse typedResponse = new Positive_logEventResponse();
                typedResponse.log = log;
                typedResponse.status = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<Positive_logEventResponse> positive_logEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(POSITIVE_LOG_EVENT));
        return positive_logEventFlowable(filter);
    }

    public List<Register_logEventResponse> getRegister_logEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(REGISTER_LOG_EVENT, transactionReceipt);
        ArrayList<Register_logEventResponse> responses = new ArrayList<Register_logEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            Register_logEventResponse typedResponse = new Register_logEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.status = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<Register_logEventResponse> register_logEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, Register_logEventResponse>() {
            @Override
            public Register_logEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(REGISTER_LOG_EVENT, log);
                Register_logEventResponse typedResponse = new Register_logEventResponse();
                typedResponse.log = log;
                typedResponse.status = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<Register_logEventResponse> register_logEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REGISTER_LOG_EVENT));
        return register_logEventFlowable(filter);
    }

    public List<State_logEventResponse> getState_logEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(STATE_LOG_EVENT, transactionReceipt);
        ArrayList<State_logEventResponse> responses = new ArrayList<State_logEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            State_logEventResponse typedResponse = new State_logEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.status = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<State_logEventResponse> state_logEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, State_logEventResponse>() {
            @Override
            public State_logEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(STATE_LOG_EVENT, log);
                State_logEventResponse typedResponse = new State_logEventResponse();
                typedResponse.log = log;
                typedResponse.status = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<State_logEventResponse> state_logEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(STATE_LOG_EVENT));
        return state_logEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> getState() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_GETSTATE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> listener(String btAddress, String lat, String log) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_LISTENER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(btAddress), 
                new org.web3j.abi.datatypes.Utf8String(lat), 
                new org.web3j.abi.datatypes.Utf8String(log)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> owner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> register(String btAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REGISTER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(btAddress)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> set_positive(String addr) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SET_POSITIVE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, addr)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> total_account() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TOTAL_ACCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> total_check() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TOTAL_CHECK, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> total_checked() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TOTAL_CHECKED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> update_linking(BigInteger id, BigInteger flag) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_UPDATE_LINKING, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(id), 
                new org.web3j.abi.datatypes.generated.Uint8(flag)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static CovidSafe_sol_CovidSafe load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new CovidSafe_sol_CovidSafe(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static CovidSafe_sol_CovidSafe load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new CovidSafe_sol_CovidSafe(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static CovidSafe_sol_CovidSafe load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new CovidSafe_sol_CovidSafe(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static CovidSafe_sol_CovidSafe load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new CovidSafe_sol_CovidSafe(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<CovidSafe_sol_CovidSafe> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(CovidSafe_sol_CovidSafe.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<CovidSafe_sol_CovidSafe> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(CovidSafe_sol_CovidSafe.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<CovidSafe_sol_CovidSafe> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(CovidSafe_sol_CovidSafe.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<CovidSafe_sol_CovidSafe> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(CovidSafe_sol_CovidSafe.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class Distance_logEventResponse extends BaseEventResponse {
        public String lat1;

        public String lon1;

        public String lat2;

        public String lon2;
    }

    public static class Linking_logEventResponse extends BaseEventResponse {
        public String status;
    }

    public static class Listener_logEventResponse extends BaseEventResponse {
        public String status;
    }

    public static class Positive_logEventResponse extends BaseEventResponse {
        public String status;
    }

    public static class Register_logEventResponse extends BaseEventResponse {
        public String status;
    }

    public static class State_logEventResponse extends BaseEventResponse {
        public String status;
    }
}
