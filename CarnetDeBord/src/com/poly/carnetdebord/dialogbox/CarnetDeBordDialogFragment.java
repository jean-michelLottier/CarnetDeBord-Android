package com.poly.carnetdebord.dialogbox;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

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

	private View customView;

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
		AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
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
		default:
			break;
		}
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

}
