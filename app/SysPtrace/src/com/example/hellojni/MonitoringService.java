package com.example.hellojni;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

import com.example.db.DatabaseManager;
import com.example.db.Entry;

public class MonitoringService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if (intent != null) {
			int position = intent.getIntExtra("position", -1);
			String packageName = intent.getStringExtra("pkg");
			new MonitorThread(packageName, position).start();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	public int getPID(String pkgName) {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningProcesses = manager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo pinfo : runningProcesses) {
			System.out.println(pinfo.processName);
			if (pinfo.processName.equals(pkgName)) {
				return pinfo.pid;
			}
		}
		return -1;
	}

	class MonitorThread extends Thread {
		String pkg;
		int position;

		public MonitorThread(String pkg, int pos) {
			this.pkg = pkg;
			this.position = pos;
		}

		public void run() {

			//
			PackageManager pm = getPackageManager();
			Intent intent = pm.getLaunchIntentForPackage(this.pkg);
			if (intent != null) {
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

				try {
					sleep(3000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				int pid = getPID(this.pkg);
				((StraceApplication) getApplication()).setRunning(
						this.position, true);
				String[] command = { "su", "-c", "/system/bin/sh" };
				Process p = null;
				String out = new String();
				try {
					p = Runtime.getRuntime().exec(command);

					DataOutputStream stdin = new DataOutputStream(
							p.getOutputStream());

					stdin.writeBytes("ptrace -p " + pid + " -f " + pkg + "\n"); // \n
					Log.e("Check", "Hello " + pid);

					InputStream stdout = p.getErrorStream();
					byte[] buffer = new byte[1024];
					int read;

//					String suid = p.toString();
//					suid = suid.substring(suid.indexOf("=") + 1,
//							suid.length() - 1);
//					while (true) {
//						// String out = new String();
//						boolean isRunning = ((StraceApplication) getApplication())
//								.getRunning(this.position);
//						// read = stdout.read(buffer);
//
//						if (isRunning == false) {
//							// stdin.writeBytes("kill -9 " + suid + "\n"); // \n
//							break;
//						}
//						// buffer = new byte[read];
//						// out += new String(buffer, 0, read);
//						// Log.e("DUMP", out);
//
//					}

//					File myFile = new File("/mnt/sdcard/" + pkg);
//					FileInputStream fIn = new FileInputStream(myFile);
//					BufferedReader myReader = new BufferedReader(
//							new InputStreamReader(fIn));
//					String aDataRow = "";
//					String aBuffer = "";
//					while ((aDataRow = myReader.readLine()) != null) {
//						aBuffer += aDataRow + "\n";
//					}
//					myReader.close();
//					Log.e("Data", aBuffer);
//					DatabaseManager dbManager = new DatabaseManager(
//							MonitoringService.this);
//					dbManager.insertDump(new Entry(pkg, aBuffer));

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Log.e("ERROR", "Didn't find an entry point of the application");
			}
		}

	}

}
