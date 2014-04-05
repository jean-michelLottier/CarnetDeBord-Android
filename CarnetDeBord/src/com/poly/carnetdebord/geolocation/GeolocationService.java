package com.poly.carnetdebord.geolocation;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GeolocationService implements IGeolocationService,
		LocationListener {

	private final Context context;
	private final LocationManager locationManager;

	/**
	 * longitude in degrees
	 */
	private double longitude;
	/**
	 * latitude in degrees
	 */
	private double latitude;
	private boolean isGPSActivated;

	public GeolocationService(Context context) {
		this.context = context;
		this.locationManager = (LocationManager) context
				.getSystemService(Activity.LOCATION_SERVICE);
		start();
	}

	@Override
	public void onLocationChanged(Location location) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean start() {
		if (isGPSActivated) {
			return true;
		}
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 60000, 0, this);
			isGPSActivated = true;
			return true;
		} else if (locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 60000, 0, this);
			isGPSActivated = true;
			return true;
		}
		return false;
	}

	@Override
	public void pause() {
		if (locationManager != null) {
			locationManager.removeUpdates(this);
		}
		isGPSActivated = false;
	}

	@Override
	public Geolocation getGeolocation() {
		if (locationManager != null) {
			latitude = locationManager.getLastKnownLocation(
					LocationManager.GPS_PROVIDER).getLatitude();
			longitude = locationManager.getLastKnownLocation(
					LocationManager.GPS_PROVIDER).getLongitude();
		}

		Geolocation geolocation = new Geolocation(longitude, latitude);

		return geolocation;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
}
