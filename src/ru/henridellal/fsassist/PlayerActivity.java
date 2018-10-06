package ru.henridellal.fsassist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayerActivity extends Activity {
	private Player player;
	private int playerId, teamSize;
	
	//All ids of layout
	private final int[] ids = new int[]{R.id.playerPageTitle, R.id.age, 
		R.id.value, R.id.wage, R.id.condition, 
		R.id.potential, R.id.rating, R.id.skillLevel,
		R.id.keep, R.id.mark, R.id.position, R.id.head,
		R.id.steal, R.id.firstTouch, R.id.pass, R.id.dribble,
		R.id.cross, R.id.finish, R.id.agility, R.id.strength,
		R.id.fitness, R.id.speed, R.id.creativity
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_page);
		//Receive player index from Intent
		Intent intent = getIntent();
		playerId = intent.getIntExtra(AssistantActivity.PLAYER_ID, 0);
		player = Container.getTeam().getPlayers().get(playerId);
		teamSize = Container.getTeam().getPlayers().size();
		setValues();
		setBestRates();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.player_menu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_item_prev_player:
				setPrevPlayer();
				setValues();
				setBestRates();
				return true;
			case R.id.menu_item_next_player:
				setNextPlayer();
				setValues();
				setBestRates();
				return true;
			default:
				return false;
		}
	}
	public void setNextPlayer(){
		if (playerId < teamSize-1) {
			playerId++;
		} else {
			playerId = 0;
		}
		player = Container.getTeam().getPlayers().get(playerId);
	}
	public void setPrevPlayer(){
		if (playerId > 0) {
			playerId--;
		} else {
			playerId = teamSize - 1;
		}
		player = Container.getTeam().getPlayers().get(playerId);
	}
	public void setValues() {
		ArrayList<String> data = player.getData(this);
		for (int i = 0; i < ids.length; i++) {
	   	 //((TextView)findViewById(ids[i]))
	   	 TextView text = ((TextView)findViewById(ids[i]));
	   	 if (i >= 8) {
	   	 	text.setTextColor(player.getAttrColor(this, i-8));
	   	 }
	   	 text.setText(data.get(i));
		}
	}
	public void setBestRates() {
		((TextView)findViewById(R.id.all_rates)).setText(player.getBestRates(11));
	}
}
