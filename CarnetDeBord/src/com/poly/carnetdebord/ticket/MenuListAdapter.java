package com.poly.carnetdebord.ticket;

import java.util.ArrayList;

import com.poly.carnetdebord.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MenuListAdapter extends BaseAdapter {

	private ArrayList<ItemMenu> list;
	  public LayoutInflater inflater;
	  public Activity activity;

	  public MenuListAdapter(Activity act, ArrayList<ItemMenu> groups) {
	    activity = act;
	    this.list = groups;
	    inflater = act.getLayoutInflater();
	  }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ItemMenu item = (ItemMenu) getItem(position);

		TextView itemName = null;
		if (convertView == null)
		{
			
			switch(item.getState())
			{
				case ItemMenu.TITLE:
					convertView = inflater.inflate(R.layout.menu_group_item, null);
					break;
				case ItemMenu.PROFILE:
					convertView = inflater.inflate(R.layout.menu_profile_item, null);
					break;
				default:
					convertView = inflater.inflate(R.layout.menu_item, null);
					break;
			}
		}
		switch(item.getState())
		{
			case ItemMenu.TITLE:
				itemName = (TextView) convertView.findViewById(R.id.menu_groupView);
				break;
			case ItemMenu.PROFILE:
				itemName = (TextView) convertView.findViewById(R.id.menu_profileItemView);
				break;
			default:
				itemName = (TextView) convertView.findViewById(R.id.menu_itemView);
				break;
		}
		itemName.setText(item.getName());
		return convertView;
	}
}
