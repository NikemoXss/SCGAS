package com.lmq.menu;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.czscg.R;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseFragment;
import com.lmq.main.api.MyLog;
import com.lmq.main.api.SystenmApi;
import com.lmq.main.listener.MenuItemListener;
import com.lmq.main.util.Data;
import com.lmq.main.util.Default;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class MenuFragment extends BaseFragment {

	private MenuItemListener menuItemListener;
	private View mainView;
	private TextView userName;
	private Button loginBtn;
	public ImageView userImage;
	private DisplayImageOptions options;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.layout_menu1, null);
		initViews(mainView);
		return mainView;
	}

	protected void initViews(View view) {
		SharedPreferences sp = getActivity().getSharedPreferences("user", 0);
		userImage = (ImageView) view.findViewById(R.id.menu_imag);
		view.findViewById(R.id.one1).setOnClickListener(menuItemListener);
		view.findViewById(R.id.one).setOnClickListener(menuItemListener);
		view.findViewById(R.id.two).setOnClickListener(menuItemListener);
		view.findViewById(R.id.three).setOnClickListener(menuItemListener);
		view.findViewById(R.id.four).setOnClickListener(menuItemListener);

		loginBtn = (Button) view.findViewById(R.id.menu_login_reg_btn);
		userName = (TextView) view.findViewById(R.id.menu_username);
		loginBtn.setOnClickListener(menuItemListener);
		view.findViewById(R.id.company_tel).setOnClickListener(menuItemListener);

		options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.default_user_image).showImageOnFail(R.drawable.default_user_image).resetViewBeforeLoading(true).cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();

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

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if (Default.userId != 0) {

			setUserImage();
		} else {
			setDefaultImage();
		}
		super.onResume();
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
							MyLog.d("zzx", "exit成功");
							Default.layout_type = Default.pageStyleLogin;
							Default.userId = 0;
							setDefaultImage();
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

	public void addMenuItemListener(MenuItemListener menuItemListener) {
		this.menuItemListener = menuItemListener;
	}

	public void setUserImage() {

		loginBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_out_bg));
		loginBtn.setText("退              出");

		ImageLoader.getInstance().displayImage(Default.ip + Default.user_photo_path, userImage, options, new SimpleImageLoadingListener() {
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
				userImage.setImageBitmap(loadedImage);
			}
		});

	}

	public void setDefaultImage() {
		loginBtn.setVisibility(View.VISIBLE);
		userName.setVisibility(View.GONE);//
		loginBtn.setText("");
		userImage.setImageDrawable(getResources().getDrawable(R.drawable.default_user_image));
		loginBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_reg_btn));

	}

}
