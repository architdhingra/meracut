package com.example.archit.meracut;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.pixelcan.inkpageindicator.InkPageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Salon extends AppCompatActivity {

    public ArrayList<services> arrlist = new ArrayList<>();
    public ArrayList<NormalCardData> arr_pack = new ArrayList<>();
    ViewPager mPager;
    TestFragmentAdapter adapter;
    ScrollView child;
    public static TextView total;
    TextView addrtxt, salon_name_text,sitting,facilities;
    public ArrayList<String> arrid = new ArrayList<>();
    public ArrayList<String> arrid_pack = new ArrayList<>();
    int totalprice;
    Switch service_switch;
    TextView typeservice;
    String sid, urls[], lat = "28.716894", lon="77.12136950000";
    public static String packid_main = "";
    String viewpager_images;
    JSONObject jsonob;
    SharedPreferences.Editor edit;
    SharedPreferences sharedPref;
    ServerRequest sq;
    String salon_name, hair, hairtreat, haircol, spamassage, sex,addr;
    RecyclerView recview = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ProgressBar pb =(ProgressBar) findViewById(R.id.progress_services);
        pb.getIndeterminateDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        ProgressBar pb1 =(ProgressBar) findViewById(R.id.progress_pack_salon);
        pb1.getIndeterminateDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);


        total = (TextView) findViewById(R.id.total);
        sharedPref = getApplicationContext().getSharedPreferences("meracut", MODE_PRIVATE);
        edit = sharedPref.edit();

        salon_name_text = (TextView) findViewById(R.id.salon_name_text);
        addrtxt = (TextView)findViewById(R.id.addrtext);
        sitting = (TextView)findViewById(R.id.sitting_text);
        facilities = (TextView)findViewById(R.id.fac_text);
        Intent intent = getIntent();
        if(intent!=null) {
            Log.d("Salon", "on create:" + intent.getStringExtra("name") + " : " + intent.getStringExtra("id") + " : " + sharedPref.getString("sex", "m"));
        }

        salon_name = intent.getStringExtra("name");

        if(intent.getStringExtra("package_id")!=null) {
            packid_main = intent.getStringExtra("package_id");
            Log.d("pack_main","::"+intent.getStringExtra("package_id"));
        }
        if(intent.getStringExtra("images_url")!=null) {
            viewpager_images = intent.getStringExtra("images_url");

            urls = viewpager_images.split(";");
        }
        Log.d("check","::"+viewpager_images);

        sid = intent.getStringExtra("id");
        sex = sharedPref.getString("sex","");

        String[] para = {sid, sharedPref.getString("sex", "")};

        salon_name_text.setText(salon_name);

        Log.d("Sex", "sex : " + sex);

        sq = new ServerRequest(para, "http://www.wademy.in/meracut/salonServices.php");
        sq.execute();
        t.start();

        /*------------------------------ view Pager -------------------------------*/
        Log.d("url length", String.valueOf(urls.length));
        if (urls.length<=1){
            adapter = new TestFragmentAdapter(getSupportFragmentManager());
        }
        else {
            adapter = new TestFragmentAdapter(getSupportFragmentManager(), urls);
        }
        Log.d("Adapter", adapter.toString());
        mPager = (ViewPager) findViewById(R.id.pager);
        Log.d("pager", mPager.toString());
        mPager.setAdapter(adapter);
        Log.d("Adapter", adapter.toString());
        InkPageIndicator inkPageIndicator = (InkPageIndicator) findViewById(R.id.indicator);
        inkPageIndicator.setViewPager(mPager);
        /*-----------------------------------------------------*/

        service_switch = (Switch)findViewById(R.id.service_switch);
        service_switch.setTextOn("male");
        service_switch.setTextOff("Female");
        typeservice = (TextView)findViewById(R.id.type_service_text);
        if(sharedPref.getString("sex","m").equals("m")){
            service_switch.setChecked(false);
            typeservice.setText("Male");

        }else if(sharedPref.getString("sex","m").equals("f")){
            service_switch.setChecked(true);
            typeservice.setText("Female");
        }

        service_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(sharedPref.getString("sex","m").equals("m")){
                    //service_switch.setChecked(false);
                    edit.putString("sex","f");
                    edit.commit();

                }else if(sharedPref.getString("sex","m").equals("f")){
                    //service_switch.setChecked(true);
                    edit.putString("sex","m");
                    edit.commit();
                }

                Intent ix = new Intent(Salon.this, FirstActivity.class);
                ix.putExtra("sid",sid);
                ix.putExtra("name",salon_name);
                ix.putExtra("package_id",packid_main);
                ix.putExtra("images_url",viewpager_images);
                startActivity(ix);
                finish();
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.salon, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.navigate) {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?q=" + lat + "," + lon));
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void choosetime(View v) {

        Log.d("service",Services_Packages.getservices().toString());
        Log.d("service_packages",Services_Packages.getpacks().toString());

        if (!Services_Packages.getservices().isEmpty() || !Services_Packages.getpacks().isEmpty()) {
            Log.d("checkarr",arrid.toString());
            Intent intent = new Intent(this, TimeSlots.class);
            intent.putExtra("totalprice", totalprice);
            intent.putExtra("sid", sid);
            intent.putExtra("arrid", arrid);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Select Something Before Booking The Appointment", Toast.LENGTH_SHORT).show();
        }
        Log.d("Array id: ", String.valueOf(arrid));
    }

    public void checkbox(View view) {
        LinearLayout linear = (LinearLayout) view;
        Log.d("child of linear: ", String.valueOf(linear.getChildCount()));

        for (int i = 0; i < linear.getChildCount(); i++) {
            Log.d("inside for", "yoyo");
            View v = (View) linear.getChildAt(i);

            if (v instanceof RelativeLayout) {
                RelativeLayout relative = (RelativeLayout) v;

                for (int j = 0; j < relative.getChildCount(); j++) {
                    Log.d("inside for", "yoyo");

                    View v2 = (View) relative.getChildAt(j);
                    if (v2 instanceof CheckBox) {

                        CheckBox check = (CheckBox) v2;
                        View v3 = (View) relative.getChildAt(0);
                        if (v3 instanceof LinearLayout) {
                            Log.d("inside Linear", "inside");
                            LinearLayout ll2 = (LinearLayout) v3;
                            TextView price = (TextView) ll2.getChildAt(1);
                            TextView dprice = (TextView) ll2.getChildAt(3);
                            Log.d("clicked tag: ", dprice.getTag().toString());

                            if (check.isChecked()) {
                                Log.d("total price: ", "removed:" + dprice.getTag().toString());
                                arrid.remove(dprice.getTag().toString());
                                Services_Packages.setservices(arrid);
                                totalprice = Integer.parseInt(dprice.getText().toString());
                                Services_Packages.settotalprice(Services_Packages.gettotalprice() - totalprice);
                                total.setText("Total: " + Services_Packages.gettotalprice());
                                check.setChecked(false);
                            } else {
                                arrid.add(dprice.getTag().toString());
                                Services_Packages.setservices(arrid);
                                check.setChecked(true);
                                totalprice = Integer.parseInt(dprice.getText().toString());
                                Services_Packages.settotalprice(Services_Packages.gettotalprice() + totalprice);
                                total.setText("Total: " + Services_Packages.gettotalprice());
                                Log.d("total price: ", String.valueOf(totalprice));

                            }
                        }
                    }
                }
            }
        }
    }

    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                jsonob = sq.js;
                try {
                    if (jsonob != null) {
                         /*------------------------ card_services -----------------------------*/

                        if(!jsonob.isNull("address")){
                            addr = jsonob.getString("address");
                        }else{addr = "Not Available";}

                        final String sit = jsonob.getString("seating");
                        final String fac = jsonob.getString("facility");
                        lat = jsonob.getString("lat");
                        lon = jsonob.getString("lon");
                        Log.d("salon:",sit + fac);
                        Salon.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                findViewById(R.id.progress_services).setVisibility(View.GONE);

                                addrtxt.setText(addr);
                                sitting.setText(sit);
                                facilities.setText(fac);
                                Log.d("tag", "mainact : " + R.layout.activity_main);

                                Log.d("json object", "found:");
                                String[] servicenames = {"Hair", "Hair Treatment", "Spa and Massage", "Hair Color", "Cleanup", "Manicure", "Pedicure", "Makeup", "Facial", "Bleach", "Waxing"};
                                String[] prices = {"hair", "hairtr", "spa", "color", "clean", "manicure", "pedicure", "makeup", "facial", "bleech", "wax"};

                                for (int k = 0; k < servicenames.length; k++) {
                                    services services = new services();
                                    JSONArray jsonArr;
                                    Log.d("service name", servicenames[k]);
                                    try {
                                        /*--------------- services --------------*/
                                        jsonArr = jsonob.getJSONArray(servicenames[k]);
                                        Log.d("services: ", jsonArr.toString());
                                        String[] ser = new String[jsonArr.length()];
                                        for (int i = 0; i < jsonArr.length(); i++) {
                                            ser[i] = jsonArr.getString(i);

                                        }
                                        /*--------------- price -----------------*/
                                        JSONArray jsonpri = jsonob.getJSONArray("price_" + prices[k]);
                                        Log.d("services: ", jsonpri.toString());
                                        String[] price = new String[jsonpri.length()];
                                        for (int i = 0; i < jsonpri.length(); i++) {
                                            price[i] = jsonpri.getString(i);
                                        }
                                        /*--------------- dprice -----------------*/
                                        JSONArray jsondpri = jsonob.getJSONArray("dprice_" + prices[k]);
                                        Log.d("services: ", jsondpri.toString());
                                        String[] dprice = new String[jsondpri.length()];
                                        for (int i = 0; i < jsondpri.length(); i++) {
                                            dprice[i] = jsondpri.getString(i);
                                        }
                                        /*------------------ id -------------------*/
                                        JSONArray jsonid = jsonob.getJSONArray("id_" + prices[k]);
                                        Log.d("services: ", jsonid.toString());
                                        String[] id = new String[jsonid.length()];
                                        for (int i = 0; i < jsonid.length(); i++) {
                                            id[i] = jsonid.getString(i);
                                            Log.d("ids:", id[i]);
                                        }
                                        /*-----------------------------------------*/

                                        services.setColor("#48ea38");
                                        services.setServicename(servicenames[k]);
                                        services.setservices(ser);
                                        services.setprice(price);
                                        services.setdprice(dprice);
                                        services.setid(id);

                                        arrlist.add(services);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }

                                createservices();

                            }


                        });

                        //Log.d("salon package object", String.valueOf(jsonob.getJSONArray("packages")));
                        /*------------------------------ card_packages------------------------*/

                        //Log.d("salon package object", String.valueOf(jsonob.getJSONArray("packages")));

                        Salon.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                findViewById(R.id.progress_pack_salon).setVisibility(View.GONE);


                                try {

                                    /*JSONArray url_pack = jsonob.getJSONArray("pack_img");
                                    //Log.d("services: ", packArr.toString());
                                    final String[] urlpack = new String[url_pack.length()];
                                    for (int i = 0; i < url_pack.length(); i++) {
                                        urlpack[i] = url_pack.getString(i);
                                        Log.d("images of packages: ", urlpack[i]);
                                    }*/
                                    if(!jsonob.getString("packages").isEmpty()) {
                                        findViewById(R.id.rv_packages).setVisibility(View.VISIBLE);

                                        JSONArray packArr = jsonob.getJSONArray("packages");
                                        //Log.d("services: ", packArr.toString());
                                        String[] packnames = new String[packArr.length()];
                                        for (int i = 0; i < packArr.length(); i++) {
                                            packnames[i] = packArr.getString(i).substring(2);
                                            Log.d("salon_package", packnames[i]);
                                        }

                                        JSONArray packpriArr = jsonob.getJSONArray("packages_price");
                                        //Log.d("services: ", packArr.toString());
                                        String[] packpri = new String[packpriArr.length()];
                                        for (int i = 0; i < packpriArr.length(); i++) {
                                            packpri[i] = packpriArr.getString(i);
                                            Log.d("salon package price", packpri[i]);
                                        }
                                        JSONArray packdpriArr = jsonob.getJSONArray("packages_dprice");
                                        //Log.d("services: ", packArr.toString());
                                        String[] packdpri = new String[packdpriArr.length()];
                                        for (int i = 0; i < packdpriArr.length(); i++) {
                                            packdpri[i] = packdpriArr.getString(i);
                                            Log.d("salon package dprice", packdpri[i]);
                                        }
                                        JSONArray packid = jsonob.getJSONArray("packages_id");
                                        //Log.d("services: ", packArr.toString());
                                        String[] pack_id = new String[packid.length()];
                                        for (int i = 0; i < packid.length(); i++) {
                                            pack_id[i] = packid.getString(i);
                                            Log.d("salon package dprice", pack_id[i]);
                                        }

                                        RecyclerView rv_package = (RecyclerView) findViewById(R.id.rv_packages);
                                        RVAnormalcard packageadapter = new RVAnormalcard(arr_pack);
                                        rv_package.setAdapter(packageadapter);
                                        rv_package.setHasFixedSize(true);
                                        LinearLayoutManager llm2 = new LinearLayoutManager(getApplicationContext());
                                        llm2.setOrientation(LinearLayoutManager.HORIZONTAL);
                                        rv_package.setLayoutManager(llm2);


                                        Log.d("TAG", "inside package");

                                        for (int i = 0; i < packnames.length; i++) {
                                            NormalCardData list = new NormalCardData();

                                            list.setname(salon_name);
                                            list.setpacks(packnames[i]);
                                            list.setopri(packpri[i]);
                                            list.setdpri(packdpri[i]);
                                            list.setclassname("packages");
                                            list.setfrom("salon");
                                            list.seturl(urls[0]);
                                            list.setid(pack_id[i]);
                                            list.setcontext(getApplication());

                                            Log.d("TAG", "inside package2");

                                            arr_pack.add(list);

                                        }
                                    }else{
                                        findViewById(R.id.rv_packages).setVisibility(View.GONE);
                                        findViewById(R.id.salonpack_text).setVisibility(View.GONE);

                                    }

                                }catch (JSONException e){
                                    e.printStackTrace();
                                }

                            }
                        });

                        break;
                    }else if(sq.con_excep.equals("conexcep")){
                        Salon.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            Toast.makeText(Salon.this,"Something Went Wrong.. Please try again!!",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    Log.d("json: ", "not found");
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    });

    public void createservices() {

        if (arrlist.size() > 0) {
            findViewById(R.id.horizontalscroll).setVisibility(View.VISIBLE);

            int size = arrlist.size();

            Log.d("createservices:", "size : " + size);

            for (int i = 0; i < size; i++) {
                int id = 0;
                if (arrlist.get(i).servicename.equals("Hair")) {
                    id = R.id.serv_hair;
                }
                if (arrlist.get(i).servicename.equals("Hair Treatment")) {
                    id = R.id.serv_hairtreatment;
                }
                if (arrlist.get(i).servicename.equals("Spa and Massage")) {
                    id = R.id.serv_spa_massage;
                }
                if (arrlist.get(i).servicename.equals("Hair Color")) {
                    id = R.id.serv_hair_color;
                }
                if (arrlist.get(i).servicename.equals("Cleanup")) {
                    id = R.id.serv_cleanup;
                }
                if (arrlist.get(i).servicename.equals("Manicure")) {
                    id = R.id.serv_manicure;
                }
                if (arrlist.get(i).servicename.equals("Pedicure")) {
                    id = R.id.serv_pedicure;
                }
                if (arrlist.get(i).servicename.equals("Makeup")) {
                    id = R.id.serv_makeup;
                }
                if (arrlist.get(i).servicename.equals("Facial")) {
                    id = R.id.serv_facial;
                }
                if (arrlist.get(i).servicename.equals("Bleach")) {
                    id = R.id.serv_bleach;
                }
                if (arrlist.get(i).servicename.equals("Waxing")) {
                    id = R.id.serv_waxing;
                }


                if (id != 0) {

                    findViewById(id).setVisibility(View.VISIBLE);

                    CardView cv = (CardView) findViewById(id);

                    LinearLayout ll = (LinearLayout) cv.getChildAt(0);  //adding services to this layout

                    Log.d("createservices", "Service name : " + arrlist.get(i).servicename);

                    for (int j = 0; j < arrlist.get(i).services.length; j++) {
                        Log.d("createservices", "adding service: " + arrlist.get(i).services[j]);
                        View v2 = getLayoutInflater().inflate(R.layout.service_layout, null);
                        TextView servicename = (TextView) v2.findViewById(R.id.servname);
                        servicename.setText(arrlist.get(i).services[j]);
                        TextView price = (TextView) v2.findViewById(R.id.price);
                        price.setText(arrlist.get(i).price[j]);
                        TextView dprice = (TextView) v2.findViewById(R.id.dprice);
                        dprice.setText(arrlist.get(i).dprice[j]);
                        dprice.setTag(arrlist.get(i).id[j]);
                        Log.d("set tag: ", arrlist.get(i).id[j]);
                        ll.addView(v2);
                    }
                }

            }

        }else{
            findViewById(R.id.no_services).setVisibility(View.VISIBLE);
            TextView no_serv = (TextView) findViewById(R.id.no_services);
            if(sharedPref.getString("sex","m").equals("m")) {
                no_serv.setText("Oops!! No Services Found for Males");
            }else if(sharedPref.getString("sex","m").equals("f")){
                no_serv.setText("Oops!! No Services Found for Females");
            }

        }
    }

}
