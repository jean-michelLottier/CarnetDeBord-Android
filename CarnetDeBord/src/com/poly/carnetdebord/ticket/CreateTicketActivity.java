package com.poly.carnetdebord.ticket;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.poly.carnetdebord.R;
import com.poly.carnetdebord.geolocation.IGeolocationService;

public class CreateTicketActivity extends Activity {
	private ITicketService ticketService;
	private IGeolocationService geolocationService;

	public ITicketService getTicketService() {
		if(ticketService == null){
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
		setContentView(R.layout.activity_create_ticket);

//		Button butt = (Button) findViewById(R.id.button2);
//		butt.setOnClickListener(onClickListener);

//		geolocationService = new GeolocationService(this);
//		geolocationService.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_ticket, menu);
		return true;
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			if (v.getId() == R.id.button2) {
//				ticketService = getTicketService();
//				Ticket ticket = new Ticket();
//				ticket.setMessage("j'ai enregistrer mon premier billet");
//				ticket.setRelevance(0);
//				ticket.setState(false);
//				ticket.setTitle("test BDD");
//				ticket.setType("PLACE");
//				ticketService.saveTicket(ticket);
//				ArrayList<Ticket> tickets = ticketService.researchByTitle("test BDD");
//				if(tickets != null && !tickets.isEmpty()){
//					System.out.println("size : " + tickets.size());
//					for(Ticket ticket : tickets){
//						ticket.show();
//					}
//				}else {
//					System.out.println("tickets NULL OR EMPTY!");
//				}
//			}
		}
	};
}
