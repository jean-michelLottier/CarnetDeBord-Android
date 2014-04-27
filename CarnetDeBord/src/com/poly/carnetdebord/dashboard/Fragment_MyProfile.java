package com.poly.carnetdebord.dashboard;

import com.poly.carnetdebord.R;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
 * Fragment which shows the user profile
 * 
 */
public class Fragment_MyProfile extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = (View) getActivity().findViewById(
				R.layout.fragment_my_profile);
		View rootView = inflater.inflate(R.layout.fragment_my_profile,
				container, false);
		return rootView;
	}

}
