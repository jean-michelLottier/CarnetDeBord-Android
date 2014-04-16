package com.poly.carnetdebord.ticket;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.jjoe64.graphview.GraphView;
import com.poly.carnetdebord.R;
import com.poly.carnetdebord.geolocation.Geolocation;
import com.poly.carnetdebord.geolocation.GeolocationService;
import com.poly.carnetdebord.geolocation.IGeolocationService;
import com.poly.carnetdebord.webservice.WebService;

public class ConsultTicketActivity extends Activity {

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
			ticketService = new TicketService(this);
		}
		return ticketService;
	}

	public void setTicketService(ITicketService ticketService) {
		this.ticketService = ticketService;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consult_ticket);

		Bundle extras = getIntent().getExtras();
		// long ticketID = extras.getLong(TicketService.PARAMETER_TICKET_ID);
		// long userID = extras.getLong(TicketService.PARAMETER_USER_ID);
		long ticketID = 3;
		long userID = 1;

		ticketService = new TicketService(this);
		Geolocation geolocation = new Geolocation();
		// if (extras.getBoolean(ITicketService.KEY_IS_LOCAL_TICKET)) {
		if (false) {
			Ticket ticket = ticketService.findLocalTicketByID(ticketID);
			geolocationService = new GeolocationService(this);
			geolocation = geolocationService
					.findLocalGeolocationByTicketID(ticket.getId());
			geolocation.setTicket(ticket);
		} else {
			String urlPath = WebService.TICKET_URL_PATH + userID + "/id/"
					+ ticketID;
			new WebService(this, WebService.RequestMethod.GET).execute(urlPath);
		}

		getFragmentManager().findFragmentById(R.id.cb_ticket_map).getView()
				.setVisibility(View.GONE);
		findViewById(R.id.cb_ticket_graph_stats).setVisibility(View.GONE);

		mapButton = (ImageButton) findViewById(R.id.cb_ticket_butt_map);
		mapButton.setOnClickListener(onClickListener);
		statsButton = (ImageButton) findViewById(R.id.cb_ticket_butt_stats);
		statsButton.setOnClickListener(onClickListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.consult_ticket, menu);
		return true;
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
				v = findViewById(R.id.cb_ticket_graph_stats);
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
