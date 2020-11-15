package org.xtimms.trackbus.task;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class RetrieveDatabaseVersionTask extends AsyncTask<Integer, Void, Integer> {

    @Override
    protected Integer doInBackground(Integer... integers) {
        String str = null;
        try {
            // Create a URL for the desired page
            URL url = new URL("https://rumblur.hrebeni.uk/ridebus/version.txt");

            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder total = new StringBuilder();
            while ((str = in.readLine()) != null) {
                total.append(str);
            }
            in.close();

        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return Integer.parseInt(str);
    }
}