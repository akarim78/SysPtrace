package com.example.hellojni;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DetailsActivity extends Activity {
	private TextView tvDetails;
	private String pkgName;
	private ListView lvDetails;
	private ArrayList<String> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_layout);
		// tvDetails = (TextView) findViewById(R.id.tvDetails);
		lvDetails = (ListView) findViewById(R.id.listView1);
		pkgName = getIntent().getStringExtra("pkg");
		new FileReadOperation().execute(pkgName);

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	private class FileReadOperation extends AsyncTask<String, Void, String> {
		ProgressDialog pd;

		@Override
		protected String doInBackground(String... params) {
			data = new ArrayList<String>();
			String packageName = params[0];
			// HashMap<String, Integer> sysCallMap =
			// populateHashMap(packageName);

			File myFile = new File(ListAdapter.DIRECTORY + packageName);
			FileInputStream fIn;
			StringBuffer message = new StringBuffer();
			try {
				fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";

				while ((aDataRow = myReader.readLine()) != null) {
					String[] parts = aDataRow.split(" ");
					if (parts.length > 1) {

						message.append(parts[1] + "-----");
						message.append(parts[2]);
						message.append("\n");
						data.add(parts[1] + "-----" + parts[2]);
					}
				}
				myReader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Iterator<String> iter = sysCallMap.keySet().iterator();
			//
			// while (iter.hasNext()) {
			// String sysCallName = iter.next();
			// message.append(sysCallName + " " + sysCallMap.get(sysCallName)
			// + "\n");
			//
			// }
			return message.toString();
		}

		@Override
		protected void onPostExecute(String result) {
			if (pd != null) {
				pd.dismiss();
			}
			// tvDetails.setText(result);
			lvDetails.setAdapter(new ArrayAdapter<String>(DetailsActivity.this,
					android.R.layout.simple_list_item_1, data));
		}

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(DetailsActivity.this);
			pd.setTitle("Processing...");
			pd.setMessage("Please wait.");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	private HashMap<String, Integer> populateHashMap(String pname) {
		HashMap<String, Integer> sysCallMap = new HashMap<String, Integer>();
		File myFile = new File(ListAdapter.DIRECTORY + pname);
		FileInputStream fIn;
		try {
			fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(new InputStreamReader(
					fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				String[] parts = aDataRow.split(" ");
				if (parts.length > 1) {
					if (sysCallMap.containsKey(parts[1])) {
						sysCallMap.put(parts[1], sysCallMap.get(parts[1]) + 1);
					} else {
						sysCallMap.put(parts[1], 1);
					}
				}
				aBuffer += aDataRow + "\n";
			}
			Log.e("Data", aBuffer);
			myReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sysCallMap;
	}

}
