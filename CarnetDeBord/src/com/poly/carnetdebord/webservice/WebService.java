package com.poly.carnetdebord.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.poly.carnetdebord.ticket.ConsultTicketActivity;
import com.poly.carnetdebord.ticket.TicketService;

public class WebService extends AsyncTask<String, Response, Response> implements
		IWebService {

	public enum RequestMethod {
		GET, POST, PUT;
	};

	private String content;
	private RequestMethod requestMethod;
	private ProgressDialog progressDialog;
	private final Activity activity;

	// http://serveur10.lerb.polymtl.ca:8080/CarnetDeBord/webresources/ticket/
	public static final String TICKET_URL_PATH = "http://10.0.2.2:8080/CarnetDeBord/webresources/ticket/";

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public RequestMethod getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(RequestMethod requestMethod) {
		this.requestMethod = requestMethod;
	}

	public WebService(Activity activity, RequestMethod requestMethod) {
		this.requestMethod = requestMethod;
		this.content = null;
		this.activity = activity;
	}

	public WebService(Activity activity, RequestMethod requestMethod,
			String content) {
		this.requestMethod = requestMethod;
		this.content = content;
		this.activity = activity;
	}

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

	@Override
	protected Response doInBackground(String... urlPaths) {
		Response response = new Response();
		switch (requestMethod) {
		case POST:
			response = sendPostRequest(urlPaths[0], content);
			break;

		case PUT:
			response = sendPutRequest(urlPaths[0], content);
			break;

		default:
			response = sendGetRequest(urlPaths[0]);
			break;
		}

		return response;
	}

	@Override
	protected void onPreExecute() {
		progressDialog = ProgressDialog.show(activity, "Connexion",
				"Veuillez patienter");
	}

	@Override
	protected void onPostExecute(Response response) {
		if (activity instanceof ConsultTicketActivity) {
			TicketService ticketService = new TicketService(activity);
			ticketService.initConsultTicketActivity(response);
		}

		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
}
