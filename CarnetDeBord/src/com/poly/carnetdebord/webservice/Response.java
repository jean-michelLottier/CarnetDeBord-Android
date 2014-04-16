package com.poly.carnetdebord.webservice;

/**
 * <p>
 * template of response request.
 * </p>
 * 
 * @author jean-michel
 * 
 */
public class Response {
	public static final int BAD_REQUEST = 400;
	public static final int UNAUTHORIZED = 401;
	public static final int INTERNAL_SERVER_ERROR = 500;

	private int status;
	private String content;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
