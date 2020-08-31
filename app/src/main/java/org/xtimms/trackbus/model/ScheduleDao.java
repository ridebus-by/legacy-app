package org.xtimms.trackbus.model;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScheduleDao {
    @Query("SELECT  * FROM stop ORDER BY stopTitle ASC")
    List<Stop> getAllStops();

    @Query("SELECT * FROM route WHERE (city_id=1 AND transport_id=1)")
    List<Route> getAllRoutesBus();

    @Query("SELECT * FROM route WHERE (city_id=1)")
    List<Route> getAllRoutes();

    @Query("SELECT * FROM route WHERE (city_id=1 AND transport_id=3)")
    List<Route> getAllRoutesExpress();

    @Query("SELECT * FROM route WHERE (city_id=1 AND transport_id=2)")
    List<Route> getAllRoutesTram();

    @Query("SELECT * FROM route WHERE (city_id=1 AND transport_id=4)")
    List<Route> getAllRoutesMinibus();

    @Query("SELECT route.* FROM routeStops" +
            " INNER JOIN route on route._id = routeStops.route_id WHERE stop_id = :stopId")
    List<Route> getRoutesByStop(int stopId);

    @Query("SELECT stop.* FROM routeStops INNER JOIN stop ON stop._id = routeStops.stop_id " +
            "WHERE routeStops.route_id = :routeId ORDER BY stopNumber ASC")
    List<Stop> getStops(int routeId);

    @Query("SELECT schedule.time FROM routeStops INNER JOIN schedule " +
            "ON (schedule.routeStop_id = routeStops._id and schedule.typeDay_id = :typeDay) " +
            "WHERE (routeStops.route_id = :routeId and routeStops.stop_id = :stop_id)")
    List<String> getTimeOnStop(int typeDay, int routeId, int stop_id);

    @Query("SELECT schedule.typeDay_id FROM schedule WHERE schedule.routeStop_id = " +
            "(SELECT routeStops._id FROM routeStops WHERE route_id = :routeId AND stop_id = :stop_id)" +
            " GROUP BY schedule.typeDay_id")
    List<Integer> getTypeDay(int routeId, int stop_id);

}
