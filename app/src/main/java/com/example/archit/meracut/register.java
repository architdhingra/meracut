package com.example.archit.meracut;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class register extends Activity {

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText name,password,Email,Number,Address, Pin;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    Button btnreg, btncancel;
    String Firstname, Password = "g+", email, number, dob="2016-02-01";
    int flag = 0;
    // url to create new product
    private static String url_new_user = "http://www.meracut.com/app/new_user2.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    int flag_login = 0;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int currentapi = android.os.Build.VERSION.SDK_INT;

        if(currentapi >= 21) {
            getWindow().setStatusBarColor(getApplication().getResources().getColor(R.color.colorPrimaryDark));
        }
        setContentView(R.layout.activity_register);


        Intent intent = getIntent();

        if(intent!=null) {
            if (intent.getStringExtra("from").equals("googleplus")) {
                flag_login = 1;
                findViewById(R.id.pwd).setVisibility(View.GONE);
            }
        }


        // Edit Text
        name = (EditText) findViewById(R.id.name);
        name.setText(intent.getStringExtra("name"));

        password = (EditText) findViewById(R.id.pwd);

        Email = (EditText) findViewById(R.id.Email);
        Email.setText(intent.getStringExtra("email"));

        Number = (EditText) findViewById(R.id.Number);


        // Create button
        btnreg = (Button) findViewById(R.id.reg);
        btncancel = (Button) findViewById(R.id.cancel);
        sharedPref = getApplicationContext().getSharedPreferences("meracut", MODE_PRIVATE);
        editor = sharedPref.edit();
        name.setText(sharedPref.getString("firstname",""));

        btnreg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                NetConnection cd = new NetConnection(getApplicationContext());
                if (cd.isConnectingToInternet()) {

                    // creating new product in background thread
                    Firstname = name.getText().toString();
                    Password = password.getText().toString();
                    email = Email.getText().toString();
                    number = Number.getText().toString();

                    if (flag_login != 1) {
                        if (Firstname.length() == 0 || Password.length() == 0 || email.length() == 0 || number.length() == 0 ) {
                            Toast.makeText(getApplication(), "Please Enter All The Details", Toast.LENGTH_SHORT).show();
                        } else {
                            if (Password.length() < 8) {
                                Toast.makeText(getApplication(), "Password should be atleast 8 characters", Toast.LENGTH_SHORT).show();
                            } else if (!email.contains("@") || !email.contains(".")) {
                                Toast.makeText(getApplication(), "Invalid email address", Toast.LENGTH_SHORT).show();
                            } else {
                                new CreateNewProduct().execute();
                            }
                        }
                    } else if (number.isEmpty()) {
                        Toast.makeText(getApplication(), "Please Enter All The Details", Toast.LENGTH_SHORT).show();
                    } else {
                        new CreateNewProduct().execute();
                    }
                } else {
                    Toast.makeText(register.this, "You Are Not Connected To Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
                Intent i = new Intent(getApplicationContext(), loginScreen.class);
                startActivity(i);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_main, container, false);
            return rootView;
        }
    }

    class CreateNewProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(register.this);
            pDialog.setMessage("Registering New User..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", Firstname));
            params.add(new BasicNameValuePair("password", Password));
            params.add(new BasicNameValuePair("dob", dob));
            params.add(new BasicNameValuePair("email",email));
            params.add(new BasicNameValuePair("number",number));
            params.add(new BasicNameValuePair("address","address"));
            params.add(new BasicNameValuePair("pin","110052"));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_new_user,
                    "POST", params);

            register.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (jsonParser.flag.equals("conexp")) {
                        Toast.makeText(register.this, "Something went Wrong.. Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // check log cat from response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created a user
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    flag = 1;
                    // closing this screen
                    finish();
                } else {
                    // failed to create user
                    Log.d("failed to create user", json.toString());
                    flag = 0;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            if (flag == 1) {
                Intent intent = new Intent(getApplicationContext(), select_type.class);
                editor.putString("number", number);
                editor.putBoolean("signin", true);
                editor.commit();
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplication(), "Error", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
