package com.example.archit.meracut;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;

public class FirstActivity extends BaseActivity {
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        Intent typeintent = getIntent();
        String sid = typeintent.getStringExtra("sid");
        String name = typeintent.getStringExtra("name");
        String package_id = typeintent.getStringExtra("package_id");
        String images_url = typeintent.getStringExtra("images_url");

        //String lat = typeintent.getStringExtra("lat");
        //String lon = typeintent.getStringExtra("lon");
        Intent ix = new Intent(this, Salon.class);
        ix.putExtra("id",sid);
        ix.putExtra("name",name);
        ix.putExtra("package_id",package_id);
        ix.putExtra("images_url",images_url);
        finish();
        startActivity(ix);
    }
}
