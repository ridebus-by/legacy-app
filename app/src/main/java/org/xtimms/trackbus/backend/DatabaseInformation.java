package org.xtimms.trackbus.backend;

import com.google.gson.annotations.SerializedName;

public class DatabaseInformation {

    @SerializedName("version")
    private String version;

    public String getVersion() {
        return version;
    }

}
