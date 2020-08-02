package com.example.sigeve_android.data;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.StrictMode;

import com.example.sigeve_android.Global;
import com.example.sigeve_android.data.model.LoggedInUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    Global global = new Global();

    public Result<LoggedInUser> login(String username, String password) {
        String host = global.getHost();

        String ws = host.concat("/login");

        StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(politica);

        URL url = null;
        HttpURLConnection conn;

        try {

            url = new URL(ws);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("email", username)
                    .appendQueryParameter("password", password);

            String query = builder.build().getEncodedQuery();
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            conn.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();
            String json;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            json = response.toString();

            JSONObject jsnobject = new JSONObject(json);

            String error = jsnobject.getString("error");

            if(!error.isEmpty()){
                return new Result.Error(new Exception(error,new Throwable()));
            }
            String idUsuario = jsnobject.getString("idUsuario");
            String displayName = jsnobject.getString("displayName");
            String rol = jsnobject.getString("rol");

            System.out.println("///////////////////////////////");
            System.out.println(idUsuario);
            System.out.println(displayName);
            System.out.println(rol);

            return new Result.Success<>(new LoggedInUser(Integer.parseInt(idUsuario), displayName,Integer.parseInt(rol)));
        } catch (MalformedURLException e) {
            return new Result.Error(new IOException(e.getMessage(), e));
        }catch (IOException e){
            return new Result.Error(new IOException(e.getMessage(), e));
        }catch(JSONException e){
            return new Result.Error(new IOException(e.getMessage(), e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
