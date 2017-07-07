/**
 *
 */
package com.lmq.main.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.item.bounsItem;
import com.lmq.main.util.Data;

/**
 * 奖金记录
 *
 * @author zzx
 */
public class peopleInfoBonusActivity extends BaseActivity implements OnItemClickListener {

	private ListView mListView;
	private List mListData;
	private LayoutInflater mLayoutInflater;
	private bonusAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_info_bonus);

		TextView text = (TextView) findViewById(R.id.title);
		text.setText(R.string.peo_info7);

		mListView = (ListView) findViewById(R.id.listview);

		updateInfo(Data.peopleBounsJson);

		mAdapter = new bonusAdapter();

		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mLayoutInflater = LayoutInflater.from(this);

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

	}

	public void updateInfo(JSONObject json) {

		mListData = new ArrayList<bounsItem>();
		try {
			JSONArray jsonarray = json.getJSONArray("list");
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jo = jsonarray.getJSONObject(i);
				bounsItem item = new bounsItem();
				item.setTime(jo.getString("add_time"));
				item.setMoney(jo.getDouble("affect_money"));
				item.setInfo(jo.getString("info"));
				mListData.add(item);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	class bonusAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListData.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {

			if (arg1 == null) {
				arg1 = mLayoutInflater.inflate(R.layout.adapter_item_bonus, null);
			}

			TextView time = (TextView) arg1.findViewById(R.id.time);
			TextView info = (TextView) arg1.findViewById(R.id.info);

			bounsItem item = (bounsItem) mListData.get(arg0);

			time.setText(item.getTime());
			info.setText(item.getMoney() + "元");

			return arg1;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		bounsItem item = (bounsItem) mListData.get(position);

		AlertDialog.Builder builder = new Builder(peopleInfoBonusActivity.this);
		builder.setTitle("奖金记录");
		builder.setMessage(item.getInfo());
		builder.show();
	}

}
