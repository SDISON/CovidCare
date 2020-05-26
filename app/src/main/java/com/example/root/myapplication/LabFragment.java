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

    private static final String TAG = "debugging";
    public Spinner spinner;
    String[] states = { " Please Select One", "Maharashtra", "Gujarat", "Tamil Nadu", "Delhi", "Rajasthan", "Madhya Pradesh", "Uttar Pradesh", "West Bengal", "Andhra Pradesh", "Punjab", "Telangana", "Bihar", "Jammu and Kashmir", "Karnataka", "Haryana", "Odisha", "Kerala", "Jharkhand", "Chandigarh", "Tripura", "Assam", "Uttarakhand", "Himachal Pradesh", "Chhattisgarh", "Ladakh", "Andaman & Nicobar", "Goa", "Puducherry","Meghalaya", "Manipur", "Mizoram", "Arunachal Pradesh", "Dadra and Nagar Haveli and Daman and Diu", "Nagaland", "Lakshadweep", "Sikkim"};
    public ListView lab_list;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter2;
    public GifImageView gif_cases;
    HashMap<String, ArrayList<String>> lab_mp;
    HashMap<String, String> phone_mp;
    public boolean checker = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.nav_labs, container, false);
        Arrays.sort(states);
        spinner = root.findViewById(R.id.spinner_lab);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, states);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setEnabled(false);
        lab_list = root.findViewById(R.id.lab_list);
        adapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listItems);
        lab_list.setAdapter(adapter2);
        lab_list.setOnItemClickListener(this);
        gif_cases = root.findViewById(R.id.gif_cases);
        checkConnectivity();
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
        gif_cases.setVisibility(View.VISIBLE);
        new RetrieveData("https://api.covid19india.org/resources/resources.json", this).execute();
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
            Log.e("TAG", e.getMessage());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void fetchDataCallback(String url, String result) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(result);
            lab_mp = new HashMap<>();
            phone_mp = new HashMap<>();
            JSONArray array = jsonObject.getJSONArray("resources");
            for (int i = 0; i < array.length(); i++) {
                String s = array.getJSONObject(i).getString("state");
                String w = array.getJSONObject(i).getString("nameoftheorganisation");
                String x = array.getJSONObject(i).getString("category");
                String y = array.getJSONObject(i).getString("phonenumber");
                y = y.split(",")[0];
                if(x.equals("CoVID-19 Testing Lab")) {
                    phone_mp.put(w, y);
                    if (lab_mp.containsKey(s)) {
                        ArrayList<String> temp = lab_mp.get(s);
                        temp.add(w);
                    } else {
                        lab_mp.put(s, new ArrayList<String>());
                        ArrayList<String> temp = lab_mp.get(s);
                        temp.add(w);
                    }
                }
            }
            gif_cases.setVisibility(View.GONE);
            spinner.setEnabled(true);
        }catch(Exception e) {
            Log.e("TAG", e.getMessage());
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
