package org.xtimms.trackbus.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transport")
public class Transport {
    @ColumnInfo(name = "_id")
    @PrimaryKey(autoGenerate = true)
    private int mId;

    @ColumnInfo(name = "typeTransport")
    private String mTypeTransport;

    public Transport(int id, String typeTransport) {
        this.mId = id;
        this.mTypeTransport = typeTransport;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getTypeTransport() {
        return mTypeTransport;
    }

    public void setTypeTransport(String typeTransport) {
        this.mTypeTransport = typeTransport;
    }
}