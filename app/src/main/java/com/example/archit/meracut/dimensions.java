package com.example.archit.meracut;

/**
 * Created by Dhruv on 07-04-2016.
 */
public class dimensions{


    private static int height;
    private static int width;



    public static void setdimen(int h,int w){
            dimensions.height = h;
            dimensions.width = w;
        }
    public static int getwidth(){
        return dimensions.width;
    }
    public static int getheight(){
        return dimensions.height;
    }
}
