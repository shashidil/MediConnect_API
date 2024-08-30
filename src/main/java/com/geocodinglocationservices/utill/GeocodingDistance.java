package com.geocodinglocationservices.utill;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
public class GeocodingDistance {
        private static final String ACCESS_TOKEN = "pk.eyJ1IjoiZGlsLTk4IiwiYSI6ImNscGZzNnhnczFxdWIyanJsMmlteHBxMTUifQ.-rF5zeoc-8qmqrKMbjVaYA";
        private static final OkHttpClient httpClient = new OkHttpClient();
        private static final double MAX_DISTANCE_KM = 2000;
    public static double getDistance(double startLat, double startLng, double endLat, double endLng) throws Exception {
        double distance = calculateDistanceInKm(startLat, startLng, endLat, endLng);
        if (distance > MAX_DISTANCE_KM) {
            throw new Exception("Route exceeds maximum distance limitation");
        }
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
                 distance = jsonObject.getJSONArray("routes").getJSONObject(0).getDouble("distance");
                return distance / 1000;
            }
        }

    private static double calculateDistanceInKm(double lat1, double lng1, double lat2, double lng2) {
        final int R = 6371; // Radius of the Earth in kilometers
        double latDistance = Math.toRadians(lat2 - lat1);
        double lngDistance = Math.toRadians(lng2 - lng1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
    }


