package com.example.trucemap;

import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import static android.content.Context.LOCATION_SERVICE;

public class FirstFragment extends Fragment {


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }
    // ONVIEWCREATED START*
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Button btn_start = view.findViewById((R.id.button_start));
        final Button btn_stop = view.findViewById((R.id.button_stop));


        // BUTTON START ONCLICK FUNCTION START*
        view.findViewById(R.id.button_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final LocationManager lm = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                final String mockLoc = lm.getBestProvider(getCriteria(), true);
                final Handler handler = new Handler();
                buttonDisable(btn_start, btn_stop);

                lm.addTestProvider(mockLoc, false, false, false, false, true, true, true, 0, Criteria.ACCURACY_FINE);

                lm.setTestProviderEnabled(mockLoc, true);

                // ARRAYLIST LOADING START*
                // Initialize arraylist and load JSON into the arraylist
                ArrayList<double[]> formList = new ArrayList<>();

                try {
                    JSONObject obj = new JSONObject(loadJSONFromAsset());
                    JSONArray m_jArry = obj.getJSONArray("coords");



                    for (int i = 0; i < m_jArry.length(); i++) {
                        JSONObject jo_inside = m_jArry.getJSONObject(i);

                        Log.d("Latitude-->", jo_inside.getString("latX"));
                        Double latX = jo_inside.getDouble("latX");
                        Log.d("Longitude-->", jo_inside.getString("longY"));
                        Double longY = jo_inside.getDouble("longY");
                        Log.d("Bearing-->", jo_inside.getString("bearing"));
                        Double bearing = jo_inside.getDouble("bearing");
                        Log.d("Speed-->", jo_inside.getString("speed"));
                        Double speed = jo_inside.getDouble("speed");
                        Log.d("Accuracy-->", jo_inside.getString("accuracy"));
                        Double accuracy = jo_inside.getDouble("accuracy");

                        //Add your values in your `ArrayList` as below:
                        double m_li[] = {latX, longY, bearing, speed, accuracy};


                        formList.add(m_li);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                final ArrayList<double[]> coordsJSON = formList;

                // ARRAYLIST LOADING END*

                // SET LOCATION ON INTERVAL START*
                // Initialize iterable counter within runnable
                final int[] count = {0, 0, 0};
                // Initialize interval for use later with postDelayed
                final long INTERVAL = TimeUnit.SECONDS.toMillis(1);
                // Initialize handler so we can use it outside of the runnable


                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        // Initialize new location
                        Location fakeLoc = new Location(mockLoc);

                        // Saves the size of the JSON object
                        count[1] = coordsJSON.size() - 1;
                        int counter = count[0];

                        // Resets the count and reverses it so we go back and forth if the app is left on
                        if(count[2] == 0){
                            if(count[0] < coordsJSON.size() -1)
                                count[0]++;
                            else {
                                count[0] = count[1];
                                count[2] = 1;
                            }
                        }
                        else if(count[2] == 1 && count[0] > 0 ) {
                            count[0]--;
                        }
                        else {
                            count[2] = 0;
                        }

                        // Set location params
                        double latX = coordsJSON.get(counter)[0];
                        double longY = coordsJSON.get(counter)[1];
                        double heading = coordsJSON.get(counter)[2];
                        double speed = coordsJSON.get(counter)[3];
                        double accuracy = coordsJSON.get(counter)[4];

                        // Set location params to fakeLoc obj
                        fakeLoc.setLatitude(latX);
                        fakeLoc.setLongitude(longY);
                        fakeLoc.setTime(System.currentTimeMillis());
                        fakeLoc.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                        fakeLoc.setSpeed((float) speed);
                        fakeLoc.setAccuracy((float) accuracy);
                        fakeLoc.setBearing((float) heading);

                        lm.setTestProviderLocation(mockLoc, fakeLoc);

                        // Prints out the current lat, long and sets text view to show
                        String currLat = Double.toString(fakeLoc.getLatitude());
                        String currLong = Double.toString(fakeLoc.getLongitude());
                        String currCount = Integer.toString(count[0]);
                        TextView text = getView().findViewById(R.id.textview_first);
                        text.setText(currCount + ": " + currLat + ", " + currLong);

                        // reruns the runnable
                        handler.postDelayed(this, INTERVAL);
                    }
                };

                // calls runnable from handler
                handler.postDelayed(runnable, INTERVAL);

                btn_stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        handler.removeCallbacks(runnable);
                        buttonEnable(btn_start, btn_stop);

                    }

                });
                // SET LOCATION ON INTERVAL END*
            }
        });
        // BUTTON START ONCLICK FUNCTION END*
    }
    // ONVIEWCREATED END*

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("coords.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void buttonDisable(Button btn_start, Button btn_stop) {
        btn_start.setEnabled(false);
        btn_start.setTextColor(Color.parseColor("#808080"));

        btn_stop.setEnabled(true);
        btn_stop.setTextColor(Color.parseColor("#000000"));
    }

    public void buttonEnable(Button btn_start, Button btn_stop) {
        btn_start.setEnabled(true);
        btn_start.setTextColor(Color.parseColor("#000000"));

        btn_stop.setEnabled(false);
        btn_stop.setTextColor(Color.parseColor("#808080"));
    }

    public Criteria getCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        return criteria;
    }
};
