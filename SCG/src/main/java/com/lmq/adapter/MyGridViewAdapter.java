package com.lmq.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.czscg.R;
import com.lmq.main.activity.XxplImgActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyGridViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<Map<String, Object>> list;
	private LayoutInflater mInflater;

	public MyGridViewAdapter(Context mContext) {
		super();
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
	}

	public MyGridViewAdapter(Context mContext, List<Map<String, Object>> list) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.xxpl_item, null);
			holder.imageview = (ImageView) convertView.findViewById(R.id.xxpl_image);
			Picasso.with(mContext).load(list.get(position).get("image").toString())
					.placeholder(R.drawable.activityloading).error(R.drawable.activityloading).fit()
					.into(holder.imageview);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.imageview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, XxplImgActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelableArrayList("list", (ArrayList) list);
				bundle.putInt("position", position);
				intent.putExtras(bundle);
				mContext.startActivity(intent);
//				Toast.makeText(mContext, position + "", Toast.LENGTH_LONG).show();
			}
		});
		return convertView;
	}

	public class ViewHolder {
		public ImageView imageview;

	}

}
