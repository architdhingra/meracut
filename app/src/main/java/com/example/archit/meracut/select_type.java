package com.example.archit.meracut;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


public class select_type extends Activity implements LocationListener {

    SharedPreferences sharedPref;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    String provider;
    Intent intent=null;
    int currentapiVersion;
    int permissionCheck, permissionCheck2 = 0;
    protected String latitude = "", longitude = "";
    protected boolean gps_enabled, network_enabled;
    private ProgressDialog mProgressDialog;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        currentapiVersion = android.os.Build.VERSION.SDK_INT;

        if (currentapiVersion >= 21) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(R.layout.activity_select_type);


        Log.d("current api", String.valueOf(currentapiVersion));
        Log.d("lollipop api", String.valueOf(Build.VERSION_CODES.LOLLIPOP));


        //this.getWindow().setStatusBarColor(getApplication().getResources().getColor(R.color.colorPrimaryDark));
        intent = new Intent(getApplicationContext(), MainActivity.class);

        checkpermission();


        LocationManager lm = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if (currentapiVersion <= 22) {
            if (!gps_enabled && !network_enabled) {
                // notify user
                AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
                dialog.setMessage("Location Not Enabled, Click to Enable!");
                dialog.setCancelable(true)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // TODO Auto-generated method stub
                                        Intent myIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        startActivity(myIntent);
                                        //get gps
                                    }
                                });
                dialog.show();
            }
        }

        sharedPref = getApplicationContext().getSharedPreferences("meracut", MODE_PRIVATE);

    }

    @Override
    public void onDestroy(){

        Log.d("ondestroy", "destroyed");
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Location", "latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
        //Toast.makeText(this, "latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("lat", String.valueOf(latitude));
        editor.putString("lon", String.valueOf(longitude));
        editor.commit();

        if (currentapiVersion > 22) {
            Log.d("test","crash1");
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        locationManager.removeUpdates(this);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Location", "yoo  ");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Location", "yooo2");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Location", "yoo3");
    }


    public void spa(View v) {
        showProgressDialog();
        intent.putExtra("type", "spa");
        LoadMainActivity lm = new LoadMainActivity();
        lm.start();


    }

    public void tatoo(View v) {
        showProgressDialog();
        intent.putExtra("type", "tattoo");
        LoadMainActivity lm = new LoadMainActivity();
        lm.start();
    }

    public void salon(View v) {
        showProgressDialog();
        intent.putExtra("type", "Salon");
        LoadMainActivity lm = new LoadMainActivity();
        lm.start();

    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, Please enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }

    protected void onResume() {
        super.onResume();
        Log.d("onresume","entered");

        if (currentapiVersion > 22) {
            Log.d("test","crash2");
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("select_type", "resuming select_type activity");
                return;
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    protected void onPause() {
        super.onPause();
       /* if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;}
        Log.d("select_type","Removing Location Listener");*/
        //locationManager.removeUpdates(locationListener);

    }


    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Getting Your Location, Please Wait");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void askPerm(){

        if (currentapiVersion > 22) {
            Log.d("test","crash3");
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            } else
                permissionCheck2 = 1;
        }

    }

    public class LoadMainActivity extends Thread {
        boolean stop = true;

        public void stopthread(boolean flag){
            this.stop = flag;
        }

        @Override
        public void run() {
            while (stop) {
                try {
                    if(latitude!=""){
                        Log.d("timer","LATITUDE FOUND, EXITING TIMER THREAD");
                        intent.putExtra("lat", latitude);
                        intent.putExtra("lon", longitude);
                        select_type.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideProgressDialog();}});
                        startActivity(intent);
                        stop = false;

                    }
                    Log.d("timer","latitude no found");
                    Thread.sleep(500);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void checkpermission() {



        if (currentapiVersion > 22) {
            Log.d("test","crash4");

            permissionCheck = ContextCompat.checkSelfPermission(select_type.this, Manifest.permission.ACCESS_COARSE_LOCATION);
            Log.d("permisionn:", String.valueOf(permissionCheck));

            if (permissionCheck != -1) {
                statusCheck();
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                Log.d("Location", "Location manager");
            }
            askPerm();
        }else{

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        }
    }
}
