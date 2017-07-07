package com.lmq.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.czscg.R;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

/**
 * LruCache的流程分析: 我们从第一次进入应用的情况下开始 1 依据图片的Url从LruCache缓存中取图片.
 * 若图片存在缓存中,则显示该图片;否则显示默认图片 2 因为是第一次进入该界面所以会执行: loadBitmaps(firstVisibleItem,
 * visibleItemCount); 我们从loadBitmaps()方法作为切入点,继续往下梳理 3
 * 尝试从LruCache缓存中取图片.如果在显示即可,否则进入4 4 开启一个异步任务下载图片.下载完成后显示图片,并且将 该图片存入LruCache缓存中
 * 
 * 在停止滑动时,会调用loadBitmaps(firstVisibleItem, visibleItemCount) 情况与上类似
 */
@SuppressLint("NewApi")
public class GridViewAdapter extends ArrayAdapter<String> {

	private GridView mGridView;
	// 图片缓存类
	private LruCache<String, Bitmap> mLruCache;
	// 记录所有正在下载或等待下载的任务
	private HashSet<DownloadBitmapAsyncTask> mDownloadBitmapAsyncTaskHashSet;
	// GridView中可见的第一张图片的下标
	private int mFirstVisibleItem;
	// GridView中可见的图片的数量
	private int mVisibleItemCount;
	// 记录是否是第一次进入该界面
	private boolean isFirstEnterThisActivity = true;

	String[] str;

	public GridViewAdapter(Context context, int textViewResourceId, String[] objects, GridView gridView) {
		super(context, textViewResourceId, objects);
		str = objects;
		mGridView = gridView;
		mGridView.setOnScrollListener(new ScrollListenerImpl());

		mDownloadBitmapAsyncTaskHashSet = new HashSet<DownloadBitmapAsyncTask>();

		// 获取应用程序最大可用内存
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		// 设置图片缓存大小为maxMemory的1/6
		int cacheSize = maxMemory / 6;

		mLruCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getByteCount();
			}
		};

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String url = getItem(position);
		View view;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.xxpl_item, null);
		} else {
			view = convertView;
		}
		ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
		// 为该ImageView设置一个Tag,防止图片错位
		imageView.setTag(url);
		// 为该ImageView设置显示的图片
		setImageForImageView(url, imageView);
		return view;
	}

	/**
	 * 为ImageView设置图片(Image) 1 从缓存中获取图片 2 若图片不在缓存中则为其设置默认图片
	 */
	private void setImageForImageView(String imageUrl, ImageView imageView) {
		Bitmap bitmap = getBitmapFromLruCache(imageUrl);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			imageView.setImageResource(R.drawable.scg_logo_small);
		}
	}

	/**
	 * 将图片存储到LruCache
	 */
	public void addBitmapToLruCache(String key, Bitmap bitmap) {
		if (getBitmapFromLruCache(key) == null) {
			mLruCache.put(key, bitmap);
		}
	}

	/**
	 * 从LruCache缓存获取图片
	 */
	public Bitmap getBitmapFromLruCache(String key) {
		return mLruCache.get(key);
	}

	/**
	 * 为GridView的item加载图片
	 * 
	 * @param firstVisibleItem
	 *            GridView中可见的第一张图片的下标
	 * 
	 * @param visibleItemCount
	 *            GridView中可见的图片的数量
	 * 
	 */
	private void loadBitmaps(int firstVisibleItem, int visibleItemCount) {
		try {
			for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
				String imageUrl = str[i];
				Bitmap bitmap = getBitmapFromLruCache(imageUrl);
				if (bitmap == null) {
					DownloadBitmapAsyncTask downloadBitmapAsyncTask = new DownloadBitmapAsyncTask();
					mDownloadBitmapAsyncTaskHashSet.add(downloadBitmapAsyncTask);
					downloadBitmapAsyncTask.execute(imageUrl);
				} else {
					// 依据Tag找到对应的ImageView显示图片
					ImageView imageView = (ImageView) mGridView.findViewWithTag(imageUrl);
					if (imageView != null && bitmap != null) {
						imageView.setImageBitmap(bitmap);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取消所有正在下载或等待下载的任务
	 */
	public void cancelAllTasks() {
		if (mDownloadBitmapAsyncTaskHashSet != null) {
			for (DownloadBitmapAsyncTask task : mDownloadBitmapAsyncTaskHashSet) {
				task.cancel(false);
			}
		}
	}

	private class ScrollListenerImpl implements OnScrollListener {
		/**
		 * 
		 * 我们的本意是通过onScrollStateChanged获知:每次GridView停止滑动时加载图片 但是存在一个特殊情况:
		 * 当第一次入应用的时候,此时并没有滑动屏幕的操作即不会调用onScrollStateChanged,但应该加载图片.
		 * 所以在此处做一个特殊的处理. 即代码: if (isFirstEnterThisActivity && visibleItemCount
		 * > 0) { loadBitmaps(firstVisibleItem, visibleItemCount);
		 * isFirstEnterThisActivity = false; }
		 * 
		 * ------------------------------------------------------------
		 * 
		 * 其余的都是正常情况. 所以我们需要不断保存:firstVisibleItem和visibleItemCount
		 * 从而便于中在onScrollStateChanged()判断当停止滑动时加载图片
		 * 
		 */
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			mFirstVisibleItem = firstVisibleItem;
			mVisibleItemCount = visibleItemCount;
			if (isFirstEnterThisActivity && visibleItemCount > 0) {
				loadBitmaps(firstVisibleItem, visibleItemCount);
				isFirstEnterThisActivity = false;
			}
		}

		/**
		 * GridView停止滑动时下载图片 其余情况下取消所有正在下载或者等待下载的任务
		 */
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == SCROLL_STATE_IDLE) {
				loadBitmaps(mFirstVisibleItem, mVisibleItemCount);
			} else {
				cancelAllTasks();
			}
		}

	}

	/**
	 * 下载图片的异步任务
	 */
	class DownloadBitmapAsyncTask extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub

			super.onPostExecute(result);
			// 下载完成后,找到其对应的ImageView显示图片
			ImageView imageView = (ImageView) mGridView.findViewById(R.id.image);
			if (imageView != null && result != null) {
				imageView.setImageBitmap(result);
			}
			mDownloadBitmapAsyncTaskHashSet.remove(this);

		}

		@Override
		protected Bitmap doInBackground(String... params) {
			String imageUrl;
			imageUrl = params[0];
			Bitmap bitmap = downloadBitmap(params[0]);
			if (bitmap != null) {
				// 下载完后,将其缓存到LrcCache
				addBitmapToLruCache(params[0], bitmap);
			}
			return bitmap;

		}

	}

	// 获取Bitmap
	private Bitmap downloadBitmap(String imageUrl) {
		Bitmap bitmap = null;
		HttpURLConnection httpURLConnection = null;
		try {
			URL url = new URL(imageUrl);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setConnectTimeout(5 * 1000);
			httpURLConnection.setReadTimeout(10 * 1000);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
		return bitmap;
	}

}