package com.lmq.map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.czscg.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 演示覆盖物的用法
 */
public class MapActivity extends Activity implements OnClickListener {

	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	// 在地图中显示一个信息窗口，可以设置一个View作为该窗口的内容，也可以设置一个 BitmapDescriptor 作为该窗口的内容。
	private InfoWindow mInfoWindow;

	// 这三个就是地图上的小图标背景，是两种颜色同等大小的图片，一红一绿，他们两个交互显示就是闪烁的效果。不用时及时 recycle
	BitmapDescriptor icBig = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
	BitmapDescriptor icSmall = BitmapDescriptorFactory.fromResource(R.drawable.icon_markb);

	String[] loca;
	String who = "test";
	Map<String, Integer> map = new HashMap<String, Integer>();

	Timer timer;
	int blinked;
	public static final int DISPALY_TIME = 1200;
	public static final int BLINK_INTERVAL = 600;
	public static final int BLINK_DELAY = 1000;
	public static final int ZOOM_SIZE = 14;
	
//	119.985915,31.756729

	double baseLat = 31.756729;
	double baseLong = 119.985915;
	MarkerOptions marker, marker2;
	Marker mk;


	boolean customerMode = true;
	int zindex;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.map_layout);

		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);// 设置地图的缩放级别
		mBaiduMap.setMapStatus(msu);//

		// 添加地图加载监听器 ，
		mBaiduMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {

			@Override
			public void onMapLoaded() {
				// 地图加载完，刷新小图标
				timer = new Timer();
				startBlink(getLatLng(baseLat + "", baseLong + ""));
				setMapCenter(getLatLng(baseLat + "", baseLong + ""));
			}
		});

		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				Button button = new Button(getApplicationContext());// 新建一个按钮，这个就是点击小图标后显示的view
				button.setBackgroundResource(R.drawable.popup);// 给按钮设置背景
				OnInfoWindowClickListener listener = null;
				String all = marker.getTitle();
				String name = who;
				button.setText(name);// 设置点击后显示的文字
				button.setTextColor(getResources().getColor(R.color.black));
				listener = new OnInfoWindowClickListener() {
					public void onInfoWindowClick() {
						LatLng ll = marker.getPosition();
						// LatLng llNew = new LatLng(ll.latitude +
						// 0.005,ll.longitude + 0.005);
						marker.setPosition(ll);
						// 当前的图标隐藏掉(这个隐藏是全隐藏掉，没有找到只隐藏掉文字的直接方法，通过先隐藏再显示间接隐藏了文字)
						mBaiduMap.hideInfoWindow();
					}
				};
				LatLng ll = marker.getPosition();
				// InfoWindow 构造参数
				// view - InfoWindow 展示的 view
				// position - InfoWindow 显示的地理位置
				// yOffset - InfoWindow Y 轴偏移量
				// listener - InfoWindow 点击监听者
				mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
				mBaiduMap.showInfoWindow(mInfoWindow);
				return true;
			}
		});

//		findViewById(R.id.back).setOnClickListener(this);
	}

	public void startBlink(LatLng mlatlng) {
		marker = new MarkerOptions().position(mlatlng).icon(icBig).zIndex(zindex++);// 设置zindex是为图标设置一个id，为以后获取他
		marker.title(who);

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if (blinked > DISPALY_TIME) {
					timer.cancel();
					mk.remove();// 小图标移除
				} else {
					if (blinked <= 0) {
						mk = (Marker) mBaiduMap.addOverlay(marker);
					} else {
						if (blinked % 2 == 1) {
							mk.setIcon(icSmall);
						} else {
							mk.setIcon(icBig);
						}
					}
					blinked++;
				}

			}
		}, BLINK_DELAY, BLINK_INTERVAL);

	}

	public LatLng getLatLng(String latitude, String longtitude) {

		LatLng llA = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longtitude));
		return llA;

	}

	public void freshMap(String lat, String lon, String who) {
		startBlink(getLatLng(lat, lon));
		if (customerMode) {
			setMapCenter(getLatLng(lat, lon));
		}
	}

	public void setMapCenter(LatLng ll) {
		MapStatus mMapStatus = new MapStatus.Builder().target(ll).zoom(ZOOM_SIZE).build();
		// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		// 改变地图状态
		mBaiduMap.setMapStatus(mMapStatusUpdate);
	}

	/**
	 * 清除所有Overlay
	 * 
	 * @param view
	 */
	public void clearOverlay(View view) {
		mBaiduMap.clear();
	}

	/**
	 * 重新添加Overlay
	 * 
	 * @param view
	 */
	public void resetOverlay(View view) {
		clearOverlay(null);

	}

	@Override
	protected void onPause() {
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		super.onDestroy();
		mMapView.onDestroy();
		icBig.recycle();
		icSmall.recycle();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.back:
//			finish();
//			timer.cancel();
//			break;
		}
	}

}