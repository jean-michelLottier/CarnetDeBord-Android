package com.poly.carnetdebord.ticket;

import java.util.ArrayList;

public interface ITicketService {
	public ArrayList<Ticket> getUserTickets();

	public void saveTicket(Ticket ticket);

	public ArrayList<Ticket> researchByTitle(String title);
}
