package com.example.archit.meracut;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {
    protected FrameLayout frameLayout;

    /**
     * ListView to add navigation drawer item in it.
     * We have made it protected to access it in child class. We will just use it in child class to make item selected according to activity opened.
     */

    protected ListView mDrawerList;

    /**
     * List item array for navigation drawer items.
     * */
    protected String[] listArray = { "Home", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
    protected ArrayList<Items> _items;

    /**
     * Static variable for selected item position. Which can be used in child activity to know which item is selected from the list.
     * */
    protected static int position;

    /**
     *  This flag is used just to check that launcher activity is called first time
     *  so that we can open appropriate Activity on launch and make list item position selected accordingly.
     * */
    private static boolean isLaunch = true;

    /**
     *  Base layout node of this Activity.
     * */
    private DrawerLayout mDrawerLayout;
    SharedPreferences sharedPref;


    /**
     * Drawer listner class for drawer open, close etc.
     */
    public ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_base_layout);
        //getActionBar().setDisplayHomeAsUpEnabled(false);
        //View v = getLayoutInflater().inflate(R.layout.activity_main,null);
        //Toolbar t = (Toolbar) v.findViewById(R.id.toolbar);
        //setSupportActionBar(t);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        frameLayout = (FrameLayout)findViewById(R.id.content_frame);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        //LinearLayout layout = (LinearLayout) findViewById(R.id.layout_nav);

        sharedPref = getApplicationContext().getSharedPreferences("meracut", MODE_PRIVATE);


        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.8);
        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) mDrawerList.getLayoutParams();
        params.width = width;
        mDrawerList.setLayoutParams(params);
        //set a custom shadow that overlays the main content when the drawer opens
        //mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        _items = new ArrayList<Items>();
        _items.add(new Items("Dashboard", R.drawable.ic_home_white_24dp));
        _items.add(new Items("Profile",  R.drawable.ic_account_circle_white_24dp));
        _items.add(new Items("History",  R.drawable.ic_history_white_24dp));
        _items.add(new Items("Settings",  R.drawable.ic_settings_white_24dp));
        _items.add(new Items("Register New Salon",R.drawable.ic_assignment_white_24dp));
        _items.add(new Items("Share",  R.drawable.ic_share_white_24dp));
        _items.add(new Items("Logout",  R.drawable.ic_power_settings_new_white_24dp));
        _items.add(new Items("Call Us", R.drawable.ic_phone_white_24dp));
        _items.add(new Items("FAQ", R.drawable.ic_question_answer_white_24dp));
        _items.add(new Items("About", R.drawable.ic_format_quote_white_24dp));
        //Adding header on list view
        View header = (View)getLayoutInflater().inflate(R.layout.list_view_header_layout, null);
        ImageView icon = (ImageView)header.findViewById(R.id.item_icon_imgview);
        TextView name = (TextView)header.findViewById(R.id.drawer_name);
        TextView email = (TextView)header.findViewById(R.id.drawer_email);
        ImageView iv = (ImageView)header.findViewById(R.id.item_icon_imgview);
        loadBackdrop(iv);
        name.setText(sharedPref.getString("firstname",""));
        email.setText(sharedPref.getString("email","yoyoyoyoyo"));
        mDrawerList.addHeaderView(header);

        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new NavigationDrawerListAdapter(this, _items));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                openActivity(position);
            }
        });


        // enable ActionBar app icon to behave as action to toggle nav drawer
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setHomeButtonEnabled(true);

        //actionBar.setDisplayHomeAsUpEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions between the sliding drawer and the action bar app icon

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,						/* host Activity */
                mDrawerLayout, 				/* DrawerLayout object */
                R.drawable.ic_drawer,     /* nav drawer image to replace 'Up' caret */
                R.string.open_drawer,       /* "open drawer" description for accessibility */
                R.string.close_drawer)      /* "close drawer" description for accessibility */ {
            @Override
            public void onDrawerClosed(View drawerView) {

                //getActionBar().setTitle(listArray[position]);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(getString(R.string.app_name));
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        };

        //actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
/*
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.nav_drawer, getApplicationContext().getTheme());

        actionBarDrawerToggle.setHomeAsUpIndicator(drawable);

        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);



        /**
         * As we are calling BaseActivity from manifest file and this base activity is intended just to add navigation drawer in our app.
         * We have to open some activity with layout on launch. So we are checking if this BaseActivity is called first time then we are opening our first activity.
         * */
        if(isLaunch){
            /**
             *Setting this flag false so that next time it will not open our first activity.
             *We have to use this flag because we are using this BaseActivity as parent activity to our other activity.
             *In this case this base activity will always be call when any child activity will launch.
             */
            isLaunch = false;
            openActivity(0);
        }
    }

    /**
     * @param position
     *
     * Launching activity when any list item is clicked.
     */
    protected void openActivity(int position) {

        /**
         * We can set title & itemChecked here but as this BaseActivity is parent for other activity,
         * So whenever any activity is going to launch this BaseActivity is also going to be called and
         * it will reset this value because of initialization in onCreate method.
         * So that we are setting this in child activity.
         */
//		mDrawerList.setItemChecked(position, true);
       // 9899902414
//		setTitle(listArray[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
        BaseActivity.position = position; //Setting currently selected position in this field so that it will be available in our child activities.

        switch (position) {
            case 1:

                startActivity(new Intent(this, MainActivity.class));
                break;
            case 2:

                startActivity(new Intent(this, profile.class));
                break;
            case 0:
             //   Toast.makeText(this,"clicked",Toast.LENGTH_SHORT).show();
                break;
            case 3:
                startActivity(new Intent(this, history.class));
                break;
            case 4:
                startActivity(new Intent(this, Settings.class));
                break;
            case 5:
                String url = "http://www.meracut.com/join-us";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case 6:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Take a look at \"Meracut\" - https://play.google.com/store/apps/details?id=com.example.archit.meracut");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case 7:
                Toast.makeText(this,"Logged out",Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor edit = sharedPref.edit();
                edit.putBoolean("signin",false);
                edit.commit();
                startActivity(new Intent(this,loginScreen.class));

                break;
            case 8:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:9899892398"));
                startActivity(intent);

                break;
            case 9:

                url = "http://www.meracut.com";
                Intent ii = new Intent(Intent.ACTION_VIEW);
                ii.setData(Uri.parse(url));
                startActivity(ii);
                break;

            case 10:

                Intent ii2 = new Intent(this,about.class);
                startActivity(ii2);
                break;

            default:
                break;
        }

//		Toast.makeText(this, "Selected Item Position::"+position, Toast.LENGTH_LONG).show();
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        /*switch (item.getItemId()) {
  //          case R.id.action_settings:
    //            return true;

            default:*/
                return super.onOptionsItemSelected(item);

    }

    /* Called whenever we call invalidateOptionsMenu() */
   /* @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
      //  menu.findItem(R.id.search).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }*/

    /* We can override onBackPressed method to toggle navigation drawer*/
    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(mDrawerList)){
            mDrawerLayout.closeDrawer(mDrawerList);
        }else {
            super.onBackPressed();
        }
    }
    private void loadBackdrop(ImageView imgView) {
        Bitmap bitmap=null;
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File (sdCard.getAbsolutePath() + "/Meracut");
        File file = new File(directory, "profile.jpg"); //or any other format supported
        Log.d("file path: ", file.toString());

        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 6;
            //bitmap = BitmapFactory.decodeStream(file.toString(), null, options);
            bitmap = BitmapFactory.decodeFile(file.toString(),options);
            try {
                ExifInterface exif = new ExifInterface(file.toString());
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                }
                else if (orientation == 3) {
                    matrix.postRotate(180);
                }
                else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true); // rotating bitmap
            }
            catch (Exception e) {

            }
            imgView.setImageBitmap(bitmap);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

