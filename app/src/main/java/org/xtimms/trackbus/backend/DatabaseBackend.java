package org.xtimms.trackbus.backend;

import android.content.Context;

import androidx.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;
import org.xtimms.trackbus.R;
import org.xtimms.trackbus.database.DatabaseSchema;

import java.io.IOException;
import java.io.InputStream;

import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class DatabaseBackend {

    private final DatabaseBackendApi backendApi;

    public static DatabaseBackend with(@NonNull Context context) {
        return new DatabaseBackend(context);
    }

    private DatabaseBackend(Context context) {
        this.backendApi = buildBackendApi(context);
    }

    private DatabaseBackendApi buildBackendApi(Context context) {
        RestAdapter backendAdapter = new RestAdapter.Builder()
                .setEndpoint(context.getString(R.string.url_backend))
                .build();

        return backendAdapter.create(DatabaseBackendApi.class);
    }

    public String getDatabaseVersion() {
        try {
            return backendApi.getDatabaseInformation(DatabaseSchema.Versions.CURRENT).getVersion();
        } catch (RetrofitError error) {
            return StringUtils.EMPTY;
        }
    }

    public InputStream getDatabaseContents() {
        try {
            return backendApi.getDatabaseContents(DatabaseSchema.Versions.CURRENT).getBody().in();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
