package org.xtimms.trackbus.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "city")
public class City extends DatabaseObject {
    @ColumnInfo(name = "_id")
    @PrimaryKey(autoGenerate = true)
    private int mId;
    @ColumnInfo(name = "cityName")
    private String mCityName;

    public City(int id, String cityName) {
        this.mId = id;
        this.mCityName = cityName;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String cityName) {
        this.mCityName = cityName;
    }

    @Override
    public String getTitle() {
        return mCityName;
    }

    @Override
    public String getNumber() {
        return "";
    }

    @Override
    public boolean isEmpty() {
        return mCityName == null;
    }
}