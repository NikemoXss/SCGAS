package com.lmq.main.api;

import android.app.Application;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.jingewenku.abrahamcaijin.commonutil.CrashHandlerUtil;
import com.lmq.main.util.LockPatternUtils;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;
import com.tencent.mm.sdk.openapi.IWXAPI;

import cn.sharesdk.framework.ShareSDK;

public class LocationApplication extends Application {

    public LocationClient locationClient;
    public MyLocationListener myLocationListener;
    public TextView resultTextView;
    private static LocationApplication mInstance;
    private LockPatternUtils mLockPatternUtils;
    public TextView titleTextView;

    public static IWXAPI wxapi;

    public static LocationApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ShareSDK.initSDK(this);

        // Toast.makeText(getApplicationContext(), "application",
        // Toast.LENGTH_SHORT).show();
        mInstance = this;
        locationClient = new LocationClient(getApplicationContext());
        myLocationListener = new MyLocationListener();
        locationClient.registerLocationListener(myLocationListener);
        mLockPatternUtils = new LockPatternUtils(this);
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        // 自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        // 包括BD0    9LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        //热修复初始
        sophi();
        //捕获异常
        CrashE();
    }

    private void CrashE() {
        CrashHandlerUtil crashHandlerUtil = CrashHandlerUtil.getInstance();
        crashHandlerUtil.init(getApplicationContext());
        crashHandlerUtil.setCrashTip("很抱歉，程序出现异常，即将退出！");
    }

    public void sophi() {
        SophixManager.getInstance().setContext(this)
                .setAppVersion(SystenmApi.getVersion(getApplicationContext()))
                .setAesKey(null)
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                            Toast.makeText(getApplicationContext(), "补丁加载成功", Toast.LENGTH_LONG).show();
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后应用自杀
                            Toast.makeText(getApplicationContext(), "补丁加载成功,有个新补丁", Toast.LENGTH_LONG).show();
                        } else if (code == PatchStatus.CODE_LOAD_FAIL) {
                            // 内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
                            // SophixManager.getInstance().cleanPatches();
                            Toast.makeText(getApplicationContext(), "补丁加载失败", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "其他" + code, Toast.LENGTH_LONG).show();
                            // 其它错误信息, 查看PatchStatus类说明
                        }
                    }
                }).initialize();
        SophixManager.getInstance().queryAndLoadNewPatch();
    }

    public LockPatternUtils getLockPatternUtils() {
        return mLockPatternUtils;
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append(location.getCity());

            logMsg(sb.toString());
            MyLog.i("BaiduLocationApiDem", sb.toString());
        }

    }

    /**
     * 显示请求字符串
     *
     * @param str
     */
    public void logMsg(String str) {
        try {
            if (resultTextView != null) {
                resultTextView.setText("已定位:" + str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
