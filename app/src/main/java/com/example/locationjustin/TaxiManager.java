package com.example.locationjustin;

import android.location.Location;

public class TaxiManager {

    private Location destinationLocation;

    public void setDestinationLocation(Location destinationLocation) {

        this.destinationLocation = destinationLocation;
    }

    // get the distance between current location and destination location
    public float returnTheDistanceToDestinationLocationInMeters(Location currentLocation) {

        if (currentLocation != null && destinationLocation != null) {

            return currentLocation.distanceTo(destinationLocation);
        } else {
            return -100.0f;
        }
    }

    // get the distance between current location and destination location by miles
    public String returnTheMilesBetweenCurrentLocationAndDestinationLocation(Location currentLocation, int metersPerMile) {

        int miles = (int) (returnTheDistanceToDestinationLocationInMeters(currentLocation) / metersPerMile);

        if (miles == 1) {

            return "1 Mile.";
        } else if (miles > 1) {
            return miles + "Miles.";
        } else {

            return "No Miles."
        }
    }

}
