package com.poly.carnetdebord.ticket;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.os.AsyncTask;

import com.poly.carnetdebord.webservice.IWebService;
import com.poly.carnetdebord.webservice.Response;
import com.poly.carnetdebord.webservice.WebService;
import com.poly.carnetdebord.webservice.WebService.RequestMethod;

public class TicketService implements ITicketService {

	private static final String URL_PATH = "http://10.0.2.2:8080/CarnetDeBord/webresources/ticket/userid/id/ticketid";

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
}
