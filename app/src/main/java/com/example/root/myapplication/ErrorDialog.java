package com.example.root.myapplication;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ErrorDialog extends androidx.fragment.app.DialogFragment implements View.OnClickListener {

    public TextView error_text;
    public Button btn1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.error_dialog, container, false);
        Bundle b = getArguments();
        error_text = root.findViewById(R.id.error_text);
        error_text.setText(b.getString("error"));
        btn1 = root.findViewById(R.id.btn1);
        btn1.setOnClickListener(handler1);
        return root;
    }

    View.OnClickListener handler1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getFragmentManager();
            ErrorDialog f = (ErrorDialog) fragmentManager.findFragmentByTag("error");
            if(f!=null){
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(f).commit();
            }
        }
    };

    @Override
    public void onClick(View v) {

    }
}
