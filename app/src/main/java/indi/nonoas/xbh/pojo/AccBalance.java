package indi.nonoas.xbh.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

import java.math.BigDecimal;

/**
 * @author Nonoas
 * @date 2021-09-20 23:27
 */
@Entity(indexes = {@Index(
		value = "accId DESC, timestamp DESC", unique = true
)})
public class AccBalance {
	@Id(autoincrement = true)
	private Long id;
	private Long accId;
	private Long timestamp;
	private String accName;
	private String balance;

	@Generated(hash = 2116256628)
	public AccBalance(Long id, Long accId, Long timestamp, String accName,
	                  String balance) {
		this.id = id;
		this.accId = accId;
		this.timestamp = timestamp;
		this.accName = accName;
		this.balance = balance;
	}

	@Generated(hash = 2030343539)
	public AccBalance() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAccId() {
		return this.accId;
	}

	public void setAccId(Long accId) {
		this.accId = accId;
	}

	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
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

}
