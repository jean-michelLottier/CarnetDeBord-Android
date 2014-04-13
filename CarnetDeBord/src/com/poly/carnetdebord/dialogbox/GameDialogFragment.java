package com.poly.carnetdebord.dialogbox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

/**
 * <p>
 * This class permit to manage all dialog boxes of "Carnet De bord" API.
 * </p>
 * 
 * @author jean-michel
 * 
 */
public class GameDialogFragment extends DialogFragment {
	public static final String BOX_DIALOG_KEY = "box_dialog_key";

	// Box dialog value

	// Box dialog parameters

	// Box dialog contents

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
		case 1:
			break;
		default:
			break;
		}
		return alertDialog;
	}

}
