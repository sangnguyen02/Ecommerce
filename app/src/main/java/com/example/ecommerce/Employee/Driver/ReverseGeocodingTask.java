package com.example.ecommerce.Employee.Driver;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ReverseGeocodingTask extends AsyncTask<Double, Void, String> {

    private final TextView textView;
    private boolean isCancelled = false; // Flag to check if the task is cancelled
    String apigeofy = "3e150f47e1ba4055b8c06a09061ea420";

    public ReverseGeocodingTask(TextView textView) {
        this.textView = textView;
    }

    @Override
    protected String doInBackground(Double... params) {
        if (isCancelled) {
            Log.e("ReverseGeo","CancelConvert");
            // Task is cancelled, return null or handle accordingly
            return null;
        }

        double latitude = params[0];
        double longitude = params[1];

        try {
            String apiUrl = "https://api.geoapify.com/v1/geocode/reverse?lat=" + latitude + "&lon=" + longitude + "&apiKey=" + URLEncoder.encode(apigeofy, "UTF-8");
            URL url = new URL(apiUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // Parse and handle the JSON response
                return response.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Handle the error according to your needs
        }
    }

    @Override
    protected void onPostExecute(String result) {
        // Update UI or handle the result as needed
        if (!isCancelled && result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray features = jsonObject.getJSONArray("features");

                if (features.length() > 0) {
                    JSONObject firstFeature = features.getJSONObject(0);
                    JSONObject properties = firstFeature.getJSONObject("properties");

                    // Extract the formatted address
                    String formattedAddress = properties.optString("formatted", "Address not available");
                    textView.setText(formattedAddress);
                    Log.e("Add:", formattedAddress);
                } else {
                    textView.setText("Address not found");
                    Log.e("Add:", "Address not found");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                textView.setText("Error parsing JSON");
                Log.e("Add:", "Error parsing JSON");
            }
        }
    }

    // Method to cancel the task
    public void cancelTask() {
        isCancelled = true;
        cancel(true);
    }
}
