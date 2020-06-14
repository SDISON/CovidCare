package com.example.root.myapplication;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.lang.reflect.Field;
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
        String d;
        public obj2(String w, String x, String y, String z)
        {
            a = x;
            b = y;
            c = z;
            d = w;
        }
    }
    private String[] states = { "All States", "Maharashtra", "Gujarat", "Tamil Nadu", "Delhi", "Rajasthan", "Madhya Pradesh", "Uttar Pradesh", "West Bengal", "Andhra Pradesh", "Punjab", "Telangana", "Bihar", "Jammu and Kashmir", "Karnataka", "Haryana", "Odisha", "Kerala", "Jharkhand", "Chandigarh", "Tripura", "Assam", "Uttarakhand", "Himachal Pradesh", "Chhattisgarh", "Ladakh", "Andaman and Nicobar", "Goa", "Puducherry","Meghalaya", "Manipur", "Mizoram", "Arunachal Pradesh", "Dadra & Nagar Haveli", "Nagaland", "Lakshadweep", "Sikkim"};
    private String [] types = {"Confirmed", "Recovered", "Deceased"};
    private String [][] dict = {{"All States", "tt"},{"Maharashtra", "mh"}, {"Gujarat", "gj"}, {"Tamil Nadu", "tn"}, {"Delhi", "dl"}, {"Rajasthan", "rj"}, {"Madhya Pradesh", "mp"}, {"Uttar Pradesh", "up"}, {"West Bengal", "wb"}, {"Andhra Pradesh", "ap"}, {"Punjab", "pb"}, {"Telangana", "tg"}, {"Bihar", "br"}, {"Jammu and Kashmir", "jk"}, {"Karnataka", "ka"}, {"Haryana", "hr"}, {"Odisha", "or"}, {"Kerala", "kl"}, {"Jharkhand", "jh"}, {"Chandigarh", "ch"}, {"Tripura", "tr"}, {"Assam", "as"}, {"Uttarakhand", "ut"}, {"Himachal Pradesh", "hp"}, {"Chhattisgarh", "ct"}, {"Ladakh", "la"}, {"Andaman and Nicobar", "an"}, {"Goa", "ga"}, {"Puducherry", "py"},{"Meghalaya", "ml"}, {"Manipur", "mn"}, {"Mizoram", "mz"}, {"Arunachal Pradesh", "ar"}, {"Dadra & Nagar Haveli", "dn"}, {"Nagaland", "nl"}, {"Lakshadweep", "ld"}, {"Sikkim", "sk"}};
    private String [] codes = {"tt", "an", "ap", "ar", "as", "br", "ch", "ct", "dd", "dl", "dn", "ga", "gj", "hp", "hr", "jh", "jk", "ka", "kl", "la", "ld", "mh", "ml", "mn", "mp", "mz", "nl", "or", "pb", "py", "rj", "sk", "tg", "tn", "tr", "tt", "up", "ut", "wb"};
    private static final String TAG = "CasesFragment_debugging";
    private TextView totalCases;
    private TextView activeCases;
    private TextView recoveredCases;
    private TextView deathCases;
    private TextView clickView;
    private HashMap<String,obj> state_mp;
    private HashMap<Integer, HashMap<String, obj2>> daily_mp;
    public GifImageView gif_cases2;
    public Spinner spinner;
    public Spinner spinner2;
    public GraphView graph;
    public Switch switch_list;
    public Button update_data;
    private String item1 = "All States";
    private String item2 = "Confirmed";
    public View root;
    private boolean updating;
    private Context context;
    private BottomNavigationView bottomNavigationMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Arrays.sort(states);
        root =  inflater.inflate(R.layout.nav_cases, container, false);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        intializeView();
        context = getContext();
        updating = true;
        state_mp = new HashMap<>();
        daily_mp = new HashMap<>();
        checkData();
    }

    private void intializeView()
    {
        totalCases = root.findViewById(R.id.totalCases);
        activeCases = root.findViewById(R.id.activeCases);
        recoveredCases = root.findViewById(R.id.recoveredCases);
        deathCases = root.findViewById(R.id.deathCases);
        clickView = root.findViewById(R.id.clickView);
        gif_cases2 = root.findViewById(R.id.gif_cases2);
        graph = root.findViewById(R.id.graph);
        switch_list = root.findViewById(R.id.switch1);
        switch_list.setEnabled(false);
        update_data = root.findViewById(R.id.update_data);
        update_data.setOnClickListener(handler);
        Activity temp = getActivity();
        try {
            bottomNavigationMenu = temp.findViewById(R.id.bottom_navigation);
        }catch(NullPointerException e)
        {
            Log.e(TAG, e.getMessage());
        }
        MenuItem tempy = bottomNavigationMenu.getMenu().findItem(R.id.nav_cases);
        tempy.setChecked(true);

        //Creating the ArrayAdapter instance having the country list
        spinner = root.findViewById(R.id.spinner);
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);
            popupWindow.setHeight(10);
        }catch (Exception e)
        {
            Log.e(TAG, "not working");
        }
        ArrayAdapter<String> adapter;
        try {
            adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_item, states);
            //adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);
            spinner.setEnabled(false);
        }catch(NullPointerException e)
        {
            Log.e(TAG, e.getMessage());
        }

        spinner2 = root.findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_item, types);
        //adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);
        spinner2.setEnabled(false);
        switch_list.setChecked(false);
    }

    private void updateData_(String url)
    {
        updateData obj = new updateData(url, context, this);
        obj.start();
    }

    private void checkData()
    {
        gif_cases2.setVisibility(View.VISIBLE);
        String result1 = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("jsonString1", "");
        if(result1.equals("")) {
            checkConnectivity obj = new checkConnectivity("https://api.covid19india.org/data.json", context, this);
                obj.start();
        }
        else{
            setData_("https://api.covid19india.org/data.json", result1);
        }
        String result2 = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("jsonString2", "");
        if(result2.equals("")) {
            checkConnectivity obj2 = new checkConnectivity("https://api.covid19india.org/states_daily.json", context, this);
            obj2.start();
        }
        else{
            setData_("https://api.covid19india.org/states_daily.json", result2);
        }
    }

    public void getData(String url)
    {
        new RetrieveData(url, this).execute();
    }

    @Override
    public void fetchDataCallback(String url, final String result) {
        saveData save_data = new saveData(url, result,context);
        save_data.saving();
        setData_(url, result);
    }

    private void setData_(String url, String result) {
        setData set_data = new setData(url, result, context, state_mp, daily_mp);
        int temp = set_data.start();
        Log.e(TAG, Integer.toString(temp));
        if(temp == 2){
            switch_list.setEnabled(true);
            gif_cases2.setVisibility(View.GONE);
            renderData(item1);
            createGraph_state(item1, item2);
            switch_list.setOnCheckedChangeListener(this);
            spinner.setEnabled(true);
            spinner2.setEnabled(true);
            /*if(updating) {
                updateData_("https://api.covid19india.org/data.json");
                updateData_("https://api.covid19india.org/states_daily.json");
                updating = false;
            }*/
        }
    }

    View.OnClickListener handler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateData_("https://api.covid19india.org/data.json");
            updateData_("https://api.covid19india.org/states_daily.json");
        }
    };
    private void renderData(String item)
    {
        if(item!=null) {
            try {
                obj temp = state_mp.get(item);
                totalCases.setText(temp.a);
                activeCases.setText(temp.b);
                recoveredCases.setText(temp.c);
                deathCases.setText(temp.d);
            }catch(Exception e)
            {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        if(arg0.getId() == R.id.spinner) {
            item1 = arg0.getItemAtPosition(position).toString();
            clickView.setText("");
            renderData(item1);
            createGraph_state(item1, item2);
        }
        else
        {
            clickView.setText("");
            item2 = arg0.getItemAtPosition(position).toString();
            createGraph_state(item1, item2);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void createGraph_state(String item1, String item2)
    {
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
        try {
            LineGraphSeries <DataPoint> series = new LineGraphSeries< >();
            double maxi = Double.MIN_VALUE;
            int start = 5;
            int end = 10;
            if(tt1.equals("tt"))
            {
                start = 0;
                end = 5;
            }
            final String [] values = new String[5];
            for(int i=start, j = 0;i<end;i++, j++)
            {
                DataPoint dp;
                values[j] = daily_mp.get(i).get(tt1).d;
                try {
                    values[j] = values[j].split("-")[0] + " " + values[j].split("-")[1];
                }catch(Exception e)
                {}
                if(tempt == 0)
                    dp = new DataPoint(j, Integer.valueOf(((daily_mp.get(i)).get(tt1)).a));
                else if(tempt == 1)
                    dp = new DataPoint(j, Integer.valueOf(((daily_mp.get(i)).get(tt1)).b));
                else
                    dp = new DataPoint(j, Integer.valueOf(((daily_mp.get(i)).get(tt1)).c));
                maxi = Math.max(maxi, dp.getY());
                series.appendData(dp, true, Integer.MAX_VALUE);
            }
            maxi = Math.max(100, maxi);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(values);
            //graph.getGridLabelRenderer().setHorizontalLabelsAngle(120);
            graph.getGridLabelRenderer().setTextSize(20);
            //graph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
            //graph.getGridLabelRenderer().setVerticalAxisTitle("Cases Count");
            graph.getGridLabelRenderer().setNumHorizontalLabels(6);
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.addSeries(series);
            //graph.getViewport().setScalable(true);
            //graph.getViewport().setScrollable(true);
            graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLACK);
            graph.getGridLabelRenderer().setVerticalLabelsColor(Color.BLACK);
            graph.getGridLabelRenderer().setGridColor(Color.BLACK);
            graph.getViewport().setMinX(0);
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxX(end - start - 1);
            graph.getViewport().setMaxY(maxi + maxi/2 );
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setXAxisBoundsManual(true);
            series.setDrawDataPoints(true);
            if(tempt==0)
                series.setColor(Color.RED);
            else if(tempt == 1)
                series.setColor(Color.GREEN);
            else
                series.setColor(Color.GRAY);

            series.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    String x = "On " + values[(int)dataPoint.getX()];
                    String y = " : " + (int)dataPoint.getY();
                    clickView.setText(x + y);
                }
            });
        } catch (Exception e) {
            Log.v(TAG, e.getMessage());
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
            Fragment f = fragmentManager.findFragmentByTag("case_list_frag");
            if(f!=null)
                fragmentManager.popBackStack("case_list_frag", fragmentManager.POP_BACK_STACK_INCLUSIVE);
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, cases_listFragment, "case_list_frag");
            fragmentTransaction.addToBackStack(null);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fragmentTransaction.commit();
                }
            }, 200);
        }
    }
}
