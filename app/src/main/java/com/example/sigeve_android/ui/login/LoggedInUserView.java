package com.example.sigeve_android.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private int role;

    LoggedInUserView(String displayName,int role) {
        this.displayName = displayName;
        this.role = role;
    }

    String getDisplayName() {
        return displayName;
    }

    int getRole() {
        return role;
    }
}
