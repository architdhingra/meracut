package com.example.archit.meracut;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class profile extends BaseActivity{

    private static final int REQUEST_CAMERA = 7;
    private static final int SELECT_FILE = 6;
    EditText getfrname, getsecname, getemail, work, education,phone;
    CheckBox male, female;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    DatePicker datePicker;
    static  final int CAM_REQUEST = 3;
    ImageView imgView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_profile, frameLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgView = (ImageView) findViewById(R.id.backdrop);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawer, getApplicationContext().getTheme());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            getSupportActionBar().setHomeAsUpIndicator(drawable);
        }
        mDrawerList.setItemChecked(position, true);


        getfrname = (EditText)findViewById(R.id.frstname);
        getsecname = (EditText)findViewById(R.id.secname);
        getemail = (EditText)findViewById(R.id.emailid);
        work = (EditText)findViewById(R.id.dob);
        education = (EditText)findViewById(R.id.num);
        phone = (EditText)findViewById(R.id.num);
        male = (CheckBox)findViewById(R.id.malecheck);
        female = (CheckBox)findViewById(R.id.femalecheck);
        //datePicker = (DatePicker)findViewById(R.id.datePicker);


        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("meracut", MODE_PRIVATE);

        getfrname.setText(sharedPref.getString("firstname", null));
        getsecname.setText(sharedPref.getString("lastname", null));
        getemail.setText(sharedPref.getString("email", null));
        work.setText(sharedPref.getString("work", null));
        phone.setText(sharedPref.getString("number",null));
        education.setText(sharedPref.getString("education", null));

        if(sharedPref.getString("sex",null)!=null){
            if(sharedPref.getString("sex",null).equals("m")){
                male.setChecked(true);
            }else if(sharedPref.getString("sex",null).equals("f")){
                female.setChecked(true);
            }
        }




        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(sharedPref.getString("firstname", null));

        loadBackdrop();


        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (male.isChecked()) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("sex", "m");
                    editor.commit();
                } else if (!male.isChecked() && !female.isChecked()) {
                    female.setChecked(true);
                }
                if (female.isChecked() && male.isChecked()) {
                    female.setChecked(false);
                }
            }
        });

        female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (female.isChecked()) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("sex", "f");
                    editor.commit();
                } else if (!male.isChecked() && !female.isChecked()) {
                    male.setChecked(true);
                }
                if (male.isChecked() && female.isChecked()) {
                    male.setChecked(false);
                }
            }
        });

    }


    public void add(View view){

        String frstname = getfrname.getText().toString();
        String secname = getsecname.getText().toString();
        String email = getemail.getText().toString();
        String workinfo = work.getText().toString();
        String eduinfo = education.getText().toString();
        String phoneno = phone.getText().toString();


        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("meracut", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("firstname", frstname);
        editor.putString("lastname", secname);
        editor.putString("email", email);
        editor.putString("work", workinfo);
        editor.putString("education", eduinfo);
        editor.putString("number", phoneno);

        editor.commit();

        Toast.makeText(getApplicationContext(), "Profile Saved Successfully", Toast.LENGTH_SHORT).show();

    }

    private void loadBackdrop() {
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

    public void loadImage() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                SaveImage(BitmapFactory.decodeFile(imgDecodableString));

                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));




                }
            else if (requestCode == CAM_REQUEST && resultCode == RESULT_OK) {

                loadBackdrop();


            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }



    }

    private void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Meracut");
        myDir.mkdirs();

        String fname = "profile.jpg";
        String nomedia = ".nomedia";
        File file = new File (myDir, fname);
        File file2 = new File (myDir, nomedia);

        if (file.exists ()) file.delete ();
        try {
            if(!file2.exists()){
                FileOutputStream out2 = new FileOutputStream(file2);
                out2.flush();
                out2.close();
            }

            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    public void dobPicker(View v){

        datePicker.setVisibility(View.VISIBLE);

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void loadcamera(){

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Meracut");
        myDir.mkdirs();

        File myFile = new File(myDir + "/profile.jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(myFile));

        startActivityForResult(intent, CAM_REQUEST);


    }



    public void selectImage(View view) {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(profile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    loadcamera();
                } else if (items[item].equals("Choose from Library")) {
                    loadImage();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }




}
