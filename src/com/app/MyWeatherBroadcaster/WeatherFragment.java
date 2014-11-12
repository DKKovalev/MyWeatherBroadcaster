package com.app.MyWeatherBroadcaster;

import android.support.v4.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;

import android.os.Handler;

import java.util.Date;

/**
 * Created by PsichO on 30.09.2014.
 */
public class WeatherFragment extends Fragment {
    Typeface weatherIcons;

    TextView weatherIconText;
    TextView cityText;
    TextView temperatureText;
    TextView detailsText;

    Handler handler;

    public WeatherFragment() {
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        weatherIconText = (TextView) view.findViewById(R.id.weather_icon_text);
        weatherIconText.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        cityText = (TextView) view.findViewById(R.id.city_name_text);
        temperatureText = (TextView) view.findViewById(R.id.weather_temperature_text);
        detailsText = (TextView) view.findViewById(R.id.details_text);

        weatherIconText.setTypeface(weatherIcons);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherIcons = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather_icons.ttf");
        updateWeatherData(new CityPreference(getActivity()).getCity());
    }

    private void updateWeatherData(final String city) {
        new Thread() {
            public void run() {
                final JSONObject object = FetchWeather.getJSON(getActivity(), city);
                if (object == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "City not found", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            renderWeather(object);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject jsonObject) {
        try {
            cityText.setText(jsonObject.getString("name").toUpperCase() + ", " + jsonObject.getJSONObject("sys").getString("country"));
            JSONObject details = jsonObject.getJSONArray("weather").getJSONObject(0);
            JSONObject main = jsonObject.getJSONObject("main");
            detailsText.setText(details.getString("description").toUpperCase() +
                    "\n" + "Humidity: " + main.getString("humidity") + "%" +
                    "\n" + "Pressure: " + main.getString("pressure") + " hPa");

            temperatureText.setText(String.format("%.2f", main.getDouble("temp")) + " ℃");

            setWeatherIcon(details.getInt("id"), jsonObject.getJSONObject("sys").getLong("sunrise") * 1000,
                    jsonObject.getJSONObject("sys").getLong("sunset") * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setWeatherIcon(int actualID, long sunrise, long sunset) {
        String icon = "";
        int id = actualID / 100;

        if (actualID == 800) {
            long currTime = new Date().getTime();
            if (currTime >= sunrise && currTime < sunset) {
                icon = getActivity().getString(R.string.weather_sunny);
            } else {
                icon = getActivity().getString(R.string.weather_night);
            }
        } else {
            switch (id){
                case 2:
                    icon = getActivity().getString(R.string.weather_storm);
                    break;
                case 5:
                    icon = getActivity().getString(R.string.weather_rain);
                    break;
                case 6:
                    icon = getActivity().getString(R.string.weather_snow);
                    break;
                case 8:
                    icon = getActivity().getString(R.string.weather_cloudy);
            }
        }

        weatherIconText.setText(icon);
    }

    public void changeCity(String city){
        updateWeatherData(city);
    }
}
