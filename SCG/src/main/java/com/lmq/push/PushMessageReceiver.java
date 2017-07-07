package com.lmq.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baidu.android.pushservice.PushConstants;
import com.lmq.main.activity.itemNewsActivity;
import com.lmq.main.api.MyLog;
import com.lmq.main.util.Default;
import com.lmq.menu.MainTabActivit_Scg;

public class PushMessageReceiver extends BroadcastReceiver {
	public static final String TAG = "push";

	@Override
	public void onReceive(Context context, Intent intent) {
		// MyLog.d(TAG, TAG+">>> Receive intent: \r\n" + intent);
		Log.e("pushhh",TAG+">>> Receive intent: \r\n" + intent);
		if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
			Log.e("pushhh","1111");
			// 获取消息内容
			String message = intent.getExtras().getString(PushConstants.EXTRA_PUSH_MESSAGE);
			Log.e("pushhh","1111"+message);
			// 消息的用户自定义内容读取方式
			// MyLog.i(TAG, "onMessage: " + message);

			// 自定义内容的json�?
			// MyLog.d(TAG,
			// "EXTRA_EXTRA = "
			// + intent.getStringExtra(PushConstants.EXTRA_EXTRA));

			// 用户在此自定义处理消�?以下代码为demo界面展示�?

		} else if (intent.getAction().equals(PushConstants.ACTION_RECEIVE)) {
			
			// MyLog.d(TAG, "2222222");
			// 处理绑定等方法的返回数据
			// PushManager.startWork()的返回�?通过PushConstants.METHOD_BIND得到

			// 获取方法
			final String method = intent.getStringExtra(PushConstants.EXTRA_METHOD);
			// 方法返回错误码�?若绑定返回错误（�?），则应用将不能正常接收消息�?
			// 绑定失败的原因有多种，如网络原因，或access token过期�?
			// 请不要在出错时进行简单的startWork调用，这有可能导致死循环�?
			// 可以通过限制重试次数，或者在其他时机重新调用来解决�?
			int errorCode = intent.getIntExtra(PushConstants.EXTRA_ERROR_CODE, PushConstants.ERROR_SUCCESS);
			String content = "";
			if (intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT) != null) {
				// 返回内容
				content = new String(intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT));
			}
			Log.e("pushhh","222"+content);
			// 用户在此自定义处理消�?以下代码为demo界面展示�?
			// MyLog.d(TAG, "onMessage: method : " + method);
			// MyLog.d(TAG, "onMessage: result : " + errorCode);
			// MyLog.d(TAG, "onMessage: content : " + content);
			/*
			 * Toast.makeText( context, "method : " + method + "\n result: " +
			 * errorCode + "\n content = " + content, Toast.LENGTH_SHORT)
			 * .show();
			 */

		} else if (intent.getAction().equals(PushConstants.EXTRA_PUSH_MESSAGE)) {
		
			// MyLog.d(TAG, "click = intent=" + intent.toUri(0));

			// 自定义内容的json�?
			// MyLog.d(TAG,
			// "EXTRA_EXTRA = "
			// + intent.getStringExtra(PushConstants.EXTRA_EXTRA));

			if (intent.getExtras().size() < 3) {
				return;
			}
			Default.showNewsId = Long.parseLong(intent.getStringExtra("noticeid"));
			Intent aIntent = new Intent();
			MyLog.d(TAG, TAG + "3333333" + Default.isAlive);
			if (Default.isAlive) {
				aIntent.setClass(context, itemNewsActivity.class);
			} else {
				Default.showNewsList = true;
				aIntent.setClass(context, MainTabActivit_Scg.class);
			}

			Default.showNewsId = Long.parseLong(intent.getStringExtra("noticeid"));

			aIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//			String title = intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_TITLE);
//			aIntent.putExtra(PushConstants.EXTRA_NOTIFICATION_TITLE, title);
//			String content = intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT);
//			aIntent.putExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT, content);
//			aIntent.putExtra("pushFlag", true);
//			Log.e("pushhh","3333;;"+intent.getStringExtra("noticeid")+title+content);
//			context.startActivity(aIntent);

		}
	}

}
