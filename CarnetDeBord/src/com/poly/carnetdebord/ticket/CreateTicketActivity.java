package com.poly.carnetdebord.ticket;

import com.poly.carnetdebord.R;
import com.poly.carnetdebord.R.layout;
import com.poly.carnetdebord.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CreateTicketActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_ticket);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_ticket, menu);
		return true;
	}

}
