package com.linhai.enums;

/**
 * Created by linhai on 2018/11/28.
 */
public enum Enums {

    MOBILE("中国移动","CHINA_MOBILE"),UNICOM("中国联通","CHUNA_UNICOM"),TELECOME("中国电信","CHINA_TELECOME"),
    LT("小于","1"),LOE("小于等于","2"),GT("大于","3"),GTOET("大于等于","4")
    ;

    // 成员变量
    private String name;
    private String index;

    // 构造方法
    private Enums(String name, String index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(String index) {
        for (Enums c : Enums.values()) {
            if (c.getIndex().equals(index)) {
                return c.name;
            }
        }
        return null;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
