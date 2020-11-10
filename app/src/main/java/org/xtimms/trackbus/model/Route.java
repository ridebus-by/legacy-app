package org.xtimms.trackbus.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "route", foreignKeys = {
        @ForeignKey(entity = City.class, parentColumns = "_id", childColumns = "city_id"),
        @ForeignKey(entity = Transport.class, parentColumns = "_id", childColumns = "transport_id"),
        @ForeignKey(entity = KindRoute.class, parentColumns = "_id", childColumns = "kindRoute_id")},
        indices = {@Index("city_id"), @Index("transport_id"), @Index("kindRoute_id")})
public class Route extends DatabaseObject implements Serializable {
    @ColumnInfo(name = "_id")
    @PrimaryKey(autoGenerate = true)
    private int mId;

    @ColumnInfo(name = "city_id")
    private int mCityId;

    @ColumnInfo(name = "transport_id")
    private int mTransportId;

    @ColumnInfo(name = "kindRoute_id")
    private int mKindRouteId;

    @ColumnInfo(name = "routeNumber")
    private String mRouteNumber;

    @ColumnInfo(name = "routeTitle")
    private String mRouteTitle;

    public Route(int id, int cityId, int transportId, int kindRouteId, String routeNumber, String routeTitle) {
        this.mId = id;
        this.mCityId = cityId;
        this.mTransportId = transportId;
        this.mKindRouteId = kindRouteId;
        this.mRouteNumber = routeNumber;
        this.mRouteTitle = routeTitle;
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

    public String getRouteNumber() {
        return mRouteNumber;
    }

    public void setRouteNumber(String routeNumber) {
        this.mRouteNumber = routeNumber;
    }

    public String getRouteTitle() {
        return mRouteTitle;
    }

    public void setRouteTitle(String routeTitle) {
        this.mRouteTitle = routeTitle;
    }

    @Override
    public String getTitle() {
        return mRouteTitle;
    }

    @Override
    public String getNumber() {
        return mRouteNumber;
    }

    @Override
    public boolean isEmpty() {
        return mRouteNumber == null;
    }
}