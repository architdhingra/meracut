package com.example.archit.meracut;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

public class all_stylename extends AppCompatActivity {

    ArrayList<NormalCardData> arr_styles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_stylename);

        String[] names = getIntent().getStringArrayExtra("listname");
        String[] url = getIntent().getStringArrayExtra("listurl");

        Log.d("mylist", names[1].toString());
        Log.d("mylist", url[1].toString());

        RecyclerView rv_styles = (RecyclerView) findViewById(R.id.rv_allstyles);
        RVAnormalcard styleadapter = new RVAnormalcard(arr_styles);
        rv_styles.setAdapter(styleadapter);

        rv_styles.setHasFixedSize(true);
        LinearLayoutManager llm2 = new LinearLayoutManager(getApplicationContext());
        llm2.setOrientation(LinearLayoutManager.VERTICAL);
        rv_styles.setLayoutManager(llm2);

        for(int i=0;i<names.length;i++){

            NormalCardData stylelist = new NormalCardData();

            stylelist.setname(names[i]);
            stylelist.seturl(url[i]);
            stylelist.setclassname("styles");
            stylelist.setcontext(getApplication());
            arr_styles.add(stylelist);

        }

    }
}
