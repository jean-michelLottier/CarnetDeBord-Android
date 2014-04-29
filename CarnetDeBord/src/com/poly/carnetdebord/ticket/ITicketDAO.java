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
	 * Update title, message, annex info, type, or state of ticket.
	 * </p>
	 * 
	 * @param ticket
	 */
	public void updateTicket(Ticket ticket);

	/**
	 * <p>
	 * Find tickets by title.
	 * </p>
	 * 
	 * @param title
	 * @return
	 */
	public ArrayList<Ticket> findTicketsByTitle(String title);

	/**
	 * <p>
	 * Find ticket by id.
	 * </p>
	 * 
	 * @param id
	 * @return Ticket otherwise null.
	 */
	public Ticket findTicketByID(long id);
	
	/**
	 * <p>
	 * Find tickets by user id.
	 * </p>
	 * 
	 * @param id
	 * @return Tickets otherwise an empty array.
	 */
	public ArrayList<Ticket> findTicketsByUserID(long id);
}
