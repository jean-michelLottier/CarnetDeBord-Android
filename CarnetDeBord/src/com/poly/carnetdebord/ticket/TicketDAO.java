package com.poly.carnetdebord.ticket;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.poly.carnetdebord.localstorage.DAOBase;

public class TicketDAO extends DAOBase implements ITicketDAO {

	public static final String TICKET_ID = "id";
	public static final String TICKET_USER_FK = "UserFK";
	public static final String TICKET_POSTED_DATE = "PostedDate";
	public static final String TICKET_TITLE = "Title";
	public static final String TICKET_MESSAGE = "Message";
	public static final String TICKET_TYPE = "Type";
	public static final String TICKET_ANNEX_INFO = "AnnexInfo";
	public static final String TICKET_STATE = "State";
	public static final String TICKET_RELEVANCE = "Relevance";

	public static final String TICKET_TABLE_NAME = "Ticket";

	public TicketDAO(Context pContext) {
		super(pContext);
		open();
	}

	@Override
	public void persist(Ticket ticket) {
		if (ticket == null) {
			return;
		}
		ContentValues contentValues = new ContentValues();
		contentValues.put(TICKET_USER_FK, ticket.getUserID());
		contentValues.put(TICKET_POSTED_DATE,
				String.valueOf(ticket.getPostedDate()));
		contentValues.put(TICKET_TITLE, ticket.getTitle());
		contentValues.put(TICKET_MESSAGE, ticket.getMessage());
		contentValues.put(TICKET_TYPE, ticket.getType());
		if (ticket.getAnnexInfo() != null && !ticket.getAnnexInfo().isEmpty()) {
			contentValues.put(TICKET_ANNEX_INFO, ticket.getAnnexInfo());
		}
		contentValues.put(TICKET_STATE, ticket.getState());
		contentValues.put(TICKET_RELEVANCE, ticket.getRelevance());
		sqLiteDatabase.insert(TICKET_TABLE_NAME, null, contentValues);
	}

	@Override
	public void updateTicket(Ticket ticket) {
		if (ticket == null) {
			return;
		}

		ContentValues contentValues = new ContentValues();
		contentValues.put(TICKET_TITLE, ticket.getTitle());
		contentValues.put(TICKET_MESSAGE, ticket.getMessage());
		contentValues.put(TICKET_STATE, ticket.getState());
		contentValues.put(TICKET_TYPE, ticket.getType());
		contentValues.put(TICKET_ANNEX_INFO, ticket.getAnnexInfo());

		sqLiteDatabase.update(TICKET_TABLE_NAME, contentValues, TICKET_ID + "="
				+ ticket.getId(), null);
	}

	@Override
	public ArrayList<Ticket> findTicketsByTitle(String title) {
		String strQuery = "SELECT * FROM " + TICKET_TABLE_NAME + " WHERE "
				+ TICKET_TITLE + "=?";
		Cursor cursor = sqLiteDatabase.rawQuery(strQuery,
				new String[] { String.valueOf(title) });

		ArrayList<Ticket> tickets = new ArrayList<Ticket>();
		if (cursor == null || cursor.getCount() == 0) {
			return tickets;
		}

		while (cursor.moveToNext()) {
			Ticket ticket = cursorToTicket(cursor);
			tickets.add(ticket);
		}
		return tickets;
	}

	@Override
	public Ticket findTicketByID(long id) {
		if (id < 0) {
			return null;
		}

		String strQuery = "SELECT * FROM " + TICKET_TABLE_NAME + " WHERE "
				+ TICKET_ID + " = ?";

		Cursor cursor = sqLiteDatabase.rawQuery(strQuery,
				new String[] { String.valueOf(id) });

		if (!cursor.moveToFirst()) {
			return null;
		}

		return cursorToTicket(cursor);
	}
	
	@Override
	public ArrayList<Ticket> findTicketsByUserID(long id) {
		if (id < 0) {
			return null;
		}

		String strQuery = "SELECT * FROM " + TICKET_TABLE_NAME + " WHERE "
				+ TICKET_USER_FK + " = ?";

		Cursor cursor = sqLiteDatabase.rawQuery(strQuery,
				new String[] { String.valueOf(id) });

		ArrayList<Ticket> tickets = new ArrayList<Ticket>();
		if (cursor == null || cursor.getCount() == 0) {
			return tickets;
		}

		while (cursor.moveToNext()) {
			Ticket ticket = cursorToTicket(cursor);
			tickets.add(ticket);
		}
		return tickets;
	}
	

	private Ticket cursorToTicket(Cursor cursor) {
		Ticket ticket = new Ticket();
		ticket.setId(cursor.getInt(cursor.getColumnIndex(TICKET_ID)));
		ticket.setUserID(cursor.getInt(cursor.getColumnIndex(TICKET_USER_FK)));
		DateFormat format = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy",
				Locale.ENGLISH);
		String strPostedDate = cursor.getString(cursor
				.getColumnIndex(TICKET_POSTED_DATE));
		try {
			ticket.setPostedDate(format.parse(strPostedDate));
		} catch (ParseException e) {
			System.err
					.println("Impossible to parse string date stored in database for ticket : "
							+ ticket.getId());
		}
		ticket.setTitle(cursor.getString(cursor.getColumnIndex(TICKET_TITLE)));
		ticket.setMessage(cursor.getString(cursor
				.getColumnIndex(TICKET_MESSAGE)));
		ticket.setType(cursor.getString(cursor.getColumnIndex(TICKET_TYPE)));
		String annex = cursor.getString(cursor
				.getColumnIndex(TICKET_ANNEX_INFO));
		if (annex != null && !annex.isEmpty()) {
			ticket.setAnnexInfo(annex);
		}
		int state = cursor.getInt(cursor.getColumnIndex(TICKET_STATE));
		if (state == 0) {
			ticket.setState(false);
		} else {
			ticket.setState(true);
		}
		ticket.setRelevance(cursor.getLong(cursor
				.getColumnIndex(TICKET_RELEVANCE)));

		return ticket;
	}

}
