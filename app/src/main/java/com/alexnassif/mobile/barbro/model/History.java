package com.alexnassif.mobile.barbro.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "history", foreignKeys = @ForeignKey(entity = Drink.class,
        parentColumns = "_id",
        childColumns = "idh"), indices = {@Index("idh")})
public class History {

    @PrimaryKey
    private int _id;

    @ColumnInfo(name = "idh")
    private String idh;
    private Date date;

    public History(int id, String idh, Date date){
        this._id = id;
        this.idh = idh;
        this.date = date;

    }
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
    public String getIdh() {
        return idh;
    }

    public void setIdh(String idh) {
        this.idh = idh;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
