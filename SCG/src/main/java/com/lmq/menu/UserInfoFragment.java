package com.lmq.menu;

import java.text.DecimalFormat;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.activity.MyBriberyMoney;
import com.lmq.main.activity.PeopleInfoJiaoYi_New;
import com.lmq.main.activity.loginActivity;
import com.lmq.main.activity.peopleInfoDataActivity;
import com.lmq.main.activity.peopleInfoWithdrawalActivity;
import com.lmq.main.activity.investmanager.InvestManagerStandardActivity;
import com.lmq.main.api.BaseFragment;
import com.lmq.main.api.MyLog;
import com.lmq.main.api.SystenmApi;
import com.lmq.main.listener.FragmentUpdateListener;
import com.lmq.main.util.Data;
import com.lmq.main.util.Default;
import com.lmq.pay.FyPayActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.czscg.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfoFragment extends BaseFragment implements OnClickListener {
	private View parentView;
	public static boolean mRefreshName;
	private FragmentUpdateListener mListener;
	/** 余额 */
	private TextView debit;
	/** 冻结 */
	private TextView congelation;
	/** 总额 */
	private TextView allmoney;
	/** 待收 */
	private TextView othermoney;
	private TextView MemberOrder;// 会员等级
	private TextView Username;// 账户名

	private String mPeopic;
	private ImageView mImageView;
	private SharedPreferences sp;
	private long mayuse;

	private DisplayImageOptions options;
	private Button exit_button;
	private PullToRefreshScrollView refreshView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.people_info2_new, null);

		initView(parentView);
		sp = getActivity().getSharedPreferences("user", 0);
		return parentView;

	}

	public void setListener(FragmentUpdateListener listener) {
		mListener = listener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void initView(View view) {
		view.findViewById(R.id.peopleinfo_deal).setOnClickListener(this);// 交易记录
		// view.findViewById(R.id.peopleinfo_jk).setOnClickListener(this);// 借款记录
		view.findViewById(R.id.peopleinfo_bonus).setOnClickListener(this);// 资金记录
		view.findViewById(R.id.peopleinfo_info).setOnClickListener(this);

		view.findViewById(R.id.cz).setOnClickListener(this);
		view.findViewById(R.id.tx).setOnClickListener(this);
		view.findViewById(R.id.peopleinfo_tzmanager).setOnClickListener(this);
		view.findViewById(R.id.peopleinfo_mybriberymoney).setOnClickListener(this);
		refreshView = (PullToRefreshScrollView) view.findViewById(R.id.refreshView);
		refreshView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// TODO Auto-generated method stub
				doHttpUpdatePeopleInfo();

			}
		});

		mImageView = (ImageView) view.findViewById(R.id.peo_pic);

		debit = (TextView) view.findViewById(R.id.balance);

		congelation = (TextView) view.findViewById(R.id.tv_dongjie);

		allmoney = (TextView) view.findViewById(R.id.allmoney);

		othermoney = (TextView) view.findViewById(R.id.othermoney);
		Username = (TextView) view.findViewById(R.id.peo_name);
		MemberOrder = (TextView) view.findViewById(R.id.tv_members);

		exit_button = (Button) view.findViewById(R.id.more_exit);
		exit_button.setOnClickListener(this);
		showExitBtn();

		options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.default_user_image).showImageOnFail(R.drawable.default_user_image).resetViewBeforeLoading(true).cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		// 充值
		case R.id.cz: {
			if (Default.use_fy) {
				startActivity(new Intent(getActivity(), FyPayActivity.class));
			}
		}
			break;
		// 提現
		case R.id.tx: {
			getUserBankCard();
		}
			break;
		// 账户信息
		case R.id.peopleinfo_info:

			Intent intent = new Intent(getActivity(), peopleInfoDataActivity.class);
			startActivityForResult(intent, 1);

			break;
		// 交易记录
		case R.id.peopleinfo_deal:
			getActivity().startActivity(new Intent(getActivity(), PeopleInfoJiaoYi_New.class));
			break;
		// 我的借款
		// case R.id.peopleinfo_jk:
		// startActivity(new Intent(getActivity(),
		// LendMoneyRecordActivity.class));
		// break;
		// 投资管理
		case R.id.peopleinfo_tzmanager:
			/**
			 * dzh
			 */
			startActivity(new Intent(getActivity(), InvestManagerStandardActivity.class));
			break;
		case R.id.more_exit:
			showDialog();
			break;
		case R.id.peopleinfo_mybriberymoney:
			//TODO
			Intent intent1=new Intent(getActivity(),MyBriberyMoney.class);
			startActivity(intent1);
			break;

		}

	}

	/**
	 * 退出登录按钮控制
	 */
	private void showExitBtn() {

		switch (Default.style) {

		case 0:
			if (Default.userId == 0) {
				exit_button.setVisibility(View.GONE);
			} else {
				exit_button.setVisibility(View.VISIBLE);
			}

		default:
			break;

		}
	}

	public void showDialog() {
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setTitle("退出");
		builder.setMessage("是否退出该用户");
		builder.setIcon(R.drawable.scg_logo);

		builder.setPositiveButton("退出", new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				doHttpExit();
				dialog.dismiss();
				MainTabActivit_Scg.mainTabActivity.IndexView();

			}

		});
		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	

	private void doHttpExit() {
		BaseHttpClient.post(getActivity(), Default.exit, null, new JsonHttpResponseHandler() {

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
				MyLog.d("zzx", "exit成功" + json.toString());
				try {

					if (statusCode == 200) {
						if (json.getInt("status") == 1) {
							BaseHttpClient.clearCookie();
							MyLog.d("zzx", "exit成功");
							Default.layout_type = Default.pageStyleLogin;
							Default.userId = 0;
							showExitBtn();
							// setDefaultImage();
							Data.clearInfo();
							// Intent intent = new Intent();
						} else {
							MyLog.d("zzx", "exit失败");
						}
					} else {
						MyLog.d("zzx", "exit失败");
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
			}

		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Default.LOGIN_TYPE_3) {

			if (mListener != null) {
				mListener.onFramentCallback(1, null);
			}
		}
		StringBuilder sb = new StringBuilder();
		if (resultCode == 100 || resultCode == 200) {// 注册返回
			sb.append("注册返回数据:");
			int code = data.getIntExtra("code", -1);
			String message = data.getStringExtra("message");
			sb.append("code:").append(code).append(",message:").append(message);
			if (code != -1) {
				String AccountNumber = data.getStringExtra("AccountNumber");
				sb.append(",AccountNumber:").append(AccountNumber);
				showCustomToast(sb.toString());
			}
			if (code == 88) {
				showCustomToast("成功绑定托管账户！");
			}
		}

	}

	public void updateUserInfo(JSONObject json) {

		try {
			Username.setText(json.getString("username"));
			MemberOrder.setText(json.getString("credits"));
			DecimalFormat d = new DecimalFormat("##0.00");
			// 设置帐户信息
			// 总额
			allmoney.setText(d.format(json.getDouble("total")) + "元");
			// 冻结
			congelation.setText(d.format(json.getDouble("freeze")) + "元");
			// 待收
			othermoney.setText(d.format(json.getDouble("collect")) + "元");
			// 余额
			mayuse = json.getLong("mayuse");
			debit.setText(d.format(json.getDouble("mayuse")) + "元");
			mPeopic = json.getString("head");
			ImageLoader.getInstance().displayImage(Default.ip + mPeopic, mImageView, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					String message = null;
					switch (failReason.getType()) {
					case IO_ERROR:
						message = "Input/Output error";
						break;
					case DECODING_ERROR:
						message = "解析错误";
						break;
					case NETWORK_DENIED:
						message = "网络错误";
						break;
					case OUT_OF_MEMORY:
						message = "内存错误";
						break;
					case UNKNOWN:
						message = "位置错误";
						break;
					}
					Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					loadedImage = SystenmApi.getRoundCornerBitmap(loadedImage, 100);
					mImageView.setImageBitmap(loadedImage);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void login(int type) {
		startActivityForResult(new Intent(getActivity(), loginActivity.class), type);
		getActivity().overridePendingTransition(R.anim.down_to_up, R.anim.to_up);

		Data.clearInfo();
	}

	public void showPeopleWithdrawalInfo() {

		BaseHttpClient.post(getActivity(), Default.peoInfoWithdrawal, null, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				showLoadingDialogNoCancle(getResources().getString(R.string.toast2));
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				try {

					if (statusCode == 200) {
						if (response.getInt("status") == 1) {
							Intent intent = new Intent(getActivity(), peopleInfoWithdrawalActivity.class);

							Data.peopleWithdrawalJson = response;

							getActivity().startActivity(intent);
							getActivity().overridePendingTransition(R.anim.right_to_left, R.anim.to_left);
						} else {
							showCustomToast(response.getString

							("message"));
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
				showCustomToast(responseString);
			}

		});

	}

	/**
	 * 获取用户绑定银行卡信息
	 */

	public void getUserBankCard() {

		BaseHttpClient.post(getActivity(), Default.peoInfoWithdrawal, null, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				showLoadingDialogNoCancle(getResources().getString(R.string.toast2));
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				try {

					if (statusCode == 200) {
						if (response.getInt("status") == 1) {
							MyLog.e("获取提现的信息", response.toString());
							// 获取到用户绑定银行卡信息
							decoidTheUesrCardInfo(response);

						} else {
							showCustomToast(response.getString("message"));
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
				showCustomToast(responseString);
			}
		});

	}

	// 解析获取到银行卡信息
	public void decoidTheUesrCardInfo(JSONObject json) {
		//
		try {
			if (json.getString("bank_num") != null && !json.getString("bank_num").equals("")) {

				Intent intent = new Intent(getActivity(), peopleInfoWithdrawalActivity.class);

				intent.putExtra("bank_num", json.getString("bank_num"));

				if (json.getString("bank_name") != null && !json.getString("bank_name").equals("")) {

					intent.putExtra("bank_name", json.getString("bank_name"));

				}
				if (json.getString("real_name") != null && !json.getString("real_name").equals("")) {

					intent.putExtra("real_name", json.getString("real_name"));
				}
				// if (json.getString("all_money") != null
				// && !json.getString("all_money").equals("")) {
				intent.putExtra("all_money", json.getString("all_money"));
				MyLog.e("提现准备传的钱数", json.getString("all_money"));
				// }
				if (json.getString("qixian") != null && !json.getString("qixian").equals("")) {

					intent.putExtra("qixian", json.getString("qixian"));
				}

				startActivity(intent);

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void doHttpUpdatePeopleInfo() {

		BaseHttpClient.post(getActivity(), Default.peoInfoUpdate, null, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				showLoadingDialogNoCancle(getResources().getString(R.string.toast2));
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);

				try {
					if (statusCode == 200) {
						if (response.getInt("status") == 1) {
							updateUserInfo(response);
						} else {
							showCustomToast(response.getString("message"));
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
				showCustomToast(responseString);
			}

		});
		refreshView.onRefreshComplete();

	}

	@Override
	public void onResume() {
		super.onResume();
		showExitBtn();
		doHttpUpdatePeopleInfo();
	}

}
