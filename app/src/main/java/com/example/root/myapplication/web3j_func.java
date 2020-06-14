package com.example.root.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.abi.datatypes.Int;
import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.io.File;
import java.security.Provider;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class web3j_func extends AsyncTask<String, String, String> {

    private Web3j web3;
    private String walletPath;
    private String password;
    private String mnemonic;
    private String fileName;
    public final String TAG = "web3j_debugging";
    public Context context;
    private Login login;
    private HomeFragment homeFragment;
    private MyService myService;
    public final String contract_address = "0x978e7aa44e84a90858a0c002248e1755a94d4f4f";
    //public static int HARDENED_BIT = 0x80000000;

    web3j_func(){

    }

    web3j_func(MyService myService, Context context){
        this.myService = myService;
        this.context = context;
    }

    web3j_func(HomeFragment homeFragment, Context context) {
        this.homeFragment = homeFragment;
        this.context = context;
    }

    web3j_func(Activity activity, String pwd, Context context)  //for create wallet
    {
        walletPath =  activity.getFilesDir().getAbsolutePath();
        password = pwd;
        this.context = context;
        setupBouncyCastle();
    }

    web3j_func(Login obj, Activity activity, String mne0, String pwd, Context context){
        login = obj;
        walletPath =  activity.getFilesDir().getAbsolutePath();
        password = pwd;
        mnemonic = mne0;
        this.context = context;
        setupBouncyCastle();
    }

    private void setupBouncyCastle() {
        final Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
        if (provider == null) {
            // Web3j will set up the provider lazily when it's first used.
            return;
        }
        if (provider.getClass().equals(BouncyCastleProvider.class)) {
            // BC with same package name, shouldn't happen in real life.
            return;
        }
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }

    public boolean checkFile(String path){
        try {
            if (new File(path).exists()) {
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }

    public void saveFile(){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("wallet_file", walletPath+"/"+fileName).apply();
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("pwd", password).apply();
    }

    public void connectEth(int flag)
    {
        Log.v(TAG, "Connecting to Ethereum network...");
        // FIXME: Add your own API key here
        web3 = Web3j.build(new HttpService("https://rinkeby.infura.io/v3/61d966b8e53a4f69900202268da29f75"));
        try {
            Web3ClientVersion clientVersion = web3.web3ClientVersion().sendAsync().get();
            if(!clientVersion.hasError()){
                Log.v(TAG, "Connected!");
            }
            else {
                Log.v(TAG, clientVersion.getError().getMessage());
                if(flag == 0){
                    login.dialog("No internet connectivity");
                }
                else if(flag == 1){
                    homeFragment.settingStatus("No internet connectivity");
                }
            }
        } catch (Exception e) {
            Log.v(TAG, e.getMessage());
        }
    }

    public String createWallet(){

        try{
            Bip39Wallet wallet =  WalletUtils.generateBip39Wallet(password,new File(walletPath));
            Log.v(TAG, "Wallet generated");
            fileName = wallet.getFilename();
            Log.v(TAG, fileName);
            mnemonic = wallet.getMnemonic();
            saveFile();
            return mnemonic;
        }
        catch (Exception e){
            Log.v(TAG, e.toString());
        }
        return "";
    }

    public void import_wallet()
    {
        Credentials credentials = WalletUtils.loadBip39Credentials(password, mnemonic);
        Log.e(TAG, credentials.getAddress());
        try {
            fileName = WalletUtils.generateWalletFile(password, credentials.getEcKeyPair(), new File(walletPath), false);
            saveFile();
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
        getAddress();
    }

    public void import_wallet_metamask(){
        int[] derivationPath = {44 | Bip32ECKeyPair.HARDENED_BIT, 60 | Bip32ECKeyPair.HARDENED_BIT, 0 | Bip32ECKeyPair.HARDENED_BIT, 0,0};

// Generate a BIP32 master keypair from the mnemonic phrase
        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(MnemonicUtils.generateSeed(mnemonic, null));

// Derive the keypair using the derivation path
        Bip32ECKeyPair  derivedKeyPair = Bip32ECKeyPair.deriveKeyPair(masterKeypair, derivationPath);

// Load the wallet for the derived keypair
        Credentials credentials = Credentials.create(derivedKeyPair);
        Log.e(TAG, credentials.getAddress());
        try {
            fileName = WalletUtils.generateWalletFile(password, credentials.getEcKeyPair(), new File(walletPath), false);
            saveFile();
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
    }

    public String getAddress(){
        try {
            Credentials credentials = WalletUtils.loadCredentials(password, walletPath+"/"+fileName);
            Log.v(TAG, credentials.getAddress());
            return credentials.getAddress();
        }
        catch (Exception e){
            Log.v(TAG, e.getMessage());
        }
        return "";
    }

    @Override
    protected String doInBackground(String... strings) {
        String status = "";
        if(strings[0].equals("login")) {
            try {
                String logged = PreferenceManager.getDefaultSharedPreferences(context).getString("wallet_file", "");
                String pwd = PreferenceManager.getDefaultSharedPreferences(context).getString("pwd", "");
                Credentials credentials = WalletUtils.loadCredentials(pwd, logged);
                CovidSafe_sol_CovidSafe contract = CovidSafe_sol_CovidSafe.load(contract_address, web3, credentials, new DefaultGasProvider());
                TransactionReceipt transactionReceipt = contract.register(strings[1]).send();
                List<CovidSafe_sol_CovidSafe.Register_logEventResponse> log = contract.getRegister_logEvents(transactionReceipt);
                status = log.get(0).status;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage() + "");
            }
        }
        else if(strings[0].equals("status")){
            try {
                String logged = PreferenceManager.getDefaultSharedPreferences(context).getString("wallet_file", "");
                String pwd = PreferenceManager.getDefaultSharedPreferences(context).getString("pwd", "");
                Credentials credentials = WalletUtils.loadCredentials(pwd, logged);
                CovidSafe_sol_CovidSafe contract = CovidSafe_sol_CovidSafe.load(contract_address, web3, credentials, new DefaultGasProvider());
                TransactionReceipt transactionReceipt = contract.getState().send();
                List<CovidSafe_sol_CovidSafe.State_logEventResponse> log = contract.getState_logEvents(transactionReceipt);
                status = log.get(0).status;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage() + "");
            }
        }
        else if(strings[0].equals("listener")){
            try {
                String btAddress = strings[1];
                String lat = strings[2];
                String lon = strings[3];
                String id = strings[4];
                String logged = PreferenceManager.getDefaultSharedPreferences(context).getString("wallet_file", "");
                String pwd = PreferenceManager.getDefaultSharedPreferences(context).getString("pwd", "");
                Credentials credentials = WalletUtils.loadCredentials(pwd, logged);
                CovidSafe_sol_CovidSafe contract = CovidSafe_sol_CovidSafe.load(contract_address, web3, credentials, new DefaultGasProvider());
                TransactionReceipt transactionReceipt = contract.listener(btAddress, lat, lon).send();
                List<CovidSafe_sol_CovidSafe.Listener_logEventResponse> log = contract.getListener_logEvents(transactionReceipt);
                String hash = transactionReceipt.getTransactionHash();
                status = log.get(0).status;
                status += ":" + id;
                status += ":" + hash;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage() + "");
            }
        }
        return status;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.v(TAG, s);
        String arr[] = s.split(":");
        s = arr[0];
        if(s.equals("Device successfully registered") || s.equals("Device already registered")) {
            login.finish();
        }
        else if(s.equals("Device registered with another address")){
            login.dialog(s);
        }
        else if(s.equals("you are ok") || s.equals("Alert! Someone you contacted turned positive") || s.equals("you are positive")){
            homeFragment.settingStatus(s);
        }
        else if(s.equals("Call successfully registered") || s.equals("Call successfully acknowleged") || s.equals("Device not registered")){
            myService.remove(s, Integer.parseInt(arr[1]), arr[2]);
        }
    }
}
//TransactionReceipt transactionReceipt = contract.register(btAddress).send();
//List<CovidSafe_sol_CovidSafe.Register_logEventResponse> log = contract.getRegister_logEvents(transactionReceipt);
//Log.v(TAG, log.get(0).status);
//RemoteCall<TransactionReceipt> remoteCall = contract.register(btAddress);
//TransactionReceipt transactionReceipt = remoteCall.send();
            /*java8.util.concurrent.CompletableFuture<TransactionReceipt> transactionReceiptCompletableFuture =  remoteCall.sendAsync();
            transactionReceiptCompletableFuture.thenAccept(transactionReceipt -> {
                List<CovidSafe_sol_CovidSafe.Register_logEventResponse> log = contract.getRegister_logEvents(transactionReceipt);
                Log.v(TAG, "hello");
                Log.v(TAG, log.get(0).status);
            }).exceptionally(transactionReceipt -> {
                return null;
            });*/
            /*List<CovidSafe_sol_CovidSafe.Register_logEventResponse> log = contract.getRegister_logEvents(transactionReceipt);
            Log.v(TAG, "hello");
            Log.v(TAG, log.get(0).status);*/
