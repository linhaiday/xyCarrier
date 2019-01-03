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
 * Created by linhai on 2018/12/15.
 */
public class TaobaoAlgorithm {

    public static int test(){

        return 1;
    }

    //芝麻分
    public static void zmf(JSONObject self, JSONObject partner, JSONObject result) {
        JSONObject selfUserinfo = self.getJSONObject("userinfo");
        JSONObject partnerUserinfo = partner.getJSONObject("userinfo");
        result.put("tb_zmf",selfUserinfo.getString("zm_score")==null?"":selfUserinfo.getString("zm_score"));
        result.put("tb_partner_zmf",partnerUserinfo.getString("zm_score")==null?"":partnerUserinfo.getString("zm_score"));
        System.out.println("申请人芝麻分："+result.get("tb_zmf"));
        System.out.println("申请人配偶芝麻分："+result.get("tb_partner_zmf"));
    }

    //淘宝认证姓名是否与申请填写时姓名一致    int(否：0，是：1)
    public static void consistencyName(JSONObject self, JSONObject partner, JSONObject application, JSONObject result) {
        //申请填写的姓名
        String applicantName = application.getString("customerName");
        //申请人姓名
        String selfName = self.getJSONObject("userinfo").getString("real_name");
        //申请人配偶姓名
        String partnerName = self.getJSONObject("userinfo").getString("real_name");
        result.put("tb_name_consistency", StringUtils.equals(applicantName,selfName)?1:0);
        result.put("tb_partner_name_consistency", StringUtils.equals(applicantName,partnerName)?1:0);
        System.out.println("淘宝认证姓名是否与申请填写时姓名一致："+result.get("tb_name_consistency"));
        System.out.println("配偶_淘宝认证姓名是否与申请填写时姓名一致："+result.get("tb_partner_name_consistency"));
    }

    //申请时填写的亲密联系人是否出现在订单中	int(否：0，是：1)
    public static void contact(JSONObject self, JSONObject partner, JSONObject application, JSONObject result) {
        //亲密联系人
        String contacts = application.getString("customerIntimateContactsMobile1")+","
                +application.getString("customerIntimateContactsMobile2")+","
                +application.getString("customerIntimateContactsMobile3");
        //申请人的亲密联系人是否出现
        int selfContacts = 0;
        //申请人配偶的亲密联系人是否出现
        int partnerContacts = 0;
        JSONArray selfTradedetails = self.getJSONObject("tradedetails").getJSONArray("tradedetails");
        JSONArray partnerTradedetails = partner.getJSONObject("tradedetails").getJSONArray("tradedetails");
        //亲密联系人是否出现在订单中
        for (Object tradedetail:selfTradedetails) {
            if(StringUtils.indexOf(contacts,((JSONObject)tradedetail).getString("deliver_mobilephone"))>=0){
                selfContacts = 1;
                break;
            };
        }
        //配偶_亲密联系人是否出现在订单中
        for (Object tradedetail:partnerTradedetails) {
            if(StringUtils.equals(contacts,((JSONObject)tradedetail).getString("deliver_name"))){
                partnerContacts = 1;
                break;
            };
        }

        result.put("tb_contact",selfContacts);
        result.put("tb_partner_contact\n",partnerContacts);
        System.out.println("申请时填写的亲密联系人是否出现在订单中:"+result.get("tb_contact"));
        System.out.println("配偶_申请时填写的亲密联系人是否出现在订单中:"+result.get("tb_partner_contact"));
    }

    //支付宝账号
    public static void zfb(JSONObject self, JSONObject partner, JSONObject result) {
        result.put("tb_zfb",self.getJSONObject("userinfo").getString("alipay_account")==null?"":self.getJSONObject("userinfo").getString("alipay_account"));
        result.put("tb_partner_zfb",partner.getJSONObject("userinfo").getString("alipay_account")==null?"":self.getJSONObject("userinfo").getString("alipay_account"));
        System.out.println("支付宝账号:"+result.getString("tb_zfb"));
        System.out.println("配偶_支付宝账号:"+result.getString("tb_partner_zfb"));
    }

    //实名认证姓名
    public static void realName(JSONObject self, JSONObject partner, JSONObject result) {
        result.put("tb_real_name",self.getJSONObject("userinfo").getString("real_name")==null?"":self.getJSONObject("userinfo").getString("real_name"));
        result.put("tb_partner_real_name",partner.getJSONObject("userinfo").getString("real_name")==null?"":self.getJSONObject("userinfo").getString("real_name"));
        System.out.println("支付宝账号:"+result.getString("tb_real_name"));
        System.out.println("配偶_支付宝账号:"+result.getString("tb_partner_real_name"));
    }

    //实名认证身份证号
    public static void realId(JSONObject self, JSONObject partner, JSONObject result) {
        result.put("tb_real_id",self.getJSONObject("userinfo").getString("id_card")==null?"":self.getJSONObject("userinfo").getString("id_card"));
        result.put("tb_partner_real_id",partner.getJSONObject("userinfo").getString("id_card")==null?"":self.getJSONObject("userinfo").getString("id_card"));
        System.out.println("实名认证身份证号:"+result.getString("tb_real_id"));
        System.out.println("配偶_实名认证身份证号:"+result.getString("tb_partner_real_id"));
    }

    //实名认证状态
    public static void realStatus(JSONObject self, JSONObject partner, JSONObject result) {
        result.put("tb_real_status",StringUtils.equals(self.getJSONObject("userinfo").getString("account_auth"),"已通过实名认证")?1:0);
        result.put("tb_partner_real_status",StringUtils.equals(partner.getJSONObject("userinfo").getString("account_auth"),"已通过实名认证")?1:0);
        System.out.println("实名认证状态:"+result.getString("tb_real_status"));
        System.out.println("配偶_实名认状态:"+result.getString("tb_partner_real_status"));
    }

    //账户余额
    public static void account(JSONObject self, JSONObject partner, JSONObject result) {
        result.put("tb_account",StringUtils.isBlank(self.getJSONObject("alipaywealth").getString("balance"))?"":self.getJSONObject("alipaywealth").getString("balance"));
        result.put("tb_partner_account",StringUtils.isBlank(partner.getJSONObject("alipaywealth").getString("balance"))?"":partner.getJSONObject("alipaywealth").getString("balance"));
        System.out.println("账户余额:"+result.getString("tb_account"));
        System.out.println("配偶_账户余额:"+result.getString("tb_partner_account"));
    }

    //花呗可用额度
    public static void usable(JSONObject self, JSONObject partner, JSONObject result) {
        result.put("tb_usable_limit_hb",StringUtils.isBlank(self.getJSONObject("alipaywealth").getString("huabei_creditamount"))?"":self.getJSONObject("alipaywealth").getString("huabei_creditamount"));
        result.put("tb_partner_usable_limit_hb",StringUtils.isBlank(partner.getJSONObject("alipaywealth").getString("huabei_creditamount"))?"":partner.getJSONObject("alipaywealth").getString("huabei_creditamount"));
        System.out.println("花呗可用额度:"+result.getString("tb_usable_limit_hb"));
        System.out.println("配偶_花呗可用额度:"+result.getString("tb_partner_usable_limit_hb"));
    }

    //花呗总额度
    public static void total(JSONObject self, JSONObject partner, JSONObject result) {
        result.put("tb_total_limit_hb",StringUtils.isBlank(self.getJSONObject("alipaywealth").getString("huabei_totalcreditamount"))?"":self.getJSONObject("alipaywealth").getString("huabei_totalcreditamount"));
        result.put("tb_partner_total_limit_hb",StringUtils.isBlank(partner.getJSONObject("alipaywealth").getString("huabei_totalcreditamount"))?"":partner.getJSONObject("alipaywealth").getString("huabei_totalcreditamount"));
        System.out.println("花呗总额度:"+result.getString("tb_total_limit_hb"));
        System.out.println("配偶_花呗总额度:"+result.getString("tb_partner_total_limit_hb"));
    }

    //有效订单总次数
    //有效订单总金额
    public static void validOrder(JSONObject self, JSONObject partner, JSONObject result) {

        int num = 0;
        int money = 0;
        JSONArray tradedetails = self.getJSONObject("tradedetails").getJSONArray("tradedetails");
        for (Object tradedetail:tradedetails) {
            if(StringUtils.indexOf(JSON.parseObject(tradedetail.toString()).getString("trade_status"),"成功")<0) continue;
            num += 1;
            money += Integer.parseInt(JSON.parseObject(tradedetail.toString()).getString("actual_fee")==null?"0":JSON.parseObject(tradedetail.toString()).getString("actual_fee"));
        }

        result.put("tb_valid_order_cnt",num);
        result.put("tb_valid_order_amount",money);
        System.out.println("有效订单总次数:"+num);
        System.out.println("有效订单总金额:"+money);

        num = 0;
        money = 0;
        tradedetails = partner.getJSONObject("tradedetails").getJSONArray("tradedetails");
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

    //最近N天有效订单次数
    //最近N天有效订单金额
    //最近N天居住地有效订单次数
    public static void validOrderDay(JSONObject self, JSONObject partner,JSONObject application, JSONObject result,int day){

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

        JSONArray tradedetails = self.getJSONObject("tradedetails").getJSONArray("tradedetails");
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
        result.put("tb_valid_order_cnt_"+day+"d",num);
        result.put("tb_valid_order_amount_"+day+"d",mon);
        result.put("tb_place_valid_order_amount_"+day+"d",addrNum);
        System.out.println("最近"+day+"天有效订单次数:"+num);
        System.out.println("最近"+day+"天有效订单金额:"+mon);
        System.out.println("最近"+day+"天居住地有效订单次数:"+addrNum);


        num = 0;
        addrNum = 0;
        money = 0;
        tradedetails = partner.getJSONObject("tradedetails").getJSONArray("tradedetails");
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

        mon = String.format("%.2f",Integer.valueOf(money).doubleValue()/100);
        result.put("tb_partner_valid_order_cnt_"+day+"d",num);
        result.put("tb_partner_valid_order_amount_"+day+"d",mon);
        result.put("tb_partner_place_valid_order_amount_"+day+"d",addrNum);
        System.out.println("配偶_最近"+day+"天有效订单次数:"+num);
        System.out.println("配偶_最近"+day+"天有效订单金额:"+mon);
        System.out.println("配偶_最近"+day+"天居住地有效订单次数:"+addrNum);
    }

    //每个月的有效订单次数
    //每个月的有效订单金额
    public static void eachValidOrder(JSONObject self, JSONObject partner, JSONObject result) {

        Map<String,Integer> num = new HashMap<>();
        Map<String,String> money = new HashMap<>();
        JSONArray tradedetails = self.getJSONObject("tradedetails").getJSONArray("tradedetails");
        for (Object tradedetail:tradedetails) {
            if(StringUtils.indexOf(JSON.parseObject(tradedetail.toString()).getString("trade_status"),"成功")<0) continue;
            String date = CarrierDateUtil.yearMonth(JSON.parseObject(tradedetail.toString()).getString("trade_createtime"));
            num.put(date,num.get(date)==null?1:num.get(date)+1);
            String je = JSON.parseObject(tradedetail.toString()).getString("actual_fee");
            money.put(date,money.get(date)==null?je:(Integer.parseInt(money.get(date))+Integer.parseInt(je))+"");
        }


        Set entry = money.entrySet();
        Iterator itor = entry.iterator();
        while (itor.hasNext()){
            Map.Entry me = (Map.Entry)itor.next();
            money.put(me.getKey().toString(),Integer.valueOf(me.getValue().toString())/100+"");
        }
        result.put("tb_each_valid_order_cnt",num);
        result.put("tb_each_valid_order_amount",money);
        System.out.println("每个月的有效订单次数："+num);
        System.out.println("每个月的有效订单金额："+money);

        num.clear();
        money.clear();
        tradedetails = partner.getJSONObject("tradedetails").getJSONArray("tradedetails");
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

    //月平均有效订单次数
    //月平均有效订单金额
    public static void avgValidOrder(JSONObject result) {
        Map<String,Integer> cnt = (Map<String,Integer>)result.get("tb_each_valid_order_cnt");
        Map<String,String> amount = (Map<String,String>)result.get("tb_each_valid_order_amount");
        Double count = 0d;
        int num = 0;
        for (String key:cnt.keySet()) {
            num += Integer.parseInt(cnt.get(key).toString());
        }
        for (String key:amount.keySet()) {
            count += Double.parseDouble(amount.get(key).toString());
        }
        result.put("tb_avg_valid_order_cnt",String.format("%.2f",Integer.valueOf(num).doubleValue()/cnt.keySet().size()));
        result.put("tb_avg_valid_order_amount",String.format("%.2f",count/amount.keySet().size()));
        System.out.println("月平均有效订单次数："+result.get("tb_avg_valid_order_cnt"));
        System.out.println("月平均有效订单金额："+result.get("tb_avg_valid_order_amount"));

        cnt = (Map<String,Integer>)result.get("tb_partner_each_valid_order_cnt");
        amount = (Map<String,String>)result.get("tb_partner_each_valid_order_amount");
        count = 0d;
        num = 0;
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

    //大于等于N元的有效订单数量
    public static void upValidOrder(JSONObject self, JSONObject partner, JSONObject result,int money,String selfCode,String partneerCode) {
        int num = 0;
        JSONArray tradedetails = self.getJSONObject("tradedetails").getJSONArray("tradedetails");
        for (Object tradedetail:tradedetails) {
            if(StringUtils.indexOf(JSON.parseObject(tradedetail.toString()).getString("trade_status"),"成功")>=0
                    && Integer.parseInt(JSON.parseObject(tradedetail.toString()).getString("actual_fee"))>=money*100)
                num += 1;
        }

        result.put(selfCode,num);
        System.out.println("大于等于"+money+"元的有效订单数量:"+num);

        num = 0;
        tradedetails = partner.getJSONObject("tradedetails").getJSONArray("tradedetails");
        for (Object tradedetail:tradedetails) {
            if(StringUtils.indexOf(JSON.parseObject(tradedetail.toString()).getString("trade_status"),"成功")>=0
                    && Integer.parseInt(JSON.parseObject(tradedetail.toString()).getString("actual_fee"))>=money*100)
                num += 1;
        }

        result.put(partneerCode,num);
        System.out.println("配偶_大于等于"+money+"元的有效订单数量:"+num);
    }

    //最近N天除本人外的收货号码数
    public static void excSelfPhoneCnt(JSONObject self, JSONObject partner, JSONObject application, JSONObject result, int day, String selfCode, String partnerCode) {

        int num = 0;
        //申请日期
        String applicationDate = application.getString("customerApplyDate");
        //申请日期最近N天
        String beforeDate = CarrierDateUtil.getBeforeOrAfterDate(applicationDate,day);
        //得到yyyy-mm-dd
        String nBefore = CarrierDateUtil.yearMonthDay(beforeDate);

        JSONArray tradedetails = self.getJSONObject("tradedetails").getJSONArray("tradedetails");
        String selftName = self.getJSONObject("userinfo").getString("real_name");
        for (Object tradedetail:tradedetails) {
            if(CarrierDateUtil.dateScope(applicationDate,
                    nBefore,
                    CarrierDateUtil.yearMonthDay(JSON.parseObject(tradedetail.toString()).getString("trade_createtime")),"")
                    && StringUtils.indexOf(JSON.parseObject(tradedetail.toString()).getString("trade_status"),"成功")>=0
                    && !StringUtils.equals(JSON.parseObject(tradedetail.toString()).getString("deliver_name"),selftName))
                num += 1;
        }

        result.put(selfCode,num);
        System.out.println("最近"+day+"天除本人外的收货号码数："+num);

        num = 0;
        tradedetails = partner.getJSONObject("tradedetails").getJSONArray("tradedetails");
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
        System.out.println("最近"+day+"天除本人外的收货号码数："+num);
    }

    //无购物月数
    public static void noShopMonth(JSONObject self, JSONObject partner, JSONObject application, JSONObject result) {

        try {
            //申请日期
            String applicationDate = application.getString("customerApplyDate");
            //申请日期与当前日期之间包含的所有月份
            List<String> list = CarrierDateUtil.getMonthBetween(applicationDate,CarrierDateUtil.getNowDate());
            Set<String> set = new HashSet<>();
            JSONArray tradedetails = self.getJSONObject("tradedetails").getJSONArray("tradedetails");
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

            result.put("tb_no_shop_month",all.size());
            System.out.println("无购物月数："+all.size());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //收件地址总数量
    //收件地址中收件人为本人的城市个数
    //收件地址中是否有申请人户籍所在地
    //收货地址为申请城市的数量
    //收件人为非本人的地址数量
    public static void address(JSONObject self, JSONObject partner, JSONObject application, JSONObject result) {

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
        JSONArray recentdeliveraddress = self.getJSONArray("recentdeliveraddress");
        for (Object recent:recentdeliveraddress) {
            JSONObject jsonObject = JSON.parseObject(recent.toString());
            address.add(jsonObject.get("deliver_address").toString());
            if(StringUtils.equals(selfName,jsonObject.getString("deliver_name")))
                city.add(jsonObject.getString("city"));
            else exeSelf += 1;
            if(!bl && StringUtils.equals(house,jsonObject.getString("deliver_address"))) bl = true;
            if(StringUtils.equals(applyPlaceCity,jsonObject.getString("city"))) applyPlaceCityCount += 1;
        }

        result.put("tb_address",address.size());
        result.put("tb_self_address",city.size());
        result.put("tb_register",bl);
        result.put("tb_apply_city",applyPlaceCityCount);
        result.put("tb_exc_self_address",exeSelf);
        System.out.println("收件地址总数量："+address.size());
        System.out.println("收件地址中收件人为本人的城市个数："+city.size());
        System.out.println("收件地址中是否有申请人户籍所在地："+bl);
        System.out.println("收货地址为申请城市的数量："+applyPlaceCityCount);
        System.out.println("收件人为非本人的地址数量："+exeSelf);


        address.clear();
        city.clear();
        applyPlaceCityCount = 0;
        exeSelf = 0;
        bl = false;
        recentdeliveraddress = partner.getJSONArray("recentdeliveraddress");
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

    //近N天有效订单收件人为本人的地址数量
    //近N天有效订单收件人为配偶的地址数量
    //配偶_近N天有效订单收件人为本人的地址数量
    //配偶_近N天有效订单收件人为申请人的地址数量
    public static void validOrderSelfAddress(JSONObject self, JSONObject partner, JSONObject application, JSONObject result,int day) {


        /*Set<String> set = new HashSet<>();
        JSONArray tradedetails = self.getJSONObject("tradedetails").getJSONArray("tradedetails");
        for (Object tradedetail:tradedetails) {
            if(StringUtils.indexOf(JSON.parseObject(tradedetail.toString()).getString("trade_status"),"成功")<0) continue;
                num += 1;
            if(CarrierDateUtil.compareDate(applicationDate,CarrierDateUtil.getNowDate(),"yyyy-MM-dd")>=0)
        }*/
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

    //现居住地址送货次数
    //配偶_申请人现居住地址送货次数
    public static void placeCnt(JSONObject self, JSONObject partner, JSONObject application, JSONObject result) {
        //申请人现居住地址
        String applicationAddress = application.getString("customerHomeAddress");

        int num = 0;
        JSONArray tradedetails = self.getJSONObject("tradedetails").getJSONArray("tradedetails");
        for (Object tradedetail:tradedetails) {
            if(StringUtils.equals(JSON.parseObject(tradedetail.toString()).getString("deliver_address"),applicationAddress))
                num += 1;
        }

        result.put("tb_place_cnt",num);
        System.out.println("现居住地址送货次数:"+num);

        num = 0;
        tradedetails = partner.getJSONObject("tradedetails").getJSONArray("tradedetails");
        for (Object tradedetail:tradedetails) {
            if(StringUtils.equals(JSON.parseObject(tradedetail.toString()).getString("deliver_address"),applicationAddress))
                num += 1;
        }

        result.put("tb_partner_place_cnt",num);
        System.out.println("配偶_申请人现居住地址送货次数:"+num);
    }

    //现居住地址送货首次使用距离当前天数
    //现居住地址送货最后一次使用距离当前天数
    //配偶_申请人现居住地址送货首次使用距离当前天数
    //配偶_申请人现居住地址送货最后一次使用距离当前天数
    public static void placeInterval(JSONObject self, JSONObject partner, JSONObject application, JSONObject result) {
        //申请人现居住地址
        String applicationAddress = application.getString("customerHomeAddress");

        int days = 0;
        List<String> list = new ArrayList<>();
        JSONArray tradedetails = self.getJSONObject("tradedetails").getJSONArray("tradedetails");
        for (Object tradedetail:tradedetails) {
            if(StringUtils.equals(JSON.parseObject(tradedetail.toString()).getString("deliver_address"),applicationAddress)) {
                list.add(CarrierDateUtil.yearMonthDay(JSON.parseObject(tradedetail.toString()).getString("trade_createtime")).replaceAll("-",""));
            }
        }
        days = 0;
        if(list.size()>0) {
            Collections.sort(list);
            days = CarrierDateUtil.differentDays(CarrierDateUtil.toDate(list.get(0)), new Date());
        }
        result.put("tb_place_first_interval", days);
        System.out.println("现居住地址送货首次使用距离当前天数:" + days);
        days = 0;
        if(list.size()>0) {
            days = CarrierDateUtil.differentDays(CarrierDateUtil.toDate(list.get(list.size() - 1)), new Date());
        }
        result.put("tb_place_last_interval", days);
        System.out.println("现居住地址送货最后一次使用距离当前天数:" + days);
        list.clear();
        tradedetails = partner.getJSONObject("tradedetails").getJSONArray("tradedetails");
        for (Object tradedetail:tradedetails) {
            if(StringUtils.equals(JSON.parseObject(tradedetail.toString()).getString("deliver_address"),applicationAddress)) {
                list.add(CarrierDateUtil.yearMonthDay(JSON.parseObject(tradedetail.toString()).getString("trade_createtime")).replaceAll("-",""));
            }
        }

        days = 0;
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

    //1点-7点下订单的笔数占比
    //配偶_1点-7点下订单的笔数占比
    public static void orderPerHour(JSONObject self, JSONObject partner, JSONObject result) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        int num = 0;
        JSONArray tradedetails = self.getJSONObject("tradedetails").getJSONArray("tradedetails");
        for (Object tradedetail:tradedetails) {
            if(CarrierDateUtil.compareDate("01:00:00",format.format(CarrierDateUtil.toDate(JSON.parseObject(tradedetail.toString()).getString("trade_createtime"))),"HH:mm:ss")<1
            && CarrierDateUtil.compareDate("07:00:00",format.format(CarrierDateUtil.toDate(JSON.parseObject(tradedetail.toString()).getString("trade_createtime"))),"HH:mm:ss")>-1) {
                num += 1;
            }
        }

        result.put("tb_order_per_1_7_hour",String.format("%,2f",Integer.valueOf(num).doubleValue()/tradedetails.size()));
        System.out.println("1点-7点下订单的笔数占比:"+result.get("tb_order_per_1_7_hour"));

        num = 0;
        tradedetails = partner.getJSONObject("tradedetails").getJSONArray("tradedetails");
        for (Object tradedetail:tradedetails) {
            if(CarrierDateUtil.compareDate("01:00:00",format.format(CarrierDateUtil.toDate(JSON.parseObject(tradedetail.toString()).getString("trade_createtime"))),"HH:mm:ss")<1
                    && CarrierDateUtil.compareDate("07:00:00",format.format(CarrierDateUtil.toDate(JSON.parseObject(tradedetail.toString()).getString("trade_createtime"))),"HH:mm:ss")>-1) {
                num += 1;
            }
        }

        result.put("tb_partner_order_per_1_7_hour",String.format("%,2f",Integer.valueOf(num).doubleValue()/tradedetails.size()));
        System.out.println("配偶_1点-7点下订单的笔数占比:"+result.get("tb_partner_order_per_1_7_hour"));
    }

    //账单中出现某字段的订单次数
    //账单中出现某字段的订单金额
    //配偶_账单中出现某字段的订单次数
    //配偶_账单中出现某字段的订单金额
    public static void orderCntAmount(JSONObject self, JSONObject partner, JSONObject result,String[] fields,String str) {

        int num = 0;
        int money = 0;
        JSONArray tradedetails = self.getJSONObject("tradedetails").getJSONArray("tradedetails");
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

        result.put("tb_"+str+"_order_cnt",num);
        System.out.println("账单中出现"+str+"字段的订单次数:"+num);
        result.put("tb_"+str+"_order_amount",money/100);
        System.out.println("账单中出现"+str+"字段的订单金额:"+money/100);

        num = 0;
        money = 0;
        tradedetails = partner.getJSONObject("tradedetails").getJSONArray("tradedetails");
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
