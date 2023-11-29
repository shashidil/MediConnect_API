package com.geocodinglocationservices.utill;

import java.io.Console;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

public class GeocodingService {
    private static final OkHttpClient httpClient = new OkHttpClient();
    private static final String ACCESS_TOKEN = "pk.eyJ1IjoiZGlsLTk4IiwiYSI6ImNscGZzNnhnczFxdWIyanJsMmlteHBxMTUifQ.-rF5zeoc-8qmqrKMbjVaYA";

    public static LatLng getCoordinates(String address) throws IOException {
        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
        String url = "https://api.mapbox.com/geocoding/v5/mapbox.places/" + encodedAddress + ".json?access_token=" + ACCESS_TOKEN;

        Request request = new Request.Builder().url(url).build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String responseBody = response.body().string();
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONArray features = jsonObject.getJSONArray("features");

            if (features.length() > 0) {
                JSONObject feature = features.getJSONObject(0);
                JSONArray coordinates = feature.getJSONObject("geometry").getJSONArray("coordinates");
                double lon = coordinates.getDouble(0);
                double lat = coordinates.getDouble(1);

                return new LatLng(lat, lon);
            } else {
                throw new RuntimeException("No geocoding result for the given address");
            }
        }
    }

    public static class LatLng {
        public final double latitude;
        public final double longitude;

        public LatLng(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}
