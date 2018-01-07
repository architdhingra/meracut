package com.example.archit.meracut;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TimeSlots extends AppCompatActivity {

    String sid;
    int totalprice;
    public ArrayList<String> arrid = new ArrayList<>();
    int year_x, month_x, day_x;
    static final int dialog_id = 1;
    String date;
    String time;
    public TimeSlotsAdapter timeadapter;
    JSONObject jsonobject;
    TextView b;
    ArrayList<String> packages_list = new ArrayList<String>();
    ServerRequest serverbook;
    private ProgressDialog mProgressDialog;
    public ArrayList<StyleNameData> time_list = new ArrayList<StyleNameData>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slots);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        sid = intent.getStringExtra("sid");
        totalprice = intent.getIntExtra("totalprice", 0);
        arrid = intent.getStringArrayListExtra("arrid");
        showdate();
        b = (TextView) findViewById(R.id.dateid);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdate();
            }
        });
        Log.d("Time Slots", sid + "::" + totalprice + "::" + arrid);

        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        packages_list = Services_Packages.getpacks();




    }


    public void showdate() {
        showDialog(dialog_id);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == dialog_id) {
            Calendar cl = Calendar.getInstance();
            int date = cl.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);

            dialog.getDatePicker().setMinDate(cl.getTimeInMillis());
            cl.add(Calendar.DAY_OF_MONTH, 30);
            dialog.getDatePicker().setMaxDate(cl.getTimeInMillis());
            return dialog;
        } else {
            return super.onCreateDialog(id);
        }
    }

    private DatePickerDialog.OnDateSetListener dpickerListener =
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    year_x = year;
                    month_x = monthOfYear + 1;
                    day_x = dayOfMonth;
                    date = year_x + "-" + month_x + "-" + day_x;
                    b.setText("Your Selected Date is: "+date+"\n Click to change");
                    setGridView();
                    //Toast.makeText(TimeSlots.this, date, Toast.LENGTH_LONG).show();

                }
            };

    public void bookappoint(View v) {

        if (date == null && time == null) {

            Toast.makeText(TimeSlots.this, "Please Select Date and Time Of The Appointment", Toast.LENGTH_LONG).show();

        } else if (date == null) {
            Toast.makeText(TimeSlots.this, "Please Select a Date", Toast.LENGTH_LONG).show();
        } else if (time == null) {
            Toast.makeText(TimeSlots.this, "Please Select a Time Slot", Toast.LENGTH_LONG).show();
        } else {
            book();
        }

    }

    public void book() {
        showProgressDialog();
        String tprice = String.valueOf(totalprice);
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("meracut", MODE_PRIVATE);

        Log.d("booking", "Booking Your Appintment With Details as follow: ");
        Log.d("booking", "service id's :" + String.valueOf(arrid));
        Log.d("booking", "Salon id :" + sid);
        Log.d("booking", "Date :" + date);
        Log.d("booking", "Time :" + time);
        Log.d("booking", "Time :" + sharedPref.getString("number", "00"));


        String[] para = {sid, tprice, date, time, sharedPref.getString("number", "")};
        Log.d("Check", String.valueOf(arrid));
        if (arrid.isEmpty()){
            serverbook = new ServerRequest(packages_list, para, "http://www.wademy.in/meracut/app/booking.php", "packages");
            serverbook.execute();
            t.start();
        }
        else if (packages_list.isEmpty()) {
            serverbook = new ServerRequest(arrid, para, "http://www.wademy.in/meracut/app/booking.php", "services");
            serverbook.execute();
            t.start();
        }
        else
        {
            serverbook = new ServerRequest(arrid, packages_list, para, "http://www.wademy.in/meracut/app/booking.php");
            serverbook.execute();
            t.start();
        }
    }


    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                Log.d("booking", "confirming");
                jsonobject = serverbook.js;
                //Log.d("json object",jsonobject.toString());

                try {
                    if (jsonobject != null) {
                        TimeSlots.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideProgressDialog();
                            }
                        });

                        final String status = jsonobject.getString("status");
                        if (status.equals("Confirmed")) {
                            TimeSlots.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("booking", "confirmed");
                                    Toast.makeText(TimeSlots.this, "Your Appointment has been booked", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                            });
                        } else {
                            TimeSlots.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("booking", "not confirmed");
                                    Toast.makeText(TimeSlots.this, "Error! Your Appointment could not be booked.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        break;
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

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Booking Appointment, Please Wait!!");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }


    public void setGridView() {

        time_list.clear();
        ArrayList<String> times = getTimeSlots();

        for (int i = 0; i < times.size(); i++) {
            time_list.add(new StyleNameData(times.get(i)));
        }

        final ScrollableGridView gridview = (ScrollableGridView) findViewById(R.id.gridview_time);

        timeadapter = new TimeSlotsAdapter(this,time_list);
        gridview.setAdapter(timeadapter);
        gridview.setExpanded(true);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                for (int i = 0; i < gridview.getChildCount(); i++) {

                    LinearLayout ll = (LinearLayout) gridview.getChildAt(i);
                    TextView tx = (TextView) ll.getChildAt(0);
                    tx.setBackgroundColor(getResources().getColor(R.color.white));
                    tx.setTextColor(getResources().getColor(R.color.black));
                }

                LinearLayout ll = (LinearLayout) v;
                TextView tx = (TextView) ll.getChildAt(0);

                tx.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tx.setTextColor(getResources().getColor(R.color.white));
                time = tx.getText().toString();

            }
        });
    }


    public ArrayList getTimeSlots(){

        final Calendar cal = Calendar.getInstance();
        int currenthour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        ArrayList<String> timeslots = new ArrayList<>();

        DateFormat dateFormat = new SimpleDateFormat("h:mm a");

        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 10);
        now.set(Calendar.MINUTE, 0);
        Log.d("times Starting",dateFormat.format(now.getTime()));
        int i=0;

        Log.d("day",day_x+"::"+cal.get(Calendar.DAY_OF_MONTH));
        Log.d("Month",month_x+"::"+(cal.get(Calendar.MONTH)+1));

        if((cal.get(Calendar.MONTH)+1) == month_x && cal.get(Calendar.DAY_OF_MONTH ) == day_x){
            Log.d("Today","Today");
            while(true){

                now.add(Calendar.MINUTE,20);

                if(dateFormat.format(now.getTime()).contains("10") && dateFormat.format(now.getTime()).contains("p")){
                    break;
                }else{

                    if(now.get(Calendar.HOUR_OF_DAY) >= currenthour) {

                        if(now.get(Calendar.HOUR_OF_DAY) == currenthour){

                            if(now.get(Calendar.MINUTE)>=minute){
                                //Log.d("timess", dateFormat.format(now.getTime()));
                                timeslots.add(dateFormat.format(now.getTime()));
                                i++;
                            }

                        }else{
                            //Log.d("timess", dateFormat.format(now.getTime()));
                            timeslots.add(dateFormat.format(now.getTime()));
                            i++;
                        }
                    }
                }
            }
        }else{
            Log.d("Other day","Other day");
            while(true){

                now.add(Calendar.MINUTE,20);

                if(dateFormat.format(now.getTime()).contains("10") && dateFormat.format(now.getTime()).contains("p")){
                    break;
                }else{

                    timeslots.add(dateFormat.format(now.getTime()));

                }
            }

        }
        for(int j=0;j<timeslots.size();j++){
            Log.d("times slots",timeslots.get(j));
        }
        return timeslots;
    }


}
