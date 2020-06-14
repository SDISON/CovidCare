package com.example.root.myapplication;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class setData {

    private String url;
    private String result;
    private final String TAG = "setData_debugging";
    private JSONObject jsonObject;
    private HashMap<String,CasesFragment.obj> state_mp;
    private HashMap<Integer, HashMap<String, CasesFragment.obj2>> daily_mp;
    private HashMap<String, ArrayList<String>> lab_mp;
    private HashMap<String, String> phone_mp;
    private Context context;
    private String [] codes = {"tt", "an", "ap", "ar", "as", "br", "ch", "ct", "dd", "dl", "dn", "ga", "gj", "hp", "hr", "jh", "jk", "ka", "kl", "la", "ld", "mh", "ml", "mn", "mp", "mz", "nl", "or", "pb", "py", "rj", "sk", "tg", "tn", "tr", "tt", "up", "ut", "wb"};

    public setData(String x, String y, Context z, HashMap<String, CasesFragment.obj> map_x, HashMap<Integer, HashMap<String, CasesFragment.obj2>> map_y)
    {
        url = x;
        result = y;
        state_mp = map_x;
        daily_mp = map_y;
        context = z;
    }

    public setData(String x, String y, HashMap<String, ArrayList<String>> hash_x, HashMap<String, String> hash_y){
        url = x;
        result = y;
        lab_mp = hash_x;
        phone_mp = hash_y;
    }

    public int start()
    {
        if (url.equals("https://api.covid19india.org/data.json")) {
            Log.v(TAG, "for first url");
            try {
                jsonObject = new JSONObject(result);
                JSONArray array = jsonObject.getJSONArray("statewise");
                for (int i = 0; i < array.length(); i++) {
                    String s = array.getJSONObject(i).getString("state");
                    String w = array.getJSONObject(i).getString("confirmed");
                    String x = array.getJSONObject(i).getString("active");
                    String y = array.getJSONObject(i).getString("recovered");
                    String z = array.getJSONObject(i).getString("deaths");
                    if(s.equals("Total"))
                        state_mp.put("All States", new CasesFragment.obj(w, x, y, z));
                    else if(s.equals("Dadra and Nagar Haveli and Daman and Diu"))
                        state_mp.put("Dadra & Nagar Haveli", new CasesFragment.obj(w, x, y, z));
                    else if(s.equals("Andaman and Nicobar Islands"))
                        state_mp.put("Andaman and Nicobar", new CasesFragment.obj(w, x, y, z));
                    else
                        state_mp.put(s, new CasesFragment.obj(w, x, y, z));
                }
                array = jsonObject.getJSONArray("cases_time_series");
                int counter = 0;
                for (int i = array.length()-5; i < array.length(); i++) {
                    HashMap<String, CasesFragment.obj2> inner = new HashMap<>();
                    String w = array.getJSONObject(i).getString("date");
                    String x = array.getJSONObject(i).getString("dailyconfirmed");
                    String y = array.getJSONObject(i).getString("dailyrecovered");
                    String z = array.getJSONObject(i).getString("dailydeceased");
                    inner.put("tt", new CasesFragment.obj2(w, x, y, z));
                    daily_mp.put(counter, inner);
                    counter += 1;
                }
                return 1;
            } catch (JSONException err) {
                Log.d("Error", err.toString());
            }
        } else if (url.equals("https://api.covid19india.org/states_daily.json")) {
            Log.v(TAG, "For second url");
            try {
                jsonObject = new JSONObject(result);
                JSONArray array = jsonObject.getJSONArray("states_daily");
                int counter = 5;
                for (int i = array.length()-15; i < array.length(); i+=3) {
                    HashMap<String, CasesFragment.obj2> inner = new HashMap<>();
                    for (String code : codes) {
                        String w = array.getJSONObject(i).getString("date");
                        String x = array.getJSONObject(i).getString(code);
                        String y = array.getJSONObject(i+1).getString(code);
                        String z = array.getJSONObject(i+2).getString(code);
                        inner.put(code, new CasesFragment.obj2(w, x, y, z));
                    }
                    daily_mp.put(counter, inner);
                    counter += 1;
                }
            }catch (JSONException err) {
                Log.d(TAG, err.toString());
            }
            return 2;
        }
        else if(url.equals("https://api.covid19india.org/resources/resources.json"))
        {
            Log.v(TAG, "for third url");
            try {
                jsonObject = new JSONObject(result);
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
                            lab_mp.put(s, new ArrayList<>());
                            ArrayList<String> temp = lab_mp.get(s);
                            temp.add(w);
                        }
                    }
                }
            }catch(Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return 0;
    }
}
