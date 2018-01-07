package com.example.archit.meracut;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class Test extends AppCompatActivity {

    JSONObject j;
    TextView tv;
    ServerRequest sq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String test[] = {"beauty","28.64231","77.178092"};
        sq = new ServerRequest(test, "http://www.meracut.com/app/salonType1.php");
        sq.execute();

        tv = (TextView)findViewById(R.id.test);
        t.start();

    }

    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                j = sq.js;
                try {
                    if(j!=null){
                        Log.d("json object","found: "+j.getString("name"));

                        Test.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    tv.setText(j.getString("name"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        break;
                    }
                    Log.d("json: ","not found");
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    });

}
