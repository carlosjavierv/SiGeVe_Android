package com.example.sigeve_android;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UbicacionActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Bundle dato;
    int idUsuario;
    int idVehiculo;
    String host;
    double tvLatitude, tvLongitude;
    TextView txtDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);

        dato = getIntent().getExtras();

        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtDescripcion.setText(dato.getString("vehiculo"));

        idUsuario = ((Global) this.getApplication()).getIdUsuario();
        idVehiculo = dato.getInt("idVehiculo");
        host = ((Global) this.getApplication()).getHost();
        getData();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng position = new LatLng(tvLatitude, tvLongitude);
        mMap.addMarker(new MarkerOptions().position(position).title("Posición"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position,14.0f));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
    }

    public void volver(View v) {
        Intent intentEnviar = new Intent(UbicacionActivity.this, PrincipalUsuarioActivity.class);
        startActivity(intentEnviar);
    }

    public void getData() {

        String ws = host.concat("/ubicacion/");
        ws = ws.concat(String.valueOf(idVehiculo));

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

            JSONArray jsonArr = new JSONArray(json);
            String latitud = null;
            String longitud = null;

            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject objeto = jsonArr.getJSONObject(i);
                latitud = objeto.optString("latitud");
                longitud = objeto.optString("longitud");
            }

            if(latitud != null && longitud != null){
                tvLatitude = Double.parseDouble(latitud);
                tvLongitude = Double.parseDouble(longitud);
            }

        } catch (MalformedURLException e) {
            Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void actualizar(View v){
        getData();
    }
}
