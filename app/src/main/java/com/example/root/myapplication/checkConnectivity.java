package com.example.root.myapplication;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class checkConnectivity implements Runnable {
    String url;
    Handler handler;
    Context context;
    CasesFragment casesFragment;
    LabFragment labFragment;
    private final String TAG = "Connectivity_debugging";

    public checkConnectivity(String x, Context z, CasesFragment y)
    {
        url = x;
        context = z;
        casesFragment = y;
    }

    public checkConnectivity(String x, Context y, LabFragment z)
    {
        url = x;
        context = y;
        labFragment = z;
    }

    public void start()
    {
        Log.v(TAG, "starting connection handler");
        handler = new Handler();
        handler.postDelayed(this, 0);
    }

    @Override
    public void run() {
        try {
            if(url.equals("https://api.covid19india.org/data.json") || url.equals("https://api.covid19india.org/states_daily.json")) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    handler.removeCallbacks(this);
                    casesFragment.getData(url);
                } else {
                    Toast.makeText(context, " No Internet connectivity", Toast.LENGTH_SHORT).show();
                    handler.postDelayed(this, 5000);
                }
            }
            else if(url.equals("https://api.covid19india.org/resources/resources.json")){
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    handler.removeCallbacks(this);
                    labFragment.getData(url);
                } else {
                    Toast.makeText(context, " No Internet connectivity", Toast.LENGTH_SHORT).show();
                    handler.postDelayed(this, 5000);
                }
            }
        }catch (NullPointerException e)
        {
            Log.e(TAG, e.getMessage());
            handler.postDelayed(this, 100);
        }
    }
}
