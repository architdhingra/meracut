package com.example.archit.meracut;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends BaseActivity {


    ViewPager mPager;
    TestFragmentAdapter adapter;
    public ArrayList<NormalCardData> arr_nearby = new ArrayList<>() ;
    public ArrayList<NormalCardData> arr_package = new ArrayList<>() ;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static String url_new_user = "http://www.wademy.in/meracut/salonType.php";
    RecyclerView rv;
    ImageView img;
    Bitmap bitmap;
    String[] stylesarr,stylesurlarr;
    Intent ix;
    loadContent loadmaincontent=null;
    String type,lat,lon,id;
    int flag_con = 0;
    JSONObject json;
    public ArrayList<StyleNameData> stylenames = new ArrayList<StyleNameData>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*----------------------getting typee from intent -------------------*/
        ix = new Intent(this, FirstActivity.class);
        ix.putExtra("lat", lat);
        ix.putExtra("lon", lon);
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("meracut", MODE_PRIVATE);
        flag_con = 0 ;
        ProgressBar pb =(ProgressBar) findViewById(R.id.progress_style);
        pb.getIndeterminateDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        ProgressBar pb1 =(ProgressBar) findViewById(R.id.progress_nearby);
        pb1.getIndeterminateDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        ProgressBar pb2 =(ProgressBar) findViewById(R.id.progress_pack);
        pb2.getIndeterminateDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        Intent typeintent = getIntent();

        type = typeintent.getStringExtra("type");
        lat = typeintent.getStringExtra("lat");
        lon = typeintent.getStringExtra("lon");

        if (type == null) {type = sharedPref.getString("type",null);}
        if (lat == null)  {lat = sharedPref.getString("lat",null);}
        if (lon == null)  {lon = sharedPref.getString("lon",null);}

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("lat",lat);
        editor.putString("lon",lon);
        editor.putString("type",type);
        editor.commit();
        Log.d("mainactivity location: ",lat+" :: " + lon);
        Log.d("shared prefs ","type: "+sharedPref.getString("type",null));
        Log.d("shared prefs ","lat: "+sharedPref.getString("lat",null));
        Log.d("shared prefs ","lon: "+sharedPref.getString("lon",null));

        /*-------------------------------------------------------------------*/


        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawer, getApplicationContext().getTheme());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            getSupportActionBar().setHomeAsUpIndicator(drawable);
        }
        mDrawerList.setItemChecked(position, true);


        Thread Loadwhenconnected = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        NetConnection cd = new NetConnection(getApplicationContext());
                        if(cd.isConnectingToInternet()) {                                       // CHECKING INTERNET CONNECTION

                            ////////////////////////////////GCM CODE///////////////////////////////////////////////////////////


                            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                                @Override
                                public void onReceive(Context context, Intent intent) {

                                    SharedPreferences sharedPreferences =
                                            PreferenceManager.getDefaultSharedPreferences(context);
                                    boolean sentToken = sharedPreferences
                                            .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                                }
                            };


                            registerReceiver();

                            if (checkPlayServices()) {
                                // Start IntentService to register this application with GCM.
                                Log.d("tag", "before intent");
                                Intent intent = new Intent(getApplicationContext(), RegistrationIntentService.class);
                                startService(intent);
                                Log.d("tag", "after intent");
                            }

                            ////////////////////////////////GCM CODE END/////////////////////////////////////////////////////////

                            Log.d("tag", "above executee");
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    findViewById(R.id.nores_nearby).setVisibility(View.GONE);
                                    findViewById(R.id.nores_package).setVisibility(View.GONE);
                                    findViewById(R.id.nores_styles).setVisibility(View.GONE);

                                    findViewById(R.id.progress_nearby).setVisibility(View.VISIBLE);
                                    findViewById(R.id.progress_pack).setVisibility(View.VISIBLE);
                                    findViewById(R.id.progress_style).setVisibility(View.VISIBLE);

                                }
                            });
                            loadmaincontent = new loadContent();
                            loadmaincontent.execute();

                            Log.d("tag", "below executee");
                            break;
                        }
                        if(flag_con == 0){
                            flag_con = 1;

                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    findViewById(R.id.progress_nearby).setVisibility(View.GONE);
                                    findViewById(R.id.progress_pack).setVisibility(View.GONE);
                                    findViewById(R.id.progress_style).setVisibility(View.GONE);

                                    findViewById(R.id.nores_nearby).setVisibility(View.VISIBLE);
                                    findViewById(R.id.nores_package).setVisibility(View.VISIBLE);
                                    findViewById(R.id.nores_styles).setVisibility(View.VISIBLE);
                                    //Toast.makeText(getApplicationContext(), "You are not connected to internet!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }


                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        Loadwhenconnected.start();

        /*------------------------------ view pager ------------------------------------*/
        adapter = new TestFragmentAdapter(getSupportFragmentManager());
        Log.d("Adapter", adapter.toString());
        mPager = (ViewPager)findViewById(R.id.pager);
        Log.d("pager", mPager.toString());
        mPager.setAdapter(adapter);
        Log.d("Adapter", adapter.toString());
        InkPageIndicator inkPageIndicator = (InkPageIndicator) findViewById(R.id.indicator);
        inkPageIndicator.setViewPager(mPager);
        //inkPageIndicator.;
        /*-------------------------------------------------------*/


        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;


        dimensions.setdimen(height, width);

       /* if(sharedPref.getBoolean("onetime",true)) {
            SharedPreferences.Editor edit = sharedPref.edit();
            edit.putBoolean("onetime",false);
            edit.commit();
            createPopup();
        }*/

        //CreateStyleName();
    }

    @Override
    public void onResume(){
        super.onResume();
        /*SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("meracut", MODE_PRIVATE);
        Log.d("shared resume prefs ","type: "+sharedPref.getString("type",null));
        Log.d("shared resume prefs ","lat: "+sharedPref.getString("lat",null));
        Log.d("shared resume prefs ","lon: "+sharedPref.getString("lon",null));*/
        /*lat = sharedPref.getString("lat",null);
        lon = sharedPref.getString("lon",null);
        type = sharedPref.getString("type",null);
        Log.d("Resuming","MainActivity");
        new loadContent().execute();*/
    }

    @Override
    public void onPause(){
        super.onPause();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    //TODO: Reset your views
                    return false;
                }
            });
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {

                    Log.d("search",s.toString());

                    Intent intent = new Intent(MainActivity.this ,SearchResults.class);
                    intent.putExtra("query",s.toString());
                    startActivity(intent);

                    return false; //do the default
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    //NOTE: doing anything here is optional, onNewIntent is the important bit
                    if (s.length() > 1) { //2 chars or more
                        //TODO: filter/return results
                    } else if (s.length() == 0) {
                        //TODO: reset the displayed data
                    }
                    return false;
                }

            });
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.search){
            onSearchRequested();
        }

        return super.onOptionsItemSelected(item);
    }


    public void salon(View a){
        startActivity(new Intent(this,Salon.class));
    }


    public void createPopup(){

        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.popup, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

        alertDialogBuilder.setView(promptsView);

        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(ix);
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        final CheckBox male = (CheckBox)promptsView.findViewById(R.id.filter_male);
        final CheckBox female = (CheckBox)promptsView.findViewById(R.id.filter_female);
        final CheckBox unisex = (CheckBox)promptsView.findViewById(R.id.filter_unisex);
        final CheckBox salon = (CheckBox)promptsView.findViewById(R.id.filter_salon);
        final CheckBox spa = (CheckBox)promptsView.findViewById(R.id.filter_spa);
        final CheckBox tatto = (CheckBox)promptsView.findViewById(R.id.filter_tatto);

        if(sharedPref.getString("sex",null)!=null){
            if(sharedPref.getString("sex",null).equals("m")){
                male.setChecked(true);
            }else if(sharedPref.getString("sex",null).equals("f")){
                female.setChecked(true);
            }else if(sharedPref.getString("sex",null).equals("")){
                unisex.setChecked(true);
            }
        }

        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /*if (male.isChecked()) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("sex", "m");
                    editor.commit();
                } else if (!male.isChecked() && !female.isChecked()) {
                    male.setChecked(true);
                }
                if (female.isChecked() && male.isChecked()) {
                    female.setChecked(false);
                }*/
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("sex", "m");
                editor.commit();
                if(female.isChecked()){
                    female.setChecked(false);
                }
                if(unisex.isChecked()){
                    unisex.setChecked(false);
                }


            }
        });

        female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /*if (female.isChecked()) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("sex", "f");
                    editor.commit();
                } else if (!male.isChecked() && !female.isChecked()) {
                    female.setChecked(true);
                }
                if (male.isChecked() || female.isChecked()) {
                    male.setChecked(false);
                }*/
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("sex", "f");
                editor.commit();

                if(male.isChecked()){
                    male.setChecked(false);
                }
                if(unisex.isChecked()){
                    unisex.setChecked(false);
                }



            }
        });

        unisex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /*if (unisex.isChecked()) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("sex", "");
                    editor.commit();
                } else if (!male.isChecked() && !female.isChecked() && !unisex.isChecked()) {
                    unisex.setChecked(true);
                }
                if (male.isChecked() || female.isChecked()) {
                    male.setChecked(false);
                    female.setChecked(false);
                }*/
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("sex", "");
                editor.commit();

                if(female.isChecked()){
                    female.setChecked(false);
                }
                if(male.isChecked()){
                    male.setChecked(false);
                }
            }
        });



        spa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /*if (female.isChecked()) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("sex", "f");
                    editor.commit();
                } else if (!male.isChecked() && !female.isChecked()) {
                    female.setChecked(true);
                }
                if (male.isChecked() || female.isChecked()) {
                    male.setChecked(false);
                }*/

                if(salon.isChecked()){
                    salon.setChecked(false);
                }
                if(tatto.isChecked()){
                    tatto.setChecked(false);
                }

                ix.putExtra("type", "spa");

            }
        });
        salon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /*if (female.isChecked()) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("sex", "f");
                    editor.commit();
                } else if (!male.isChecked() && !female.isChecked()) {
                    female.setChecked(true);
                }
                if (male.isChecked() || female.isChecked()) {
                    male.setChecked(false);
                }*/


                if(spa.isChecked()){
                    spa.setChecked(false);
                }
                if(tatto.isChecked()){
                    tatto.setChecked(false);
                }

                ix.putExtra("type", "Salon");

            }
        });
        tatto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /*if (female.isChecked()) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("sex", "f");
                    editor.commit();
                } else if (!male.isChecked() && !female.isChecked()) {
                    female.setChecked(true);
                }
                if (male.isChecked() || female.isChecked()) {
                    male.setChecked(false);
                }*/


                if(salon.isChecked()){
                    salon.setChecked(false);
                }
                if(spa.isChecked()){
                    spa.setChecked(false);
                }
                ix.putExtra("type", "tattoo");


            }
        });

    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("TAG", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /////////////////////////// get json string from server /////////////////////

    class loadContent extends AsyncTask<String, String, String> {

        int flag = 0;

        @Override
        protected void onPreExecute() {
            Log.d("Loadcontent","Inside Pre-Execute");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d("async","iside doinbackground1" + type);
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("type", type));
            param.add(new BasicNameValuePair("lon", lon));
            param.add(new BasicNameValuePair("lat", lat));
            json = jsonParser.makeHttpRequest(url_new_user,
                    "POST", param);
            Log.d("async","iside doinbackground2");

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (jsonParser.flag.equals("conexp")) {
                        Toast.makeText(MainActivity.this, "Something went Wrong.. Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    /*------------------------- card_nearby ----------------------------------------*/

                    final String obj = json.getString("name");
                    final String dist = json.getString("distance");
                    id = json.getString("id");

                    JSONArray url_image = json.getJSONArray("url_images");
                    //Log.d("services: ", packArr.toString());
                    final String[] urlarr = new String[url_image.length()];
                    for (int i = 0; i < url_image.length(); i++) {
                        urlarr[i] = url_image.getString(i);
                        Log.d("images of salon: ", urlarr[i]);
                    }
                    Log.d("length url", String.valueOf(urlarr.length));

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rv  = (RecyclerView) findViewById(R.id.rv_nearby);
                            rv.setHasFixedSize(true);
                            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                            llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                            rv.setLayoutManager(llm);

                            RVAnormalcard adapter = new RVAnormalcard(arr_nearby);
                            rv.setAdapter(adapter);

                            String[] name1 = obj.split(":");
                            Log.d("length salon", String.valueOf(name1.length));
                            String[] distance = dist.split(":");
                            String[] ids = id.split(":");
                            findViewById(R.id.progress_nearby).setVisibility(View.GONE);
                            for (int i = 1; i < name1.length; i++) {
                                NormalCardData list = new NormalCardData();
                                Log.d("namessss",name1[i]+" : "+ distance[i]);
                                list.setname(name1[i]);
                                list.seturl(urlarr[i-1]);
                                Log.d("loading setting "+(i-1),urlarr[i-1]);
                                list.setdistance(Float.parseFloat(distance[i]));
                                list.setclassname("nearby");
                                list.setid(ids[i]);
                                list.setcontext(getApplication());
                                arr_nearby.add(list);
                                //rv.setAdapter(adapter);
                            }
                            Collections.sort(arr_nearby, new normalcardcomparator());
                            adapter.notifyDataSetChanged();
                        }
                    });


                    /*------------------------------------------ card_packages------------------------*/

                    final String pack_object = json.getString("packages");

                    Log.d("packages:: ",pack_object);

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            findViewById(R.id.progress_pack).setVisibility(View.GONE);

                            try {

                                if(!json.isNull("pack_id")) {
                                    findViewById(R.id.rv_packages).setVisibility(View.VISIBLE);

                                    RecyclerView rv_package = (RecyclerView) findViewById(R.id.rv_packages);
                                    RVAnormalcard packageadapter = new RVAnormalcard(arr_package);
                                    rv_package.setAdapter(packageadapter);

                                    rv_package.setHasFixedSize(true);
                                    LinearLayoutManager llm2 = new LinearLayoutManager(getApplicationContext());
                                    llm2.setOrientation(LinearLayoutManager.HORIZONTAL);
                                    rv_package.setLayoutManager(llm2);



                                    JSONArray url_pack = json.getJSONArray("pack_img");
                                    //Log.d("services: ", packArr.toString());
                                    final String[] urlpack = new String[url_pack.length()];
                                    for (int i = 0; i < url_pack.length(); i++) {
                                        urlpack[i] = url_pack.getString(i);
                                        Log.d("images of packages: ", urlpack[i]);
                                    }

                                    JSONArray salonid = json.getJSONArray("salon_id");
                                    //Log.d("services: ", packArr.toString());
                                    String[] salon_id = new String[salonid.length()];
                                    for (int i = 0; i < salonid.length(); i++) {
                                        salon_id[i] = salonid.getString(i);
                                        Log.d("salon package dprice", salon_id[i]);
                                    }
                                    JSONArray packid = json.getJSONArray("pack_id");
                                    //Log.d("services: ", packArr.toString());
                                    String[] pack_id = new String[packid.length()];
                                    for (int i = 0; i < packid.length(); i++) {
                                        pack_id[i] = packid.getString(i);
                                        Log.d("salon package dprice", pack_id[i]);
                                    }

                                    String[] pack = pack_object.split(";");

                                    Log.d("TAG", "inside package");


                                        Log.d("pckages", "found" + pack.length);
                                        Log.d("pckages id", "found" + pack_id.length);
                                        Log.d("pckages salonid", "found" + salon_id.length);

                                        for (int i = 1; i < pack.length; i++) {
                                            NormalCardData list = new NormalCardData();
                                            String[] arr = pack[i].split(":");
                                            list.setname(arr[0]);
                                            list.setopri(arr[1]);
                                            list.setdpri(arr[2]);
                                            list.setfrom("main");
                                            list.setcontext(getApplication());
                                            list.seturl(urlpack[i - 1]);
                                            list.setid(salon_id[i - 1]);
                                            list.setpackid(pack_id[i - 1]);

                                            String allservices = "";
                                            for (int j = 3; j < arr.length; j++) {
                                                allservices += " + " + arr[j];
                                            }
                                            allservices = allservices.substring(2);
                                            Log.d("main_packages ", allservices);
                                            Log.d("main_packages ", arr[1]);
                                            Log.d("main_packages ", arr[2]);

                                            list.setpacks(allservices);
                                            list.setclassname("packages");
                                            list.setcontext(getApplication());

                                            Log.d("TAG", "inside package2");

                                            arr_package.add(list);
                                            //rv_package.setAdapter(packageadapter);
                                            // Log.d("tag", "adding" + list.name);

                                        }

                                }else {
                                    Log.d("packages", "not found");
                                    findViewById(R.id.text_packages).setVisibility(View.GONE);
                                    findViewById(R.id.rv_packages).setVisibility(View.GONE);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });

                    LoadStyles.start();

                    /*---------------------------------------------------------*/


                   flag = 1;
                    // closing this screen
                    //finish();
                } else {
                    // failed to create user

                    //Log.d("failed to login", json.toString());
                    //Intent in = new Intent(getApplicationContext(),MainActivity.class);
                    //startActivity(in);
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
    }



    //////////////////////// load image from server ///////////////////////////////

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading Image ....");
            pDialog.show();*/

        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {

            if(image != null){
                img.setImageBitmap(image);
                //pDialog.dismiss();

            }else{

                //pDialog.dismiss();
                Toast.makeText(MainActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void CreateStyleName(){
        findViewById(R.id.progress_style).setVisibility(View.GONE);

        findViewById(R.id.gridview).setVisibility(View.VISIBLE);
        ScrollableGridView gridview = (ScrollableGridView) findViewById(R.id.gridview);
        gridview.setAdapter(new StyleNameGridViewAdapter(this,stylenames));
        gridview.setExpanded(true);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Log.d("style activity","style clicked");
                Intent intent = new Intent(MainActivity.this,all_stylename.class);
                intent.putExtra("listname",stylesarr);
                intent.putExtra("listurl",stylesurlarr);
                startActivity(intent);
            }
        });
    }

    Thread LoadStyles = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    if (json != null) {

                        Log.d("json","styles found");

                        /*---------------- names --------------------*/
                        JSONArray jsonstyles = json.getJSONArray("stylename");
                        Log.d("services: ", jsonstyles.toString());
                        stylesarr = new String[jsonstyles.length()];
                        for (int i = 0; i < jsonstyles.length(); i++) {
                            stylesarr[i] = jsonstyles.getString(i);
                        }
                        /*-------------------- URL --------------------*/
                        JSONArray jsonstylesurl = json.getJSONArray("url");
                        Log.d("services: ", jsonstylesurl.toString());
                        stylesurlarr = new String[jsonstylesurl.length()];
                        for (int i = 0; i < jsonstylesurl.length(); i++) {
                            stylesurlarr[i] = jsonstylesurl.getString(i);
                        }
                        /*-----------------------------------------------*/

                        for(int i=0;i<stylesarr.length;i++) {
                            Log.d("stylename: ",stylesarr[i]);
                            Log.d("styleurl: ",stylesurlarr[i]);
                            stylenames.add(new StyleNameData(stylesarr[i],stylesurlarr[i],getApplication()));
                        }


                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                CreateStyleName();
                            }
                        });

                        break;
                    }
                    Log.d("json: ", "styles not found");
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