package org.xtimms.trackbus.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import org.xtimms.trackbus.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LogsActivity extends AppBaseActivity {

    private final StringBuilder log = new StringBuilder();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_log);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        enableHomeAsUp();

        TextView logTextView = findViewById(R.id.logTextView);
        logTextView.setMovementMethod(new ScrollingMovementMethod());

        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder log = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line.substring(line.indexOf(": ") + 2)).append("\n");
            }

            this.log.append(log.toString().replace(this.log.toString(), ""));
            logTextView.setText(this.log.toString());
        } catch (IOException e) {
            Log.e("wifiDirectHandler", "Failure reading logcat");
        }
    }
}
