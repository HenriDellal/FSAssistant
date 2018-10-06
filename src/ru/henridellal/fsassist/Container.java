package ru.henridellal.fsassist;

import android.content.Context;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.File;

public class Container {
	private static Team team;
	public static Team getTeam() {
		return team;
	}
	public static void setTeam(Context context, String path, int teamType) {
		team = new Team(context, path);
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString((teamType == 0) ?"file_path_main":"file_path_youth", path).commit();
	} 
	public static void setTeam(Context context, File file, int teamType) {
		team = new Team(context, file);
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString((teamType == 0)?"file_path_main":"file_path_youth", file.getPath()).commit();
	}
}
