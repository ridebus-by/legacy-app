package org.xtimms.trackbus.backend;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;

interface DatabaseBackendApi {

    @GET("/databases/{schema}")
    DatabaseInformation getDatabaseInformation(
            @Path("schema") int schemaVersion);

    @GET("/databases/{schema}/contents")
    Response getDatabaseContents(
            @Path("schema") int schemaVersion);

}
