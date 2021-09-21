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

	@Generated(hash = 1463076452)
	public Account(Long id, String accName, int acc_type, String initBalance) {
		this.id = id;
		this.accName = accName;
		this.acc_type = acc_type;
		this.initBalance = initBalance;
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
}
