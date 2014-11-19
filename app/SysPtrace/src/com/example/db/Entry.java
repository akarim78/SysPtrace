package com.example.db;

public class Entry {
	private int id;
	private String pkg;
	private String dump;

	public Entry(String pkg, String dump) {
		this.pkg = pkg;
		this.dump = dump;
	}

	public Entry(int id, String pkg, String dump) {
		this.id = id;
		this.pkg = pkg;
		this.dump = dump;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPkg() {
		return pkg;
	}

	public void setPkg(String pkg) {
		this.pkg = pkg;
	}

	public String getDump() {
		return dump;
	}

	public void setDump(String dump) {
		this.dump = dump;
	}
}
