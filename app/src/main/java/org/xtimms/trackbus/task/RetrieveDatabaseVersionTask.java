package org.xtimms.trackbus.task;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RetrieveDatabaseVersionTask extends AsyncTask<String, Integer, String> {

        String line = null;

        protected String doInBackground(String...params) {
            URL url;
            try {
                url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                InputStream is = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                line = br.readLine();
                br.close();
                Log.d("VERSION", line);
                return line;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "1";
        }
}