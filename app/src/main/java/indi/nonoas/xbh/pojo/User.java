package indi.nonoas.xbh.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class User {

	@Id
	private String userId;
	private String name;
	private String password;

	@Generated(hash = 1423317323)
	public User(String userId, String name, String password) {
		this.userId = userId;
		this.name = name;
		this.password = password;
	}

	@Generated(hash = 586692638)
	public User() {
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
