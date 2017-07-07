package com.lmq.main.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.api.MyLog;
import com.lmq.main.util.ADInfo;
import com.lmq.main.util.Default;
import com.lmq.menu.tab.FwhtFragment;
import com.lmq.menu.tab.TpjlFragment;
import com.lmq.menu.tab.ZcjlFragment;
import com.lmq.menu.tab.ZcjsFragment;
import com.lmq.view.CycleViewPager;
import com.lmq.view.CycleViewPager.ImageCycleViewListener;
import com.lmq.view.ViewFactory;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.czscg.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

//众筹详情
public class ZcItem_Sx extends FragmentActivity implements OnClickListener {

	private List<ImageView> views = new ArrayList<ImageView>();
	private List<ADInfo> infos = new ArrayList<ADInfo>();
	private CycleViewPager cycleViewPager;
	TextView zcxq_zcjs, zcxq_zcjl, zcxq_tpjl, zcjl_fwht, title;
	TextView zcxq_title, zcxq_zcje, zcxq_ycje, zcxq_yjqx, zc_qtje, zc_dbxe, Zc_czrs, zc_sysj, zc_fbsj, zcxq_jindu,
			zc_item_ljrg;
	ZcjsFragment zcjs_fragment;
	ZcjlFragment zcjl_fragment;
	TpjlFragment tpjl_fragment;
	FwhtFragment fwht_Fragment;
	Fragment zcxq_fragment;
	ImageView title_right;
	ScrollView zcxq_scroll;
	String id = "";
	String progress = "";
	ProgressBar zcxq_progress;
	String imgs = "";
	String[] strings;
	String raise_info_str = "", log_zcjl = "";
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	Map<String, Object> map;
	String raise_status;
	String vote_str;
	Handler handler = new Handler();
	// http://blog.csdn.net/stevenhu_223/article/details/45577781
	private String[] imageUrls = { "http://img.taodiantong.cn/v55183/infoimg/2013-07/130720115322ky.jpg",
			"http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg",
			"http://pic18.nipic.com/20111215/577405_080531548148_2.jpg",
			"http://pic15.nipic.com/20110722/2912365_092519919000_2.jpg",
			"http://pic.58pic.com/58pic/12/64/27/55U58PICrdX.jpg" };
	protected String zfpwd_str;
	protected String tbmoney_str;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zcitem_sx);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		zcxq_zcjs = (TextView) findViewById(R.id.zcxq_zcjs);
		zcxq_zcjs.setOnClickListener(this);
		zcxq_zcjl = (TextView) findViewById(R.id.zcxq_zcjl);
		zcxq_zcjl.setOnClickListener(this);
		zcxq_tpjl = (TextView) findViewById(R.id.zcxq_tpjl);
		zcxq_tpjl.setOnClickListener(this);
		zcjl_fwht = (TextView) findViewById(R.id.zcjl_fwht);
		zcjl_fwht.setOnClickListener(this);
		title_right = (ImageView) findViewById(R.id.title_right);
		title_right.setVisibility(View.VISIBLE);
		title_right.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		title.setText("众筹详情");
		zcxq_scroll = (ScrollView) findViewById(R.id.zcxq_scroll);
		zcxq_progress = (ProgressBar) findViewById(R.id.zcxq_progress);
		// TextView
		// zcxq_title,zcxq_zcje,zcxq_ycje,zcxq_yjqx,Zc_zcje,Zc_ycje,Zc_czrs,zc_sysj,zc_fbsj;
		zcxq_title = (TextView) findViewById(R.id.zcxq_title);
		zcxq_zcje = (TextView) findViewById(R.id.zcxq_zcje);
		zcxq_ycje = (TextView) findViewById(R.id.zcxq_ycje);
		zcxq_yjqx = (TextView) findViewById(R.id.zcxq_yjqx);
		zc_qtje = (TextView) findViewById(R.id.zc_qtje);
		zc_dbxe = (TextView) findViewById(R.id.zc_dbxe);
		Zc_czrs = (TextView) findViewById(R.id.Zc_czrs);
		zc_sysj = (TextView) findViewById(R.id.zc_sysj);
		zc_fbsj = (TextView) findViewById(R.id.zc_fbsj);
		zcxq_jindu = (TextView) findViewById(R.id.zcxq_jindu);
		zc_item_ljrg = (TextView) findViewById(R.id.zc_item_ljrg);
		zc_item_ljrg.setOnClickListener(this);
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		progress = intent.getStringExtra("progress");
		zcxq_progress.setProgress((int) Double.parseDouble(progress));
		zcxq_jindu.setText("进度" + progress + "%");
		configImageLoader();
		// initFragment();

	}

	private void inputTitleDialog() {
		LayoutInflater mInflater = null;
		mInflater = LayoutInflater.from(ZcItem_Sx.this);
		View layout = mInflater.inflate(R.layout.zc_diydialog, null);

		final EditText zfpwd = (EditText) layout.findViewById(R.id.zc_tbje);
		final EditText dxb_zfpwd = (EditText) layout.findViewById(R.id.zc_zfpwd);

		// if (is_direction.equals("0")) {
		// layout.findViewById(R.id.layout_dxb).setVisibility(View.GONE);
		// }
		// final EditText inputServer = new EditText(this);
		// inputServer.setFocusable(true);
		// inputServer.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("请输入支付密码").setIcon(R.drawable.scg_logo).setView(layout).setNegativeButton("取消", null);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				zfpwd_str = dxb_zfpwd.getText().toString();
				tbmoney_str = zfpwd.getText().toString();
				Toast.makeText(ZcItem_Sx.this, zfpwd_str + tbmoney_str, 1).show();
				doZcTbHttp(zfpwd_str, tbmoney_str);
			}
		});
		builder.show();
	}

	// 首次请求
	public void doZcTbHttp(String pwd, String money) {
		JsonBuilder builder = new JsonBuilder();
		builder.put("id", id);
		builder.put("password", pwd);
		builder.put("money", money);
		// 填充参数
		BaseHttpClient.post(ZcItem_Sx.this, Default.zcListtb, builder, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

				// showLoadingDialogNoCancle("请稍后努力加载中...");
				// Toast.makeText(ZcItem_Sx.this, "请稍后努力加载中...", 1).show();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, json);
				MyLog.e("statusCode=" + statusCode + "--headers=" + headers.toString() + "--json=" + json.toString());

				if (statusCode == 200) {
					try {
						if (json.getInt("status") == 1) {
							// initJsonData(json);
						} else {
							Toast.makeText(ZcItem_Sx.this, json.getString("message"), 1).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});

	}

	// 首次请求
	public void doZcxqHttp() {
		JsonBuilder builder = new JsonBuilder();
		builder.put("id", id);

		// 填充参数
		BaseHttpClient.post(ZcItem_Sx.this, Default.zcListxq, builder, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

				// showLoadingDialogNoCancle("请稍后努力加载中...");
				// Toast.makeText(ZcItem_Sx.this, "请稍后努力加载中...", 1).show();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, json);
				MyLog.e("statusCode=" + statusCode + "--headers=" + headers.toString() + "--json=" + json.toString());

				if (statusCode == 200) {
					try {
						if (json.getInt("status") == 1) {
							initJsonData(json);
						} else {
							// Toast.makeText(ZcItem_Sx.this,
							// json.getString("message"), 1).show();
						}
						Toast.makeText(ZcItem_Sx.this, json.getString("message"), 1).show();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});

	}

	/*
	 * 将时间戳转换为时间
	 */
	public static String stampToDate(String s) {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		long lt = new Long(s + "000");
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}

	public static String codeToName(String code) {
		String name = "";
		switch (Integer.parseInt(code)) {
		case 1:
			name = "预热中";
			break;
		case 3:
			name = "立即认购";
			break;
		case 4:
			name = "复审中";
			break;
		case 5:
			name = "出售中";
			break;
		case 6:
			name = "投票中";
			break;
		case 7:
			name = "审核投票";
			break;
		case 8:
			name = "流标";
			break;
		case 9:
			name = "回款中";
			break;
		case 10:
			name = "已完成";
			break;

		default:
			break;
		}
		return name;
	}

	protected void initJsonData(JSONObject json) {
		// TODO Auto-generated method stub
		// TextView
		// zcxq_title,zcxq_zcje,zcxq_ycje,zcxq_yjqx,Zc_zcje,Zc_ycje,Zc_czrs,zc_sysj,zc_fbsj;
		try {
			String infor = json.getString("infor");
			JSONObject jsonObject = new JSONObject(infor);
			zcxq_title.setText(jsonObject.optString("raise_name"));
			zcxq_zcje.setText(jsonObject.optString("raise_money") + "(元)");
			zcxq_ycje.setText(jsonObject.optString("have_money") + "(元)");
			zcxq_yjqx.setText(jsonObject.optString("own_date") + "(天)");
			zc_qtje.setText(jsonObject.optString("invest_min") + "(元)");
			zc_dbxe.setText(jsonObject.optString("invest_max") + "(元)");
			Zc_czrs.setText(jsonObject.optString("people") + "(人)");
			raise_info_str = (jsonObject.optString("raise_info"));
			// 倒计时运算
			// 开始时间-现在时间（start_time-nowtime）
			Long starttime = Long.parseLong(jsonObject.optString("start_time") + "000");
			Long nowTime = new Date().getTime();
			long time = starttime - nowTime;
			MyCountDownTimer mc = new MyCountDownTimer(time, 1000);
			mc.start();
			zc_fbsj.setText(stampToDate(jsonObject.optString("add_time")));
			// 轮播图
			imgs = jsonObject.optString("info_image");
			strings = imgs.split(",");
			initialize();

			raise_status = jsonObject.optString("raise_status");

			// 设置按钮状态
			zc_item_ljrg.setText(codeToName(raise_status));
			log_zcjl = json.optString("log");

			vote_str = json.optString("vote");

			initFragment(raise_info_str);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initFragment(String str) {
		// TODO Auto-generated method stub
		zcjs_fragment = new ZcjsFragment();
		zcjl_fragment = new ZcjlFragment();
		tpjl_fragment = new TpjlFragment();
		fwht_Fragment = new FwhtFragment();
		Bundle bundle = new Bundle();
		bundle.putString("zcjs_str", str);
		zcjs_fragment.setArguments(bundle);
		getSupportFragmentManager().beginTransaction().replace(R.id.zcxq_frame, zcjs_fragment).commit();

	}

	private void configImageLoader() {
		// TODO Auto-generated method stub

		// 初始化ImageLoader
		@SuppressWarnings("deprecation")
		DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.activityloading) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.activityloading) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.activityloading) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build(); // 创建配置过得DisplayImageOption对象

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.defaultDisplayImageOptions(options).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);

	}

	@SuppressLint("NewApi")
	private void initialize() {

		cycleViewPager = (CycleViewPager) getFragmentManager().findFragmentById(R.id.fragment_cycle_viewpager_content);

		// for (int i = 0; i < imageUrls.length; i++) {
		// ADInfo info = new ADInfo();
		// info.setUrl(imageUrls[i]);
		// info.setContent("图片-->" + i);
		// infos.add(info);
		// }

		for (int i = 0; i < strings.length; i++) {
			ADInfo info = new ADInfo();
			info.setUrl(Default.yu + strings[i]);
			info.setContent("图片-->" + i);
			infos.add(info);
		}

		// 将最后一个ImageView添加进来
		views.add(ViewFactory.getImageView(this, infos.get(infos.size() - 1).getUrl()));
		for (int i = 0; i < infos.size(); i++) {
			views.add(ViewFactory.getImageView(this, infos.get(i).getUrl()));
		}
		// 将第一个ImageView添加进来
		views.add(ViewFactory.getImageView(this, infos.get(0).getUrl()));

		// 设置循环，在调用setData方法前调用
		cycleViewPager.setCycle(true);

		// 在加载数据前设置是否循环
		cycleViewPager.setData(views, infos, mAdCycleViewListener);
		// 设置轮播
		cycleViewPager.setWheel(true);

		// 设置轮播时间，默认5000ms
		cycleViewPager.setTime(2000);
		// 设置圆点指示图标组居中显示，默认靠右
		cycleViewPager.setIndicatorCenter();
	}

	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

		@Override
		public void onImageClick(ADInfo info, int position, View imageView) {
			if (cycleViewPager.isCycle()) {
				position = position - 1;
				Toast.makeText(ZcItem_Sx.this, "position-->" + info.getContent(), Toast.LENGTH_SHORT).show();
			}

		}

	};

	@Override
	public void onClick(View v) {
		zcxq_zcjs.setBackgroundResource(R.drawable.btn_clear_bk);
		zcxq_zcjl.setBackgroundResource(R.drawable.btn_clear_bk);
		zcxq_tpjl.setBackgroundResource(R.drawable.btn_clear_bk);
		zcjl_fwht.setBackgroundResource(R.drawable.btn_clear_bk);
		switch (v.getId()) {
		case R.id.zcxq_zcjs:
			zcxq_zcjs.setBackgroundResource(R.drawable.btn_bk);
			Bundle bundle = new Bundle();
			bundle.putString("zcjs_str", raise_info_str);
			zcjs_fragment.setArguments(bundle);
			getSupportFragmentManager().beginTransaction().replace(R.id.zcxq_frame, zcjs_fragment).commit();
			handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					zcxq_scroll.fullScroll(ScrollView.FOCUS_DOWN);
				}
			});
			break;
		case R.id.zcxq_zcjl:
			zcxq_zcjl.setBackgroundResource(R.drawable.btn_bk);
			Bundle bundle1 = new Bundle();
			bundle1.putString("log_zcjl", log_zcjl);
			zcjl_fragment.setArguments(bundle1);
			getSupportFragmentManager().beginTransaction().replace(R.id.zcxq_frame, zcjl_fragment).commit();
			handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					zcxq_scroll.fullScroll(ScrollView.FOCUS_DOWN);
				}
			});
			break;
		case R.id.zcxq_tpjl:
			zcxq_tpjl.setBackgroundResource(R.drawable.btn_bk);
			Bundle bundle2 = new Bundle();
			bundle2.putString("raise_status", raise_status);
			bundle2.putString("vote_str", vote_str);
			bundle2.putString("id", id);
			tpjl_fragment.setArguments(bundle2);

			getSupportFragmentManager().beginTransaction().replace(R.id.zcxq_frame, tpjl_fragment).commit();
			handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					zcxq_scroll.fullScroll(ScrollView.FOCUS_DOWN);
				}
			});
			break;
		case R.id.zcjl_fwht:
			zcjl_fwht.setBackgroundResource(R.drawable.btn_bk);
			getSupportFragmentManager().beginTransaction().replace(R.id.zcxq_frame, fwht_Fragment).commit();
			break;
		case R.id.title_right:
			finish();
			break;
		case R.id.zc_item_ljrg:
			if (Default.userId == 0) {
				Toast.makeText(ZcItem_Sx.this, "请先登录", Toast.LENGTH_LONG).show();
			} else {
				inputTitleDialog();
			}

			break;

		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		doZcxqHttp();
	}

	// http://www.jb51.net/article/52696.htm
	class MyCountDownTimer extends CountDownTimer {

		public MyCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			zc_sysj.setText(secToTime((int) millisUntilFinished / 1000));
			// Log.e("onTick",secToTime((int)millisUntilFinished/1000));
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			zc_sysj.setText("开始啦");
		}

	}

	public static String secToTime(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		int day = 0;
		if (time <= 0)
			return "00:00";
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = unitFormat(minute) + ":" + unitFormat(second);
			} else {
				hour = minute / 60;
				// if (hour > 99)
				// return "99:59:59";

				if (hour < 24) {
					minute = minute % 60;
					second = time - hour * 3600 - minute * 60;
					timeStr = unitFormat(hour) + "时" + unitFormat(minute) + "分" + unitFormat(second) + "秒";
				} else {
					day = hour / 24;
					minute = minute % 60;
					second = time - hour * 3600 - minute * 60;
					hour = hour % 24;
					timeStr = unitFormat(day) + "天" + unitFormat(hour) + "时" + unitFormat(minute) + "分"
							+ unitFormat(second) + "秒";
				}
			}
		}
		return timeStr;
	}

	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}

}
