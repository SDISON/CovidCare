package com.example.root.myapplication;


import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


public class Cases_ListFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {


    private static final String TAG = "debugging";

    class obj
    {
        String s;
        int a;
        int b;
        int c;
        int d;
        obj(String s, int w, int x, int y, int z){
            this.s = s;
            a = w;
            b = x;
            c = y;
            d = z;
        }
    }
    public Switch switch_detail;
    public View root;
    String[] states_list = {"Maharashtra", "Gujarat", "Tamil Nadu", "Delhi", "Rajasthan", "Madhya Pradesh", "Uttar Pradesh", "West Bengal", "Andhra Pradesh", "Punjab", "Telangana", "Bihar", "Jammu and Kashmir", "Karnataka", "Haryana", "Odisha", "Kerala", "Jharkhand", "Chandigarh", "Tripura", "Assam", "Uttarakhand", "Himachal Pradesh", "Chhattisgarh", "Ladakh", "Andaman and Nicobar", "Goa", "Puducherry","Meghalaya", "Manipur", "Mizoram", "Arunachal Pradesh", "Dadra & Nagar Haveli", "Nagaland", "Lakshadweep", "Sikkim"};
    String[] sort = {"State", "Total", "Active", "Cured", "Death"};
    public Spinner spinner3;
    ArrayList<obj> data;
    TableLayout tableLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.nav_cases_list, container, false);
        switch_detail = root.findViewById(R.id.switch2);
        spinner3 = root.findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, sort);
        //adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner3.setAdapter(adapter);
        spinner3.setOnItemSelectedListener(this);
        Arrays.sort(states_list);

        tableLayout = root.findViewById(R.id.list_cases);

        Bundle b = getArguments();
        data  = setData(b);
        addHeading();
        addData();
        return root;
    }

    public ArrayList<obj> setData(Bundle b)
    {
        ArrayList<obj> ar = new ArrayList<>();
        for(int i=0;i<states_list.length;i++)
        {
            ArrayList<String> temp = b.getStringArrayList(states_list[i]);
            ar.add(new obj(states_list[i], Integer.parseInt(temp.get(0)), Integer.parseInt(temp.get(1)), Integer.parseInt(temp.get(2)), Integer.parseInt(temp.get(3))));
        }
        return ar;
    }

    public void addHeading()
    {
        TableRow row = new TableRow(getContext());
        TableRow.LayoutParams lp_  = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
        row.setLayoutParams(lp_);
        row.setBackgroundResource(R.color.black);
        row.setMinimumHeight(3);
        tableLayout.addView(row);
        for(int i=0;i<1;i++)
        {
            row = new TableRow(getContext());
            TableRow.LayoutParams lp  = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);
            TextView t1 = new TextView(getContext());
            t1.setTextColor(Color.BLACK);
            t1.setGravity(Gravity.CENTER_VERTICAL);
            t1.setHeight(80);
            t1.setTextSize(17);
            TextView t2 = new TextView(getContext());
            t2.setTextColor(Color.WHITE);
            t2.setTextSize(17);
            TextView t3 = new TextView(getContext());
            t3.setTextColor(Color.RED);
            t3.setTextSize(17);
            TextView t4 = new TextView(getContext());
            t4.setTextColor(Color.GREEN);
            t4.setTextSize(17);
            TextView t5 = new TextView(getContext());
            t5.setTextColor(Color.DKGRAY);
            t5.setTextSize(17);
            t1.setText("State");
            row.addView(t1);
            t2.setText("Total");
            row.addView(t2);
            t3.setText("Active");
            row.addView(t3);
            t4.setText("Cured");
            row.addView(t4);
            t5.setText("Death");
            row.addView(t5);
            //row.setMinimumHeight(100);
            tableLayout.addView(row);
        }
        row = new TableRow(getContext());
        row.setLayoutParams(lp_);
        row.setBackgroundResource(R.color.black);
        row.setMinimumHeight(3);
        tableLayout.addView(row);
    }
    public void addData()
    {
        for(int i=0, j=0;i<data.size();i++, j+=2)
        {
            TableRow row = new TableRow(getContext());
            TableRow.LayoutParams lp  = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);
            TextView t1 = new TextView(getContext());
            t1.setTextColor(Color.BLACK);
            t1.setGravity(Gravity.CENTER_VERTICAL);
            t1.setHeight(70);
            TextView t2 = new TextView(getContext());
            t2.setTextColor(Color.WHITE);
            TextView t3 = new TextView(getContext());
            t3.setTextColor(Color.RED);
            TextView t4 = new TextView(getContext());
            t4.setTextColor(Color.GREEN);
            TextView t5 = new TextView(getContext());
            t5.setTextColor(Color.DKGRAY);
            obj temp= data.get(i);
            t1.setText(temp.s);
            row.addView(t1);
            t2.setText(Integer.toString(temp.a));
            row.addView(t2);
            t3.setText(Integer.toString(temp.b));
            row.addView(t3);
            t4.setText(Integer.toString(temp.c));
            row.addView(t4);
            t5.setText(Integer.toString(temp.d));
            row.addView(t5);
            //row.setMinimumHeight(70);
            tableLayout.addView(row);
            //TableRow row2 = new TableRow(getContext());
            //TableRow.LayoutParams lp_  = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            //row2.setLayoutParams(lp_);
            //row2.setBackgroundResource(R.color.grey);
            //row2.setMinimumHeight(3);
            //tableLayout.addView(row2);
        }
        switch_detail.setOnCheckedChangeListener(this);
    }

    public void sortBy(String item)
    {
        if(item.equals("State"))
        {
            Collections.sort(data, new Comparator<obj>() {
                public int compare(obj x, obj y)
                {
                    return x.s.compareTo(y.s);
                }
            });
        }
        else if(item.equals("Total"))
        {
            Collections.sort(data, new Comparator<obj>() {
                public int compare(obj x, obj y)
                {
                    return -(x.a - y.a);
                }
            });
        }
        else if(item.equals("Active"))
        {
            Collections.sort(data, new Comparator<obj>() {
                public int compare(obj x, obj y)
                {
                    return -(x.b - y.b);
                }
            });
        }
        else if(item.equals("Cured"))
        {
            Collections.sort(data, new Comparator<obj>() {
                public int compare(obj x, obj y)
                {
                    return -(x.c - y.c);
                }
            });
        }
        else if(item.equals("Death"))
        {
            Collections.sort(data, new Comparator<obj>() {
                public int compare(obj x, obj y)
                {
                    return -(x.d - y.d);
                }
            });
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked)
        {
            CasesFragment casesFragment = new CasesFragment();
            Bundle b = new Bundle();
            casesFragment.setArguments(b);
            FragmentManager fragmentManager = getFragmentManager();
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, casesFragment);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fragmentTransaction.commit();
                }
            }, 200);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TableLayout constraintLayout = root.findViewById(R.id.list_cases);
        String item = parent.getItemAtPosition(position).toString();
        sortBy(item);
        constraintLayout.removeAllViews();
        addHeading();
        addData();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
