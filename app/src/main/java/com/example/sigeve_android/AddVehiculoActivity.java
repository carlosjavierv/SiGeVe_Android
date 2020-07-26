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

public class AddVehiculoActivity extends AppCompatActivity {
    EditText txtMarca,txtModelo,txtAnio,txtPlaca;
    Global global = new Global();
    Spinner spUnidad,spConductor;
    ArrayList<Unidad> unidades = new ArrayList<>();
    ArrayList<Conductor> conductores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehiculo);

        txtMarca = findViewById(R.id.txtNombre);
        txtModelo = findViewById(R.id.txtModelo);
        txtAnio = findViewById(R.id.txtAnio);
        txtPlaca = findViewById(R.id.txtPlaca);

        spUnidad = findViewById(R.id.spUnidad);
        spConductor = findViewById(R.id.spConductor);

        getListaUnidad();
        getListaConductor();
    }

    public void Guardar(View v) {

        String marca = txtMarca.getText().toString();
        String modelo = txtModelo.getText().toString();
        String anio = txtAnio.getText().toString();
        String placa = txtPlaca.getText().toString();
        Unidad unidad = (Unidad)spUnidad.getSelectedItem();
        Conductor conductor = (Conductor) spConductor.getSelectedItem();

        if (!marca.isEmpty() && !placa.isEmpty()) {
            String host = global.getHost();

            //UIL Del web service
            String ws = host.concat("/vehiculo");

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
                        .appendQueryParameter("marca", marca)
                        .appendQueryParameter("modelo", modelo)
                        .appendQueryParameter("anio", anio)
                        .appendQueryParameter("placa", placa)
                        .appendQueryParameter("unidad_id", String.valueOf(unidad.getId()))
                        .appendQueryParameter("usuario_id", String.valueOf(conductor.getId()));

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

                Intent ide = new Intent(AddVehiculoActivity.this, PrincipalAdminActivity.class);
                startActivity(ide);

            } catch (MalformedURLException e) {
                Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Datos requeridos: marca, placa", Toast.LENGTH_LONG).show();
        }
    }

    public void getListaUnidad() {

        //UIL Del web service
        String host = global.getHost();
        String ws = host.concat("/unidad");

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

                Unidad unidad = new Unidad(Integer.parseInt(objeto.optString("id")), objeto.optString("nombre"));
                unidades.add(unidad);
            }
            ArrayAdapter<Unidad> adapterUnidad = new ArrayAdapter<Unidad>(this, android.R.layout.simple_spinner_item, unidades);
            spUnidad.setAdapter(adapterUnidad);

        } catch (MalformedURLException e) {
            Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }
    public void getListaConductor() {

        //UIL Del web service
        String host = global.getHost();
        String ws = host.concat("/conductor");

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

                Conductor conductor = new Conductor(Integer.parseInt(objeto.optString("id")), objeto.optString("nombre"));
                conductores.add(conductor);
            }
            ArrayAdapter<Conductor> adapterConductor = new ArrayAdapter<Conductor>(this, android.R.layout.simple_spinner_item, conductores);
            spConductor.setAdapter(adapterConductor);

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
