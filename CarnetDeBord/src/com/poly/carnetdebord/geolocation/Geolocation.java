package com.poly.carnetdebord.geolocation;

import com.poly.carnetdebord.ticket.Ticket;

public class Geolocation {
	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getAdminArea() {
		return adminArea;
	}

	public void setAdminArea(String adminArea) {
		this.adminArea = adminArea;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	private long id;
	private Ticket ticket;
	private double longitude;
	private double latitude;
	private String address;
	private String countryName;
	private String adminArea;
	private String locality;
	private String postalCode;

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

	public String getFullAdress() {
		StringBuilder sb = new StringBuilder();
		if (address != null && !address.isEmpty()) {
			sb.append(address);
		}
		if (locality != null && !locality.isEmpty()) {
			sb.append(" ").append(locality);
		}
		if (adminArea != null && !adminArea.isEmpty()) {
			sb.append(" ").append(adminArea);
		}
		if (postalCode != null && !postalCode.isEmpty()) {
			sb.append(" ").append(postalCode);
		}
		if (countryName != null && !countryName.isEmpty()) {
			sb.append(" ").append(countryName);
		}

		return sb.toString();
	}
}
