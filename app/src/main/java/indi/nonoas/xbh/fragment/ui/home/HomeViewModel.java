package indi.nonoas.xbh.fragment.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import indi.nonoas.xbh.pojo.AccBalance;

public class HomeViewModel extends ViewModel {

	/**
	 * 余额列表
	 */
	private final MutableLiveData<List<AccBalance>> mBalanceList;

	/**
	 * 总余额
	 */
	private final MutableLiveData<String> mBalance;

	public HomeViewModel() {
		mBalance = new MutableLiveData<>();
		mBalanceList = new MutableLiveData<>();
		mBalance.setValue("0");
		mBalanceList.setValue(new ArrayList<>());
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

	public void setBalanceList(List<AccBalance> list) {
		mBalanceList.setValue(list);
	}

	public MutableLiveData<List<AccBalance>> getBalanceListData() {
		return mBalanceList;
	}

	public List<AccBalance> getBalanceList() {
		return mBalanceList.getValue();
	}
}
