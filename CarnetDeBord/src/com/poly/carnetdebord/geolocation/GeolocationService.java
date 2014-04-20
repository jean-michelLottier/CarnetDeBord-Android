package com.poly.carnetdebord.geolocation;

import java.util.concurrent.ExecutionException;

import org.json.simple.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.poly.carnetdebord.ticket.CreateTicketActivity;
import com.poly.carnetdebord.ticket.TicketService;
import com.poly.carnetdebord.webservice.Response;
import com.poly.carnetdebord.webservice.WebService;
import com.poly.carnetdebord.webservice.WebService.RequestMethod;

public class GeolocationService implements IGeolocationService,
		LocationListener {

	private final Activity activity;
	private final LocationManager locationManager;
	private ProgressDialog progressDialog;
	private Location location;

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

		start();

		if (locationManager != null) {
			location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		this.location = location;
		if (activity instanceof CreateTicketActivity) {
			String urlPath = WebService.MAP_GOOGLE_URL_PATH.replace("latitude",
					String.valueOf(location.getLatitude())).replace(
					"longitude", String.valueOf(location.getLongitude()));
			new WebService(activity, RequestMethod.GET).execute(urlPath);
		}

		pause();
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
	public void stopProgressBar() {
		if (progressDialog != null && progressDialog.isShowing()) {
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

	@Override
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public Geolocation getGeolocation() {
		if (location == null) {
			System.out
					.println("*******************location null*******************");
			return null;
		}

		Geolocation geolocation = new Geolocation();
		geolocation.setLatitude(location.getLatitude());
		geolocation.setLongitude(location.getLongitude());

		return geolocation;
	}
}
