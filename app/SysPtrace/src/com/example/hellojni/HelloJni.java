/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.hellojni;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class HelloJni extends Activity {
	/** Called when the activity is first created. */
	ListView lv;
	List<RunningAppProcessInfo> runningProcesses;
	List<PInfo> installedApps;
	ListAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		lv = (ListView) findViewById(R.id.lv);

		installedApps = getInstalledApps(false);
		((StraceApplication) getApplication()).setAppData(installedApps);
		adapter = new ListAdapter(this, installedApps);
		if (installedApps != null && installedApps.size() > 0) {
			// Set data to the list adapter
			lv.setAdapter(adapter);
		} else {
			// In case there are no processes running (not a chance :))
			Toast.makeText(getApplicationContext(),
					"No application is running", Toast.LENGTH_LONG).show();
		}

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent i = new Intent(HelloJni.this, MonitoringService.class);
				i.putExtra("pkg", installedApps.get(position).pname);
				i.putExtra("position", position);
				startService(i);
				// ((ListAdapter) lv.getAdapter()).notifyDataSetChanged();

			}
		});

		// EditText myFilter = (EditText) findViewById(R.id.etFilter);
		// myFilter.addTextChangedListener(new TextWatcher() {
		//
		// public void afterTextChanged(Editable s) {
		// Log.e("TEXT", "after");
		// }
		//
		// public void beforeTextChanged(CharSequence s, int start, int count,
		// int after) {
		// Log.e("TEXT", "before");
		// }
		//
		// public void onTextChanged(CharSequence s, int start, int before,
		// int count) {
		// adapter.getFilter().filter(s.toString());
		// Log.e("TEXT", "changed");
		// }
		// });

	}

	public native int startProcess(int pid);

	/*
	 * A native method that is implemented by the 'hello-jni' native library,
	 * which is packaged with this application.
	 */
	public native String stringFromJNI();

	/*
	 * This is another native method declaration that is *not* implemented by
	 * 'hello-jni'. This is simply to show that you can declare as many native
	 * methods in your Java code as you want, their implementation is searched
	 * in the currently loaded native libraries only the first time you call
	 * them.
	 * 
	 * Trying to call this function will result in a
	 * java.lang.UnsatisfiedLinkError exception !
	 */
	public native String unimplementedStringFromJNI();

	/*
	 * this is used to load the 'hello-jni' library on application startup. The
	 * library has already been unpacked into
	 * /data/data/com.example.hellojni/lib/libhello-jni.so at installation time
	 * by the package manager.
	 */
	static {
		System.loadLibrary("hello-jni");
	}

	private ArrayList<PInfo> getInstalledApps(boolean getSysPackages) {
		ArrayList<PInfo> res = new ArrayList<PInfo>();
		List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);

		for (int i = 0; i < packs.size(); i++) {
			PackageInfo p = packs.get(i);

			Intent intent = getPackageManager().getLaunchIntentForPackage(
					p.packageName);
			if (intent != null) {
				if ((!getSysPackages) && (p.versionName == null)) {
					continue;
				}
				PInfo newInfo = new PInfo();
				newInfo.appname = p.applicationInfo.loadLabel(
						getPackageManager()).toString();
				newInfo.pname = p.packageName;
				newInfo.versionName = p.versionName;
				newInfo.versionCode = p.versionCode;
				newInfo.icon = p.applicationInfo.loadIcon(getPackageManager());

				res.add(newInfo);
			}
		}
		return res;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_delete:
			File fileList = new File(ListAdapter.DIRECTORY);

			// check if dir is not null
			if (fileList != null) {

				// so we can list all files
				File[] filenames = fileList.listFiles();

				// loop through each file and delete
				for (File tmpf : filenames) {
					tmpf.delete();
				}
			}
			adapter = new ListAdapter(this, installedApps);
			lv.setAdapter(adapter);

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
