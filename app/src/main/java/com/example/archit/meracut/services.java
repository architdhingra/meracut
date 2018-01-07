package com.example.archit.meracut;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Dhruv on 29-03-2016.
 */
public class services {

    public String servicename;

    public String[] services;
    public String[] price;
    public String[] dprice;
    public String[] id;
    public String color;
    public View v=null;
    public ViewGroup vg=null;
    public services() {

    }



    public void setServicename(String itemName) {
        this.servicename = itemName;
    }

    public void setservices(String[] ser1) {
        this.services = ser1;
    }
    public void setprice(String[] pr) {
        this.price = pr;
    }
    public void setdprice(String[] dpr) {
        this.dprice = dpr;
    }
    public void setid(String[] id) {
        this.id = id;
    }

    public void setColor(String color){this.color = color;}
    public void setView(View view){this.v = view;}
    public void setViewgroup(ViewGroup viewgrp){this.vg = viewgrp;}





}
