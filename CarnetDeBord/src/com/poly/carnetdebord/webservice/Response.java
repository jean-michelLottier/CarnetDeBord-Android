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
	public static final int NO_CONTENT = 204;
	public static final int CONFLICT = 409;
	public static final int OK = 200;
	public static final int UNSUPPORTED_MEDIA_TYPE = 415;

	private int status;
	private String content;
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

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
