package com.poly.carnetdebord.login;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.carnetdebord.dashboard.DashBoardActivity;
import com.poly.carnetdebord.dialogbox.CarnetDeBordDialogFragment;
import com.poly.carnetdebord.localstorage.SessionManager;
import com.poly.carnetdebord.utilities.AppMode;
import com.poly.carnetdebord.utilities.User;
import com.poly.carnetdebord.utilities.AppMode.FinishCode;
import com.poly.carnetdebord.webservice.Response;
import com.poly.carnetdebord.webservice.WebService;

/*
 * Class which deals with the server response about the requests sended from the RegisterActivity and the LoginActivity
 * 
 */
public class LoginService implements ILoginService {

	private static LoginService localService = null;
	private OnClickListener onClickRegisterSucces = new OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.cancel();
			activity.finish();
		}
	};
	private OnClickListener onClickRegisterFail = new OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.cancel();
		}
	};
	private OnClickListener onClickConnectionFail = new OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.cancel();
		}
	};

	// Classe interne charg�e de cr�er l'instance de service (safe contre le
	// muti-threading)
	private static class LocalServiceHolder {

		private static final LoginService INSTANCE = new LoginService();
	}

	private static Activity activity;

	private LoginService() {
	}

	public static LoginService getInstance(Activity a) {

		if (localService == null) {
			localService = LocalServiceHolder.INSTANCE;
		}
		activity = a;

		return localService;
	}

	@Override
	public void initSession(Response response) {
		// TODO Auto-generated method stub

		switch (response.getStatus()) {
		case Response.BAD_REQUEST:
			CarnetDeBordDialogFragment.showSimpleDialog(activity,
					"Connexion �chou�", "Probl�me de connexion au serveur",
					onClickConnectionFail);
			break;
		case Response.UNSUPPORTED_MEDIA_TYPE:
			CarnetDeBordDialogFragment.showSimpleDialog(activity,
					"Connexion �chou�", "Probl�me de connexion au serveur",
					onClickConnectionFail);
			break;
		case Response.INTERNAL_SERVER_ERROR:
			CarnetDeBordDialogFragment.showSimpleDialog(activity,
					"Connexion �chou�", "Probl�me au niveau du serveur",
					onClickConnectionFail);
			break;
		case Response.NO_CONTENT:
			CarnetDeBordDialogFragment.showSimpleDialog(activity,
					"Connexion �chou�",
					"Probl�me de renvoi de donn�es du serveur",
					onClickConnectionFail);
			break;
		case Response.UNAUTHORIZED:
			CarnetDeBordDialogFragment.showSimpleDialog(activity,
					"Connexion �chou�", "Probl�me d'identification du compte",
					onClickConnectionFail);
			((LoginActivity) activity).resetForm();
			break;
		case Response.OK:
			System.out.println("**** valide ****");
			SessionManager session = new SessionManager(activity);
			ObjectMapper mapper = new ObjectMapper();
			User user;
			try {
				user = mapper.readValue(response.getContent(), User.class);
				session.initPlayerSession(user);
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			activity.startActivityForResult(
					new Intent(activity.getApplicationContext(),
							DashBoardActivity.class), FinishCode.REQUEST_EXIT);
			AppMode.getInstance().setMode(AppMode.PROFILE);
			break;
		default:
			break;
		}
	}

	@Override
	public void finishRegister(Response response) {
		// TODO Auto-generated method stub
		CarnetDeBordDialogFragment dialog = new CarnetDeBordDialogFragment();
		switch (response.getStatus()) {
		case Response.BAD_REQUEST:
			CarnetDeBordDialogFragment.showSimpleDialog(activity,
					"Enregistrement �chou�",
					"Probl�me de connexion au serveur", onClickRegisterFail);
			break;
		case Response.INTERNAL_SERVER_ERROR:
			CarnetDeBordDialogFragment.showSimpleDialog(activity,
					"Enregistrement �chou�", "Probl�me au niveau du serveur",
					onClickRegisterFail);
			break;
		case Response.NO_CONTENT:
			CarnetDeBordDialogFragment.showSimpleDialog(activity,
					"Enregistrement �chou�",
					"Probl�me de renvoi de donn�es du serveur",
					onClickRegisterFail);
			break;
		case Response.UNAUTHORIZED:
			CarnetDeBordDialogFragment.showSimpleDialog(activity,
					"Enregistrement �chou�",
					"Probl�me d'identification du compte", onClickRegisterFail);
			break;
		case Response.CONFLICT:
			CarnetDeBordDialogFragment
					.showSimpleDialog(
							activity,
							"Enregistrement �chou�",
							"Cet email est d�ja rattach� � un compte, veuillez en s�lectionner un autre",
							onClickRegisterFail);
			((RegisterActivity) activity).resetForm();
			break;
		case Response.OK:
			System.out.println("**** valide ****");
			CarnetDeBordDialogFragment
					.showSimpleDialog(
							activity,
							"Enregistrement termin�",
							"Un email de confirmation vous a �t� envoy�, veuillez le consulter afin de pouvoir vous authentifier",
							onClickRegisterSucces);
			AppMode.getInstance().setMode(AppMode.LOGIN);
			break;
		default:
			break;
		}
	}

}
