package com.poly.carnetdebord.ticket;

import java.util.ArrayList;

import com.poly.carnetdebord.geolocation.Geolocation;
import com.poly.carnetdebord.webservice.Response;

public interface ITicketService {
	public static final String KEY_IS_LOCAL_TICKET = "isLocalTicket";

	public void saveLocalTicket(Ticket ticket);

	public ArrayList<Ticket> researchByTitle(String title);

	public Ticket findLocalTicketByID(long id);

	public void initConsultTicketActivity(Response response);

	public void initConsultTicketActivity(Geolocation geolocation);

	public void initCreateTicketActivity(Response response);

	public void initCartographyTicketActivity(Response response);
}
