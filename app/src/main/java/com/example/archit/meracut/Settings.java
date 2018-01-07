package com.example.archit.meracut;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    SharedPreferences sharedPref;
    //private GoogleApiClient mGoogleApiClient;
    LinearLayout log;
    Boolean signedin = false;


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        signedin = sharedPref.getBoolean("signin", false);

        if(Build.VERSION.SDK_INT>=23) {
            if (!android.provider.Settings.System.canWrite(getApplicationContext())) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                startActivity(intent);
            }
        }

        log = (LinearLayout)findViewById(R.id.logout);

        log.setOnClickListener(logout);


/*
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestEmail()
                .requestScopes(Plus.SCOPE_PLUS_LOGIN, Plus.SCOPE_PLUS_PROFILE, new Scope("https://www.googleapis.com/auth/plus.profile.emails.read"))
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();


*/
    }

    private View.OnClickListener logout = new View.OnClickListener() {
        public void onClick(View v) {
            if(signedin == true) {
                Toast.makeText(getApplicationContext(), "Logged out",Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor edit = sharedPref.edit();
                edit.putBoolean("signin",false);
                edit.commit();
                startActivity(new Intent(getApplicationContext(),loginScreen.class));
            }else{
                Toast.makeText(getApplicationContext(), "You are not Signed in!!" + "\n" + " \t Please Sign In.", Toast.LENGTH_SHORT).show();
            }
           // Intent intent = new Intent(getApplicationContext(), loginActivity.class);
           // startActivity(intent);
        }
    };
/*
    private void signOut() {
        if(mGoogleApiClient.isConnected()) {
        /*mGoogleApiClient.clearDefaultAccountAndReconnect();
        mGoogleApiClient.disconnect();*/
       /* Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        Log.d(TAG,"signed out status "+status);
                        // [END_EXCLUDE]
                    }
                });*/
/*            mGoogleApiClient.clearDefaultAccountAndReconnect();

            revokeAccess();

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("signin", false);
            editor.commit();
            Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show();
        }
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
        mGoogleApiClient.disconnect();
    }

*/
    public void share(View v){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Take a look at \"Meracut\" - https://play.google.com/store/apps/details?id=com.example.archit.meracut");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void policy(View cc){

        String url = "http://www.meracut.com";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);

    }




}
