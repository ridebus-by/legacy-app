package org.xtimms.ridebus.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "stop", foreignKeys = {
        @ForeignKey(entity = City.class, parentColumns = "_id", childColumns = "city_id"),
        @ForeignKey(entity = Transport.class, parentColumns = "_id", childColumns = "transport_id"),
        @ForeignKey(entity = KindRoute.class, parentColumns = "_id", childColumns = "kindRoute_id")},
        indices = {@Index("city_id"), @Index("transport_id"), @Index("kindRoute_id")})
public class Stop extends DatabaseObject implements Serializable {
    @ColumnInfo(name = "_id")
    @PrimaryKey(autoGenerate = true)
    private int mId;

    @ColumnInfo(name = "city_id")
    private int mCityId;

    @ColumnInfo(name = "transport_id")
    private int mTransportId;

    @ColumnInfo(name = "kindRoute_id")
    private int mKindRouteId;

    @ColumnInfo(name = "stopTitle")
    private String mStopTitle;

    @ColumnInfo(name = "mark")
    private String mMark;

    @Ignore
    public Stop() {
    }

    public Stop(int id, int cityId, int transportId, int kindRouteId, String stopTitle, String mark) {
        this.mId = id;
        this.mCityId = cityId;
        this.mTransportId = transportId;
        this.mKindRouteId = kindRouteId;
        this.mStopTitle = stopTitle;
        this.mMark = mark;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public int getCityId() {
        return mCityId;
    }

    public void setCityId(int cityId) {
        this.mCityId = cityId;
    }

    public int getTransportId() {
        return mTransportId;
    }

    public void setTransportId(int transportId) {
        this.mTransportId = transportId;
    }

    public int getKindRouteId() {
        return mKindRouteId;
    }

    public void setKindRouteId(int kindRouteId) {
        this.mKindRouteId = kindRouteId;
    }

    public String getStopTitle() {
        return mStopTitle;
    }

    public void setStopTitle(String stopTitle) {
        this.mStopTitle = stopTitle;
    }

    public String getMark() {
        return mMark;
    }

    public void setMark(String mark) {
        this.mMark = mark;
    }

    @Override
    public String getTitle() {
        return mStopTitle;
    }

    @Override
    public String getNumber() {
        return "";
    }

    @Override
    public boolean isEmpty() {
        return mStopTitle == null;
    }
}