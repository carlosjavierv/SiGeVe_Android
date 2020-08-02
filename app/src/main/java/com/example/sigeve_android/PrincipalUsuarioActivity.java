package com.example.sigeve_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sigeve_android.data.model.Vehiculo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PrincipalUsuarioActivity extends AppCompatActivity {
    Bundle dato;
//    Global global;
    TextView txtNombre,txtUnidad;
    String host;
    ListView list;

    int idUsuario;
    ArrayList<Vehiculo> vehiculos = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_usuario);

        dato = getIntent().getExtras();

        host = ((Global) this.getApplication()).getHost();
        idUsuario = ((Global) this.getApplication()).getIdUsuario();

        txtNombre = findViewById(R.id.txtNombre);
        txtUnidad = findViewById(R.id.txtUnidad);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, vehiculos);
        list = findViewById(R.id.list);
        list.setAdapter(arrayAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Vehiculo item = (Vehiculo)parent.getItemAtPosition(position);

                Intent intent = new Intent(PrincipalUsuarioActivity.this, UbicacionActivity.class);
                //based on item add info to intent
                intent.putExtra("vehiculo",item.toString());
                intent.putExtra("idVehiculo",item.getId());

                startActivity(intent);
            }
        });

        getData(idUsuario);
        getListaVehiculos();
    }

    public void getData(int id) {

        String ws = host.concat("/usuario/");
        ws = ws.concat(String.valueOf(id));

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
            String unidad = jsnobject.getString("unidad");

            JSONObject unidadObject = new JSONObject(unidad);

            String nombreUnidad = unidadObject.getString("nombre");

            txtNombre.setText(nom);
            txtUnidad.setText(nombreUnidad);

        } catch (MalformedURLException e) {
            Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void getListaVehiculos() {
        String ws = host.concat("/vehiculos?usuario_id="+Integer.toString(idUsuario));

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

                Vehiculo vehiculo = new Vehiculo(
                        Integer.parseInt(objeto.optString("id")),
                        0,
                        0,
                        objeto.optString("marca"),
                        null,
                        null,
                        objeto.optString("placa")
                );

                System.out.println("<---------------------------->");
                System.out.println(vehiculo.getMarca());

                vehiculos.add(vehiculo);

            }

            arrayAdapter.notifyDataSetChanged();


        } catch (MalformedURLException e) {
            Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

}
