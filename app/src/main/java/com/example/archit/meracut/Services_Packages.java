package com.example.archit.meracut;

import java.util.ArrayList;

/**
 * Created by Dhruv on 28-05-2016.
 */
public class Services_Packages{



    private static ArrayList<String> arrservices = new ArrayList<>();
    private static ArrayList<String> arrpacks = new ArrayList<>();
    private static int totalprice;

    public static void setservices(ArrayList<String> array){
        Services_Packages.arrservices = array;
    }

    public static ArrayList<String> getservices(){
        return Services_Packages.arrservices;

    }
    public static void setpacks(ArrayList<String> packarray){
        Services_Packages.arrpacks = packarray;
    }

    public static ArrayList<String> getpacks(){
        return Services_Packages.arrpacks;

    }
    public static void settotalprice(int price){
        Services_Packages.totalprice = price;
    }

    public static int gettotalprice(){
        return Services_Packages.totalprice;

    }


}
