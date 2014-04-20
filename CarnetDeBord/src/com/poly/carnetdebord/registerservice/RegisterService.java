package com.poly.carnetdebord.registerservice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.poly.carnetdebord.localstorage.SessionManager;
import com.poly.carnetdebord.login.RegisterActivity;
import com.poly.carnetdebord.webservice.Response;
import com.poly.carnetdebord.webservice.WebService;

public class RegisterService implements IRegisterService {

	private static RegisterService localService = null;
	
	//Classe interne chargée de créer l'instance de service (safe contre le muti-threading)
	private static class LocalServiceHolder {
		
		private static final RegisterService INSTANCE = new RegisterService();
	}
	
	
	private static Activity activity;
	
	
	
	private RegisterService()
	{
	}
	
	public static RegisterService getInstance(Activity a)
	{

		
		if (localService==null)
		{
			localService = LocalServiceHolder.INSTANCE;
		}
		activity = a;
		
		return localService;
	}

	@Override
	public void initSession(Response response) {
		// TODO Auto-generated method stub
		if(response.getStatus() == Response.BAD_REQUEST)
		{
			System.out.println("**** invalide ****");
		}
		else
		{
			System.out.println("**** valide ****");
			activity.finish();
		}
	}

}
