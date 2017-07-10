package com.lmq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.czscg.R;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Administrator on 2017/7/10.
 */

public class ZxAdapter extends BaseAdapter {

    Context context;
    LayoutInflater mInflater;

    public ZxAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        {
            //This is the point
//            if (convertView != null && convertView.getTag() != null && convertView.getTag() instanceof VideoHolder) {
//                ((VideoHolder) convertView.getTag()).jcVideoPlayer.release();
//            }
//
//            VideoHolder viewHolder;
//            if (convertView != null && convertView.getTag() != null && convertView.getTag() instanceof VideoHolder) {
//                viewHolder = (VideoHolder) convertView.getTag();
//            } else {
//                viewHolder = new VideoHolder();
//                convertView = mInflater.inflate(R.layout.item_list_zx, null);
//                viewHolder.jcVideoPlayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
//                convertView.setTag(viewHolder);
//            }

            VideoHolder  viewHolder = new VideoHolder();
            convertView = mInflater.inflate(R.layout.item_list_zx, null);
            viewHolder.jcVideoPlayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
            convertView.setTag(viewHolder);

            viewHolder.jcVideoPlayer.setUp(
                    "http://gslb.miaopai.com/stream/ed5HCfnhovu3tyIQAiv60Q__.mp4", JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
            Glide.with(context).load("http://a4.att.hudong.com/05/71/01300000057455120185716259013.jpg").into(viewHolder.jcVideoPlayer.thumbImageView);
            return convertView;
        }
    }

    class VideoHolder {
        JCVideoPlayerStandard jcVideoPlayer;
    }
}
