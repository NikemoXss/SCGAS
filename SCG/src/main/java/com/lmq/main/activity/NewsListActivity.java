/**
 *
 */
package com.lmq.main.activity;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.item.newsItem;
import com.lmq.main.util.Data;
import com.lmq.main.util.Default;
import com.lmq.view.MyListView;
import com.lmq.view.MyListView.LoadMoreInfo;

/**
 * 网站公告
 */
public class NewsListActivity extends BaseActivity implements OnClickListener {

	private MyListView mListView;
	private LayoutInflater mInflater;
	private newsAdapter adapter;

	private int curPage, maxPage;
	private int pageCount = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_list);

		findViewById(R.id.back).setOnClickListener(this);
		TextView title = (TextView) findViewById(R.id.textView1);
		title.setText("公告");
		if (Data.NewsList == null || Data.NewsList.size() == 0) {
			Data.NewsList.clear();
			JsonBuilder builder = new JsonBuilder();
			builder.put("limit", pageCount);
			builder.put("page", "1");
			doHttp(builder);
		}

		initView();
		if (Default.showNewsList) {
			handler.sendEmptyMessage(3);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish(0);
			break;
		}
	}

	@Override
	public void finish() {
		super.finish();
	}

	protected void initView() {
		mListView = (MyListView) findViewById(R.id.listview);
		mInflater = LayoutInflater.from(this);
		adapter = new newsAdapter();
		mListView.setAdapter(adapter);
		mListView.setDividerHeight(0);
		mListView.showFootView(true);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(NewsListActivity.this, itemNewsActivity.class);
				Default.showNewsId = ((newsItem) Data.NewsList.get(arg2 - 1)).getId();
				NewsListActivity.this.startActivity(intent);
			}
		});

		mListView.setOnLoadMoreInfo(new LoadMoreInfo() {
			@Override
			public void onRefresh() {

				Data.NewsList.clear();
				JsonBuilder builder = new JsonBuilder();
				builder.put("limit", pageCount);
				builder.put("page", 1);
				doHttp(builder);
			}

			@Override
			public void onLoadMore() {
				if (curPage + 1 <= maxPage) {
					curPage++;
					JsonBuilder builder = new JsonBuilder();
					builder.put("limit", pageCount);
					builder.put("page", curPage);
					doHttp(builder);
				} else {
					handler.sendEmptyMessage(1);
				}
			}
		});

	}

	public void initData(JSONObject json) {
		JSONArray list = null;
		try {
			if (json.has("list") && !json.isNull("list")) {
				list = json.getJSONArray("list");
			}
			if (list != null) {
				for (int i = 0; i < list.length(); i++) {
					JSONObject temp = list.getJSONObject(i);
					newsItem item = new newsItem(temp);

					Data.NewsList.add(item);
				}
			}

			maxPage = json.getInt("totalPage");
			curPage = json.getInt("nowPage");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		adapter.notifyDataSetChanged();
	}

	class newsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (Data.NewsList == null) {
				return 0;
			}
			return Data.NewsList.size();
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
				arg1 = mInflater.inflate(R.layout.adapter_item_news, null);
			}
			{
				TextView type = (TextView) arg1.findViewById(R.id.name);
				TextView info = (TextView) arg1.findViewById(R.id.time);

				newsItem item = (newsItem) Data.NewsList.get(arg0);

				type.setText(item.getName());
				info.setText(item.getTime());
			}
			return arg1;
		}
	}

	public void doHttp(JsonBuilder builder) {

		BaseHttpClient.post(getBaseContext(), Default.news, builder, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				showLoadingDialogNoCancle(getResources().getString(R.string.toast2));
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, json);

				mListView.onRefreshComplete();
				mListView.onLoadMoreComplete();

				try {

					if (statusCode == 200) {
						if (json.getInt("status") == 1) {
							initData(json);
						} else {
							showCustomToast(json.getString("message"));
						}
					} else {
						showCustomToast(R.string.toast1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				dismissLoadingDialog();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				dismissLoadingDialog();
				mListView.onRefreshComplete();
				mListView.onLoadMoreComplete();
				showCustomToast(responseString);
			}
		});
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				mListView.onRefreshComplete();
				mListView.onLoadMoreComplete();
				showCustomToast("无更多数据！");
			}
			if (msg.what == 3) {
				Intent intent = new Intent(NewsListActivity.this, itemNewsActivity.class);
				intent.putExtra("id", Default.showNewsId);
				NewsListActivity.this.startActivity(intent);
				Default.showNewsList = false;
			}
		}

	};
}
