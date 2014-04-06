package com.poly.carnetdebord.localstorage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * <p>
 * Permit to access CarnetDeBord database.
 * </p>
 * 
 * @author jean-michel
 * 
 */
public abstract class DAOBase {

	protected final static int VERSION = 1;
	protected final static String NAME = "database.db";

	protected SQLiteDatabase sqLiteDatabase = null;
	protected DatabaseHandler databaseHandler = null;

	public DAOBase(Context pContext) {
		this.databaseHandler = new DatabaseHandler(pContext, NAME, null,
				VERSION);
	}

	public SQLiteDatabase open() {
		sqLiteDatabase = databaseHandler.getWritableDatabase();
		return sqLiteDatabase;
	}

	public void close() {
		sqLiteDatabase.close();
	}

	public SQLiteDatabase getDb() {
		return sqLiteDatabase;
	}
}
