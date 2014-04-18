package com.poly.carnetdebord.service;

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

import com.poly.carnetdebord.dialogbox.CarnetDeBordDialogFragment;
import com.poly.carnetdebord.ticket.ConsultTicketActivity;
import com.poly.carnetdebord.ticket.CreateTicketActivity;
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
	public static final String MAP_GOOGLE_URL_PATH = "http://maps.google.com/maps/api/geocode/json?latlng=latitude,longitude&sensor=true";

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

		System.out.println("Envoi...");
		Response response = new Response();
		if (content == null || content.isEmpty()) {
			System.out.println("Json non reconnu");
			response.setStatus(Response.BAD_REQUEST);
			return response;
		}

		HttpURLConnection con = openConnection(urlPath);
		if (con == null) {
			System.out.println("Mauvais URL");
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
			System.out.println("Mauvaise connection");
			response.setStatus(con.getResponseCode());
			response.setContent(readResponse(con));
			con.disconnect();
			System.out.println("Mauvaise Reponse");
		} catch (ProtocolException e) {
			System.out.println("Erreur protocol");
			return null;
		} catch (IOException e) {
			response.setStatus(Response.BAD_REQUEST);
			System.out.println("Erreur IOexception:"+con.getErrorStream());
			return response;
		}

		System.out.println("Reception depuis le serveur");
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
		while (!isConnectingToInternet()) {
			CarnetDeBordDialogFragment dialogFragment = new CarnetDeBordDialogFragment();
			Bundle args = new Bundle();
			args.putInt(CarnetDeBordDialogFragment.BOX_DIALOG_KEY,
					CarnetDeBordDialogFragment.BOX_DIALOG_DISCONNECTED);
			args.putString(CarnetDeBordDialogFragment.BOX_DIALOG_PARAMETER_URL,
					urlPaths[0]);
			args.putString(
					CarnetDeBordDialogFragment.BOX_DIALOG_PARAMETER_REQUESTMETHOD,
					requestMethod.toString());
			dialogFragment.setArguments(args);
			dialogFragment.show(activity.getFragmentManager(),
					"CarnetDeBordDialogFragment");
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
		if (activity instanceof ConsultTicketActivity
				|| activity instanceof CreateTicketActivity) {
			progressDialog = ProgressDialog.show(activity, "Connexion",
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

		if (activity instanceof ConsultTicketActivity) {
			TicketService ticketService = new TicketService(activity);
			ticketService.initConsultTicketActivity(response);
		}

		if (activity instanceof CreateTicketActivity) {
			TicketService ticketService = new TicketService(activity);
			ticketService.initCreateTicketActivity(response);
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
