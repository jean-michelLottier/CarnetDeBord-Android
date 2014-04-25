package com.poly.carnetdebord.ticket;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.poly.carnetdebord.R;
import com.poly.carnetdebord.dialogbox.CarnetDeBordDialogFragment;
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
		String urlPath = WebService.TICKET_URL_PATH.replace("userid", "1")
				.replace("ticketid", "1");
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
		ticket.setUserID(Long.valueOf(json.get(PARAMETER_USER_ID).toString()));
		ticket.setId(Long.valueOf(json.get(PARAMETER_TICKET_ID).toString()));
		if (json.get(PARAMETER_ANNEX_INFO) != null) {
			ticket.setAnnexInfo(json.get(PARAMETER_ANNEX_INFO).toString());
		}
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
		JSONArray jsonArray = new JSONArray();
		try {
			JSONObject json = (JSONObject) new JSONParser().parse(response
					.getContent());
			JSONObject jsonTicket = (JSONObject) new JSONParser().parse(json
					.get("ticket").toString());
			System.out.println(jsonTicket.toJSONString());
			jsonArray = (JSONArray) new JSONParser().parse(json.get(
					"monitoring").toString());

			ticket = convertFromJSON(jsonTicket);
			geolocation.setTicket(ticket);
			geolocation.setId(Long
					.valueOf(json.get("geolocationID").toString()));
			geolocation.setLatitude(Double.valueOf(json.get("latitude")
					.toString()));
			geolocation.setLongitude(Double.valueOf(json.get("longitude")
					.toString()));
			geolocation.setAddress(json.get("address").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		initConsultTicketActivity(geolocation);
		initGraphView(jsonArray);
	}

	private void initGraphView(JSONArray jsonArray) {
		if (jsonArray == null || jsonArray.isEmpty()) {
			return;
		}

		DateFormat format1 = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy",
				Locale.ENGLISH);
		DateFormat format2 = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);

		HashMap<String, Integer> results = new HashMap<String, Integer>();
		for (int i = 0; i < jsonArray.size(); ++i) {
			JSONObject jsono;
			try {
				jsono = (JSONObject) new JSONParser().parse(jsonArray.get(i)
						.toString());

				System.out.println(jsono.toJSONString());
				Date date = format1.parse(jsono.get("lastVisitedDate")
						.toString());
				System.out.println(date);
				if (results.containsKey(format2.format(date))) {
					int value = results.get(format2.format(date));
					results.put(format2.format(date), ++value);
				} else {
					results.put(format2.format(date), 1);
				}

			} catch (org.json.simple.parser.ParseException e) {
				System.err.println("Impossible to parse content.");
				e.printStackTrace();
			} catch (ParseException e) {
				System.err.println("Impossible to parse content.");
				e.printStackTrace();
			}
		}

		GraphView graphView = new LineGraphView(activity, "Popularité");
		String[] horizontalAxis = results.keySet().toArray(
				new String[results.size()]);
		GraphViewData[] data = new GraphViewData[results.size()];
		int i = 0;
		for (String key : results.keySet()) {
			data[i] = new GraphViewData(i, results.get(key));
			i++;
		}

		GraphViewSeriesStyle seriesStyle = new GraphViewSeriesStyle();
		seriesStyle.setValueDependentColor(new ValueDependentColor() {
			@Override
			public int get(GraphViewDataInterface data) {
				// the higher the more red
				return Color.rgb((int) (150 + ((data.getY() / 3) * 100)),
						(int) (150 - ((data.getY() / 3) * 150)),
						(int) (150 - ((data.getY() / 3) * 150)));
			}
		});

		graphView.addSeries(new GraphViewSeries("Vues", seriesStyle, data));
		graphView.setHorizontalLabels(horizontalAxis);
		graphView.setViewPort(0, 10);
		graphView.setScrollable(true);
		graphView.setScalable(true);
		graphView.setShowLegend(true);

		LinearLayout linearLayout = (LinearLayout) activity
				.findViewById(R.id.cb_ticket_graph_stats);
		linearLayout.setGravity(Gravity.CENTER_VERTICAL);
		linearLayout.addView(graphView);
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
	}

	@Override
	public void initCreateTicketActivity(Response response) {
		if (response == null || response.getStatus() == Response.BAD_REQUEST) {
			CarnetDeBordDialogFragment dialogFragment = new CarnetDeBordDialogFragment();
			Bundle args = new Bundle();
			args.putInt(CarnetDeBordDialogFragment.BOX_DIALOG_KEY,
					CarnetDeBordDialogFragment.BOX_DIALOG_DISCONNECTED);
			args.putString(CarnetDeBordDialogFragment.BOX_DIALOG_PARAMETER_URL,
					response.getUrl());
			args.putString(
					CarnetDeBordDialogFragment.BOX_DIALOG_PARAMETER_REQUESTMETHOD,
					RequestMethod.GET.toString());
			dialogFragment.setArguments(args);
			dialogFragment.show(activity.getFragmentManager(),
					"CarnetDeBordDialogFragment");
			return;
		}
		String address = null;
		try {
			JSONObject json = (JSONObject) new JSONParser().parse(response
					.getContent());
			JSONArray jsona = (JSONArray) new JSONParser().parse(json.get(
					"results").toString());
			json = (JSONObject) new JSONParser().parse(jsona.get(0).toString());
			address = json.get("formatted_address").toString();
		} catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (address != null) {
			TextView locationTextView = (TextView) activity
					.findViewById(R.id.cb_ticket_location);
			locationTextView.setText(address);
		}
	}

	@Override
	public void initCartographyTicketActivity(Response response) {
		if (response == null || response.getStatus() == Response.NO_CONTENT) {
			return;
		}

		ArrayList<Geolocation> geolocations = new ArrayList<Geolocation>();
		try {
			JSONArray jsona = (JSONArray) new JSONParser().parse(response
					.getContent());
			for (int i = 0; i < jsona.size(); ++i) {
				JSONObject json = (JSONObject) new JSONParser().parse(jsona
						.get(i).toString());
				Ticket ticket = convertFromJSON(json);
				Geolocation geolocation = new Geolocation();
				geolocation.setTicket(ticket);
				geolocation.setId(Long.valueOf(json.get("geolocationID")
						.toString()));
				geolocation.setLatitude(Double.valueOf(json.get("latitude")
						.toString()));
				geolocation.setLongitude(Double.valueOf(json.get("longitude")
						.toString()));
				geolocation.setAddress(json.get("address").toString());

				geolocations.add(geolocation);

				GoogleMap googleMap = ((MapFragment) activity
						.getFragmentManager().findFragmentById(
								R.id.cb_ticket_map_carto)).getMap();
				LatLng ticketPosition = new LatLng(geolocation.getLatitude(),
						geolocation.getLongitude());
				googleMap.setMyLocationEnabled(true);
				googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
						ticketPosition, 13));
				googleMap.addMarker(new MarkerOptions().title(
						geolocation.getTicket().getTitle()).position(
						ticketPosition));
			}
		} catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ListView listView = (ListView) activity
				.findViewById(R.id.cb_overview_tickets);
		listView.setScrollContainer(false);
		listView.setAdapter(new OverviewTicketAdapter(activity,
				R.layout.overview_tickets, geolocations));

	}
}
