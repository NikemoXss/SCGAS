package com.lmq.main.dialog;

import java.text.DecimalFormat;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.czscg.R;

public class ZqzrDialog extends BaseDialog {

	private Button mButton;
	private android.view.View.OnClickListener mListener;
	// private EditText mEditText;

	// private String info;

	private double price, dq_money, account_money;
	private String password;
	private EditText pin;

	/**
	 * @param context
	 * @param dq_money
	 *            债券总额
	 * @param price
	 *            债券转让金额
	 * @param account_money
	 *            用户账户余额
	 * @param password
	 *            支付密码
	 */
	public ZqzrDialog(Context context, double dq_money, double price, double account_money, String password) {
		super(context);

		this.dq_money = dq_money;
		this.price = price;
		this.account_money = account_money;
		this.password = password;
		init();
	}

	private void init() {
		setContentView(R.layout.diloag_zqzr);
		mButton = (Button) findViewById(R.id.gm);
		TextView ze = (TextView) findViewById(R.id.ze);
		TextView jg = (TextView) findViewById(R.id.jg);
		TextView ye = (TextView) findViewById(R.id.ye);
		pin = (EditText) findViewById(R.id.zq_item_pin);
		LinearLayout lv_psw = (LinearLayout) findViewById(R.id.lv_psw);
		{
			pin.setFocusable(true);
		}

		DecimalFormat d = new DecimalFormat("##0.00");
		ze.setText(d.format(price) + "");
		jg.setText(d.format(dq_money) + "");
		ye.setText(d.format(account_money) + "");

	}

	public void setListener(android.view.View.OnClickListener listener) {
		mListener = listener;
		mButton.setOnClickListener(mListener);
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	public String getPassword() {
		return pin.getText().toString();
	}
}
