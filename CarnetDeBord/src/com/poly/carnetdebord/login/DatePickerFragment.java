package com.poly.carnetdebord.login;

import java.util.Calendar;

import com.poly.carnetdebord.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

public class DatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {

	private TextView dateView;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		dateView = (TextView) getActivity()
				.findViewById(R.id.register_birthday);
		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// TODO Auto-generated method stub
		dateView.setText(year + "/" + monthOfYear + "/" + dayOfMonth);

	}

}