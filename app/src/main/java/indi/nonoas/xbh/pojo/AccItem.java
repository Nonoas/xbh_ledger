package indi.nonoas.xbh.pojo;

/**
 * @author Nonoas
 * @date 2021-09-25 12:48
 */
public class AccItem {
	private Long id;
	private String accName;
	private int acc_type;
	private int resId;
	private String srcUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public int getAcc_type() {
		return acc_type;
	}

	public void setAcc_type(int acc_type) {
		this.acc_type = acc_type;
	}

	public int getResId() {
		return resId;
	}

	public void setResId(int resId) {
		this.resId = resId;
	}

	public String getSrcUrl() {
		return srcUrl;
	}

	public void setSrcUrl(String srcUrl) {
		this.srcUrl = srcUrl;
	}
}
