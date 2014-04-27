package com.poly.carnetdebord.dashboard;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.jjoe64.graphview.GraphView;
import com.poly.carnetdebord.R;
import com.poly.carnetdebord.geolocation.Geolocation;
import com.poly.carnetdebord.geolocation.GeolocationService;
import com.poly.carnetdebord.geolocation.IGeolocationService;
import com.poly.carnetdebord.ticket.ITicketService;
import com.poly.carnetdebord.ticket.Ticket;
import com.poly.carnetdebord.ticket.TicketService;
import com.poly.carnetdebord.webservice.WebService;

/*
 * 
 * Fragment used to consult a ticket
 * 
 */
public class Fragment_ConsultTicket extends Fragment {

	private TextView locationTextView;
	private TextView titleTextView;
	private TextView contentTextView;
	private ImageButton mapButton;
	private GoogleMap googleMap;
	private ImageButton statsButton;
	private GraphView statsGraphView;

	private ITicketService ticketService;
	private IGeolocationService geolocationService;

	public ITicketService getTicketService() {
		if (ticketService == null) {
			ticketService = new TicketService(getActivity());
		}
		return ticketService;
	}

	public void setTicketService(ITicketService ticketService) {
		this.ticketService = ticketService;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_consult_ticket,
				container, false);
		long ticketID = 4;
		long userID = 25;

		ticketService = new TicketService(getActivity());
		Geolocation geolocation = new Geolocation();
		// if (extras.getBoolean(ITicketService.KEY_IS_LOCAL_TICKET)) {
		if (false) {
			Ticket ticket = ticketService.findLocalTicketByID(ticketID);
			geolocationService = new GeolocationService(getActivity());
			geolocation = geolocationService
					.findLocalGeolocationByTicketID(ticket.getId());
			geolocation.setTicket(ticket);
		} else {
			String urlPath = WebService.TICKET_URL_PATH + userID + "/id/"
					+ ticketID;
			new WebService(getActivity(), WebService.RequestMethod.GET)
					.execute(urlPath);
		}

		getFragmentManager().findFragmentById(R.id.cb_ticket_map).getView()
				.setVisibility(View.GONE);
		getActivity().findViewById(R.id.cb_ticket_graph_stats).setVisibility(
				View.GONE);

		mapButton = (ImageButton) getActivity().findViewById(
				R.id.cb_ticket_butt_map);
		mapButton.setOnClickListener(onClickListener);
		statsButton = (ImageButton) getActivity().findViewById(
				R.id.cb_ticket_butt_stats);
		statsButton.setOnClickListener(onClickListener);
		return rootView;
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.cb_ticket_butt_map:
				v = getFragmentManager().findFragmentById(R.id.cb_ticket_map)
						.getView();
				if (v.getVisibility() == View.VISIBLE) {
					v.setVisibility(View.GONE);
				} else {
					v.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.cb_ticket_butt_stats:
				v = getActivity().findViewById(R.id.cb_ticket_graph_stats);
				if (v.getVisibility() == View.VISIBLE) {
					v.setVisibility(View.GONE);
				} else {
					v.setVisibility(View.VISIBLE);
				}
			default:
				break;
			}
		}
	};

}
