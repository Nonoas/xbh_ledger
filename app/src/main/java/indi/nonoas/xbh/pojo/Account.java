package indi.nonoas.xbh.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Account {
	@Id
	private String id;
	private String accName;
	private int accType;
	private String initBalance = "0";
	private int iconId;
	private String iconUrl;

	@Generated(hash = 88043702)
	public Account(String id, String accName, int accType, String initBalance,
			int iconId, String iconUrl) {
		this.id = id;
		this.accName = accName;
		this.accType = accType;
		this.initBalance = initBalance;
		this.iconId = iconId;
		this.iconUrl = iconUrl;
	}

	@Generated(hash = 882125521)
	public Account() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccName() {
		return this.accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public int getAccType() {
		return this.accType;
	}

	public void setAccType(int accType) {
		this.accType = accType;
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
