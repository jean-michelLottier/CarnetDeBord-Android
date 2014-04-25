package com.poly.carnetdebord.ticket;

import android.app.Fragment;

public class ItemMenu {

	public static final int PROFILE = 0;
	public static final int TITLE = 1;
	public static final int ITEM = 2;
	
	private String name;

	private Fragment fragment;
	private int state = ITEM;

	public ItemMenu(String n, Fragment f, int s) {
		name = n;
		fragment = f;
		state = s;
	}

	
	public ItemMenu(String n, Fragment f) {
		name = n;
		fragment = f;
	}
	
	public ItemMenu(String n) {
		name = n;
		fragment = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Fragment getFragment() {
		return fragment;
	}

	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
	}

	public int getState() {
		return state;
	}


	public void setState(int state) {
		this.state = state;
	}

}
