package com.lmq.menu;

import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.czscg.R;
import com.lmq.gesture.UnlockGesturePasswordActivity;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.http.NetWorkStatusBroadcastReceiver;
import com.lmq.main.activity.LoginActivity_Scg;
import com.lmq.main.api.ActivityManager;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.api.MyLog;
import com.lmq.main.api.res.SyncImageLoader;
import com.lmq.main.dialog.NewVersionDialog;
import com.lmq.main.util.Default;
import com.lmq.menu.tab.FragmentFactory;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

//scg
public class MainTabActivit_Scg extends FragmentActivity {
    public static MainTabActivit_Scg mainTabActivity;
    private RadioGroup radioGroup;
    private TextView titleView;
    private NetWorkStatusBroadcastReceiver netWorkStatusBroadcastReceiver;
    private SyncImageLoader syncImageLoader;
    private RadioButton oneButton, towButton, threeButton, fourButton;
    public JCVideoPlayerStandard VideoPlaying;
    /**
     * 当前TabBar 选择项
     */
    private int currentIndex = 1;
    /***
     * 当前TabBar 选择项 之前的选择项
     */
    private int lastIndex = 1;

    private boolean changeToHomeFlag = false;
    private int homeType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        mainTabActivity = this;
        titleView = (TextView) findViewById(R.id.title_view);
        oneButton = (RadioButton) findViewById(R.id.tab_one);
        towButton = (RadioButton) findViewById(R.id.tab_two);
        threeButton = (RadioButton) findViewById(R.id.tab_three);
        fourButton = (RadioButton) findViewById(R.id.tab_four);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCacheExtraOptions(480, 800)
                // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null).threadPoolSize(3)
                // default
                .threadPriority(Thread.NORM_PRIORITY - 2)
                // default
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                // default
                .denyCacheImageMultipleSizesInMemory().memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13)
                // default
                .diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs().build();

        ImageLoader.getInstance().init(config);
        netWorkStatusBroadcastReceiver = new NetWorkStatusBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStatusBroadcastReceiver, filter);

        Fragment fragment = FragmentFactory.getInstanceByIndex(1);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        titleView.setText("理财");
        oneButton.setChecked(true);

        radioGroup = (RadioGroup) findViewById(R.id.rg_tab);
        // showThePasswordView();
        if (!Default.isActive) {
            // app 从后台唤醒，进入前台
            if (!Default.isgestures) {

                showThePasswordView();
            } else {
                Default.isgestures = false;
            }
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                /**
                 * 判断用户是否登陆 ， 借款和个人账户的跳转选择
                 */

                int selectedId = 0;
                switch (checkedId) {
                    case R.id.tab_one:
                        selectedId = 1;
                        break;
                    case R.id.tab_two:
                        selectedId = 2;
                        break;
                    case R.id.tab_three:
                        selectedId = 3;
                        break;
                    case R.id.tab_four:
                        selectedId = 4;
                        break;
                    default:
                        break;
                }
                lastIndex = currentIndex;
                currentIndex = selectedId;
                changeTitle(selectedId);

                Fragment fragment = FragmentFactory.getInstanceByIndex(selectedId);
                if (changeToHomeFlag) {
                    HomeFragment homeFragment = (HomeFragment) fragment;
                    homeFragment.swith_flag = homeType;
                    changeToHomeFlag = false;
                    homeType = -1;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment)
                        .commitAllowingStateLoss();

            }
        });

        /***
         * 处理推送消息
         */

        Intent pushIntent = getIntent();

        if (pushIntent != null) {

            if (pushIntent.hasExtra("pushFlag")) {

                if (pushIntent.getBooleanExtra("pushFlag", false)) {

                    startActivity(pushIntent);

                }

            }

        }

        getVersion();
        checkNewVersion();

    }

    public int getPercent(int x, int total) {
        int result = 0;// 接受百分比的值
        double x_double = Double.parseDouble(x + "");
        int tempresult = (int) (x / Double.parseDouble(total + "") * 100);

        return tempresult;
    }

    /**
     * 根据地址下载最新版本的APP
     *
     * @param download_url
     */
    public void downloadNewVersionApp(String download_url) {

        // Toast.makeText(LogoActicity.this, download_url,
        // Toast.LENGTH_SHORT).show();

        // final String fileName = "updata.apk";
        // File tmpFile = new File("/sdcard/update");
        // if (!tmpFile.exists()) {
        // tmpFile.mkdir();
        // }
        // final File file = new File("/sdcard/update/" + fileName);
        String filePathStr = Environment.getExternalStorageDirectory() + "/temp/";
        // String fileName = "newVersion.apk";
        // File file = new File(filePathStr);
        // File saveFile = null;
        // try {
        //// saveFile = File.createTempFile("newVersion", ".apk", file);
        // // saveFile = File.createTempFile("newVersion", ".apk");
        // // saveFile=BaseHttpClient.createTempFile("newVersion", 1024,
        // // MainTabActivity_Sx.this);
        //
        // } catch (Exception e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        File file2 = getFilePath(filePathStr, "updata.apk");
        // saveFile=BaseHttpClient.createTempFile("newVersion", 1024,
        // MainTabActivity_Sx.this);
        BaseHttpClient.downloadFile(MainTabActivit_Scg.this, download_url, new FileAsyncHttpResponseHandler(file2) {

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();
                MyLog.e("onStart----------->" + "onStart");

            }

            @Override
            public void onProgress(int bytesWritten, int totalSize) {
                // TODO Auto-generated method stub
                super.onProgress(bytesWritten, totalSize);
                int num = getPercent(bytesWritten, totalSize);
                dialog.progressBar.setProgress(num);
                dialog.showProgressView.setText(num + "%");
                Log.e("onProgress", num + "");

            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, File file) {
                // TODO Auto-generated method stub

                MyLog.e("SUCCESS");
                BaseHttpClient.httpClient.setTimeout(3 * 1000);
                dialog.updateBtn.setText("安裝");
                dialog.updateBtn.setTag(101);


                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本
                    Uri apkUri = FileProvider.getUriForFile(MainTabActivit_Scg.this, "com.czscg.fileprovider", file);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                }
                startActivity(intent);

//                MyLog.e(file.getPath());
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.fromFile(new File(file.getPath())),
//                        "application/vnd.android.package-archive");
//                startActivity(intent);
                finish();

            }

            @Override
            public void onFailure(int arg0, Header[] arg1, Throwable arg2, File arg3) {
                // TODO Auto-generated method stub
                Toast.makeText(MainTabActivit_Scg.this, "更新失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static File getFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return file;
    }

    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {

        }
    }

    private NewVersionDialog dialog;
    private String downloadURL = "";

    // 提交用户反馈到服务器
    public void checkNewVersion() {

        dialog = new NewVersionDialog(MainTabActivit_Scg.this, "");
        dialog.setListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                switch (v.getId()) {
                    // 下载更新
                    case R.id.download_newapp:

                        dialog.updateBtn.setEnabled(false);

                        // dialog.exitbtn.setEnabled(false);
                        // dialog.updateBtn.setVisibility(View.GONE);
                        // dialog.exitbtn.setVisibility(View.GONE);
                        dialog.showProgress();
                        downloadNewVersionApp(dialog.getPath());

                        // ActivityInfo activityInfo =
                        // SystenmApi.getBrowserApp(getApplicationContext());
                        // if (activityInfo != null) {
                        // Uri uri = Uri.parse(dialog.getPath());
                        // Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        // intent.setClassName(activityInfo.packageName,
                        // activityInfo.name);
                        // startActivity(intent);
                        // } else {
                        // showCustomToast("手机没有浏览器，无法更新...");
                        // }

                        break;
                    // 退出程序
                    case R.id.newversion_exit:
                        doHttp();
                        finish();

                        break;

                    default:
                        break;
                }

            }

        });

        JsonBuilder jsonBuilder = new JsonBuilder();

        jsonBuilder.put("version", Default.curVersion);

        BaseHttpClient.post(getBaseContext(), Default.version, jsonBuilder, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
                Log.e("onSuccess", Default.curVersion + ";" + statusCode + response.toString());
                try {

                    if (statusCode == 200) {
                        MyLog.e(response.toString());
                        if (response.getInt("status") == 1) {

                        } else if (response.getInt("status") == 0) {

                            String temp = dialog.versionTextView.getText().toString();
                            dialog.versionTextView.setText(temp + "(" + response.getString("version") + ")");
                            dialog.show();
                            dialog.setPath(Default.ip + "/" + response.getString("url").replace("\\/", ""));
                            // UpdateChecker2 updateChecker = new
                            // UpdateChecker2(MainTabActivity_Sx.this);
                            // updateChecker.setCheckUrl("1111");
                            // updateChecker.checkForUpdates(Default.ip+response.getString("path").replace("\\/",""));

                            Log.e("onSuccess++++", response.getString("url").replace("\\/", ""));
                        } else {

                        }
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
            }

        });

    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public void getVersion() {
        try {
            Default.curVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;// curVersioncode
            // Default.curVersioncode =
            // getPackageManager().getPackageInfo(getPackageName(),
            // 0).versionCode;// curVersioncode
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public SyncImageLoader getImageLodader() {
        return syncImageLoader;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 4000:
                if (resultCode == Default.LOGIN_TYPE_2) {
                    Fragment fragment = FragmentFactory.getInstanceByIndex(1);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                } else {
                    changeTOFirst(lastIndex);
                }

                break;
            case 5000:
                if (resultCode == Default.LOGIN_TYPE_2) {
                    titleView.setText("个人账户");
                    Fragment fragment = FragmentFactory.getInstanceByIndex(2);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment)
                            .commitAllowingStateLoss();
                } else {
                    changeTOFirst(lastIndex);
                }
                break;
            case 6000:
                // 设置头型
                if (resultCode == Default.LOGIN_TYPE_2) {

                    // sm.toggle(true);
                } else {

                }
                break;

            default:
                break;
        }
    }

    /**
     * 跳转到上一页
     */
    private void changeTOFirst(int index) {
        switch (index) {
            case 1:
                oneButton.setChecked(true);
                break;
            case 2:
                towButton.setChecked(true);
                break;
            case 3:
                threeButton.setChecked(true);
                break;
            case 4:
                fourButton.setChecked(true);
                break;

            default:
                break;
        }
        changeTitle(index);

    }

    /**
     * 更改标题
     *
     * @param index Tab 选项
     */
    private void changeTitle(int index) {

        switch (index) {
            case 1:
                titleView.setText("理财");
                break;
            case 2:
                if (Default.userId == 0) {
                    Intent userInfoLoginIntent = new Intent();
                    userInfoLoginIntent.setClass(this, LoginActivity_Scg.class);
                    startActivity(userInfoLoginIntent);
                } else {
                    titleView.setText("个人账户");
                }
                break;
            case 3:
                titleView.setText("设置");
                break;
            case 4:
                titleView.setText("咨询");
                break;
            default:
                break;
        }
    }

    /***
     * 跳转到首页
     *
     * @return
     */

    public void IndexView() {

        oneButton.setChecked(true);

    }

    /**
     * 跳转到我要投资
     */
    public void changeHomeFragment(int type) {
        homeType = type;
        changeToHomeFlag = true;
        towButton.setChecked(true);

    }

    /***
     * 跳转到借款页
     *
     * @return
     */

    public void LendMoneyView() {

        threeButton.setChecked(true);

    }



    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (!Default.isActive) {
            // app 从后台唤醒，进入前台
            if (!Default.isgestures) {

                showThePasswordView();
            } else {
                Default.isgestures = false;
            }
            Default.isActive = true;
        }
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        android.app.ActivityManager activityManager = (android.app.ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }

        for (RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    public void showThePasswordView() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("lmq", 0);

        if (sharedPreferences.getBoolean("sl", false)) {
            Intent intent = new Intent(getApplicationContext(), UnlockGesturePasswordActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);

        }

    }

    private long lastTime;
    private int closeNum;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                closeNum++;
                if (System.currentTimeMillis() - lastTime > 5000) {
                    closeNum = 0;
                }
                lastTime = System.currentTimeMillis();
                if (closeNum == 1) {
                    doHttp();
                    finish();
                } else {
                    showCustomToast(R.string.toast6);
                }
            }
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void doHttp() {

        BaseHttpClient.post(getBaseContext(), Default.exit, null, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);

                try {

                    if (statusCode == 200) {
                        if (response.getInt("status") == 1) {
                            MyLog.d("zzx", "exit成功");
                            ActivityManager.closeAllActivity();
                        } else {
                            MyLog.d("zzx", "exit失败");
                        }
                    } else {
                        MyLog.d("zzx", "exit失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
            }

        });

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        unregisterReceiver(netWorkStatusBroadcastReceiver); // 取消监听
        super.onDestroy();
    }

    public void showCustomToast(String text) {
        View toastRoot = LayoutInflater.from(MainTabActivit_Scg.this).inflate(R.layout.common_toast, null);
        ((TextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
        Toast toast = new Toast(MainTabActivit_Scg.this);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastRoot);
        toast.show();
    }

    public void showCustomToast(int id) {
        showCustomToast(getResources().getString(id).toString());
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // sm.toggle(true);

            return false;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        Default.isAlive = false;
    }

    @Override
    protected void onStart() {
//        Intent intent = new Intent(MainTabActivit_Scg.this, FloatViewService.class);
//        startService(intent);
        super.onStart();
        getJxjNum();
    }

    @Override
    protected void onStop() {
//        Intent intent = new Intent(MainTabActivit_Scg.this, FloatViewService.class);
//        stopService(intent);
        super.onStop();
        if (!isAppOnForeground()) {
            // 全局变量isActive = false 记录当前已经进入后台
            Default.isActive = false;
        }
    }

    private void getJxjNum() {

        BaseHttpClient.post(getBaseContext(), Default.ishasjxj, null, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);

                try {

                    if (statusCode == 200) {
                        if (response.getInt("status") == 1) {
                            String mNum = response.getString("message");
                            dialog(mNum);
                        } else {

                        }
                    } else {
                        // showCustomToast("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
            }

        });

    }

    public void dialog(String num) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // 先得到构造器
        builder.setTitle("恭喜您获得" + num + "张加息劵,"); // 设置标题
        builder.setMessage("可前往个人账户加息劵中查看！"); // 设置内容
        builder.setIcon(R.drawable.scg_logo_small);// 设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // 设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // 关闭dialog
                // Toast.makeText(MainTabActivit_Scg.this, "确认" + which,
                // Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // 设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // Toast.makeText(MainTabActivit_Scg.this, "取消" + which,
                // Toast.LENGTH_SHORT).show();
            }
        });
        // 参数都设置完成了，创建并显示出来
        builder.create().show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (VideoPlaying != null) {
            if (VideoPlaying.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING) {
                VideoPlaying.startButton.performClick();
            } else if (VideoPlaying.currentState == JCVideoPlayer.CURRENT_STATE_PREPARING) {
                JCVideoPlayer.releaseAllVideos();
            }
        }

    }
}
