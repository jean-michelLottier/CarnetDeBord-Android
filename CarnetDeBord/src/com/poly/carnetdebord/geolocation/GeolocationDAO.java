package com.poly.carnetdebord.geolocation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.poly.carnetdebord.localstorage.DAOBase;

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
		contentValues.put(databaseHandler.GEOLOCATION_TICKET_FK, geolocation
				.getTicket().getId());
		contentValues.put(databaseHandler.GEOLOCATION_LATITUDE,
				geolocation.getLatitude());
		contentValues.put(databaseHandler.GEOLOCATION_LONGITUDE,
				geolocation.getLongitude());
		contentValues.put(databaseHandler.GEOLOCATION_ADDRESS,
				geolocation.getAddress());

		sqLiteDatabase.insert(databaseHandler.GEOLOCATION_TABLE_NAME, null,
				contentValues);
	}

	@Override
	public Geolocation findGeolocationByTicketID(long ticketID) {
		String strQuery = "SELECT * FROM "
				+ databaseHandler.GEOLOCATION_TABLE_NAME + " WHERE "
				+ databaseHandler.GEOLOCATION_TICKET_FK + " = ?";

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
				.getColumnIndex(databaseHandler.TABLE_KEY)));
		geolocation.setLatitude(cursor.getDouble(cursor
				.getColumnIndex(databaseHandler.GEOLOCATION_LATITUDE)));
		geolocation.setLongitude(cursor.getDouble(cursor
				.getColumnIndex(databaseHandler.GEOLOCATION_LONGITUDE)));
		geolocation.setAddress(cursor.getString(cursor
				.getColumnIndex(databaseHandler.GEOLOCATION_ADDRESS)));

		return geolocation;
	}
}
