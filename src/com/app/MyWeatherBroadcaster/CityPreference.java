package com.app.MyWeatherBroadcaster;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by PsichO on 30.09.2014.
 */
public class CityPreference {
    SharedPreferences preferences;

    public CityPreference(Activity activity) {
        preferences = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    String getCity(){
        return preferences.getString("city", "Moscow, RU");
    }

    void setCity(String city){
        preferences.edit().putString("city", city).commit();
    }
}
