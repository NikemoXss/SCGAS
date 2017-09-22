package com.lmq.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.main.activity.BidItem_Scg;
import com.lmq.main.activity.LoginActivity_Scg;
import com.lmq.main.util.Default;

import java.util.List;
import java.util.Map;

public class IndexTouZiAdapter extends BaseAdapter {

    private Context mContext;
    private List<Map<String, Object>> list;
    private LayoutInflater mInflater;

    public IndexTouZiAdapter(Context mContext) {
        super();
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public IndexTouZiAdapter(Context mContext, List<Map<String, Object>> list) {
        super();
        mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_newlist_indexfragment, null);
            holder.new_yjnhll = (TextView) convertView.findViewById(R.id.new_yjnhll);
            holder.bd_date = (TextView) convertView.findViewById(R.id.bd_date);
            holder.new_ljjr = (TextView) convertView.findViewById(R.id.new_ljjr);

            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_item = (TextView) convertView.findViewById(R.id.tv_item);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.new_yjnhll.setText(list.get(position).get("borrow_interest_rate").toString());
        holder.bd_date.setText(list.get(position).get("borrow_duration").toString());

        String str[]=new String[]{"期限最短","速度最快","稳定收益","利率最高"};
        String tv="高收益，短期限";
        if(position<str.length){
            tv=str[position];
        }
        holder.tv_item.setText(tv);

        String repayment_type=list.get(position).get("repayment_type").toString();
        if("1".equals(repayment_type)){
            holder.tv_date.setText("天");
        }else {
            holder.tv_date.setText("个月");
        }

        holder.new_ljjr.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (Default.userId != 0) {
                    Intent intent1 = new Intent(mContext, LoginActivity_Scg.class);
                    mContext.startActivity(intent1);
                } else {
                    Long id = (Long) list.get(position).get("id");
                    String string = (String) list.get(position).get("repayment_type");
                    Intent intent = new Intent(mContext, BidItem_Scg.class);
                    intent.putExtra("id", id);
                    mContext.startActivity(intent);
                }


            }
        });
        return convertView;
    }

    public class ViewHolder {
        TextView new_yjnhll,bd_date,new_ljjr,tv_date,tv_item;
    }

}
