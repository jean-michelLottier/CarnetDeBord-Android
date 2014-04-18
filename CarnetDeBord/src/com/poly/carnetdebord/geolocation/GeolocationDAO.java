package com.poly.carnetdebord.geolocation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.poly.carnetdebord.localstorage.DAOBase;
import com.poly.carnetdebord.localstorage.DatabaseHandler;

public class GeolocationDAO extends DAOBase implements IGeolocationDAO {

	public GeolocationDAO(Context pContext) {
		super(pContext);
		open();
	}

	@Override
	public void persist(Geolocation geolocation) {
		if (geolocation == null) {
			return;
		}

		ContentValues contentValues = new ContentValues();
		contentValues.put(DatabaseHandler.GEOLOCATION_TICKET_FK, geolocation
				.getTicket().getId());
		contentValues.put(DatabaseHandler.GEOLOCATION_LATITUDE,
				geolocation.getLatitude());
		contentValues.put(DatabaseHandler.GEOLOCATION_LONGITUDE,
				geolocation.getLongitude());
		contentValues.put(DatabaseHandler.GEOLOCATION_ADDRESS,
				geolocation.getAddress());

		sqLiteDatabase.insert(DatabaseHandler.GEOLOCATION_TABLE_NAME, null,
				contentValues);
	}

	@Override
	public Geolocation findGeolocationByTicketID(long ticketID) {
		String strQuery = "SELECT * FROM "
				+ DatabaseHandler.GEOLOCATION_TABLE_NAME + " WHERE "
				+ DatabaseHandler.GEOLOCATION_TICKET_FK + " = ?";

		Cursor cursor = sqLiteDatabase.rawQuery(strQuery,
				new String[] { String.valueOf(ticketID) });

		if (!cursor.moveToFirst()) {
			return null;
		}

		return cursorToGeolocation(cursor);
	}

	private Geolocation cursorToGeolocation(Cursor cursor) {
		if (cursor == null) {
			return null;
		}

		Geolocation geolocation = new Geolocation();
		geolocation.setId(cursor.getLong(cursor
				.getColumnIndex(DatabaseHandler.TABLE_KEY)));
		geolocation.setLatitude(cursor.getDouble(cursor
				.getColumnIndex(DatabaseHandler.GEOLOCATION_LATITUDE)));
		geolocation.setLongitude(cursor.getDouble(cursor
				.getColumnIndex(DatabaseHandler.GEOLOCATION_LONGITUDE)));
		geolocation.setAddress(cursor.getString(cursor
				.getColumnIndex(DatabaseHandler.GEOLOCATION_ADDRESS)));

		return geolocation;
	}
}
