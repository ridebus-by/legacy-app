package org.xtimms.trackbus.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xtimms.trackbus.util.DataUtils;
import org.xtimms.trackbus.R;
import org.xtimms.trackbus.adapter.MyAppsAdapter;

public class MyAppsActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_apps);

        Toolbar toolbar = findViewById(R.id.toolbar_my_apps);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recycler_my_apps = findViewById(R.id.recycler_my_apps);
        MyAppsAdapter adapter = new MyAppsAdapter(this, DataUtils.getMyAppsData());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_my_apps.setLayoutManager(linearLayoutManager);
        recycler_my_apps.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Menu
        //When home is clicked
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}