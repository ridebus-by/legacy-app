package org.xtimms.ridebus.model;

import java.io.Serializable;

public abstract class DatabaseObject implements Serializable {
    public abstract String getTitle();

    public abstract String getNumber();

    public abstract boolean isEmpty();
}
