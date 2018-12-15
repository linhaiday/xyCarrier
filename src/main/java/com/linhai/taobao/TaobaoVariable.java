package com.linhai.taobao;

import com.alibaba.fastjson.JSONObject;
import com.linhai.DataSource.DataSource;

/**
 * Created by linhai on 2018/12/15.
 */
public class TaobaoVariable {

    public static void clean(JSONObject self,JSONObject partner,JSONObject application,JSONObject result){


    }


    public static void main(String[] args) {

        JSONObject result = new JSONObject();
        //封装自己的数据源
        JSONObject self = JSONObject.parseObject(DataSource.dataSource());
        //封装配偶的数据源
        JSONObject partner = JSONObject.parseObject(DataSource.dataSource());
        //申请人信息
        JSONObject applicant = JSONObject.parseObject(DataSource.applicant());
        //数据清洗入口方法
        try {
            clean(self.getJSONObject("data"),partner.getJSONObject("data"),applicant.getJSONObject("userInfo"),result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //遍历结果集
        System.out.println(result.toJSONString());
    }
}
