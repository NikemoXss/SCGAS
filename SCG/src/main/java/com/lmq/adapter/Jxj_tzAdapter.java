package com.lmq.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.czscg.R;

import java.util.List;
import java.util.Map;

public class Jxj_tzAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> list;
	private LayoutInflater mInflater;

	public Jxj_tzAdapter(Context mContext) {
		super();
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
	}

	public Jxj_tzAdapter(Context mContext, List<Map<String, Object>> list) {
		super();
		mInflater = LayoutInflater.from(mContext);
		this.mContext = mContext;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_jxj_tz, null);
			holder.id_jxj = (TextView) convertView.findViewById(R.id.id_jxj);
			holder.rate_jxj = (TextView) convertView.findViewById(R.id.rate_jxj);
			holder.end_time_jxj = (TextView) convertView.findViewById(R.id.end_time_jxj);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.id_jxj.setText(list.get(position).get("id").toString());
		holder.rate_jxj.setText(list.get(position).get("rate").toString() + "%");
		holder.end_time_jxj.setText("限" + list.get(position).get("end_time").toString() + "之前使用");
		return convertView;
	}

	public class ViewHolder {
		TextView id_jxj, rate_jxj, end_time_jxj;
	}

}
