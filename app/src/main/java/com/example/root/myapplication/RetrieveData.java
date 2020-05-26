package com.example.root.myapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class RetrieveData extends AsyncTask<String, String, String> {

    String url;
    HttpURLConnection urlConnection;
    public final String TAG = "debugging";
    FetchDataCallbackInterface callbackInterface;

    public RetrieveData(String url, FetchDataCallbackInterface callbackInterface)
    {
        Log.v(TAG, "in constructor");
        this.url = url;
        this.callbackInterface = callbackInterface;
        Log.v(TAG, "pass from  constructor");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... args) {
        Log.v(TAG, "in bgd");
        StringBuilder result = new StringBuilder();
        try
        {
            URL url_ = new URL(this.url);
            urlConnection = (HttpURLConnection) url_.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while((line = reader.readLine())!=null) {
                result.append(line);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        finally{
            urlConnection.disconnect();
        }
        Log.v(TAG, "pass bgd");
        return result.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        this.callbackInterface.fetchDataCallback(url, s);
    }
}
