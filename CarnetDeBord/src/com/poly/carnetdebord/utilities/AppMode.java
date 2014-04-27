package com.poly.carnetdebord.utilities;

import android.app.Activity;
import android.content.Context;

/*
 * 
 * A Singleton-Class used to store the current mode of the application
 * 
 */
public class AppMode {

	public static final int REGISTER = 0;
	public static final int LOGIN = 1;
	public static final int PROFILE = 2;
	public static final int MY_TICKETS = 3;
	public static final int FIND_TICKETS = 4;
	public static final int DISCONNECT = 5;
	public static final int CONSULT_TICKET = 6;
	public static final int CREATE_TICKET = 7;
	public static final int MY_ITINARY = 8;
	public static final int QUIT = 9;

	/*
	 * 
	 * A Subclass which stores codes to use for when an activity finishes
	 */
	public class FinishCode {
		public static final int REQUEST_EXIT = 2;
		public static final int RESULT_QUIT = 3;
	};

	private static AppMode appmode = null;
	private static Integer mode = null;

	private static class AppModeHolder {

		private static final AppMode INSTANCE = new AppMode();
	}

	public void setMode(int mode) {
		AppMode.mode = mode;
	}

	public Integer getMode() {
		return AppMode.mode;
	}

	public boolean isMode(int mode) {
		return AppMode.mode == mode;
	}

	public static AppMode getInstance() {

		if (appmode == null) {
			appmode = AppModeHolder.INSTANCE;
		}

		return appmode;
	}

}
