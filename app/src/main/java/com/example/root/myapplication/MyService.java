package com.example.root.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class MyService extends Service {

    class notif{
        int id;
        String btAddres;
        String lat;
        String lon;
        public notif(int w, String x, String y, String z){
            id = w;
            btAddres = x;
            lat = y;
            lon = z;
        }
    }

    public BroadcastReceiver receiver;
    public BroadcastReceiver receiver2;
    public BluetoothAdapter bluetoothAdapter;
    FusedLocationProviderClient mFusedLocationClient;
    private final String TAG = "Service_debugging";
    public String  temp[] = new String[2];
    public HashMap<String, Date> mp;
    private Messenger replyMessanger;
    public Messenger mMessenger;
    public Context context;
    private int id;
    private Queue<notif> queue;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        //throw new UnsupportedOperationException("Not yet implemented");
        return mMessenger.getBinder();
    }

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    replyMessanger = msg.replyTo;
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
        }

    public void msg(String messageText)
    {
        try {
            Log.v(TAG, "sending message");
            Message message = new Message();
            message.obj = messageText;
            replyMessanger.send(message);
        }catch(RemoteException e){
            Log.v(TAG, e.getMessage());
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMessenger = new Messenger(new IncomingHandler());
        mp = new HashMap<>();
        initialize();
        action_foundBroadcaster();
        discoveryBroadcaster();
    }

    public void initialize()
    {
        id = 0;
        queue = new LinkedList<>();
        context = getApplicationContext();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        if (bluetoothAdapter == null) {
            Log.v(TAG, "no bluetooth");
        }
        if (!bluetoothAdapter.isEnabled()) {
            Log.v(TAG, "try to enable bluetooth");
            bluetoothAdapter.enable();
            Log.v(TAG, "bluetooth enabled");
        }
        if (!bluetoothAdapter.isDiscovering()) {
            Log.v(TAG, "start first discovering");
            bluetoothAdapter.startDiscovery();
        }
    }

    public void action_foundBroadcaster() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    int Rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String deviceName = device.getName();
                    String deviceHardwareAddress = device.getAddress();
                    if(!mp.containsKey(deviceHardwareAddress))
                    {
                        mp.put(deviceHardwareAddress, Calendar.getInstance().getTime());
                        String s = deviceName + " " + deviceHardwareAddress + " " + Rssi;
                        Log.v(TAG, s);
                        msg(deviceHardwareAddress);
                        queue.add(new notif(id++, deviceHardwareAddress, temp[0]+"", temp[1]+""));
                        send_queue_data();
                    }
                    showNotification("STAY AWAY PLEASE!!!");
                }
            }
        };

        Log.v(MainActivity.TAG, "start broadcaster");
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
        Log.v(MainActivity.TAG, "broadcaster ready");
    }

    public void discoveryBroadcaster() {
        receiver2 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v(TAG, "starting discovery");
                String action = intent.getAction();
                if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    Log.v(TAG, "Again start discovery");
                    bluetoothAdapter.startDiscovery();
                }
            }
        };
        Log.v(MainActivity.TAG, "start discovery broadcaster");
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver2, filter);
        Log.v(MainActivity.TAG, "discovery broadcaster ready");
    }

    public void send_queue_data(){
        if(queue.size() > 0){
            notif object = queue.peek();
            web3j_func web3 = new web3j_func(MyService.this, context);
            web3.connectEth(404);
            web3.execute("listener", object.btAddres, object.lat, object.lon, Integer.toString(object.id));
        }
    }

    public void remove(String s, int id, String hash){
        String hash_string = PreferenceManager.getDefaultSharedPreferences(context).getString("hash", "");
        if(hash_string.equals(""))
            hash_string += hash;
        else
            hash_string += ":" + hash;
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("hash", hash_string).apply();
        notif object = queue.peek();
        if(object.id == id){
            queue.poll();
            send_queue_data();
        }
    }

    private void getLastLocation(){
        if (isLocationEnabled()) {
            try {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    requestNewLocationData();
                                }
                            }
                        }
                );
            }catch (SecurityException e)
            {

            }
        } else {
            Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            mFusedLocationClient.requestLocationUpdates(
                    mLocationRequest, mLocationCallback,
                    Looper.myLooper()
            );
        }
        catch(SecurityException e)
        {

        }
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            Log.v(TAG, (mLastLocation.getLatitude() + ""));
            Log.v(TAG, (mLastLocation.getLongitude() + ""));
            temp[0] = Double.toString(mLastLocation.getLatitude());
            temp[1] = Double.toString(mLastLocation.getLongitude());
        }
    };

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("1", "CHANNEL1", importance);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showNotification(String s) {
        createNotificationChannel();
        Log.v(TAG, "notification creation");
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        Notification notification = new NotificationCompat.Builder(this, "1").setSmallIcon(R.drawable.ic_add_alert_black_24dp).setContentTitle("ALERT").setContentText(s).setPriority(NotificationCompat.PRIORITY_MAX).build();
        notificationManagerCompat.notify(1, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        unregisterReceiver(receiver2);
    }

    /*BluetoothAdapter.LeScanCallback callback = new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanData) {
                Log.v(TAG, "sss");
                //if (scanData[7] == 0x02 && scanData[8] == 0x15) { // iBeacon indicator
                    //Log.v(TAG, bytesToHexString(scanData));
                  //  UUID uuid = getGuidFromByteArray(Arrays.copyOfRange(scanData, 9, 25));
                    //int major = (scanData[25] & 0xff) * 0x100 + (scanData[26] & 0xff);
                    //int minor = (scanData[27] & 0xff) * 0x100 + (scanData[28] & 0xff);
                    byte txpw = scanData[29];

                    //Log.v(TAG, Byte.toString(txpw));
                double acc = calculateDistance2(txpw, rssi);
                Log.v(TAG, Double.toString(acc));
                //}
            }
        };
        bluetoothAdapter.startLeScan(callback);*/

    /*public double calculateDistance2(int a, int b) {
        return Math.pow(10d, ((double) a - b) / (10 * 2));
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder buffer = new StringBuilder();
        for(int i=0; i<bytes.length; i++) {
            buffer.append(String.format("%02x", bytes[i]));
        }
        return buffer.toString();
    }
    public static UUID getGuidFromByteArray(byte[] bytes)
    {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        UUID uuid = new UUID(bb.getLong(), bb.getLong());
        return uuid;
    }*/


}
