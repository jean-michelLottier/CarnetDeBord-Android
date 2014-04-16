package com.poly.carnetdebord.geolocation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.json.simple.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.poly.carnetdebord.ticket.TicketService;
import com.poly.carnetdebord.webservice.Response;
import com.poly.carnetdebord.webservice.WebService;
import com.poly.carnetdebord.webservice.WebService.RequestMethod;

public class GeolocationService implements IGeolocationService,
		LocationListener {

	private final Activity activity;
	private final LocationManager locationManager;
	private Geocoder geocoder;
	private ProgressDialog progressDialog;

	/**
	 * longitude in degrees
	 */
	public static double longitude;
	/**
	 * latitude in degrees
	 */
	public static double latitude;
	private boolean isGPSActivated;

	private static String PARAMETER_GEOLOCATION_ID = "geolocationID";
	private static String PARAMETER_LATITUDE = "latitude";
	private static String PARAMETER_LONGITUDE = "longitude";
	private static String PARAMETER_ADDRESS = "address";

	private IGeolocationDAO geolocationDAO;

	public GeolocationService(Activity activity) {
		this.activity = activity;
		this.locationManager = (LocationManager) activity
				.getSystemService(Context.LOCATION_SERVICE);
		this.geocoder = new Geocoder(activity, Locale.getDefault());

		start();

		if (locationManager != null) {
			Location location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location != null) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
			}
		}
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
			Location location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location != null) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
			}

			stopProgressBar();
		}
		System.out.println("$$$$$$$$$$$latitude : " + latitude
				+ ", longitude : " + longitude + "$$$$$$$$$$$");
		Geolocation geolocation = new Geolocation(longitude, latitude);
		List<Address> addresses = null;
		try {
			geocoder = new Geocoder(activity);
			addresses = geocoder.getFromLocation(latitude, longitude, 1);
		} catch (IOException e) {
			System.err.println("Impossible to get address location");
			e.printStackTrace();
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
		GeolocationService.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		GeolocationService.latitude = latitude;
	}

	@Override
	public void stopProgressBar() {
		if (progressDialog != null && progressDialog.isShowing()) {
			System.out
					.println("************** onLocationChanged fermeture progress dialog **************");
			progressDialog.dismiss();
		}
	}

	@Override
	public Geolocation findLocalGeolocationByTicketID(long ticketID) {
		if (ticketID < 0) {
			return null;
		}

		geolocationDAO = new GeolocationDAO(activity);
		return geolocationDAO.findGeolocationByTicketID(ticketID);
	}

	@Override
	public void saveLocalGeolocation(Geolocation geolocation) {
		if (geolocation == null) {
			return;
		}

		geolocationDAO = new GeolocationDAO(activity);
		geolocationDAO.persist(geolocation);
	}

	@Override
	public void saveRemoteGeolocation(Geolocation geolocation) {
		// TODO Auto-generated method stub
		AsyncTask<String, Response, Response> response = new WebService(
				activity, RequestMethod.POST, convertToJSON(geolocation)
						.toString()).execute(WebService.TICKET_URL_PATH);

		try {
			if (response.get().getStatus() == Response.BAD_REQUEST) {
				System.err.println("Request no valid!");
				return;
			} else if (response.get().getStatus() == Response.INTERNAL_SERVER_ERROR) {
				System.err.println("Problem with server");
				return;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}

	@SuppressWarnings("unchecked")
	public static JSONObject convertToJSON(Geolocation geolocation) {
		if (geolocation == null) {
			return null;
		}

		JSONObject json = TicketService.convertToJSON(geolocation.getTicket());
		if (json == null) {
			json = new JSONObject();
		}

		json.put(PARAMETER_GEOLOCATION_ID, geolocation.getId());
		json.put(PARAMETER_LATITUDE, geolocation.getLatitude());
		json.put(PARAMETER_LONGITUDE, geolocation.getLongitude());
		json.put(PARAMETER_ADDRESS, geolocation.getAddress());

		return json;
	}

	@Override
	public boolean isGPSActivated() {
		return isGPSActivated;
	}

	public void setGPSActivated(boolean isGPSActivated) {
		this.isGPSActivated = isGPSActivated;
	}
}
