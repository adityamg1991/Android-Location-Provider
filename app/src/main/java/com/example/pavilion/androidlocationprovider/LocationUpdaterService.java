package com.example.pavilion.androidlocationprovider;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;

/**
 * Created by aditya on 21/08/15.
 */
public class LocationUpdaterService extends Service implements LocationListener{

    private LocationManager managerLocation;
    private double accuracy;
    private long intervalMinTime;
    private float intervalMinDistance;
    private ResultReceiver resultReceiver;

    private long maxTimeDiff;
    private long timeOut;

    @Override
    public void onCreate() {
        super.onCreate();
        managerLocation = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(null != intent) {

            Bundle bundle = intent.getExtras();
            if(null != bundle) {

                this.accuracy = bundle.getDouble(LocationConstants.LOC_REQUIRED_ACCURACY);
                this.intervalMinTime = bundle.getLong(LocationConstants.LOC_MIN_TIME_BETWEEN_UPDATES);
                this.intervalMinDistance = bundle.getFloat(LocationConstants.LOC_MIN_DISTANCE_BETWEEN_UPDATES);
                this.resultReceiver = bundle.getParcelable(LocationConstants.LOC_RESULT_RECEIVER);
                this.maxTimeDiff = bundle.getLong(LocationConstants.LOC_MAX_OLD_LOCATION);
                this.timeOut = bundle.getLong(LocationConstants.LOC_TIME_OUT);
            }
        }

        getBestLocation();
        return START_REDELIVER_INTENT;
    }


    private void getBestLocation() {

        // Start timer if timeout is present
        if(this.timeOut != LocationConstants.NO_TIME_OUT) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopServiceAndDeliverResult(null);
                }
            }, this.timeOut);

        }

        if(null != managerLocation) {

            boolean isGPSEnabled = managerLocation
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            boolean isNetworkEnabled = managerLocation
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!isGPSEnabled && !isNetworkEnabled) {
                stopServiceAndDeliverResult(null);
            } else {

                /**
                 * At least one of GPS and Network is active
                 */
                if(isGPSEnabled) {

                    Location lastGPSLocation = managerLocation.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(null != lastGPSLocation) {

                        // Time of last GPS location
                        long locationTime = lastGPSLocation.getTime();

                        if((System.currentTimeMillis() - locationTime) < maxTimeDiff
                                && lastGPSLocation.getAccuracy() < accuracy) {

                            stopServiceAndDeliverResult(lastGPSLocation);
                            return;
                        }
                    }
                }

                if(isNetworkEnabled) {

                    Location lastNetworkLocation = managerLocation.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if(null != lastNetworkLocation) {

                        // Time of last Network location
                        long locationTime = lastNetworkLocation.getTime();

                        if((System.currentTimeMillis() - locationTime) < maxTimeDiff
                                && lastNetworkLocation.getAccuracy() < accuracy) {

                            stopServiceAndDeliverResult(lastNetworkLocation);
                            return;
                        }
                    }
                }

                managerLocation.requestLocationUpdates
                        (LocationManager.GPS_PROVIDER, intervalMinTime, intervalMinDistance, this);

                managerLocation.requestLocationUpdates
                        (LocationManager.NETWORK_PROVIDER, intervalMinTime, intervalMinDistance, this);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        if(location.getAccuracy() < this.accuracy) {
            stopServiceAndDeliverResult(location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    private void stopServiceAndDeliverResult(Location loc) {

        if(managerLocation != null && resultReceiver != null) {

            // Remove location updates
            managerLocation.removeUpdates(this);

            // Pass data to calling component
            Bundle bundle = new Bundle();
            bundle.putParcelable(LocationConstants.LOCATION, loc);
            resultReceiver.send(Activity.RESULT_OK, bundle);
            stopSelf();

            managerLocation = null;
            resultReceiver = null;
        }
    }
}
