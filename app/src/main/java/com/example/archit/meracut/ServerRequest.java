package com.example.archit.meracut;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dhruv on 09-05-2016.
 */
public class ServerRequest extends AsyncTask<String, String, String> {

    int flag = 0;
    private static final String TAG_SUCCESS = "success";
    List<NameValuePair> param = new ArrayList<NameValuePair>();
    String url;
    ArrayList<String> x;
    String[] type;
    JSONParser jsonParser = new JSONParser();
    public JSONObject js = null;
    public String stopflag = "ok";
    public String con_excep="";

    ServerRequest(String[] p, String url){
        this.type = p;
        this.url = url;
    }

    ServerRequest(ArrayList<String> x,  String[] p, String url, String konsa){
        this.type = p;
        this.url = url;
        if (konsa.equals("services")) {
            for (int i = 0; i < x.size(); i++) {
                param.add(new BasicNameValuePair("services[]", x.get(i)));
            }
        }
        if (konsa.equals("packages")){
            for (int i = 0; i < x.size(); i++) {
                param.add(new BasicNameValuePair("packages[]", x.get(i)));
            }
        }
    }

    ServerRequest(ArrayList<String> x, ArrayList<String> y, String[] p, String url){
        this.type = p;
        this.url = url;
        for (int i = 0; i < x.size(); i++) {
            param.add(new BasicNameValuePair("services[]", x.get(i)));
        }
        for (int i = 0; i < y.size(); i++) {
            param.add(new BasicNameValuePair("packages[]", y.get(i)));
        }
    }

    @Override
    protected void onPreExecute() {
        Log.d("async", "inside async pre execute");
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        Log.d("async", "iside doinbackground1" + type);

        for(int i=0;i<type.length;i++){
            Log.d("type",type[i]);
            param.add(new BasicNameValuePair("request"+i, type[i]));
        }

        JSONObject json = jsonParser.makeHttpRequest(url,
                "POST", param);


        if (jsonParser.flag.equals("conexp")) {
                   this.con_excep = "conexcep";
        }



        Log.d("async", "Below http Request");
        try {
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                Log.d("success", String.valueOf(success));
                js = json;
                Log.d("ServerRequest ","saving json object");
                //final String obj = json.getString("name");
                flag = 1;
            }
            else {
                this.stopflag = "failed";
                Log.d("failed", String.valueOf(success));
                // failed to create user
                flag = 0;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(String file_url) {
        if (flag == 1) {
            //  Toast.makeText(getApplication(), "token sent", Toast.LENGTH_SHORT).show();
        } else {
            // Toast.makeText(getApplication(), "token not sent", Toast.LENGTH_SHORT).show();
        }

    }


    public JSONObject getObject() throws JSONException {
        Log.d("ServerRequest", "Getting json object");
        return this.js;
    }
}


