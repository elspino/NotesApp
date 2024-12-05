package com.polines.notesapp.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherService {
    private static final String TAG = "WeatherService";

    public static void getWeather(Context context, final WeatherCallback callback) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=Perm&appid=d2a7bc8be3b4dcb199f91e8677a90159&lang=ru&units=metric";

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            if (response != null && !response.isEmpty()) {
                Log.d(TAG, "Response: " + response);
            }

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("weather" ) && jsonObject.getJSONArray("weather").length() > 0) {
                    String weather = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                    callback.onWeatherReceived("Погода: " + weather);
                } else {
                    callback.onWeatherReceived("Weather information is missing");
                }
            } catch (JSONException e) {
                Log.e(TAG, "JSON parsing error: ", e);
                callback.onWeatherReceived("Error fetching weather");
            }
        }, error -> {
            Log.e(TAG, "Network error: ", error);
            callback.onWeatherReceived("Error fetching weather");
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response.statusCode == 200) {
                    return super.parseNetworkResponse(response);
                } else {
                    return Response.error(new VolleyError("Error fetching weather"));
                }
            }
        };

        Volley.newRequestQueue(context).add(request);
    }



    public interface WeatherCallback {
        void onWeatherReceived(String weatherData);
    }
}
