package com.example.root.myapplication;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class LabFragment extends Fragment implements AdapterView.OnItemSelectedListener, FetchDataCallbackInterface, AdapterView.OnItemClickListener {

    private static final String TAG = "LabFragment_debugging";
    public Spinner spinner;
    String[] states = { " Please Select One", "Maharashtra", "Gujarat", "Tamil Nadu", "Delhi", "Rajasthan", "Madhya Pradesh", "Uttar Pradesh", "West Bengal", "Andhra Pradesh", "Punjab", "Telangana", "Bihar", "Jammu and Kashmir", "Karnataka", "Haryana", "Odisha", "Kerala", "Jharkhand", "Chandigarh", "Tripura", "Assam", "Uttarakhand", "Himachal Pradesh", "Chhattisgarh", "Ladakh", "Andaman & Nicobar", "Goa", "Puducherry","Meghalaya", "Manipur", "Mizoram", "Arunachal Pradesh", "Dadra and Nagar Haveli", "Nagaland", "Lakshadweep", "Sikkim"};
    public ListView lab_list;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter2;
    public GifImageView gif_cases;
    HashMap<String, ArrayList<String>> lab_mp;
    HashMap<String, String> phone_mp;
    public boolean checker = false;
    public Context context;
    public boolean updating;
    public View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.nav_labs, container, false);
        Arrays.sort(states);
        updating = true;
        initializeView();
        context = getContext();
        checkData();
        return root;
    }

    public void initializeView()
    {
        spinner = root.findViewById(R.id.spinner_lab);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, states);
        //adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setEnabled(false);
        lab_list = root.findViewById(R.id.lab_list);
        adapter2 = new ArrayAdapter<String>(getActivity(), R.layout.list_item, listItems);
        lab_list.setAdapter(adapter2);
        lab_list.setOnItemClickListener(this);
        gif_cases = root.findViewById(R.id.gif_cases);
        lab_mp = new HashMap<>();
        phone_mp = new HashMap<>();
    }

    private void updateData_(String url){
        updateData obj = new updateData(url, context, this);
        obj.start();
    }

    private void checkData()
    {
        gif_cases.setVisibility(View.VISIBLE);
        String result1 = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("jsonString3", "");
        if(result1.equals("")) {
            checkConnectivity obj = new checkConnectivity("https://api.covid19india.org/resources/resources.json", context, this);
            obj.start();
        }
        else{
            setData_("https://api.covid19india.org/resources/resources.json", result1);
        }
    }

    public void getData(String url)
    {
        new RetrieveData(url, this).execute();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        try {
            if(checker) {
                listItems.clear();
                ArrayList<String> temp = lab_mp.get(item);
                for (String s : temp)
                    listItems.add(s);
                adapter2.notifyDataSetChanged();
            }
            else
                checker = true;
        }
        catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void fetchDataCallback(String url, String result) {
        Log.v(TAG, "in fetch");
        saveData save_data = new saveData(url, result, context);
        save_data.saving();
        setData_(url, result);
    }

    public void setData_(String url, String result)
    {
        HashMap<String, ArrayList<String>> lab_mp_temp = new HashMap<>();
        HashMap<String, String> phone_mp_temp = new HashMap<>();
        setData set_data = new setData(url, result, lab_mp_temp, phone_mp_temp);
        lab_mp = lab_mp_temp;
        phone_mp = phone_mp_temp;
        int temp = set_data.start();
        gif_cases.setVisibility(View.GONE);
        spinner.setEnabled(true);
        if(updating) {
            updateData_("https://api.covid19india.org/resources/resources.json");
            updating = false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        DialogFragment dialogFragment = new DialogFragment();
        Bundle b = new Bundle();
        b.putString("name", item);
        b.putString("phone", phone_mp.get(item));
        dialogFragment.setArguments(b);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DialogFragment prev = (DialogFragment) fragmentManager.findFragmentByTag("dialog");
        if(prev!=null)
            fragmentTransaction.remove(prev);
        dialogFragment.show(fragmentTransaction,"dialog");
    }
}
