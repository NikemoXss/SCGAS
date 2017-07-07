package com.lmq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.czscg.R;

import java.util.List;
import java.util.Map;

public class RedPkgAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> list;
	private LayoutInflater mInflater;

	public RedPkgAdapter(Context mContext) {
		super();
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
	}

	public RedPkgAdapter(Context mContext, List<Map<String, Object>> list) {
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_redpkg, null);
			holder.money_pkg = (TextView) convertView.findViewById(R.id.money_pkg);
			holder.dataType_pkg = (TextView) convertView.findViewById(R.id.dataType_pkg);
			holder.end_time_pkg = (TextView) convertView.findViewById(R.id.end_time_pkg);
			holder.canuse_redpkg = (TextView) convertView.findViewById(R.id.canuse_redpkg);
			holder.isuse_redpkg = (ImageView) convertView.findViewById(R.id.isuse_redpkg);
			holder.notuse_redpkg = (ImageView) convertView.findViewById(R.id.notuse_redpkg);

			String status = list.get(position).get("status_scg").toString();
			if ("未使用".endsWith(status)) {
				holder.canuse_redpkg.setVisibility(View.GONE);
				holder.isuse_redpkg.setVisibility(View.GONE);
				holder.notuse_redpkg.setVisibility(View.GONE);
			} else if ("已使用".endsWith(status)) {
				holder.canuse_redpkg.setVisibility(View.GONE);
				holder.isuse_redpkg.setVisibility(View.GONE);
				holder.notuse_redpkg.setVisibility(View.VISIBLE);
			} else {
				holder.canuse_redpkg.setVisibility(View.GONE);
				holder.isuse_redpkg.setVisibility(View.VISIBLE);
				holder.notuse_redpkg.setVisibility(View.GONE);
			}

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		  
		holder.money_pkg.setText(list.get(position).get("money_scg").toString());
		holder.dataType_pkg.setText(list.get(position).get("dataType_scg").toString());
		holder.end_time_pkg.setText("限"+list.get(position).get("end_time_scg").toString()+"之前使用，并且投资金额不少于"+list.get(position).get("invest_money_scg").toString()+"元");

		return convertView;
	}

	public class ViewHolder {
		public TextView money_pkg, dataType_pkg, end_time_pkg, canuse_redpkg;
		public ImageView isuse_redpkg, notuse_redpkg;

	}

}
