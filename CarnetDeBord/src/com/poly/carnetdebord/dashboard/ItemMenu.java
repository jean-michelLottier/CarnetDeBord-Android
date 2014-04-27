package com.poly.carnetdebord.dashboard;

import android.support.v4.app.Fragment;

/*
 * 
 * A class which represents an item of the dashboard-menu
 * Each item is made of:
 * - A name
 * - A fragment which represents the layout page which will appear if you click on the item
 * - A state to select the layout of the item itself in the list
 * - A mode which is the mode the application will be if you click on the item
 * 
 */
public class ItemMenu {

	public static final int PROFILE = 0;
	public static final int TITLE = 1;
	public static final int ITEM = 2;

	private String name;

	private Fragment fragment;
	private int state = ITEM;
	private Integer mode = null;

	public ItemMenu(String n, Fragment f, int s, Integer m) {
		name = n;
		fragment = f;
		state = s;
		setMode(m);
	}

	public ItemMenu(String n, Fragment f, Integer m) {
		name = n;
		fragment = f;
		setMode(m);
	}

	public ItemMenu(String n) {
		name = n;
		fragment = null;
	}

	public ItemMenu(String n, Integer m) {
		name = n;
		fragment = null;
		setMode(m);
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

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

}
