package com.lmq.menu;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.czscg.R;
import com.lmq.adapter.ZxAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;


/**
 * Created by Administrator on 2017/7/10.
 */
//http://blog.csdn.net/First_CooMan/article/details/69367519     jcvideoplayer遇到的坑
public class ZxFragment extends Fragment{

    @InjectView(R.id.videoList)
    ListView videoList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zx, container, false);
        ButterKnife.inject(this, view);
        return view;

    }

    public int firstVisible = 0, visibleCount = 0, totalCount = 0;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ZxAdapter zxAdapter=new ZxAdapter(getActivity());
        videoList.setAdapter(zxAdapter);
        zxAdapter.notifyDataSetChanged();
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

        videoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JCVideoPlayerStandard videoPlayerStandard1 = (JCVideoPlayerStandard) view.findViewById(R.id.videoplayer);
                if (videoPlayerStandard1.currentState == JCVideoPlayer.CURRENT_STATE_NORMAL || videoPlayerStandard1.currentState == JCVideoPlayer.CURRENT_STATE_ERROR) {
                    Log.e("videoTest", videoPlayerStandard1.currentState + "======================performClick======================");
                    videoPlayerStandard1.startButton.performClick();
                    MainTabActivit_Scg.mainTabActivity.VideoPlaying = videoPlayerStandard1;
                }
                JCVideoPlayer.releaseAllVideos();
                MainTabActivit_Scg.mainTabActivity.VideoPlaying = null;
            }
        });
    }

    void autoPlayVideo(AbsListView view) {
        Log.e("videoTest", "firstVisiblePos  =  " + firstVisible + "visibleItemCount =  " + visibleCount);
        for (int i = 0; i < visibleCount; i++) {
            if (view != null && view.getChildAt(i) != null && view.getChildAt(i).findViewById(R.id.videoplayer) != null) {
                JCVideoPlayerStandard videoPlayerStandard1 = (JCVideoPlayerStandard) view.getChildAt(i).findViewById(R.id.videoplayer);
                Rect rect = new Rect();
                videoPlayerStandard1.getLocalVisibleRect(rect);
                int videoheight3 = videoPlayerStandard1.getHeight();
                Log.e("videoTest", "i=" + i + "===" + "videoheight3:" + videoheight3 + "===" + "rect.top:" + rect.top + "===" + "rect.bottom:" + rect.bottom);
                if (rect.top == 0 && rect.bottom == videoheight3) {
                    if (videoPlayerStandard1.currentState == JCVideoPlayer.CURRENT_STATE_NORMAL || videoPlayerStandard1.currentState == JCVideoPlayer.CURRENT_STATE_ERROR) {
                        Log.e("videoTest", videoPlayerStandard1.currentState + "======================performClick======================");
                        videoPlayerStandard1.startButton.performClick();
                        MainTabActivit_Scg.mainTabActivity.VideoPlaying = videoPlayerStandard1;
                    }
                    return;
                }

            }
        }
        Log.e("videoTest", "======================releaseAllVideos===================");
        JCVideoPlayer.releaseAllVideos();
        MainTabActivit_Scg.mainTabActivity.VideoPlaying = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

}
