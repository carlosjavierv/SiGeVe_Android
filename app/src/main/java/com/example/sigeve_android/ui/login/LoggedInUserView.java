package com.example.sigeve_android.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private int id;
    private int rol;
    private String displayName;

    LoggedInUserView(int id,String displayName,int rol) {
        this.id = id;
        this.rol = rol;
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }

    int getRol() {
        return rol;
    }

    int getId() {
        return id;
    }
}
