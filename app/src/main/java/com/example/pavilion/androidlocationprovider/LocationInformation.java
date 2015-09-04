package com.example.pavilion.androidlocationprovider;

import android.content.Context;
import android.os.ResultReceiver;

/**
 * Created by aditya on 21/08/15.
 */
public class LocationInformation {

    private Context context;

    // Minimum Desired Accuracy in meters
    private float accuracy;

    // Time between updates in milliseconds
    private long minTimeBetweenUpdates;

    // Distance between successive location updates (in meters)
    private float minDistanceForUpdate;

    // Max old permissible location (time in milli seconds)
    private long maxPermissibleOldLocation;

    private ResultReceiver resultReceiver;

    // Max time for search for location (time in milli seconds)
    private long timeOut;


    public long getTimeOut() {
        return timeOut;
    }

    /**
     * Time in milli second
     * @param context
     * @param accuracy
     * @param minTimeBetweenUpdates
     * @param minDistanceForUpdate
     * @param resultReceiver
     * @param maxPermissibleOldLocation
     * @param timeOut
     */
    public LocationInformation(Context context,
                               float accuracy, long minTimeBetweenUpdates,
                               long minDistanceForUpdate, ResultReceiver resultReceiver, long maxPermissibleOldLocation, long timeOut) {
        this.context = context;
        this.accuracy = accuracy;
        this.minTimeBetweenUpdates = minTimeBetweenUpdates;
        this.minDistanceForUpdate = minDistanceForUpdate;
        this.resultReceiver = resultReceiver;
        this.maxPermissibleOldLocation = maxPermissibleOldLocation;
        this.timeOut = timeOut;
    }


    public LocationInformation(Context context, ResultReceiver resultReceiver) {

        this(context, 100, 0, 0, resultReceiver, Long.MAX_VALUE, LocationConstants.NO_TIME_OUT);
    }


    public long getMaxPermissibleOldLocation() {
        return maxPermissibleOldLocation;
    }

    public Context getContext() {
        return context;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public long getMinTimeBetweenUpdates() {
        return minTimeBetweenUpdates;
    }

    public float getMinDistanceForUpdate() {
        return minDistanceForUpdate;
    }

    public ResultReceiver getResultReceiver() {
        return resultReceiver;
    }
}
