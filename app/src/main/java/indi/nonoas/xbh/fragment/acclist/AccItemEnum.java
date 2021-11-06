package indi.nonoas.xbh.fragment.acclist;

import java.util.HashMap;
import java.util.Map;

import indi.nonoas.xbh.R;

@SuppressWarnings("all")
public enum AccItemEnum {

    ALIPAY("00001", "支付宝", R.drawable.ic_alipay),

    WEIXIN("00002", "微信", R.drawable.ic_weixin),

    DFCF("00003", "东方财富", R.drawable.ic_dfcf),

    ZGYH("00004", "中国银行", R.drawable.ic_zgyh),

    YZYH("00005", "邮政银行", R.drawable.ic_yzyh),

    ZSYH("00006", "招商银行", R.drawable.ic_zsyh),

    JIANGSUYH("00007", "江苏银行", R.drawable.ic_jiangsuyh),

    JSYH("00008", "建设银行", R.drawable.ic_jsyh),

    OTHER("00000", "其他账户", R.drawable.ic_other_acc);

    private HashMap<String, Object> map;

    /**
     * 本地图标ID映射 map：根据 账户id取到图标ID
     */
    public static final HashMap<String, Integer> ICONID_MAP = new HashMap<>();

    public static final String K_ID = "id";
    public static final String K_IMG = "img";
    public static final String K_NAME = "name";

    static {
        putToMap(ALIPAY);
        putToMap(WEIXIN);
        putToMap(DFCF);
        putToMap(ZGYH);
        putToMap(YZYH);
        putToMap(ZSYH);
        putToMap(JIANGSUYH);
        putToMap(JSYH);
        putToMap(OTHER);
    }

    AccItemEnum(String accId, String accName, int iconId) {
        map = new HashMap<>(4, 1);
        map.put(K_ID, accId);
        map.put(K_NAME, accName);
        map.put(K_IMG, iconId);
    }

    public String getAccName() {
        return (String) map.get(K_NAME);
    }

    public String getAccId() {
        return (String) map.get(K_ID);
    }

    public int getIconId() {
        return (int) map.get(K_IMG);
    }

    public Map<String, Object> getMap() {
        return map;
    }

    private static void putToMap(AccItemEnum accItem) {
        ICONID_MAP.put(accItem.getAccId(), accItem.getIconId());
    }

}
