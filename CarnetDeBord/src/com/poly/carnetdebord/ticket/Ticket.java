package com.poly.carnetdebord.ticket;

import java.util.Date;

public class Ticket {
	private long id;
	private long userID;
	private Date postedDate;
	private String title;
	private String message;
	private String type;
	private String annexInfo;
	private boolean state;
	private long relevance;
	private long geolocationID;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserID() {
		return userID;
	}

	public void setUserID(long userID) {
		this.userID = userID;
	}

	public Date getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAnnexInfo() {
		return annexInfo;
	}

	public void setAnnexInfo(String annexInfo) {
		this.annexInfo = annexInfo;
	}

	public boolean getState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public long getRelevance() {
		return relevance;
	}

	public void setRelevance(long relevance) {
		this.relevance = relevance;
	}

	public long getGeolocationID() {
		return geolocationID;
	}

	public void setGeolocationID(long geolocationID) {
		this.geolocationID = geolocationID;
	}

	public void show() {
		System.out.println("id : " + id + ", userFK : " + userID
				+ ", postedDate : " + postedDate + ", title : " + title
				+ ", message : " + message + ", type : " + type
				// + ", ammexInfo : " + annexInfo == null ? "null" : annexInfo
				+ ", state : " + state + ", relevance : " + relevance);
	}
}
