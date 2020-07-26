package com.example.sigeve_android.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private int rol;

    LoggedInUserView(String displayName,int rol) {
        this.displayName = displayName;
        this.rol = rol;
    }

    String getDisplayName() {
        return displayName;
    }

    int getRol() {
        return rol;
    }
}
