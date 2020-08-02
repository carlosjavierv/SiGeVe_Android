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

public class EditUsuarioActivity extends AppCompatActivity {

    String host;
    EditText txtNombre,txtEmail,txtContrasenia;
    String id;
    Bundle dato;
    Spinner spUnidad;
    ArrayList<Unidad> unidades = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_usuario);

        host = ((Global) this.getApplication()).getHost();
        txtNombre = findViewById(R.id.txtNombre);
        txtEmail = findViewById(R.id.txtEmail);
        txtContrasenia = findViewById(R.id.txtContrasenia);
        spUnidad = findViewById(R.id.spUnidad);

        dato = getIntent().getExtras();
        id = dato.getString("id");
        getData();
    }

    public void getData() {

        String ws = host.concat("/conductor/");
        ws = ws.concat(id);

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

            JSONObject jsnobject = new JSONObject(json);

            String nom = jsnobject.getString("nombre");
            String email = jsnobject.getString("email");

            txtNombre.setText(nom);
            txtEmail.setText(email);

            if (jsnobject.getString("unidad_id") != null) {
                getListaUnidad(Integer.parseInt(jsnobject.getString("unidad_id")));
            }

        } catch (MalformedURLException e) {
            Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void getListaUnidad(int idUnidad) {

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

            Unidad selUnidad = null;

            for(Unidad und : unidades) {
                if(und.getId() == idUnidad) {
                    selUnidad = und;
                }
            }

            if(selUnidad != null){
                int spinnerPosition = adapterUnidad.getPosition(selUnidad);
                spUnidad.setSelection(spinnerPosition);
            }

        } catch (MalformedURLException e) {
            Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    public void Guardar(View v) {

        String nombre = txtNombre.getText().toString();
        String email = txtEmail.getText().toString();
        String contrasenia = txtContrasenia.getText().toString();
        Unidad unidad = (Unidad)spUnidad.getSelectedItem();

//        Rol rol = (Rol)spRol.getSelectedItem();

        if (!nombre.isEmpty() && !email.isEmpty() && !contrasenia.isEmpty()) {

            //UIL Del web service
            String ws = host.concat("/usuario/"+id);

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
                conn.setRequestMethod("PUT");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("nombre", nombre)
                        .appendQueryParameter("email", email)
                        .appendQueryParameter("unidad_id", String.valueOf(unidad.getId()))
                        .appendQueryParameter("password", contrasenia);

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

                Intent ide = new Intent(EditUsuarioActivity.this, PrincipalAdminActivity.class);
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

    public void Cancelar(View v) {
        finish();
    }

    public void Eliminar(View v){

        //UIL Del web service
        String ws = host.concat("/usuario/"+id);

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
            conn.setRequestMethod("DELETE");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

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

            Intent ide = new Intent(EditUsuarioActivity.this, PrincipalAdminActivity.class);
            startActivity(ide);

        } catch (MalformedURLException e) {
            Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
