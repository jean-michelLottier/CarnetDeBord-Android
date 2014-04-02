package com.poly.carnetdebord.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class WebService implements IWebService {

	@Override
	public Response sendGetRequest(String urlPath) {
		Response response = new Response();
		HttpURLConnection con = openConnection(urlPath);
		if (con == null) {
			response.setStatus(Response.BAD_REQUEST);
			return response;
		}

		try {
			con.setRequestMethod("GET");
			response.setStatus(con.getResponseCode());
			response.setContent(readResponse(con));
			con.disconnect();
		} catch (ProtocolException e) {
			return null;
		} catch (IOException e) {
			response.setStatus(Response.BAD_REQUEST);
			return response;
		}

		return response;
	}

	@Override
	public Response sendPostRequest(String urlPath, String content) {
		Response response = new Response();
		if (content == null || content.isEmpty()) {
			response.setStatus(Response.BAD_REQUEST);
			return response;
		}

		HttpURLConnection con = openConnection(urlPath);
		if (con == null) {
			response.setStatus(Response.BAD_REQUEST);
			return response;
		}

		try {
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			con.connect();
			writeContentRequest(con, content);
			response.setStatus(con.getResponseCode());
			response.setContent(readResponse(con));
			con.disconnect();
		} catch (ProtocolException e) {
			return null;
		} catch (IOException e) {
			response.setStatus(Response.BAD_REQUEST);
			return response;
		}

		return response;
	}

	@Override
	public Response sendPutRequest(String urlPath, String content) {
		Response response = new Response();
		if (content == null || content.isEmpty()) {
			response.setStatus(Response.BAD_REQUEST);
			return response;
		}

		HttpURLConnection con = openConnection(urlPath);
		if (con == null) {
			response.setStatus(Response.BAD_REQUEST);
			return response;
		}

		try {
			con.setRequestMethod("PUT");
			con.setDoOutput(true);
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			con.connect();
			writeContentRequest(con, content);
			response.setStatus(con.getResponseCode());
			response.setContent(readResponse(con));
			con.disconnect();
		} catch (ProtocolException e) {
			return null;
		} catch (IOException e) {
			response.setStatus(Response.BAD_REQUEST);
			return response;
		}

		return response;
	}

	private HttpURLConnection openConnection(String urlPath) {
		HttpURLConnection con;
		try {
			URL url = new URL(urlPath);
			con = (HttpURLConnection) url.openConnection();
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		}

		return con;
	}

	private String readResponse(HttpURLConnection con) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer result = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			result.append(inputLine);
		}
		in.close();

		return result.toString();
	}

	private void writeContentRequest(HttpURLConnection con, String content)
			throws IOException {
		OutputStreamWriter osw;
		osw = new OutputStreamWriter(con.getOutputStream());
		osw.write(content);
		osw.flush();
		osw.close();
	}
}
