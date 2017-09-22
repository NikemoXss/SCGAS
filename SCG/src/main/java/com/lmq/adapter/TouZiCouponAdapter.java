package com.lmq.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.view.RoundProgressBar_Tz_Sx;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class TouZiCouponAdapter extends BaseAdapter {

    private Context mContext;
    private List<Map<String, Object>> list;
    private LayoutInflater mInflater;

    public TouZiCouponAdapter(Context mContext) {
        super();
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public TouZiCouponAdapter(Context mContext, List<Map<String, Object>> list) {
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
            convertView = mInflater.inflate(R.layout.item_coupon_tzy, null);
            holder.new_tzy_yjnhll = (TextView) convertView.findViewById(R.id.new_tzy_yjnhll);
            holder.new_tzy_extra = (TextView) convertView.findViewById(R.id.new_tzy_extra);
            holder.bd_tzy_je = (TextView) convertView.findViewById(R.id.bd_tzy_je);
            holder.tv_tzy_je = (TextView) convertView.findViewById(R.id.tv_tzy_je);
            holder.bd_tzy_date = (TextView) convertView.findViewById(R.id.bd_tzy_date);
            holder.tv_tzy_date = (TextView) convertView.findViewById(R.id.tv_tzy_date);
            holder.item_tzy_title = (TextView) convertView.findViewById(R.id.item_tzy_title);
            holder.progress_tzy_scw = (RoundProgressBar_Tz_Sx) convertView.findViewById(R.id.progress_tzy_scw);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item_tzy_title.setText(list.get(position).get("borrow_name").toString());
        holder.new_tzy_yjnhll.setText(list.get(position).get("item_nhl_sx").toString());

        if(!"0.00".equals(list.get(position).get("extra_rate").toString())){
            holder.new_tzy_extra.setText("+"+list.get(position).get("extra_rate").toString());
        }
        Double tzje=(Double)list.get(position).get("item_jkje_sx");
        double n = (double)tzje/10000;
        holder.bd_tzy_je.setText(n+"");
        holder.bd_tzy_date.setText(list.get(position).get("item_tzqx_sx").toString());
        Double hastzje=(Double)list.get(position).get("has_borrow");
        Double jepro=div(hastzje,tzje,2)*100;

        holder.progress_tzy_scw.setProgress(jepro.intValue());


        String repayment_type=list.get(position).get("repayment_type").toString();
        if("1".equals(repayment_type)){
            holder.tv_tzy_date.setText("天");
        }else {
            holder.tv_tzy_date.setText("个月");
        }
        return convertView;
    }

    public static Double div(Double v1,Double v2,int scale){
        if(scale<0){
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        if(v1!=null&&v2!=null){
            BigDecimal b1 = new BigDecimal(v1+"");
            BigDecimal b2 = new BigDecimal(v2+"");
            return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
       return  0.00;
    }


    public class ViewHolder {
        TextView item_tzy_title, new_tzy_yjnhll,new_tzy_extra,bd_tzy_je,tv_tzy_je,bd_tzy_date,tv_tzy_date;
        RoundProgressBar_Tz_Sx progress_tzy_scw;
    }

}
