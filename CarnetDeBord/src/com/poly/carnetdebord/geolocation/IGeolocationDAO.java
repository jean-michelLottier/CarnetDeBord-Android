package com.poly.carnetdebord.geolocation;

public interface IGeolocationDAO {
	public void persist(Geolocation geolocation);

	public Geolocation findGeolocationByTicketID(long ticketID);
}
