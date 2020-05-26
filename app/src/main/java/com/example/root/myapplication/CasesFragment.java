package com.example.root.myapplication;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;

public class CasesFragment extends Fragment implements FetchDataCallbackInterface, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    static class obj{
        String a;
        String b;
        String c;
        String d;
        public obj(String w, String x, String y, String z)
        {
            a = w;
            b = x;
            c = y;
            d = z;
        }
    }
    static class obj2{
        String a;
        String b;
        String c;
        public obj2(String x, String y, String z)
        {
            a = x;
            b = y;
            c = z;
        }
    }
    String[] states = { "All States", "Maharashtra", "Gujarat", "Tamil Nadu", "Delhi", "Rajasthan", "Madhya Pradesh", "Uttar Pradesh", "West Bengal", "Andhra Pradesh", "Punjab", "Telangana", "Bihar", "Jammu and Kashmir", "Karnataka", "Haryana", "Odisha", "Kerala", "Jharkhand", "Chandigarh", "Tripura", "Assam", "Uttarakhand", "Himachal Pradesh", "Chhattisgarh", "Ladakh", "Andaman and Nicobar", "Goa", "Puducherry","Meghalaya", "Manipur", "Mizoram", "Arunachal Pradesh", "Dadra & Nagar Haveli", "Nagaland", "Lakshadweep", "Sikkim"};
    String [] types = {"Confirmed", "Recovered", "Deceased"};
    String [][] dict = {{"All States", "tt"},{"Maharashtra", "mh"}, {"Gujarat", "gj"}, {"Tamil Nadu", "tn"}, {"Delhi", "dl"}, {"Rajasthan", "rj"}, {"Madhya Pradesh", "mp"}, {"Uttar Pradesh", "up"}, {"West Bengal", "wb"}, {"Andhra Pradesh", "ap"}, {"Punjab", "pb"}, {"Telangana", "tg"}, {"Bihar", "br"}, {"Jammu and Kashmir", "jk"}, {"Karnataka", "ka"}, {"Haryana", "hr"}, {"Odisha", "or"}, {"Kerala", "kl"}, {"Jharkhand", "jh"}, {"Chandigarh", "ch"}, {"Tripura", "tr"}, {"Assam", "as"}, {"Uttarakhand", "ut"}, {"Himachal Pradesh", "hp"}, {"Chhattisgarh", "ct"}, {"Ladakh", "la"}, {"Andaman and Nicobar", "an"}, {"Goa", "ga"}, {"Puducherry", "py"},{"Meghalaya", "ml"}, {"Manipur", "mn"}, {"Mizoram", "mz"}, {"Arunachal Pradesh", "ar"}, {"Dadra & Nagar Haveli", "dn"}, {"Nagaland", "nl"}, {"Lakshadweep", "ld"}, {"Sikkim", "sk"}};
    String [] codes = {"tt", "an", "ap", "ar", "as", "br", "ch", "ct", "dd", "dl", "dn", "ga", "gj", "hp", "hr", "jh", "jk", "ka", "kl", "la", "ld", "mh", "ml", "mn", "mp", "mz", "nl", "or", "pb", "py", "rj", "sk", "tg", "tn", "tr", "tt", "up", "ut", "wb"};
    private static final String TAG = "debugging";
    private TextView totalCases;
    private TextView activeCases;
    private TextView recoveredCases;
    private TextView deathCases;
    private HashMap<String,obj> state_mp;
    private HashMap<Integer, HashMap<String, obj2>> daily_mp;
    public GifImageView gif_cases2;
    public Spinner spinner;
    public Spinner spinner2;
    public GraphView graph;
    public int counter = 0;
    public int cc = 0;
    public Switch switch_list;
    public String item1 = "All States";
    public String item2 = "Confirmed";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Arrays.sort(states);
        View root =  inflater.inflate(R.layout.nav_cases, container, false);
        totalCases = root.findViewById(R.id.totalCases);
        activeCases = root.findViewById(R.id.activeCases);
        recoveredCases = root.findViewById(R.id.recoveredCases);
        deathCases = root.findViewById(R.id.deathCases);
        gif_cases2 = root.findViewById(R.id.gif_cases2);
        graph = root.findViewById(R.id.graph);
        switch_list = root.findViewById(R.id.switch1);
        switch_list.setEnabled(false);

        checkConnectivity();

        //Creating the ArrayAdapter instance having the country list
        spinner = root.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, states);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setEnabled(false);

        spinner2 = root.findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, types);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);
        spinner2.setEnabled(false);
        return root;
    }

    public void checkConnectivity()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!= null && networkInfo.isConnected())
            getData();
        else
            Toast.makeText(getActivity(), " No Internet connectivity", Toast.LENGTH_SHORT).show();
    }

    public void getData()
    {
        gif_cases2.setVisibility(View.VISIBLE);
        new RetrieveData("https://api.covid19india.org/data.json", this).execute();
        new RetrieveData("https://api.covid19india.org/states_daily.json", this).execute();
    }

    @Override
    public void fetchDataCallback(String url, String result) {
        JSONObject jsonObject;
        if (url.equals("https://api.covid19india.org/data.json")) {
            Log.v(TAG, "for first url");
            try {
                jsonObject = new JSONObject(result);
                state_mp = new HashMap<>();
                JSONArray array = jsonObject.getJSONArray("statewise");
                for (int i = 0; i < array.length(); i++) {
                    String s = array.getJSONObject(i).getString("state");
                    String w = array.getJSONObject(i).getString("confirmed");
                    String x = array.getJSONObject(i).getString("active");
                    String y = array.getJSONObject(i).getString("recovered");
                    String z = array.getJSONObject(i).getString("deaths");
                    if(s.equals("Total"))
                        state_mp.put("All States", new obj(w, x, y, z));
                    else if(s.equals("Dadra and Nagar Haveli and Daman and Diu"))
                        state_mp.put("Dadra & Nagar Haveli", new obj(w, x, y, z));
                    else if(s.equals("Andaman and Nicobar Islands"))
                        state_mp.put("Andaman and Nicobar", new obj(w, x, y, z));
                    else
                        state_mp.put(s, new obj(w, x, y, z));
                }
                daily_mp = new HashMap<>();
                array = jsonObject.getJSONArray("cases_time_series");
                for (int i = 0; i < array.length(); i++) {
                    HashMap<String, obj2> inner = new HashMap<>();
                    String x = array.getJSONObject(i).getString("dailyconfirmed");
                    String y = array.getJSONObject(i).getString("dailyrecovered");
                    String z = array.getJSONObject(i).getString("dailydeceased");
                    inner.put("tt", new obj2(x, y, z));
                    daily_mp.put(counter, inner);
                    counter += 1;
                }
                //Log.e(TAG, "values imp from url1" + Integer.toString(counter) +" "+ Integer.toString(cc));
            } catch (JSONException err) {
                Log.d("Error", err.toString());
            }
        } else if (url.equals("https://api.covid19india.org/states_daily.json")) {
            Log.v(TAG, "For second url");
            try {
                cc = counter;
                jsonObject = new JSONObject(result);
                JSONArray array = jsonObject.getJSONArray("states_daily");
                for (int i = 0; i < array.length(); i+=3) {
                    //String s = array.getJSONObject(i).getString("date");
                    HashMap<String, obj2> inner = new HashMap<>();
                    for (String code : codes) {
                        String x = array.getJSONObject(i).getString(code);
                        String y = array.getJSONObject(i+1).getString(code);
                        String z = array.getJSONObject(i+2).getString(code);
                        inner.put(code, new obj2(x, y, z));
                    }
                    daily_mp.put(cc, inner);
                    cc += 1;
                }
            }catch (JSONException err) {
                Log.d("Error", err.toString());
            }
            switch_list.setEnabled(true);
            gif_cases2.setVisibility(View.GONE);
            renderData(item1);
            createGraph_state(item1, item2);
            switch_list.setOnCheckedChangeListener(this);
            spinner.setEnabled(true);
            spinner2.setEnabled(true);
        }
    }

    public void renderData(String item)
    {
        if(item!=null) {
            Log.v(TAG, item);
            try {
                Log.v(TAG, item);
                obj temp = state_mp.get(item);
                totalCases.setText(temp.a);
                activeCases.setText(temp.b);
                recoveredCases.setText(temp.c);
                deathCases.setText(temp.d);
                //Log.v(TAG, totalCases.getText().toString() + " " + activeCases.getText().toString() + " " + recoveredCases.getText().toString() + " " + deathCases.getText().toString());
            }catch(Exception e)
            {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Log.v(TAG, "in spinner");
        if(arg0.getId() == R.id.spinner) {
            item1 = arg0.getItemAtPosition(position).toString();
            renderData(item1);
            createGraph_state(item1, item2);
        }
        else
        {
            item2 = arg0.getItemAtPosition(position).toString();
            createGraph_state(item1, item2);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void createGraph_state(String item1, String item2)
    {
        //Log.v(TAG, "in create graph 1 ");
        graph.removeAllSeries();
        graph.setVisibility(View.VISIBLE);
        String tt1 = "";
        for(int i = 0 ;i< dict.length;i++)
            if(item1.equals(dict[i][0]))
                tt1 = dict[i][1];
        int tempt;
        if(item2.equals("Confirmed"))
            tempt = 0;
        else if(item2.equals("Recovered"))
            tempt = 1;
        else
            tempt = 2;
        //Log.v(TAG, "in create graph 2");
        try {
            LineGraphSeries <DataPoint> series = new LineGraphSeries< >();
            double maxi = Double.MIN_VALUE;
            int start = counter;
            int end = cc;
            if(tt1.equals("tt"))
            {
                start = 0;
                end = counter;
            }
            //Log.v(TAG, Integer.toString(start)+" "+Integer.toString(end) + Integer.toString(tempt) + tt1);
            for(int i=start, j = 0;i<end;i++, j++)
            {
                //Log.v(TAG, "in loop");
                DataPoint dp;
                if(tempt == 0)
                    dp = new DataPoint(j, Integer.valueOf(((daily_mp.get(i)).get(tt1)).a));
                else if(tempt == 1)
                    dp = new DataPoint(j, Integer.valueOf(((daily_mp.get(i)).get(tt1)).b));
                else
                    dp = new DataPoint(j, Integer.valueOf(((daily_mp.get(i)).get(tt1)).c));
                maxi = Math.max(maxi, dp.getY());
                //Log.v(TAG, Double.toString(dp.getX())+" "+Double.toString(dp.getY()));
                series.appendData(dp, true, Integer.MAX_VALUE);
            }
            graph.addSeries(series);
            graph.getViewport().setMinX(0);
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxX(end - start + 2 );
            graph.getViewport().setMaxY(maxi + 100 );
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setXAxisBoundsManual(true);
        } catch (Exception e) {
            Log.v(TAG, e.getMessage());
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.v(TAG, "on button click");
        if(isChecked)
        {
            Cases_ListFragment cases_listFragment = new Cases_ListFragment();
            Bundle b = new Bundle();
            for(String i : states)
            {
                if(i.equals("All States"))
                    continue;
                ArrayList<String> temp = new ArrayList<>();
                obj temp2 = state_mp.get(i);
                temp.add(temp2.a);
                temp.add(temp2.b);
                temp.add(temp2.c);
                temp.add(temp2.d);
                b.putStringArrayList(i, temp);
            }
            cases_listFragment.setArguments(b);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, cases_listFragment);
            fragmentTransaction.commit();
        }
    }
}
