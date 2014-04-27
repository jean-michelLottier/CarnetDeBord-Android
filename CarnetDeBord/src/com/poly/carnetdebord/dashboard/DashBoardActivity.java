package com.poly.carnetdebord.dashboard;

import java.util.ArrayList;

import com.poly.carnetdebord.R;
import com.poly.carnetdebord.dialogbox.CarnetDeBordDialogFragment;
import com.poly.carnetdebord.utilities.AppMode;
import com.poly.carnetdebord.utilities.AppMode.FinishCode;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/*
 * 
 * FragmentActivity which uses a sliding menu to navigate between multiple Fragments
 * 
 * 
 */
public class DashBoardActivity extends FragmentActivity {

	public static enum Mode {
		PROFILE, TICKETS, ITINARY, SEARCH
	};

	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ArrayList<ItemMenu> listMenu = new ArrayList<ItemMenu>();
	private String mTitle = null;
	private AppMode appMode = AppMode.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dash_board);
		appMode.setMode(AppMode.PROFILE);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		mDrawerList = (ListView) findViewById(R.id.dash_menu);

		createMenu();
		MenuListAdapter adapter = new MenuListAdapter(this, listMenu);

		mDrawerList.setAdapter(adapter);

		Fragment_MyProfile firstFragment = new Fragment_MyProfile();
		if (mTitle == null) {
			mTitle = (String) getTitle();
		}
		// Add the fragment to the 'fragment_container' FrameLayout
		getSupportFragmentManager().beginTransaction()
				.add(R.id.content_frame, (Fragment) firstFragment).commit();

		// getActionBar().setTitle("Carnet de bord");
		// mListView.setAdapter(new
		// ArrayAdapter<String>(this,R.id.dash_menu_item,));
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getActionBar().setTitle(mTitle);
				// invalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				//mTitle = "Menu";
				//getActionBar().setTitle(mTitle);
				// invalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		mDrawerList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		ItemMenu item = listMenu.get(position);
		if (item.getFragment() != null) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, item.getFragment()).commit();
		}
		if (item.getMode() != null) {

			CarnetDeBordDialogFragment dialogFragment = new CarnetDeBordDialogFragment();
			switch (item.getMode()) {
			case AppMode.DISCONNECT:
				dialogFragment.showDisconnectDashboardDialog(this);
				break;
			case AppMode.QUIT:
				dialogFragment.showQuitAPIBoxDialog(this);
				break;
			default:
				appMode.setMode(item.getMode());
				break;
			}
		}
		mTitle = listMenu.get(position).getName();
		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mTitle);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		/*
		 * getMenuInflater().inflate(R.menu.dash_board, menu); return true;/*
		 * boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		 * menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		 */
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care) of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public void createMenu() {

		ItemMenu profil = new ItemMenu("Mon profil", new Fragment_MyProfile(),
				AppMode.PROFILE);
		profil.setState(ItemMenu.PROFILE);
		listMenu.add(profil);
		listMenu.add(new ItemMenu("Mes billets", new Fragment_MyTickets(),
				AppMode.MY_TICKETS));
		listMenu.add(new ItemMenu("Créer un billet",
				new Fragment_CreateTicket(), AppMode.CREATE_TICKET));
		listMenu.add(new ItemMenu("Recherche de billets",
				new Fragment_FindTickets(), AppMode.FIND_TICKETS));
		listMenu.add(new ItemMenu("Mon parcours", new Fragment_MyItinary(),
				AppMode.MY_ITINARY));
		listMenu.add(new ItemMenu("Déconnexion", AppMode.DISCONNECT));
		listMenu.add(new ItemMenu("Quitter l'application", AppMode.QUIT));

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == FinishCode.REQUEST_EXIT) {
			if (resultCode == FinishCode.RESULT_QUIT) {
				setResult(FinishCode.RESULT_QUIT, null);
				this.finish();

			}
		}
	}

	@Override
	public void onBackPressed() {
		CarnetDeBordDialogFragment dialogFragment = new CarnetDeBordDialogFragment();
		dialogFragment.showDisconnectDashboardDialog(this);
		return;
	}

}
