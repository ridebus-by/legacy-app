package org.xtimms.trackbus.util;

public enum TransportId {

    BUS("Автобус", 1),
    TRAM("Трамвай", 2),
    EXPRESS("Пригород", 3),
    MINIBUS("Маршрутка", 4);

    private final int mIdInDatabase;

    TransportId(String transport, int id) {
        this.mIdInDatabase = id;
    }

    public int getIdInDatabase() {
        return mIdInDatabase;
    }

}
