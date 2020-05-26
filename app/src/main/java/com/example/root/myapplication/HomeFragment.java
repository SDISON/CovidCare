package com.example.root.myapplication;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    public ImageButton labs;
    public TextView status;
    public ImageButton call;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.nav_home, container, false);
        status = root.findViewById(R.id.status);
        labs = root.findViewById(R.id.img3);
        labs.setOnClickListener(handler1);
        call = root.findViewById(R.id.img4);
        call.setOnClickListener(handler2);
        settingStatus();
        return root;
    }

    public void settingStatus()
    {
        status.setText("You are safe");
    }

    View.OnClickListener handler1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LabFragment labFragment = new LabFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, labFragment);
            fragmentTransaction.commit();
        }
    };

    View.OnClickListener handler2 =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+917457868171"));
            startActivity(intent);
        }
    };

    @Override
    public void onClick(View v) {

    }
}
