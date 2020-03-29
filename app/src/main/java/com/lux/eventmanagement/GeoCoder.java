package com.lux.eventmanagement;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.firestore.GeoPoint;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.URLEncoder;

public class GeoCoder {
    private static final String TAG_GEOCODER = "Geocoder";

    private static GeoPoint geoPoint;
    private GeoCoder() {
    }


    public static class GeoCoderTask extends AsyncTask<String, String, GeoPoint> {

        public GeoCoderTask(AsyncResponse delegate) {
            this.delegate = delegate;
        }

        public interface AsyncResponse {
            void processFinish(GeoPoint geoPoint);
        }

        // todo: override oncancel method to handle error
        AsyncResponse delegate;
        @Override
        protected GeoPoint doInBackground(String... strings) {
             String streetNumber = strings[0];
             String streetName = strings[1];
             String city = strings[2];
             return getGpsCoordinates(streetNumber,streetName,city);
        }

        @Override
        protected void onPostExecute(GeoPoint geoPoint) {
            this.delegate.processFinish(geoPoint);
        }

    }

//    public static void main(String[] args) throws IOException {
//        GeoPoint geoPoint = GeoCoder.getGeoCoderInstance().getGpsCoordinates("6", "Place d'Armes", "Luxembourg");
//        System.out.println(geoPoint.toString());
//    }

    private static String callApi(String streetNumber, String streetName, String city) throws IOException {
        String apiKey = "705714eff31ef7";
        String encodedQuery = URLEncoder.encode(streetNumber+", "+streetName+", "+city+", Luxembourg");
        String url = "https://us1.locationiq.com/v1/search.php?key="+apiKey+"&q="+encodedQuery+"&format=json";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    // https://us1.locationiq.com/v1/search.php?key=705714eff31ef7&q=2,rue%20Mambra,%20Mamer,Luxembourg&format=json

    private static JsonObject getJsonObject(String apiResponse) {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(apiResponse);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        return jsonArray.get(0).getAsJsonObject();
    }

    private static GeoPoint getGeoPoint (JsonObject jsonObject) {
        return new GeoPoint(jsonObject.get("lat").getAsDouble(),jsonObject.get("lon").getAsDouble());
    }

    private static GeoPoint getGpsCoordinates(String streetNumber, String streetName, String city) {
        String response = "";
        try {
             response = callApi(streetNumber, streetName, city);
             Log.d(TAG_GEOCODER, response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonObject jsonObject = getJsonObject(response);
        return getGeoPoint(jsonObject);
    }


    }
