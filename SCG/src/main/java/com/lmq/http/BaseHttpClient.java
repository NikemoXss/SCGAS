package com.lmq.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import com.lmq.main.api.JsonBuilder;
import com.lmq.main.api.MyLog;
import com.lmq.main.util.Default;
import com.lmq.menu.MainTabActivit_Scg;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.widget.Toast;

public class BaseHttpClient {

	// 网络请求URL
	// private static final String BASE_URL = "http://ios.jinrongdai.com";

	public static AsyncHttpClient httpClient = new AsyncHttpClient();

	/**
	 * doGet请求
	 *
	 * @param url
	 *            请求URL
	 * @param params
	 *            请求参数
	 * @param handler
	 *            回调函数
	 */
	public static void get(Context context, String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
		if (NetWorkStatusBroadcastReceiver.netWorkAviable) {
			httpClient.get(getAbsoluteUrl(url), params, responseHandler);
		} else {

			Toast.makeText(context, "您的网络连接已中断,请检查你的网络设置", Toast.LENGTH_SHORT).show();
		}

	}
	
	public static void getL(Context context, String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
		if (NetWorkStatusBroadcastReceiver.netWorkAviable) {
			httpClient.get(url, params, responseHandler);
		} else {

			Toast.makeText(context, "您的网络连接已中断,请检查你的网络设置", Toast.LENGTH_SHORT).show();
		}

	}

	public static void normalDoPost(Context context, String url, RequestParams requestParams,
			AsyncHttpResponseHandler responseHandler) {

		if (NetWorkStatusBroadcastReceiver.netWorkAviable) {
			httpClient.post(url, requestParams, responseHandler);
		} else {

			Toast.makeText(context, "您的网络连接已中断,请检查你的网络设置", Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * @param context
	 * @param url
	 *            请求接口地址
	 * @param json
	 *            请求服务器需要传递的参数 格式为JSON格式数组
	 * @param responseHandler
	 *            对服务器请求的回调函数 直接解完成的JSON数据
	 */

	public static void post(Context context, String url, JsonBuilder json, JsonHttpResponseHandler responseHandler) {

		// saveCookie();
		StringEntity stringEntity = null;
		MyLog.d("http", "http-url=" + getAbsoluteUrl(url));
		if (json == null) {
			json = new JsonBuilder();
		}
		// json.put("uid", Default.userId);
		MyLog.d("http", "http-send=" + json.toJsonString().toString());
		try {
			stringEntity = new StringEntity(json.toJsonString().toString(), HTTP.UTF_8);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (NetWorkStatusBroadcastReceiver.netWorkAviable) {
			httpClient.post(context, getAbsoluteUrl(url), stringEntity, "application/json;charset=UTF-8",
					responseHandler);
		} else {

			Toast.makeText(context, "您的网络连接已中断,请检查你的网络设置", Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * @param context
	 * @param downloadUrl
	 *            下载地址
	 * @param responseHandler
	 *            回调函数
	 */

	public static void downloadFile(Context context, String downloadUrl, FileAsyncHttpResponseHandler responseHandler) {
		if (NetWorkStatusBroadcastReceiver.netWorkAviable) {
			httpClient.get(downloadUrl, responseHandler);
		} else {
			Toast.makeText(context, "您的网络连接已中断,请检查你的网络设置", Toast.LENGTH_SHORT).show();
		}

	}

	public static File createTempFile(String namePart, int byteSize, Context context) {
		try {
			File f = File.createTempFile(namePart, ".apk", context.getFilesDir());
			FileOutputStream fos = new FileOutputStream(f);
			Random r = new Random();
			byte[] buffer = new byte[byteSize];
			r.nextBytes(buffer);
			fos.write(buffer);
			fos.flush();
			fos.close();
			return f;
		} catch (Throwable t) {
			MyLog.e("BaseClient", "createTempFile failed", t);
		}
		return null;
	}

	public static void saveCookie() {
		PersistentCookieStore cookieStore = new PersistentCookieStore(MainTabActivit_Scg.mainTabActivity);
		httpClient.setCookieStore(cookieStore);
	}

	public static List<Cookie> getCookie() {
		PersistentCookieStore cookieStore = new PersistentCookieStore(MainTabActivit_Scg.mainTabActivity);
		List<Cookie> cookies = cookieStore.getCookies();
		return cookies;
	}

	public static void clearCookie() {
		PersistentCookieStore cookieStore = new PersistentCookieStore(MainTabActivit_Scg.mainTabActivity);
		cookieStore.clear();
	}

	/**
	 * @param url
	 *            接口地址
	 * @return
	 */
	private static String getAbsoluteUrl(String url) {

		return Default.ip + url;
	}

	//http://blog.csdn.net/ljcitworld/article/details/51670910
//	public void upload(String url) {  
//	    // 将bitmap转为string，并使用BASE64加密  
//	    String photo = Utils.BitmapToString(bitmap);  
//	    // 获取到图片的名字  
//	    String name = photoPath.substring(photoPath.lastIndexOf("/")).substring(1);  
//	    // new一个请求参数  
//	    RequestParams params = new RequestParams();  
//	    // 将图片和名字添加到参数中  
//	    params.put("photo", photo);  
//	    params.put("name", name);  
//	    AsyncHttpClient client = new AsyncHttpClient();  
//	    // 调用AsyncHttpClient的post方法  
//	    client.post(url, params, new AsyncHttpResponseHandler() {  
//
//			@Override
//			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//				// TODO Auto-generated method stub
//				
//			}  
//	    });  
//	} 
}
