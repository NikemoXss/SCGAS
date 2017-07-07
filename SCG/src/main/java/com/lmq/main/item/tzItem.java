package com.lmq.main.item;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;

import com.lmq.main.api.MyLog;

@SuppressLint("SimpleDateFormat")
public class tzItem {
	// 发标人id
	private long uid;
	// 标id
	private long id;
	// 标种类型 2 流转标 3表示普通标 4担保标 5秒还标
	// 6净值标
	// 7抵押标 101债权转让
	private int type;
	// 标进度
	private double progress;
	// 标种名称
	private String name;
	// 投标人头像
	private String peopic;
	// 投标人名称
	private String peoname;
	// 发标日期
	private String time;
	// 投标总次数
	private int tzcs;
	// 标奖励
	private String jl;
	// 年化利率
	private double nhll;
	// 定向标密码
	private int suo;
	// 借款期限
	private String jkqx;
	// 借款金额
	private long money;
	// 奖励积分
	private long credits;
	// 借款方式
	private String jkfs;
	// 借款方式
	private int jkfsType;
	// 最小投标金额
	private Double zxtbje;
	// 最大投标金额
	private String zdtbje;
	// 剩余时间
	private String sysj;
	// 标种描述
	private String info;
	// 标种风险控制
	private String info2;
	// 还需要金额
	private double hxje;

	// 是否推荐标 0未推荐 1推荐
	private int suggest = 0;

	// 借款说明
	private String borrowInfo;

	// 债券转让 新增字段

	// 到期待收本息
	private double dq_money;

	// 资金保障
	private String borrowCapital;

	// 资金用途
	private String borrowUse;

	// 定投宝 收益方式
	private String borrowBenefit;

	// 是否可以购买 Flag
	private int valid;

	/** 标状态 */
	private int borrow_status;

	private ArrayList<String> imageArray;

	private int cs_type;
	// 债权转让 转让期限
	private String period;
	// 借款人uid
	private long borrow_uid;
	/**
	 * 还款方式
	 */
	private String repayment_name;
	/**
	 * 最大限额
	 */
	private String borrow_use;
	/**
	 * 担保机构
	 */
	private String danbao_name;
	private String per_transfer;

	public tzItem() {}

	public void init(JSONObject json) {
		try {
			initInfo(json);

			if (json.has("per_transfer")) {
				per_transfer = json.optString("per_transfer");
			}
			if (json.has("danbao_name")) {
				danbao_name = json.optString("danbao_name");
			} else {
				danbao_name = "暂无担保机构";
			}
			if (json.has("borrow_use")) {

				if (json.optString("borrow_use") == null || json.optString("borrow_use").equals("null")) {

					borrow_use = "暂无信息";

				} else {

					borrow_use = json.optString("borrow_use", "");
				}

			}
			if (json.has("repayment_name")) {
				repayment_name = json.optString("repayment_name");
			}
			if (json.has("huankuan_type")) {
				jkfs = json.optString("huankuan_type");
			}
			if (json.has("borrow_info")) {
				info = json.optString("borrow_info");
			}
			if (json.has("borrow_risk")) {
				info2 = json.optString("borrow_risk");
			}
			if (json.has("borrow_min")) {
				zxtbje = json.getDouble("borrow_min");
			}
			if (json.has("borrow_max")) {
				zdtbje = json.optString("borrow_max");
			}
			if (json.has("need")) {
				hxje = json.getDouble("need");
			}
			if (json.has("lefttime")) {
				sysj = json.optString("lefttime");
			}
			if (json.has("invest_num")) {
				tzcs = json.getInt("invest_num");
			}
			if (json.has("addtime")) {
				time = json.optString("addtime", "");
			}
			if (json.has("reward")) {
				jl = json.optString("reward");
			}
			if (json.has("suggest")) {
				suggest = json.optInt("suggest", 0);
			}

			if (json.has("borrow_status")) {
				borrow_status = json.optInt("borrow_status", -1);
			}

			if (json.has("period")) {
				period = json.optString("period", "测试");
			}
			if (json.has("borrow_uid")) {
				borrow_uid = json.optInt("borrow_uid", 0);
			}

			if (json.has("arrimg_path")) {
				imageArray = new ArrayList<String>();
				JSONArray array = json.optJSONArray("arrimg_path");
				for (int i = 0; i < array.length(); i++) {
					imageArray.add(array.getString(i));
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<String> getImageArray() {
		return imageArray;
	}

	public void initInfo(JSONObject json) {
		try {

			if (json.has("uid")) {
				uid = json.getLong("uid");
			}

			if (json.has("id")) {
				id = json.getLong("id");
			}

			if (json.has("class")) {
				cs_type = json.optInt("class");
			}

			if (json.has("type") && json.get("type") != null) {
				type = json.getInt("type");
			}
			if (json.has("invest_num")) {
				tzcs = json.getInt("invest_num");
			}

			if (json.has("progress")) {
				progress = json.getDouble("progress");
			}

			if (json.has("borrow_name")) {
				name = json.optString("borrow_name");
			}

			if (json.has("borrow_interest_rate")) {
				nhll = json.getDouble("borrow_interest_rate");
			}

			if (json.has("repayment_type")) {
				jkfsType = json.getInt("repayment_type");
			}

			if (json.has("borrow_risk")) {
				info2 = json.optString("borrow_risk");
			}

			if (json.has("borrow_duration")) {
				jkqx = json.optString("borrow_duration");
			}

			if (json.has("borrow_money")) {
				money = json.getLong("borrow_money");
			}

			if (json.has("credits")) {
				credits = json.getLong("credits");
			}

			if (json.has("imgpath")) {
				peopic = json.optString("imgpath");
			}

			if (json.has("borrow_benefit")) {
				borrowBenefit = json.optString("borrow_benefit");
			}

			if (json.has("user_name")) {
				peoname = json.optString("user_name");
			}

			if (json.has("borrow_info")) {
				borrowInfo = json.optString("borrow_info");
			}

			if (json.has("borrow_capital")) {
				borrowCapital = json.optString("borrow_capital");
			}

			if (json.has("borrow_use")) {
				borrowUse = json.optString("borrow_use");
			}

			if (json.has("suo")) {
				suo = json.getInt("suo");
			}

			if (json.has("dq_money")) {
				dq_money = json.getDouble("dq_money");
			}

			if (json.has("valid")) {
				valid = json.getInt("valid");
			}

		} catch (Exception e) {
			MyLog.d("error", "加载tzItem基础信息错误");
			e.printStackTrace();
		}
	}

	// ////////////////////////////////////////////////////////////

	public long getUid() {
		return uid;
	}

	public String getBorrowInfo() {
		return borrowInfo;
	}

	public void setBorrowInfo(String borrowInfo) {
		this.borrowInfo = borrowInfo;
	}

	public int getCs_type() {
		return cs_type;
	}

	public void setCs_type(int cs_type) {
		this.cs_type = cs_type;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getBorrow_status() {
		return borrow_status;
	}

	public void setBorrow_status(int borrow_status) {
		this.borrow_status = borrow_status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getBorrowCapital() {
		return borrowCapital;
	}

	public void setBorrowCapital(String borrowCapital) {
		this.borrowCapital = borrowCapital;
	}

	public String getBorrowUse() {
		return borrowUse;
	}

	public void setBorrowUse(String borrowUse) {
		this.borrowUse = borrowUse;
	}

	public double getProgress() {
		return progress;
	}

	public String getBorrowBenefit() {
		return borrowBenefit;
	}

	public void setBorrowBenefit(String borrowBenefit) {
		this.borrowBenefit = borrowBenefit;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo2(String info) {
		info2 = info;
	}

	public String getInfo2() {
		return info2;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public double getNhll() {
		return nhll;
	}

	public void setNhll(double nhll) {
		this.nhll = nhll;
	}

	public String getJkqx() {
		return jkqx;
	}

	public void setJkqx(String jkqx) {
		this.jkqx = jkqx;
	}

	public String getJkfs() {
		return jkfs;
	}

	public void setJkfs(String jkfs) {
		this.jkfs = jkfs;
	}

	public long getMoney() {
		return money;
	}

	public void setMoney(long money) {
		this.money = money;
	}

	public String getZdtbje() {
		return zdtbje;
	}

	public void setZdtbje(String zdtbje) {
		this.zdtbje = zdtbje;
	}

	public String getSysj() {
		return sysj;
	}

	public void setSysj(String sysj) {
		this.sysj = sysj;
	}

	public long getCredits() {
		return credits;
	}

	public void setCredits(long credits) {
		this.credits = credits;
	}

	public int getJkfsType() {
		return jkfsType;
	}

	public void setJkfsType(int jkfsType) {
		this.jkfsType = jkfsType;
	}

	public String getPeopic() {
		return peopic;
	}

	public void setPeopic(String peopic) {
		this.peopic = peopic;
	}

	public String getPeoname() {
		return peoname;
	}

	public void setPeoname(String peoname) {
		this.peoname = peoname;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getTzcs() {
		return tzcs;
	}

	public void setTzcs(int tzcs) {
		this.tzcs = tzcs;
	}

	public String getJl() {
		return jl;
	}

	public void setJl(String jl) {
		this.jl = jl;
	}

	public int getSuo() {
		return suo;
	}

	public void setSuo(int suo) {
		this.suo = suo;
	}

	public int getSuggest() {
		return suggest;
	}

	public void setSuggest(int suggest) {
		this.suggest = suggest;
	}

	public double getDq_money() {
		return dq_money;
	}

	public void setDq_money(double dq_money) {
		this.dq_money = dq_money;
	}

	public int getValid() {
		return valid;
	}

	public void setValid(int valid) {
		this.valid = valid;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public long getBorrow_uid() {
		return borrow_uid;
	}

	public void setBorrow_uid(long borrow_uid) {
		this.borrow_uid = borrow_uid;
	}

	public Double getZxtbje() {
		return zxtbje;
	}

	public void setZxtbje(Double zxtbje) {
		this.zxtbje = zxtbje;
	}

	public double getHxje() {
		return hxje;
	}

	public void setHxje(double hxje) {
		this.hxje = hxje;
	}

	public String getRepayment_name() {
		return repayment_name;
	}

	public void setRepayment_name(String repayment_name) {
		this.repayment_name = repayment_name;
	}

	public String getBorrow_use() {
		return borrow_use;
	}

	public void setBorrow_use(String borrow_use) {
		this.borrow_use = borrow_use;
	}

	public String getDanbao_name() {
		return danbao_name;
	}

	public void setDanbao_name(String danbao_name) {
		this.danbao_name = danbao_name;
	}

	public String getPer_transfer() {
		return per_transfer;
	}

	public void setPer_transfer(String per_transfer) {
		this.per_transfer = per_transfer;
	}

	@Override
	public String toString() {
		return "tzItem [uid=" + uid + ", id=" + id + ", type=" + type + ", progress=" + progress + ", name=" + name + ", peopic=" + peopic + ", peoname=" + peoname + ", time=" + time + ", tzcs=" + tzcs + ", jl=" + jl + ", nhll=" + nhll + ", suo=" + suo + ", jkqx=" + jkqx + ", money=" + money + ", credits=" + credits + ", jkfs=" + jkfs + ", jkfsType=" + jkfsType + ", zxtbje=" + zxtbje + ", zdtbje=" + zdtbje + ", sysj=" + sysj + ", info=" + info + ", info2=" + info2 + ", hxje=" + hxje + ", suggest=" + suggest + ", dq_money=" + dq_money + ", valid=" + valid + "]";
	}

}
