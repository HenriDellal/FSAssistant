package ru.henridellal.fsassist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.*;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class Team {
	public final static int MODE_NAME = 00;
	public final static int MODE_AGE = 01;
	public final static int MODE_VALUE = 02;
	public final static int MODE_WAGE = 03;
	public final static int MODE_CONDITION = 04;
	public final static int MODE_POTENTIAL = 05;
	public final static int MODE_RATING = 06;
	public final static int MODE_SKILL_LEVEL = 07;
	public final static int MODE_ATTR_KEEP = 0100;
	public final static int MODE_ATTR_MARK = 0101;
	public final static int MODE_ATTR_POSITION = 0102;
	public final static int MODE_ATTR_HEAD = 0103;
	public final static int MODE_ATTR_STEAL = 0104;
	public final static int MODE_ATTR_FIRST_TOUCH = 0105;
	public final static int MODE_ATTR_PASS = 0106;
	public final static int MODE_ATTR_DRIBBLE = 0107;
	public final static int MODE_ATTR_CROSS = 0110;
	public final static int MODE_ATTR_FINISH = 0111;
	public final static int MODE_ATTR_AGILITY = 0112;
	public final static int MODE_ATTR_STRENGTH = 0113;
	public final static int MODE_ATTR_FITNESS = 0114;
	public final static int MODE_ATTR_SPEED = 0115;
	public final static int MODE_ATTR_CREATIVITY = 0116;
	public final static int MODE_RATE_GK = 0200;
	public final static int MODE_RATE_SW = 0201;
	public final static int MODE_RATE_DLR = 0202;
	public final static int MODE_RATE_WBLR = 0203;
	public final static int MODE_RATE_DC = 0204;
	public final static int MODE_RATE_DMC = 0205;
	public final static int MODE_RATE_MLR = 0206;
	public final static int MODE_RATE_AMLR = 0207;
	public final static int MODE_RATE_AMC = 0210;
	public final static int MODE_RATE_MC = 0211;
	public final static int MODE_RATE_ST = 0212;
	public final static int MODE_MASK = 3 << 6;
	public final static int MODE_CATEGORY_INFO = 0;
	public final static int MODE_CATEGORY_ATTR = 0100;
	public final static int MODE_CATEGORY_RATE = 0200;
	
	private static int MODE;
	
	private long wage;
	private ArrayList<Player> players;
	private File playersFile;
	
	public static void setMode(int mode) {
		MODE = mode;
	}
	
	public void sortPlayers() {
		if ((MODE & MODE_MASK) == MODE_CATEGORY_INFO) {
			Collections.sort(this.players, new Comparator<Player>(){
				public int compare(Player first, Player second) {
					/*if (first.getInfo(MODE) > second.getInfo(MODE)) {
						return -1;
					} else if (first.getInfo(MODE) < second.getInfo(MODE)) {
						return 1;
					} else {
						return 0;
					}*/
					return first.compareInfo(second, MODE);
				}
			});
		} else if ((MODE & MODE_MASK) == MODE_CATEGORY_ATTR) {
			Collections.sort(this.players, new Comparator<Player>(){
				public int compare(Player first, Player second) {
					return first.compareAttr(second, MODE-MODE_CATEGORY_ATTR);
				}
			});
		} else if ((MODE & MODE_MASK) == MODE_CATEGORY_RATE) {
			Collections.sort(this.players, new Comparator<Player>(){
				@Override
				public int compare(Player first, Player second) {
					return first.compareRate(second, MODE-MODE_CATEGORY_RATE);
				}
			});
		}
	}
	
	private void playersInit(Context c, String path){
		playersFile = new File(Environment.getExternalStorageDirectory(), path);
		playersInit(c, playersFile);
	}
	private void playersInit(Context c, File f){
		playersFile = f;
		this.players = new ArrayList<Player>();
		//get a copy of .xls file
		StringBuffer playersSheet = new StringBuffer();
		FileInputStream sheetReader = null;
		BufferedReader br = null;
		
		try {
			sheetReader = new FileInputStream(playersFile);
			br = new BufferedReader(new InputStreamReader(sheetReader));
			String line = null;
			//Skipping the first line
			line = br.readLine();
			while ((line = br.readLine()) != null) {
				// get strings separated by whitespaces
				String[] data = Pattern.compile("\\s").split(line);
				this.players.add(new Player(data));
				
			}
		} catch (Exception e) {
			Toast.makeText(c, ""+e, Toast.LENGTH_LONG).show();
			try {
				sheetReader.close();
				br.close();
			} catch (Exception ex) {
			//	Toast.makeText(c, ""+ex, Toast.LENGTH_LONG).show();
			}
		}
	}
	public ArrayList<Player> getPlayers() {
		return this.players;
	}
	private void setWage(){
		for (int i = 0; i < this.players.size(); i++) {
			this.wage += this.players.get(i).getWage();
		}
	}
	//new constructor for FileChooser
	public Team(Context c, File f){
		this.playersInit(c, f);
		this.setWage();
	}
	// TODO this constructor should be removed
	public Team(Context c, String path){
		this.playersInit(c, path);
		this.setWage();
	}
}
