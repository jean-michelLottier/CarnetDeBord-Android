package com.poly.carnetdebord.ticket;

import java.util.ArrayList;

public interface ITicketDAO {
	/**
	 * <p>
	 * Persist a ticket in database.
	 * </p>
	 * 
	 * @param ticket
	 */
	public void persist(Ticket ticket);

	/**
	 * <p>
	 * Find tickets by title.
	 * </p>
	 * 
	 * @param title
	 * @return
	 */
	public ArrayList<Ticket> findTicketsByTitle(String title);
}
