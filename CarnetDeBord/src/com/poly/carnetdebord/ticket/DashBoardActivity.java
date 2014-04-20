package com.poly.carnetdebord.ticket;

import java.util.ArrayList;

import com.poly.carnetdebord.R;
import com.poly.carnetdebord.R.layout;
import com.poly.carnetdebord.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.SparseArray;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class DashBoardActivity extends Activity {


	private ExpandableListView mListView;
	private ArrayList<String[]> menu = new ArrayList<String[]>();
	private DrawerLayout mDrawerLayout;
	SparseArray<GroupItem_Menu> groups = new SparseArray<GroupItem_Menu>();
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dash_board);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

	    createMenu();
	    ExpandableListView mlistView = (ExpandableListView) findViewById(R.id.dash_menu);
	    ExpandableListAdapter adapter = new ExpandableListAdapter(this,
	        groups);
	    
	    mlistView.setAdapter(adapter);
	    
	    Fragment_carnetdebord firstFragment = new Fragment_carnetdebord();

        // Add the fragment to the 'fragment_container' FrameLayout
        getFragmentManager().beginTransaction().add(R.id.content_frame, (Fragment)firstFragment).commit();
		
		//mListView.setAdapter(new ArrayAdapter<String>(this,R.id.dash_menu_item,));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dash_board, menu);
		return true;
	}
	
	public void createMenu()
	{
		menu.add(getResources().getStringArray(R.array.menu_carnetdebord));
		menu.add(getResources().getStringArray(R.array.menu_profil));
		menu.add(getResources().getStringArray(R.array.menu_billets));
		
		for(int i=0; i<menu.size(); i++)
		{
			String[] array = menu.get(i);
			GroupItem_Menu group = new GroupItem_Menu(array[0]);
			for(int j=1; j<array.length;j++)
			{
				group.children.add(array[j]);
			}
			groups.append(i, group);
		}
	}
	
}
