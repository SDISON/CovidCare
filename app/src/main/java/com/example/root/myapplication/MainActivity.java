package com.example.root.myapplication;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

    public final static String TAG = "MainActivity_debugging";
    public final int BLUETOOTH_CODE = 100;
    public final int BLUETOOTH_ADMIN_CODE = 101;
    public final int ACCESS_FINE_LOCATION = 102;
    public final int INTERNET = 103;
    public final int ACCESS_NETWORK_STATE = 104;
    public final int CALL_PHONE = 105;
    public final int ACCESS_COARSE_LOCATION = 106;
    public int current = R.id.nav_service;
    private Messenger mService = null;
    private boolean mBound = false;
    static public String data = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "on create hook method called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new Spalsh(), "Splash").commit();
        }
        Log.v(TAG, "successfully passed on create");
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@androidx.annotation.NonNull MenuItem menuItem) {
            int count = (getSupportFragmentManager().getBackStackEntryCount());
            switch (menuItem.getItemId()){
                case R.id.nav_service:
                    if(count>=1)
                        pop_pop();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new ServiceFragment(), "Service").addToBackStack("Service").commit();
                    break;
                case R.id.nav_cases:
                    if(count>=1)
                        pop_pop();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new CasesFragment(), "Cases").addToBackStack("Cases").commit();
                    break;
                case R.id.nav_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment(), "Home").commit();
                    break;
            }
            return true;
        }
    };

    public void pop_pop()
    {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        while(count!=0) {
            getSupportFragmentManager().popBackStack();
            count--;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkPermission(Manifest.permission.BLUETOOTH, BLUETOOTH_CODE);
        checkPermission(Manifest.permission.BLUETOOTH_ADMIN,BLUETOOTH_ADMIN_CODE);
        checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, ACCESS_FINE_LOCATION);
        checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, ACCESS_COARSE_LOCATION);
        checkPermission(Manifest.permission.INTERNET, INTERNET);
        checkPermission(Manifest.permission.ACCESS_NETWORK_STATE, ACCESS_NETWORK_STATE);
        //checkPermission(Manifest.permission.CALL_PHONE, CALL_PHONE);
        //checkPermission(Manifest.permission.BLUETOOTH_PRIVILEGED, BLUETOOTH_PRIVILEDGED);
        if(!isMyServiceRunning(MyService.class))
        {
            Log.v(TAG, "starting service");
            final Intent service = new Intent(this, MyService.class);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getBaseContext().bindService(service, mConnection, Context.BIND_AUTO_CREATE);
                    Log.v(TAG, "service started");
                }
            }, 10000);
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            mService = new Messenger(service);
            mBound = true;
            Message msg = Message.obtain(null, 1, 0, 0);
            Messenger replyMessenger = new Messenger(new HandlerReplyMsg());
            msg.replyTo = replyMessenger;
            if(mBound) {
                try {
                    Log.v(TAG, "start replymsg address sending");
                    mService.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mService = null;
            mBound = false;
        }
    };

    // handler for message from service

    static class HandlerReplyMsg extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String recdMessage = msg.obj.toString(); //msg received from service
            Log.v(TAG, recdMessage);
            data += recdMessage + "\n";
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
        this.unbindService(mConnection);
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
