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
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.poly.carnetdebord.dashboard.Fragment_FindTickets;
import com.poly.carnetdebord.dashboard.Fragment_ConsultTicket;
import com.poly.carnetdebord.dashboard.Fragment_CreateTicket;
import com.poly.carnetdebord.dialogbox.CarnetDeBordDialogFragment;
import com.poly.carnetdebord.login.ILoginService;
import com.poly.carnetdebord.login.LoginActivity;
import com.poly.carnetdebord.login.LoginService;
import com.poly.carnetdebord.login.RegisterActivity;
import com.poly.carnetdebord.ticket.TicketService;
import com.poly.carnetdebord.utilities.AppMode;

public class WebService extends AsyncTask<String, Response, Response> implements
		IWebService {

	public enum RequestMethod {
		GET, POST, PUT;
	};

	private String content;
	private RequestMethod requestMethod;
	private ProgressDialog progressDialog;
	private Activity activity;
	private int mode;

	// http://10.0.2.2:8080/CarnetDeBord/webresources/ticket/
	public static final String TICKET_URL_PATH = "http://serveur10.lerb.polymtl.ca:8080/CarnetDeBord/webresources/ticket/";
	public static final String MAP_GOOGLE_URL_PATH = "http://maps.google.com/maps/api/geocode/json?latlng=latitude,longitude&sensor=true";
	public static final String HISTORICAL_URL_PATH = "http://serveur10.lerb.polymtl.ca:8080/CarnetDeBord/webresources/historical/";

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
		this.mode = AppMode.getInstance().getMode();
		this.activity = activity;
	}

	public WebService(Activity activity,RequestMethod requestMethod,
			String content) {
		this.requestMethod = requestMethod;
		this.content = content;
		this.mode = AppMode.getInstance().getMode();
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
		// in.close();
		return result.toString();
	}

	private void writeContentRequest(HttpURLConnection con, String content)
			throws IOException {
		OutputStreamWriter osw;
		osw = new OutputStreamWriter(con.getOutputStream());
		osw.write(content);
		osw.flush();
		// osw.close();
	}

	@Override
	protected Response doInBackground(String... urlPaths) {
		while (!isConnectingToInternet()) {
			CarnetDeBordDialogFragment dialogFragment = new CarnetDeBordDialogFragment();
			dialogFragment.showDisconnectedBoxDialog(activity, urlPaths[0], requestMethod);
			return null;
		}

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

		response.setUrl(urlPaths[0]);
		return response;
	}

	@Override
	protected void onPreExecute() {
		if (mode == AppMode.CONSULT_TICKET || mode == AppMode.CREATE_TICKET
				|| mode == AppMode.LOGIN) {
			progressDialog = ProgressDialog.show(activity, "Connexion",
					"Veuillez patienter");
		}

		if (mode == AppMode.REGISTER) {
			progressDialog = ProgressDialog.show(activity, "Enregistrement",
					"Veuillez patienter");
		}
	}
	

	@Override
	protected void onPostExecute(Response response) {
		if (response == null) {
			Toast.makeText(activity, "Vous êtes déconnecté.", Toast.LENGTH_LONG)
					.show();
			return;
		}

		if (mode == AppMode.CONSULT_TICKET) {
			TicketService ticketService = new TicketService(activity);
			ticketService.initConsultTicketActivity(response);
		}

		if (mode == AppMode.CREATE_TICKET) {
			TicketService ticketService = new TicketService(activity);
			ticketService.initCreateTicketActivity(response);
		}

		if (mode == AppMode.FIND_TICKETS) {
			TicketService ticketService = new TicketService(activity);
			ticketService.initCartographyTicketActivity(response);
		}

		if (mode == AppMode.LOGIN) {
			ILoginService loginService = LoginService.getInstance(activity);
			loginService.initSession(response);
		}

		if (mode == AppMode.REGISTER) {
			ILoginService loginService = LoginService.getInstance(activity);
			loginService.finishRegister(response);
		}
		
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	@Override
	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}
}
