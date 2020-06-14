package com.example.root.myapplication;


import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceFragment extends Fragment {

    public TextView current;
    public ListView service_list;
    private ArrayList<String> listItems = new ArrayList<>();
    private ArrayAdapter adapter2;
    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return root = inflater.inflate(R.layout.nav_service, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        service_list = root.findViewById(R.id.service_list);
        adapter2 = new ArrayAdapter<String>(getActivity(), R.layout.list_item, listItems);
        service_list.setAdapter(adapter2);
        BottomNavigationView bottomNavigationMenu = getActivity().findViewById(R.id.bottom_navigation);
        MenuItem temp = bottomNavigationMenu.getMenu().findItem(R.id.nav_service);
        temp.setChecked(true);
        set_data();
    }

    public  void set_data()
    {
        String hash_string = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("hash", "");
        String temp[] = hash_string.split(":");
        for(String x: temp)
            listItems.add(x);
        adapter2.notifyDataSetChanged();
    }
}
