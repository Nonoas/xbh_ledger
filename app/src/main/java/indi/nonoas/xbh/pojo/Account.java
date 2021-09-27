package indi.nonoas.xbh.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Account {
	@Id(autoincrement = true)
	private Long id;
	private String accName;
	private int acc_type;
	private String initBalance = "0";
	private int iconId;
	private String iconUrl;

	@Generated(hash = 279537611)
	public Account(Long id, String accName, int acc_type, String initBalance,
			int iconId, String iconUrl) {
		this.id = id;
		this.accName = accName;
		this.acc_type = acc_type;
		this.initBalance = initBalance;
		this.iconId = iconId;
		this.iconUrl = iconUrl;
	}

	@Generated(hash = 882125521)
	public Account() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccName() {
		return this.accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public int getAcc_type() {
		return this.acc_type;
	}

	public void setAcc_type(int acc_type) {
		this.acc_type = acc_type;
	}

	public String getInitBalance() {
		return this.initBalance;
	}

	public void setInitBalance(String initBalance) {
		this.initBalance = initBalance;
	}

	public int getIconId() {
		return this.iconId;
	}

	public void setIconId(int iconId) {
		this.iconId = iconId;
	}

	public String getIconUrl() {
		return this.iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
}
