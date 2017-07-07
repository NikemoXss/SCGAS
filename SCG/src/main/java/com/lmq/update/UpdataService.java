package com.lmq.update;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;

public class UpdataService extends Service {

	/** 安卓系统下载类 **/
	DownloadManager manager;

	/** 接收下载完的广播 **/
	DownloadCompleteReceiver receiver;

	/** 初始化下载器 **/

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

//		String urlString = intent.getStringExtra("Updata");
		// String bbmc = intent.getStringExtra("bbmc");
		String str="http://downloadpkg.apicloud.com/app/download?path=http://7xuj0x.com1.z0.glb.clouddn.com/5894408ca18fcdc2df05f4a0c8c36ec_d";

		// 调用下载
		initDownManager(/*IpUtil.ConnectIP + */str);

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onDestroy() {

		// 注销下载广播
		if (receiver != null)
			unregisterReceiver(receiver);

		super.onDestroy();
	}

	// 接受下载完成后的intent
	class DownloadCompleteReceiver extends BroadcastReceiver {

		@SuppressLint("NewApi")
		@Override
		public void onReceive(Context context, Intent intent) {

			// 判断是否下载完成的广播
			if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

				// 获取下载的文件id
				long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

				// 自动安装apk
				installAPK(manager.getUriForDownloadedFile(downId));

				// 停止服务并关闭广播
				UpdataService.this.stopSelf();
			}
		}

		/**
		 * 安装apk文件
		 */
		private void installAPK(Uri apk) {

			// 通过Intent安装APK文件
			Intent intents = new Intent();
			intents.setAction("android.intent.action.VIEW");
			// intents.addCategory("android.intent.category.DEFAULT");
			// intents.setType("application/vnd.android.package-archive");
			// intents.setData(apk);
			intents.setDataAndType(apk, "application/vnd.android.package-archive");
			intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// android.os.Process.killProcess(android.os.Process.myPid());
			// 如果不加上这句的话在apk安装完成之后点击单开会崩溃
			startActivity(intents);
		}
	}

	@SuppressLint("NewApi")
	private void initDownManager(String string) {

		manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

		receiver = new DownloadCompleteReceiver();

		// 设置下载地址
		// TODO 下载 地址
		// DownloadManager.Request down = new DownloadManager.Request(
		// Uri.parse("http://gdown.baidu.com/data/wisegame/fd84b7f6746f0b18/baiduyinyue_4802.apk"));
		Request down = new Request(Uri.parse(string));
		// 设置允许使用的网络类型，这里是移动网络和wifi都可以
		down.setAllowedNetworkTypes(Request.NETWORK_MOBILE | Request.NETWORK_WIFI);

		// 下载时，通知栏显示途中
		down.setNotificationVisibility(Request.VISIBILITY_VISIBLE);

		// 显示下载界面
		down.setVisibleInDownloadsUi(true);

		// 设置下载后文件存放的位置
		down.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "TBYW" + ".apk");
		// down.setDestinationInExternalPublicDir(
		// Environment.getExternalStorageDirectory() + "zahy/apk",
		// "zahy.apk");
		// 将下载请求放入队列
		manager.enqueue(down);

		// 注册下载广播
		registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}
}
