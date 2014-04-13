package com.poly.carnetdebord.geolocation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GeolocationService implements IGeolocationService,
		LocationListener {

	private final Context context;
	private final LocationManager locationManager;
	private final Geocoder geocoder;
	private final ProgressDialog progressDialog;

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
		this.geocoder = new Geocoder(context, Locale.getDefault());
		progressDialog = ProgressDialog.show(context, "Géolocalisation",
				"En cours", true);
		start();
	}

	@Override
	public void onLocationChanged(Location location) {
		System.out.println("************** onLocationChanged  **************");
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		stopProgressBar();
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
		System.out.println("************** start **************");
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

		System.out.println("************** END start **************");
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
			Location location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location == null) {
				return null;
			}

			stopProgressBar();
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		}
		System.out.println("latitude : " + latitude + ", logitude : "
				+ longitude);
		Geolocation geolocation = new Geolocation(longitude, latitude);
		List<Address> addresses = null;
		try {
			addresses = geocoder.getFromLocation(latitude, longitude, 1);
		} catch (IOException e) {
			System.err.println("Impossible to get address location");
			return null;
		}

		if (addresses == null || addresses.isEmpty()) {
			return null;
		}

		Address address = addresses.get(0);
		geolocation.setAddress(address.getAddressLine(0).trim());
		geolocation.setAdminArea(address.getAdminArea().trim());
		geolocation.setCountryName(address.getCountryName().trim());
		geolocation.setLocality(address.getLocality().trim());
		geolocation.setPostalCode(address.getPostalCode().trim());

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

	@Override
	public void stopProgressBar() {
		if (progressDialog != null && progressDialog.isShowing()) {
			System.out
					.println("************** onLocationChanged fermeture progress dialog **************");
			progressDialog.dismiss();
		}
	}
}
