package com.poly.carnetdebord.dashboard;

import android.app.Activity;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.poly.carnetdebord.R;
import com.poly.carnetdebord.geolocation.GeolocationService;
import com.poly.carnetdebord.geolocation.IGeolocationService;

/*
 * A fragment which show a map to find tickets in the user's area
 * 
 */
public class Fragment_FindTickets extends Fragment {

	private IGeolocationService geolocationService;
	private LocationManager lm;

	private ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_find_tickets,
				container, false);
		geolocationService = new GeolocationService(getActivity());
		lm = geolocationService.start();
		if (lm != null) {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 35000, 0,
					(LocationListener) geolocationService);
		}
		return rootView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		try {
			Fragment fragment = (getFragmentManager()
					.findFragmentById(R.id.cb_ticket_map_carto));
			getActivity().getSupportFragmentManager().beginTransaction()
					.remove(fragment).commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
