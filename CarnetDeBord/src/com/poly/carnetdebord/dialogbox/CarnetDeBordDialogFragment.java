package com.poly.carnetdebord.dialogbox;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.poly.carnetdebord.dashboard.Fragment_CreateTicket;
import com.poly.carnetdebord.localstorage.SessionManager;
import com.poly.carnetdebord.login.LoginActivity;
import com.poly.carnetdebord.utilities.AppMode;
import com.poly.carnetdebord.utilities.AppMode.FinishCode;
import com.poly.carnetdebord.webservice.WebService;
import com.poly.carnetdebord.webservice.WebService.RequestMethod;

/**
 * <p>
 * This class permit to manage all dialog boxes of "Carnet De bord" API.
 * </p>
 * 
 * @author jean-michel
 * 
 */
public class CarnetDeBordDialogFragment extends DialogFragment {
	public static final String BOX_DIALOG_KEY = "box_dialog_key";

	// Box dialog value
	public static final int BOX_DIALOG_DISCONNECTED = 0;
	public static final int BOX_DIALOG_GPS_UNABLED = 1;
	public static final int BOX_DIALOG_QUIT_API = 2;
	public static final int BOX_DIALOG_SIMPLE = 3;
	public static final int BOX_DIALOG_DISCONNECT_DASHBOARD = 4;

	// Box dialog parameters
	public static final String BOX_DIALOG_PARAMETER_URL = "urlPath";
	public static final String BOX_DIALOG_PARAMETER_REQUESTMETHOD = "requestMethod";
	public static final String BOX_DIALOG_PARAMETER_TITLE = "title";
	public static final String BOX_DIALOG_PARAMETER_MSG = "msg";

	// Box dialog contents
	private static final String BOX_DIALOG_BUTTON_RETRY = "Réessayer";
	private static final String BOX_DIALOG_BUTTON_QUIT_API = "Quitter l'application";
	private static final String BOX_DIALOG_BUTTON_ACTIVATE_GPS = "Activer le GPS";
	private static final String BOX_DIALOG_DISCONNECTED_TITLE = "Information";
	private static final String BOX_DIALOG_DISCONNECTED_MESSAGE = "Vous n'êtes plus connecté au réseau internet.";
	private static final String BOX_DIALOG_GPS_UNABLED_MESSAGE = "Votre service se localisation n'est pas activé. \nVeuillez l'activer pour continuer.";
	private static final String BOX_DIALOG_QUIT_API_MESSAGE = "Êtes-vous sur de vouloir quitter?\n (Niveau de batterie consommé : ";
	private static final String BOX_DIALOG_BUTTON_YES = "Oui";
	private static final String BOX_DIALOG_BUTTON_NO = "Non";
	private static final String BOX_DIALOG_DISCONNECT_PROMPT_TITLE = "Déconnexion";
	private static final String BOX_DIALOG_DISCONNECT_PROMPT = "Vous êtes sur le point de vous déconnecter, voulez-vous continuer?";

	

	/**
	 * <p>
	 * Constructor of GameDialogFragment class
	 * </p>
	 */
	@Override
	public Dialog onCreateDialog(Bundle saveInstanceState) {
		AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
		.create();
		
		return alertDialog;
	}

	public void showDisconnectDashboardDialog(final Activity activity) {
		AlertDialog alertDialog = new AlertDialog.Builder(activity)
		.create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.setTitle(BOX_DIALOG_DISCONNECT_PROMPT_TITLE);
		alertDialog.setMessage(BOX_DIALOG_DISCONNECT_PROMPT);

		alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
				BOX_DIALOG_BUTTON_NO, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
				BOX_DIALOG_BUTTON_YES, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						AppMode.getInstance().setMode(AppMode.LOGIN);
						activity.finish();
					}
				});

		alertDialog.show();
	}

	public static void showSimpleDialog(final Activity activity, String title, String message, OnClickListener onclick)
	{
		AlertDialog a = new AlertDialog.Builder(activity).create();
		a.setCanceledOnTouchOutside(false);
		a.setTitle(title);
		a.setMessage(message);
		a.setButton(DialogInterface.BUTTON_NEUTRAL,"Continuer",onclick);
		a.show();
	}
	
	public void showBoxDialogIntent(final Activity activity,Class<?> c) {
		AlertDialog alertDialog = new AlertDialog.Builder(activity)
	.create();
		alertDialog.setTitle(BOX_DIALOG_DISCONNECTED_TITLE);
		alertDialog.setMessage(BOX_DIALOG_GPS_UNABLED_MESSAGE);
		alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,
				BOX_DIALOG_BUTTON_ACTIVATE_GPS,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						activity.startActivity(new Intent(
								android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
						dialog.dismiss();
					}
				});

		alertDialog.show();
	}
	
	public void showGPSUnabledBoxDialog(final Activity activity) {
		AlertDialog alertDialog = new AlertDialog.Builder(activity)
		.create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.setTitle(BOX_DIALOG_DISCONNECTED_TITLE);
		alertDialog.setMessage(BOX_DIALOG_GPS_UNABLED_MESSAGE);
		alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,
				BOX_DIALOG_BUTTON_ACTIVATE_GPS,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						activity.startActivity(new Intent(
								android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
						dialog.dismiss();
					}
				});

		alertDialog.show();
	}

	public void showDisconnectedBoxDialog(final Activity activity,final String urlPath, final RequestMethod requestMethod) {
		AlertDialog alertDialog = new AlertDialog.Builder(activity)
		.create();

		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.setTitle(BOX_DIALOG_DISCONNECTED_TITLE);
		alertDialog.setMessage(BOX_DIALOG_DISCONNECTED_MESSAGE);
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
				BOX_DIALOG_BUTTON_RETRY, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						new WebService(activity, requestMethod)
								.execute(urlPath);
						dialog.dismiss();
					}
				});

		alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
				BOX_DIALOG_BUTTON_QUIT_API,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if(!AppMode.getInstance().isMode(AppMode.LOGIN))
						{
							activity.setResult(FinishCode.RESULT_QUIT,null);
						}
						activity.finish();
					}
				});

		alertDialog.show();
	}

	public void showQuitAPIBoxDialog(final Activity activity) {
		AlertDialog alertDialog = new AlertDialog.Builder(activity)
		.create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.setTitle(BOX_DIALOG_DISCONNECTED_TITLE);
		boolean sessionOpened = true;
		IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = null;
		try{
		batteryStatus = activity.registerReceiver(null, iFilter);
		}catch(NullPointerException e)
		{
			sessionOpened =false;
		}
		if(sessionOpened)
		{
			int currentBattery = batteryStatus.getIntExtra(
					BatteryManager.EXTRA_LEVEL, -1);
			SessionManager session = new SessionManager(activity);
			int energyConsumption = session.getBatteryLevel() - currentBattery;
			alertDialog.setMessage(BOX_DIALOG_QUIT_API_MESSAGE + energyConsumption
					+ "%)");
		}
		else
		{
			alertDialog.setMessage(BOX_DIALOG_QUIT_API_MESSAGE + "0%)");
		}

		alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
				BOX_DIALOG_BUTTON_NO, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
				BOX_DIALOG_BUTTON_YES, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(!AppMode.getInstance().isMode(AppMode.LOGIN))
						{
							activity.setResult(FinishCode.RESULT_QUIT,null);
						}
						SessionManager session = new SessionManager(activity);
						session.clearSession();
						activity.finish();
					}
				});

		alertDialog.show();
	}

}
