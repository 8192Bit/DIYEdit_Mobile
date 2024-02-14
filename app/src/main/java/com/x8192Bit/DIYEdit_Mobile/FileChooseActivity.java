package com.x8192Bit.DIYEdit_Mobile;

import static com.x8192Bit.DIYEdit_Mobile.MainActivity.CHOOSE_RESULT;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import x8192Bit.DIYEdit_Mobile.databinding.ActivityFileChooseBinding;

import x8192Bit.DIYEdit_Mobile.R;



public class FileChooseActivity extends AppCompatActivity {

	public static final int CHOOSE_FILE = 0;
	public static final int CHOOSE_DIRECTORY = 1;

	private int chooseType = CHOOSE_FILE;


	File CurrentPath = null;

	//TODO UPDATE!!!!!!!!!!!!!!!!!!!!!!!!FUK LISTVIEW
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_choose);

		Intent intent = getIntent();
		chooseType = intent.getIntExtra(MainActivity.CHOOSE_TYPE, CHOOSE_FILE);

		CurrentPath = Environment.getExternalStorageDirectory();

		refreshList(getApplicationContext());

		ListView fileChooser = findViewById(R.id.fileChooseView);
		Button directoryButton = findViewById(R.id.fileChooseButton);
		fileChooser.setItemsCanFocus(true);

		directoryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
				Intent intentResult = new Intent();
				intentResult.putExtra(CHOOSE_RESULT, CurrentPath.getAbsolutePath());
				setResult(1919810, intentResult);
				finish();
				}  catch (Exception e){
					new AlertDialog.Builder(FileChooseActivity.this)
							.setTitle("What the fuck????!!")
							.setMessage("GO FUCK YOURSELF!!!!!!!!!!!!!!!!!11").show();
				}
			}
		});

		fileChooser.setOnItemClickListener((adapterView, view, i, l) -> {
			File originalPath = CurrentPath;
			try {
				String path = (String) (((TextView)((LinearLayout) view).getChildAt(1))).getText();
				if(path.equals("..")){
					CurrentPath = CurrentPath.getParentFile();
				} else {
					CurrentPath = new File(CurrentPath.getAbsolutePath() + '/' + path);
					if(CurrentPath.isDirectory() && chooseType==CHOOSE_DIRECTORY) {
						fileChooser.setSelection(i);
					} else if (CurrentPath.isFile() && chooseType==CHOOSE_FILE) {
						Intent intentResult = new Intent();
						intentResult.putExtra(CHOOSE_RESULT, CurrentPath.getAbsolutePath());
						setResult(1919810, intentResult);
						finish();
					}
				}
				refreshList(getApplicationContext());
			} catch (NullPointerException e){
				//Toast t = Toast.makeText(getApplicationContext(),"目录不存在",Toast.LENGTH_SHORT);
				//t.show();
				CurrentPath = originalPath;
			} catch (Exception e){
				new AlertDialog.Builder(FileChooseActivity.this)
						.setTitle("What the fuck????!!")
						.setMessage("GO FUCK YOURSELF!!!!!!!!!!!!!!!!!11").show();
				CurrentPath = originalPath;
			}
		});
	}

	public void refreshList(Context c) {
		ListView fileChooser = findViewById(R.id.fileChooseView);
		TextView PathView = findViewById(R.id.fileChooseTextView);
		String[] from = {"img", "text"};
		int[] to = {R.id.FileChooseItemImageView, R.id.FileChooseItemTextView};
		ArrayList<Map<String, Object>> files = new ArrayList<>();
		HashMap<String, Object> parentFolder = new HashMap<>();
		parentFolder.put("img", R.drawable.exit);
		parentFolder.put("text", "..");
		files.add(parentFolder);
		for (File f : Objects.requireNonNull(CurrentPath.listFiles())) {
			HashMap<String, Object> file = new HashMap<>();
			if (f.isFile()) {
				String splited[] =f.getName().split("\\.");
				if(f.getName().equals("MDATA") || f.getName().equals("GDATA") ||f.getName().equals("RDATA")){
					file.put("img",  R.drawable.save_wii);
				} else if (splited.length > 1 &&( splited[splited.length-1].equals("sav") || splited[splited.length-1].equals("dsv"))) {
					file.put("img", R.drawable.save_ds);
				} else if (splited.length > 1 && splited[splited.length-1].equals("mio")) {
					file.put("img", R.mipmap.ic_launcher_foreground);
				} else {
					file.put("img", R.drawable.baseline_file_open_24);
				}
			} else if (f.isDirectory()) {
				file.put("img", R.drawable.directory);
			}
			file.put("text", f.getName());
			files.add(file);
		}
		fileChooser.setAdapter(new SimpleAdapter(c,files,R.layout.filechoose_item_layout,from,to));
		PathView.setText(CurrentPath.getAbsolutePath());
	}
}