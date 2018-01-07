package com.example.archit.meracut;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.IOException;

public class splashScreen2 extends AppCompatActivity {

    Drawable d;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPref = getApplicationContext().getSharedPreferences("meracut", MODE_PRIVATE);

        ImageView iv = (ImageView) findViewById(R.id.logo);

        try {
            d = Drawable.createFromStream(getAssets().open("splash_screen.png"), null);
            iv.setBackground(d);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent=null;
                    if(sharedPref.getBoolean("signin",false)==false) {
                        intent = new Intent(splashScreen2.this, loginScreen.class);
                    }else{
                        intent = new Intent(splashScreen2.this, select_type.class);
                    }
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }


}
