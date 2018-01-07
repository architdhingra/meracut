package com.example.archit.meracut;


import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class RVAnormalcard extends RecyclerView.Adapter<RVAnormalcard.docViewHolder> {


    private ArrayList<NormalCardData> arrList = new ArrayList<>();
    int height;
    int width;
    Bitmap bitmap;
    public ArrayList<String> arrid_pack = new ArrayList<>();

    RVAnormalcard(ArrayList<NormalCardData> arrList){

        this.arrList = arrList;

        notifyItemChanged(0,arrList.size());
    }

    public static class docViewHolder extends RecyclerView.ViewHolder{
        public View itemview;
        CardView cv;
        TextView Name;
        TextView distance;
        ImageView salonphoto;
        ImageView packphoto;
        CheckBox pack_check;

        TextView pack_Name;
        TextView combo_pack;
        TextView opri;
        TextView dpri;
        TextView discount;
        TextView serv_avail, btime, bdate, bprice;





        public docViewHolder(final View itemView) {
            super(itemView);
            this.itemview = itemView;
            cv = (CardView)itemView.findViewById(R.id.cv_nearby);
            Name = (TextView)itemView.findViewById(R.id.name);
            distance = (TextView)itemView.findViewById(R.id.dist);
            salonphoto = (ImageView)itemView.findViewById(R.id.doc_photo);
            packphoto = (ImageView)itemView.findViewById(R.id.pack_photo);

            pack_Name = (TextView) itemView.findViewById(R.id.pack_name);
            combo_pack = (TextView) itemView.findViewById(R.id.combo_pack);
            opri = (TextView) itemView.findViewById(R.id.opri);
            dpri = (TextView) itemView.findViewById(R.id.dpri);
            discount = (TextView) itemView.findViewById(R.id.package_discount);

            serv_avail = (TextView) itemView.findViewById(R.id.serv_availed_text);
            bprice = (TextView) itemView.findViewById(R.id.serv_bprice_text);
            btime = (TextView) itemView.findViewById(R.id.serv_btime_text);
            bdate = (TextView) itemView.findViewById(R.id.serv_bdate_text);

            pack_check = (CheckBox)itemView.findViewById(R.id.pack_check);



            }

    }


    @Override
    public docViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = null;
        Log.d("position: ",i+" : "+arrList.get(i).cardname);
        if((arrList.get(i).cardname.toString()).equals("nearby")){
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_nearby, viewGroup, false);
            LinearLayout layout = (LinearLayout)v.findViewById(R.id.layout_nearby);

            int h= (int) ( dimensions.getheight()*0.4);
            int w= (int) ( dimensions.getwidth()*0.45);
            Log.d("tag", "dimens: " + w + "::" + h);
            layout.setLayoutParams(new LinearLayout.LayoutParams(w, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
        else if((arrList.get(i).cardname.toString()).equals("packages")){
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_packages, viewGroup, false);
            TextView tx = (TextView)v.findViewById(R.id.opri);
            tx.setBackgroundResource(R.drawable.cross);
            LinearLayout layout = (LinearLayout)v.findViewById(R.id.layout_pack);

            int h= (int) ( dimensions.getheight()*0.4);
            int w= (int) ( dimensions.getwidth()*0.45);
            Log.d("tag", "dimens: " + w + "::" + h);
            layout.setLayoutParams(new LinearLayout.LayoutParams(w, ViewGroup.LayoutParams.WRAP_CONTENT));

        }
        else if((arrList.get(i).cardname.toString()).equals("history")){
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_history, viewGroup, false);
        }
		else if((arrList.get(i).cardname.toString()).equals("search")){
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_search, viewGroup, false);
        }
        else if((arrList.get(i).cardname.toString()).equals("styles")){
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_allstyles, viewGroup, false);
        }
        docViewHolder pvh = new docViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final docViewHolder DocViewHolder, final int i) {

        String image_url = "http://www.meracut.com/app/images/img3.jpg";

        if((arrList.get(i).cardname.toString()).equals("nearby")) {
            DocViewHolder.Name.setText(arrList.get(i).name);
            float f = arrList.get(i).distance;
            DecimalFormat df = new DecimalFormat("#.0");

            DocViewHolder.distance.setText(df.format(f)+"km");


            if(!arrList.get(i).url.isEmpty()) {
                String[] url = arrList.get(i).url.split(";");
                Log.d("Loading image",url[0]);
                image_url = url[0];

            }

            Picasso.with(arrList.get(i).aa)
                    .load(image_url)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.img4)
                    .into(DocViewHolder.salonphoto);

        }


        if((arrList.get(i).cardname.toString()).equals("packages")) {

            Log.d("tag","inside packages docviewholder");

            if(!arrList.get(i).url.isEmpty()) {
                String[] url = arrList.get(i).url.split(";");
                Log.d("Loading image",url[0]);
                image_url = url[0];

            }

            Picasso.with(arrList.get(i).aa)
                    .load(image_url)
                    .into(DocViewHolder.packphoto);

            DocViewHolder.pack_Name.setText(arrList.get(i).name);
            DocViewHolder.combo_pack.setText(arrList.get(i).packname);
            DocViewHolder.opri.setText(arrList.get(i).opri);
            DocViewHolder.dpri.setText(arrList.get(i).dpri);
            DocViewHolder.discount.setText("Flat " + arrList.get(i).discount + "% Off");

            if(arrList.get(i).from.equals("main")) {
                DocViewHolder.pack_check.setVisibility(View.GONE);
            }

            if(arrList.get(i).from.equals("salon")) {
                if(arrList.get(i).id.equals(Salon.packid_main)) {
                    DocViewHolder.pack_check.setChecked(true);
                    arrid_pack.add(arrList.get(i).id);
                    Services_Packages.setpacks(arrid_pack);
                    Services_Packages.settotalprice(Services_Packages.gettotalprice() + Integer.parseInt(arrList.get(i).dpri));
                    TextView tx = Salon.total;
                    tx.setText("Total: " + String.valueOf(Services_Packages.gettotalprice()));
                }


            }



        }


        if((arrList.get(i).cardname.toString()).equals("history")) {

            Log.d("historyRVA","inside history docviewholder");

            DocViewHolder.Name.setText(arrList.get(i).name);
            DocViewHolder.serv_avail.setText(arrList.get(i).packname);
            DocViewHolder.bdate.setText(arrList.get(i).bookingdate);
            DocViewHolder.btime.setText(arrList.get(i).bookingtime);
            DocViewHolder.bprice.setText(arrList.get(i).opri);

            if(!arrList.get(i).url.isEmpty()) {
                String[] url = arrList.get(i).url.split(";");
                Log.d("Loading image",url[0]);
                image_url = url[0];

            }

            Picasso.with(arrList.get(i).aa)
                    .load(image_url)
                    .placeholder(R.drawable.placeholder)
                    .into(DocViewHolder.salonphoto);


        }
		
		if((arrList.get(i).cardname.toString()).equals("search")) {

            Log.d("searchRVA","inside search docviewholder");
            DocViewHolder.Name.setText(arrList.get(i).name);

            if(!arrList.get(i).url.isEmpty()) {
                String[] url = arrList.get(i).url.split(";");
                Log.d("Loading image",url[0]);
                image_url = url[0];

            }

            Picasso.with(arrList.get(i).aa)
                    .load(image_url)
                    .placeholder(R.drawable.placeholder)
                    .into(DocViewHolder.salonphoto);

        }

        if((arrList.get(i).cardname.toString()).equals("styles")) {
            DocViewHolder.Name.setText(arrList.get(i).name);


            if(!arrList.get(i).url.isEmpty()) {
                image_url = arrList.get(i).url;

            }

            Picasso.with(arrList.get(i).aa)
                    .load(image_url)

                    .into(DocViewHolder.salonphoto);

        }



        DocViewHolder.itemview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if((arrList.get(i).cardname.toString()).equals("nearby")) {
                    Log.d("salon clicked",arrList.get(i).id);
                    Intent intent = new Intent(v.getContext(),Salon.class);
                    intent.putExtra("id",arrList.get(i).id);
                    intent.putExtra("images_url",arrList.get(i).url);
                    intent.putExtra("name",arrList.get(i).name);
                    v.getContext().startActivity(intent);
                }
				
				if((arrList.get(i).cardname.toString()).equals("search")) {
                    Log.d("search salon clicked",arrList.get(i).id);
                    Intent intent = new Intent(v.getContext(),Salon.class);
                    intent.putExtra("id",arrList.get(i).id);
                    intent.putExtra("images_url",arrList.get(i).url);
                    intent.putExtra("name",arrList.get(i).name);
                    v.getContext().startActivity(intent);
                }

                if((arrList.get(i).cardname.toString()).equals("packages")) {
                    if((arrList.get(i).from.toString()).equals("main")){

                        Intent intent = new Intent(v.getContext(),Salon.class);
                        intent.putExtra("id",arrList.get(i).id);
                        intent.putExtra("images_url",arrList.get(i).url);
                        intent.putExtra("package_id",arrList.get(i).pack_id);
                        Log.d("pack_main2",arrList.get(i).pack_id);
                        intent.putExtra("name",arrList.get(i).name);
                        v.getContext().startActivity(intent);
                    }
                    else if((arrList.get(i).from.toString()).equals("salon")){

                        if(!DocViewHolder.pack_check.isChecked()) {
                            DocViewHolder.pack_check.setChecked(true);
                            arrid_pack.add(arrList.get(i).id);
                            Services_Packages.setpacks(arrid_pack);
                            Services_Packages.settotalprice(Services_Packages.gettotalprice() + Integer.parseInt(arrList.get(i).dpri));
                            TextView tx = Salon.total;
                            tx.setText("Total: " + String.valueOf(Services_Packages.gettotalprice()));
                        }
                        else if(DocViewHolder.pack_check.isChecked()){
                            DocViewHolder.pack_check.setChecked(false);
                            arrid_pack.remove(arrList.get(i).id);
                            Services_Packages.setpacks(arrid_pack);
                            Services_Packages.settotalprice(Services_Packages.gettotalprice() - Integer.parseInt(arrList.get(i).dpri));
                            TextView tx = Salon.total;
                            tx.setText("Total: " + String.valueOf(Services_Packages.gettotalprice()));

                        }
                    }

                }

            }

        });



    }

    @Override
    public int getItemCount() {

        return arrList.size();
    }



}





