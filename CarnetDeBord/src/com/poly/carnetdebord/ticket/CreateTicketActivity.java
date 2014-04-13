package com.poly.carnetdebord.ticket;

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

public class CreateTicketActivity extends Activity {
	// Service
	private ITicketService ticketService;
	private IGeolocationService geolocationService;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_ticket);

		geolocationService = new GeolocationService(this);
		locationTextView = (TextView) findViewById(R.id.cb_ticket_location);
		new Thread(new Runnable() {

			@Override
			public void run() {
				Geolocation geolocation = null;
				int cpt = 0;
				while (geolocation == null) {
					System.out.println("************ cpt = " + cpt
							+ "************");
					if (cpt > 3) {
						geolocationService.stopProgressBar();
						return;
					}
					geolocation = geolocationService.getGeolocation();
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						return;
					}
					cpt++;
				}

				if (geolocation != null) {
					locationTextView.setText(geolocation.getFullAdress());
				}
			}
		}).start();

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
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.cb_ticket_submit:
				Ticket ticket = new Ticket();
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

				// ticketService.saveTicket(ticket);
				// ArrayList<Ticket> tickets =
				// ticketService.researchByTitle("test BDD");
				// if(tickets != null && !tickets.isEmpty()){
				// System.out.println("size : " + tickets.size());
				// for(Ticket ticket : tickets){
				// ticket.show();
				// }
				// }else {
				// System.out.println("tickets NULL OR EMPTY!");
				// }
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
				System.out.println("Event radio button selected");
				typeSelected = "EVENT";
				break;
			case R.id.cb_ticket_rb_anecdote:
				System.out.println("Anecdote radio button selected");
				break;
			default:
				System.out.println("Place radio button selected");
				typeSelected = "PLACE";
				break;
			}
		}
	};
}
