package com.poly.carnetdebord.geolocation;

import android.app.Activity;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

import com.poly.carnetdebord.R;

public class CartographyTicketsActivity extends Activity {

	private IGeolocationService geolocationService;
	private LocationManager lm;

	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cartography_tickets);

		geolocationService = new GeolocationService(this);
		lm = geolocationService.start();
		if (lm != null) {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 35000, 0,
					(LocationListener) geolocationService);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cartography_tickets, menu);
		return true;
	}
}
