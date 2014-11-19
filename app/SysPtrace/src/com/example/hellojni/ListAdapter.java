package com.example.hellojni;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ListAdapter extends ArrayAdapter<PInfo> {
	// List context
	private final Activity context;
	// List values
	public List<PInfo> packages;
	public List<PInfo> filtered;
	private AppFilter filter;

	public static final String DIRECTORY = "/mnt/sdcard/sysfiles/";

	public ListAdapter(Activity context, List<PInfo> values) {
		super(context, R.layout.item_layout, values);
		this.context = context;
		this.packages = values;
		this.filtered = values;
	}

	@Override
	public Filter getFilter() {
		if (filter == null) {
			filter = new AppFilter();
		}
		return filter;
	}

	/**
	 * Constructing list element view
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		final View rowView = inflater.inflate(R.layout.item_layout, parent,
				false);

		TextView appName = (TextView) rowView.findViewById(R.id.appNameText);
		appName.setText(packages.get(position).appname);
		ImageView img = (ImageView) rowView.findViewById(R.id.detailsIco);
		img.setImageDrawable(packages.get(position).getIcon());

		ToggleButton btn = (ToggleButton) rowView.findViewById(R.id.onOff);
		Button btnHistory = (Button) rowView.findViewById(R.id.btnHistory);

		btn.setTag(position);
		if (((StraceApplication) context.getApplication()).getRunningList()
				.size() > 0) {
			btn.setChecked(((StraceApplication) context.getApplication())
					.getRunning(position));
		}

		btn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// get data
				int pos = -1;
				Object obj = buttonView.getTag();
				if (obj instanceof Integer) {
					pos = ((Integer) obj).intValue();
				}
				if (isChecked) {
					// start monitoring
					Intent i = new Intent(context, MonitoringService.class);
					i.putExtra("pkg", packages.get(pos).pname);
					i.putExtra("position", pos);
					context.startService(i);
				} else {
					((StraceApplication) context.getApplication()).setRunning(
							pos, false);
					ActivityManager mActivityManager = (ActivityManager) context
							.getSystemService(Context.ACTIVITY_SERVICE);
					mActivityManager.killBackgroundProcesses(packages.get(pos).pname);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					File file = new File(DIRECTORY
							+ packages.get(pos).pname);
					Button btnHist = (Button) rowView
							.findViewById(R.id.btnHistory);
					if (file.exists())
						btnHist.setEnabled(true);
				}
			}
		});

		// check file
		File file = new File(DIRECTORY + packages.get(position).pname);
		if (!file.exists())
			btnHistory.setEnabled(false);

		btnHistory.setTag(position);
		btnHistory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// get data
				int pos = -1;
				Object obj = v.getTag();
				if (obj instanceof Integer) {
					pos = ((Integer) obj).intValue();
				}
				String packageName = packages.get(pos).pname;
				Intent intent = new Intent(context, DetailsActivity.class);
				intent.putExtra("pkg", packageName);
				context.startActivity(intent);

			}
		});
		return rowView;
	}

	private class AppFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			constraint = constraint.toString().toLowerCase();
			FilterResults result = new FilterResults();
			if (constraint != null && constraint.toString().length() > 0) {
				ArrayList<PInfo> filteredItems = new ArrayList<PInfo>();

				for (int i = 0, l = filtered.size(); i < l; i++) {
					PInfo pinfo = filtered.get(i);
					if (pinfo.toString().toLowerCase().contains(constraint))
						filteredItems.add(pinfo);
				}
				result.count = filteredItems.size();
				result.values = filteredItems;
			} else {
				Log.e("ERROR", "Check1");
				synchronized (this) {
					result.values = filtered;
					result.count = filtered.size();
				}
			}
			return result;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			filtered = (ArrayList<PInfo>) results.values;
			notifyDataSetChanged();
			clear();
			for (int i = 0, l = filtered.size(); i < l; i++)
				add(filtered.get(i));
			notifyDataSetInvalidated();

		}
	}

}
