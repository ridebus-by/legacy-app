package org.xtimms.trackbus.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "schedule", foreignKeys = {
        @ForeignKey(entity = RouteStops.class, parentColumns = "_id", childColumns = "routeStop_id"),
        @ForeignKey(entity = TypeDay.class, parentColumns = "_id", childColumns = "typeDay_id")},
        indices = {@Index("typeDay_id"), @Index("routeStop_id")})
public class Schedule {
    @ColumnInfo(name = "_id")
    @PrimaryKey(autoGenerate = true)
    private int mId;

    @ColumnInfo(name = "routeStop_id")
    private int mRouteStopId;

    @ColumnInfo(name = "typeDay_id")
    private int mTypeDayId;

    @ColumnInfo(name = "time")
    private String mTime;

    public Schedule(int id, int routeStopId, int typeDayId, String time) {
        this.mId = id;
        this.mRouteStopId = routeStopId;
        this.mTypeDayId = typeDayId;
        this.mTime = time;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public int getRouteStopId() {
        return mRouteStopId;
    }

    public void setRouteStopId(int routeStopId) {
        this.mRouteStopId = routeStopId;
    }

    public int getTypeDayId() {
        return mTypeDayId;
    }

    public void setTypeDayId(int typeDayId) {
        this.mTypeDayId = typeDayId;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        this.mTime = time;
    }
}