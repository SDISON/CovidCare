package com.example.root.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Wallet_details extends Fragment {

    public View root;
    public final String TAG = "Wallet_detail_debugging";
    public TextView txt1;
    public TextView txt2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(TAG, "wallet class");
        root = inflater.inflate(R.layout.wallet_details, container, false);
        txt1 = root.findViewById(R.id.txt1);
        txt2 = root.findViewById(R.id.txt2);
        Bundle b = getArguments();
        txt1.setText(b.getString("mnemonic"));
        txt2.setText(b.getString("address"));
        Button button3 = root.findViewById(R.id.button1);
        button3.setOnClickListener(handler1);
        return root;
    }

    View.OnClickListener handler1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            HomeFragment homeFragment = new HomeFragment();
            FragmentManager fragmentManager = getFragmentManager();
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, homeFragment);
            fragmentTransaction.commit();
        }
    };
}
