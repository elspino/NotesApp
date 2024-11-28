package com.polines.notesapp.service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherService {
    public static void getWeather(Context context, final WeatherCallback callback) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=YOUR_CITY&appid=YOUR_API_KEY";

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String weather = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                callback.onWeatherReceived("Current Weather: " + weather);
            } catch (JSONException e) {
                e.printStackTrace();
                callback.onWeatherReceived("Error fetching weather");
            }
        }, error -> callback.onWeatherReceived("Error fetching weather"));

        Volley.newRequestQueue(context).add(request);
    }

    public interface WeatherCallback {
        void onWeatherReceived(String weatherData);
    }
}
