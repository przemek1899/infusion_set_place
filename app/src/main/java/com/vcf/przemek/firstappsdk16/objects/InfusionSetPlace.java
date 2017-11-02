package com.vcf.przemek.firstappsdk16.objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Przemek on 2017-02-23.
 */

public class InfusionSetPlace {

    int ID;
    private String place;
    private Date date;
    private long date_long;
    private boolean is_not_working;

    public InfusionSetPlace(){

    }

    public InfusionSetPlace(int id, String place, long date_long, boolean is_not_working){
        this.ID = id;
        this.place = place;
        this.date = new Date(date_long);
        this.date_long = date_long;
        this.is_not_working = is_not_working;
    }


    public long getDate_long() {
        return date_long;
    }

    public void setDate_long(long date_long) {
        this.date_long = date_long;
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

    public static JSONObject toJson(int id, String place, long longTime, boolean not_working){
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("place", place);
            json.put("date", longTime);
            json.put("is_not_working", not_working);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return json;
    }

}
