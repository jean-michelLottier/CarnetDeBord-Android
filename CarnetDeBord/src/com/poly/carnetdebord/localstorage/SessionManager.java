package com.poly.carnetdebord.localstorage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.poly.carnetdebord.login.User;

public class SessionManager {
	SharedPreferences sharedPreferences;
	Editor editor;
	private Context context;
	int PRIVATE_MODE = 0;

	private static final String PREF_NAME = "UserSession";
	private static final String KEY_ID = "userID";
	private static final String KEY_CREATION_DATE = "creationDate";
	private static final String KEY_LOGIN = "login";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_NAME = "name";
	private static final String KEY_FIRSTNAME = "firstname";
	private static final String KEY_BIRTH_DATE = "birthDate";
	private static final String KEY_ACTIVATION = "activation";

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public SessionManager(Context context) {
		this.setContext(context);
		sharedPreferences = context.getSharedPreferences(PREF_NAME,
				PRIVATE_MODE);
		editor = sharedPreferences.edit();
	}

	public void initPlayerSession(User user) {
		editor.putLong(KEY_ID, user.getId());
		editor.putString(KEY_CREATION_DATE, user.getCreationDate().toString());
		editor.putString(KEY_LOGIN, user.getLogin());
		editor.putString(KEY_PASSWORD, user.getPassword());
		editor.putString(KEY_NAME, user.getName());
		editor.putString(KEY_FIRSTNAME, user.getFirstname());
		editor.putString(KEY_BIRTH_DATE, user.getBirthDate().toString());
		editor.putBoolean(KEY_ACTIVATION, user.isActivate());
		editor.commit();
	}

	public long getUserID() {
		return sharedPreferences.getLong(KEY_ID, 0);
	}

	public Date getCreationDate() {
		DateFormat format = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy",
				Locale.ENGLISH);
		String dateString = sharedPreferences
				.getString(KEY_CREATION_DATE, null);
		Date date = new Date();
		if (dateString != null && !dateString.isEmpty()) {
			try {
				date = format.parse(dateString);
			} catch (ParseException e) {
				System.err.println("Error occured during the date parse.");
			}
		}

		return date;
	}

	public String getLogin() {
		return sharedPreferences.getString(KEY_LOGIN, null);
	}

	public String getPassword() {
		return sharedPreferences.getString(KEY_PASSWORD, null);
	}

	public String getName() {
		return sharedPreferences.getString(KEY_NAME, null);
	}

	public String getFirstname() {
		return sharedPreferences.getString(KEY_FIRSTNAME, null);
	}

	public Date getBirthDate() {
		DateFormat format = new SimpleDateFormat("yyyy/mm/dd", Locale.ENGLISH);
		String dateString = sharedPreferences.getString(KEY_BIRTH_DATE, null);
		Date date = new Date();

		if (dateString != null && !dateString.isEmpty()) {
			try {
				date = format.parse(dateString);
			} catch (ParseException e) {
				System.err.println("Error occured during the date parse.");
			}
		}

		return date;
	}

	public boolean isActivate() {
		return sharedPreferences.getBoolean(KEY_ACTIVATION, false);
	}

	public User getUserDetails() {
		User user = new User();
		user.setId(getUserID());
		user.setCreationDate(getCreationDate());
		user.setLogin(getLogin());
		user.setPassword(getPassword());
		user.setName(getName());
		user.setFirstname(getFirstname());
		user.setBirthDate(getBirthDate());
		user.setActivate(isActivate());

		return user;
	}

	public void clearSession() {
		editor.clear();
		editor.commit();
	}

}
