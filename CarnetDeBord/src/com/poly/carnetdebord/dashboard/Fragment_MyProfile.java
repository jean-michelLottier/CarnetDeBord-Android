package com.poly.carnetdebord.dashboard;

import com.poly.carnetdebord.R;
import com.poly.carnetdebord.localstorage.SessionManager;
import com.poly.carnetdebord.utilities.AppMode;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/*
 * Fragment which shows the user profile
 * 
 */
public class Fragment_MyProfile extends Fragment {
	
	private TextView welcome_title;
	private Button button_create_ticket;
	private Button button_find_tickets;
	private Button button_last_tickets;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_my_profile,
				container, false);
		welcome_title = (TextView) rootView.findViewById(R.id.profile_welcome_text);
		button_create_ticket = (Button) rootView.findViewById(R.id.profile_createticket);
		button_find_tickets = (Button) rootView.findViewById(R.id.profile_findtickets);
		button_last_tickets = (Button) rootView.findViewById(R.id.profile_lasttickets);
		SessionManager session = new SessionManager(getActivity());
		
		welcome_title.setText("Bienvenue, "+session.getFirstname()+" "+session.getName()+"!");
		button_create_ticket.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//AppMode.getInstance().setMode(AppMode.CREATE_TICKET);
				//DashBoardActivity activity = (DashBoardActivity) getActivity();
				//activity.selectItemMode(AppMode.CREATE_TICKET);
				//getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Fragment_CreateTicket()).commit();
			}
		});
		button_find_tickets.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//DashBoardActivity activity = (DashBoardActivity) getActivity();
				//activity.selectItemMode(AppMode.FIND_TICKETS);
				//AppMode.getInstance().setMode(AppMode.FIND_TICKETS);
				//getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Fragment_FindTickets()).commit();
			}
		});

		return rootView;
	}

}
