package com.example.ism2022;


import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName="fxrates")
public class FXRate implements Serializable {
    private String date;
    private String euro;
    private String dolar;
    private String pound;
    private String gold;
    @PrimaryKey(autoGenerate = true)
    private int id;
    @Ignore
    private String uid;


    public FXRate(String date, String euro, String dolar, String pound, String gold) {
        this.date = date;
        this.euro = euro;
        this.dolar = dolar;
        this.pound = pound;
        this.gold = gold;
    }
@Ignore
    public FXRate() {
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEuro() {
        return euro;
    }

    public void setEuro(String euro) {
        this.euro = euro;
    }

    public String getDolar() {
        return dolar;
    }

    public void setDolar(String dolar) {
        this.dolar = dolar;
    }

    public String getPound() {
        return pound;
    }

    public void setPound(String pound) {
        this.pound = pound;
    }

    public String getGold() {
        return gold;
    }

    public void setGold(String gold) {
        this.gold = gold;
    }

    @Override
    public String toString() {
        return "FXRate{" + "date='" + date + '\'' + ", euro='" + euro + '\'' + ", dolar='" + dolar + '\'' + ", pound='" + pound + '\'' + ", gold='" + gold + '\'' + '}';
    }
}
