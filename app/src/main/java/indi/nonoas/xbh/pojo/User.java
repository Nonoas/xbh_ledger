package indi.nonoas.xbh.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class User {

	@Id(autoincrement = true)
	private Long userId;
	private String name;

	@Generated(hash = 2030012975)
	public User(Long userId, String name) {
		this.userId = userId;
		this.name = name;
	}

	@Generated(hash = 586692638)
	public User() {
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
