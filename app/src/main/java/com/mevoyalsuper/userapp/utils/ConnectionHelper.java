package com.mevoyalsuper.userapp.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by ivan on 5/29/15.
 */
public class ConnectionHelper {

    public static final String API_WEB_SERVER_ADDRESS = "http://192.168.0.103:3000/api";

    public static String getUrl(String address) throws IOException {
        URL url = new URL(address);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setDoInput(true);

        // Starts the query
        connection.connect();

        return readIt(connection);
    }

    public static String readIt(HttpURLConnection request) throws IOException {
        String response = "EMPTY";

        try {
            if (request.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
                String inputLine;
                response = "";
                while ((inputLine = in.readLine()) != null) {
                    response += inputLine + "\n";
                }
                in.close();
            }else{
                response = request.getResponseCode() + ": " + request.getResponseMessage();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("WEB", response);

        return response;
    }

    public static String getRandomString(){
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        int size = 20;

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }

        return sb.toString();
    }
}


