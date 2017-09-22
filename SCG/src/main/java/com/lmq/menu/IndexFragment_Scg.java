package com.lmq.menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.adapter.IndexTouZiAdapter;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.activity.NewsListActivity;
import com.lmq.main.api.BaseFragment;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.api.MyLog;
import com.lmq.main.util.Default;
import com.lmq.main.util.ViewPagerAdapter;
import com.lmq.view.AutoVerticalScrollTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

//苏常主页
public class IndexFragment_Scg extends BaseFragment implements OnClickListener, OnPageChangeListener {

    private int maxPage, curPage = 1, pageCount = 5;

    private ViewPagerAdapter vpAdapter;
    private List<View> views;
    private ArrayList<View> footPointViews;
    private List<String> bannerItemspic;
    private LinearLayout footpointview;
    private static final int[] pics = {R.drawable.slide};
    private ViewPager flipper;
    private int index;
    private boolean change;
    private int number = 0;
    private boolean isRunning = true;

    private IndexTouZiAdapter indexTouZiAdapter;
    List<Map<String, Object>> dlist = new ArrayList<Map<String, Object>>();
    Map<String, Object> map = null;
    private JSONArray jsonArray = null;

    private AutoVerticalScrollTextView autoVerticalScrollTextView;

    List<String> list_str;
    List<String> mStr;

    @InjectView(R.id.videoList)
    ListView videoList;
    @InjectView(R.id.refreshLayout)
    RefreshLayout refreshLayout;


    TextView new_yjnhll, bd_date, new_ljjr;

    private String[] strings1 = {"我的剑，就是你的剑!", "俺也是从石头里蹦出来得!", "我用双手成就你的梦想!", "人在塔在!", "犯我德邦者，虽远必诛!", "我会让你看看什么叫残忍!",
            "我的大刀早已饥渴难耐了!"};
    private Handler mHandler = new Handler();
    private Runnable runable = new Runnable() {

        @Override
        public void run() {
            changePic();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.layout_index, null);
        mainView.findViewById(R.id.gg_loadmore).setOnClickListener(this);

        new_yjnhll = (TextView) mainView.findViewById(R.id.new_yjnhll);
        bd_date = (TextView) mainView.findViewById(R.id.bd_date);
        new_ljjr = (TextView) mainView.findViewById(R.id.new_ljjr);


        footpointview = (LinearLayout) mainView.findViewById(R.id.footer_point);
        autoVerticalScrollTextView = (AutoVerticalScrollTextView) mainView
                .findViewById(R.id.autoVerticalScrollTextView);
        // autoVerticalScrollTextView.setText(strings[0]);

        autoVerticalScrollTextView.setOnClickListener(this);

        initView(mainView);
        mHandler.postDelayed(runable, 5000);
        ButterKnife.inject(this, mainView);
        return mainView;
    }

    public int firstVisible = 0, visibleCount = 0, totalCount = 0;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        indexTouZiAdapter = new IndexTouZiAdapter(getActivity(), dlist);
        videoList.setAdapter(indexTouZiAdapter);
        indexTouZiAdapter.notifyDataSetChanged();
        videoList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        Log.e("videoTest", "SCROLL_STATE_FLING");
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        Log.e("videoTest", "SCROLL_STATE_IDLE");
//                        autoPlayVideo(view);
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        Log.e("videoTest", "SCROLL_STATE_TOUCH_SCROLL");

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // firstVisibleItem   当前第一个可见的item
                // visibleItemCount   当前可见的item个数
                if (firstVisible == firstVisibleItem) {
                    return;
                }
                firstVisible = firstVisibleItem;
                visibleCount = visibleItemCount;
                totalCount = totalItemCount;
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore();
            }
        });

        //设置 Header 为 Material风格
        refreshLayout.setRefreshHeader(new MaterialHeader(getActivity()).setShowBezierWave(true));
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));


    }

    public void initBannerData(JSONArray array) {

        if (array != null) {
            views.clear();
            // flipper.removeAllViews();
            flipper.setBackgroundDrawable(null);
            footPointViews.clear();
            footpointview.removeAllViews();
            bannerItemspic = new ArrayList<String>();
            try {
//				JSONArray array = json.getJSONArray("list");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);

                    bannerItemspic.add(jsonObject.get("pic").toString());
                    // bannerItemspic.add(array.getString(i));

                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            for (int i = 0; i < bannerItemspic.size(); i++) {
                ImageView footView = new ImageView(MainTabActivit_Scg.mainTabActivity);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = 10;
                footView.setLayoutParams(params);

                if (i == 0) {
                    footView.setImageResource(R.drawable.b_dx1);
                } else {
                    footView.setImageResource(R.drawable.b_dx2);
                }
                footpointview.addView(footView);
                footPointViews.add(footView);

                final ImageView iv = new ImageView(MainTabActivit_Scg.mainTabActivity);
                iv.setScaleType(ScaleType.FIT_XY);
                iv.setTag(i);

                DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(pics[0])
                        .showImageOnFail(pics[0]).resetViewBeforeLoading(true).cacheOnDisk(true)
                        .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565)
                        .considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();
                Log.e("url", bannerItemspic.get(i));
                ImageLoader.getInstance().displayImage(Default.ip + bannerItemspic.get(i), iv, options,
                        new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                            }

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
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                if (loadedImage != null) {

                                    iv.setImageBitmap(loadedImage);
                                }

                            }
                        });
                views.add(iv);
            }

        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//		doHttp();
//		ggdoHttp();
//		dobHttp();
        doNewHttp();
    }


    public void doNewHttp() {

        // 填充参数
        BaseHttpClient.post(getActivity(), Default.sytznewList, null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, json);
                try {
                    if (statusCode == 200) {
                        if (json.getInt("status") == 200) {
                            //轮播图
                            JSONArray slideArray = json.getJSONArray("slide");
                            initBannerData(slideArray);
                            vpAdapter.notifyDataSetChanged();
                            //公告
                            JSONArray noticeArray = json.getJSONArray("notice");
                            initnoticeJsonData(noticeArray);
                            //新手标
                            JSONArray newArray = json.getJSONArray("new");
                            initNewPersionJsonData(newArray);
                            //推荐标
                            JSONArray listArray = json.getJSONArray("list");
                            initTzBiaoJsonData(listArray);
//							String string = json.getString("msg");
                            // int count = string.length();

                        } else {
                            showCustomToast(json.getString("message"));
                        }
                    } else {
                        showCustomToast(R.string.toast1);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                dismissLoadingDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
                dismissLoadingDialog();
            }
        });
    }

    protected void initnoticeJsonData(JSONArray jsonArray) {
//		JSONArray jsonArray = json.getJSONArray("msg");
        try {
            list_str = new ArrayList<String>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                list_str.add(jsonObject.optString("title"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Message message = new Message();
        message.obj = list_str;
        message.what = 111;
        handler.sendMessage(message);
    }



    public void ggdoHttp() {

        // 填充参数
        BaseHttpClient.post(getActivity(), Default.sygg, null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, json);

                try {
                    if (statusCode == 200) {
                        if (json.getInt("status") == 1) {
                            String string = json.getString("msg");
                            // int count = string.length();
                            JSONArray jsonArray = json.getJSONArray("msg");
                            list_str = new ArrayList<String>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                list_str.add(jsonObject.optString("title"));
                            }
                            Message message = new Message();
                            message.obj = list_str;
                            message.what = 111;
                            handler.sendMessage(message);
                        } else {
                            showCustomToast(json.getString("message"));
                        }
                    } else {
                        showCustomToast(R.string.toast1);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                dismissLoadingDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
                dismissLoadingDialog();
            }
        });

    }


    public void dobHttp() {
        JsonBuilder builder = new JsonBuilder();
        curPage = 1;
        builder.put("type", 11);
        builder.put("page", curPage);
        builder.put("limit", pageCount);

        // 填充参数
        BaseHttpClient.post(getActivity(), Default.tzAllList, builder, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();

                showLoadingDialogNoCancle("请稍后努力加载中...");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, json);
                dismissLoadingDialog();
                MyLog.e("statusCode=" + statusCode + "--headers=" + headers.toString() + "--json=" + json.toString());

                if (statusCode == 200) {
                    try {
                        if (json.getInt("status") == 1) {
//							initJsonData(json);
                        } else {
                            showCustomToast(json.getString("message"));
                            dlist.clear();
                        }
                        dismissLoadingDialog();
                        indexTouZiAdapter.notifyDataSetChanged();
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
                dismissLoadingDialog();
            }
        });

    }

    protected void initNewPersionJsonData(JSONArray jsonArray) {
        try {
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject templist = jsonArray.getJSONObject(i);
                    templist.getLong("id");

                    //    TextView new_yjnhll, bd_date, new_ljjr;

                    new_yjnhll.setText(templist.optString("borrow_interest_rate"));
                    templist.optString("borrow_money");
                    templist.optString("borrow_status");
                    templist.optString("extra_rate");
                    templist.optString("borrow_duration");   //表示天数或者月份
                    templist.optString("repayment_type");  //字段表示  1表示天  其他都是月

                }
            }
            // handler.sendEmptyMessage(1);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void initTzBiaoJsonData(JSONArray jsonArray) {
        dlist.clear();
        try {
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject templist = jsonArray.getJSONObject(i);
                    map = new HashMap<String, Object>();
                    map.put("id", templist.getLong("id"));

                    map.put("borrow_interest_rate", templist.optString("borrow_interest_rate"));
                    map.put("borrow_money", templist.optString("borrow_money"));
                    map.put("borrow_status", templist.optString("borrow_status"));
                    map.put("extra_rate", templist.optString("extra_rate"));
                    map.put("borrow_duration", templist.optString("borrow_duration"));   //表示天数或者月份
                    map.put("repayment_type", templist.optString("repayment_type"));  //字段表示  1表示天  其他都是月

                    dlist.add(map);
                }
            }
            dismissLoadingDialog();
            indexTouZiAdapter.notifyDataSetChanged();
            // handler.sendEmptyMessage(1);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        indexTouZiAdapter.notifyDataSetChanged();
    }

    /**
     * 获取幻灯片
     */
    public void doHttp() {
        // RequestParams params = new RequestParams();
        JsonBuilder builder = new JsonBuilder();

        // 填充参数
        BaseHttpClient.post(getActivity(), Default.bannerPic, builder, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, json);
                dismissLoadingDialog();
                MyLog.e("statusCode=" + statusCode + "--headers=" + headers.toString() + "--json=" + json.toString());

                if (statusCode == 200) {
                    try {
                        if (json.getInt("status") == 1) {
//							initBannerData(json);
                            vpAdapter.notifyDataSetChanged();
                        } else {
                            // showCustomToast(json.getString("message"));
                        }

                        dismissLoadingDialog();
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

                dismissLoadingDialog();
            }
        });

    }

    public void stop() {
        try {
            mHandler.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void changePic() {
        if (getActivity() == null) {
            return;
        }
        if (!change) {

            if (index < views.size() - 1) {
                index++;
                setImage(index);
                flipper.setCurrentItem(index);
                if (index == views.size() - 1) {
                    change = true;
                }
            }

        } else {
            if (index > 0) {
                index--;
                setImage(index);
                flipper.setCurrentItem(index);
                if (index == 0) {
                    change = false;
                }
            }
        }
        mHandler.postDelayed(runable, 5000);
    }

    void setImage(int i) {
        for (int j = 0; j < footPointViews.size(); j++) {
            ImageView iv = (ImageView) footPointViews.get(j);
            if (j != i) {
                iv.setImageResource(R.drawable.b_dx2);
            } else {
                iv.setImageResource(R.drawable.b_dx1);
            }
        }
    }

    public int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public void initView(View view) {

        views = new ArrayList<View>();
        footPointViews = new ArrayList<View>();
        flipper = (ViewPager) view.findViewById(R.id.flipper);
        ImageView footView = new ImageView(getActivity());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 10;
        footView.setLayoutParams(params);

        footView.setImageResource(R.drawable.b_dx1);

        footPointViews.add(footView);
        footpointview.addView(footView);

        flipper = (ViewPager) view.findViewById(R.id.flipper);

        vpAdapter = new ViewPagerAdapter(views);

        flipper.setAdapter(vpAdapter);
        // 绑定回调
        flipper.setOnPageChangeListener(this);

        // new Thread() {
        // @Override
        // public void run() {
        // while (isRunning) {
        // SystemClock.sleep(3000);
        // handler.sendEmptyMessage(199);
        // }
        // }
        // }.start();

    }


    private Handler handler = new Handler() {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 199) {
                autoVerticalScrollTextView.next();
                number++;
                // autoVerticalScrollTextView.setText(strings[number %
                // strings.length]);
                autoVerticalScrollTextView.setText(mStr.get(number % mStr.size()));
            } else if (msg.what == 111) {
                mStr = (List<String>) msg.obj;
                if (mStr.size() > 0) {
                    autoVerticalScrollTextView.setText(mStr.get(0));
                    new Thread() {
                        @Override
                        public void run() {
                            while (isRunning) {
                                SystemClock.sleep(3000);
                                handler.sendEmptyMessage(199);
                            }
                        }
                    }.start();
                }

            }
        }
    };


    private String getSubtract(Double all, Double has) {
        BigDecimal b1 = new BigDecimal(all.toString());
        BigDecimal b2 = new BigDecimal(has.toString());
        Double isuse = b1.subtract(b2).doubleValue();
        return isuse + "";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.gg_loadmore:
//                Intent intent = new Intent(getActivity(), TouziActivity.class);
//                startActivity(intent);
                    MainTabActivit_Scg.mainTabActivity.TouziView();

//			ToQQ.toQQ(getActivity(),"349729837");
                break;
            case R.id.autoVerticalScrollTextView:
                getActivity().startActivity(new Intent(getActivity(), NewsListActivity.class));
                break;
            default:

                break;

        }

    }

    public int shabiphp(int i) {
        int j = 0;
        switch (i) {
            case 1:
                j = 3;
                break;
            case 2:
                j = 4;
                break;
            case 3:
                j = 5;
                break;
            case 4:
                j = 6;
                break;
            case 5:
                j = 7;
                break;

            default:
                break;
        }

        return j;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int arg0) {
        index = arg0;
        setImage(arg0);

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        isRunning = false;
    }

}
