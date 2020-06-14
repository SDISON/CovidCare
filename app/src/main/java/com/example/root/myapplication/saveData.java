package com.example.root.myapplication;

import android.content.Context;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Collections;

public class saveData {

    private String url;
    private String result;
    private Context context;
    private final String TAG = "saveData_debugging";

    public saveData(String x, String y, Context z)
    {
        url = x;
        result = y;
        context = z;
    }

    public void saving()
    {
        Log.v(TAG, "saving data to shared preference");
        if(url.equals("https://api.covid19india.org/data.json")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    PreferenceManager.getDefaultSharedPreferences(context).edit().putString("jsonString1", result).apply();
                }
            }, 0);
        }
        else if(url.equals("https://api.covid19india.org/states_daily.json")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    PreferenceManager.getDefaultSharedPreferences(context).edit().putString("jsonString2", result).apply();
                }
            }, 0);
        }
        else if(url.equals("https://api.covid19india.org/resources/resources.json")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    PreferenceManager.getDefaultSharedPreferences(context).edit().putString("jsonString3", result).apply();
                }
            }, 0);
        }
        Log.v(TAG, "data saved successfully");
    }
}
