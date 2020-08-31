package org.xtimms.trackbus.util;

public enum TypeOfDay {
    WEEKDAYS("По будним дням", 1), WEEKEND("По выходным дням", 2), ON_SATURDAYS("По субботам", 3),
    ON_SUNDAYS("По воскресеньям", 4), ON_FRIDAY("По пятницам", 5);

    private final int mIdInDatabase;

    TypeOfDay(String typeDay, int id) {
        this.mIdInDatabase = id;
    }

    public int getIdInDatabase() {
        return mIdInDatabase;
    }

}
