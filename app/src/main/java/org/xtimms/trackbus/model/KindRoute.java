package org.xtimms.trackbus.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "kindRoute")
public class KindRoute {
    @ColumnInfo(name = "_id")
    @PrimaryKey(autoGenerate = true)
    private int mId;

    @ColumnInfo(name = "kindRoute")
    private String mKindRoute;

    public KindRoute(int id, String kindRoute) {
        this.mId = id;
        this.mKindRoute = kindRoute;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getKindRoute() {
        return mKindRoute;
    }

    public void setKindRoute(String kindRoute) {
        this.mKindRoute = kindRoute;
    }
}