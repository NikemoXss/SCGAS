package com.lmq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.czscg.R;

import java.util.List;
import java.util.Map;

public class MyBmAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> list;
	private LayoutInflater mInflater;

	public MyBmAdapter(Context mContext) {
		super();
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
	}

	public MyBmAdapter(Context mContext, List<Map<String, Object>> list) {
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
			convertView = mInflater.inflate(R.layout.item_mybm, null);
			holder.tvje = (TextView) convertView.findViewById(R.id.je);
			holder.tvrq = (TextView) convertView.findViewById(R.id.rq);
			holder.tzmje=(TextView) convertView.findViewById(R.id.tzmje);
			holder.tvje.setText(list.get(position).get("yhj").toString());
			holder.tvrq.setText("3.截至时间："+list.get(position).get("lasttime").toString());
			holder.tzmje.setText("2.需投资满"+list.get(position).get("limit").toString()+"；只能使用一次");
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

		}

		return convertView;
	}

	public class ViewHolder {
		public TextView tvrq;
		public TextView tvje;
		public TextView tzmje;

	}

}
