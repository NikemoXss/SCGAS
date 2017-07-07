package com.lmq.main.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.czscg.R;

public class DedetCancleDialog {
	private Context context;
	private PopupWindow popupWindow;
	private Button cButton, sButton;
	private OnClickListener mClickListener;
	private TextView titleView;
	private EditText passwrod;
	private Object object;

	public DedetCancleDialog(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;

		View view = LayoutInflater.from(context).inflate(R.layout.lmq_dialog_layout, null);
		cButton = (Button) view.findViewById(R.id.dialog_cancle);
		sButton = (Button) view.findViewById(R.id.dialog_submit);
		titleView = (TextView) view.findViewById(R.id.dialog_title);
		passwrod = (EditText) view.findViewById(R.id.dialog_password);

		popupWindow = new PopupWindow(view, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

	}

	public void setObject(Object obj) {
		object = obj;
	}

	public Object getObject() {

		return object;
	}

	public void clearPassword() {

		passwrod.setText("");
	}

	public void setonClickListener(OnClickListener listener) {

		sButton.setOnClickListener(listener);
		cButton.setOnClickListener(listener);
	}

	public void setDialogTitle(String title) {

		titleView.setText(title);

	}

	public String getPassword() {

		return passwrod.getText().toString();

	}

	// 下拉式 弹出 pop菜单 parent 右下角
	public void showAsDropDown(View parent) {

		popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);

		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(false);
		// 刷新状态
		popupWindow.update();
	}

	public boolean isShowing() {

		return popupWindow.isShowing();

	}

	// 隐藏菜单
	public void dismiss() {
		popupWindow.dismiss();
	}

}
