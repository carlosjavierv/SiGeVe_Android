package com.example.sigeve_android;

import android.app.Application;
/**
 * this class is used as globals
 * */
public class Global extends Application {
    private int idUsuario;
    private String host = "http://192.168.0.108:8000/api";
    private String publicPath = "http://192.168.0.108:8000";

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPublicPath() {
        return publicPath;
    }

    public void setPublicPath(String publicPath) {
        this.publicPath = publicPath;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}