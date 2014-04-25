package com.poly.carnetdebord.dialogbox;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.poly.carnetdebord.localstorage.SessionManager;
import com.poly.carnetdebord.login.LoginActivity;
import com.poly.carnetdebord.ticket.CreateTicketActivity;
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

	// Box dialog parameters
	public static final String BOX_DIALOG_PARAMETER_URL = "urlPath";
	public static final String BOX_DIALOG_PARAMETER_REQUESTMETHOD = "requestMethod";

	// Box dialog contents
	private static final String BOX_DIALOG_BUTTON_RETRY = "Réessayer";
	private static final String BOX_DIALOG_BUTTON_QUIT_API = "Quitter l'application";
	private static final String BOX_DIALOG_BUTTON_ACTIVATE_GPS = "Acitver le GPS";
	private static final String BOX_DIALOG_DISCONNECTED_TITLE = "Information";
	private static final String BOX_DIALOG_DISCONNECTED_MESSAGE = "Vous n'êtes plus connecté au réseau internet.";
	private static final String BOX_DIALOG_GPS_UNABLED_MESSAGE = "Votre service se localisation n'est pas activé. \nVeuillez l'activer pour continuer.";
	private static final String BOX_DIALOG_QUIT_API_MESSAGE = "Êtes-vous sûr de vouloir quitter?\n (Niveau de batterie consommé : ";
	private static final String BOX_DIALOG_BUTTON_YES = "Oui";
	private static final String BOX_DIALOG_BUTTON_NO = "Non";

	private View customView;
	private AlertDialog alertDialog;
	private final OnKeyListener backListener = new OnKeyListener() {

		@Override
		public boolean onKey(DialogInterface dialog, int keycode, KeyEvent event) {
			// TODO Auto-generated method stub
			if (keycode == KeyEvent.KEYCODE_BACK) {
				return true;
			}
			return false;
		}

	};

	/**
	 * <p>
	 * Constructor of GameDialogFragment class
	 * </p>
	 */
	@Override
	public Dialog onCreateDialog(Bundle saveInstanceState) {
		alertDialog = new AlertDialog.Builder(getActivity())
				.create();

		int boxID = getArguments().getInt(BOX_DIALOG_KEY);
		switch (boxID) {
		case BOX_DIALOG_DISCONNECTED:
			String urlPath = getArguments().getString(BOX_DIALOG_PARAMETER_URL);
			String request = getArguments().getString(
					BOX_DIALOG_PARAMETER_REQUESTMETHOD);
			RequestMethod requestMethod;
			if (request.equals(RequestMethod.PUT)) {
				requestMethod = RequestMethod.PUT;
			} else if (request.equals(RequestMethod.POST)) {
				requestMethod = RequestMethod.POST;
			} else {
				requestMethod = RequestMethod.GET;
			}
			alertDialog = initDisconnectedBoxDialog(alertDialog, urlPath,
					requestMethod);
			break;
		case BOX_DIALOG_GPS_UNABLED:
			alertDialog = initGPSUnabledBoxDialog(alertDialog);
			break;
		case BOX_DIALOG_QUIT_API:
			alertDialog = initQuitAPIBoxDialog(alertDialog);
			break;
		default:
			break;
		}
		return alertDialog;
	}
	
	public void showSimpleDialog(String title, String message, OnClickListener onclick)
	{
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,"Continuer",onclick);
		alertDialog.show();
	}
	
	private AlertDialog initBoxDialogIntent(AlertDialog alertDialog, Class<?> c) {
		alertDialog.setTitle(BOX_DIALOG_DISCONNECTED_TITLE);
		alertDialog.setMessage(BOX_DIALOG_GPS_UNABLED_MESSAGE);
		alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,
				BOX_DIALOG_BUTTON_ACTIVATE_GPS,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Activity activity = ((AlertDialog) dialog)
								.getOwnerActivity();
						activity.startActivity(new Intent(
								android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
						dismiss();
					}
				});

		return alertDialog;
	}
	
	private AlertDialog initGPSUnabledBoxDialog(AlertDialog alertDialog) {
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.setTitle(BOX_DIALOG_DISCONNECTED_TITLE);
		alertDialog.setMessage(BOX_DIALOG_GPS_UNABLED_MESSAGE);
		alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,
				BOX_DIALOG_BUTTON_ACTIVATE_GPS,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Activity activity = ((AlertDialog) dialog)
								.getOwnerActivity();
						activity.startActivity(new Intent(
								android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
						dismiss();
					}
				});

		return alertDialog;
	}

	private AlertDialog initDisconnectedBoxDialog(AlertDialog alertDialog,
			final String urlPath, final RequestMethod requestMethod) {
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.setTitle(BOX_DIALOG_DISCONNECTED_TITLE);
		alertDialog.setMessage(BOX_DIALOG_DISCONNECTED_MESSAGE);
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
				BOX_DIALOG_BUTTON_RETRY, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						CreateTicketActivity activity = (CreateTicketActivity) ((AlertDialog) dialog)
								.getOwnerActivity();
						new WebService(activity, requestMethod)
								.execute(urlPath);
						dismiss();
					}
				});

		alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
				BOX_DIALOG_BUTTON_QUIT_API,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						CreateTicketActivity activity = (CreateTicketActivity) ((AlertDialog) dialog)
								.getOwnerActivity();

						Intent intent = new Intent(activity,
								LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						dismiss();
					}
				});

		return alertDialog;
	}

	private AlertDialog initQuitAPIBoxDialog(AlertDialog alertDialog) {
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.setTitle(BOX_DIALOG_DISCONNECTED_TITLE);

		Activity activity = alertDialog.getOwnerActivity();
		IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = activity.registerReceiver(null, iFilter);
		int currentBattery = batteryStatus.getIntExtra(
				BatteryManager.EXTRA_LEVEL, -1);
		SessionManager session = new SessionManager(activity);
		int energyConsumption = session.getBatteryLevel() - currentBattery;
		alertDialog.setMessage(BOX_DIALOG_QUIT_API_MESSAGE + energyConsumption
				+ "%)");

		alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
				BOX_DIALOG_BUTTON_NO, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dismiss();
					}
				});

		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
				BOX_DIALOG_BUTTON_YES, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Activity activity = ((AlertDialog) dialog)
								.getOwnerActivity();
						Intent intent = new Intent(activity,
								LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						activity.finish();
					}
				});

		return alertDialog;
	}

}
