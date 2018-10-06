package ru.henridellal.fsassist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class ComparatorActivity extends Activity
{
	//Key for intent data containing player index
	public static final String FIRST_PLAYER_ID = "ru.henridellal.fsassist.FIRST_PLAYER_ID";
	public static final String SECOND_PLAYER_ID = "ru.henridellal.fsassist.SECOND_PLAYER_ID";
	//MenuAdapter adapter;
	private AttrAdapter adapter;
	/** Called when the activity is first created. */
	//String[] attr;
	
	private int[] ids;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comparator);
		ids = new int[2];
		Intent intent = getIntent();
		ids[0] = intent.getIntExtra(FIRST_PLAYER_ID, 0);
		ids[1] = intent.getIntExtra(SECOND_PLAYER_ID, 1);
		//attr = getResources().getStringArray(R.array.sorting_params);
		ListView attrList = (ListView)findViewById(R.id.attr_list);
		
		//adapter = new MenuAdapter(this);
		adapter = new AttrAdapter(this, ids[0], ids[1]);
		attrList.setAdapter(adapter);
	}
	/*public MenuAdapter getAdapter() {
		return adapter;
	}*/
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.assistant_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			/*case R.id.menu_item_about:
				startActivity(new Intent(MainActivity.this, AboutActivity.class));
				finish();
				return true;
			default:
				return false;
		}
	}*/
	private class AttrAdapter extends BaseAdapter {
		/*
			Custom adapter for main menu
		*/
		//int[] drawableIds = int[]{};
		private Context context;
		private String[] attr;
		private Player firstPlayer, secondPlayer;
		private int lightGreenColor, blackColor;
		public AttrAdapter(Context context, int firstPlayerIndex, int secondPlayerIndex) {
			super();
			attr = getResources().getStringArray(R.array.sorting_parameters);
			this.context = context;
			Activity activity = ((Activity)this.context);
			firstPlayer = Container.getTeam().getPlayers().get(firstPlayerIndex);
			((TextView)activity.findViewById(R.id.player_name_1)).setText(firstPlayer.getName());
			((TextView)activity.findViewById(R.id.player_surname_1)).setText(firstPlayer.getSurname());
			secondPlayer = Container.getTeam().getPlayers().get(secondPlayerIndex);
			((TextView)activity.findViewById(R.id.player_name_2)).setText(secondPlayer.getName());
			((TextView)activity.findViewById(R.id.player_surname_2)).setText(secondPlayer.getSurname());
			lightGreenColor = context.getResources().getColor(R.color.light_green_500);
			blackColor = context.getResources().getColor(R.color.black);
		}
		@Override
		public boolean isEnabled(int position) {
			return true;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v;
			/*  if there is no view created for
				list item(player) then we create
				a new instance of View
			*/
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.attr_list_item, parent, false);
				//Toast.makeText(AssistantActivity.this, "is running", Toast.LENGTH_LONG).show();
			} else {
				v = convertView;
			}
			int pos = position+1;
			TextView firstAttribute = ((TextView) v.findViewById(R.id.attr_1));
			firstAttribute.setText(firstPlayer.getDetailedData(context).get(pos));
			TextView secondAttribute = ((TextView) v.findViewById(R.id.attr_2));
			secondAttribute.setText(secondPlayer.getDetailedData(context).get(pos));
			int firstColor, secondColor;
			int switchInt;
			if ((pos >= 8) && (pos < 23)) {
				switchInt = firstPlayer.compareAttr(secondPlayer, pos-8);
			} else if (pos >= 23) {
				switchInt = firstPlayer.compareRate(secondPlayer, pos-23);
			} else {
				switchInt = 0;
			}
				switch (switchInt) {
					case -1:
						firstColor = lightGreenColor;
						secondColor = blackColor;
						break;
					case 1:
						firstColor = blackColor;
						secondColor = lightGreenColor;
						break;
					default:
						firstColor = blackColor;
						secondColor = blackColor;
				}
			firstAttribute.setTextColor(firstColor);
			secondAttribute.setTextColor(secondColor);
			((TextView) v.findViewById(R.id.attr_name)).setText(attr[pos]);
			
			return v;
		}
		@Override
		public int getCount() {
			return attr.length-1;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}
	
		@Override
		public long getItemId(int position) {
			return 0;
		}
	}
}
