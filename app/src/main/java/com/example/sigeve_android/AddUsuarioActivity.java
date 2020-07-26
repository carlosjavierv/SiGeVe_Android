package com.example.sigeve_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sigeve_android.data.model.Conductor;
import com.example.sigeve_android.data.model.Rol;
import com.example.sigeve_android.data.model.Unidad;

import org.json.JSONArray;
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
import java.util.ArrayList;

public class AddUsuarioActivity extends AppCompatActivity {
    Global global = new Global();
    EditText txtNombre,txtEmail,txtContrasenia;
//    ArrayList<Rol> roles = new ArrayList<>();
//    Spinner spRol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_usuario);

        txtNombre = findViewById(R.id.txtNombre);
        txtEmail = findViewById(R.id.txtEmail);
        txtContrasenia = findViewById(R.id.txtContrasenia);

//        spRol = findViewById(R.id.spRol);
//        getListaRol();
    }

    public void Guardar(View v) {

        String nombre = txtNombre.getText().toString();
        String email = txtEmail.getText().toString();
        String contrasenia = txtContrasenia.getText().toString();
//        Rol rol = (Rol)spRol.getSelectedItem();

        if (!nombre.isEmpty() && !email.isEmpty() && !contrasenia.isEmpty()) {
            String host = global.getHost();

            //UIL Del web service
            String ws = host.concat("/usuario");

            //Permisos de la aplicacion
            StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);

            URL url = null;
            HttpURLConnection conn;
            //Capturar excepciones
            try {
                url = new URL(ws);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("nombre", nombre)
                        .appendQueryParameter("email", email)
                        .appendQueryParameter("password", contrasenia);
//                        .appendQueryParameter("rol_id", String.valueOf(rol.getId()))

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

                JSONObject jsonObject = new JSONObject(json);
                JSONObject jsonResult = (JSONObject) jsonObject.get("result");

                Toast.makeText(getApplicationContext(), jsonResult.getString("message"), Toast.LENGTH_LONG).show();

                Intent ide = new Intent(AddUsuarioActivity.this, PrincipalAdminActivity.class);
                startActivity(ide);

            } catch (MalformedURLException e) {
                Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Datos requeridos: nombre, email, contraseña", Toast.LENGTH_LONG).show();
        }
    }

    public void getListaRol() {

        //UIL Del web service
        String host = global.getHost();
        String ws = host.concat("/rol");

        //Permisos de la aplicacion
        StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(politica);

        URL url = null;
        HttpURLConnection conn;

        try {
            url = new URL(ws);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();
            String json;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            json = response.toString();

            JSONArray jsonArr = null;

            jsonArr = new JSONArray(json);

            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject objeto = jsonArr.getJSONObject(i);

                Rol rol = new Rol(Integer.parseInt(objeto.optString("id")), objeto.optString("nombre"));
//                roles.add(rol);
            }
//            ArrayAdapter<Rol> adapterRol = new ArrayAdapter<Rol>(this, android.R.layout.simple_spinner_item, roles);
//            spRol.setAdapter(adapterRol);

        } catch (MalformedURLException e) {
            Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void Cancelar(View v) {
        finish();
    }

}
