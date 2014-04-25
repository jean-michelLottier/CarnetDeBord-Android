package com.poly.carnetdebord.login;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;

import com.poly.carnetdebord.dialogbox.CarnetDeBordDialogFragment;
import com.poly.carnetdebord.localstorage.SessionManager;
import com.poly.carnetdebord.ticket.DashBoardActivity;
import com.poly.carnetdebord.webservice.Response;
import com.poly.carnetdebord.webservice.WebService;

public class LoginService implements ILoginService {

	private static LoginService localService = null;
	private OnClickListener onClickRegisterSucces = new OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			activity.finish();
		}
	};
	private OnClickListener onClickRegisterFail = new OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.dismiss();
		}
	};
	
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
			SessionManager session = new SessionManager(activity);
			System.out.println("retour serveur: "+response.getContent());
			activity.startActivity(new Intent(activity.getApplicationContext(),DashBoardActivity.class));
		}
	}

	@Override
	public void finishRegister(Response response) {
		// TODO Auto-generated method stub
		if(response.getStatus() == Response.BAD_REQUEST)
		{
			CarnetDeBordDialogFragment dialog = new CarnetDeBordDialogFragment();
			dialog.showSimpleDialog("Enregistrement échoué", "Problème de connexion au serveur", onClickRegisterFail);
			
			System.out.println("**** invalide ****");
		}
		else
		{
			System.out.println("**** valide ****");
			CarnetDeBordDialogFragment dialog = new CarnetDeBordDialogFragment();
			dialog.showSimpleDialog("Enregistrement terminé", "Un email de confirmation vous a été envoyé, veuillez le consulter afin de pouvoir vous authentifier", onClickRegisterSucces);
		}
	}

}
