package indi.nonoas.xbh.fragment.stats;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import indi.nonoas.xbh.pojo.AccBalance;

public class StatsViewModel extends ViewModel {

    private MutableLiveData<List<AccBalance>> totBalanceList;

    public StatsViewModel() {
        totBalanceList = new MutableLiveData<>();
        totBalanceList.setValue(new ArrayList<>());
    }

    public List<AccBalance> getTotBalanceList() {
        return totBalanceList.getValue();
    }

    public MutableLiveData<List<AccBalance>> getTotBalanceListData() {
        return totBalanceList;
    }

    public void setTotBalanceList(List<AccBalance> accBalances) {
        totBalanceList.setValue(accBalances);
    }
}