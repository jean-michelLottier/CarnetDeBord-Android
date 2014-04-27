package com.poly.carnetdebord.dashboard;

import com.poly.carnetdebord.R;
import com.poly.carnetdebord.R.layout;
import com.poly.carnetdebord.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

/*
 * Fragment which marks the different places where the user created a ticket to form his itinary
 * 
 */

public class Fragment_MyItinary extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_my_itinary,
				container, false);
		return rootView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		try {
			Fragment fragment = (getFragmentManager()
					.findFragmentById(R.id.cb_ticket_map_myItinary));
			getActivity().getSupportFragmentManager().beginTransaction()
					.remove(fragment).commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
