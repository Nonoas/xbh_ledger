package indi.nonoas.xbh.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class User {

	@Id(autoincrement = true)
	private long id;
	private String name;

	@Generated(hash = 1144922831)
	public User(long id, String name) {
		this.id = id;
		this.name = name;
	}

	@Generated(hash = 586692638)
	public User() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
