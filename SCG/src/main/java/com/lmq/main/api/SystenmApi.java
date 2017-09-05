package com.lmq.main.api;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.http.conn.util.InetAddressUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.czscg.R;
import com.lmq.main.util.Default;
import com.lmq.main.util.DesUtil;

public class SystenmApi {

	public static String[] split(String original, String regex) {
		// 取子串的起始位置
		int startIndex = 0;
		// 将结果数据先放入Vector中
		Vector v = new Vector();
		// 返回的结果字符串数组
		String[] str = null;
		// 存储取子串时起始位置
		int index = 0;
		// 获得匹配子串的位置
		startIndex = original.indexOf(regex);
		//
		// System.out.println("0" + startIndex);
		// 如果起始字符串的位置小于字符串的长度，则证明没有取到字符串末尾。
		// -1代表取到了末尾
		while (startIndex < original.length() && startIndex != -1) {
			String temp = original.substring(index, startIndex);
			// System.out.println(" " + startIndex);
			// 取子串
			v.addElement(temp);
			// 设置取子串的起始位置
			index = startIndex + regex.length();
			// 获得匹配子串的位置
			startIndex = original.indexOf(regex, startIndex + regex.length());
		}
		// 取结束的子串
		v.addElement(original.substring(index + 1 - regex.length()));
		// 将Vector对象转换成数组
		str = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			str[i] = (String) v.elementAt(i);
		}
		// 返回生成的数组
		return str;
	}

	/**
	 * 检测邮箱地址是否合法
	 *
	 * @param email
	 * @return true合法 false不合法
	 */
	public static boolean isEmail(String email) {
		if (null == email || "".equals(email)) {
			return false;
		}
		// Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
		Pattern p = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|"
				+ "(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/***
	 * 四舍五入
	 *
	 * @param ft
	 * @return
	 */

	public static float getRounding(double ft) {
		float value = 0;
		int scale = 2;// 设置位数
		if(Double.isNaN(ft)){
			return (float) 0.0;
		}
		int roundingMode = BigDecimal.ROUND_HALF_UP;// 表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
		BigDecimal bd = new BigDecimal(ft);
		bd = bd.setScale(scale, roundingMode);
		value = bd.floatValue();

		return value;
	}

	/**
	 * 移动方法
	 *
	 * @param v
	 *            需要移动的View
	 * @param startX
	 *            起始x坐标
	 * @param toX
	 *            终止x坐标
	 * @param startY
	 *            起始y坐标
	 * @param toY
	 *            终止y坐标
	 */
	public static void moveFrontBg(View v, int startX, int toX, int startY, int toY) {
		TranslateAnimation anim = new TranslateAnimation(startX, toX, startY, toY);
		anim.setDuration(200);
		anim.setFillAfter(true);
		v.startAnimation(anim);
	}

	public static String getMoneyInfo(double money) {
		String info = "";
		int type = 0;

		if (money > 9999 && money < 100000000) {
			type = 1;
			info = money / 10000 + "";
		} else if (money >= 100000000) {
			type = 2;
			info = money / 100000000 + "";
		} else {
			info = money + "";
		}

		int index = info.indexOf(".");
		if (index > 0) {
			String temp = info.substring(index + 1, info.length());
			if (temp.length() == 1) {
				if (Integer.parseInt(temp) > 0) {
					info = info.substring(0, index + 2);
				} else {
					info = info.substring(0, index);
				}
			} else {
				if (Integer.parseInt(temp.substring(0, 2)) > 0) {
					info = info.substring(0, index + 3);
				} else {
					info = info.substring(0, index);
				}
			}
		}
		if (type == 1) {
			info = info + "万";
		} else if (type == 2) {
			info = info + "亿";
		}

		return info;
	}

	/**
	 * @param context
	 *            上下文
	 * @param name
	 *            用户名
	 * @param pwd
	 *            用户密码
	 */
	public static void saveUserLoginInfo(Context context, String name, String pwd) {

		try {
			DesUtil des = new DesUtil();
			SharedPreferences sp = context.getSharedPreferences(Default.userPreferences, 0);
			Editor edit = sp.edit();

			edit.putString(Default.userName, des.encrypt(name));
			edit.putString(Default.userPassword, des.encrypt(pwd));
			edit.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 是否连接WIFI
	public static Boolean isWifiConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetworkInfo.isConnected()) {
			return true;
		}

		return false;
	}

	public static String getIPStr(Context context, boolean wifi) {

		String rtnStr = "";
		if (wifi) {

			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			// 判断wifi是否开启
			if (!wifiManager.isWifiEnabled()) {
				wifiManager.setWifiEnabled(true);
			}
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo.getIpAddress();

			rtnStr = intToIp(ipAddress);
		} else {

			try {
				for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
						.hasMoreElements();) {
					NetworkInterface intf = en.nextElement();
					for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
						InetAddress inetAddress = enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress()) {
							rtnStr = inetAddress.getHostAddress().toString();
						}
					}
				}
			} catch (SocketException ex) {
				Log.e("WifiPreference IpAddress", ex.toString());
			}

		}

		return rtnStr;
	}

	public static String GetHostIp() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr.hasMoreElements();) {
					InetAddress inetAddress = ipAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress();
					}
				}
			}
		} catch (SocketException ex) {
		} catch (Exception e) {
		}
		return null;
	}

	public static String getLocalIpAddress() {

		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {

						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException e) {
			// TODO: handle exception
			// Utils.log("WifiPreference IpAddress---error-" + e.toString());
		}

		return null;
	}

	private static String intToIp(int i) {

		return (i & 0xFF) + "." + (i >> 8 & 0xFF) + "." + (i >> 16 & 0xFF) + "." + (i >> 24 & 0xFF);
	}

	/**
	 * @param context
	 *            上下文
	 * @return returnList 0:userName 1:userPasswrod
	 */
	public static List<String> getUserSavedUserNameAndPwd(Context context) {

		List<String> returnList = null;

		SharedPreferences sp = context.getSharedPreferences(Default.userPreferences, 0);
		// 获取用户保存的信息
		String name = sp.getString(Default.userName, "");
		String pwd = sp.getString(Default.userPassword, "");
		boolean mRemember = sp.getBoolean(Default.userRemember, false);

		// 判断获取到用户把保存的信息
		if (!name.equals("") && !pwd.equals("")) {

			try {
				DesUtil des = new DesUtil();
				returnList = new ArrayList<String>();
				returnList.add(des.encrypt(name));
				returnList.add(des.encrypt(pwd));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return returnList;

	}

	/** 注册用户名判断 */
	public static int ByteLenth(String name) {
		int n = 0;
		for (int i = 0; i < name.length(); i++) {
			if (isChinese(name.charAt(i))) {
				n = n + 2;
			} else {
				n = n + 1;
			}
		}
		return n;
	}

	private static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub ==

		Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub ==

		Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub ==

		Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub ==

		Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub ==

		Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	/**
	 * 判断一个字符 是否为空或者为NULL
	 *
	 * @param str
	 *            待检验的字符串
	 * @return 返回的状态
	 */
	public static Boolean isNullOrBlank(String str) {

		boolean returnFlag = false;

		if (str == null) {
			returnFlag = true;
		} else if (str.equals("")) {

			returnFlag = true;
		}

		return returnFlag;

	}

	/**
	 * 根据要求显示用户名 其他的显示为*
	 *
	 * @param name
	 * @param index
	 * @return
	 */
	public static String showUserWithIndex(String name, int index) {
		StringBuilder returnStr = new StringBuilder();

		if (!isNullOrBlank(name)) {

			if (index < name.length()) {

				for (int i = 0; i < name.length(); i++) {

					if (i <= index) {
						returnStr.append(name.charAt(i));
					} else {
						returnStr.append("*");

					}
				}
			}

		}

		return returnStr.toString();
	}

	/**
	 * 清楚用户存储的信息
	 *
	 * @param context
	 */
	public void cleanUserSaveInfo(Context context) {

		SharedPreferences sp = context.getSharedPreferences(Default.userPreferences, 0);
		Editor edit = sp.edit();
		edit.putString(Default.userName, "");
		edit.putString(Default.userPassword, "");
		edit.putBoolean(Default.userRemember, false);
		edit.commit();
	}

	/**
	 * 图片 圆角
	 *
	 * @param bitmap
	 * @param roundPX
	 * @return
	 */
	public static Bitmap getRoundCornerBitmap(Bitmap bitmap, float roundPX) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int min = 90;
		if (width < min || height < min) {
			width = height = min;
		}

		Bitmap bitmap2 = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap2);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, width, height);
		final RectF rectF = new RectF(rect);

		paint.setColor(color);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawRoundRect(rectF, roundPX, roundPX, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return bitmap2;
	}

	/***
	 * 显示分享界面
	 *
	 * @param context
	 * @param title
	 * @param Contenttext
	 */
	public static void showShareView(Context context, String title, String Contenttext, String url) {
//		ShareSDK.initSDK(context);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		oks.setTitleUrl(url);
		oks.setTitle(title);
		oks.setText(Contenttext);
		oks.setImageUrl(Default.yu+"UF/data/logo1.png");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// URL url1 = context.getClassLoader().getResource("logo1.png");
		// oks.setImagePath(url1.getPath());// 确保SDcard下面存在此张图片
		// Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
		// R.drawable.logo1);
		// Uri uri =
		// Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(),
		// bitmap, null,null));
		// oks.setImagePath(uri.getPath());// 确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(url);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		// oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(context.getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(url);
		// activity.id);
		// 启动分享GUI
		oks.show(context);
	}

	/**
	 * 获取版本号
	 *
	 * @return 当前应用的版本号
	 */
	public static String getVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 判断手机格式是否正确
	 *
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {

//		String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";

		String regExp = "^1\\d{10}$";

//		String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

//		String telRegex = "13\\d{9}|14[57]\\d{8}|15[012356789]\\d{8}|18[01256789]\\d{8}|17[0678]\\d{8}";
		if (TextUtils.isEmpty(mobiles)) {
			return false;
		} else {
			return mobiles.matches(regExp);
		}
	}

	/**
	 * 验证身份证号是否符合规则
	 * 
	 * @param text
	 *            身份证号
	 * @return
	 */
	public static boolean personIdValidation(String text) {
		String regx = "[0-9]{17}x";
		String reg1 = "[0-9]{15}";
		String regex = "[0-9]{18}";
		return text.matches(regx) || text.matches(reg1) || text.matches(regex);
	}

	public static ActivityInfo getBrowserApp(Context context) {
		String default_browser = "android.intent.category.DEFAULT";
		String browsable = "android.intent.category.BROWSABLE";
		String view = "android.intent.action.VIEW";

		Intent intent = new Intent(view);
		intent.addCategory(default_browser);
		intent.addCategory(browsable);
		Uri uri = Uri.parse("http://");
		intent.setDataAndType(uri, null);

		// 找出手机当前安装的所有浏览器程序
		List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(intent,
				PackageManager.GET_INTENT_FILTERS);
		if (resolveInfoList.size() > 0) {
			ActivityInfo activityInfo = resolveInfoList.get(0).activityInfo;
			return activityInfo;
		} else {
			return null;
		}
	}

	public static void hiddenKeyBoard(Activity context) {

		((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
				context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

}
