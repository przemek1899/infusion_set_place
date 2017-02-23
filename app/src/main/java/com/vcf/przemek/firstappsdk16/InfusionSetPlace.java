package com.vcf.przemek.firstappsdk16;

import java.util.Date;

/**
 * Created by Przemek on 2017-02-23.
 */

public class InfusionSetPlace {

    int ID;
    private String place;
    private Date date;

    public String getDate_str() {
        return date_str;
    }

    private String date_str;
    private boolean is_working;

    public InfusionSetPlace(){

    }

    public InfusionSetPlace(int id, String place, Date date, String date_str, boolean is_working){
        this.ID = id;
        this.place = place;
        this.date = date;
        this.date_str = date_str;
        this.is_working = is_working;
    }


    public Date getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public boolean is_working() {
        return is_working;
    }

    public int getID() {

        return ID;
    }

}
