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
import android.view.View;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class AssistantActivity extends Activity
{
	//Key for intent data containing player index
	public static final String PLAYER_ID = "ru.henridellal.fsassist.PLAYER_ID";
	
	private String filePath;
	private File playerFile;
	private PlayerAdapter adapter;
	private int teamType;
	/** Called when the activity is first created. */
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assistant);
		//Container.setTeam(this, "Players.xls");
		Intent intent = getIntent();
		teamType = intent.getIntExtra(MainActivity.TEAM_TYPE, 0);
		if (teamType == 0) {
			filePath = PreferenceManager.getDefaultSharedPreferences(this).getString("file_path_main", "");
		} else {
			filePath = PreferenceManager.getDefaultSharedPreferences(this).getString("file_path_youth", "");
		}
		playerFile = new File(filePath);
		if (!playerFile.isFile()) {
			Intent i = new Intent(AssistantActivity.this, FileChooserActivity.class);
			i.putExtra(MainActivity.TEAM_TYPE, teamType);
			startActivity(i);
			finish();
			//Toast.makeText(this, playerFile.getPath(), Toast.LENGTH_LONG).show();
		} else {
			Container.setTeam(this, playerFile, teamType);
			ListView playersList = (ListView)findViewById(R.id.playersList);
			adapter = new PlayerAdapter(this);
			playersList.setAdapter(adapter);
			//adapter.sort();
			playersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				//Starts activity that shows player's skills
				public void onItemClick(AdapterView parent, View v, int position, long id) {
					Intent intent = new Intent(AssistantActivity.this, PlayerActivity.class);
					intent.putExtra(PLAYER_ID, position);
					startActivity(intent);
				}
			});
		}
	}
	public PlayerAdapter getAdapter() {
		return adapter;
	}
	public void openSortDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String[] commands = getResources().getStringArray(R.array.sorting_parameters);
		/*String[] commands = new String[]{"Name", "Age", "Value", "Wage", "Condition", "Potential",
				"Rating", "Skill level", "Keep", "Mark", "Position", "Head", "Steal", "First touch",
				"Pass", "Dribble", "Cross", "Finish", "Agility", "Strength", "Fitness", "Speed", "Creativity", 
				"GK rate", "SW rate", "DLR rate", "WBLR rate", "DC rate", "DMC rate", "MLR rate",
				"AMLR rate", "AMC rate", "MC rate", "ST rate"};*/
		ArrayAdapter<String> sortAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, commands);
		builder.setAdapter(sortAdapter, 
			new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface di, int which) {
					int mode = 0;
					if (which  <= 7) {
						mode = which;
					} else if ((which > 7) && (which <= 22)) {
						mode = which + 56;
					} else if (which > 22) {
						mode = which + 105;
					}
					Team.setMode(mode);
					//PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("sort_mode", mode).commit();
					getAdapter().sort();
				}
			}
		);
		builder.setTitle("Sort by");
		builder.setCancelable(true);
		builder.create().show();
	}
	
	public void openCompareDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		PlayerAdapter plAdapter = new PlayerAdapter(this);
		builder.setAdapter(plAdapter, 
			new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface di, int which) {
					openCompareDialog(which);
				}
			}
		);
		builder.setTitle("Choose first player to compare");
		builder.setCancelable(true);
		builder.create().show();
	}
	public void openCompareDialog(final int firstPlayerId) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		PlayerAdapter plAdapter = new PlayerAdapter(this);
		builder.setAdapter(plAdapter, 
			new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface di, int which) {
					Intent i = new Intent(AssistantActivity.this, ComparatorActivity.class);
					i.putExtra(ComparatorActivity.FIRST_PLAYER_ID, firstPlayerId);
					i.putExtra(ComparatorActivity.SECOND_PLAYER_ID, which);
					startActivity(i);
					finish();
				}
			}
		);
		builder.setTitle("Choose second player to compare");
		builder.setCancelable(true);
		builder.create().show();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.assistant_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_item_sort:
				openSortDialog();
				return true;
			case R.id.menu_item_load_file:
				Intent i = new Intent(AssistantActivity.this, FileChooserActivity.class);
				i.putExtra(MainActivity.TEAM_TYPE, teamType);
				startActivity(i);
				finish();
				return true;
			case R.id.menu_item_compare:
				openCompareDialog();
				return true;
			default:
				return false;
		}
	}
	private class PlayerAdapter extends BaseAdapter {
		/*
			Custom adapter for players list
		*/
		private Context context;
		private ArrayList<Player> players;
		public void sort() {
			Container.getTeam().sortPlayers();
			this.players = Container.getTeam().getPlayers();
			notifyDataSetChanged();
		}
		public PlayerAdapter(Context context) {
			super();
			this.context = context;
			sort();
			//this.players = Container.getTeam().getPlayers();
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
				v = inflater.inflate(R.layout.simple_list_item, parent, false);
				//Toast.makeText(AssistantActivity.this, "is running", Toast.LENGTH_LONG).show();
			} else {
				v = convertView;
			}
			((TextView) v.findViewById(R.id.playerName)).setText(players.get(position).getFullName());
			((TextView) v.findViewById(R.id.additionalInfo)).setText(players.get(position).getBestRates(3));
			return v;
		}
		@Override
		public int getCount() {
			return players.size();
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
