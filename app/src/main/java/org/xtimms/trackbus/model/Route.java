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
        @ForeignKey(entity = KindRoute.class, parentColumns = "_id", childColumns = "kindRoute_id"),
        @ForeignKey(entity = ClassTransport.class, parentColumns = "_id", childColumns = "transportClass_id")},
        indices = {@Index("city_id"), @Index("transport_id"), @Index("kindRoute_id"), @Index("transportClass_id")})
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

    @ColumnInfo(name = "fare")
    private String mFare;

    @ColumnInfo(name = "traffic")
    private String mTraffic;

    @ColumnInfo(name = "working_time")
    private String mWorkingTime;

    @ColumnInfo(name = "transportClass_id")
    private int mTransportClassId;

    @ColumnInfo(name = "paymentMethods")
    private String mPaymentMethods;

    @ColumnInfo(name = "itinerary")
    private String mItinerary;

    @ColumnInfo(name = "company")
    private String mCompany;

    @ColumnInfo(name = "technicalInformation")
    private String mTech;

    public Route(int id, int cityId, int transportId, int kindRouteId, String routeNumber, String routeTitle, String fare, String traffic, String workingTime, int transportClassId, String paymentMethods, String itinerary, String company, String tech) {
        this.mId = id;
        this.mCityId = cityId;
        this.mTransportId = transportId;
        this.mKindRouteId = kindRouteId;
        this.mRouteNumber = routeNumber;
        this.mRouteTitle = routeTitle;
        this.mFare = fare;
        this.mTraffic = traffic;
        this.mWorkingTime = workingTime;
        this.mTransportClassId = transportClassId;
        this.mPaymentMethods = paymentMethods;
        this.mItinerary = itinerary;
        this.mCompany = company;
        this.mTech = tech;
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

    public String getFare() {
        return mFare;
    }

    public void setFare(String fare) {
        this.mFare = fare;
    }

    public String getTraffic() {
        return mTraffic;
    }

    public void setTraffic(String traffic) {
        this.mTraffic = traffic;
    }

    public String getWorkingTime() {
        return mWorkingTime;
    }

    public void setWorkingTime(String workingTime) {
        this.mWorkingTime = workingTime;
    }

    public int getTransportClassId() {
        return mTransportClassId;
    }

    public void setTransportClassId(int transportClassId) {
        this.mTransportClassId = transportClassId;
    }

    public String getPaymentMethods() {
        return mPaymentMethods;
    }

    public void setPaymentMethods(String paymentMethods) {
        this.mPaymentMethods = paymentMethods;
    }

    public String getItinerary() {
        return mItinerary;
    }

    public void setItinerary(String itinerary) {
        this.mItinerary = itinerary;
    }

    public String getCompany() {
        return mCompany;
    }

    public void setCompany(String company) {
        this.mCompany = company;
    }

    public String getTech() {
        return mTech;
    }

    public void setTech(String tech) {
        this.mTech = tech;
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