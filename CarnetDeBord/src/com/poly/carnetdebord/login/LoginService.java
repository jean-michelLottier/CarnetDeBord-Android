package com.poly.carnetdebord.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.poly.carnetdebord.localstorage.SessionManager;
import com.poly.carnetdebord.ticket.DashBoardActivity;
import com.poly.carnetdebord.webservice.Response;
import com.poly.carnetdebord.webservice.WebService;

public class LoginService implements ILoginService {

	private static LoginService localService = null;
	
	//Classe interne chargée de créer l'instance de service (safe contre le muti-threading)
	private static class LocalServiceHolder {
		
		private static final LoginService INSTANCE = new LoginService();
	}
	
	
	private static Activity activity;
	
	
	
	private LoginService()
	{
	}
	
	public static LoginService getInstance(Activity a)
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
			activity.startActivity(new Intent(activity.getApplicationContext(),DashBoardActivity.class));
		}
	}

}
