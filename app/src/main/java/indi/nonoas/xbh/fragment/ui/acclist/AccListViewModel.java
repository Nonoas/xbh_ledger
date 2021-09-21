package indi.nonoas.xbh.fragment.ui.acclist;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @author Nonoas
 * @date 2021-09-21 21:49
 */
public class AccListViewModel extends ViewModel {

	/**
	 * 总余额
	 */
	private final MutableLiveData<String> mBalance;

	public AccListViewModel() {
		mBalance = new MutableLiveData<>();
		mBalance.setValue("0");
	}

	public MutableLiveData<String> getBalanceData() {
		return mBalance;
	}

	public String getBalance() {
		return mBalance.getValue();
	}

	/**
	 * 修改总余额
	 *
	 * @param str 总余额
	 */
	public void setBalance(String str) {
		mBalance.setValue(str);
	}


}
