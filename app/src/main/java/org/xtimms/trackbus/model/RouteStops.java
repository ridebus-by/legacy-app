package org.xtimms.trackbus.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "routeStops", foreignKeys = {
        @ForeignKey(entity = Route.class, parentColumns = "_id", childColumns = "route_id"),
        @ForeignKey(entity = Stop.class, parentColumns = "_id", childColumns = "stop_id")},
        indices = {@Index("route_id"), @Index("stop_id")})
public class RouteStops {
    @ColumnInfo(name = "_id")
    @PrimaryKey(autoGenerate = true)
    private int mId;

    @ColumnInfo(name = "route_id")
    private int mRouteId;

    @ColumnInfo(name = "stop_id")
    private int mStopId;

    @ColumnInfo(name = "stopNumber")
    private int mStopNumber;

    public RouteStops(int id, int routeId, int stopId, int stopNumber) {
        this.mId = id;
        this.mRouteId = routeId;
        this.mStopId = stopId;
        this.mStopNumber = stopNumber;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public int getRouteId() {
        return mRouteId;
    }

    public void setRouteId(int routeId) {
        this.mRouteId = routeId;
    }

    public int getStopId() {
        return mStopId;
    }

    public void setStopId(int stopId) {
        this.mStopId = stopId;
    }

    public int getStopNumber() {
        return mStopNumber;
    }

    public void setStopNumber(int stopNumber) {
        this.mStopNumber = stopNumber;
    }
}