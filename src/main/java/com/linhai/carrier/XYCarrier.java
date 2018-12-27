package com.linhai.carrier;

import com.alibaba.fastjson.JSONObject;
import com.linhai.DataSource.DataSource;
import com.linhai.comm.Algorithm;
import com.linhai.comm.CarrierDateUtil;

import java.util.*;

/**
 * Created by linhai on 2018/11/27.
 */
public class XYCarrier {

    /**
     * 数据清洗方法
     * @param data
     * @param data,applicant,result
     * @return
     */
    public static JSONObject clean(JSONObject data, JSONObject applicant, JSONObject result) throws Exception{

        //格式化数据
        CarrierDateUtil.order(data);
        //清除归属地缓存
        Algorithm.mobileFrom.remove();
        //手机号归属地
        Algorithm.phoneLocal(data,result);
        //运营商身份证号码
        Algorithm.operatorId(data,result);
        //运营商姓名
        Algorithm.operatorName(data,result);
        //近1个月联系号码数
        //近1个月联系十次以上的号码数量
        //近1个月互通电话的号码数量
        Algorithm.contactNumberCountByNearlyAMonth(data,applicant,result);
        //近1个月联系次数最多的归属地
        String maxLocal = Algorithm.friendCity(data,applicant,1);
        result.put("friend_city_center_1m",maxLocal);
        //近1个月朋友圈中心地是否与手机归属地一致
        result.put("is_city_match_friend_city_center_1m",
                (applicant.get("customerHouseholdRegisterProvince").toString() + applicant.get("customerHouseholdRegisterCity").toString()).equals(maxLocal));
        //近1个月非11位号码通话次数
        Integer not_11_num_1m = Algorithm.callsOfNot11(data,applicant,1);
        result.put("not_11_num_1m",not_11_num_1m);
        //近3个月非11位号码通话次数
        Integer not_11_num_3m = Algorithm.callsOfNot11(data,applicant,3);
        result.put("not_11_num_3m",not_11_num_3m);
        //近6个月非11位号码通话次数
        Integer not_11_num_6m = Algorithm.callsOfNot11(data,applicant,6);
        result.put("not_11_num_6m",not_11_num_6m);
        //近3个月月均非11位号码通话次数
        result.put("not_11_num_avg_3m",String.format("%.2f",not_11_num_3m.doubleValue()/3));
        //近6个月月均非11位号码通话次数
        result.put("not_11_num_avg_6m",String.format("%.2f",not_11_num_6m.doubleValue()/6));
        //近1个月总通话次数比非11位号码通话次数
        Integer total_num_1m = Algorithm.callsOfNumbers(data,applicant,1);
        result.put("total_not_11_num_mul_1m",String.format("%.2f",total_num_1m.doubleValue()/not_11_num_1m));
        //近3个月总通话次数比非11位号码通话次数
        Integer total_num_3m = Algorithm.callsOfNumbers(data,applicant,3);
        result.put("total_not_11_num_mul_3m",String.format("%.2f",total_num_3m.doubleValue()/not_11_num_3m));
        //近6个月总通话次数比非11位号码通话次数
        Integer total_num_6m = Algorithm.callsOfNumbers(data,applicant,6);
        result.put("total_not_11_num_mul_6m",String.format("%.2f",total_num_6m.doubleValue()/not_11_num_6m));
        //近3个月月均总通话次数比月均非11位号码通话次数
        result.put("total_not_11_num_avg_mul_3m",String.format("%.2f",total_num_3m.doubleValue()/not_11_num_3m));
        //近6个月月均总通话次数比月均非11位号码通话次数
        result.put("total_not_11_num_avg_mul_6m",String.format("%.2f",total_num_6m.doubleValue()/not_11_num_6m));
        //近1个月主叫时长
        Integer total_dial_length_1m = Algorithm.dialingTime(data,applicant,1);
        result.put("total_dial_length_1m",total_dial_length_1m);
        //近3个月主叫时长
        Integer total_dial_length_3m = Algorithm.dialingTime(data,applicant,3);
        result.put("total_dial_length_3m",total_dial_length_3m);
        //近6个月主叫时长
        Integer total_dial_length_6m = Algorithm.dialingTime(data,applicant,6);
        result.put("total_dial_length_6m",total_dial_length_6m);
        //近3个月月均主叫时长
        result.put("total_dial_length_avg_3m",String.format("%.2f",total_dial_length_3m.doubleValue()/3));
        //近6个月月均主叫时长
        result.put("total_dial_length_avg_6m",String.format("%.2f",total_dial_length_6m.doubleValue()/6));
        //近1个月总通话数比主叫次数
        Integer total_dial_num_1m = Algorithm.dialingNumbers(data,applicant,1);
        result.put("total_dial_num_mul_1m",String.format("%.2f",total_num_1m.doubleValue()/total_dial_num_1m));
        //近3个月总通话数比主叫次数
        Integer total_dial_num_3m = Algorithm.dialingNumbers(data,applicant,3);
        result.put("total_dial_num_mul_3m",String.format("%.2f",total_num_3m.doubleValue()/total_dial_num_3m));
        //近6个月总通话数比主叫次数
        Integer total_dial_num_6m = Algorithm.dialingNumbers(data,applicant,6);
        result.put("total_dial_num_mul_6m",String.format("%.2f",total_num_6m.doubleValue()/total_dial_num_6m));
        //近1个月总通话时长比主叫通话时长
        Integer total_length_1m = Algorithm.callDuration(data,applicant,1);
        result.put("total_dial_length_mul_1m",String.format("%.2f",total_length_1m.doubleValue()/total_dial_length_1m));
        //近3个月总通话时长比主叫通话时长
        Integer total_length_3m = Algorithm.callDuration(data,applicant,3);
        result.put("total_dial_length_mul_3m",String.format("%.2f",total_length_3m.doubleValue()/total_dial_length_3m));
        //近6个月总通话时长比主叫通话时长
        Integer total_length_6m = Algorithm.callDuration(data,applicant,6);
        result.put("total_dial_length_mul_6m",String.format("%.2f",total_length_6m.doubleValue()/total_dial_length_6m));
        //近1个月申请地通话次数
        Integer apply_num_1m = Algorithm.applyCallNumbers(data,applicant,1);
        result.put("apply_num_1m",apply_num_1m);
        //近3个月申请地通话次数
        Integer apply_num_3m = Algorithm.applyCallNumbers(data,applicant,3);
        result.put("apply_num_3m",apply_num_3m);
        //近6个月申请地通话次数
        Integer apply_num_6m = Algorithm.applyCallNumbers(data,applicant,6);
        result.put("apply_num_6m",apply_num_6m);
        //近3个月月均申请地通话次数
        result.put("",apply_num_3m/3);
        //近6个月月均申请地通话次数
        result.put("",apply_num_6m/6);
        //近1个月申请地通话时长(秒)
        Integer apply_time_1m = Algorithm.applyCalltimes(data,applicant,1);
        result.put("apply_time_1m",apply_time_1m);
        //近3个月申请地通话时长(秒)
        Integer apply_time_3m = Algorithm.applyCalltimes(data,applicant,3);
        result.put("apply_time_3m",apply_time_3m);
        //近6个月申请地通话时长(秒)
        Integer apply_time_6m = Algorithm.applyCalltimes(data,applicant,6);
        result.put("apply_time_6m",apply_time_6m);
        //近3个月月均申请地通话时长(秒)
        result.put("apply_time_avg_3m",apply_time_3m/3);
        //近6个月月均申请地通话时长(秒)
        result.put("apply_time_avg_6m",apply_time_6m/6);
        //近3个月，除近1个月，剩余2个月无通话天数
        Integer no_call_not_1m_day_3m = Algorithm.callDuration(data,applicant,3,1);
        result.put("no_call_not_1m_day_3m",no_call_not_1m_day_3m);
        //近6个月，除近1个月，剩余5个月无通话天数
        Integer no_call_not_1m_day_6m = Algorithm.callDuration(data,applicant,6,1);
        result.put("no_call_not_1m_day_6m",no_call_not_1m_day_6m);
        //近6个月，除近2个月，剩余4个月无通话天数
        Integer no_call_not_2m_day_6m = Algorithm.callDuration(data,applicant,6,2);
        result.put("no_call_not_2m_day_6m",no_call_not_2m_day_6m);
        //近3个月，除近1个月，剩余2个月月均无通话天数
        result.put("no_call_not_1m_day_avg_3m",String.format("%.2f",no_call_not_1m_day_3m.doubleValue()/2));
        //近6个月，除近1个月，剩余5个月月均无通话天数
        result.put("no_call_not_1m_day_avg_6m",String.format("%.2f",no_call_not_1m_day_6m.doubleValue()/5));
        //近6个月，除近2个月，剩余4个月月均无通话天数
        result.put("no_call_not_2m_day_avg_6m",String.format("%.2f",no_call_not_2m_day_6m.doubleValue()/4));
        //近1个月top10联系人号码
        List<String> top_10_phone_1m = Algorithm.top10ByPhone(data,applicant,1,10);
        result.put("top_10_phone_1m",top_10_phone_1m.toString());
        //近3个月top10联系人号码
        List<String> top_10_phone_3m = Algorithm.top10ByPhone(data,applicant,3,10);
        result.put("top_10_phone_3m",top_10_phone_3m.toString());
        //近6个月top10联系人号码
        List<String> top_10_phone_6m = Algorithm.top10ByPhone(data,applicant,6,10);
        result.put("top_10_phone_6m",top_10_phone_6m.toString());
        //近1个月top10联系人号码归属地
        Set<String> top_10_local_1m = Algorithm.top10ByLocation(top_10_phone_1m);
        result.put("top_10_local_1m",top_10_local_1m.toString());
        //近3个月top10联系人号码归属地
        Set<String> top_10_local_3m = Algorithm.top10ByLocation(top_10_phone_3m);
        result.put("top_10_local_3m",top_10_local_3m.toString());
        //近6个月top10联系人号码归属地
        Set<String> top_10_local_6m = Algorithm.top10ByLocation(top_10_phone_6m);
        result.put("top_10_local_6m",top_10_local_6m.toString());
        //近1个月7:00-17:00主叫次数
        Integer dial_num_7_17_1m = Algorithm.dialCallNumbers(data,applicant,1,"7:00:00","17:00:00");
        result.put("dial_num_7_17_1m",dial_num_7_17_1m);
        //近3个月7:00-17:00主叫次数
        Integer dial_num_7_17_3m = Algorithm.dialCallNumbers(data,applicant,3,"7:00:00","17:00:00");
        result.put("dial_num_7_17_3m",dial_num_7_17_3m);
        //近6个月7:00-17:00主叫次数
        Integer dial_num_7_17_6m = Algorithm.dialCallNumbers(data,applicant,6,"7:00:00","17:00:00");
        result.put("dial_num_7_17_6m",dial_num_7_17_6m);
        //近3个月7:00-17:00月均主叫次数
        result.put("dial_num_avg_7_17_3m",String.format("%.2f",dial_num_7_17_3m.doubleValue()/3));
        //近6个月7:00-17:00月均主叫次数
        result.put("dial_num_avg_7_17_6m",String.format("%.2f",dial_num_7_17_6m.doubleValue()/6));
        //近1个月7:00-17:00主叫时长
        Integer dial_length_7_17_1m = Algorithm.dialCallTimes(data,applicant,1,"7:00:00","17:00:00");
        result.put("dial_length_7_17_1m",dial_length_7_17_1m);
        //近3个月7:00-17:00主叫时长
        Integer dial_length_7_17_3m = Algorithm.dialCallTimes(data,applicant,3,"7:00:00","17:00:00");
        result.put("dial_length_7_17_3m",dial_length_7_17_3m);
        //近6个月7:00-17:00主叫时长
        Integer dial_length_7_17_6m = Algorithm.dialCallTimes(data,applicant,6,"7:00:00","17:00:00");
        result.put("dial_length_7_17_6m",dial_length_7_17_6m);
        //近3个月7:00-17:00月均主叫时长
        result.put("dial_length_avg_7_17_3m",String.format("%.2f",dial_length_7_17_3m.doubleValue()/3));
        //近6个月7:00-17:00月均主叫时长
        result.put("dial_length_avg_7_17_6m",String.format("%.2f",dial_length_7_17_6m.doubleValue()/6));
        //近1个月7:00-17:00总通话次数
        Integer total_num_7_17_1m = Algorithm.callNumbers(data,applicant,1,"7:00:00","17:00:00");
        result.put("total_num_7_17_1m",total_num_7_17_1m);
        //近3个月7:00-17:00总通话次数
        Integer total_num_7_17_3m = Algorithm.callNumbers(data,applicant,3,"7:00:00","17:00:00");
        result.put("total_num_7_17_3m",total_num_7_17_3m);
        //近6个月7:00-17:00总通话次数
        Integer total_num_7_17_6m = Algorithm.callNumbers(data,applicant,6,"7:00:00","17:00:00");
        result.put("total_num_7_17_6m",total_num_7_17_6m);
        //近3个月7:00-17:00月均总通话次数
        result.put("total_num_avg_7_17_3m",String.format("%.2f",total_num_7_17_3m.doubleValue()/3));
        //近6个月7:00-17:00月均总通话次数
        result.put("total_num_avg_7_17_6m",String.format("%.2f",total_num_7_17_6m.doubleValue()/6));
        //近1个月7:00-17:00总通话时长
        Integer total_length_7_17_1m = Algorithm.callTimes(data,applicant,1,"7:00:00","17:00:00");
        result.put("total_length_7_17_1m",total_length_7_17_1m);
        //近3个月7:00-17:00总通话时长
        Integer total_length_7_17_3m = Algorithm.callTimes(data,applicant,3,"7:00:00","17:00:00");
        result.put("total_length_7_17_3m",total_length_7_17_3m);
        //近6个月7:00-17:00总通话时长
        Integer total_length_7_17_6m = Algorithm.callTimes(data,applicant,6,"7:00:00","17:00:00");
        result.put("total_length_7_17_6m",total_length_7_17_6m);
        //近3个月7:00-17:00月均总通话时长
        result.put("total_length_avg_7_17_3m",String.format("%.2f",total_length_7_17_3m.doubleValue()/3));
        //近6个月7:00-17:00月均总通话时长
        result.put("total_length_avg_7_17_6m",String.format("%.2f",total_length_7_17_6m.doubleValue()/6));
        //近1个月7:00-17:00主叫号码数
        result.put("dial_phone_num_7_17_1m",Algorithm.dialPhoneNumbers(data,applicant,1,"7:00:00","17:00:00"));
        //近3个月7:00-17:00主叫号码数
        Integer dial_phone_num_7_17_3m = Algorithm.dialPhoneNumbers(data,applicant,3,"7:00:00","17:00:00");
        result.put("dial_phone_num_7_17_3m",dial_phone_num_7_17_3m);
        //近6个月7:00-17:00主叫号码数
        Integer dial_phone_num_7_17_6m = Algorithm.dialPhoneNumbers(data,applicant,6,"7:00:00","17:00:00");
        result.put("dial_phone_num_7_17_6m",dial_phone_num_7_17_6m);
        //近3个月7:00-17:00月均主叫号码数
        result.put("dial_phone_num_avg_7_17_3m",String.format("%.2f",dial_phone_num_7_17_3m.doubleValue()/3));
        //近6个月7:00-17:00月均主叫号码数
        result.put("dial_phone_num_avg_7_17_6m",String.format("%.2f",dial_phone_num_7_17_6m.doubleValue()/6));
        //近1个月7:00-17:00总通话号码数
        result.put("total_phone_num_7_17_1m",Algorithm.phoneNumbers(data,applicant,1,"7:00:00","17:00:00"));
        //近3个月7:00-17:00总通话号码数
        Integer total_phone_num_7_17_3m = Algorithm.phoneNumbers(data,applicant,3,"7:00:00","17:00:00");
        result.put("total_phone_num_7_17_3m",total_phone_num_7_17_3m);
        //近6个月7:00-17:00总通话号码数
        Integer total_phone_num_7_17_6m = Algorithm.phoneNumbers(data,applicant,6,"7:00:00","17:00:00");
        result.put("total_phone_num_7_17_6m",total_phone_num_7_17_6m);
        //近3个月7:00-17:00月均总通话号码数
        result.put("total_phone_num_avg_7_17_3m",String.format("%.2f",total_phone_num_7_17_3m.doubleValue()/3));
        //近6个月7:00-17:00月均总通话号码数
        result.put("total_phone_num_avg_7_17_6m",String.format("%.2f",total_phone_num_7_17_6m.doubleValue()/6));
        //近1个月7:00-17:00总通话次数比主叫次数
        result.put("total_dial_num_mul_7_17_1m",String.format("%.2f",total_num_7_17_1m.doubleValue()/dial_num_7_17_1m.doubleValue()));
        //近3个月7:00-17:00总通话次数比主叫次数
        result.put("total_dial_num_mul_7_17_3m",String.format("%.2f",total_num_7_17_3m.doubleValue()/dial_num_7_17_3m.doubleValue()));
        //近6个月7:00-17:00总通话次数比主叫次数
        result.put("total_dial_num_mul_7_17_6m",String.format("%.2f",total_num_7_17_6m.doubleValue()/dial_num_7_17_6m.doubleValue()));
        //近1个月7:00-17:00总通话时长比主叫时长
        result.put("total_dial_length_mul_7_17_1m",String.format("%.2f",total_length_7_17_1m.doubleValue()/dial_length_7_17_1m.doubleValue()));
        //近3个月7:00-17:00总通话时长比主叫时长
        result.put("total_dial_length_mul_7_17_3m",String.format("%.2f",total_length_7_17_3m.doubleValue()/dial_length_7_17_3m.doubleValue()));
        //近6个月7:00-17:00总通话时长比主叫时长
        result.put("total_dial_length_mul_7_17_6m",String.format("%.2f",total_length_7_17_6m.doubleValue()/dial_length_7_17_6m.doubleValue()));
        //近1个月17:00-22:00主叫次数
        Integer dial_num_17_22_1m = Algorithm.dialCallNumbers(data,applicant,1,"17:00:00","22:00:00");
        result.put("dial_num_17_22_1m",dial_num_17_22_1m);
        //近3个月17:00-22:00主叫次数
        Integer dial_num_17_22_3m = Algorithm.dialCallNumbers(data,applicant,3,"17:00:00","22:00:00");
        result.put("dial_num_17_22_3m",dial_num_17_22_3m);
        //近6个月17:00-22:00主叫次数
        Integer dial_num_17_22_6m = Algorithm.dialCallNumbers(data,applicant,6,"17:00:00","22:00:00");
        result.put("dial_num_17_22_6m",dial_num_17_22_6m);
        //近3个月17:00-22:00月均主叫次数
        result.put("dial_num_avg_17_22_3m",String.format("%.2f",dial_num_17_22_3m.doubleValue()/3));
        //近6个月17:00-22:00月均主叫次数
        result.put("dial_num_avg_17_22_6m",String.format("%.2f",dial_num_17_22_6m.doubleValue()/6));
        //近1个月17:00-22:00主叫时长
        Integer dial_length_17_22_1m = Algorithm.dialCallTimes(data,applicant,1,"17:00:00","22:00:00");
        result.put("dial_length_17_22_1m",dial_length_17_22_1m);
        //近3个月17:00-22:00主叫时长
        Integer dial_length_17_22_3m = Algorithm.dialCallTimes(data,applicant,3,"17:00:00","22:00:00");
        result.put("dial_length_17_22_3m",dial_length_17_22_3m);
        //近6个月17:00-22:00主叫时长
        Integer dial_length_17_22_6m = Algorithm.dialCallTimes(data,applicant,6,"17:00:00","22:00:00");
        result.put("dial_length_17_22_6m",dial_length_17_22_6m);
        //近3个月17:00-22:00月均主叫时长
        result.put("dial_length_avg_17_22_3m",String.format("%.2f",dial_length_17_22_3m.doubleValue()/3));
        //近6个月17:00-22:00月均主叫时长
        result.put("dial_length_avg_17_22_6m",String.format("%.2f",dial_length_17_22_6m.doubleValue()/6));
        //近1个月17:00-22:00总通话次数
        Integer total_num_17_22_1m = Algorithm.callNumbers(data,applicant,1,"17:00:00","22:00:00");
        result.put("total_num_17_22_1m",total_num_17_22_1m);
        //近3个月17:00-22:00总通话次数
        Integer total_num_17_22_3m = Algorithm.callNumbers(data,applicant,3,"17:00:00","22:00:00");
        result.put("total_num_17_22_3m",total_num_17_22_3m);
        //近6个月17:00-22:00总通话次数
        Integer total_num_17_22_6m = Algorithm.callNumbers(data,applicant,6,"17:00:00","22:00:00");
        result.put("total_num_17_22_6m",total_num_17_22_6m);
        //近3个月17:00-22:00月均总通话次数
        result.put("total_num_avg_17_22_3m",String.format("%.2f",total_num_17_22_3m.doubleValue()/3));
        //近6个月17:00-22:00月均总通话次数
        result.put("total_num_avg_17_22_6m",String.format("%.2f",total_num_17_22_6m.doubleValue()/6));
        //近1个月17:00-22:00总通话时长
        Integer total_length_17_22_1m = Algorithm.callTimes(data,applicant,1,"17:00:00","22:00:00");
        result.put("total_length_17_22_1m",total_length_17_22_1m);
        //近3个月17:00-22:00总通话时长
        Integer total_length_17_22_3m = Algorithm.callTimes(data,applicant,3,"17:00:00","22:00:00");
        result.put("total_length_17_22_3m",total_length_17_22_3m);
        //近6个月17:00-22:00总通话时长
        Integer total_length_17_22_6m = Algorithm.callTimes(data,applicant,6,"17:00:00","22:00:00");
        result.put("total_length_17_22_6m",total_length_17_22_6m);
        //近3个月17:00-22:00月均总通话时长
        result.put("total_length_avg_17_22_3m",String.format("%.2f",total_length_17_22_3m.doubleValue()/3));
        //近6个月17:00-22:00月均总通话时长
        result.put("total_length_avg_17_22_6m",String.format("%.2f",total_length_17_22_6m.doubleValue()/6));
        //近1个月17:00-22:00主叫号码数
        Integer dial_phone_num_17_22_1m = Algorithm.dialPhoneNumbers(data,applicant,1,"17:00:00","22:00:00");
        result.put("dial_phone_num_17_22_1m",dial_phone_num_17_22_1m);
        //近3个月17:00-22:00主叫号码数
        Integer dial_phone_num_17_22_3m = Algorithm.dialPhoneNumbers(data,applicant,3,"17:00:00","22:00:00");
        result.put("dial_phone_num_17_22_3m",dial_phone_num_17_22_3m);
        //近6个月17:00-22:00主叫号码数
        Integer dial_phone_num_17_22_6m = Algorithm.dialPhoneNumbers(data,applicant,6,"17:00:00","22:00:00");
        result.put("dial_phone_num_17_22_6m",dial_phone_num_17_22_6m);
        //近3个月17:00-22:00月均主叫号码数
        result.put("dial_phone_num_avg_17_22_3m",String.format("%.2f",dial_phone_num_17_22_3m.doubleValue()/3));
        //近6个月17:00-22:00月均主叫号码数
        result.put("dial_phone_num_avg_17_22_6m",String.format("%.2f",dial_phone_num_17_22_6m.doubleValue()/6));
        //近1个月17:00-22:00总通话号码数
        Integer total_phone_num_17_22_1m = Algorithm.phoneNumbers(data,applicant,1,"17:00:00","22:00:00");
        result.put("total_phone_num_17_22_1m",total_phone_num_17_22_1m);
        //近3个月17:00-22:00总通话号码数
        Integer total_phone_num_17_22_3m = Algorithm.phoneNumbers(data,applicant,3,"17:00:00","22:00:00");
        result.put("total_phone_num_17_22_3m",total_phone_num_17_22_3m);
        //近6个月17:00-22:00总通话号码数
        Integer total_phone_num_17_22_6m = Algorithm.phoneNumbers(data,applicant,6,"17:00:00","22:00:00");
        result.put("total_phone_num_17_22_6m",total_phone_num_17_22_6m);
        //近3个月17:00-22:00月均总通话号码数
        result.put("total_phone_num_avg_17_22_3m",String.format("%.2f",total_phone_num_17_22_3m.doubleValue()/3));
        //近6个月17:00-22:00月均总通话号码数
        result.put("total_phone_num_avg_17_22_6m",String.format("%.2f",total_phone_num_17_22_6m.doubleValue()/6));
        //近1个月17:00-22:00总通话次数比主叫次数
        result.put("total_dial_num_mul_17_22_1m",String.format("%.2f",total_num_17_22_1m.doubleValue()/dial_num_17_22_1m.doubleValue()));
        //近3个月17:00-22:00总通话次数比主叫次数
        result.put("total_dial_num_mul_17_22_3m",String.format("%.2f",total_num_17_22_3m.doubleValue()/dial_num_17_22_3m.doubleValue()));
        //近6个月17:00-22:00总通话次数比主叫次数
        result.put("total_dial_num_mul_17_22_6m",String.format("%.2f",total_num_17_22_6m.doubleValue()/dial_num_17_22_6m.doubleValue()));
        //近1个月17:00-22:00总通话时长比主叫时长
        result.put("total_dial_length_mul_17_22_1m",String.format("%.2f",total_length_17_22_1m.doubleValue()/dial_length_17_22_1m.doubleValue()));
        //近3个月17:00-22:00总通话时长比主叫时长
        result.put("total_dial_length_mul_17_22_3m",String.format("%.2f",total_length_17_22_3m.doubleValue()/dial_length_17_22_3m.doubleValue()));
        //近6个月17:00-22:00总通话时长比主叫时长
        result.put("total_dial_length_mul_17_22_6m",String.format("%.2f",total_length_17_22_6m.doubleValue()/dial_length_17_22_6m.doubleValue()));
        //近1个月22:00-7:00主叫次数
        Integer dial_num_22_7_1m = Algorithm.dialCallNumbers(data,applicant,1,"22:00:00","7:00:00");
        result.put("dial_num_22_7_1m",dial_num_22_7_1m);
        //近3个月22:00-7:00主叫次数
        Integer dial_num_22_7_3m = Algorithm.dialCallNumbers(data,applicant,3,"22:00:00","7:00:00");
        result.put("dial_num_22_7_3m",dial_num_22_7_3m);
        //近6个月22:00-7:00主叫次数
        Integer dial_num_22_7_6m = Algorithm.dialCallNumbers(data,applicant,6,"22:00:00","7:00:00");
        result.put("dial_num_22_7_6m",dial_num_22_7_6m);
        //近3个月22:00-7:00月均主叫次数
        result.put("dial_num_avg_22_7_3m",String.format("%.2f",dial_num_22_7_3m.doubleValue()/3));
        //近6个月22:00-7:00月均主叫次数
        result.put("dial_num_avg_22_7_6m",String.format("%.2f",dial_num_22_7_6m.doubleValue()/6));
        //近1个月22:00-7:00主叫时长
        Integer dial_length_22_7_1m = Algorithm.dialCallTimes(data,applicant,1,"22:00:00","7:00:00");
        result.put("dial_length_22_7_1m",dial_length_22_7_1m);
        //近3个月22:00-7:00主叫时长
        Integer dial_length_22_7_3m = Algorithm.dialCallTimes(data,applicant,3,"22:00:00","7:00:00");
        result.put("dial_length_22_7_3m",dial_length_22_7_3m);
        //近6个月22:00-7:00主叫时长
        Integer dial_length_22_7_6m = Algorithm.dialCallTimes(data,applicant,6,"22:00:00","7:00:00");
        result.put("dial_length_22_7_6m",dial_length_22_7_6m);
        //近3个月22:00-7:00月均主叫时长
        result.put("dial_length_avg_22_7_3m",String.format("%.2f",dial_length_22_7_3m.doubleValue()/3));
        //近6个月22:00-7:00月均主叫时长
        result.put("dial_length_avg_22_7_6m",String.format("%.2f",dial_length_22_7_6m.doubleValue()/6));
        //近1个月22:00-7:00总通话次数
        Integer total_num_22_7_1m = Algorithm.callNumbers(data,applicant,1,"22:00:00","7:00:00");
        result.put("total_num_22_7_1m",total_num_22_7_1m);
        //近3个月22:00-7:00总通话次数
        Integer total_num_22_7_3m = Algorithm.callNumbers(data,applicant,3,"22:00:00","7:00:00");
        result.put("total_num_22_7_3m",total_num_22_7_3m);
        //近6个月22:00-7:00总通话次数
        Integer total_num_22_7_6m = Algorithm.callNumbers(data,applicant,6,"22:00:00","7:00:00");
        result.put("total_num_22_7_6m",total_num_22_7_6m);
        //近3个月22:00-7:00月均总通话次数
        result.put("total_num_avg_22_7_3m",String.format("%.2f",total_num_22_7_3m.doubleValue()/3));
        //近6个月22:00-7:00月均总通话次数
        result.put("total_num_avg_22_7_6m",String.format("%.2f",total_num_22_7_6m.doubleValue()/6));
        //近1个月22:00-7:00总通话时长
        Integer total_length_22_7_1m = Algorithm.callTimes(data,applicant,1,"22:00:00","7:00:00");
        result.put("total_length_22_7_1m",total_length_22_7_1m);
        //近3个月22:00-7:00总通话时长
        Integer total_length_22_7_3m = Algorithm.callTimes(data,applicant,3,"22:00:00","7:00:00");
        result.put("total_length_22_7_3m",total_length_22_7_3m);
        //近6个月22:00-7:00总通话时长
        Integer total_length_22_7_6m = Algorithm.callTimes(data,applicant,6,"22:00:00","7:00:00");
        result.put("total_length_22_7_6m",total_length_22_7_6m);
        //近3个月22:00-7:00月均总通话时长
        result.put("total_length_avg_22_7_3m",String.format("%.2f",total_length_22_7_3m.doubleValue()/3));
        //近6个月22:00-7:00月均总通话时长
        result.put("total_length_avg_22_7_6m",String.format("%.2f",total_length_22_7_6m.doubleValue()/6));
        //近1个月22:00-7:00主叫号码数
        Integer dial_phone_num_22_7_1m = Algorithm.dialPhoneNumbers(data,applicant,1,"22:00:00","7:00:00");
        result.put("dial_phone_num_22_7_1m",dial_phone_num_22_7_1m);
        //近3个月22:00-7:00主叫号码数
        Integer dial_phone_num_22_7_3m = Algorithm.dialPhoneNumbers(data,applicant,3,"22:00:00","7:00:00");
        result.put("dial_phone_num_22_7_3m",dial_phone_num_22_7_3m);
        //近6个月22:00-7:00主叫号码数
        Integer dial_phone_num_22_7_6m = Algorithm.dialPhoneNumbers(data,applicant,6,"22:00:00","7:00:00");
        result.put("dial_phone_num_22_7_6m",dial_phone_num_22_7_6m);
        //近3个月22:00-7:00月均主叫号码数
        result.put("dial_phone_num_avg_22_7_3m",String.format("%.2f",dial_phone_num_22_7_3m.doubleValue()/3));
        //近6个月22:00-7:00月均主叫号码数
        result.put("dial_phone_num_avg_22_7_6m",String.format("%.2f",dial_phone_num_22_7_6m.doubleValue()/6));
        //近1个月22:00-7:00总通话号码数
        Integer total_phone_num_22_7_1m = Algorithm.phoneNumbers(data,applicant,1,"22:00:00","7:00:00");
        result.put("total_phone_num_22_7_1m",total_phone_num_22_7_1m);
        //近3个月22:00-7:00总通话号码数
        Integer total_phone_num_22_7_3m = Algorithm.phoneNumbers(data,applicant,3,"22:00:00","7:00:00");
        result.put("total_phone_num_22_7_3m",total_phone_num_22_7_3m);
        //近6个月22:00-7:00总通话号码数
        Integer total_phone_num_22_7_6m = Algorithm.phoneNumbers(data,applicant,6,"22:00:00","7:00:00");
        result.put("total_phone_num_22_7_6m",total_phone_num_22_7_6m);
        //近3个月22:00-7:00月均总通话号码数
        result.put("total_phone_num_avg_22_7_3m",String.format("%.2f",total_phone_num_22_7_3m.doubleValue()/3));
        //近6个月22:00-7:00月均总通话号码数
        result.put("total_phone_num_avg_22_7_6m",String.format("%.2f",total_phone_num_22_7_6m.doubleValue()/6));
        //近1个月22:00-7:00总通话次数比主叫次数
        result.put("total_dial_num_mul_22_7_1m",String.format("%.2f",total_num_22_7_1m.doubleValue()/dial_num_22_7_1m.doubleValue()));
        //近3个月22:00-7:00总通话次数比主叫次数
        result.put("total_dial_num_mul_22_7_3m",String.format("%.2f",total_num_22_7_3m.doubleValue()/dial_num_22_7_3m.doubleValue()));
        //近6个月22:00-7:00总通话次数比主叫次数
        result.put("total_dial_num_mul_22_7_6m",String.format("%.2f",total_num_22_7_6m.doubleValue()/dial_num_22_7_6m.doubleValue()));
        //近1个月22:00-7:00总通话时长比主叫时长
        result.put("total_dial_length_mul_22_7_1m",String.format("%.2f",total_length_22_7_1m.doubleValue()/dial_length_22_7_1m.doubleValue()));
        //近3个月22:00-7:00总通话时长比主叫时长
        result.put("total_dial_length_mul_22_7_3m",String.format("%.2f",total_length_22_7_3m.doubleValue()/dial_length_22_7_3m.doubleValue()));
        //近6个月22:00-7:00总通话时长比主叫时长
        result.put("total_dial_length_mul_22_7_6m",String.format("%.2f",total_length_22_7_6m.doubleValue()/dial_length_22_7_6m.doubleValue()));
        //近1月停留城市
        Map<String,Integer> city_1m = Algorithm.city(data,applicant,1);
        result.put("city_1m",city_1m.keySet().toString());
        //近3月停留城市
        Map<String,Integer> city_3m = Algorithm.city(data,applicant,3);
        result.put("city_3m",city_3m.keySet().toString());
        //近6月停留城市
        Map<String,Integer> city_6m = Algorithm.city(data,applicant,6);
        result.put("city_6m",city_6m.keySet().toString());
        //近1月停留城市个数
        result.put("city_num_1m",city_1m.size());
        //近3月停留城市个数
        result.put("city_num_3m",city_3m.size());
        //近6月停留城市个数
        result.put("city_num_6m",city_6m.size());
        //近1月停留次数最多的城市
        result.put("city_num_1m",Algorithm.maxCity(city_1m));
        //近3月停留次数最多的城市
        result.put("city_num_3m",Algorithm.maxCity(city_3m));
        //近6月停留次数最多的城市
        result.put("city_num_6m",Algorithm.maxCity(city_6m));
        //近1月停留城市时长（天）
        Map<String,Integer> city_length_1m = city_1m;
        result.put("city_length_1m",city_length_1m);
        //近3月停留城市时长（天）
        Map<String,Integer> city_length_3m = city_1m;
        result.put("city_length_3m",city_length_3m);
        //近6月停留城市时长（天）
        Map<String,Integer> city_length_6m = city_1m;
        result.put("city_length_6m",city_length_6m);
        //近1月停留时长最长的城市
        result.put("max_city_length_1m",Algorithm.maxCity(city_length_1m));
        //近3月停留时长最长的城市
        result.put("max_city_length_3m",Algorithm.maxCity(city_length_3m));
        //近6月停留时长最长的城市
        result.put("max_city_length_6m",Algorithm.maxCity(city_length_6m));
        //近3个月TOP10联系人变化指数
        result.put("top_10_change_exp_3m",Algorithm.top10ChangeExp(data,applicant,3,0));
        //近3个月，除近1个月，TOP10联系人变化指数
        result.put("top_10_change_exp_not_1m_3m",Algorithm.top10ChangeExp(data,applicant,3,1));
        //近4个月TOP10联系人变化指数
        result.put("top_10_change_exp_4m",Algorithm.top10ChangeExp(data,applicant,4,0));
        //近4个月，除近1个月，TOP10联系人变化指数
        result.put("top_10_change_exp_not_1m_4m",Algorithm.top10ChangeExp(data,applicant,4,1));
        //近4个月，除近2个月，TOP10联系人变化指数
        result.put("top_10_change_exp_not_2m_4m",Algorithm.top10ChangeExp(data,applicant,4,2));
        //近5个月TOP10联系人变化指数
        result.put("top_10_change_exp_5m",Algorithm.top10ChangeExp(data,applicant,5,0));
        //近5个月，除近1个月，TOP10联系人变化指数
        result.put("top_10_change_exp_not_1m_5m",Algorithm.top10ChangeExp(data,applicant,5,1));
        //近5个月，除近2个月，TOP10联系人变化指数
        result.put("top_10_change_exp_not_2m_5m",Algorithm.top10ChangeExp(data,applicant,5,2));
        //近6个月TOP10联系人变化指数
        result.put("top_10_change_exp_6m",Algorithm.top10ChangeExp(data,applicant,6,0));
        //近6个月，除近1个月，TOP10联系人变化指数
        result.put("top_10_change_exp_not_1m_6m",Algorithm.top10ChangeExp(data,applicant,6,1));
        //近6个月，除近2个月，TOP10联系人变化指数
        result.put("top_10_change_exp_not_2m_6m",Algorithm.top10ChangeExp(data,applicant,6,2));
        //近3个月TOP10联系人手机号归属地为申请地的数量
        Integer top_10_local_num_3m = Algorithm.localNum(top_10_phone_3m,applicant);
        result.put("top_10_local_num_3m",top_10_local_num_3m);
        //近3个月，每个月都出现的手机号码归属地为申请地的数量
        result.put("top_10_ever_month_local_num_3m",Algorithm.localNum(Algorithm.everMonthLocalNum(data,applicant,3),applicant));
        //近6个月TOP10联系人手机号归属地为申请地的数量
        Integer top_10_local_num_6m = Algorithm.localNum(top_10_phone_6m,applicant);
        result.put("top_10_local_num_6m",top_10_local_num_6m);
        //近6个月，每个月都出现的手机号码归属地为申请地的数量
        result.put("top_10_ever_month_local_num_6m",Algorithm.localNum(Algorithm.everMonthLocalNum(data,applicant,6),applicant));
        //近3个月TOP10联系人手机号归属地为申请地的数量
        result.put("top_10_apply_num_3m",top_10_local_num_3m);
        //近6个月TOP10联系人手机号归属地为申请地的数量
        result.put("top_10_apply_num_6m",top_10_local_num_6m);
        //近3个月TOP10联系人手机号归属地为非申请地的数量
        result.put("top_10_not_apply_num_3m",10-top_10_local_num_3m);
        //近6个月TOP10联系人手机号归属地为非申请地的数量
        result.put("top_10_not_apply_num_3m",10-top_10_local_num_6m);
        //近3个月TOP10联系人手机号归属地为户籍所在地的数量
        result.put("top_10_register_num_3m",Algorithm.registerNum(top_10_phone_3m,applicant));
        //近6个月TOP10联系人手机号归属地为户籍所在地的数量
        result.put("top_10_register_num_6m",Algorithm.registerNum(top_10_phone_6m,applicant));
        //近1个月TOP10联系人非11位手机号码数
        result.put("top_10_not_11_num_1m",Algorithm.topNumbers(data,applicant,1,0,10,11));
        //近3个月TOP10联系人非11位手机号码数
        result.put("top_10_not_11_num_3m",Algorithm.topNumbers(data,applicant,3,0,10,11));
        //近4个月，除近1个月，TOP10联系人非11位手机号码数
        result.put("top_10_not_11_num_not_1m_4m",Algorithm.topNumbers(data,applicant,4,1,10,11));
        //近4个月，除近2个月，TOP10联系人非11位手机号码数
        result.put("top_10_not_11_num_not_2m_4m",Algorithm.topNumbers(data,applicant,4,2,10,11));
        //近6个月TOP10联系人非11位手机号码数
        result.put("top_10_not_11_num_6m",Algorithm.topNumbers(data,applicant,6,0,10,11));
        //近6个月，除近1个月，TOP10联系人非11位手机号码数
        result.put("top_10_not_11_num_not_1m_6m",Algorithm.topNumbers(data,applicant,6,1,10,11));
        //近6个月，除近2个月，TOP10联系人非11位手机号码数
        result.put("top_10_not_11_num_not_2m_6m",Algorithm.topNumbers(data,applicant,6,2,10,11));
        //近6个月，TOP10联系人出现过非11位手机号的月份数
        result.put("top_10_not_11_month_num_6m",Algorithm.topMonths(data,applicant,6,0,10,11));
        //近1个月通话时长小于等于5秒的次数
        Integer low_5s_length_num_1m = Algorithm.talkTime(data,applicant,1,2,5);
        result.put("low_5s_length_num_1m",low_5s_length_num_1m);
        //近3个月通话时长小于等于5秒的次数
        Integer low_5s_length_num_3m = Algorithm.talkTime(data,applicant,3,2,5);
        result.put("low_5s_length_num_3m",low_5s_length_num_3m);
        //近6个月通话时长小于等于5秒的次数
        Integer low_5s_length_num_6m = Algorithm.talkTime(data,applicant,6,2,5);
        result.put("low_5s_length_num_6m",low_5s_length_num_6m);
        //近1个月通话时长小于等于10秒的次数
        Integer low_10s_length_num_1m = Algorithm.talkTime(data,applicant,1,2,10);
        result.put("low_10s_length_num_1m",low_10s_length_num_1m);
        //近3个月通话时长小于等于10秒的次数
        Integer low_10s_length_num_3m = Algorithm.talkTime(data,applicant,3,2,10);
        result.put("low_10s_length_num_3m",low_10s_length_num_3m);
        //近6个月通话时长小于等于10秒的次数
        Integer low_10s_length_num_6m = Algorithm.talkTime(data,applicant,6,2,10);
        result.put("low_10s_length_num_6m",low_10s_length_num_6m);
        //近1个月通话时长小于等于30秒的次数
        Integer low_30s_length_num_1m = Algorithm.talkTime(data,applicant,1,2,30);
        result.put("low_30s_length_num_1m",low_30s_length_num_1m);
        //近3个月通话时长小于等于30秒的次数
        Integer low_30s_length_num_3m = Algorithm.talkTime(data,applicant,3,2,30);
        result.put("low_30s_length_num_3m",low_30s_length_num_3m);
        //近6个月通话时长小于等于30秒的次数
        Integer low_30s_length_num_6m = Algorithm.talkTime(data,applicant,6,2,30);
        result.put("low_30s_length_num_6m",low_30s_length_num_6m);
        //近1个月通话时长大于20秒的次数
        Integer over_20s_length_num_1m = Algorithm.talkTime(data,applicant,1,3,20);
        result.put("over_20s_length_num_1m",over_20s_length_num_1m);
        //近3个月通话时长大于20秒的次数
        Integer over_20s_length_num_3m = Algorithm.talkTime(data,applicant,3,3,20);
        result.put("over_20s_length_num_3m",over_20s_length_num_3m);
        //近6个月通话时长大于20秒的次数
        Integer over_20s_length_num_6m = Algorithm.talkTime(data,applicant,6,3,20);
        result.put("over_20s_length_num_6m",over_20s_length_num_6m);
        //近1个月通话时长大于30秒的次数
        Integer over_30s_length_num_1m = Algorithm.talkTime(data,applicant,1,3,30);
        result.put("over_30s_length_num_1m",over_30s_length_num_1m);
        //近3个月通话时长大于30秒的次数
        Integer over_30s_length_num_3m = Algorithm.talkTime(data,applicant,3,3,30);
        result.put("over_30s_length_num_3m",over_30s_length_num_3m);
        //近6个月通话时长大于30秒的次数
        Integer over_30s_length_num_6m = Algorithm.talkTime(data,applicant,6,3,30);
        result.put("over_30s_length_num_6m",over_30s_length_num_6m);
        //近1个月的总通话次数
        Integer callNumbersByOneMonth = Algorithm.callNumbers(data,applicant,1);
        result.put("total_call_num_1m",callNumbersByOneMonth);
        //近3个月的总通话次数
        Integer callNumbersByThreeMonth = Algorithm.callNumbers(data,applicant,3);
        result.put("total_call_num_3m",callNumbersByThreeMonth);
        //近6个月的总通话次数
        Integer callNumbersBySixMonth = Algorithm.callNumbers(data,applicant,6);
        result.put("total_call_num_6m",callNumbersBySixMonth);
        //近3个月月均总通话次数
        result.put("total_avg_call_num_3m",String.format("%.2f",callNumbersByThreeMonth.doubleValue()/3));
        //近6个月月均总通话次数
        result.put("total_avg_call_num_6m",String.format("%.2f",callNumbersBySixMonth.doubleValue()/6));
        //近6个月月均总通话次数比近1个月总通话次数
        result.put("total_avg_call_num_6m_1m",String.format("%.2f",callNumbersBySixMonth.doubleValue()/(6*callNumbersByOneMonth)));
        //近1个月通话时长小于等于5秒的次数占总通话次数比例
        result.put("total_low_5s_per_1m",String.format("%.2f",low_5s_length_num_1m.doubleValue()/callNumbersByOneMonth.doubleValue()));
        //近3个月通话时长小于等于5秒的次数占总通话次数比例
        result.put("total_low_5s_per_3m",String.format("%.2f",low_5s_length_num_3m.doubleValue()/callNumbersByThreeMonth.doubleValue()));
        //近6个月通话时长小于等于5秒的次数占总通话次数比例
        result.put("total_low_5s_per_6m",String.format("%.2f",low_5s_length_num_6m.doubleValue()/callNumbersBySixMonth.doubleValue()));
        //近1个月通话时长小于等于10秒的次数占总通话次数比例
        result.put("total_low_10s_per_1m",String.format("%.2f",low_10s_length_num_1m.doubleValue()/callNumbersByOneMonth.doubleValue()));
        //近3个月通话时长小于等于10秒的次数占总通话次数比例
        result.put("total_low_10s_per_3m",String.format("%.2f",low_10s_length_num_3m.doubleValue()/callNumbersByThreeMonth.doubleValue()));
        //近6个月通话时长小于等于10秒的次数占总通话次数比例
        result.put("total_low_10s_per_6m",String.format("%.2f",low_10s_length_num_6m.doubleValue()/callNumbersBySixMonth.doubleValue()));
        //近1个月通话时长小于等于20秒的次数
        Integer low_20s_length_num_1m = Algorithm.talkTime(data,applicant,1,2,20);
        //近1个月通话时长小于等于20秒的次数占总通话次数比例
        result.put("total_low_20s_per_1m",String.format("%.2f",low_20s_length_num_1m.doubleValue()/callNumbersByOneMonth.doubleValue()));
        //近3个月通话时长小于等于20秒的次数
        Integer low_20s_length_num_3m = Algorithm.talkTime(data,applicant,3,2,20);
        //近3个月通话时长小于等于20秒的次数占总通话次数比例
        result.put("total_low_20s_per_3m",String.format("%.2f",low_20s_length_num_3m.doubleValue()/callNumbersByThreeMonth.doubleValue()));
        //近6个月通话时长小于等于20秒的次数
        Integer low_20s_length_num_6m = Algorithm.talkTime(data,applicant,6,2,20);
        //近6个月通话时长小于等于20秒的次数占总通话次数比例
        result.put("total_low_20s_per_6m",String.format("%.2f",low_20s_length_num_6m.doubleValue()/callNumbersBySixMonth.doubleValue()));
        //近1个月连续3天以上无通话的次数
        result.put("3_day_no_call_1m",Algorithm.noCall(data,applicant,1,3));
        //近1个月连续5天以上无通话的次数
        result.put("5_day_no_call_1m",Algorithm.noCall(data,applicant,1,5));
        //近1个月连续7天以上无通话的次数
        result.put("7_day_no_call_1m",Algorithm.noCall(data,applicant,1,7));
        //近3个月连续3天以上无通话的次数
        result.put("3_day_no_call_3m",Algorithm.noCall(data,applicant,3,3));
        //近3个月连续5天以上无通话的次数
        result.put("5_day_no_call_3m",Algorithm.noCall(data,applicant,3,5));
        //近3个月连续7天以上无通话的次数
        result.put("7_day_no_call_3m",Algorithm.noCall(data,applicant,3,7));
        //近6个月连续3天以上无通话的次数
        result.put("3_day_no_call_6m",Algorithm.noCall(data,applicant,6,3));
        //近6个月连续5天以上无通话的次数
        result.put("5_day_no_call_6m",Algorithm.noCall(data,applicant,6,5));
        //近6个月连续7天以上无通话的次数
        result.put("7_day_no_call_6m",Algorithm.noCall(data,applicant,6,7));
        //近1个月日均通话次数
        result.put("day_avg_num_1m",String.format("%.2f",callNumbersByOneMonth.doubleValue()/ CarrierDateUtil.daysBetween(CarrierDateUtil.monthsBefore(1,applicant.get("customerApplyDate").toString()),CarrierDateUtil.getNowDate())));
        //近3个月日均通话次数
        result.put("day_avg_num_3m",String.format("%.2f",callNumbersByThreeMonth.doubleValue()/CarrierDateUtil.daysBetween(CarrierDateUtil.monthsBefore(3,applicant.get("customerApplyDate").toString()),CarrierDateUtil.getNowDate())));
        //近6个月日均通话次数
        result.put("day_avg_num_6m",String.format("%.2f",callNumbersBySixMonth.doubleValue()/CarrierDateUtil.daysBetween(CarrierDateUtil.monthsBefore(6,applicant.get("customerApplyDate").toString()),CarrierDateUtil.getNowDate())));
        //近3个月，除近1个月，其余2个月月均通话次数
        result.put("day_avg_num_not_1m_3m",Algorithm.callAvgNumbers(data,applicant,3,1));
        //近6个月，除近1个月，其余5个月月均通话次数
        result.put("day_avg_num_not_1m_6m",Algorithm.callAvgNumbers(data,applicant,6,1));
        //除近3个月，剩余月总通话时长
        Integer day_call_length_3m = Algorithm.totalCalllength(data,applicant,3);
        result.put("day_call_length_3m",day_call_length_3m);
        //除近3个月，剩余月月均总通话时长
        String day_avg_call_length_3m = Algorithm.avgCalllength(data,applicant,3);
        result.put("day_avg_call_length_3m",day_avg_call_length_3m);
        //入网时长（天）network_length
        result.put("network_length",Algorithm.networkLength(data,applicant));
        //近1个月主叫次数
        Integer dial_num_1m = Algorithm.dialNum(data,applicant,1);
        result.put("dial_num_1m",dial_num_1m);
        //近3个月主叫次数
        Integer dial_num_3m = Algorithm.dialNum(data,applicant,3);
        result.put("dial_num_3m",Algorithm.dialNum(data,applicant,3));
        //近6个月主叫次数
        Integer dial_num_6m = Algorithm.dialNum(data,applicant,6);
        result.put("dial_num_6m",Algorithm.dialNum(data,applicant,6));
        //近3个月月均主叫次数
        result.put("dial_avg_num_3m",String.format("%.2f",dial_num_3m.doubleValue()/3));
        //近6个月月均主叫次数
        result.put("dial_avg_num_6m",String.format("%.2f",dial_num_6m.doubleValue()/6));
        //近6个月月均主叫次数比近1个月主叫次数
        result.put("dial_avg_num_6m_1m",String.format("%.2f",dial_num_6m.doubleValue()/(6*dial_num_1m)));
        //近1个月无通话天数
        Integer no_call_1m = Algorithm.callDuration(data,applicant,1,0);
        result.put("no_call_1m",no_call_1m);
        //近3个月无通话天数
        Integer no_call_3m = Algorithm.callDuration(data,applicant,3,0);
        result.put("no_call_3m",no_call_3m);
        //近6个月无通话天数
        Integer no_call_6m = Algorithm.callDuration(data,applicant,6,0);
        result.put("no_call_6m",no_call_6m);
        //近3个月无通话天数比近1个月无通话天数
        result.put("no_call_3m_1m",String.format("%.2f",no_call_3m.doubleValue()/no_call_1m.doubleValue()));
        //近6个月无通话天数比近1个月无通话天数
        result.put("no_call_6m_1m",String.format("%.2f",no_call_6m.doubleValue()/no_call_1m.doubleValue()));
        //近3个月消费总金额
        Integer total_money_3m = Algorithm.totalMoney(data,applicant,3);
        //近6个月消费总金额
        Integer total_money_6m = Algorithm.totalMoney(data,applicant,6);
        //近3个月月均消费金额
        result.put("total_avg_money_3m",String.format("%.2f",total_money_3m.doubleValue()/(3*100)));
        //近6个月月均消费金额
        result.put("total_avg_money_6m",String.format("%.2f",total_money_6m.doubleValue()/(6*100)));
        //近1个月联系3次以上的号码数
        result.put("contact_3_num_1m",Algorithm.contactNumbers(data,applicant,1,3).size());
        //近1个月联系5次以上的号码数
        result.put("contact_5_num_1m",Algorithm.contactNumbers(data,applicant,1,5).size());
        //近3个月联系3次以上的号码数
        result.put("contact_3_num_3m",Algorithm.contactNumbers(data,applicant,3,3).size());
        //近3个月联系5次以上的号码数
        result.put("contact_5_num_3m",Algorithm.contactNumbers(data,applicant,3,5).size());
        //近1个月最多通话所在城
        //近1个月第二多通话所在城
        List<String> firstCallCity1m = Algorithm.firstCallCity(data,applicant,1);
        result.put("first_call_city_1m",firstCallCity1m.size()>=1?firstCallCity1m.get(0):"");
        result.put("second_call_city_1m",firstCallCity1m.size()>=2?firstCallCity1m.get(1):"");
        //近3个月最多通话所在城
        //近3个月第二多通话所在城
        List<String> firstCallCity3m = Algorithm.firstCallCity(data,applicant,3);
        result.put("first_call_city_3m",firstCallCity3m.size()>=1?firstCallCity3m.get(0):"");
        result.put("second_call_city_3m",firstCallCity3m.size()>=2?firstCallCity3m.get(1):"");
        //近6个月最多通话所在城
        //近6个月第二多通话所在城
        List<String> firstCallCity6m = Algorithm.firstCallCity(data,applicant,6);
        result.put("first_call_city_6m",firstCallCity6m.size()>=1?firstCallCity6m.get(0):"");
        result.put("second_call_city_6m",firstCallCity6m.size()>=2?firstCallCity6m.get(1):"");
        //近1个月最多通话所在城市次数
        //近1个月第二多通话所在城市次数
        List<Integer> firstCallNumCity1m = Algorithm.firstCallNumCity(data,applicant,1);
        result.put("first_call_num_city_1m",firstCallNumCity1m.size()>=1?firstCallNumCity1m.get(0):"");
        result.put("second_call_num_city_1m",firstCallNumCity1m.size()>=2?firstCallNumCity1m.get(1):"");
        //近3个月最多通话所在城市次数
        //近3个月第二多通话所在城市次数
        List<Integer> firstCallNumCity3m = Algorithm.firstCallNumCity(data,applicant,3);
        result.put("first_call_num_city_3m",firstCallNumCity3m.size()>=1?firstCallNumCity3m.get(0):"");
        result.put("second_call_num_city_3m",firstCallNumCity3m.size()>=2?firstCallNumCity3m.get(1):"");
        //近6个月最多通话所在城市次数
        //近6个月第二多通话所在城市次数
        List<Integer> firstCallNumCity6m = Algorithm.firstCallNumCity(data,applicant,6);
        result.put("first_call_num_city_6m",firstCallNumCity6m.size()>=1?firstCallNumCity6m.get(0):"");
        result.put("second_call_num_city_6m",firstCallNumCity6m.size()>=2?firstCallNumCity6m.get(1):"");
        //近1个月总通话次数比近1个月最多通话所在城市次数
        result.put("total_first_call_num_1m",firstCallNumCity1m.size()==0?0:String.format("%.2f",callNumbersByOneMonth.doubleValue()/firstCallNumCity1m.get(0).doubleValue()));
        //近3个月总通话次数比近3个月最多通话所在城市次数
        result.put("total_first_call_num_3m",firstCallNumCity3m.size()==0?0:String.format("%.2f",callNumbersByThreeMonth.doubleValue()/firstCallNumCity3m.get(0).doubleValue()));
        //近6个月总通话次数比近6个月最多通话所在城市次数
        result.put("total_first_call_num_6m", firstCallNumCity6m.size()==0?0:String.format("%.2f",callNumbersBySixMonth.doubleValue()/firstCallNumCity6m.get(0).doubleValue()));
        //近1个月总通话时长（秒）
        result.put("total_call_length_sec_1m",total_length_1m);
        //近1个月总通话时长（分）
        result.put("total_call_length_minute_1m",Math.ceil(total_length_1m.doubleValue()/60));
        //近3个月总通话时长（秒）
        result.put("total_call_length_sec_3m",total_length_3m);
        //近3个月总通话时长（分）
        result.put("total_call_length_minute_3m",Math.ceil(total_length_3m.doubleValue()/60));
        //近6个月总通话时长（秒）
        result.put("total_call_length_sec_6m",total_length_6m);
        //近6个月总通话时长（分）
        result.put("total_call_length_minute_6m",Math.ceil(total_length_6m.doubleValue()/60));
        //近3个月月均总通话时长
        result.put("avg_call_length_sec_3m",String.format("%.2f",total_length_3m.doubleValue()/3));
        //近6个月月均总通话时长
        result.put("avg_call_length_sec_6m",String.format("%.2f",total_length_6m.doubleValue()/6));
        //除近2个月，剩余月总通话时长（秒）
        Integer day_call_length_2m = Algorithm.totalCalllength(data,applicant,2);
        result.put("day_call_length_sec_2m",day_call_length_2m);
        //除近2个月，剩余月总通话时长（分）
        result.put("day_call_length_minute_2m",Math.ceil(day_call_length_2m.doubleValue()/60));
        //除近2个月，剩余月月均总通话时长（秒）
        result.put("avg_call_length_sec_2m",String.format("%.2f",day_call_length_2m.doubleValue()/2));
        //除近2个月，剩余月月均总通话时长（分）
        result.put("avg_call_length_minute_2m",String.format("%.2f",day_call_length_2m.doubleValue()/(2*60)));
        //除近2个月，剩余月月均总通话时长比近1个月总通话时长
        result.put("avg_call_length_sec_2m_total_1m",String.format("%.2f",day_call_length_2m.doubleValue()/(2*total_length_1m)));
        //近1个月TOP10联系人联系最多号码的次数（正常号码）
        //近3个月TOP10联系人联系最多号码的次数（正常号码）
        //近6个月TOP10联系人联系最多号码的次数（正常号码）

        //近3个月互通号码个数（正常号码）
        result.put("inter_peer_num_3m",Algorithm.interPeerNum(data,applicant,3));
        //近6个月互通号码个数（正常号码）
        result.put("inter_peer_num_6m",Algorithm.interPeerNum(data,applicant,6));
        //近3个月联系10次以上号码个数（正常号码）
        result.put("good_friend_num_3m",Algorithm.goodFriendNum(data,applicant,3));
        //近6个月联系10次以上号码个数（正常号码）
        result.put("good_friend_num_6m",Algorithm.goodFriendNum(data,applicant,6));
        //近1个月拨打110次数
        result.put("call_110_num_1m",Algorithm.callCount(data,applicant,1,"110"));
        //近3个月拨打110次数
        result.put("call_110_num_3m",Algorithm.callCount(data,applicant,3,"110"));
        //近6个月拨打110次数
        result.put("call_110_num_6m",Algorithm.callCount(data,applicant,6,"110"));
        //近1个月拨打12345次数
        result.put("call_12345_num_1m",Algorithm.callCount(data,applicant,1,"12345"));
        //近3个月拨打12345次数
        result.put("call_12345_num_3m",Algorithm.callCount(data,applicant,3,"12345"));
        //近6个月拨打12345次数
        result.put("call_12345_num_6m",Algorithm.callCount(data,applicant,6,"12345"));
        //近1个月拨打12321次数
        result.put("call_12321_num_1m",Algorithm.callCount(data,applicant,1,"12321"));
        //近3个月拨打12321次数
        result.put("call_12321_num_3m",Algorithm.callCount(data,applicant,3,"12321"));
        //近6个月拨打12321次数
        result.put("call_12321_num_6m",Algorithm.callCount(data,applicant,6,"12321"));
        //近1个月互通5次以上号码个数（正常号码）
        result.put("interflow_5_num_1m",Algorithm.contactNumbers(data,applicant,1,5));
        //近3个月互通5次以上号码个数（正常号码）
        result.put("interflow_5_num_3m",Algorithm.contactNumbers(data,applicant,3,5));
        //近1个月互通10次以上号码个数（正常号码）
        result.put("interflow_10_num_1m",Algorithm.contactNumbers(data,applicant,1,10));
        //近3个月互通10次以上号码个数（正常号码）
        result.put("interflow_10_num_3m",Algorithm.contactNumbers(data,applicant,3,10));
        //近6个月互通10次以上号码个数（正常号码）
        result.put("interflow_10_num_6m",Algorithm.contactNumbers(data,applicant,6,10));
        //近1个月23:30-7:00的通话次数
        Integer total_num_2330_7_1m = Algorithm.callNumbers(data,applicant,1,"23:30:00","7:00:00");
        result.put("total_num_2330_7_1m",total_num_2330_7_1m);
        //近3个月23:30-7:00的通话次数
        Integer total_num_2330_7_3m = Algorithm.callNumbers(data,applicant,3,"23:30:00","7:00:00");
        result.put("total_num_2330_7_3m",total_num_2330_7_3m);
        //近6个月23:30-7:00的通话次数
        Integer total_num_2330_7_6m = Algorithm.callNumbers(data,applicant,6,"23:30:00","7:00:00");
        result.put("total_num_2330_7_6m",total_num_2330_7_6m);

        return result;
    }

    /**
     * 测试类main方法
     * @param args
     */
    public static void main(String[] args) throws Exception {

        JSONObject result = new JSONObject();
        //封装数据源
        JSONObject jsonObject = JSONObject.parseObject(DataSource.dataSource());
        //申请人信息
        JSONObject applicant = JSONObject.parseObject(DataSource.applicant());
        //数据清洗入口方法
        try {
            clean(jsonObject.getJSONObject("data"),applicant.getJSONObject("userInfo"),result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //遍历结果集
        System.out.println(result.toJSONString());

    }

}
