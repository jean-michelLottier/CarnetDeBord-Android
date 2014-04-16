package com.poly.carnetdebord.localstorage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * <p>
 * Definition of CarnetDeBord's tables
 * </p>
 * 
 * @author jean-michel
 * 
 */
public class DatabaseHandler extends SQLiteOpenHelper {
	public static final String TICKET_TABLE_NAME = "Ticket";
	public static final String GEOLOCATION_TABLE_NAME = "Geolocation";

	public static final String TABLE_KEY = "id";

	public static final String TICKET_USER_FK = "UserFK";
	public static final String TICKET_POSTED_DATE = "PostedDate";
	public static final String TICKET_TITLE = "Title";
	public static final String TICKET_MESSAGE = "Message";
	public static final String TICKET_TYPE = "Type";
	public static final String TICKET_ANNEX_INFO = "AnnexInfo";
	public static final String TICKET_STATE = "State";
	public static final String TICKET_RELEVANCE = "Relevance";

	public static final String GEOLOCATION_TICKET_FK = "TicketFK";
	public static final String GEOLOCATION_LONGITUDE = "Longitude";
	public static final String GEOLOCATION_LATITUDE = "Latitude";
	public static final String GEOLOCATION_ADDRESS = "Address";

	public static final String TICKET_TABLE_CREATE = "CREATE TABLE "
			+ TICKET_TABLE_NAME + " (" + TABLE_KEY
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + TICKET_USER_FK
			+ " INTEGER, " + TICKET_POSTED_DATE + " TIMESTAMP, " + TICKET_TITLE
			+ " VARCHAR(255), " + TICKET_MESSAGE + " LONGTEXT, " + TICKET_TYPE
			+ " ENUM, " + TICKET_ANNEX_INFO + " TEXT, " + TICKET_STATE
			+ " TINYTEXT, " + TICKET_RELEVANCE + " INTEGER);";
	public static final String TICKET_TABLE_DROP = "DROP TABLE IF EXISTS "
			+ TICKET_TABLE_NAME + ";";

	public static final String GEOLOCATION_TABLE_CREATE = "CREATE TABLE "
			+ GEOLOCATION_TABLE_NAME + " (" + TABLE_KEY
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + GEOLOCATION_TICKET_FK
			+ " INTEGER, " + GEOLOCATION_LONGITUDE + " FLOAT, "
			+ GEOLOCATION_LATITUDE + " FLOAT, " + GEOLOCATION_ADDRESS
			+ " VARCHAR(255), " + " FOREIGN KEY(" + GEOLOCATION_TICKET_FK
			+ ") REFERENCES " + TICKET_TABLE_NAME + "(" + TABLE_KEY
			+ ") ON DELETE CASCADE);";
	public static final String GEOLOCATION_TABLE_DROP = "DROP TABLE IF EXISTS "
			+ GEOLOCATION_TABLE_NAME + ";";

	public DatabaseHandler(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TICKET_TABLE_CREATE);
		db.execSQL(GEOLOCATION_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TICKET_TABLE_DROP);
		db.execSQL("DROP TABLE IF EXISTS " + GEOLOCATION_TABLE_DROP);
		onCreate(db);
	}
}
