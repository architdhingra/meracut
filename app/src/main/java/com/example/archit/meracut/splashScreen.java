package com.example.archit.meracut;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import java.io.IOException;

public class splashScreen extends AppCompatActivity {

    Drawable d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView iv = (ImageView) findViewById(R.id.logo);

        try {
            d = Drawable.createFromStream(getAssets().open("launchscreen.png"), null);
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
                    Intent intent = new Intent(splashScreen.this,splashScreen2.class);
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

