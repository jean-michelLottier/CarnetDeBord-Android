package com.poly.carnetdebord.dashboard;

import com.poly.carnetdebord.R;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
 * 
 * Fragment which shows the tickets created by the user
 * 
 */
public class Fragment_MyTickets extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_my_tickets,
				container, false);
		return rootView;
	}

}
