package com.example.sigeve_android;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class SvLocationListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        if (location != null)
        {
            // Do something knowing the location changed by the distance you requested
            System.out.println("||||||||||||||||||||||||||||||||||||||||||||>");
            System.out.println(location.getLatitude());
            System.out.println(location.getLongitude());
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
