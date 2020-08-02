package com.example.sigeve_android.ui.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.sigeve_android.EditConcductorActivity;
import com.example.sigeve_android.EditUnidadActivity;
import com.example.sigeve_android.EditUsuarioActivity;
import com.example.sigeve_android.EditVehiculoActivity;
import com.example.sigeve_android.Global;
import com.example.sigeve_android.PrincipalAdminActivity;
import com.example.sigeve_android.R;
import com.example.sigeve_android.data.model.Conductor;
import com.example.sigeve_android.data.model.Unidad;
import com.example.sigeve_android.data.model.Usuario;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    Global global = new Global();
    ListView list;
    ArrayList<Unidad> unidades = new ArrayList<>();
    ArrayList<Conductor> conductores = new ArrayList<>();
    ArrayList<Vehiculo> vehiculos = new ArrayList<>();
    ArrayList<Usuario> usuarios = new ArrayList<>();

    ArrayAdapter arrayAdapter;
    View root;

    public static PlaceholderFragment newInstance(String index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);

        String index = "";

        if (getArguments() != null) {
            index = getArguments().getString(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_main, container, false);
        final TextView textView = root.findViewById(R.id.section_label);

        pageViewModel.getText().observe(this, new Observer<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
//                ((PrincipalAdminActivity) Objects.requireNonNull(getActivity())).getListaUnidad();
                switch (s) {
                    case "Unidad":
                        arrayAdapter = new ArrayAdapter(root.getContext(), android.R.layout.simple_list_item_1, unidades);
                        list = root.findViewById(R.id.list);
                        list.setAdapter(arrayAdapter);

                        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Unidad unidad = (Unidad)parent.getItemAtPosition(position);

                                Intent intent = new Intent(root.getContext(), EditUnidadActivity.class);
                                intent.putExtra("id",String.valueOf(unidad.getId()));

                                startActivity(intent);
                            }
                        });

                        if (unidades.size() == 0) {
                            getListaUnidad();
                        }
                        break;
                    case "Chofer":
                        arrayAdapter = new ArrayAdapter(root.getContext(), android.R.layout.simple_list_item_1, conductores);
                        list = root.findViewById(R.id.list);
                        list.setAdapter(arrayAdapter);

                        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Conductor conductor = (Conductor)parent.getItemAtPosition(position);

                                Intent intent = new Intent(root.getContext(), EditConcductorActivity.class);
                                intent.putExtra("id",String.valueOf(conductor.getId()));

                                startActivity(intent);
                            }
                        });


                        if (conductores.size() == 0) {
                            getListaConductor();
                        }
                        break;
                    case "Autos":
                        arrayAdapter = new ArrayAdapter(root.getContext(), android.R.layout.simple_list_item_1, vehiculos);
                        list = root.findViewById(R.id.list);
                        list.setAdapter(arrayAdapter);

                        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Vehiculo vehiculo = (Vehiculo)parent.getItemAtPosition(position);

                                Intent intent = new Intent(root.getContext(), EditVehiculoActivity.class);
                                intent.putExtra("id",String.valueOf(vehiculo.getId()));

                                startActivity(intent);
                            }
                        });
                        if (vehiculos.size() == 0) {
                            getListaVehiculo();
                        }
                        break;
                    case "Usuario":
                        arrayAdapter = new ArrayAdapter(root.getContext(), android.R.layout.simple_list_item_1, usuarios);
                        list = root.findViewById(R.id.list);
                        list.setAdapter(arrayAdapter);

                        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Usuario usuario = (Usuario)parent.getItemAtPosition(position);

                                Intent intent = new Intent(root.getContext(), EditUsuarioActivity.class);
                                intent.putExtra("id",String.valueOf(usuario.getId()));

                                startActivity(intent);
                            }
                        });

                        if (usuarios.size() == 0) {
                            getListaUsuario();
                        }
                        break;
                }
            }
        });
        return root;

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
            arrayAdapter.notifyDataSetChanged();

        } catch (MalformedURLException e) {
            Toast.makeText(root.getContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(root.getContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Toast.makeText(root.getContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }
    public void getListaVehiculo() {

        //UIL Del web service
        String host = global.getHost();
        String ws = host.concat("/vehiculo");

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

            JSONArray jsonArr = new JSONArray(json);

            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject objeto = jsonArr.getJSONObject(i);

                Vehiculo vehiculo = new Vehiculo(
                        Integer.parseInt(objeto.optString("id")),
                        Integer.parseInt(objeto.optString("unidad_id")),
                        Integer.parseInt(objeto.optString("usuario_id")),
                        objeto.optString("marca"),
                        objeto.optString("modelo"),
                        objeto.optString("anio"),
                        objeto.optString("placa")
                );
                vehiculos.add(vehiculo);
            }
            arrayAdapter.notifyDataSetChanged();

        } catch (MalformedURLException e) {
            Toast.makeText(root.getContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(root.getContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Toast.makeText(root.getContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
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
            arrayAdapter.notifyDataSetChanged();

        } catch (MalformedURLException e) {
            Toast.makeText(root.getContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(root.getContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Toast.makeText(root.getContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }
    public void getListaUsuario() {

        //UIL Del web service
        String host = global.getHost();
        String ws = host.concat("/usuario");

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

                Usuario usuario = new Usuario(Integer.parseInt(objeto.optString("id")), objeto.optString("nombre"));
                usuarios.add(usuario);
            }
            arrayAdapter.notifyDataSetChanged();

        } catch (MalformedURLException e) {
            Toast.makeText(root.getContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(root.getContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Toast.makeText(root.getContext(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

}