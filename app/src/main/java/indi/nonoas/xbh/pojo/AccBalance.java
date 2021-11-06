package indi.nonoas.xbh.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

/**
 * @author Nonoas
 * @date 2021-09-20 23:27
 */
@Entity(indexes = {@Index(
		value = "accNo DESC, date DESC", unique = true
)})
public class AccBalance {
	@Id(autoincrement = true)
	private Long serialNo;
	private String accNo;
	private Long date;
	private String userId;
	private String accName;
	private String balance;

	@Transient
	private int iconId;
	@Transient
	private String iconUrl;
	@Transient
	private String totBalance;

	@Generated(hash = 1785955755)
	public AccBalance(Long serialNo, String accNo, Long date, String userId, String accName,
			String balance) {
		this.serialNo = serialNo;
		this.accNo = accNo;
		this.date = date;
		this.userId = userId;
		this.accName = accName;
		this.balance = balance;
	}

	@Generated(hash = 2030343539)
	public AccBalance() {
	}

	public Long getSerialNo() {
		return this.serialNo;
	}

	public void setSerialNo(Long serialNo) {
		this.serialNo = serialNo;
	}

	public String getAccNo() {
		return this.accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public Long getDate() {
		return this.date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public String getAccName() {
		return this.accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public String getBalance() {
		return this.balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public int getIconId() {
		return iconId;
	}

	public void setIconId(int iconId) {
		this.iconId = iconId;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTotBalance() {
		return totBalance;
	}

	public void setTotBalance(String totBalance) {
		this.totBalance = totBalance;
	}
}
