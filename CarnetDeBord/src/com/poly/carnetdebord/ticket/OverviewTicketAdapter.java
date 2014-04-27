package com.poly.carnetdebord.ticket;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.poly.carnetdebord.R;
import com.poly.carnetdebord.dashboard.Fragment_ConsultTicket;
import com.poly.carnetdebord.geolocation.Geolocation;
import com.poly.carnetdebord.localstorage.SessionManager;

public class OverviewTicketAdapter extends ArrayAdapter<Geolocation> {

	private final Context context;
	private final List<Geolocation> geolocations;

	public OverviewTicketAdapter(Context context, int textViewResourceId,
			List<Geolocation> geolocations) {
		super(context, textViewResourceId, geolocations);

		this.context = context;
		this.geolocations = geolocations;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		Geolocation geolocation = geolocations.get(position);

		View rowView = inflater.inflate(R.layout.overview_tickets, parent,
				false);
		TextView locationTextView = (TextView) rowView
				.findViewById(R.id.cb_ticket_location);
		locationTextView.setText(geolocation.getAddress());
		TextView titleTextView = (TextView) rowView
				.findViewById(R.id.cb_ticket_title);
		titleTextView.setText(geolocation.getTicket().getTitle());
		TextView contentTextView = (TextView) rowView
				.findViewById(R.id.cb_ticket_content);
		String snippet;
		if (geolocation.getTicket().getMessage().length() > 35) {
			snippet = geolocation.getTicket().getMessage().substring(0, 35)
					+ "...";
		} else {
			snippet = geolocation.getTicket().getMessage();
		}
		contentTextView.setText(snippet);

		rowView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Geolocation geolocation = geolocations.get(position);
				SessionManager session = new SessionManager(context);

				Intent intent = new Intent(v.getContext(),
						Fragment_ConsultTicket.class);
				intent.putExtra(TicketService.PARAMETER_TICKET_ID, geolocation
						.getTicket().getId());
				// intent.putExtra(TicketService.PARAMETER_USER_ID,
				// session.getUserID());
				intent.putExtra(TicketService.KEY_IS_LOCAL_TICKET, true);
				context.startActivity(intent);
			}
		});

		return rowView;
	}
}
