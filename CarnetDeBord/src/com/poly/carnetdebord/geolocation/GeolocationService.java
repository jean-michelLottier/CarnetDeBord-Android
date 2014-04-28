package com.poly.carnetdebord.geolocation;

import java.util.concurrent.ExecutionException;

import org.json.simple.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.poly.carnetdebord.R;
import com.poly.carnetdebord.dashboard.Fragment_CreateTicket;
import com.poly.carnetdebord.dialogbox.CarnetDeBordDialogFragment;
import com.poly.carnetdebord.ticket.TicketService;
import com.poly.carnetdebord.utilities.AppMode;
import com.poly.carnetdebord.webservice.Response;
import com.poly.carnetdebord.webservice.WebService;
import com.poly.carnetdebord.webservice.WebService.RequestMethod;

public class GeolocationService extends Service implements IGeolocationService,
		LocationListener {

	private final Activity activity;
	private LocationManager locationManager;
	private ProgressDialog progressDialog;
	private Location location;

	private boolean isGPSActivated;

	private static String PARAMETER_GEOLOCATION_ID = "geolocationID";
	private static String PARAMETER_LATITUDE = "latitude";
	private static String PARAMETER_LONGITUDE = "longitude";
	private static String PARAMETER_ADDRESS = "address";

	private IGeolocationDAO geolocationDAO;
	private int mode;

	public GeolocationService(Activity activity) {
		this.activity = activity;
		this.mode = AppMode.getInstance().getMode();
	}

	@Override
	public void onLocationChanged(Location location) {
		System.out.println("**location changed**");
		this.location = location;
		if (mode==AppMode.CREATE_TICKET) {
			String urlPath = WebService.MAP_GOOGLE_URL_PATH.replace("latitude",
					String.valueOf(location.getLatitude())).replace(
					"longitude", String.valueOf(location.getLongitude()));
			new WebService(activity, RequestMethod.GET).execute(urlPath);
		} else if (mode == AppMode.FIND_TICKETS) {
			retrieveActualRegion();
		}

		if (mode == AppMode.CREATE_TICKET) {
			pause();
		}
	}

	private void retrieveActualRegion() {
		if (location == null) {
			return;
		}

		GoogleMap googleMap = ((MapFragment) activity.getFragmentManager()
				.findFragmentById(R.id.cb_ticket_map_carto)).getMap();

		LatLng userPosition = new LatLng(location.getLatitude(),
				location.getLongitude());
		googleMap.setMyLocationEnabled(true);
		googleMap.moveCamera(CameraUpdateFactory
				.newLatLngZoom(userPosition, 13));
		googleMap.addMarker(new MarkerOptions().icon(
				BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
				.position(userPosition));

		String urlPath = WebService.TICKET_URL_PATH + "/longitude/"
				+ location.getLongitude() + "/latitude/"
				+ location.getLatitude();
		new WebService(activity, RequestMethod.GET).execute(urlPath);
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
	public LocationManager start() {
		locationManager = (LocationManager) activity
				.getSystemService(Context.LOCATION_SERVICE);

		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			CarnetDeBordDialogFragment dialogFragment = new CarnetDeBordDialogFragment();
			dialogFragment.showGPSUnabledBoxDialog(activity);
			return null;
		}

		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 60000, 0, this);
			isGPSActivated = true;
			return locationManager;
		} else if (locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 60000, 0, this);
			isGPSActivated = true;
			return locationManager;
		}

		return null;
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
			return null;
		}

		Geolocation geolocation = new Geolocation();
		geolocation.setLatitude(location.getLatitude());
		geolocation.setLongitude(location.getLongitude());

		return geolocation;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
