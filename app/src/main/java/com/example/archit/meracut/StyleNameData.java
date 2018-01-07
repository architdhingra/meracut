package com.example.archit.meracut;

import android.app.Application;

/**
 * Created by Dhruv on 19-05-2016.
 */
public class StyleNameData {

    private String StyleName;
    private String url;
    private Application aa;

    String timeslots;

    public StyleNameData(String style,String urll, Application a) {
        this.StyleName = style;
        this.url = urll;
        this.aa = a;
    }

    public StyleNameData(String time) {
        this.timeslots = time;

    }

    public String getstylename() {
        return StyleName;
    }

    public void setstylename(String style) {
        this.StyleName = style;
    }
    public String geturl() {
        return url;
    }

    public void seturl(String urll) {
        this.StyleName = urll;
    }

    public String gettime() {
        return timeslots;
    }
    public void setcontext(Application a){this.aa = a;}
    public Application getapp(){return aa;}


}
