package com.example.archit.meracut;

import android.app.Application;

import java.util.Comparator;

/**
 * Created by Dhruv on 23-03-2016.
 */
public class NormalCardData {


        String name;
        float distance;
        String cardname;
        String opri;
        String dpri;
        String packname;
        Application aa;
        String id;
        String pack_id;
        String from;
        String discount;
        String bookingdate;
        String bookingtime;
        String url;

        NormalCardData(){

        }

        public void setname(String name){
            this.name = name;
        }
        public void seturl(String urli){
                this.url = urli;
        }
        public void setid(String id){
                this.id = id;
        }
        public void setdistance(float dist){
            this.distance = dist;
        }
        public void setclassname(String card){
            this.cardname = card;
        }
        public void setcontext(Application a){this.aa = a;}
        public Application getapp(){return aa;}


        public void setopri(String opri){
                this.opri = opri;
        }
        public void setdpri(String dpri){
            this.dpri = dpri;
            int d = Integer.parseInt(this.dpri);
            int o = Integer.parseInt(this.opri);
            int s = o-d;
            int dis = (s*100);
            int disc = dis/o;
            this.discount = String.valueOf(disc);

        }

        public void setpacks(String packname){
                this.packname = packname;
        }

        public void setbtime(String btime){
                this.bookingtime = btime;
        }
        public void setfrom(String frm){
                this.from = frm;
        }
        public void setpackid(String id){
                this.pack_id = id;
        }
        public void setbdate(String bdate){
                this.bookingdate = bdate;
        }




}
class normalcardcomparator implements Comparator<NormalCardData>
{

        @Override
        public int compare(NormalCardData lhs, NormalCardData rhs) {
                float change1 = lhs.distance;
                float change2 = rhs.distance;
                if (change1 < change2) return -1;
                if (change1 > change2) return 1;
                return 0;
        }
}
