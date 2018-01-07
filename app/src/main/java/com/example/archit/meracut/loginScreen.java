package com.example.archit.meracut;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class loginScreen extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static String url_new_user = "http://www.wademy.in/meracut/login2.php";
    EditText username, pwd;
    Button login;
    TextView register;
    GoogleSignInAccount acct;
    String usrname, pswd, number, emg;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private static final String TAG_SUCCESS = "success";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    int success;



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int currentapi = android.os.Build.VERSION.SDK_INT;

        if(currentapi >= 21) {
            getWindow().setStatusBarColor(getApplication().getResources().getColor(R.color.colorPrimaryDark));
        }

        setContentView(R.layout.activity_loginscreen);

        checkpermission();

        sharedPref = getApplicationContext().getSharedPreferences("meracut", MODE_PRIVATE);
        editor = sharedPref.edit();

        findViewById(R.id.googlesignin).setOnClickListener(this);

        username = (EditText)findViewById(R.id.username);
        pwd = (EditText)findViewById(R.id.pwd);
        login = (Button)findViewById(R.id.login);
        register = (TextView)findViewById(R.id.register);
        login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if(username.length()!=0 && pwd.length()!=0) {
                    usrname = username.getText().toString();
                    pswd = pwd.getText().toString();
                    NetConnection cd = new NetConnection(getApplicationContext());
                    if(cd.isConnectingToInternet()) {
                        new login().execute();
                    }else{
                        Toast.makeText(loginScreen.this,"You Are Not Connected To Internet",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(getApplication(), "Please Enter Your Details First", Toast.LENGTH_LONG).show();
            }
        });
        register.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),register.class);
                intent.putExtra("from","register");
                startActivity(intent);
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        NetConnection cd = new NetConnection(getApplicationContext());
        if(cd.isConnectingToInternet()) {
            signIn();
        }else{
            Toast.makeText(loginScreen.this,"You Are Not Connected To Internet",Toast.LENGTH_SHORT).show();
        }

        Log.d("TAG","Signing in");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    class login extends AsyncTask<String,String,String> {

        int flag = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(loginScreen.this);
            pDialog.setMessage("Logging In..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {


            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("uname", usrname));
            param.add(new BasicNameValuePair("pname", pswd));

            JSONObject json = jsonParser.makeHttpRequest(url_new_user,
                    "POST", param);

            loginScreen.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (jsonParser.flag.equals("conexp")) {
                        Toast.makeText(loginScreen.this, "Something went Wrong.. Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });



            try {
                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created a user
                    //Intent i = new Intent(getApplicationContext(), startScreen.class);
                    //startActivity(i);
                    number = json.getString("number");
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
            // dismiss the dialog once done
            pDialog.dismiss();
            if (flag == 1){
                Intent i = new Intent(getApplicationContext(), select_type.class);
                editor.putBoolean("signin",true);
                editor.putString("number", number);
                editor.putString("email", usrname);
                editor.putString("sex", "m");
                editor.commit();
                startActivity(i);

            }

            else {
                if (pswd.equals("g+")){
                    gredirect();
                }
                Toast.makeText(getApplication(), "Invalid Password or Id", Toast.LENGTH_SHORT).show();
                username.setText("");
                pwd.setText("");
            }
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

      /*  OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            mGoogleApiClient.connect();
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            mGoogleApiClient.connect();
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }*/
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("TAG", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            Log.d("TAG", "inside success");
            acct = result.getSignInAccount();
            Log.d("Display name: ", acct.getDisplayName());
            Log.d("EMAIL: ",acct.getEmail());

            String name[] = acct.getDisplayName().split(" ");

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("firstname", name[0]);
            editor.putString("lastname", name[1]);
            Log.d("name: ", name[0] + " : " + name[1]);
            emg = acct.getEmail();
            editor.putString("email", acct.getEmail());
            editor.putBoolean("signin", true);
            editor.putString("sex", "m");

            editor.commit();

            usrname = emg;
            pswd = "g+";
            new login().execute();

    }
    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]

                        // [END_EXCLUDE]
                    }
                });
    }


    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void signIn() {
        Log.d("TAG","inside signin");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void checkpermission(){

        /*int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        Log.d("version","test");
        Log.d("version release",":::"+android.os.Build.VERSION.RELEASE);
        Log.d("version int",":::"+currentapiVersion);*/



            String [] PermissionsLocation =
                    {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    };

            int RequestLocationId = 0;
            int permissionCheck = ContextCompat.checkSelfPermission(loginScreen.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
            Log.d("permisionn:", String.valueOf(permissionCheck));
            if (permissionCheck==-1) {
                requestPermissions(PermissionsLocation, RequestLocationId);
            }




    }
    public void gredirect(){

        Intent intent = new Intent(this, register.class);
        intent.putExtra("from","googleplus");
        intent.putExtra("name",acct.getDisplayName());
        intent.putExtra("email",acct.getEmail());
        startActivity(intent);
        finish();
    }

}
