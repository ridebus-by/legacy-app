package org.xtimms.trackbus.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "typeDay")
public class TypeDay {
    @ColumnInfo(name = "_id")
    @PrimaryKey(autoGenerate = true)
    private int mId;

    @ColumnInfo(name = "typeDay")
    private String mTypeDay;

    public TypeDay(int id, String typeDay) {
        this.mId = id;
        this.mTypeDay = typeDay;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getTypeDay() {
        return mTypeDay;
    }

    public void setTypeDay(String typeDay) {
        this.mTypeDay = typeDay;
    }
}
