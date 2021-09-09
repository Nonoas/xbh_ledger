package indi.nonoas.xbh.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Account {
    @Id
    private long id;
    private String accName;
    private int acc_type;
    @Generated(hash = 1869848303)
    public Account(long id, String accName, int acc_type) {
        this.id = id;
        this.accName = accName;
        this.acc_type = acc_type;
    }
    @Generated(hash = 882125521)
    public Account() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
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
}
