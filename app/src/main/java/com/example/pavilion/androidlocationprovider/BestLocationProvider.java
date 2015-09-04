package com.example.pavilion.androidlocationprovider;

import android.content.Intent;

/**
 * Created by aditya on 21/08/15.
 */
public class BestLocationProvider {

    private static BestLocationProvider instance;

    private BestLocationProvider() {

    }

    public static BestLocationProvider getInstance() {

        if(null == instance) {
            instance = new BestLocationProvider();
        }
        return instance;
    }

    public void getLocation(LocationInformation locInfo) {

        Intent i = new Intent(locInfo.getContext(), LocationUpdaterService.class);

        i.putExtra(LocationConstants.LOC_REQUIRED_ACCURACY, locInfo.getAccuracy());
        i.putExtra(LocationConstants.LOC_MIN_TIME_BETWEEN_UPDATES, locInfo.getMinTimeBetweenUpdates());
        i.putExtra(LocationConstants.LOC_MIN_DISTANCE_BETWEEN_UPDATES, locInfo.getMinDistanceForUpdate());
        i.putExtra(LocationConstants.LOC_RESULT_RECEIVER, locInfo.getResultReceiver());
        i.putExtra(LocationConstants.LOC_MAX_OLD_LOCATION, locInfo.getMaxPermissibleOldLocation());
        i.putExtra(LocationConstants.LOC_TIME_OUT, locInfo.getTimeOut());

        locInfo.getContext().startService(i);
    }
}
