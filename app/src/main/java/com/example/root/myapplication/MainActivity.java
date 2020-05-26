package com.example.root.myapplication;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

public class MainActivity extends FragmentActivity {

    public final static String TAG = "debugging";
    public final int BLUETOOTH_CODE = 100;
    public final int BLUETOOTH_ADMIN_CODE = 101;
    public final int ACCESS_FINE_LOCATION = 102;
    public final int INTERNET = 103;
    public final int ACCESS_NETWORK_STATE = 104;
    public final int CALL_PHONE = 105;
    public int current = R.id.nav_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "on create hook method called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
        }
        Log.v(TAG, "successfully passed on create");
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@androidx.annotation.NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            switch (menuItem.getItemId()){
                case R.id.nav_service:
                    selectedFragment = new ServiceFragment();
                    break;
                case R.id.nav_cases:
                    selectedFragment =  new CasesFragment();
                    break;
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container, selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        checkPermission(Manifest.permission.BLUETOOTH, BLUETOOTH_CODE);
        checkPermission(Manifest.permission.BLUETOOTH_ADMIN,BLUETOOTH_ADMIN_CODE);
        checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, ACCESS_FINE_LOCATION);
        checkPermission(Manifest.permission.INTERNET, INTERNET);
        checkPermission(Manifest.permission.ACCESS_NETWORK_STATE, ACCESS_NETWORK_STATE);
        checkPermission(Manifest.permission.CALL_PHONE, CALL_PHONE);
        if(!isMyServiceRunning(MyService.class))
        {
            Log.v(TAG, "starting service");
            Intent service = new Intent(this, MyService.class);
            this.startService(service);
            Log.v(TAG, "service started");
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void stoppingService(View view)
    {
        Log.v(TAG, "stopping service");
        Intent service = new Intent(this, MyService.class);
        this.stopService(service);
        Log.v(TAG, "service stopped");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void checkPermission(String permission, int requestCode)
    {
        if(ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[] {permission}, requestCode);
        }
        else
        {
            String p_type = permission.substring(19);
            //Toast.makeText(this, p_type+" permission granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == BLUETOOTH_CODE)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                String p_type = permissions[0].substring(19);
                //Toast.makeText(this, p_type+" permission granted", Toast.LENGTH_SHORT).show();
            }
            else
            {
                String p_type = permissions[0].substring(19);
                //Toast.makeText(this, p_type+" permission granted", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
