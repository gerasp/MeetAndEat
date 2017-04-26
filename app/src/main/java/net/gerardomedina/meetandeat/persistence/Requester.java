package net.gerardomedina.meetandeat.persistence;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class Requester {

    private final String baseUrl = "http://192.168.1.39/ws/";

    public Requester() {

    }

    public JSONObject httpRequest(String urlString, String method, Map<String,String> parameters) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(baseUrl+urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            if (method.equals("POST")) {
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(parameters));
                writer.flush();
                writer.close();
                os.close();
            }

            connection.connect();
            int status = connection.getResponseCode();
            switch (status) {
                case 200:
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) sb.append(line).append("\n");
                    br.close();
                    return new JSONObject(sb.toString());
                default:
                    return new JSONObject("{'code':-1}");
            }
        } catch (MalformedURLException e) {
            Log.e("HTTP Connection", "Malformed URL: " + e.toString());
        } catch (IOException e) {
            Log.e("HTTP Connection", "IO Exception: " + e.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data: " + e.toString());
        } finally {
            if (connection != null) {
                try { connection.disconnect(); }
                catch (Exception e) {
                    Log.e("HTTP Connection", "Error closing connection: " + e.toString());
                }
            }
        }
        return new JSONObject();
    }

    private String getPostDataString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first) first = false;
            else result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
