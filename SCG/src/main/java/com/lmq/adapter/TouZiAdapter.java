package com.lmq.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.main.activity.BidItem_Scg;

import java.util.List;
import java.util.Map;

public class TouZiAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> list;
	private LayoutInflater mInflater;

	public TouZiAdapter(Context mContext) {
		super();
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
	}

	public TouZiAdapter(Context mContext, List<Map<String, Object>> list) {
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
			convertView = mInflater.inflate(R.layout.item_list_indexfragment, null);
			holder.list_borrow_name = (TextView) convertView.findViewById(R.id.list_borrow_name);
			holder.list_borrow_money = (TextView) convertView.findViewById(R.id.list_borrow_money);
			holder.list_has_borrow = (TextView) convertView.findViewById(R.id.list_has_borrow);
			holder.list_borrow_duration = (TextView) convertView.findViewById(R.id.list_borrow_duration);
			holder.list_borrow_interest_rate = (TextView) convertView.findViewById(R.id.list_borrow_interest_rate);
			holder.list_jindu = (TextView) convertView.findViewById(R.id.list_jindu);
			holder.list_progress = (ProgressBar) convertView.findViewById(R.id.list_progress);
			holder.title_icon_r = (ImageView) convertView.findViewById(R.id.title_icon_r);
			holder.title_icon_l = (ImageView) convertView.findViewById(R.id.title_icon_l);
			holder.item_bg = (FrameLayout) convertView.findViewById(R.id.item_bg);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (!"1".equals(list.get(position).get("repayment_type").toString())) {// 1表示天标
																				// 其余数值表示月标
			holder.title_icon_r.setBackgroundResource(R.drawable.image_r2);
			holder.title_icon_l.setBackgroundResource(R.drawable.title_i2);
			holder.item_bg.setBackgroundResource(R.drawable.item_bg_2);
		} else {
			holder.title_icon_r.setBackgroundResource(R.drawable.image_r1);
			holder.title_icon_l.setBackgroundResource(R.drawable.title_i1);
			holder.item_bg.setBackgroundResource(R.drawable.item_bg_1);
		}
		holder.list_progress.setProgress((int) Double.parseDouble(list.get(position).get("progress_tz_sx").toString()));
		holder.list_borrow_name.setText(list.get(position).get("item_tzbt_sx").toString());
		holder.list_borrow_money.setText(list.get(position).get("item_jkje_sx").toString());
		holder.list_has_borrow.setText(list.get(position).get("has_borrow_sx").toString());
		holder.list_borrow_duration.setText(list.get(position).get("item_tzqx_sx").toString());
		holder.list_borrow_interest_rate.setText(list.get(position).get("item_nhl_sx").toString());
		holder.list_jindu.setText("进度:" + list.get(position).get("progress_tz_sx").toString() + "%");

		holder.item_bg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Long id = (Long) list.get(position).get("id");
				int type = (Integer) list.get(position).get("type");
				String string = (String) list.get(position).get("repayment_type");
				Intent intent = new Intent(mContext, BidItem_Scg.class);
				intent.putExtra("id", id);
				intent.putExtra("type", type);
				mContext.startActivity(intent);
			}  
		});
		return convertView;
	}

	public class ViewHolder {
		TextView list_borrow_name, list_borrow_money, list_has_borrow, list_borrow_duration, list_borrow_interest_rate,
				list_jindu;
		ProgressBar list_progress;
		ImageView title_icon_r, title_icon_l;
		FrameLayout item_bg;
	}

}
