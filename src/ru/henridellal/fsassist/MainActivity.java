package ru.henridellal.fsassist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.Manifest;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends Activity
{
	//Key for intent data containing player index
	public static final String TEAM_TYPE = "ru.henridellal.fsassist.TEAM_TYPE";
	
	//MenuAdapter adapter;
	private ArrayAdapter<String> adapter;
	/** Called when the activity is first created. */
	private String[] commands;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		commands = new String[]{
			getResources().getString(R.string.main_squad),
			getResources().getString(R.string.youth_squad)
		};
		ListView commandsList = (ListView)findViewById(R.id.commandsList);
		
		//adapter = new MenuAdapter(this);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, commands);
		commandsList.setAdapter(adapter);
		commandsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			//Starts activity that shows player's skills
			public void onItemClick(AdapterView parent, View v, int position, long id) {
				//Toast.makeText(MainActivity.this, "is running", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(MainActivity.this, AssistantActivity.class);
				intent.putExtra(TEAM_TYPE, position);
				startActivity(intent);
			}
		});
		if (Build.VERSION.SDK_INT >= 23) {
			requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
			if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				finish();
			}
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
			finish();
		}
	}
}
