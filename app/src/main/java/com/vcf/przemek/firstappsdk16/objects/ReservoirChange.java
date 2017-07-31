package com.vcf.przemek.firstappsdk16.objects;

import java.util.Date;

/**
 * Created by Przemek on 2017-05-02.
 */

public class ReservoirChange {

    public ReservoirChange(int id, Date date){
        this.ID = id;
        this.date = date;
    }

    int ID;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    private Date date;
}

