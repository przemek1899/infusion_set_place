package com.vcf.przemek.firstappsdk16;

import java.util.Date;

/**
 * Created by Przemek on 2017-02-23.
 */

public class InfusionSetPlace {

    int ID;
    private String place;
    private Date date;
    private boolean is_not_working;

    public InfusionSetPlace(){

    }

    public InfusionSetPlace(int id, String place, Date date, boolean is_not_working){
        this.ID = id;
        this.place = place;
        this.date = date;
        this.is_not_working = is_not_working;
    }


    public Date getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public boolean is_not_working() {
        return is_not_working;
    }

    public int getID() {

        return ID;
    }

}
