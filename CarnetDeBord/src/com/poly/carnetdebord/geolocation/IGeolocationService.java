package com.poly.carnetdebord.geolocation;

import android.location.Location;

public interface IGeolocationService {
	public boolean start();

	public void pause();

	public Geolocation getGeolocation();

	public void stopProgressBar();

	public Geolocation findLocalGeolocationByTicketID(long ticketID);

	public void saveLocalGeolocation(Geolocation geolocation);

	public void saveRemoteGeolocation(Geolocation geolocation);

	public boolean isGPSActivated();

	public Location getLocation();
}
