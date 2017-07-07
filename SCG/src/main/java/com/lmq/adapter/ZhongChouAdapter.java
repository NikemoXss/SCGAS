package com.lmq.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.main.activity.ZcItem_Sx;
import com.lmq.main.util.Default;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class ZhongChouAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> list;
	private LayoutInflater mInflater;

	public ZhongChouAdapter(Context mContext) {
		super();
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
	}

	public ZhongChouAdapter(Context mContext, List<Map<String, Object>> list) {
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
			convertView = mInflater.inflate(R.layout.item_zhongzhou_sx, null);
			holder.Zc_progress = (ProgressBar) convertView.findViewById(R.id.Zc_progress);
			holder.zc_title = (TextView) convertView.findViewById(R.id.zc_title);
			// holder.zc_fbsj = (TextView)
			// convertView.findViewById(R.id.zc_fbsj);
			holder.Zc_zcje = (TextView) convertView.findViewById(R.id.Zc_zcje);
			holder.Zc_ycje = (TextView) convertView.findViewById(R.id.Zc_ycje);
			holder.Zc_czrs = (TextView) convertView.findViewById(R.id.Zc_czrs);
			holder.Zc_jindu = (TextView) convertView.findViewById(R.id.Zc_jindu);
			holder.Zc_starttime = (TextView) convertView.findViewById(R.id.Zc_starttime);
			holder.zc_item_ljrg = (TextView) convertView.findViewById(R.id.zc_item_ljrg);
			holder.Zc_pic = (ImageView) convertView.findViewById(R.id.Zc_pic);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.Zc_progress.setProgress((int) Double.parseDouble(list.get(position).get("Zc_progress").toString()));
		holder.zc_title.setText(list.get(position).get("zc_title").toString());
		// holder.zc_fbsj.setText(list.get(position).get("zc_fbsj").toString());
		holder.Zc_zcje.setText(list.get(position).get("Zc_zcje").toString());
		holder.Zc_ycje.setText(list.get(position).get("Zc_ycje").toString());
		holder.Zc_czrs.setText(list.get(position).get("Zc_czrs").toString());
		holder.Zc_jindu.setText(list.get(position).get("Zc_jindu").toString());
		holder.Zc_starttime.setText(list.get(position).get("Zc_starttime").toString());

		Picasso.with(mContext).load(Default.yu + list.get(position).get("Zc_pic").toString())
				.placeholder(R.drawable.activityloading).error(R.drawable.activityloading).fit().into(holder.Zc_pic);
		holder.zc_item_ljrg.setText(codeToName(list.get(position).get("raise_status").toString()));
		holder.zc_item_ljrg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, ZcItem_Sx.class);
				intent.putExtra("id", list.get(position).get("id").toString());
				intent.putExtra("progress", list.get(position).get("Zc_progress").toString());
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}

	public class ViewHolder {
		public TextView zc_title;
		// public TextView zc_fbsj;
		public TextView Zc_zcje;
		public TextView Zc_ycje;
		public TextView Zc_czrs;
		ProgressBar Zc_progress;
		public TextView Zc_jindu;
		public TextView Zc_starttime;
		TextView zc_item_ljrg;
		ImageView Zc_pic;

	}
	
	public static String codeToName(String code){
		String name="";
		switch (Integer.parseInt(code)) {
		case 1:
			name="预热中";
			break;
		case 3:
			name="立即认购";
			break;
		case 4:
			name="复审中";
			break;
		case 5:
			name="出售中";
			break;
		case 6:
			name="投票中";
			break;
		case 7:
			name="审核投票";
			break;
		case 8:
			name="流标";
			break;
		case 9:
			name="回款中";
			break;
		case 10:
			name="已完成";
			break;

		default:
			break;
		}
	return name;
	}

}
