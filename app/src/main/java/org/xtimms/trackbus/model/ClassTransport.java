package org.xtimms.trackbus.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "classTransport")
public class ClassTransport {

    @ColumnInfo(name = "_id")
    @PrimaryKey(autoGenerate = true)
    private int mId;

    @ColumnInfo(name = "class")
    private String mClassTransport;

    public ClassTransport(int id, String classTransport) {
        this.mId = id;
        this.mClassTransport = classTransport;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getClassTransport() {
        return mClassTransport;
    }

    public void setClassTransport(String classTransport) {
        this.mClassTransport = classTransport;
    }
}
