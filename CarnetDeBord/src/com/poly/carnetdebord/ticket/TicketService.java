package com.poly.carnetdebord.ticket;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.poly.carnetdebord.webservice.IWebService;
import com.poly.carnetdebord.webservice.Response;
import com.poly.carnetdebord.webservice.WebService;
import com.poly.carnetdebord.webservice.WebService.RequestMethod;

public class TicketService implements ITicketService {
	private static final String URL_PATH = "http://serveur10.lerb.polymtl.ca:8080/CarnetDeBord/webresources/ticket/";

	private static final String PARAMETER_TICKET_ID = "ticketid";
	private static final String PARAMETER_USER_ID = "userid";
	private static final String PARAMETER_TITLE = "title";
	private static final String PARAMETER_MESSAGE = "message";
	private static final String PARAMETER_TYPE = "type";
	private static final String PARAMETER_ANNEX_INFO = "annexinfo";
	private static final String PARAMETER_STATE = "state";

	private final Context context;

	private IWebService webService;

	private ITicketDAO ticketDAO;

	public TicketService(Context context) {
		this.context = context;
	}

	@Override
	public ArrayList<Ticket> getUserTickets() {
		System.out
				.println("***********TicketService getUserTickets***********");
		String urlPath = URL_PATH.replace("userid", "1").replace("ticketid",
				"1");
		System.out.println("urlpath : " + urlPath);
		// webService = new WebService();
		AsyncTask<String, Response, Response> response = new WebService(
				RequestMethod.GET).execute(urlPath);
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
	public void saveTicket(Ticket ticket) {
		if (ticket == null) {
			return;
		}

		AsyncTask<String, Response, Response> response = new WebService(
				RequestMethod.POST, convertToJSON(ticket).toString())
				.execute(URL_PATH);

		try {
			if (response.get().getStatus() == Response.BAD_REQUEST) {
				System.err.println("Request no valid!");
				return;
			} else if (response.get().getStatus() == Response.INTERNAL_SERVER_ERROR) {
				System.err.println("Problem with server");
				return;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		ticket.setPostedDate(Calendar.getInstance().getTime());
		ticketDAO = new TicketDAO(context);
		ticketDAO.persist(ticket);
	}

	@Override
	public ArrayList<Ticket> researchByTitle(String title) {
		if (title == null || title.isEmpty()) {
			return null;
		}

		ticketDAO = new TicketDAO(context);
		return ticketDAO.findTicketsByTitle(title);
	}

	private JSONObject convertToJSON(Ticket ticket) {
		if (ticket == null) {
			return null;
		}

		JSONObject json = new JSONObject();

		try {
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
		} catch (JSONException e) {
			System.err.println("Impossible to create Json object");
			return null;
		}
		return json;
	}
}
