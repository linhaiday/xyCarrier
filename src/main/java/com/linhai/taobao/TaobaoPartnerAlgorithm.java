package com.linhai.taobao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linhai.comm.CarrierDateUtil;
import com.linhai.comm.LevenshteinUtil;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 申请人配偶变量清洗
 */
public class TaobaoPartnerAlgorithm {

    //配偶_芝麻分
    public static void zmf(JSONObject partner, JSONObject result) {
        JSONObject partnerUserinfo = partner.getJSONObject("userinfo");
        result.put("tb_partner_zmf",partnerUserinfo.getString("zm_score")==null?"":partnerUserinfo.getString("zm_score"));
        System.out.println("配偶_申请人配偶芝麻分："+result.get("tb_partner_zmf"));
    }

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

    //配偶_支付宝账号
    public static void zfb(JSONObject partner, JSONObject result) {
        result.put("tb_partner_zfb",partner.getJSONObject("userinfo").getString("alipay_account")==null?"":partner.getJSONObject("userinfo").getString("alipay_account"));
        System.out.println("配偶_支付宝账号:"+result.getString("tb_partner_zfb"));
    }

    //配偶_实名认证姓名
    public static void realName(JSONObject partner, JSONObject result) {
        result.put("tb_partner_real_name",partner.getJSONObject("userinfo").getString("real_name")==null?"":partner.getJSONObject("userinfo").getString("real_name"));
        System.out.println("配偶_支付宝账号:"+result.getString("tb_partner_real_name"));
    }

    //配偶_实名认证身份证号
    public static void realId(JSONObject partner, JSONObject result) {
        result.put("tb_partner_real_id",partner.getJSONObject("userinfo").getString("id_card")==null?"":partner.getJSONObject("userinfo").getString("id_card"));
        System.out.println("配偶_实名认证身份证号:"+result.getString("tb_partner_real_id"));
    }

    //配偶_实名认证状态
    public static void realStatus(JSONObject partner, JSONObject result) {
        result.put("tb_partner_real_status",StringUtils.equals(partner.getJSONObject("userinfo").getString("account_auth"),"已通过实名认证")?1:0);
        System.out.println("配偶_实名认状态:"+result.getString("tb_partner_real_status"));
    }

    //配偶_账户余额
    public static void account(JSONObject partner, JSONObject result) {
        result.put("tb_partner_account",StringUtils.isBlank(partner.getJSONObject("alipaywealth").getString("balance"))?"":partner.getJSONObject("alipaywealth").getString("balance"));
        System.out.println("配偶_账户余额:"+result.getString("tb_partner_account"));
    }

    //配偶_花呗可用额度
    public static void usable(JSONObject partner, JSONObject result) {
        result.put("tb_partner_usable_limit_hb",StringUtils.isBlank(partner.getJSONObject("alipaywealth").getString("huabei_creditamount"))?"":partner.getJSONObject("alipaywealth").getString("huabei_creditamount"));
        System.out.println("配偶_花呗可用额度:"+result.getString("tb_partner_usable_limit_hb"));
    }

    //配偶_花呗总额度
    public static void total(JSONObject partner, JSONObject result) {
        result.put("tb_partner_total_limit_hb",StringUtils.isBlank(partner.getJSONObject("alipaywealth").getString("huabei_totalcreditamount"))?"":partner.getJSONObject("alipaywealth").getString("huabei_totalcreditamount"));
        System.out.println("配偶_花呗总额度:"+result.getString("tb_partner_total_limit_hb"));
    }

    //配偶_有效订单总次数
    //配偶_有效订单总金额
    public static void validOrder(JSONObject partner, JSONObject result) {

        int num = 0;
        int money = 0;

        JSONArray tradedetails = partner.getJSONObject("tradedetails").getJSONArray("tradedetails");
        for (Object tradedetail:tradedetails) {
            if(StringUtils.indexOf(JSON.parseObject(tradedetail.toString()).getString("trade_status"),"成功")<0) continue;
            num += 1;
            money += Integer.parseInt(JSON.parseObject(tradedetail.toString()).getString("actual_fee")==null?"0":JSON.parseObject(tradedetail.toString()).getString("actual_fee"));
        }

        String mon = String.format("%.2f",Integer.valueOf(money).doubleValue()/100);
        result.put("tb_partner_valid_order_cnt",num);
        result.put("tb_partner_valid_order_amount",mon);
        System.out.println("配偶_有效订单总次数:"+num);
        System.out.println("配偶_有效订单总金额:"+mon);

    }

    //配偶_最近N天有效订单次数
    //配偶_最近N天有效订单金额
    //配偶_最近N天居住地有效订单次数
    public static void validOrderDay(JSONObject partner,JSONObject application, JSONObject result,int day){

        int num = 0;
        int addrNum = 0;
        int money = 0;
        //居住地
        String applicationAddr = application.getString("customerHouseholdRegisterAddress");
        //申请日期
        String applicationDate = application.getString("customerApplyDate");
        //申请日期最近N天
        String beforeDate = CarrierDateUtil.getBeforeOrAfterDate(applicationDate,day);
        //得到yyyy-mm-dd
        String nBefore = CarrierDateUtil.yearMonthDay(beforeDate);

        JSONArray tradedetails = partner.getJSONObject("tradedetails").getJSONArray("tradedetails");
        for (Object tradedetail:tradedetails) {
            if(!CarrierDateUtil.dateScope(applicationDate,
                    nBefore,
                    CarrierDateUtil.yearMonthDay(JSON.parseObject(tradedetail.toString()).getString("trade_createtime")),"")
                    || StringUtils.indexOf(JSON.parseObject(tradedetail.toString()).getString("trade_status"),"成功")<0)
                continue;

            num += 1;
            money += Integer.parseInt(JSON.parseObject(tradedetail.toString()).getString("actual_fee")==null?"0":JSON.parseObject(tradedetail.toString()).getString("actual_fee"));
            if(LevenshteinUtil.isSimilarAddress(JSON.parseObject(tradedetail.toString()).getString("trade_status"),applicationAddr))
                addrNum += 1;
        }

        String mon = String.format("%.2f",Integer.valueOf(money).doubleValue()/100);
        result.put("tb_partner_valid_order_cnt_"+day+"d",num);
        result.put("tb_partner_valid_order_amount_"+day+"d",mon);
        result.put("tb_partner_place_valid_order_amount_"+day+"d",addrNum);
        System.out.println("配偶_最近"+day+"天有效订单次数:"+num);
        System.out.println("配偶_最近"+day+"天有效订单金额:"+mon);
        System.out.println("配偶_最近"+day+"天居住地有效订单次数:"+addrNum);
    }

    //配偶_每个月的有效订单次数
    //配偶_每个月的有效订单金额
    public static void eachValidOrder(JSONObject partner, JSONObject result) {

        Map<String,Integer> num = new HashMap<>();
        Map<String,String> money = new HashMap<>();
        JSONArray tradedetails = partner.getJSONObject("tradedetails").getJSONArray("tradedetails");
        for (Object tradedetail:tradedetails) {
            if(StringUtils.indexOf(JSON.parseObject(tradedetail.toString()).getString("trade_status"),"成功")<0) continue;
            String date = CarrierDateUtil.yearMonth(JSON.parseObject(tradedetail.toString()).getString("trade_createtime"));
            num.put(date,num.get(date)==null?1:num.get(date)+1);
            String je = JSON.parseObject(tradedetail.toString()).getString("actual_fee");
            money.put(date,money.get(date)==null?je:(Integer.parseInt(money.get(date))+Integer.parseInt(je))+"");
        }

        for (String key:money.keySet()) {
            money.put(key,Integer.parseInt(money.get(key))/100+"");
        }
        result.put("tb_partner_each_valid_order_cnt",num);
        result.put("tb_partner_each_valid_order_amount",money);
        System.out.println("配偶_每个月的有效订单次数："+num);
        System.out.println("配偶_每个月的有效订单金额："+money);
    }

    //配偶_月平均有效订单次数
    //配偶_月平均有效订单金额
    public static void avgValidOrder(JSONObject result) {
        Double count = 0d;
        int num = 0;
        Map<String,Integer> cnt = (Map<String,Integer>)result.get("tb_partner_each_valid_order_cnt");
        Map<String,String> amount = (Map<String,String>)result.get("tb_partner_each_valid_order_amount");
        for (String key:cnt.keySet()) {
            num += Integer.parseInt(cnt.get(key).toString());
        }
        for (String key:amount.keySet()) {
            count += Double.parseDouble(amount.get(key).toString());
        }
        result.put("tb_partner_avg_valid_order_cnt",String.format("%.2f",Integer.valueOf(num).doubleValue()/cnt.keySet().size()));
        result.put("tb_partner_avg_valid_order_amount",String.format("%.2f",count/amount.keySet().size()));
        System.out.println("配偶_月平均有效订单次数："+result.get("tb_partner_avg_valid_order_cnt"));
        System.out.println("配偶_月平均有效订单金额："+result.get("tb_partner_avg_valid_order_amount"));
    }

    //配偶_大于等于N元的有效订单数量
    public static void upValidOrder(JSONObject partner, JSONObject result,int money,String partneerCode) {
        int num = 0;
        JSONArray tradedetails = partner.getJSONObject("tradedetails").getJSONArray("tradedetails");
        for (Object tradedetail:tradedetails) {
            if(StringUtils.indexOf(JSON.parseObject(tradedetail.toString()).getString("trade_status"),"成功")>=0
                    && Integer.parseInt(JSON.parseObject(tradedetail.toString()).getString("actual_fee"))>=money*100)
                num += 1;
        }

        result.put(partneerCode,num);
        System.out.println("配偶_大于等于"+money+"元的有效订单数量:"+num);
    }

    //配偶_最近N天除本人外的收货号码数
    public static void excSelfPhoneCnt(JSONObject partner, JSONObject application, JSONObject result, int day, String partnerCode) {

        int num = 0;
        //申请日期
        String applicationDate = application.getString("customerApplyDate");
        //申请日期最近N天
        String beforeDate = CarrierDateUtil.getBeforeOrAfterDate(applicationDate,day);
        //得到yyyy-mm-dd
        String nBefore = CarrierDateUtil.yearMonthDay(beforeDate);

        JSONArray tradedetails = partner.getJSONObject("tradedetails").getJSONArray("tradedetails");
        String partnerName = partner.getJSONObject("userinfo").getString("real_name");
        for (Object tradedetail:tradedetails) {
            if(CarrierDateUtil.dateScope(applicationDate,
                    nBefore,
                    CarrierDateUtil.yearMonthDay(JSON.parseObject(tradedetail.toString()).getString("trade_createtime")),"")
                    && StringUtils.indexOf(JSON.parseObject(tradedetail.toString()).getString("trade_status"),"成功")>=0
                    && !StringUtils.equals(JSON.parseObject(tradedetail.toString()).getString("deliver_name"),partnerName))
                num += 1;
        }

        result.put(partnerCode,num);
        System.out.println("配偶_最近"+day+"天除本人外的收货号码数："+num);
    }

    //配偶_无购物月数
    public static void noShopMonth(JSONObject partner, JSONObject application, JSONObject result) {

        try {
            //申请日期
            String applicationDate = application.getString("customerApplyDate");
            //申请日期与当前日期之间包含的所有月份
            List<String> list = CarrierDateUtil.getMonthBetween(applicationDate,CarrierDateUtil.getNowDate());
            Set<String> set = new HashSet<>();
            JSONArray tradedetails = partner.getJSONObject("tradedetails").getJSONArray("tradedetails");
            for (Object tradedetail:tradedetails) {
                if(CarrierDateUtil.dateScope(CarrierDateUtil.getNowDate(),
                        applicationDate,
                        CarrierDateUtil.yearMonthDay(JSON.parseObject(tradedetail.toString()).getString("trade_createtime")),"")
                        && StringUtils.indexOf(JSON.parseObject(tradedetail.toString()).getString("trade_status"),"成功")>=0)
                    set.add(CarrierDateUtil.yearMonth(JSON.parseObject(tradedetail.toString()).getString("trade_createtime")));
                //if(CarrierDateUtil.compareDate(applicationDate,CarrierDateUtil.getNowDate(),"yyyy-MM-dd")>=0)
            }
            Set<String> all = new HashSet(list);
            all.removeAll(set);

            result.put("tb_partner_no_shop_month",all.size());
            System.out.println("配偶_无购物月数："+all.size());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //配偶_收件地址总数量
    //配偶_收件地址中收件人为本人的城市个数
    //配偶_收件地址中是否有申请人户籍所在地
    //配偶_收货地址为申请城市的数量
    //配偶_收件人为非本人的地址数量
    public static void address(JSONObject partner, JSONObject application, JSONObject result) {

        Set<String> address = new HashSet<>();
        Set<String> city = new HashSet<>();
        int applyPlaceCityCount = 0;
        int exeSelf = 0;
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
            address.add(jsonObject.get("deliver_address").toString());
            if(StringUtils.equals(selfName,jsonObject.getString("deliver_name")))
                city.add(jsonObject.getString("city"));
            else exeSelf += 1;
            if(!bl && StringUtils.equals(house,jsonObject.getString("deliver_address"))) bl = true;
            if(StringUtils.equals(applyPlaceCity,jsonObject.getString("city"))) applyPlaceCityCount += 1;
        }

        result.put("tb_partner_address",address.size());
        result.put("tb_partner_self_address",city.size());
        result.put("tb_partner_partner_register",bl);
        result.put("tb_partner_apply_city",applyPlaceCityCount);
        result.put("tb_partner_exc_self_address",exeSelf);
        System.out.println("配偶_收件地址总数量："+address.size());
        System.out.println("配偶_收件地址中收件人为本人的城市个数："+city.size());
        System.out.println("配偶_收件地址中是否有申请人户籍所在地："+bl);
        System.out.println("配偶_收货地址为申请城市的数量："+applyPlaceCityCount);
        System.out.println("配偶_收件人为非本人的地址数量："+exeSelf);

    }

    //配偶_近N天有效订单收件人为本人的地址数量
    //配偶_近N天有效订单收件人为申请人的地址数量
    public static void validOrderSelfAddress(JSONObject partner, JSONObject application, JSONObject result,int day) {


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

        JSONArray tradedetails = partner.getJSONObject("tradedetails").getJSONArray("tradedetails");
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

    //配偶_1点-7点下订单的笔数占比
    public static void orderPerHour(JSONObject partner, JSONObject result) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        int num = 0;
        JSONArray tradedetails = partner.getJSONObject("tradedetails").getJSONArray("tradedetails");
        for (Object tradedetail:tradedetails) {
            if(CarrierDateUtil.compareDate("01:00:00",format.format(CarrierDateUtil.toDate(JSON.parseObject(tradedetail.toString()).getString("trade_createtime"))),"HH:mm:ss")<1
                    && CarrierDateUtil.compareDate("07:00:00",format.format(CarrierDateUtil.toDate(JSON.parseObject(tradedetail.toString()).getString("trade_createtime"))),"HH:mm:ss")>-1) {
                num += 1;
            }
        }

        result.put("tb_partner_order_per_1_7_hour",String.format("%,2f",Integer.valueOf(num).doubleValue()/tradedetails.size()));
        System.out.println("配偶_1点-7点下订单的笔数占比:"+result.get("tb_partner_order_per_1_7_hour"));
    }

    //配偶_账单中出现某字段的订单次数
    //配偶_账单中出现某字段的订单金额
    public static void orderCntAmount(JSONObject partner, JSONObject result,String[] fields,String str) {

        int num = 0;
        int money = 0;
        JSONArray tradedetails = partner.getJSONObject("tradedetails").getJSONArray("tradedetails");
        for (Object tradedetail:tradedetails) {
            JSONArray sub_orders = JSON.parseObject(tradedetail.toString()).getJSONArray("sub_orders");
            bk:for (Object order:sub_orders) {
                for (String field:fields) {
                    if(StringUtils.indexOf(JSON.parseObject(order.toString()).getString("item_name"),field)>=0){
                        num +=1;
                        money += Integer.parseInt(JSON.parseObject(tradedetail.toString()).getString("actual_fee"));
                        break bk;
                    }
                }
            }
        }

        result.put("tb_partner_"+str+"_order_cnt",num);
        System.out.println("配偶_账单中出现"+str+"字段的订单次数:"+num);
        result.put("tb_partner_"+str+"_order_amount",money/100);
        System.out.println("配偶_账单中出现"+str+"字段的订单金额:"+money/100);
    }
}
