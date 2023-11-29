package com.geocodinglocationservices.utill;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
public class GeocodingDistance {

        private static final String ACCESS_TOKEN = "pk.eyJ1IjoiZGlsLTk4IiwiYSI6ImNscGZzNnhnczFxdWIyanJsMmlteHBxMTUifQ.-rF5zeoc-8qmqrKMbjVaYA";
        private static final OkHttpClient httpClient = new OkHttpClient();

        public static double getDistance(double startLat, double startLng, double endLat, double endLng) throws Exception {
            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.mapbox.com/directions/v5/mapbox/driving")
                    .newBuilder();
            urlBuilder.addQueryParameter("access_token", ACCESS_TOKEN);
            urlBuilder.addQueryParameter("geometries", "geojson");
            String coordinates = startLng + "," + startLat + ";" + endLng + "," + endLat;
            urlBuilder.addPathSegment(coordinates);

            Request request = new Request.Builder()
                    .url(urlBuilder.build().toString())
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new Exception("Unexpected code " + response);

                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);
                double distance = jsonObject.getJSONArray("routes").getJSONObject(0).getDouble("distance");

                // Convert distance from meters to kilometers
                return distance / 1000;
            }
        }
    }


