package com.lmq.main.dialog;

import com.ant.liao.GifView;
import com.czscg.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * @author zzx
 */

public class ProgressDialog_gif {

	public Dialog mDialog;
	private AnimationDrawable animationDrawable = null;
	private TextView text;
	WebView webView;
	public ProgressDialog_gif(Context context) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.progress_view_gif, null);

//        webView = (WebView) view.findViewById(R.id.html_sx);
//        webView.setVerticalScrollbarOverlay(true);
//        //设置WebView支持JavaScript
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setDefaultTextEncodingName("utf-8");
//        String url = "file:///android_asset/bearfly.html";
//        webView.loadUrl(url);
		
		GifView gf1 = (GifView) view.findViewById(R.id.gif_sx);
		gf1.setGifImage(R.drawable.bearfly1);//gif是你要加载的gif动态图片
		mDialog = new Dialog(context, R.style.dialog);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(false);
	}

//	public void setText(String message) {
//		if (text != null) {
//			text.setText(message);
//		}
//	}

	public void show() {
		mDialog.show();
	}

	public void setCanceledOnTouchOutside(boolean cancel) {
		mDialog.setCanceledOnTouchOutside(cancel);
	}

	public void dismiss() {
		if (mDialog.isShowing()) {
			mDialog.dismiss();
//			animationDrawable.stop();
		}
	}

	public boolean isShowing() {
		return mDialog.isShowing();
	}

}
