package com.poly.carnetdebord.geolocation;

import com.poly.carnetdebord.ticket.Ticket;

public class Geolocation {
	private long id;
	private Ticket ticket;
	private double longitude;
	private double latitude;
	private String address;

	public Geolocation() {

	}

	public Geolocation(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public Geolocation(double longitude, double latitude, String address) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.address = address;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
