package indi.nonoas.xbh.fragment.stats;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import indi.nonoas.xbh.pojo.AccBalance;

public class StatsViewModel extends ViewModel {

    private MutableLiveData<List<AccBalance>> mBalanceList;

    public StatsViewModel() {
        mBalanceList = new MutableLiveData<>();
        mBalanceList.setValue(new ArrayList<>());
    }

    public List<AccBalance> getBalanceList() {
        return mBalanceList.getValue();
    }

    public MutableLiveData<List<AccBalance>> getBalanceListData() {
        return mBalanceList;
    }

    public void setBalanceList(List<AccBalance> accBalances) {
        mBalanceList.setValue(accBalances);
    }
}