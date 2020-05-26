package com.example.root.myapplication;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


public class DialogFragment extends androidx.fragment.app.DialogFragment implements View.OnClickListener {

public TextView dialog_text;
public ImageButton call_lab;
String phone;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_box, container, false);
        Bundle b = getArguments();
        dialog_text = root.findViewById(R.id.dialog_text);
        dialog_text.setText(b.getString("name"));
        call_lab = root.findViewById(R.id.img6);
        call_lab.setOnClickListener(handler1);
        phone = b.getString("phone");
        return root;
    }

    View.OnClickListener handler1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phone));
            startActivity(intent);
        }
    };

    @Override
    public void onClick(View v) {

    }
}
