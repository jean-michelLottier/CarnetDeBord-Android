package com.poly.carnetdebord.dashboard;

import java.util.ArrayList;

import com.poly.carnetdebord.R;
import com.poly.carnetdebord.localstorage.SessionManager;
import com.poly.carnetdebord.ticket.Ticket;
import com.poly.carnetdebord.ticket.TicketDAO;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 
 * Fragment which shows the tickets created by the user
 * 
 */
public class Fragment_MyTickets extends Fragment {
	private GridView gridView;
	private ArrayList<Ticket> myTickets;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_my_tickets,
				container, false);

		SessionManager session = new SessionManager(getActivity());
		TicketDAO ticketDAO = new TicketDAO(getActivity()); // Recuperation des
															// tickets de l'user
															// par son ID via le
															// DAO

		myTickets = ticketDAO.findTicketsByUserID(session.getUserID());
		gridView = (GridView) getActivity().findViewById(R.id.tickets_gridView);

		if (!myTickets.isEmpty()) {
			gridView.setAdapter(new GridTicketAdapter(getActivity(), myTickets));
		}

		// Listener pour gérer le cas où l'on clique sur un Billet afin d'y
		// accéder
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				Ticket ticket = myTickets.get(position);
				Fragment consultTicket = new Fragment_ConsultTicket();

				// TODO
				// Ne pas oublier d'ajouter les arguments du Bundle nécessaire au demmarage du
				// fragment ConsultTicket

				getActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.content_frame, consultTicket).commit();
			}
		});

		return rootView;
	}

}
