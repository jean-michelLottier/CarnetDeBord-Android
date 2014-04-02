package com.poly.carnetdebord.webservice;

public interface IWebService {
	/**
	 * <p>
	 * Send GET request.
	 * </p>
	 * 
	 * @param urlPath
	 * @return {@link Response} otherwise null. Please check the response's
	 *         status
	 */
	public Response sendGetRequest(String urlPath);

	/**
	 * <p>
	 * Send POST request.
	 * </p>
	 * 
	 * @param urlPath
	 * @param content
	 * @return {@link Response} otherwise null. Please check the response's
	 *         status
	 */
	public Response sendPostRequest(String urlPath, String content);

	/**
	 * <p>
	 * Send PUT request.
	 * </p>
	 * 
	 * @param urlPath
	 * @param content
	 * @return {@link Response} otherwise null. Please check the response's
	 *         status
	 */
	public Response sendPutRequest(String urlPath, String content);
}
