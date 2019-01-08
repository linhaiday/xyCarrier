package com.linhai.taobao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linhai.comm.CarrierDateUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class TaobaoCrossAlgorithm {

    //配偶_淘宝认证姓名是否与申请填写时姓名一致    int(否：0，是：1)
    public static void consistencyName(JSONObject partner, JSONObject application, JSONObject result) {
        //申请填写的姓名
        String applicantName = application.getString("customerName");
        //申请人配偶姓名
        String partnerName = partner.getJSONObject("userinfo").getString("real_name");
        result.put("tb_partner_name_consistency", StringUtils.equals(applicantName,partnerName)?1:0);
        System.out.println("配偶_淘宝认证姓名是否与申请填写时姓名一致："+result.get("tb_partner_name_consistency"));
    }

    //配偶_申请时填写的亲密联系人是否出现在订单中	int(否：0，是：1)
    public static void contact(JSONObject partner, JSONObject application, JSONObject result) {
        //亲密联系人
        String contacts = application.getString("customerIntimateContactsMobile1")+","
                +application.getString("customerIntimateContactsMobile2")+","
                +application.getString("customerIntimateContactsMobile3");
        //申请人配偶的亲密联系人是否出现
        int partnerContacts = 0;
        JSONArray partnerTradedetails = partner.getJSONObject("tradedetails").getJSONArray("tradedetails");

        //配偶_亲密联系人是否出现在订单中
        for (Object tradedetail:partnerTradedetails) {
            if(StringUtils.equals(contacts,((JSONObject)tradedetail).getString("deliver_name"))){
                partnerContacts = 1;
                break;
            }
        }

        result.put("tb_partner_contact",partnerContacts);
        System.out.println("配偶_申请时填写的亲密联系人是否出现在订单中:"+result.get("tb_partner_contact"));
    }

    //配偶_收件地址总数量
    //配偶_收件地址中收件人为本人的城市个数
    //配偶_收件地址中是否有申请人户籍所在地
    //配偶_收货地址为申请城市的数量
    //配偶_收件人为非本人的地址数量
    public static void address(JSONObject partner, JSONObject application, JSONObject result) {

        int applyPlaceCityCount = 0;
        boolean bl = false;
        //申请人姓名
        String selfName = application.getString("customerName");
        //申请人户籍所在地
        String house = application.getString("customerHouseholdRegisterAddress");
        //申请城市
        String applyPlaceCity = application.getString("customerApplyPlaceCity");

        JSONArray recentdeliveraddress = partner.getJSONArray("recentdeliveraddress");
        for (Object recent:recentdeliveraddress) {
            JSONObject jsonObject = JSON.parseObject(recent.toString());
            if(!bl && StringUtils.equals(house,jsonObject.getString("deliver_address"))) bl = true;
            if(StringUtils.equals(applyPlaceCity,jsonObject.getString("city"))) applyPlaceCityCount += 1;
        }

        result.put("tb_partner_partner_register",bl);
        result.put("tb_partner_apply_city",applyPlaceCityCount);
        System.out.println("配偶_收件地址中是否有申请人户籍所在地："+bl);
        System.out.println("配偶_收货地址为申请城市的数量："+applyPlaceCityCount);

    }

    //近N天有效订单收件人为配偶的地址数量
    //配偶_近N天有效订单收件人为申请人的地址数量
    public static void validOrderSelfAddress(JSONObject self, JSONObject partner, JSONObject application, JSONObject result, int day) {

        Set<String> selfAddress = new HashSet<>();
        Set<String> partnerAddress = new HashSet<>();
        //申请日期
        String applicationDate = application.getString("customerApplyDate");
        //申请日期最近N天
        String beforeDate = CarrierDateUtil.getBeforeOrAfterDate(applicationDate,day);
        //得到yyyy-mm-dd
        String nBefore = CarrierDateUtil.yearMonthDay(beforeDate);
        //申请人姓名
        String selfName = application.getString("customerName");
        //配偶姓名
        String partnerName = partner.getJSONObject("userinfo").getString("real_name");
        JSONArray tradedetails = self.getJSONObject("tradedetails").getJSONArray("tradedetails");
        for (Object tradedetail:tradedetails) {
            if(!CarrierDateUtil.dateScope(applicationDate,
                    nBefore,
                    CarrierDateUtil.yearMonthDay(JSON.parseObject(tradedetail.toString()).getString("trade_createtime")),"")
                    || StringUtils.indexOf(JSON.parseObject(tradedetail.toString()).getString("trade_status"),"成功")<0) continue;
            //本人
            if(StringUtils.equals(JSON.parseObject(tradedetail.toString()).getString("deliver_name"),selfName))
                selfAddress.add(JSON.parseObject(tradedetail.toString()).getString("deliver_address"));
            //配偶
            if(StringUtils.equals(JSON.parseObject(tradedetail.toString()).getString("deliver_name"),partnerName))
                partnerAddress.add(JSON.parseObject(tradedetail.toString()).getString("deliver_address"));
        }

        result.put("tb_valid_order_self_address_"+day+"d",selfAddress.size());
        result.put("tb_valid_order_spouse_address_"+day+"d",partnerAddress.size());
        System.out.println("近"+day+"天有效订单收件人为本人的地址数量"+selfAddress.size());
        System.out.println("近"+day+"天有效订单收件人为配偶的地址数量"+partnerAddress.size());

        selfAddress.clear();
        partnerAddress.clear();
        tradedetails = partner.getJSONObject("tradedetails").getJSONArray("tradedetails");
        for (Object tradedetail:tradedetails) {
            if(!CarrierDateUtil.dateScope(applicationDate,
                    nBefore,
                    CarrierDateUtil.yearMonthDay(JSON.parseObject(tradedetail.toString()).getString("trade_createtime")),"")
                    || StringUtils.indexOf(JSON.parseObject(tradedetail.toString()).getString("trade_status"),"成功")<0) continue;
            //本人
            if(StringUtils.equals(JSON.parseObject(tradedetail.toString()).getString("deliver_name"),partnerName))
                selfAddress.add(JSON.parseObject(tradedetail.toString()).getString("deliver_address"));
            //申请人
            if(StringUtils.equals(JSON.parseObject(tradedetail.toString()).getString("deliver_name"),selfName))
                partnerAddress.add(JSON.parseObject(tradedetail.toString()).getString("deliver_address"));
        }

        result.put("tb_partner_valid_order_self_address_"+day+"d",selfAddress.size());
        result.put("tb_partner_valid_order_spouse_address_"+day+"d",partnerAddress.size());
        System.out.println("配偶_近"+day+"天有效订单收件人为本人的地址数量"+selfAddress.size());
        System.out.println("配偶_近"+day+"天有效订单收件人为申请人的地址数量"+partnerAddress.size());
    }

    //配偶_申请人现居住地址送货次数
    public static void placeCnt(JSONObject partner, JSONObject application, JSONObject result) {
        //申请人现居住地址
        String applicationAddress = application.getString("customerHomeAddress");

        int num = 0;
        JSONArray tradedetails = partner.getJSONObject("tradedetails").getJSONArray("tradedetails");
        for (Object tradedetail:tradedetails) {
            if(StringUtils.equals(JSON.parseObject(tradedetail.toString()).getString("deliver_address"),applicationAddress))
                num += 1;
        }

        result.put("tb_partner_place_cnt",num);
        System.out.println("配偶_申请人现居住地址送货次数:"+num);
    }

    //配偶_申请人现居住地址送货首次使用距离当前天数
    //配偶_申请人现居住地址送货最后一次使用距离当前天数
    public static void placeInterval(JSONObject partner, JSONObject application, JSONObject result) {
        //申请人现居住地址
        String applicationAddress = application.getString("customerHomeAddress");

        List<String> list = new ArrayList<>();
        JSONArray tradedetails = partner.getJSONObject("tradedetails").getJSONArray("tradedetails");
        for (Object tradedetail:tradedetails) {
            if(StringUtils.equals(JSON.parseObject(tradedetail.toString()).getString("deliver_address"),applicationAddress)) {
                list.add(CarrierDateUtil.yearMonthDay(JSON.parseObject(tradedetail.toString()).getString("trade_createtime")).replaceAll("-",""));
            }
        }

        int days = 0;
        if(list.size()>0) {
            Collections.sort(list);
            days = CarrierDateUtil.differentDays(CarrierDateUtil.toDate(list.get(0)), new Date());
        }
        result.put("tb_partner_place_first_interval", days);
        System.out.println("配偶_申请人现居住地址送货首次使用距离当前天数:" + days);

        days = 0;
        if(list.size()>0) {
            days = CarrierDateUtil.differentDays(CarrierDateUtil.toDate(list.get(list.size() - 1)), new Date());
        }
        result.put("tb_partner_place_last_interval", days);
        System.out.println("配偶_申请人现居住地址送货最后一次使用距离当前天数:" + days);
    }

    //近一年内收货地址中是否有和配偶相同的收货地址
    //配偶_近一年内收货地址中是否有和申请人相同的收货地址
    public static void sameAddressYear(JSONObject self, JSONObject partner, JSONObject application, JSONObject result,int day) {
        //申请日期
        String applicationDate = application.getString("customerApplyDate");
        //申请日期最近N天
        String beforeDate = CarrierDateUtil.getBeforeOrAfterDate(applicationDate,day);
        //得到yyyy-mm-dd
        String nBefore = CarrierDateUtil.yearMonthDay(beforeDate);
        //申请人地址
        Set<String> applicationAddress = new HashSet<>();
        //配偶地址
        Set<String> partnerAddress = new HashSet<>();
        //
        JSONArray recentdeliveraddress = self.getJSONArray("recentdeliveraddress");
        for (Object address:recentdeliveraddress) {
            if(CarrierDateUtil.dateScope(applicationDate,
                    nBefore,
                    CarrierDateUtil.yearMonthDay(JSON.parseObject(address.toString()).getString("trade_createtime")),"")
                    && StringUtils.isNotBlank(JSON.parseObject(address.toString()).getString("deliver_address")))
                applicationAddress.add(JSON.parseObject(address.toString()).getString("deliver_address"));
        }

        recentdeliveraddress = partner.getJSONArray("recentdeliveraddress");
        for (Object address:recentdeliveraddress) {
            if(CarrierDateUtil.dateScope(applicationDate,
                    nBefore,
                    CarrierDateUtil.yearMonthDay(JSON.parseObject(address.toString()).getString("trade_createtime")),"")
                    && StringUtils.isNotBlank(JSON.parseObject(address.toString()).getString("deliver_address")))
                partnerAddress.add(JSON.parseObject(address.toString()).getString("deliver_address"));
        }

        List<String> address1 = new ArrayList<>(applicationAddress);
        List<String> address2 = new ArrayList<>(partnerAddress);
        address1.retainAll(address2);

        result.put("tb_same_address_year",address1.size()>0?1:0);
        result.put("tb_partner_same_address_year",address1.size()>0?1:0);
        System.out.println("近一年内收货地址中是否有和配偶相同的收货地址:"+(address1.size()>0?1:0));
        System.out.println("配偶_近一年内收货地址中是否有和申请人相同的收货地址:"+(address1.size()>0?1:0));

    }

    //淘宝收货号码是否含配偶的电话号码
    //淘宝收货姓名是否含配偶的姓名
    //配偶_淘宝收货号码是否含申请人的电话号码
    //配偶_淘宝收货姓名是否含申请人的姓名
    public static void samePhone(JSONObject self, JSONObject partner, JSONObject application, JSONObject result) {
        //申请人电话
        String applicationPhone = application.getString("customerMobile");
        //申请人姓名
        String applicationName = application.getString("customerName");
        //配偶电话
        String partnerPhone = partner.getJSONObject("userinfo").getString("phone_number");
        //配偶姓名
        String partnerName = partner.getJSONObject("userinfo").getString("real_name");
        int appPhone = 0;
        int appName = 0;
        int partPhone = 0;
        int partName = 0;
        JSONArray recentdeliveraddress = self.getJSONArray("recentdeliveraddress");
        for (Object address:recentdeliveraddress) {
            if(StringUtils.equals(JSON.parseObject(address.toString()).getString("deliver_mobilephone"),partnerPhone))
                partPhone = 1;
            if(StringUtils.equals(JSON.parseObject(address.toString()).getString("deliver_name"),partnerName))
                partName = 1;
        }

        recentdeliveraddress = partner.getJSONArray("recentdeliveraddress");
        for (Object address:recentdeliveraddress) {
            if(StringUtils.equals(JSON.parseObject(address.toString()).getString("deliver_mobilephone"),applicationPhone))
                appPhone = 1;
            if(StringUtils.equals(JSON.parseObject(address.toString()).getString("deliver_name"),applicationName))
                appName = 1;
        }

        result.put("tb_same_phone",partPhone);
        result.put("tb_partner_same_phone",appPhone);
        System.out.println("淘宝收货号码是否含配偶的电话号码："+partPhone);
        System.out.println("配偶_淘宝收货号码是否含申请人的电话号码："+appPhone);
        result.put("tb_same_name",partName);
        result.put("tb_partner_same_name",appName);
        System.out.println("淘宝收货姓名是否含配偶的姓名："+partName);
        System.out.println("配偶_淘宝收货号码是否含申请人的电话号码："+appName);
    }

    //只有最近N个月的订单包含配偶，而之前没有
    //配偶_只有最近N个月的订单包含申请人，而之前没有
    public static void spouseOnlyRecently(JSONObject self, JSONObject partner, JSONObject application, JSONObject result,int m) {
        int app = 0;
        int part = 0;
        //申请人姓名
        String applicationName = application.getString("customerName");
        //配偶姓名
        String partnerName = partner.getJSONObject("userinfo").getString("real_name");
        //申请日期
        String applicationDate = application.getString("customerApplyDate");
        //申请日期最近N个月
        String beforeDate = CarrierDateUtil.monthsBefore(m,applicationDate);
        //得到yyyy-mm-dd
        String nBefore = CarrierDateUtil.yearMonthDay(beforeDate);
        JSONArray recentdeliveraddress = self.getJSONArray("recentdeliveraddress");
        for (Object address:recentdeliveraddress) {
            if(CarrierDateUtil.dateScope(applicationDate,
                    nBefore,
                    CarrierDateUtil.yearMonthDay(JSON.parseObject(address.toString()).getString("trade_createtime")),"yyyy-mm-dd")
                    && StringUtils.equals(JSON.parseObject(address.toString()).getString("deliver_name"),partnerName))
                part = 1;
        }
        for (Object address:recentdeliveraddress) {
            if(CarrierDateUtil.compareDate(nBefore,
                    CarrierDateUtil.yearMonthDay(JSON.parseObject(address.toString()).getString("trade_createtime")),"yyyy-mm-dd")>0
                    && StringUtils.equals(JSON.parseObject(address.toString()).getString("deliver_name"),partnerName))
                part = 0;
        }

        result.put("tb_spouse_only_recently",part);
        System.out.println("只有最近"+m+"个月的订单包含配偶，而之前没有："+part);

        recentdeliveraddress = partner.getJSONArray("recentdeliveraddress");
        for (Object address:recentdeliveraddress) {
            if(CarrierDateUtil.dateScope(applicationDate,
                    nBefore,
                    CarrierDateUtil.yearMonthDay(JSON.parseObject(address.toString()).getString("trade_createtime")),"yyyy-mm-dd")
                    && StringUtils.equals(JSON.parseObject(address.toString()).getString("deliver_name"),applicationName))
                app = 1;
        }
        for (Object address:recentdeliveraddress) {
            if(CarrierDateUtil.compareDate(nBefore,
                    CarrierDateUtil.yearMonthDay(JSON.parseObject(address.toString()).getString("trade_createtime")),"yyyy-mm-dd")>0
                    && StringUtils.equals(JSON.parseObject(address.toString()).getString("deliver_name"),applicationName))
                app = 0;
        }

        result.put("tb_partner_spouse_only_recently",app);
        System.out.println("只有最近"+m+"个月的订单包含配偶，而之前没有："+app);

    }
}
