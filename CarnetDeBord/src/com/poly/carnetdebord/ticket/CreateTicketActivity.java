package com.poly.carnetdebord.ticket;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.poly.carnetdebord.R;
import com.poly.carnetdebord.geolocation.Geolocation;
import com.poly.carnetdebord.geolocation.GeolocationService;
import com.poly.carnetdebord.geolocation.IGeolocationService;
import com.poly.carnetdebord.localstorage.SessionManager;

public class CreateTicketActivity extends Activity {
	// Service
	private ITicketService ticketService;
	private IGeolocationService geolocationService;
	private SessionManager session;

	// Components
	private TextView locationTextView;
	private EditText titleEditText;
	private EditText contentEditText;
	private RadioGroup typeRadioGroup;
	private EditText additionalInfEditText;
	private CheckBox stateCheckBox;
	private Button submitButton;
	private String typeSelected = "PLACE";
	private boolean stateSelected;

	private static final String WARNING_EMPTY_FIELD_MESSAGE = "Vous devez remplir tous les champs obligatoires";

	public ITicketService getTicketService() {
		if (ticketService == null) {
			ticketService = new TicketService(this);
		}
		return ticketService;
	}

	public void setTicketService(ITicketService ticketService) {
		this.ticketService = ticketService;
	}

	private SessionManager initSession() {
		session = new SessionManager(this);
		return session;
	}

	private void quitSession() {
		session.clearSession();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_ticket);

		geolocationService = new GeolocationService(this);
		if (geolocationService.isGPSActivated()) {
			Geolocation geolocation = geolocationService.getGeolocation();
			locationTextView = (TextView) findViewById(R.id.cb_ticket_location);
			locationTextView.setText(geolocation.getFullAdress());
		}

		typeRadioGroup = (RadioGroup) findViewById(R.id.cb_ticket_rbg);
		typeRadioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
		additionalInfEditText = (EditText) findViewById(R.id.cb_ticket_more_information_edit);
		stateCheckBox = (CheckBox) findViewById(R.id.cb_ticket_state);
		stateCheckBox.setOnClickListener(onClickListener);

		submitButton = (Button) findViewById(R.id.cb_ticket_submit);
		submitButton.setOnClickListener(onClickListener);
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
			switch (v.getId()) {
			case R.id.cb_ticket_submit:
				titleEditText = (EditText) findViewById(R.id.cb_ticket_title);
				contentEditText = (EditText) findViewById(R.id.cb_ticket_content);
				if (titleEditText.getText() == null
						|| titleEditText.getText().toString().isEmpty()
						|| contentEditText.getText() == null
						|| contentEditText.getText().toString().isEmpty()) {
					Toast.makeText(CreateTicketActivity.this,
							WARNING_EMPTY_FIELD_MESSAGE, Toast.LENGTH_SHORT)
							.show();
					return;
				}

				Ticket ticket = new Ticket();
				ticket.setTitle(titleEditText.getText().toString().trim());
				ticket.setMessage(contentEditText.getText().toString().trim());
				ticket.setType(typeSelected);
				additionalInfEditText = (EditText) findViewById(R.id.cb_ticket_more_information_edit);
				if (additionalInfEditText.getText() != null
						&& !additionalInfEditText.getText().toString()
								.isEmpty()) {
					ticket.setAnnexInfo(additionalInfEditText.getText()
							.toString());
				}
				ticket.setRelevance(0);
				ticket.setState(stateSelected);

				ticket.show();

				// todo remplacer le userid par le userid qui est dans le
				// sessionManager
				// session = initSession();
				// ticket.setUserID(session.getUserID());
				ticket.setUserID(1);

				ticketService = getTicketService();
				ticketService.saveLocalTicket(ticket);

				Geolocation geolocation = geolocationService.getGeolocation();
				geolocation.setAddress(geolocation.getFullAdress());
				geolocation.setTicket(ticket);
				geolocationService.saveLocalGeolocation(geolocation);
				geolocationService.saveRemoteGeolocation(geolocation);

				// ////////TEST//////////////
				ArrayList<Ticket> tickets = ticketService
						.researchByTitle(ticket.getTitle());
				if (tickets != null && !tickets.isEmpty()) {
					System.out.println("size : " + tickets.size());
					for (Ticket current : tickets) {
						current.show();
					}
				} else {
					System.out.println("tickets NULL OR EMPTY!");
				}
				// ////////END TEST////////////
				break;
			case R.id.cb_ticket_state:
				System.out.println("Check box selected");
				stateSelected = !stateSelected;
			default:
				break;
			}
		}
	};

	OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.cb_ticket_rb_event:
				typeSelected = "EVENT";
				break;
			case R.id.cb_ticket_rb_anecdote:
				typeSelected = "ANECDOTE";
				break;
			default:
				typeSelected = "PLACE";
				break;
			}
		}
	};
}
