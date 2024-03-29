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

            return "No Miles.";
        }
    }

    //get the time by distanceInMeters and milesPerHour and metersPerMile
    public String returnTheTimeLeftToGetToDestinationLocation(Location currentLocation, float milesPerHour, float metersPerMile) {

        float distanceInMeters = returnTheDistanceToDestinationLocationInMeters(currentLocation);
        float timeLeft = distanceInMeters / ((milesPerHour) * (metersPerMile));

        String timeResult = "";

        int timeLeftInHours = (int) timeLeft;

        if(timeLeftInHours == 1)
        {
            timeResult = timeResult + "1 Hour " ;
        }
        else if(timeLeftInHours > 1)
        {
            timeResult = timeResult + timeLeftInHours +" Hours " ;
        }

        int minutesLeft = (int) ((timeLeft - timeLeftInHours) * 60);

        if(minutesLeft == 1) {

            timeResult = timeResult + "1 Minute ";
        }

        else if(minutesLeft > 1) {

            timeResult = timeResult + minutesLeft + " Minutes ";
        }

        if(timeLeftInHours <= 0 && minutesLeft <= 0) {

            timeResult = "Less than a minute is left to get the destination location.";
        }

        return  timeResult;

    }

}
