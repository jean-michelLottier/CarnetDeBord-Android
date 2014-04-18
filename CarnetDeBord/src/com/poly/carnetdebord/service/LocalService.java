package com.poly.carnetdebord.service;

import android.content.Context;
import android.content.Intent;

import com.poly.carnetdebord.localstorage.SessionManager;
import com.poly.carnetdebord.login.RegisterActivity;

public class LocalService implements ILocalService {

	private static LocalService localService = null;
	
	//Classe interne chargée de créer l'instance de service (safe contre le muti-threading)
	private static class LocalServiceHolder {
		
		private static final LocalService INSTANCE = new LocalService();
	}
	
	
	private static Context context;
	
	
	
	private LocalService()
	{
	}
	
	public static LocalService getInstance(Context c)
	{

		
		if (localService==null)
		{
			localService = LocalServiceHolder.INSTANCE;
		}
		context = c;
		
		return localService;
	}
	
	@Override
	public void goRegister() {
		// TODO Auto-generated method stub
		context.startActivity(new Intent(context,RegisterActivity.class));
	}

	@Override
	public SessionManager initSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void quitSession() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goQuitApp() {
		// TODO Auto-generated method stub
		
	}

}
