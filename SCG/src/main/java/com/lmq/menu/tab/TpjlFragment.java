package com.lmq.menu.tab;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.activity.ZcItem_Sx;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.api.MyLog;
import com.lmq.main.util.Default;
import com.czscg.R;

import android.R.string;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class TpjlFragment extends Fragment {

	TextView zc_oppose;
	TextView zc_support;
	ProgressBar zcxq_progress;
	ProgressBar zcxq_b_progress;
	String id;
	String jztime;
	TextView tbjl_content;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View mainView = inflater.inflate(R.layout.activity_tpjlfragment, null);

		Bundle bundle = getArguments();// 从activity传过来的Bundle
		String raise_status = bundle.getString("raise_status");
		String vote_str = bundle.getString("vote_str");
		id = bundle.getString("id");// 标的id
		// mainView.findViewById(R.id.tx_sx);
		initView(mainView, raise_status, vote_str);
		return mainView;
	}

	public void initView(View mainView, String raise_status, String vote_str) {
		zc_oppose = (TextView) mainView.findViewById(R.id.zc_oppose);
		zc_support = (TextView) mainView.findViewById(R.id.zc_support);
		zcxq_progress = (ProgressBar) mainView.findViewById(R.id.zcxq_progress);
		zcxq_b_progress = (ProgressBar) mainView.findViewById(R.id.zcxq_b_progress);
		tbjl_content = (TextView) mainView.findViewById(R.id.tbjl_content);
		try {
			if ("".equals(vote_str)) {
				zc_oppose.setText("未开始");
				zc_support.setText("未开始");
				zc_oppose.setBackgroundResource(R.drawable.btn_yj_red);
				zc_support.setBackgroundResource(R.drawable.btn_yj_red);
				return;
			}
			JSONObject jsonObject = new JSONObject(vote_str);
			String start_time = jsonObject.optString("start_time");
			String support_ticket = jsonObject.optString("support_ticket");
			String against_ticket = jsonObject.optString("against_ticket");
			String duration = jsonObject.optString("duration");
			String total_ticket = jsonObject.optString("total_ticket");
			String is_over = jsonObject.optString("is_over");
			String oppose = getProgress(total_ticket, against_ticket);
			String suppport = getProgress(total_ticket, support_ticket);
			jztime = GetTime(start_time, duration);
			String str = "买方愿出资购买次项目，请参与众筹的注册会员投票决定是否出售次项目,投票截止到" + jztime;
			tbjl_content.setText(str);
			zcxq_progress.setProgress(Integer.parseInt(suppport));// 赞成
			zcxq_b_progress.setProgress(Integer.parseInt(oppose));// 反对
			if (Integer.parseInt(raise_status) < 6) {
				zc_oppose.setText("未开始");
				zc_support.setText("未开始");
				zc_oppose.setBackgroundResource(R.drawable.btn_yj_red);
				zc_support.setBackgroundResource(R.drawable.btn_yj_red);
			} else if (Integer.parseInt(raise_status) == 6) {
				zc_oppose.setText("开始");
				zc_support.setText("开始");
				zc_oppose.setBackgroundResource(R.drawable.btn_yj);
				zc_support.setBackgroundResource(R.drawable.btn_yj);
				zc_oppose.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						doSupportOrOppose("oppose");
					}
				});

				zc_support.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						doSupportOrOppose("support");
					}
				});

			} else {
				zc_oppose.setText("已结束");
				zc_support.setText("已结束");
				zc_oppose.setBackgroundResource(R.drawable.btn_yj_red);
				zc_support.setBackgroundResource(R.drawable.btn_yj_red);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String GetTime(String start_time, String duration) {
		int he = mul(duration, "3600");
		String time = add(start_time, he+"");
		return stampToDate(time);
	}

	/*
	 * 将时间戳转换为时间
	 */
	public static String stampToDate(String s) {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long lt = new Long(s + "000");
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}

	// 乘法
	public static int mul(String value1, String value2) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return (int) b1.multiply(b2).doubleValue();
	}

	// 加法
	public static String add(String value1, String value2) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return b1.add(b2).intValue()+"";
	}

	public static String getProgress(String ze, String yc) {
		// Double.parseDouble(ze);
		// new BigDecimal(ze);
		if (yc.equals("0")) {
			return "0";
		}
		if (ze.equals(yc)) {
			return "100";
		}
		BigDecimal b1 = new BigDecimal(ze);
		BigDecimal b2 = new BigDecimal(yc);
		return b1.divide(b2, 2, RoundingMode.HALF_UP).doubleValue() + "";
	}

	// 首次请求
	public void doSupportOrOppose(String type) {
		JsonBuilder builder = new JsonBuilder();
		builder.put("Rid", id);
		builder.put("type", type);
		// 填充参数
		BaseHttpClient.post(getActivity(), Default.zcjlissupport, builder, new JsonHttpResponseHandler() {

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
						if (json.getInt("status") == 3) {
							Toast.makeText(getActivity(), json.getString("text"), 1).show();
							zc_oppose.setClickable(false);
							zc_support.setClickable(false);
							// initJsonData(json);
						} else {
							Toast.makeText(getActivity(), json.getString("message"), 1).show();
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
}
