package com.example.root.myapplication;


import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class Spalsh extends Fragment implements Runnable {

    private static final String TAG = "Splash_debugging";
    public View root;
    public Handler handler;
    public BottomNavigationView bottomNavigationView;
    private AppCompatActivity activity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.splash, container, false);
        activity = (AppCompatActivity) getActivity();
        Window window =  activity.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        bottomNavigationView = activity.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);
        androidx.appcompat.app.ActionBar actionBar = activity.getSupportActionBar();
        actionBar.hide();
        start();
        return root;
    }

    public void start()
    {
        handler = new Handler();
        handler.postDelayed(this, 3000);
    }

    @Override
    public void run() {
        String logged = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("wallet_file", "");
        String pwd = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("pwd", "");
        web3j_func web3 = new web3j_func();
        if(!web3.checkFile(logged) && pwd.equals("")) {
            Login loginFragment = new Login();
            FragmentManager fragmentManager = getFragmentManager();
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, loginFragment);
            fragmentTransaction.commit();
        }
        else{
            HomeFragment homeFragment = new HomeFragment();
            FragmentManager fragmentManager = getFragmentManager();
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, homeFragment);
            fragmentTransaction.commit();
        }
    }
}
