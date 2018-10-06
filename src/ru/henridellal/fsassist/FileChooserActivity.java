package ru.henridellal.fsassist;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FileChooserActivity extends Activity {
	private File curDirectory;
	private int teamType;
	
	private File[] fileArray = null;
	private ArrayList<File> files = new ArrayList<File>();
	private FileListAdapter adapter=null;
	protected void setCurDirectory(File f) {
		curDirectory = f;
	}
	protected void setFile(int position, File f) {
		files.set(position, f);
	}
	protected File getFile(int position) {
		return files.get(position);
	}
	protected void setFileList(File directory) {
		fileArray = directory.listFiles(new FileFilter() {
			private boolean isExtensionValid(File f) {
				String filePath = f.getPath();
				int i = filePath.lastIndexOf('.');
				if (i > 0 && i < filePath.length()-1) {
					return filePath.substring(i+1).equals("xls");
				}
				return false;
			}
			@Override
			public boolean accept(File f) {
				if (f.isHidden()) {
					return false;
				}
				else if (f.isDirectory() || isExtensionValid(f)) {
					return true;
				} else {
					return false;
				}
			}
		});
		files.clear();
		Collections.addAll(files, fileArray);
	}
	/*protected void sortFileList() {
		Collections.sort(new ArrayList<File>(files), new Comparator<File>() {
			public int compare(File first, File second) {
				return first.getName().compareTo(second.getName());
			}
		});
	}*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_chooser);
		Intent intent = getIntent();
		teamType = intent.getIntExtra(MainActivity.TEAM_TYPE, 0);
		curDirectory = Environment.getExternalStorageDirectory();
		setFileList(curDirectory);
		ListView fileList = (ListView)findViewById(R.id.fileList);
		adapter = new FileListAdapter(this, R.layout.file_list_item);
		fileList.setAdapter(adapter);
		fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position, long id) {
				File chosenFile = getFile(position);
				if (chosenFile.isDirectory()) {
					setCurDirectory(chosenFile);
					setFileList(curDirectory);
					((FileListAdapter)parent.getAdapter()).sort();
				} else {
					try {
						Container.setTeam(FileChooserActivity.this, chosenFile, teamType);
						//i.setComponent(ComponentName.unflattenFromString("ru.henridellal.fsassist/ru.henridellal.fsassist.AssistantActivity"));
						//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						Intent intent = new Intent(FileChooserActivity.this, AssistantActivity.class);
						intent.putExtra(MainActivity.TEAM_TYPE, teamType);
						startActivity(intent);
						finish();
					} catch (Exception e) {
						Toast.makeText(FileChooserActivity.this, ""+e, Toast.LENGTH_LONG).show();
					}
				}
				//runOnUiThread()
				//updateList();
			}
		});
	}
	@Override
	public void onBackPressed() {
		if (curDirectory.equals(Environment.getExternalStorageDirectory())) {
			startActivity(new Intent(FileChooserActivity.this, AssistantActivity.class));
			finish();
		}
		else {
			try {
				setFileList(new File(curDirectory.getParent()));
				curDirectory = new File(curDirectory.getParent());
				adapter.sort();
			} catch (Exception e) {
				Toast.makeText(this, ""+e, Toast.LENGTH_LONG).show();
			}
		}
	}
	
	
	/*public void updateList() {
	
		try {
			Activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					adapter.notifyDataSetChanged();
				}
			}
		);
		} catch (Exception e) {}
	}*/
	public class FileListAdapter extends BaseAdapter {
		private int resource;
		private Context context;
		//View.OnClickListener onClickListener;
		public void sort() {
			Collections.sort(files, new Comparator<File>() {
				@Override
				public int compare(File first, File second) {
				//additional fix
					return first.getName().toLowerCase().compareTo(second.getName().toLowerCase());
				}
			});
			notifyDataSetChanged();
		}
		public FileListAdapter(Context context, int resource) {
			super();
			this.context = context;
			this.resource = resource;
			sort();
			/*onClickListener = new View.OnClickListener() {
				public void onClick(View v) {
					File chosenFile = getFile((Integer)v.getTag());
					if (chosenFile.isDirectory()) {
						setCurDirectory(chosenFile);
					} else {
						Container.setTeam(chosenFile);
					}
					//notifyDataSetChanged();
				}
			};*/
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v;
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(resource, parent, false);
				//Toast.makeText(AssistantActivity.this, "is running", Toast.LENGTH_LONG).show();
			} else {
				v = convertView;
			}
			((TextView) v.findViewById(R.id.file_name)).setText(files.get(position).getName());
			((ImageView) v.findViewById(R.id.file_image)).setImageResource(files.get(position).isDirectory() ? R.drawable.directory : R.drawable.document);
			
			return v;
		}
		@Override
		public int getCount() {
			return files.size();
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
