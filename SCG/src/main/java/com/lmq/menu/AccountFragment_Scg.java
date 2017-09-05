package com.lmq.menu;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.activity.InterestratesecuritiesActivity;
import com.lmq.main.activity.MyBriberyMoney;
import com.lmq.main.activity.PeopleInfoJiaoYi_New;
import com.lmq.main.activity.investmanager.InvestManagerStandardActivity;
import com.lmq.main.activity.peopleInfoDataActivity;
import com.lmq.main.activity.peopleInfoWithdrawalActivity;
import com.lmq.main.api.BaseFragment;
import com.lmq.main.api.MyLog;
import com.lmq.main.api.SystenmApi;
import com.lmq.main.util.Default;
import com.lmq.pay.ChoosePayType;
import com.lmq.view.ChuckWaveView;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


//苏常账户
@SuppressLint("NewApi")
public class AccountFragment_Scg extends BaseFragment implements OnClickListener {

    private ChuckWaveView mWaveView = null;

    private float mCurrentHeight = 0;

    TextView total_rs, freeze_rs, user_name_rs, daishou, may, freeze;
    ImageView user_pic;
    int jxj_num = 0;
    int[] p = new int[]{R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4, R.drawable.p5, R.drawable.p6, R.drawable.p7, R.drawable.p8, R.drawable.p9, R.drawable.p10};

    LinearLayout isvg;

    Boolean isV = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.layout_account_sx, null);
        mainView.findViewById(R.id.item_personalset).setOnClickListener(this);
        mainView.findViewById(R.id.item_tzrecord).setOnClickListener(this);
        mainView.findViewById(R.id.item_jyjl).setOnClickListener(this);
        mainView.findViewById(R.id.cz_rs).setOnClickListener(this);
        mainView.findViewById(R.id.tx_rs).setOnClickListener(this);
        mainView.findViewById(R.id.item_jxj).setOnClickListener(this);
        mainView.findViewById(R.id.item_redpkg).setOnClickListener(this);
        mainView.findViewById(R.id.item_gw).setOnClickListener(this);
        mainView.findViewById(R.id.item_feedback).setOnClickListener(this);
        total_rs = (TextView) mainView.findViewById(R.id.total_rs);
        freeze_rs = (TextView) mainView.findViewById(R.id.freeze_rs);
        user_pic = (ImageView) mainView.findViewById(R.id.user_pic);
        user_name_rs = (TextView) mainView.findViewById(R.id.user_name_rs);
        mWaveView = (ChuckWaveView) mainView.findViewById(R.id.main_wave_v1);
        isvg=(LinearLayout) mainView.findViewById(R.id.isvg);
        daishou = (TextView) mainView.findViewById(R.id.daishou);
        may = (TextView) mainView.findViewById(R.id.may);
        freeze = (TextView) mainView.findViewById(R.id.freeze);

        initview();
        return mainView;
    }

    private void initview() {
        // TODO Auto-generated method stub

        loadWaveData(0.30f);
    }

    private void loadWaveData(float height) {
        float a = 0.33f;
        height = a;
        // Toast.makeText(MainActivity.this, height + "", 1000).show();
        final List<Animator> mAnimators = new ArrayList<Animator>();
        ObjectAnimator waveShiftAnim = ObjectAnimator.ofFloat(mWaveView, "waveShiftRatio", 0f, 1f); // 水平方向循环
        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveShiftAnim.setDuration(2000);
        waveShiftAnim.setInterpolator(new LinearInterpolator());
        mAnimators.add(waveShiftAnim);

        ObjectAnimator waterLevelAnim = ObjectAnimator.ofFloat(mWaveView, "waterLevelRatio", mCurrentHeight, height); // 竖直方向从0%到x%
        waterLevelAnim.setDuration(6000);
        waterLevelAnim.setInterpolator(new DecelerateInterpolator());
        mAnimators.add(waterLevelAnim);

        mWaveView.invalidate();
        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(mAnimators);
        mAnimatorSet.start();

        mCurrentHeight = height;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        doHttpUpdatePeopleInfo();
        // getInterestRateSecuritiesList();
        // idHasInterestRateSecurities();
    }

    public void doHttpUpdatePeopleInfo() {

        BaseHttpClient.post(getActivity(), Default.peoInfoUpdate, null, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();
                showLoadingDialogNoCancle("");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
                Log.e("onSuccess1", response.toString());
                try {
                    if (statusCode == 200) {
                        if (response.getInt("status") == 1) {
                            // updateUserInfo(response);
                            total_rs.setText(response.getString("total") + "元");
                            // 冻结
                            freeze.setText(response.getString("freeze") + "元");
                            may.setText(response.getString("mayuse")+ "元");
                            daishou.setText(response.getString("collect")+ "元");
                            Picasso.with(getActivity()).load(Default.ip + response.getString("head")).into(user_pic);

                            if (Default.p == 0) {
                                java.util.Random random = new java.util.Random();// 定义随机类
                                int result = random.nextInt(10);// 返回[0,10)集合中的整数，注意不包括10
                                int resourceId = p[result];
                                Default.p = resourceId;
                                Picasso.with(getActivity()).load(resourceId).into(user_pic);
                            } else {
                                Picasso.with(getActivity()).load(Default.p).into(user_pic);
                            }

                            user_name_rs.setText(response.getString("username"));
                        } else {
                            showCustomToast(response.getString("message"));
                        }
                    } else {
                        showCustomToast(R.string.toast1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dismissLoadingDialog();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
                // dismissLoadingDialog();
                dismissLoadingDialog();
                showCustomToast(responseString);
            }

        });

    }

    //
    public void idHasInterestRateSecurities() {

        BaseHttpClient.post(getActivity(), Default.ishasjxj, null, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();
                showLoadingDialogNoCancle("");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);

                try {
                    if (statusCode == 200) {
                        if (response.getInt("status") == 1) {
                            jxj_num = response.getInt("message");
                        } else {
                            showCustomToast(response.getString("message"));
                        }
                    } else {
                        showCustomToast(R.string.toast1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dismissLoadingDialog();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
                // dismissLoadingDialog();
                dismissLoadingDialog();
                showCustomToast(responseString);
            }

        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_personalset:// 用户设置

//                int[] k={1,2,3,4,5};
//
//                for(int j = 0 ; j < 6 ; j++)
//                {
//                    System.out.println(k[j]);
//                }


                // Intent intent = new Intent(getActivity(),
                // PersonalSetActivity.class);
                Intent intent = new Intent(getActivity(), peopleInfoDataActivity.class);
                startActivity(intent);
                break;
            case R.id.item_tzrecord:// 投资记录
                Intent intent1 = new Intent(getActivity(), InvestManagerStandardActivity.class);
                startActivity(intent1);
                break;
            case R.id.item_jyjl:// 交易记录
                startActivity(new Intent(getActivity(), PeopleInfoJiaoYi_New.class));
                break;
            case R.id.cz_rs:// 充值
                Intent i = new Intent(getActivity(), ChoosePayType.class);
                startActivity(i);
                break;
            case R.id.tx_rs:// 提现
                getUserBankCard();
                break;
            case R.id.item_jxj:// 加息劵
                Intent i1 = new Intent(getActivity(), InterestratesecuritiesActivity.class);
                startActivity(i1);
                break;
            case R.id.item_redpkg://红包
                Intent i2 = new Intent(getActivity(), MyBriberyMoney.class);
                startActivity(i2);
                break;
            case R.id.item_gw:
                ActivityInfo activityInfo = SystenmApi.getBrowserApp(getActivity());
                if (activityInfo != null) {
                    Uri uri = Uri.parse(Default.yu);
                    Intent intent4 = new Intent(Intent.ACTION_VIEW, uri);
                    intent4.setClassName(activityInfo.packageName, activityInfo.name);
                    startActivity(intent4);
                } else {
                    showCustomToast("手机没有浏览器，无法访问");
                }
                break;
            case R.id.item_feedback:
                if(isV){
                    isvg.setVisibility(View.VISIBLE);
                    isV=false;
                }else {
                    isvg.setVisibility(View.GONE);
                    isV=true;
                }
                break;
            default:
                break;

        }
    }

    public void getUserBankCard() {

        BaseHttpClient.post(getActivity(), Default.peoInfoWithdrawal, null, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();
                showLoadingDialogNoCancle(getResources().getString(R.string.toast2));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
                try {

                    if (statusCode == 200) {
                        if (response.getInt("status") == 1) {
                            MyLog.e("获取提现的信息", response.toString());
                            // 获取到用户绑定银行卡信息
                            decoidTheUesrCardInfo(response);

                        } else {
                            showCustomToast(response.getString("message"));
                        }
                    } else {
                        showCustomToast(R.string.toast1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dismissLoadingDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
                dismissLoadingDialog();
                showCustomToast(responseString);
            }
        });

    }

    // 解析获取到银行卡信息
    public void decoidTheUesrCardInfo(JSONObject json) {
        try {
            if (json.getString("bank_num") != null && !json.getString("bank_num").equals("")) {

                Intent intent = new Intent(getActivity(), peopleInfoWithdrawalActivity.class);

                intent.putExtra("bank_num", json.getString("bank_num"));

                if (json.getString("bank_name") != null && !json.getString("bank_name").equals("")) {

                    intent.putExtra("bank_name", json.getString("bank_name"));

                }
                if (json.getString("real_name") != null && !json.getString("real_name").equals("")) {

                    intent.putExtra("real_name", json.getString("real_name"));
                }
                // if (json.getString("all_money") != null
                // && !json.getString("all_money").equals("")) {
                intent.putExtra("all_money", json.getString("all_money"));
                MyLog.e("提现准备传的钱数", json.getString("all_money"));
                // }
                if (json.getString("qixian") != null && !json.getString("qixian").equals("")) {

                    intent.putExtra("qixian", json.getString("qixian"));
                }

                startActivity(intent);

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
