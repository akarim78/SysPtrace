package com.example.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager extends SQLiteOpenHelper {
	public static final String DB_NAME = "syscalls";
	public static final String TABLE_SYSCALLS = "syscalls";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_PKG = "pkg";
	public static final String COLUMN_DUMP = "dump";
	// Database creation sql statement
	private static final String TABLE_CREATE = "create table " + TABLE_SYSCALLS
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_PKG + " text not null, " + COLUMN_DUMP
			+ " text not null);";

	public DatabaseManager(Context context) {
		super(context, DB_NAME, null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public Entry getDumpByPkg(String pkg) {
		SQLiteDatabase database = getReadableDatabase();
		Entry entry = null;

		Cursor cursor = database.query(TABLE_SYSCALLS, null, COLUMN_PKG + "=?",
				new String[] { pkg }, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
				String packageName = cursor.getString(cursor
						.getColumnIndex(COLUMN_PKG));
				String dump = cursor.getString(cursor
						.getColumnIndex(COLUMN_DUMP));
				entry = new Entry(id, packageName, dump);
				cursor.moveToNext();
			}
		}
		if (entry != null) {
			Log.e("Data", entry.getPkg() + "--" + entry.getDump());
		}

		cursor.close();
		database.close();
		return entry;
	}

	public long insertDump(Entry entry) {
		SQLiteDatabase database = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_PKG, entry.getPkg());
		values.put(COLUMN_DUMP, entry.getDump());
		long insertId = database.insert(TABLE_SYSCALLS, null, values);
		Log.e("INSERT", "insert" + insertId);
		database.close();
		return insertId;
	}

	public int updateDump(Entry entry) {
		SQLiteDatabase database = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_PKG, entry.getPkg());
		values.put(COLUMN_DUMP, entry.getDump());
		int rowsUpdated = database.update(TABLE_SYSCALLS, values, COLUMN_PKG
				+ " = ?", new String[] { entry.getPkg() });
		Log.e("UPDATE", "update" + rowsUpdated);
		database.close();
		return rowsUpdated;
	}

	public boolean packageExists(String pkg) {
		boolean exists = false;
		SQLiteDatabase database = getReadableDatabase();
		List<Entry> comments = new ArrayList<Entry>();

		Cursor cursor = database.query(TABLE_SYSCALLS, null, COLUMN_PKG + "=?",
				new String[] { pkg }, null, null, null);
		if (cursor != null && cursor.getCount() > 0)
			exists = true;
		cursor.close();
		database.close();

		return exists;
	}

}
