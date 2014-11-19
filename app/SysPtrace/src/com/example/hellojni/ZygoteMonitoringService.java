package com.example.hellojni;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.example.hellojni.MonitoringService.MonitorThread;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

public class ZygoteMonitoringService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		new ZygoteMonitorThread().start();
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

	class ZygoteMonitorThread extends Thread {

		public ZygoteMonitorThread() {
		}

		public void run() {

			
			
			String[] command = { "su", "-c", "/system/bin/sh" };// {"./strace",
																// "-p",
																// pid+""};//{"ls"};//
			
			
			Process p = null;
			try {
				p = Runtime.getRuntime().exec(command);

				DataOutputStream stdin = new DataOutputStream(
						p.getOutputStream());

//				stdin.writeBytes("strace -p " + pid + "\n"); // \n

				InputStream stdout = p.getErrorStream();
				byte[] buffer = new byte[1024];
				int read;

				String suid = p.toString();
				suid = suid.substring(suid.indexOf("=") + 1, suid.length() - 1);
				while (true) {
					String out = new String();
					read = stdout.read(buffer);
					
					// buffer = new byte[read];
					out += new String(buffer, 0, read);
					String response = out.toString();
					Log.e("ZYGOTE", response);

				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
