package com.example.archit.meracut;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchResults extends AppCompatActivity {

    ServerRequest sq_search;
    JSONObject jsonob;
    public ArrayList<NormalCardData> arr_search = new ArrayList<>() ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);


        Intent intent = getIntent();
        String query = intent.getStringExtra("query");

        showResults(query);

    }

    public void showResults(String query) {
        // Query your data set and show results
        String[] para = {query};
        sq_search = new ServerRequest(para, "http://www.wademy.in/meracut/search.php");
        //sq_search.js=null;
        sq_search.execute();
        LoadSearchResults.start();
    }


    Thread LoadSearchResults = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                jsonob = sq_search.js;
                try {
                    if (jsonob != null) {

                         /*------------------------ card_search -----------------------------*/

                        //final String name = jsonob.getString("name");

                        SearchResults.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                findViewById(R.id.progress_search).setVisibility(View.GONE);

                                try{
                                    if(!jsonob.isNull("name")) {
                                    String[] urlarr = null;

                                    if(!jsonob.isNull("image")) {
                                        Log.d("loading images search","loading images search");
                                        JSONArray url_image = jsonob.getJSONArray("image");
                                        //Log.d("services: ", packArr.toString());
                                        urlarr = new String[url_image.length()];
                                        for (int i = 0; i < url_image.length(); i++) {
                                            urlarr[i] = url_image.getString(i);
                                            Log.d("images of salon: ", urlarr[i]);
                                        }
                                    }

                                    JSONArray jsonArr = jsonob.getJSONArray("name");
                                    Log.d("search names: ", jsonArr.toString());
                                    String[] search = new String[jsonArr.length()];
                                    for (int i = 0; i < jsonArr.length(); i++) {
                                        search[i] = jsonArr.getString(i);

                                    }

                                    jsonArr = jsonob.getJSONArray("id");
                                    Log.d("search id: ", jsonArr.toString());
                                    String[] search_id = new String[jsonArr.length()];
                                    for (int i = 0; i < jsonArr.length(); i++) {
                                        search_id[i] = jsonArr.getString(i);

                                    }


                                        findViewById(R.id.rv_search).setVisibility(View.VISIBLE);

                                        RecyclerView rv_search = (RecyclerView) findViewById(R.id.rv_search);
                                        RVAnormalcard searchadapter = new RVAnormalcard(arr_search);
                                        rv_search.setAdapter(searchadapter);
                                        rv_search.setHasFixedSize(true);
                                        LinearLayoutManager llm2 = new LinearLayoutManager(getApplicationContext());
                                        llm2.setOrientation(LinearLayoutManager.VERTICAL);
                                        rv_search.setLayoutManager(llm2);


                                        for (int k = 0; k < search.length; k++) {

                                            NormalCardData list = new NormalCardData();

                                            list.setname(search[k]);
                                            list.setclassname("search");
                                            list.setcontext(getApplication());
                                            list.setid(search_id[k]);
                                            if (urlarr != null) {
                                                list.seturl(urlarr[k]);
                                            }

                                            arr_search.add(list);

                                        }
                                    }else{
                                        findViewById(R.id.nores_search).setVisibility(View.VISIBLE);
                                    }

                                }catch(JSONException e){
                                    e.printStackTrace();
                                }

                            }

                        });

                        break;
                    }else if(sq_search.con_excep.equals("conexcep")){
                        SearchResults.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SearchResults.this,"Something Went Wrong.. Please try again!!",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    Log.d("json: ", "not found");
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            sq_search.js = null;
        }
    });

}
