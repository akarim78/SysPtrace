package com.example.hellojni;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class PInfo {
	String appname = "";
	String pname = "";
	String versionName = "";
	int versionCode = 0;
	Drawable icon;

	private void prettyPrint() {
		Log.v("Data", appname + "\t" + pname + "\t" + versionName + "\t"
				+ versionCode);
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "PInfo [appname=" + appname + ", pname=" + pname
				+ ", versionName=" + versionName + ", versionCode="
				+ versionCode + ", icon=" + icon + "]";
	}

}
