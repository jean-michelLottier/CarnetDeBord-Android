package com.poly.carnetdebord.service;

import com.poly.carnetdebord.localstorage.SessionManager;

public interface ILocalService {
	
	public void goRegister();

	public SessionManager initSession();

	public void quitSession();

	public void goQuitApp();

}
