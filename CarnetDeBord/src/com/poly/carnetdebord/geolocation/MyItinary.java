package com.poly.carnetdebord.geolocation;

import com.poly.carnetdebord.R;
import com.poly.carnetdebord.R.layout;
import com.poly.carnetdebord.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MyItinary extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_itinary);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_itinary, menu);
		return true;
	}

}
