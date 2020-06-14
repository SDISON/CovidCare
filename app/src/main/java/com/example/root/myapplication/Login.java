package com.example.root.myapplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import pl.droidsonroids.gif.GifImageView;

public class Login extends Fragment {

    public View root;
    private EditText textView1;
    private EditText textView2;
    private EditText editText3;
    private Button button2;
    private GifImageView gifImageView;
    public final String TAG = "Login_debugging";
    Activity activity;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.login, container, false);
        initializeView();
        return root;
    }

    public void initializeView(){
        textView1 = root.findViewById(R.id.editText);
        textView2 = root.findViewById(R.id.editText2);
        editText3 = root.findViewById(R.id.editText3);
        //textView2.setEnabled(false); //deactivate password
        button2 = root.findViewById(R.id.button2);
        button2.setOnClickListener(handler1);
        Button button1 = root.findViewById(R.id.button1);
        button1.setOnClickListener(handler2);
        button1.setEnabled(false); //deactivate create button
        gifImageView = root.findViewById(R.id.gif0);
        activity = getActivity();
        context = getContext();
    }

    View.OnClickListener handler1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String pwd = textView2.getText().toString();
            String mne = textView1.getText().toString();
            String btAddress = editText3.getText().toString().trim();
            if(pwd.length()<8){
                ErrorDialog errorDialog = new ErrorDialog();
                Bundle b = new Bundle();
                b.putString("error", "Password cannot be less than 8 characters");
                errorDialog.setArguments(b);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                errorDialog.show(fragmentTransaction, "error");
            }
            else if(mne.split(" ").length!=12){
                ErrorDialog errorDialog = new ErrorDialog();
                Bundle b = new Bundle();
                b.putString("error", "Mnemonic must be of 12 words");
                errorDialog.setArguments(b);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                errorDialog.show(fragmentTransaction, "error");
            }
            else if(btAddress.length()!=17 && btAddress.split(":").length!=6){
                ErrorDialog errorDialog = new ErrorDialog();
                Bundle b = new Bundle();
                b.putString("error", "Bt Address must be correct");
                errorDialog.setArguments(b);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                errorDialog.show(fragmentTransaction, "error");
            }
            else {
                button2.setEnabled(false);
                textView1.setEnabled(false);
                textView2.setEnabled(false);
                editText3.setEnabled(false);
                gifImageView.setVisibility(View.VISIBLE);
                web3j_func web3 = new web3j_func(Login.this, activity, mne, pwd, context);
                //web3.import_wallet();
                web3.import_wallet_metamask();
                /*String btAddress = Settings.Secure.getString(activity.getContentResolver(), "bluetooth_address");
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    try{
                        Field mServiceField = bluetoothAdapter.getClass().getDeclaredField("mService");
                        mServiceField.setAccessible(true);
                        Object btManagerService = mServiceField.get(bluetoothAdapter);
                        if(btManagerService!=null){
                            btAddress = (String) btManagerService.getClass().getMethod("getAddress").invoke(btManagerService);
                        }
                    }catch(Exception e){
                        Log.e(TAG, e.getMessage());
                    }
                }*/
                web3.connectEth(0);
                web3.execute("login", btAddress);
            }
        }
    };

    public void finish(){
        HomeFragment homeFragment = new HomeFragment();
        FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, homeFragment);
        fragmentTransaction.commit();
    }

    public void dialog(String s){
        button2.setEnabled(true);
        textView1.setEnabled(true);
        textView2.setEnabled(true);
        editText3.setEnabled(true);
        gifImageView.setVisibility(View.GONE);
        ErrorDialog errorDialog = new ErrorDialog();
        Bundle b = new Bundle();
        b.putString("error", s);
        errorDialog.setArguments(b);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        errorDialog.show(fragmentTransaction, "error");
    }

    View.OnClickListener handler2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String pwd = textView2.getText().toString();
            if(false){
                ErrorDialog errorDialog = new ErrorDialog();
                Bundle b = new Bundle();
                b.putString("error", "Password cannot be less than 8 characters");
                errorDialog.setArguments(b);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                errorDialog.show(fragmentTransaction, "error");
            }
            else {
                web3j_func web3 = new web3j_func(activity, pwd, context);
                String mnemonic = web3.createWallet();
                String address = web3.getAddress();
                String btAddress = Settings.Secure.getString(activity.getContentResolver(), "bluetooth_address");
                web3.connectEth(404);
                Wallet_details wallet_details = new Wallet_details();
                Bundle b = new Bundle();
                b.putString("mnemonic", mnemonic);
                b.putString("address", address);
                wallet_details.setArguments(b);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, wallet_details, "wallet_frag");
                fragmentTransaction.commit();
            }
        }
    };

}
