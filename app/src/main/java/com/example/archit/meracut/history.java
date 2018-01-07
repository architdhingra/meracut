package com.example.archit.meracut;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class history extends AppCompatActivity {

    public ArrayList<NormalCardData> arr_history = new ArrayList<>() ;

    ServerRequest serverhistory;
    JSONObject jsonob;
    Loadhistory lh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("meracut", MODE_PRIVATE);
        //sharedPref.getString("number","");
        String[] para = {sharedPref.getString("number","")};

        Log.d("history",""+sharedPref.getString("number","not found")+para);

        serverhistory = new ServerRequest(para,"http://www.wademy.in/meracut/history.php");
        serverhistory.js = null;
        serverhistory.execute();
        lh = new Loadhistory();
        lh.start();


    }



    public class Loadhistory extends Thread {
        boolean stop = true;

        public void stopthread(boolean flag){
            this.stop = flag;
        }

        @Override
        public void run() {
            while (stop) {
                jsonob = serverhistory.js;
                try {
                    if (jsonob != null) {
                        Log.d("history","History Loaded");

                        history.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                createcards();

                            }
                        });

                      stop = false;

                    }else if(serverhistory.stopflag.equals("failed")){
                        stop = false;

                        history.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                findViewById(R.id.nores_history).setVisibility(View.VISIBLE);
                                findViewById(R.id.progress_history).setVisibility(View.GONE);

                            }
                        });


                        Log.d("Failed","stopping the thread");
                    }else if(serverhistory.con_excep.equals("conexcep")){
                        history.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(history.this,"Something Went Wrong.. Please try again!!",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    Thread.sleep(500);
                    Log.d("history","Loading history");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        lh.stopthread(false);
    }

    public void createcards(){
        findViewById(R.id.progress_history).setVisibility(View.GONE);

        try {
            if(!jsonob.isNull("sname")) {

                findViewById(R.id.rv_history).setVisibility(View.VISIBLE);

                RecyclerView rv = (RecyclerView) findViewById(R.id.rv_history);
                RVAnormalcard adapter = new RVAnormalcard(arr_history);
                rv.setAdapter(adapter);

                rv.setHasFixedSize(true);
                LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                rv.setLayoutManager(llm);

                JSONArray url_image = jsonob.getJSONArray("image");
                //Log.d("services: ", packArr.toString());
                final String[] urlarr = new String[url_image.length()];
                for (int i = 0; i < url_image.length(); i++) {
                    urlarr[i] = url_image.getString(i);
                    Log.d("images of salon: ", urlarr[i]);
                }

                JSONArray jsonArr = jsonob.getJSONArray("services");    //services
                Log.d("history object ", jsonArr.toString());
                String[] ser = new String[jsonArr.length()];
                for (int i = 0; i < jsonArr.length(); i++) {
                    ser[i] = jsonArr.getString(i).substring(2);
                    Log.d("history", ser[i]);
                }

                jsonArr = jsonob.getJSONArray("sname");                  // salon name
                Log.d("history object ", jsonArr.toString());
                String[] name = new String[jsonArr.length()];
                for (int i = 0; i < jsonArr.length(); i++) {
                    name[i] = jsonArr.getString(i);
                    Log.d("history", name[i]);
                }
                jsonArr = jsonob.getJSONArray("bdate");                 // booking date
                Log.d("history object ", jsonArr.toString());
                String[] bdate = new String[jsonArr.length()];
                for (int i = 0; i < jsonArr.length(); i++) {
                    bdate[i] = jsonArr.getString(i);
                    Log.d("history", bdate[i]);
                }

                jsonArr = jsonob.getJSONArray("btime");                 // booking time
                Log.d("history object ", jsonArr.toString());
                String[] btime = new String[jsonArr.length()];
                for (int i = 0; i < jsonArr.length(); i++) {
                    btime[i] = jsonArr.getString(i);
                    Log.d("history", btime[i]);
                }
                jsonArr = jsonob.getJSONArray("tprice");            // booking price
                Log.d("history object ", jsonArr.toString());
                String[] bprice = new String[jsonArr.length()];
                for (int i = 0; i < jsonArr.length(); i++) {
                    bprice[i] = jsonArr.getString(i);
                    Log.d("history", bprice[i]);
                }


                for (int i = 0; i < ser.length; i++) {
                    NormalCardData list = new NormalCardData();

                    list.setname(name[i]);
                    list.setpacks(ser[i]);
                    list.setclassname("history");
                    list.setbdate(bdate[i]);
                    list.seturl(urlarr[i]);
                    list.setcontext(getApplication());
                    list.setbtime(btime[i]);
                    list.setopri(bprice[i]);
                    arr_history.add(list);
                }
            }
            else{
                findViewById(R.id.nores_history).setVisibility(View.VISIBLE);
            }


        }catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
