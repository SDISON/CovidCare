package com.example.root.myapplication;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class updateData implements Runnable {
    String url;
    Handler handler;
    Context context;
    CasesFragment casesFragment;
    LabFragment labFragment;
    private final String TAG = "updateData_debugging";

    public updateData(String x, Context z, CasesFragment y)
    {
        url = x;
        context = z;
        casesFragment = y;
    }

    public updateData(String x, Context z, LabFragment y)
    {
        url = x;
        context = z;
        labFragment = y;
    }

    public void start()
    {
        Log.v(TAG, "starting update handler");
        handler = new Handler();
        handler.postDelayed(this, 5000);
    }

    @Override
    public void run() {
        if(url.equals("https://api.covid19india.org/data.json") || url.equals("https://api.covid19india.org/states_daily.json")) {
            handler.removeCallbacks(this);
            casesFragment.getData(url);
        }
        else if(url.equals("https://api.covid19india.org/resources/resources.json")){
            handler.removeCallbacks(this);
            labFragment.getData(url);
        }
    }
}