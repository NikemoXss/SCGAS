package com.lmq.main.item;

import org.json.JSONObject;

/**
 * 投资管理
 */
public class InvestManageItem {

	private String borrow_name; // 标名称
	private String borrow_id; // 投标ID
	private String danbao; // 投资 担保名称
	private String borrow_interest_rate; // 投标利率
	private String investor_capital;// 投标金额
	private String invest_time;// 投标添加时间
	private String deadline;// 结束时间
	private String interest_rate; // 债权利率
	private String addtime; // 债权添加时间
	private String id; // 债权ID
	private String money;// 转让本金
	private String total_periods;// 已认购债权（转让期数/总期数）
	private String buy_money;// 已认购债权（购买价格）

	private Long borrow_money;
	private String borrow_duration;
	private String capital_interest;
	private String back;
	private String total;
	private String repayment_time;
	private String receive_money;
	private Long capital;
	private String breakday;
	private String interest;
	private String receive_capital;
	private String receive_interest;
	private double interest_fee;
	private String status;
	private int sort_order;
	private String transfer_price;
	private String add_time;
	private String invest_id;
	private String period;
	private String total_period;
	private String buy_time;
	private int dbsatus;
	private String user_name;
	private String has_pay;
	private String cancel_times;
	private String remark;
	private String cancel_time;
	public String investor_interest;

	public InvestManageItem(JSONObject json) {
		if (json.has("cancel_time")) {

			cancel_time = json.optString("cancel_time", "-");
		}
		if (json.has("remark")) {

			remark = json.optString("remark", "-");
		}
		if (json.has("cancel_times")) {

			cancel_times = json.optString("cancel_times", "-");
		}
		if (json.has("has_pay")) {

			has_pay = json.optString("has_pay", "-");
		}
		if (json.has("user_name")) {

			user_name = json.optString("user_name", "-");
		}
		if (json.has("dbsatus")) {

			dbsatus = json.optInt("dbsatus", 0);
		}
		if (json.has("buy_time")) {

			buy_time = json.optString("buy_time", "0");
		}
		if (json.has("buy_time")) {

			buy_time = json.optString("buy_time", "0");
		}
		if (json.has("total_period")) {

			total_period = json.optString("total_period", "0");
		}
		if (json.has("period")) {

			period = json.optString("period", "0");
		}
		if (json.has("invest_id")) {

			invest_id = json.optString("invest_id", "0");
		}
		if (json.has("add_time")) {

			add_time = json.optString("add_time", "0");
		}
		if (json.has("transfer_price")) {

			transfer_price = json.optString("transfer_price", "0");
		}
		if (json.has("sort_order")) {

			sort_order = json.optInt("sort_order", 0);
		}
		if (json.has("status")) {

			status = json.optString("status", "-");
		}
		if (json.has("interest_fee")) {

			interest_fee = json.optDouble("interest_fee", 0);
		}
		if (json.has("receive_interest")) {

			receive_interest = json.optString("receive_interest", "");
		}
		if (json.has("receive_capital")) {

			receive_capital = json.optString("receive_capital", "");
		}
		if (json.has("interest")) {

			interest = json.optString("interest", "");
		}
		if (json.has("breakday")) {

			breakday = json.optString("breakday", "");
		}
		if (json.has("capital")) {

			capital = json.optLong("capital", 0);
		}
		if (json.has("receive_money")) {

			receive_money = json.optString("receive_money", "");
		}
		if (json.has("repayment_time")) {

			repayment_time = json.optString("repayment_time", "");
		}
		if (json.has("total")) {

			total = json.optString("total", "");
		}
		if (json.has("back")) {

			back = json.optString("back", "");
		}
		if (json.has("capital_interest")) {

			capital_interest = json.optString("capital_interest", "");
		}
		if (json.has("borrow_duration")) {

			borrow_duration = json.optString("borrow_duration", "");
		}
		if (json.has("borrow_money")) {

			borrow_money = json.optLong("borrow_money", 0);
		}
		if (json.has("buy_money")) {

			buy_money = json.optString("buy_money", "");
		}
		if (json.has("total_periods")) {

			total_periods = json.optString("total_periods", "");
		}
		if (json.has("money")) {

			money = json.optString("money", "");
		}
		if (json.has("borrow_name")) {

			borrow_name = json.optString("borrow_name", "");
			if("null".equals(borrow_name)){
				borrow_name="测试";
			}
		}

		if (json.has("borrow_id")) {

			borrow_id = json.optString("borrow_id", "");
		}

		if (json.has("danbao")) {

			danbao = json.optString("danbao", "");
		}

		if (json.has("borrow_interest_rate")) {

			borrow_interest_rate = json.optString("borrow_interest_rate", "");
			if("null".equals(borrow_interest_rate)){
				borrow_interest_rate="测试";
			}
			
		}

		if (json.has("investor_capital")) {

			investor_capital = json.optString("investor_capital", "");
		}

		if (json.has("invest_time")) {

			invest_time = json.optString("invest_time", "");
		}
		if (json.has("deadline")) {

			deadline = json.optString("deadline", "");
		}

		if (json.has("interest_rate")) {

			interest_rate = json.optString("interest_rate", "");
		}
		if (json.has("id")) {

			id = json.optString("id", "");
		}
		if (json.has("addtime")) {

			addtime = json.optString("addtime", "");
		}
		if (json.has("investor_interest")) {
			
			investor_interest = json.optString("investor_interest", "");
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getInterest_rate() {
		return interest_rate;
	}

	public void setInterest_rate(String interest_rate) {
		this.interest_rate = interest_rate;
	}

	public String getBorrow_name() {
		return borrow_name;
	}

	public void setBorrow_name(String borrow_name) {
		this.borrow_name = borrow_name;
	}

	public String getBorrow_id() {
		return borrow_id;
	}

	public void setBorrow_id(String borrow_id) {
		this.borrow_id = borrow_id;
	}

	public String getDanbao() {
		return danbao;
	}

	public void setDanbao(String danbao) {
		this.danbao = danbao;
	}

	public String getBorrow_interest_rate() {
		return borrow_interest_rate;
	}

	public void setBorrow_interest_rate(String borrow_interest_rate) {
		this.borrow_interest_rate = borrow_interest_rate;
	}

	public String getInvestor_capital() {
		return investor_capital;
	}

	public void setInvestor_capital(String investor_capital) {
		this.investor_capital = investor_capital;
	}

	public String getInvest_time() {
		return invest_time;
	}

	public void setInvest_time(String invest_time) {
		this.invest_time = invest_time;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getTotal_periods() {
		return total_periods;
	}

	public void setTotal_periods(String total_periods) {
		this.total_periods = total_periods;
	}

	public String getBuy_money() {
		return buy_money;
	}

	public void setBuy_money(String buy_money) {
		this.buy_money = buy_money;
	}

	public String getBorrow_duration() {
		return borrow_duration;
	}

	public void setBorrow_duration(String borrow_duration) {
		this.borrow_duration = borrow_duration;
	}

	public String getCapital_interest() {
		return capital_interest;
	}

	public void setCapital_interest(String capital_interest) {
		this.capital_interest = capital_interest;
	}

	public String getBack() {
		return back;
	}

	public void setBack(String back) {
		this.back = back;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getRepayment_time() {
		return repayment_time;
	}

	public void setRepayment_time(String repayment_time) {
		this.repayment_time = repayment_time;
	}

	public String getReceive_money() {
		return receive_money;
	}

	public void setReceive_money(String receive_money) {
		this.receive_money = receive_money;
	}

	public String getBreakday() {
		return breakday;
	}

	public void setBreakday(String breakday) {
		this.breakday = breakday;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getReceive_capital() {
		return receive_capital;
	}

	public void setReceive_capital(String receive_capital) {
		this.receive_capital = receive_capital;
	}

	public String getReceive_interest() {
		return receive_interest;
	}

	public void setReceive_interest(String receive_interest) {
		this.receive_interest = receive_interest;
	}

	public Long getBorrow_money() {
		return borrow_money;
	}

	public void setBorrow_money(Long borrow_money) {
		this.borrow_money = borrow_money;
	}

	public Long getCapital() {
		return capital;
	}

	public void setCapital(Long capital) {
		this.capital = capital;
	}

	public double getInterest_fee() {
		return interest_fee;
	}

	public void setInterest_fee(double interest_fee) {
		this.interest_fee = interest_fee;
	}

	public int getSort_order() {
		return sort_order;
	}

	public void setSort_order(int sort_order) {
		this.sort_order = sort_order;
	}

	public String getTransfer_price() {
		return transfer_price;
	}

	public void setTransfer_price(String transfer_price) {
		this.transfer_price = transfer_price;
	}

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

	public String getInvest_id() {
		return invest_id;
	}

	public void setInvest_id(String invest_id) {
		this.invest_id = invest_id;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getTotal_period() {
		return total_period;
	}

	public void setTotal_period(String total_period) {
		this.total_period = total_period;
	}

	public String getBuy_time() {
		return buy_time;
	}

	public void setBuy_time(String buy_time) {
		this.buy_time = buy_time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getDbsatus() {
		return dbsatus;
	}

	public void setDbsatus(int dbsatus) {
		this.dbsatus = dbsatus;
	}

	public String getHas_pay() {
		return has_pay;
	}

	public void setHas_pay(String has_pay) {
		this.has_pay = has_pay;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getCancel_times() {
		return cancel_times;
	}

	public void setCancel_times(String cancel_times) {
		this.cancel_times = cancel_times;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCancel_time() {
		return cancel_time;
	}

	public void setCancel_time(String cancel_time) {
		this.cancel_time = cancel_time;
	}

	@Override
	public String toString() {
		return "InvestManageItem{" + "borrow_name='" + borrow_name + '\'' + ", borrow_id='" + borrow_id + '\'' + ", danbao='" + danbao + '\'' + ", borrow_interest_rate='" + borrow_interest_rate + '\'' + ", investor_capital='" + investor_capital + '\'' + ", invest_time='" + invest_time + '\'' + ", deadline='" + deadline + '\'' + '}';
	}
}
