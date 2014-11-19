package com.example.hellojni;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.res.Configuration;

public class StraceApplication extends Application {

	private static StraceApplication singleton;
	private ArrayList<Boolean> running;
	private List<PInfo> installedApps;

	public StraceApplication getInstance() {
		return singleton;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		singleton = this;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public void setAppData(List<PInfo> apps) {
		this.installedApps = apps;
		this.running = new ArrayList<Boolean>();
		for (int i = 0; i < apps.size(); i++) {
			running.add(false);
		}
	}

	public void setRunning(int position, boolean isRunning) {
		this.running.set(position, isRunning);
	}

	public boolean getRunning(int position) {
		return this.running.get(position);
	}

	public List<Boolean> getRunningList() {
		return this.running;
	}

}
