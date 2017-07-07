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

public class ZcjlItemAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> list;
	private LayoutInflater mInflater;

	public ZcjlItemAdapter(Context mContext) {
		super();
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
	}

	public ZcjlItemAdapter(Context mContext, List<Map<String, Object>> list) {
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
			convertView = mInflater.inflate(R.layout.item_zcjl, null);
			holder.zcjl_tbr = (TextView) convertView.findViewById(R.id.zcjl_tbr);
			holder.zcjl_tbje = (TextView) convertView.findViewById(R.id.zcjl_tbje);
			holder.zcjl_tbsj = (TextView) convertView.findViewById(R.id.zcjl_tbsj);
			holder.zcjl_tbfs = (TextView) convertView.findViewById(R.id.zcjl_tbfs);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.zcjl_tbr.setText(list.get(position).get("zcjl_tbr").toString());
		holder.zcjl_tbje.setText(list.get(position).get("zcjl_tbje").toString());
		holder.zcjl_tbsj.setText(list.get(position).get("zcjl_tbsj").toString());
		holder.zcjl_tbfs.setText(list.get(position).get("zcjl_tbfs").toString());

		return convertView;
	}

	public class ViewHolder {
		public TextView zcjl_tbr;
		public TextView zcjl_tbje;
		public TextView zcjl_tbsj;
		public TextView zcjl_tbfs;
	}

}
