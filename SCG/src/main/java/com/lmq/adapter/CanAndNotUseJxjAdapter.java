package com.lmq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.czscg.R;

import java.util.List;
import java.util.Map;

public class CanAndNotUseJxjAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> list;
	private LayoutInflater mInflater;

	public CanAndNotUseJxjAdapter(Context mContext) {
		super();
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
	}

	public CanAndNotUseJxjAdapter(Context mContext, List<Map<String, Object>> list) {
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
			convertView = mInflater.inflate(R.layout.item_canandnotjxj, null);
			//public TextView borrow_id_scg,type_scg,time_tv_scg,use_time_scg,end_time_scg;
			holder.borrow_id_scg=(TextView) convertView.findViewById(R.id.borrow_id_scg);
			holder.type_scg=(TextView) convertView.findViewById(R.id.type_scg);
			holder.time_tv_scg=(TextView) convertView.findViewById(R.id.time_tv_scg);
			holder.use_time_scg=(TextView) convertView.findViewById(R.id.use_time_scg);
			holder.end_time_scg=(TextView) convertView.findViewById(R.id.end_time_scg);
			holder.rate_scg=(TextView) convertView.findViewById(R.id.rate_scg);
			
			holder.sybh_ll = (LinearLayout) convertView.findViewById(R.id.sybh_ll);
			holder.use_time_ll = (LinearLayout) convertView.findViewById(R.id.use_time_ll);
			holder.item_bg_jxj_1 = (LinearLayout) convertView.findViewById(R.id.item_bg_jxj_1);
			holder.item_bg_jxj_2 = (LinearLayout) convertView.findViewById(R.id.item_bg_jxj_2);
			holder.item_bg_jxj_3 = (LinearLayout) convertView.findViewById(R.id.item_bg_jxj_3);

			String status = list.get(position).get("status_scg").toString();
			if ("2".endsWith(status)) {
				holder.item_bg_jxj_1.setBackgroundResource(R.drawable.can_1);
				holder.sybh_ll .setVisibility(View.GONE);
				holder.use_time_ll .setVisibility(View.GONE);
				holder.time_tv_scg.setText("截止时间:");
			} else if ("3".endsWith(status)) {
				holder.item_bg_jxj_1.setBackgroundResource(R.drawable.ishas_1);
				holder.sybh_ll .setVisibility(View.VISIBLE);
				holder.use_time_ll .setVisibility(View.VISIBLE);
				holder.time_tv_scg.setText("使用时间:");
			} else {
				holder.item_bg_jxj_1.setBackgroundResource(R.drawable.not_1);
				holder.sybh_ll .setVisibility(View.GONE);
				holder.use_time_ll .setVisibility(View.GONE);
				holder.time_tv_scg.setText("截止时间:");
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

		}
		holder.rate_scg.setText(list.get(position).get("rate_scg").toString()+"%");
		holder.type_scg.setText(list.get(position).get("type_scg").toString());
		holder.end_time_scg.setText(list.get(position).get("end_time_scg").toString());
		
		String status1 = list.get(position).get("status_scg").toString();
		if ("3".endsWith(status1)) {
			holder.borrow_id_scg.setText(list.get(position).get("borrow_id_scg").toString());
			holder.use_time_scg.setText(list.get(position).get("use_time_scg").toString());
		} 
		return convertView;
	}

	public class ViewHolder {
		public TextView borrow_id_scg,type_scg,time_tv_scg,use_time_scg,end_time_scg,rate_scg;
	
		LinearLayout item_bg_jxj_1, item_bg_jxj_2, item_bg_jxj_3,sybh_ll,use_time_ll;

	}

}
