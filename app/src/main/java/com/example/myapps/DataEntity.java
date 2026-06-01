package com.example.myapps;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "data_table")
public class DataEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String numberText;
    public String spinnerText;
    public String date;


    public DataEntity(int id, String numberText, String spinnerText, String date) {
        this.id = id;
        this.numberText = numberText;
        this.spinnerText = spinnerText;
        this.date = date;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumberText() {
        return numberText;
    }

    public void setNumberText(String numberText) {
        this.numberText = numberText;
    }

    public String getSpinnerText() {
        return spinnerText;
    }

    public void setSpinnerText(String spinnerText) {
        this.spinnerText = spinnerText;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}


