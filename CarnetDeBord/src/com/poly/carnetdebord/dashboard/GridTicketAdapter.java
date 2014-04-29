package com.poly.carnetdebord.dashboard;

import java.util.ArrayList;

import com.poly.carnetdebord.R;
import com.poly.carnetdebord.ticket.Ticket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridTicketAdapter extends BaseAdapter {
		private Context context;
		private final ArrayList<Ticket> tickets;
	 
		public GridTicketAdapter(Context context, ArrayList<Ticket> tickets) {
			this.context = context;
			this.tickets = tickets;
		}
	 
		public View getView(int position, View convertView, ViewGroup parent) {
	 
			LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
			View gridView;
	 
			if (convertView == null) {
	 
				gridView = new View(context);
	 
				// get layout from mobile.xml
				gridView = inflater.inflate(R.layout.grid_item, null);
	 
				// set value into textview
				TextView textView = (TextView) gridView
						.findViewById(R.id.grid_item_label);
				textView.setText(tickets.get(position).getTitle());
	 
			} else {
				gridView = (View) convertView;
			}
	 
			return gridView;
		}
	 
		@Override
		public int getCount() {
			return tickets.size();
		}
	 
		@Override
		public Ticket getItem(int position) {
			return tickets.get(position);
		}
	 
		@Override
		public long getItemId(int position) {
			return position;
		}
	 
	}
