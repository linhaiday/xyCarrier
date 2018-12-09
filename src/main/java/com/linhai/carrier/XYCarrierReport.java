package com.linhai.carrier;

import com.alibaba.fastjson.JSONObject;
import com.linhai.DataSource.DataSource;
import com.linhai.rule.DataRule;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by linhai on 2018/11/25.
 */
public class XYCarrierReport {

    /**
     * 数据清洗方法   输入JSONObject 输出Map
     * @param data
     * @param result
     */
    public static void clean(JSONObject data,Map<String,Object> result){

        if(data==null || data.isEmpty()) return;
        //System.out.println("字符集不可为空");

        Iterator it = data.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String,JSONObject> entry = (Map.Entry<String,JSONObject>)it.next();
            String key = entry.getKey();
            instance(key,data.get(key),result);
        }

    }

    /**
     * 数据清洗方法   输入list 输出Map
     * @param list
     * @param result
     */
    public static void clean(List list,Map<String,Object> result){

        if(list==null || list.isEmpty()) return;
        //System.out.println("字符集不可为空");

        for (int i = 0; i < list.size(); i++) {
            instance("",list.get(i),result);
        }

    }

    /**
     * 数据清洗方法   输入Map 输出Map
     * @param map
     * @param result
     */
    public static void clean(Map<String,Object> map,Map<String,Object> result){

        if(map==null || map.isEmpty()) return;
        //System.out.println("字符集不可为空");

        for (String key:map.keySet()) {
            instance(key,map.get(key),result);
        }

    }

    /**
     * 数据类型对比
     * @param key
     * @param obj
     * @param result
     */
    public static void instance(String key,Object obj,Map<String,Object> result){
        if(obj instanceof Map) clean((Map) obj,result);
        else if(obj instanceof List) clean((List) obj,result);
        else if(obj instanceof JSONObject) clean((JSONObject) obj,result);
        else save(key,obj,result);
    }

    public static void save(String key,Object obj,Map<String,Object> result){
        if(key==null || "".equals(key) || obj==null) return;
        Map<String,Object> map = DataRule.report();
        if(map.get(key)!=null)
            result.put(key,obj.toString());
    }

    /**
     * 测试类main方法
     * @param args
     */
    public static void main(String[] args) {
        JSONObject result = new JSONObject();
        //封装数据源
        JSONObject jsonObject = JSONObject.parseObject(DataSource.dataSourceForReport());
        //数据清洗入口方法
        clean(jsonObject,result);
        //遍历结果集
        System.out.println(result.toJSONString());
    }



}
