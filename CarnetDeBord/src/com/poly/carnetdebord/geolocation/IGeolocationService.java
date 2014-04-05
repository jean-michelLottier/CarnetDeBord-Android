package com.poly.carnetdebord.geolocation;

public interface IGeolocationService {
	public boolean start();
	
	public void pause();
	
	public Geolocation getGeolocation();
}
