package com.example.root.myapplication;


import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kenai.jffi.Main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    public ImageButton labs;
    public TextView status;
    public TextView time;
    public ImageButton call;
    private AppCompatActivity activity;
    public BottomNavigationView bottomNavigationView;
    View root;
    public static final String TAG = "HomeFragment_debugging";
    public ListView notfy_list;
    public ArrayList<String> notfyItems;
    public ArrayAdapter<String> adapter2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.nav_home, container, false);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeView();
        status.setOnClickListener(handler3);
        if(status.getText().toString().equals("Checking Status...")) {
            web3j_func web3 = new web3j_func(HomeFragment.this, getContext());
            web3.connectEth(1);
            web3.execute("status");
        }
    }

    public void initializeView(){
        activity = (AppCompatActivity) getActivity();
        Window window =  activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        bottomNavigationView = activity.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        androidx.appcompat.app.ActionBar actionBar = activity.getSupportActionBar();
        actionBar.show();
        time = root.findViewById(R.id.time);
        status = root.findViewById(R.id.status);
        labs = root.findViewById(R.id.img3);
        labs.setOnClickListener(handler1);
        BottomNavigationView bottomNavigationMenu = getActivity().findViewById(R.id.bottom_navigation);
        MenuItem temp = bottomNavigationMenu.getMenu().findItem(R.id.nav_home);
        temp.setChecked(true);
        call = root.findViewById(R.id.img4);
        call.setOnClickListener(handler2);
        notfy_list = root.findViewById(R.id.notfy_list);
        notfyItems = new ArrayList<>();
        adapter2 = new ArrayAdapter<String>(getActivity(), R.layout.list_item, notfyItems);
        notfy_list.setAdapter(adapter2);
        String stat = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("status", "");
        String tmt = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("time", "");
        time.setText(tmt);
        if(!stat.equals(""))
            status.setText(stat);
        else
            status.setText("Checking Status ...");
        addNotification();
    }

    public void addNotification(){
        notfyItems.clear();
        notfyItems.add("Currently nearby devices");
        String temp[] = MainActivity.data.split("\n");
        Collections.addAll(notfyItems, temp);
        adapter2.notifyDataSetChanged();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addNotification();
            }
        }, 10000);
    }

    public void settingStatus(String s)
    {
        status.setText(s);
        String temp = status.getText().toString();
        Date dt = Calendar.getInstance().getTime();
        SimpleDateFormat smp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String tmp = smp.format(dt);
        if(!temp.equals("No internet connectivity")) {
            time.setText(tmp);
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString("status", temp).apply();
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString("time", tmp).apply();
        }
    }

    View.OnClickListener handler1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LabFragment labFragment = new LabFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, labFragment, "Lab_frag");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    };

    View.OnClickListener handler2 =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:1947"));
            startActivity(intent);
        }
    };

    View.OnClickListener handler3 =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            web3j_func web3 = new web3j_func(HomeFragment.this, getContext());
            web3.connectEth(1);
            web3.execute("status");
        }
    };

    @Override
    public void onClick(View v) {

    }
}
