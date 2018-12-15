package com.linhai.comm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linhai.enums.Enums;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by linhai on 2018/11/27.
 */
public class Algorithm {

    public static final ThreadLocal<Map<String,String>> mobileFrom = ThreadLocal.withInitial(HashMap::new);

    private static String getMobileFrom(String mobileNumber) throws Exception{
        Map<String,String> map = mobileFrom.get();
        if(!PhoneNumberHelper.isMobilePhone(mobileNumber)) return "";
        if(map.get(mobileNumber)==null || "".equals(map.get(mobileNumber))) {
            String phone_local = MobileFromUtil.getMobileFrom(mobileNumber);
            map.put(mobileNumber, phone_local);
            return phone_local;
        }

        return map.get(mobileNumber);
    }

    //手机号归属地
    public static void phoneLocal(JSONObject data, JSONObject result) throws Exception{

        String phone_local = getMobileFrom(data.get("mobile").toString());
        System.out.println("手机号归属地:"+phone_local);
        result.put("phone_local",phone_local);
    }

    //运营商身份证号码
    public static void operatorId(JSONObject data, JSONObject result){

        System.out.println("运营商身份证号码:"+data.get("carrier"));
        result.put("operator_id",data.get("carrier"));
    }

    //运营商姓名
    public static void operatorName(JSONObject data, JSONObject result){

        System.out.println("运营商姓名:"+ Enums.getName(data.get("carrier").toString()));

        result.put("operator_name",Enums.getName(data.get("carrier").toString()));
    }

    //近1个月联系号码数
    //近1个月联系十次以上的号码数量
    //近1个月互通电话的号码数量
    public static void contactNumberCountByNearlyAMonth(JSONObject data, JSONObject applicant, JSONObject result){

        result.put("good_friend_num_1m",0);
        result.put("friend_num_1m",0);
        result.put("inter_peer_num_1m",0);
        //联系过的号码
        Map<String,Integer> map = new HashMap<>();
        //归属地
        Map<String,Integer> location = new HashMap<>();
        //主叫号码
        Set<String> dial = new HashSet<>();
        //被叫号码
        Set<String> dialed = new HashSet<>();

        //得到1个月前的时间
        String monthBefore = CarrierDateUtil.monthsBefore(1,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        String month = CarrierDateUtil.yearMonth(monthBefore);
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject)jsonArray.get(i);
            //如果1个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
            if(CarrierDateUtil.compareDate(month+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==1) continue;
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //如果1个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
                if(CarrierDateUtil.compareDate(monthBefore,day.get("time").toString(),"yyyy-MM-dd")<1){
                    //联系的号码数
                    if(map.get(day.get("peer_number").toString())==null) map.put(day.get("peer_number").toString(),1);
                    else map.put(day.get("peer_number").toString(),map.get(day.get("peer_number").toString())+1);
                    //联系号码的归属地
                    if(location.get(day.get("location").toString())==null) location.put(day.get("location").toString(),1);
                    else location.put(day.get("location").toString(),location.get(day.get("location").toString())+1);
                    //主叫号码与被叫号码
                    if("DIAL".equals(day.get("dial_type").toString())) dial.add(day.get("peer_number").toString());
                    if("DIALED".equals(day.get("dial_type").toString())) dialed.add(day.get("peer_number").toString());
                }
            }
        }
        //近1个月联系号码数
        System.out.println("近1个月联系号码数:"+map.size());
        result.put("friend_num_1m",map.size());

        for (String key:map.keySet()) {
            //近1个月联系十次以上的号码数量
            if(map.get(key)>=10) result.put("good_friend_num_1m",((int) result.get("good_friend_num_1m"))+1);
        }
        System.out.println("近1个月联系十次以上的号码数量:"+result.get("good_friend_num_1m"));

        int integer = 0;
        String loca = "";
        for (String key:location.keySet()) {
            if(integer<location.get(key)){
                integer = location.get(key);
                loca = key;
            }
        }

        //近1个月联系次数最多的归属地
        //System.out.println("近1个月联系次数最多的归属地:"+loca);
        //result.put("friend_city_center_1m",loca);

        Set<String> set = new HashSet<>();
        set.addAll(dial);
        set.retainAll(dialed);
        //近1个月互通电话的号码数量
        result.put("inter_peer_num_1m",set.size());
    }

    //近M个月联系次数最多的归属地
    public static String friendCity(JSONObject data, JSONObject applicant, int m) throws Exception {
        Map<String,Integer> map = new HashMap<>();
        String local = "";
        //得到N个月前的时间
        String monthBefore = CarrierDateUtil.monthsBefore(m,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        String month = CarrierDateUtil.yearMonth(monthBefore);
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject)jsonArray.get(i);
            //如果N个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
            if(CarrierDateUtil.compareDate(month+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==1) continue;
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //如果N个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
                if(CarrierDateUtil.compareDate(monthBefore,day.get("time").toString(),"yyyy-MM-dd")<1){
                    local = getMobileFrom(data.get("mobile").toString());
                    map.put(local,(map.get(local)==null?0:map.get(local))+1);
                }
            }
        }

        map = CarrierDateUtil.sortByValueDescending(map);

        for (String key:map.keySet()) {
            return key;
        }

        return null;
    }

    //近N个月非11位号码通话次数
    public static int callsOfNot11(JSONObject data, JSONObject applicant, int num){

        int integer = 0;
        //得到N个月前的时间
        String monthBefore = CarrierDateUtil.monthsBefore(num,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        String month = CarrierDateUtil.yearMonth(monthBefore);
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject)jsonArray.get(i);
            //如果N个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
            if(CarrierDateUtil.compareDate(month+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==1) continue;
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //如果N个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
                if(CarrierDateUtil.compareDate(monthBefore,day.get("time").toString(),"yyyy-MM-dd")<1 &&
                        PhoneNumberHelper.isMobilePhone(day.get("peer_number").toString())){
                    integer += 1;
                }
            }
        }

        System.out.println("近"+num+"个月非11位号码通话次数:"+integer);
        return integer;
    }

    //近N个月总通话次数
    public static int callsOfNumbers(JSONObject data, JSONObject applicant, int num){

        int integer = 0;
        //得到N个月前的时间
        String monthBefore = CarrierDateUtil.monthsBefore(num,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        String month = CarrierDateUtil.yearMonth(monthBefore);
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject)jsonArray.get(i);
            //如果N个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
            if(CarrierDateUtil.compareDate(month+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==1) continue;
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //如果N个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
                if(CarrierDateUtil.compareDate(monthBefore,day.get("time").toString(),"yyyy-MM-dd")<1){
                    integer += 1;
                }
            }
        }

        System.out.println("近"+num+"个月总通话次数:"+integer);
        return integer;
    }

    //近N个月主叫时长
    public static int dialingTime(JSONObject data, JSONObject applicant, int num){

        int integer = 0;
        //得到N个月前的时间
        String monthBefore = CarrierDateUtil.monthsBefore(num,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        String month = CarrierDateUtil.yearMonth(monthBefore);
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject)jsonArray.get(i);
            //如果N个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
            if(CarrierDateUtil.compareDate(month+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==1) continue;
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //如果N个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
                if(CarrierDateUtil.compareDate(monthBefore,day.get("time").toString(),"yyyy-MM-dd")<1 &&
                        "DIAL".equals(day.get("dial_type").toString())){
                    integer += Integer.parseInt(day.get("duration").toString());
                }
            }
        }

        System.out.println("近"+num+"个月主叫时长:"+integer);
        return integer;
    }

    //近N个月主叫次数
    public static int dialingNumbers(JSONObject data, JSONObject applicant, int num){

        int integer = 0;
        //得到N个月前的时间
        String monthBefore = CarrierDateUtil.monthsBefore(num,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        String month = CarrierDateUtil.yearMonth(monthBefore);
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject)jsonArray.get(i);
            //如果N个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
            if(CarrierDateUtil.compareDate(month+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==1) continue;
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //如果N个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
                if(CarrierDateUtil.compareDate(monthBefore,day.get("time").toString(),"yyyy-MM-dd")<1 &&
                        "DIAL".equals(day.get("dial_type").toString())){
                    integer += 1;
                }
            }
        }

        System.out.println("近"+num+"个月主叫次数:"+integer);
        return integer;
    }

    //近N个月总通话时长
    public static int callDuration(JSONObject data, JSONObject applicant, int num){

        int integer = 0;
        //得到N个月前的时间
        String monthBefore = CarrierDateUtil.monthsBefore(num,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        String month = CarrierDateUtil.yearMonth(monthBefore);
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject)jsonArray.get(i);
            //如果N个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
            if(CarrierDateUtil.compareDate(month+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==1) continue;
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //如果N个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
                if(CarrierDateUtil.compareDate(monthBefore,day.get("time").toString(),"yyyy-MM-dd")<1){
                    integer += Integer.parseInt(day.get("duration").toString());
                }
            }
        }

        System.out.println("近"+num+"个月总通话时长:"+integer);
        return integer;
    }

    //近1个月申请地通话次数
    public static int applyCallNumbers(JSONObject data, JSONObject applicant, int m){

        int num = 0;
        //得到N个月前的时间
        String monthBefore = CarrierDateUtil.monthsBefore(num,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        String month = CarrierDateUtil.yearMonth(monthBefore);
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject)jsonArray.get(i);
            //如果N个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
            if(CarrierDateUtil.compareDate(month+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==1) continue;
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //如果N个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
                if(CarrierDateUtil.compareDate(monthBefore,day.get("time").toString(),"yyyy-MM-dd")<1 &&
                        (applicant.get("customerHouseholdRegisterProvince").toString() + applicant.get("customerHouseholdRegisterCity").toString())
                                .equals(day.get("location").toString())){
                    num += 1;
                }
            }
        }

        return num;
    }

    //近M个月申请地通话时长(秒)
    public static int applyCalltimes(JSONObject data, JSONObject applicant, int m){

        int num = 0;
        //得到N个月前的时间
        String monthBefore = CarrierDateUtil.monthsBefore(num,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        String month = CarrierDateUtil.yearMonth(monthBefore);
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject)jsonArray.get(i);
            //如果N个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
            if(CarrierDateUtil.compareDate(month+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==1) continue;
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //如果N个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
                if(CarrierDateUtil.compareDate(monthBefore,day.get("time").toString(),"yyyy-MM-dd")<1 &&
                        (applicant.get("customerHouseholdRegisterProvince").toString() + applicant.get("customerHouseholdRegisterCity").toString())
                                .equals(day.get("location").toString())){
                    num += Integer.parseInt(day.get("duration").toString());
                }
            }
        }

        return num;
    }

    //近M个月，除近N个月，剩余M-N个月无通话天数
    public static int callDuration(JSONObject data, JSONObject applicant, int m, int n){

        Set<String> set = new HashSet<String>();
        //得到m个月前的时间
        String mBefore = CarrierDateUtil.monthsBefore(m,applicant.get("customerApplyDate").toString());
        //得到n个月前的时间
        String nBefore = CarrierDateUtil.monthsBefore(n,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        String mMonth = CarrierDateUtil.yearMonth(mBefore);
        //得到yyyy-mm
        String nMonth = CarrierDateUtil.yearMonth(nBefore);
        //得到剩余M-N个月的总计天数
        int count = CarrierDateUtil.daysBetween(mBefore,nBefore);
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject) jsonArray.get(i);
            //当前月份若大于N个月或小于M个月，结束本次循环
            if(CarrierDateUtil.compareDate(nMonth+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==-1 ||
                    CarrierDateUtil.compareDate(mMonth+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==1)
                continue;
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //当前日期大于M且小于N，继续执行
                if(CarrierDateUtil.compareDate(mBefore,day.get("time").toString(),"yyyy-MM-dd")<1 &&
                        CarrierDateUtil.compareDate(nBefore,day.get("time").toString(),"yyyy-MM-dd")>-1){
                    //有通话天数
                    set.add(sdf.format(CarrierDateUtil.toDate(day.get("time").toString())));
                }
            }
        }

        int i = count-set.size();
        System.out.println("近"+m+"个月,除近"+n+"个月，剩余"+(m-n)+"个月无通话天数:"+i);
        return i;
    }

    //近M个月topN联系人号码
    public static List<String> top10ByPhone(JSONObject data, JSONObject applicant, int m, int n){

        Map<String, String> map = new HashMap<>();
        //得到m个月前的时间
        String monthBefore = CarrierDateUtil.monthsBefore(m,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        String month = CarrierDateUtil.yearMonth(monthBefore);
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject)jsonArray.get(i);
            //如果m个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
            if(CarrierDateUtil.compareDate(month+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==1) continue;
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //如果m个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
                if(CarrierDateUtil.compareDate(monthBefore,day.get("time").toString(),"yyyy-MM-dd")<1){
                    //联系的号码数
                    if(map.get(day.get("peer_number").toString())==null) map.put(day.get("peer_number").toString(),"1");
                    else map.put(day.get("peer_number").toString(),String.valueOf(Integer.parseInt(map.get(day.get("peer_number").toString()))+1));
                }
            }
        }
        map = CarrierDateUtil.sortByValueDescending(map);
        List<String> list = new ArrayList<>();
        for (Map.Entry<String,String> entry : map.entrySet()) {
            //System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
            if(list.size()<n) list.add(entry.getKey());
        }

        System.out.println("近"+m+"个月top"+n+"联系人号码:"+list.toString());
        return list;
    }

    //近M个月topN联系人号码归属地
    public static Set<String> top10ByLocation(List<String> data) throws Exception {

        Set<String> set = new HashSet<>();
        for (String phone : data) {
            if(PhoneNumberHelper.isMobilePhone(phone))
                set.add(getMobileFrom(phone));
        }

        System.out.println("近M个月top10联系人号码归属地:"+set.toString());
        return set;
    }

    //近M个月S-E主叫次数
    public static int dialCallNumbers(JSONObject data, JSONObject applicant, int m, String s, String e){

        int num = 0;
        boolean bl = true;
        //判断是否当天，true：当天；false：隔天
        if(Integer.parseInt(s.split(":")[0])>Integer.parseInt(e.split(":")[0])) bl = false;
        //得到M个月前的时间
        String monthBefore = CarrierDateUtil.monthsBefore(m,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        String month = CarrierDateUtil.yearMonth(monthBefore);
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject)jsonArray.get(i);
            //如果M个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
            if(CarrierDateUtil.compareDate(month+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==1) continue;
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //如果M个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
                if(CarrierDateUtil.compareDate(monthBefore,day.get("time").toString(),"yyyy-MM-dd")<1 &&
                        "DIAL".equals(day.get("dial_type").toString())){
                    String date = CarrierDateUtil.yearMonthDay(day.get("time").toString());
                    if(bl && CarrierDateUtil.compareDate(date+" "+s,day.get("time").toString(),"yyyy-MM-dd HH:mm:ss")<=0 &&
                            CarrierDateUtil.compareDate(date+" "+e,day.get("time").toString(),"yyyy-MM-dd HH:mm:ss")>=0){
                        num += 1;
                    }
                    if(!bl && (CarrierDateUtil.compareDate(date+" "+s,day.get("time").toString(),"yyyy-MM-dd HH:mm:ss")>=0 ||
                            CarrierDateUtil.compareDate(date+" "+e,day.get("time").toString(),"yyyy-MM-dd HH:mm:ss")<=0)){
                        num += 1;
                    }
                }
            }
        }

        System.out.println("近"+m+"个月"+s+"-"+e+"主叫次数:"+num);
        return num;
    }

    //近M个月S-E主叫时长
    public static int dialCallTimes(JSONObject data, JSONObject applicant, int m, String s, String e){

        int time = 0;
        boolean bl = true;
        //判断是否当天，true：当天；false：隔天
        if(Integer.parseInt(s.split(":")[0])>Integer.parseInt(e.split(":")[0])) bl = false;
        //得到M个月前的时间
        String monthBefore = CarrierDateUtil.monthsBefore(m,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        String month = CarrierDateUtil.yearMonth(monthBefore);
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject)jsonArray.get(i);
            //如果M个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
            if(CarrierDateUtil.compareDate(month+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==1) continue;
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //如果M个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
                if(CarrierDateUtil.compareDate(monthBefore,day.get("time").toString(),"yyyy-MM-dd")<1 &&
                        "DIAL".equals(day.get("dial_type").toString())){
                    String date = CarrierDateUtil.yearMonthDay(day.get("time").toString());
                    if(bl && CarrierDateUtil.compareDate(date+" "+s,day.get("time").toString(),"yyyy-MM-dd HH:mm:ss")<=0 &&
                            CarrierDateUtil.compareDate(date+" "+e,day.get("time").toString(),"yyyy-MM-dd HH:mm:ss")>=0){
                        time += Integer.parseInt(day.get("duration").toString());
                    }
                    if(!bl && CarrierDateUtil.compareDate(date+" "+s,day.get("time").toString(),"yyyy-MM-dd HH:mm:ss")>=0 &&
                            CarrierDateUtil.compareDate(date+" "+e,day.get("time").toString(),"yyyy-MM-dd HH:mm:ss")<=0){
                        time += Integer.parseInt(day.get("duration").toString());
                    }
                }
            }
        }

        System.out.println("近"+m+"个月"+s+"-"+e+"主叫时长:"+time);
        return time;
    }

    //近M个月S-E总通话次数
    public static int callNumbers(JSONObject data, JSONObject applicant, int m, String s, String e){

        int num = 0;
        boolean bl = true;
        //判断是否当天，true：当天；false：隔天
        if(Integer.parseInt(s.split(":")[0])>Integer.parseInt(e.split(":")[0])) bl = false;
        //得到M个月前的时间
        String monthBefore = CarrierDateUtil.monthsBefore(m,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        String month = CarrierDateUtil.yearMonth(monthBefore);
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject)jsonArray.get(i);
            //如果M个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
            if(CarrierDateUtil.compareDate(month+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==1) continue;
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //如果M个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
                if(CarrierDateUtil.compareDate(monthBefore,day.get("time").toString(),"yyyy-MM-dd")<1){
                    String date = CarrierDateUtil.yearMonthDay(day.get("time").toString());
                    if(bl && CarrierDateUtil.compareDate(date+" "+s,day.get("time").toString(),"yyyy-MM-dd HH:mm:ss")<=0 &&
                            CarrierDateUtil.compareDate(date+" "+e,day.get("time").toString(),"yyyy-MM-dd HH:mm:ss")>=0){
                        num += 1;
                    }
                    if(!bl && CarrierDateUtil.compareDate(date+" "+s,day.get("time").toString(),"yyyy-MM-dd HH:mm:ss")>=0 &&
                            CarrierDateUtil.compareDate(date+" "+e,day.get("time").toString(),"yyyy-MM-dd HH:mm:ss")<=0){
                        num += 1;
                    }
                }
            }
        }

        System.out.println("近"+m+"个月"+s+"-"+e+"总通话次数:"+num);
        return num;
    }

    //近M个月S-E总通话时长
    public static int callTimes(JSONObject data, JSONObject applicant, int m, String s, String e){

        int time = 0;
        boolean bl = true;
        //判断是否当天，true：当天；false：隔天
        if(Integer.parseInt(s.split(":")[0])>Integer.parseInt(e.split(":")[0])) bl = false;
        //得到M个月前的时间
        String monthBefore = CarrierDateUtil.monthsBefore(m,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        String month = CarrierDateUtil.yearMonth(monthBefore);
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject)jsonArray.get(i);
            //如果M个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
            if(CarrierDateUtil.compareDate(month+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==1) continue;
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //如果M个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
                if(CarrierDateUtil.compareDate(monthBefore,day.get("time").toString(),"yyyy-MM-dd")<1){
                    String date = CarrierDateUtil.yearMonthDay(day.get("time").toString());
                    if(bl && CarrierDateUtil.compareDate(date+" "+s,day.get("time").toString(),"yyyy-MM-dd HH:mm:ss")<=0 &&
                            CarrierDateUtil.compareDate(date+" "+e,day.get("time").toString(),"yyyy-MM-dd HH:mm:ss")>=0){
                        time += Integer.parseInt(day.get("duration").toString());
                    }
                    if(!bl && CarrierDateUtil.compareDate(date+" "+s,day.get("time").toString(),"yyyy-MM-dd HH:mm:ss")>=0 &&
                            CarrierDateUtil.compareDate(date+" "+e,day.get("time").toString(),"yyyy-MM-dd HH:mm:ss")<=0){
                        time += Integer.parseInt(day.get("duration").toString());
                    }
                }
            }
        }

        System.out.println("近"+m+"个月"+s+"-"+e+"总通话时长:"+time);
        return time;
    }

    //近M个月S-E主叫号码数
    public static int dialPhoneNumbers(JSONObject data, JSONObject applicant, int m, String s, String e){

        Set<String> set = new HashSet<>();
        boolean bl = true;
        //判断是否当天，true：当天；false：隔天
        if(Integer.parseInt(s.split(":")[0])>Integer.parseInt(e.split(":")[0])) bl = false;
        //得到M个月前的时间
        String monthBefore = CarrierDateUtil.monthsBefore(m,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        String month = CarrierDateUtil.yearMonth(monthBefore);
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject)jsonArray.get(i);
            //如果M个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
            if(CarrierDateUtil.compareDate(month+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==1) continue;
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //如果M个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
                if(CarrierDateUtil.compareDate(monthBefore,day.get("time").toString(),"yyyy-MM-dd")<1 &&
                        "DIAL".equals(day.get("dial_type").toString())){
                    String date = CarrierDateUtil.yearMonthDay(day.get("time").toString());
                    if(bl && CarrierDateUtil.compareDate(date+" "+s,day.get("time").toString(),"yyyy-MM-dd HH:mm:ss")<=0 &&
                            CarrierDateUtil.compareDate(date+" "+e,day.get("time").toString(),"yyyy-MM-dd HH:mm:ss")>=0){
                        set.add(day.get("peer_number").toString());
                    }
                    if(!bl && CarrierDateUtil.compareDate(date+" "+s,day.get("time").toString(),"yyyy-MM-dd HH:mm:ss")>=0 &&
                            CarrierDateUtil.compareDate(date+" "+e,day.get("time").toString(),"yyyy-MM-dd HH:mm:ss")<=0){
                        set.add(day.get("peer_number").toString());
                    }
                }
            }
        }

        System.out.println("近"+m+"个月"+s+"-"+e+"主叫号码数:"+set.size());
        return set.size();
    }

    //近M个月S-E总通话号码数
    public static int phoneNumbers(JSONObject data, JSONObject applicant, int m, String s, String e){

        Set<String> set = new HashSet<>();
        boolean bl = true;
        //判断是否当天，true：当天；false：隔天
        if(Integer.parseInt(s.split(":")[0])>Integer.parseInt(e.split(":")[0])) bl = false;
        //得到M个月前的时间
        String monthBefore = CarrierDateUtil.monthsBefore(m,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        String month = CarrierDateUtil.yearMonth(monthBefore);
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject)jsonArray.get(i);
            //如果M个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
            if(CarrierDateUtil.compareDate(month+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==1) continue;
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //如果M个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
                if(CarrierDateUtil.compareDate(monthBefore,day.get("time").toString(),"yyyy-MM-dd")<1){
                    String date = CarrierDateUtil.yearMonthDay(day.get("time").toString());
                    if(bl && CarrierDateUtil.compareDate(date+" "+s,day.get("time").toString(),"yyyy-MM-dd HH:mm:ss")<=0 &&
                            CarrierDateUtil.compareDate(date+" "+e,day.get("time").toString(),"yyyy-MM-dd HH:mm:ss")>=0){
                        set.add(day.get("peer_number").toString());
                    }
                    if(!bl && CarrierDateUtil.compareDate(date+" "+s,day.get("time").toString(),"yyyy-MM-dd HH:mm:ss")>=0 &&
                            CarrierDateUtil.compareDate(date+" "+e,day.get("time").toString(),"yyyy-MM-dd HH:mm:ss")<=0){
                        set.add(day.get("peer_number").toString());
                    }
                }
            }
        }

        System.out.println("近"+m+"个月"+s+"-"+e+"总通话号码数:"+set.size());
        return set.size();
    }

    //近M月停留城市时长（天）
    public static Map<String,Integer> city(JSONObject data, JSONObject applicant, int m){

        //city_length_1m
        Map<String,Integer> map = new TreeMap<>();
        String city = "";
        String date = "";
        //得到m个月前的时间
        String monthBefore = CarrierDateUtil.monthsBefore(m,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        String month = CarrierDateUtil.yearMonth(monthBefore);
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject)jsonArray.get(i);
            //如果m个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
            if(CarrierDateUtil.compareDate(month+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==1) continue;
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //如果m个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
                if(CarrierDateUtil.compareDate(monthBefore,day.get("time").toString(),"yyyy-MM-dd")<1){
                    if(!CarrierDateUtil.yearMonthDay(day.get("time").toString()).equals(date)){
                        date = CarrierDateUtil.yearMonthDay(day.get("time").toString());
                        map.put(StringUtils.equals(city,"")?day.get("location").toString():city,(map.get(city)==null?0:map.get(city))+1);
                    }
                    city = day.get("location").toString();
                }
            }
        }

        System.out.println("近"+m+"个月停留城市时长（天）:"+map.toString());
        return map;
    }

    //近M月停留时长最长的城市
    public static String maxCity(Map<String,Integer> data){
        data = CarrierDateUtil.sortByValueDescending(data);
        for (String key:data.keySet()) {
            return key;
        }
        return  null;
    }

    //近M个月TOP10联系人手机号归属地为申请地的数量
    public static int localNum(List<String> data,JSONObject applicant) throws Exception {

        int num = 0;
        String apply = applicant.get("customerHouseholdRegisterProvince").toString() + applicant.get("customerHouseholdRegisterCity").toString();
        for (String phone:data) {
            if(PhoneNumberHelper.isMobilePhone(phone) && apply.equals(getMobileFrom(phone))) num += 1;
        }

        return num;
    }

    //
    //近M个月TOP10联系人手机号归属地为户籍所在地的数量
    public static int registerNum(List<String> data,JSONObject applicant) throws Exception {

        int num = 0;
        String apply = applicant.get("customerHouseholdRegisterProvince").toString() + applicant.get("customerHouseholdRegisterCity").toString();
        for (String phone:data) {
            if(PhoneNumberHelper.isMobilePhone(phone) && apply.equals(getMobileFrom(phone))) num += 1;
        }

        return num;
    }

    //近M个月，除近N个月,TOP10联系人变化指数
    public static int top10ChangeExp(JSONObject data, JSONObject applicant, int m, int n){

        boolean bl = true;
        Set<String> set = null;
        //得到m个月前的时间
        //String monthBefore = CarrierDateUtil.monthsBefore(m,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        //String month = CarrierDateUtil.yearMonth(monthBefore);

        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");


        //便利近M个月，除近N个月的每个月
        for (int k =n; k<m; k++){
            //得到n个月前的时间
            String endDate = CarrierDateUtil.monthsBefore(k,applicant.get("customerApplyDate").toString());
            String startDate = CarrierDateUtil.monthsBefore(k+1,applicant.get("customerApplyDate").toString());
            //得到yyyy-mm
            String endMonth = CarrierDateUtil.yearMonth(endDate);
            String startMonth = CarrierDateUtil.yearMonth(startDate);
            Set<String> temp = new HashSet<>();
            Map<String,Integer> map = new HashMap<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                //按月遍历
                JSONObject mon = (JSONObject)jsonArray.get(i);
                if(CarrierDateUtil.compareDate(startMonth+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==1 ||
                        CarrierDateUtil.compareDate(endMonth+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==-1) continue;
                for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                    JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                    //如果m个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
                    if(CarrierDateUtil.compareDate(startDate,day.get("time").toString(),"yyyy-MM-dd")<1 &&
                            CarrierDateUtil.compareDate(endDate,day.get("time").toString(),"yyyy-MM-dd")>-1){
                        map.put(day.get("peer_number").toString(),(map.get(day.get("peer_number").toString())==null?0:map.get(day.get("peer_number").toString()))+1);
                    }
                }
            }
            map = CarrierDateUtil.sortByValueDescending(map);
            for (String key:map.keySet()) {
                temp.add(key);
                if(temp.size()>=10) break;
            }
            if(set==null) set = temp;
            set.retainAll(temp);
        }

        System.out.println("近"+m+"个月，除近"+n+"个月,TOP10联系人变化指数:"+(10-set.size()));
        return 10-set.size();
    }

    //近M个月，每个月都出现的手机号码
    public static List<String> everMonthLocalNum(JSONObject data, JSONObject applicant, int m){

        boolean bl = true;
        Set<String> set = new HashSet<>();
        //得到m个月前的时间
        String monthBefore = CarrierDateUtil.monthsBefore(m,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        String month = CarrierDateUtil.yearMonth(monthBefore);
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject)jsonArray.get(i);
            Set<String> phones = new HashSet<>();
            if(i!=0) bl = false;
            //如果m个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
            if(CarrierDateUtil.compareDate(month+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==1) continue;
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //如果m个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
                if(CarrierDateUtil.compareDate(monthBefore,day.get("time").toString(),"yyyy-MM-dd")<1){
                    if(bl) set.add(day.get("peer_number").toString());
                    else phones.add(day.get("peer_number").toString());
                }
            }
            if(!bl) set.retainAll(phones);
        }


        return new ArrayList<>(set);
    }

    //近M个月，除近N个月，topX联系人非Y位手机号码数
    public static int topNumbers(JSONObject data, JSONObject applicant, int m, int n, int x, int y){

        Map<String, Integer> map = new TreeMap<>();
        //得到m个月前的时间
        String monthBefore = CarrierDateUtil.monthsBefore(m,applicant.get("customerApplyDate").toString());
        //得到n个月前的时间
        String nBefore = CarrierDateUtil.monthsBefore(n,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        String month = CarrierDateUtil.yearMonth(monthBefore);
        String nmonth = CarrierDateUtil.yearMonth(nBefore);
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject)jsonArray.get(i);
            //如果m个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
            if(CarrierDateUtil.compareDate(month+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==1 ||
                    CarrierDateUtil.compareDate(nmonth+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==-1) continue;
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //如果m个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
                if(CarrierDateUtil.compareDate(monthBefore,day.get("time").toString(),"yyyy-MM-dd")<1 &&
                        CarrierDateUtil.compareDate(nBefore,day.get("time").toString(),"yyyy-MM-dd")>-1){
                    //联系的号码数
                    map.put(day.get("peer_number").toString(),(map.get(day.get("peer_number").toString())==null?0:map.get(day.get("peer_number").toString()))+1);
                }
            }
        }
        map = CarrierDateUtil.sortByValueDescending(map);
        List<String> list = new ArrayList<>();
        int length = 0;
        for (Map.Entry<String,Integer> entry : map.entrySet()) {
            if(length<x && !PhoneNumberHelper.isMobilePhone(entry.getKey())) list.add(entry.getKey());
            length++;
        }

        System.out.println("近"+m+"个月，除近"+n+"个月，top"+x+"联系人非"+y+"位手机号码数:"+list.size());
        return list.size();
    }

    //近M个月，除近N个月，topX联系人非Y位手机号码的月份数
    public static int topMonths(JSONObject data, JSONObject applicant, int m, int n, int x, int y){

        Map<String, String> map = new HashMap<>();
        Map<String, String> months = new HashMap<>();
        //得到m个月前的时间
        String monthBefore = CarrierDateUtil.monthsBefore(m,applicant.get("customerApplyDate").toString());
        //得到n个月前的时间
        String nBefore = CarrierDateUtil.monthsBefore(n,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        String month = CarrierDateUtil.yearMonth(monthBefore);
        String nmonth = CarrierDateUtil.yearMonth(nBefore);
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject)jsonArray.get(i);
            //如果m个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
            if(CarrierDateUtil.compareDate(month+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==1 ||
                    CarrierDateUtil.compareDate(nmonth+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==-1) continue;
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //如果m个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
                if(CarrierDateUtil.compareDate(monthBefore,day.get("time").toString(),"yyyy-MM-dd")<1 &&
                        CarrierDateUtil.compareDate(nBefore,day.get("time").toString(),"yyyy-MM-dd")>-1){
                    //联系的号码数
                    if(map.get(day.get("peer_number").toString())==null) map.put(day.get("peer_number").toString(),"1");
                    else map.put(day.get("peer_number").toString(),String.valueOf(Integer.parseInt(map.get(day.get("peer_number").toString()))+1));
                    //保存号码的月份
                    months.put(day.get("peer_number").toString(),months.get(day.get("peer_number").toString())==null?"":months.get(day.get("peer_number").toString())+day.get("time").toString()+",");
                }
            }
        }
        map = CarrierDateUtil.sortByValueDescending(map);
        List<String> list = new ArrayList<>();
        int length = 0;
        //取出非Y位的号码
        for (Map.Entry<String,String> entry : map.entrySet()) {
            if(length<x && !PhoneNumberHelper.isMobilePhone(entry.getKey())) list.add(entry.getKey());
            length++;
        }
        Set<String> set = new HashSet<>();
        //
        for (String key:list) {
            String[] numbers = months.get(key).substring(0,months.get(key).length()-1).split(",");
            for (String time:numbers) {
                set.add(CarrierDateUtil.yearMonth(time));
            }
        }

        System.out.println("近"+m+"个月，除近"+n+"个月，top"+x+"联系人非"+y+"位手机号码的月份数:"+set.size());
        return set.size();
    }

    //近M个月通话时长?X秒的次数(n = 1：小于；2：小于等于；0：等于；3：大于；4：大于等于)
    public static int talkTime(JSONObject data, JSONObject applicant, int m, int n, int x){

        int number = 0;
        //得到m个月前的时间
        String monthBefore = CarrierDateUtil.monthsBefore(m,applicant.get("customerApplyDate").toString());
        //得到n个月前的时间
        //String nBefore = CarrierDateUtil.monthsBefore(n,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        String month = CarrierDateUtil.yearMonth(monthBefore);
        //String nmonth = CarrierDateUtil.yearMonth(nBefore);
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject)jsonArray.get(i);
            //如果m个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
            if(CarrierDateUtil.compareDate(month+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==1) continue;
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //如果m个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
                if(CarrierDateUtil.compareDate(monthBefore,day.get("time").toString(),"yyyy-MM-dd")<1){
                    number += number(Integer.parseInt(day.get("duration").toString()),n,x);
                }
            }
        }

        System.out.println("近"+m+"个月通话时长"+Enums.getName(String.valueOf(n))+x+"秒的次数:"+number);
        return number;
    }

    //近M个月S-E总通话次数
    public static int callNumbers(JSONObject data, JSONObject applicant, int m){

        int num = 0;
        //得到M个月前的时间
        String monthBefore = CarrierDateUtil.monthsBefore(m,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        String month = CarrierDateUtil.yearMonth(monthBefore);
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject)jsonArray.get(i);
            //如果M个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
            if(CarrierDateUtil.compareDate(month+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==1) continue;
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //如果M个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
                if(CarrierDateUtil.compareDate(monthBefore,day.get("time").toString(),"yyyy-MM-dd")<1){
                    num += 1;
                }
            }
        }

        System.out.println("近"+m+"个月总通话次数:"+num);
        return num;
    }

    //近M个月连续N天以上无通话的次数
    public static int noCall(JSONObject data, JSONObject applicant, int m, int n){

        int num = 0;
        //得到M个月前的时间
        String monthBefore = CarrierDateUtil.monthsBefore(m,applicant.get("customerApplyDate").toString());
        String previousDay = applicant.get("customerApplyDate").toString();
        //得到yyyy-mm
        String month = CarrierDateUtil.yearMonth(monthBefore);
        String pMonth = CarrierDateUtil.yearMonth(previousDay);
        String lastDate = previousDay;
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject)jsonArray.get(i);
            //如果M个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
            if(CarrierDateUtil.compareDate(pMonth+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==-1 &&
                    CarrierDateUtil.compareDate(month+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==1) continue;
            System.out.println(mon.get("bill_month"));
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //如果M个月前的时间小于或者等于历史时间则不做操作，大于则结束本次循环
                if(CarrierDateUtil.compareDate(monthBefore,day.get("time").toString(),"yyyy-MM-dd")<1 &&
                        CarrierDateUtil.compareDate(previousDay,day.get("time").toString(),"yyyy-MM-dd")>-1){
                    System.out.println(day.get("time"));
                    String date = CarrierDateUtil.yearMonthDay(day.get("time").toString());
                    int days = CarrierDateUtil.daysBetween(date,lastDate);
                    lastDate = date;
                    if(days>=n) num += Math.floor(days/n);
                }
            }
        }

        System.out.println("近"+m+"个月连续"+n+"天以上无通话的次数:"+num);
        return num;
    }

    //近M个月，除近N个月，其余M-N个月月均通话次数
    public static String  callAvgNumbers(JSONObject data, JSONObject applicant, int m, int n){

        int num = 0;
        //得到m个月前的时间
        String mBefore = CarrierDateUtil.monthsBefore(m,applicant.get("customerApplyDate").toString());
        //得到n个月前的时间
        String nBefore = CarrierDateUtil.monthsBefore(n,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        String mMonth = CarrierDateUtil.yearMonth(mBefore);
        //得到yyyy-mm
        String nMonth = CarrierDateUtil.yearMonth(nBefore);
        //得到剩余M-N个月的总计天数
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject) jsonArray.get(i);
            //当前月份若大于N个月或小于M个月，结束本次循环
            if(CarrierDateUtil.compareDate(nMonth+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==-1 ||
                    CarrierDateUtil.compareDate(mMonth+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==1)
                continue;
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //当前日期大于M且小于N，继续执行
                if(CarrierDateUtil.compareDate(mBefore,day.get("time").toString(),"yyyy-MM-dd")<1 &&
                        CarrierDateUtil.compareDate(nBefore,day.get("time").toString(),"yyyy-MM-dd")>-1){
                    num += 1;
                }
            }
        }

        System.out.println("近"+m+"个月,除近"+n+"个月，剩余"+(m-n)+"个月月均通话次数:"+String.format("%.2f",Integer.valueOf(num).doubleValue()/(m-n)));
        return String.format("%.2f",Integer.valueOf(num).doubleValue()/(m-n));
    }

    //除近N个月，剩余月总通话时长
    public static Integer totalCalllength(JSONObject data, JSONObject applicant, int n){

        Integer num = 0;
        //得到n个月前的时间
        String nBefore = CarrierDateUtil.monthsBefore(n,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        String nMonth = CarrierDateUtil.yearMonth(nBefore);
        //得到剩余M-N个月的总计天数
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject) jsonArray.get(i);
            //当前月份若大于N个月或小于M个月，结束本次循环
            if(CarrierDateUtil.compareDate(nMonth+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==-1)
                continue;
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //当前日期大于M且小于N，继续执行
                if(CarrierDateUtil.compareDate(nBefore,day.get("time").toString(),"yyyy-MM-dd")>-1){
                    num += Integer.parseInt(day.get("duration").toString());
                }
            }
        }

        System.out.println("除近"+n+"个月，剩余月总通话时长:"+num);
        return num;
    }

    //除近N个月，剩余月月均总通话时长
    public static String avgCalllength(JSONObject data, JSONObject applicant, int n){

        Integer num = 0;
        Integer month = 0;
        //得到n个月前的时间
        String nBefore = CarrierDateUtil.monthsBefore(n,applicant.get("customerApplyDate").toString());
        //得到yyyy-mm
        String nMonth = CarrierDateUtil.yearMonth(nBefore);
        //得到剩余M-N个月的总计天数
        //得到通话记录详单（按月份）
        JSONArray jsonArray = data.getJSONArray("calls");
        for (int i = 0; i < jsonArray.size(); i++) {
            //按月遍历
            JSONObject mon = (JSONObject) jsonArray.get(i);
            //当前月份若大于N个月或小于M个月，结束本次循环
            if(CarrierDateUtil.compareDate(nMonth+"-01",mon.get("bill_month")+"-01","yyyy-MM-dd")==-1)
                continue;
            month += 1;
            for (int j = 0; j < mon.getJSONArray("items").size(); j++) {
                JSONObject day = (JSONObject)mon.getJSONArray("items").get(j);
                //当前日期大于M且小于N，继续执行
                if(CarrierDateUtil.compareDate(nBefore,day.get("time").toString(),"yyyy-MM-dd")>-1){
                    num += Integer.parseInt(day.get("duration").toString());
                }
            }
        }

        System.out.println("除近"+n+"个月，剩余月均通话时长:"+String.format("%.2f",num.doubleValue()/month));
        return String.format("%.2f",num.doubleValue()/month);
    }


    //比较a、b值的大小（m = 1：小于；2：小于等于；0：等于；3：大于；4：大于等于）
    private static int number(int a,int m,int b){

        int num = 0;
        switch (m){
            case 0 :
                if(a==b){
                    num = 1;
                }
                break;
            case 1 :
                if(a<b) {
                    num = 1;
                }
                break;
            case 2 :
                if(a<=b) {
                    num = 1;
                }
                break;
            case 3 :
                if(a>b) {
                    num = 1;
                }
                break;
            case 4 :
                if(a>=b) {
                    num = 1;
                }
                break;
        }
        return num;
    }

    /**
     * 将数据按月份倒序排列
     * @param data
     * @return
     */
    public static void order(JSONObject data){

        if(data==null) return;
        List<String> list = new ArrayList<>();
        //JSONArray calls = JSONArray.parseArray(data.get("calls").toString());
        //List<Object> calls = (ArrayList) data.get("calls");
        JSONArray calls = data.getJSONArray("calls");

        for (Object obj:calls) {
            list.add(JSONObject.parseObject(obj.toString()).get("bill_month").toString());
            //list.add(((JSONObject) obj).get("bill_month").toString());
        }
        Object[] arr = list.toArray();
        list = Arrays.asList(ListSort(arr));
        JSONArray jsonArray = new JSONArray();
        for (String str:list) {
            for (Object obj:calls) {
                if(StringUtils.equals(str,JSONObject.parseObject(obj.toString()).get("bill_month").toString())){
                    Object o = orderDay(obj);
                    jsonArray.set(jsonArray.size(),o);
                }
            }
        }
        data.put("calls",jsonArray);

    }

    /**
     * 将数据按天倒序排列
     * @param obj
     * @return
     */
    private static Object orderDay(Object obj){

        JSONArray items = JSONObject.parseObject(obj.toString()).getJSONArray("items");
        //JSONObject.parseObject(obj.toString()).put("items","");
        for (int i = 0; i < items.size()-1; i++) {
            for (int j = items.size()-1; j > i; j--) {
                JSONObject day = (JSONObject)items.get(j);
                JSONObject listDay = (JSONObject)items.get(j-1);
                if(CarrierDateUtil.compareDate(listDay.getString("time"),day.getString("time"),"yyyy-MM-dd")==-1){
                    items.add(j-1,day);
                    items.add(j,listDay);
                }
            }
        }
        JSONObject.parseObject(obj.toString()).put("items",items);
        return obj;
    }

    private static String[] ListSort(Object[] arr) {
        String temp;//定义一个临时变量
        for(int i=0;i<arr.length-1;i++){//冒泡趟数
            for(int j=arr.length-1;j>i;j--){
                if(CarrierDateUtil.compareDate(arr[j-1]+"-01",arr[j]+"-01","yyyy-MM-dd")==-1){
                    temp = arr[j-1].toString();
                    arr[j-1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
        String[] arrStr = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            arrStr[i] = arr[i].toString();
        }
        return arrStr;
    }
}
