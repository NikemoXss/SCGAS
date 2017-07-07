package com.lmq.main.item;

import org.json.JSONObject;

public class tzDetailItem {
	// 投标人
	private String user_name;
	// 钱
	private String investor_capital;
	// 时间
	private String add_time;

	public tzDetailItem(JSONObject json) {
		try {
			setInvestor_capital(json.getString("investor_capital"));
			setAdd_time(json.getString("add_time"));
			setUser_name(json.getString("user_name"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getInvestor_capital() {
		return investor_capital;
	}

	public void setInvestor_capital(String investor_capital) {
		this.investor_capital = investor_capital;
	}

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

}
