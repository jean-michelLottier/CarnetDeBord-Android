package com.poly.carnetdebord.ticket;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.poly.carnetdebord.R;
import com.poly.carnetdebord.geolocation.Geolocation;
import com.poly.carnetdebord.localstorage.SessionManager;
import com.poly.carnetdebord.webservice.IWebService;
import com.poly.carnetdebord.webservice.Response;
import com.poly.carnetdebord.webservice.WebService;
import com.poly.carnetdebord.webservice.WebService.RequestMethod;

public class TicketService implements ITicketService {

	public static final String PARAMETER_TICKET_ID = "ticketID";
	public static final String PARAMETER_USER_ID = "userID";
	private static final String PARAMETER_TITLE = "title";
	private static final String PARAMETER_MESSAGE = "message";
	private static final String PARAMETER_TYPE = "type";
	private static final String PARAMETER_ANNEX_INFO = "annexInfo";
	private static final String PARAMETER_STATE = "state";
	private static final String PARAMETER_POSTED_DATE = "postedDate";
	private static final String PARAMETER_RELEVANCE = "relevance";

	private final Activity activity;

	private IWebService webService;

	private ITicketDAO ticketDAO;

	private SessionManager session;

	public TicketService(Activity activity) {
		this.activity = activity;
	}

	private SessionManager initSession() {
		session = new SessionManager(activity);
		return session;
	}

	private void quitSession() {
		session.clearSession();
	}

	@Override
	public ArrayList<Ticket> getUserTickets() {
		System.out
				.println("***********TicketService getUserTickets***********");
		String urlPath = WebService.TICKET_URL_PATH.replace("userid", "1").replace("ticketid",
				"1");
		System.out.println("urlpath : " + urlPath);
		// webService = new WebService();
		AsyncTask<String, Response, Response> response = new WebService(
				activity, RequestMethod.GET).execute(urlPath);
		try {
			System.out.println("status : " + response.get().getStatus()
					+ ", content : " + response.get().getContent());
		} catch (Exception e) {
			System.out.println("!!!!!!!!!!!!!!PROBLEM!!!!!!!!!!!!!!");
		}
		System.out.println("*************************************************");
		return null;
	}

	@Override
	public void saveLocalTicket(Ticket ticket) {
		if (ticket == null) {
			return;
		}

		ticket.setPostedDate(Calendar.getInstance().getTime());
		ticketDAO = new TicketDAO(activity);
		ticketDAO.persist(ticket);
	}

	@Override
	public ArrayList<Ticket> researchByTitle(String title) {
		if (title == null || title.isEmpty()) {
			return null;
		}

		ticketDAO = new TicketDAO(activity);
		return ticketDAO.findTicketsByTitle(title);
	}

	@Override
	public Ticket findLocalTicketByID(long id) {
		if (id < 0) {
			return null;
		}

		ticketDAO = new TicketDAO(activity);

		return ticketDAO.findTicketByID(id);
	}

	@SuppressWarnings("unchecked")
	public static JSONObject convertToJSON(Ticket ticket) {
		if (ticket == null) {
			return null;
		}

		JSONObject json = new JSONObject();

		json.put(PARAMETER_USER_ID, String.valueOf(ticket.getUserID()));
		json.put(PARAMETER_TITLE, ticket.getTitle());
		json.put(PARAMETER_MESSAGE, ticket.getMessage());
		json.put(PARAMETER_STATE, ticket.getState());
		json.put(PARAMETER_TYPE, ticket.getType());
		if (ticket.getAnnexInfo() == null) {
			json.put(PARAMETER_ANNEX_INFO, "");
		} else {
			json.put(PARAMETER_ANNEX_INFO, ticket.getAnnexInfo());
		}

		return json;
	}

	private Ticket convertFromJSON(JSONObject json) {
		if (json == null) {
			return null;
		}

		Ticket ticket = new Ticket();
		ticket.setId(Long.valueOf(json.get(PARAMETER_TICKET_ID).toString()));
		ticket.setAnnexInfo(json.get(PARAMETER_ANNEX_INFO).toString());
		ticket.setMessage(json.get(PARAMETER_MESSAGE).toString());
		DateFormat format = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy",
				Locale.ENGLISH);
		String date = json.get(PARAMETER_POSTED_DATE).toString();
		try {
			ticket.setPostedDate(format.parse(date));
		} catch (ParseException e) {
			System.err.println("Impossible to parse date from json object.");
			e.printStackTrace();
		}
		ticket.setRelevance(Integer.valueOf(json.get(PARAMETER_RELEVANCE)
				.toString()));
		ticket.setState(Boolean.valueOf(json.get(PARAMETER_STATE).toString()));
		ticket.setTitle(json.get(PARAMETER_TITLE).toString());
		ticket.setType(PARAMETER_TYPE);

		return ticket;
	}

	@Override
	public void initConsultTicketActivity(Response response) {
		Ticket ticket = new Ticket();
		Geolocation geolocation = new Geolocation();
		try {
			JSONObject json = (JSONObject) new JSONParser().parse(response
					.getContent());
			ticket = convertFromJSON(json);
			geolocation.setTicket(ticket);
			geolocation.setId(Long
					.valueOf(json.get("geolocationID").toString()));
			geolocation.setLatitude(Double.valueOf(json.get("latitude")
					.toString()));
			geolocation.setLongitude(Double.valueOf(json.get("logitude")
					.toString()));
			geolocation.setAddress(json.get("address").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		initConsultTicketActivity(geolocation);
	}

	@Override
	public void initConsultTicketActivity(Geolocation geolocation) {
		TextView locationTextView = (TextView) activity
				.findViewById(R.id.cb_ticket_location);
		locationTextView.setText(geolocation.getAddress());
		TextView titleTextView = (TextView) activity
				.findViewById(R.id.cb_ticket_title);
		titleTextView.setText(geolocation.getTicket().getTitle());
		TextView contentTextView = (TextView) activity
				.findViewById(R.id.cb_ticket_content);
		contentTextView.setText(geolocation.getTicket().getMessage());
		GoogleMap googleMap = ((MapFragment) activity.getFragmentManager()
				.findFragmentById(R.id.cb_ticket_map)).getMap();
		LatLng ticketPosition = new LatLng(geolocation.getLatitude(),
				geolocation.getLongitude());
		googleMap.setMyLocationEnabled(true);
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ticketPosition,
				13));

		String snippet;
		if (geolocation.getTicket().getMessage().length() > 35) {
			snippet = geolocation.getTicket().getMessage().substring(0, 35)
					+ "...";
		} else {
			snippet = geolocation.getTicket().getMessage();
		}
		googleMap.addMarker(new MarkerOptions()
				.title(geolocation.getTicket().getTitle())
				.position(ticketPosition).snippet(snippet));
		activity.getFragmentManager().findFragmentById(R.id.cb_ticket_map)
				.getView().setVisibility(View.INVISIBLE);
	}
}
