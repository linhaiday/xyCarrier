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
 * 申请人变量清洗
 */
public class TaobaoSelfAlgorithm {

    //芝麻分
    public static void zmf(JSONObject self, JSONObject result) {
        JSONObject selfUserinfo = self.getJSONObject("userinfo");
        result.put("tb_zmf",selfUserinfo.getString("zm_score")==null?"":selfUserinfo.getString("zm_score"));
        System.out.println("申请人芝麻分："+result.get("tb_zmf"));
    }

    //淘宝认证姓名是否与申请填写时姓名一致    int(否：0，是：1)
    public static void consistencyName(JSONObject self, JSONObject application, JSONObject result) {
        //申请填写的姓名
        String applicantName = application.getString("customerName");
        //申请人姓名
        String selfName = self.getJSONObject("userinfo").getString("real_name");
        result.put("tb_name_consistency", StringUtils.equals(applicantName,selfName)?1:0);
        System.out.println("淘宝认证姓名是否与申请填写时姓名一致："+result.get("tb_name_consistency"));
    }

    //申请时填写的亲密联系人是否出现在订单中	int(否：0，是：1)
    public static void contact(JSONObject self, JSONObject application, JSONObject result) {
        //亲密联系人
        String contacts = application.getString("customerIntimateContactsMobile1")+","
                +application.getString("customerIntimateContactsMobile2")+","
                +application.getString("customerIntimateContactsMobile3");
        //申请人的亲密联系人是否出现
        int selfContacts = 0;
        JSONArray selfTradedetails = self.getJSONObject("tradedetails").getJSONArray("tradedetails");
        //亲密联系人是否出现在订单中
        for (Object tradedetail:selfTradedetails) {
            if(StringUtils.indexOf(contacts,((JSONObject)tradedetail).getString("deliver_mobilephone"))>=0){
                selfContacts = 1;
                break;
            };
        }

        result.put("tb_contact",selfContacts);
        System.out.println("申请时填写的亲密联系人是否出现在订单中:"+result.get("tb_contact"));
    }

    //支付宝账号
    public static void zfb(JSONObject self, JSONObject result) {
        result.put("tb_zfb",self.getJSONObject("userinfo").getString("alipay_account")==null?"":self.getJSONObject("userinfo").getString("alipay_account"));
        System.out.println("支付宝账号:"+result.getString("tb_zfb"));
    }

    //实名认证姓名
    public static void realName(JSONObject self, JSONObject result) {
        result.put("tb_real_name",self.getJSONObject("userinfo").getString("real_name")==null?"":self.getJSONObject("userinfo").getString("real_name"));
        System.out.println("支付宝账号:"+result.getString("tb_real_name"));
    }

    //实名认证身份证号
    public static void realId(JSONObject self, JSONObject result) {
        result.put("tb_real_id",self.getJSONObject("userinfo").getString("id_card")==null?"":self.getJSONObject("userinfo").getString("id_card"));
        System.out.println("实名认证身份证号:"+result.getString("tb_real_id"));
    }

    //实名认证状态
    public static void realStatus(JSONObject self, JSONObject result) {
        result.put("tb_real_status",StringUtils.equals(self.getJSONObject("userinfo").getString("account_auth"),"已通过实名认证")?1:0);
        System.out.println("实名认证状态:"+result.getString("tb_real_status"));
    }

    //账户余额
    public static void account(JSONObject self, JSONObject result) {
        result.put("tb_account",StringUtils.isBlank(self.getJSONObject("alipaywealth").getString("balance"))?"":self.getJSONObject("alipaywealth").getString("balance"));
        System.out.println("账户余额:"+result.getString("tb_account"));
    }

    //花呗可用额度
    public static void usable(JSONObject self, JSONObject result) {
        result.put("tb_usable_limit_hb",StringUtils.isBlank(self.getJSONObject("alipaywealth").getString("huabei_creditamount"))?"":self.getJSONObject("alipaywealth").getString("huabei_creditamount"));
        System.out.println("花呗可用额度:"+result.getString("tb_usable_limit_hb"));
    }

    //花呗总额度
    public static void total(JSONObject self, JSONObject result) {
        result.put("tb_total_limit_hb",StringUtils.isBlank(self.getJSONObject("alipaywealth").getString("huabei_totalcreditamount"))?"":self.getJSONObject("alipaywealth").getString("huabei_totalcreditamount"));
        System.out.println("花呗总额度:"+result.getString("tb_total_limit_hb"));
    }

    //有效订单总次数
    //有效订单总金额
    public static void validOrder(JSONObject self, JSONObject result) {

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
    }

    //最近N天有效订单次数
    //最近N天有效订单金额
    //最近N天居住地有效订单次数
    public static void validOrderDay(JSONObject self,JSONObject application, JSONObject result,int day){

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
    }

    //每个月的有效订单次数
    //每个月的有效订单金额
    public static void eachValidOrder(JSONObject self, JSONObject result) {

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

    }

    //大于等于N元的有效订单数量
    public static void upValidOrder(JSONObject self, JSONObject result,int money,String selfCode) {
        int num = 0;
        JSONArray tradedetails = self.getJSONObject("tradedetails").getJSONArray("tradedetails");
        for (Object tradedetail:tradedetails) {
            if(StringUtils.indexOf(JSON.parseObject(tradedetail.toString()).getString("trade_status"),"成功")>=0
                    && Integer.parseInt(JSON.parseObject(tradedetail.toString()).getString("actual_fee"))>=money*100)
                num += 1;
        }

        result.put(selfCode,num);
        System.out.println("大于等于"+money+"元的有效订单数量:"+num);
    }

    //最近N天除本人外的收货号码数
    public static void excSelfPhoneCnt(JSONObject self, JSONObject application, JSONObject result, int day, String selfCode) {

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
    }

    //无购物月数
    public static void noShopMonth(JSONObject self, JSONObject application, JSONObject result) {

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
    public static void address(JSONObject self, JSONObject application, JSONObject result) {

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
    }

    //近N天有效订单收件人为本人的地址数量
    public static void validOrderSelfAddress(JSONObject self, JSONObject application, JSONObject result,int day) {


        Set<String> selfAddress = new HashSet<>();
        //申请日期
        String applicationDate = application.getString("customerApplyDate");
        //申请日期最近N天
        String beforeDate = CarrierDateUtil.getBeforeOrAfterDate(applicationDate,day);
        //得到yyyy-mm-dd
        String nBefore = CarrierDateUtil.yearMonthDay(beforeDate);
        //申请人姓名
        String selfName = application.getString("customerName");
        JSONArray tradedetails = self.getJSONObject("tradedetails").getJSONArray("tradedetails");
        for (Object tradedetail:tradedetails) {
            if(!CarrierDateUtil.dateScope(applicationDate,
                    nBefore,
                    CarrierDateUtil.yearMonthDay(JSON.parseObject(tradedetail.toString()).getString("trade_createtime")),"")
                    || StringUtils.indexOf(JSON.parseObject(tradedetail.toString()).getString("trade_status"),"成功")<0) continue;
            //本人
            if(StringUtils.equals(JSON.parseObject(tradedetail.toString()).getString("deliver_name"),selfName))
                selfAddress.add(JSON.parseObject(tradedetail.toString()).getString("deliver_address"));
        }

        result.put("tb_valid_order_self_address_"+day+"d",selfAddress.size());
        System.out.println("近"+day+"天有效订单收件人为本人的地址数量"+selfAddress.size());
    }

    //现居住地址送货次数
    public static void placeCnt(JSONObject self, JSONObject application, JSONObject result) {
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
    }

    //现居住地址送货首次使用距离当前天数
    //现居住地址送货最后一次使用距离当前天数
    public static void placeInterval(JSONObject self, JSONObject application, JSONObject result) {
        //申请人现居住地址
        String applicationAddress = application.getString("customerHomeAddress");

        List<String> list = new ArrayList<>();
        JSONArray tradedetails = self.getJSONObject("tradedetails").getJSONArray("tradedetails");
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
        result.put("tb_place_first_interval", days);
        System.out.println("现居住地址送货首次使用距离当前天数:" + days);
        days = 0;
        if(list.size()>0) {
            days = CarrierDateUtil.differentDays(CarrierDateUtil.toDate(list.get(list.size() - 1)), new Date());
        }
        result.put("tb_place_last_interval", days);
        System.out.println("现居住地址送货最后一次使用距离当前天数:" + days);
    }

    //1点-7点下订单的笔数占比
    //配偶_1点-7点下订单的笔数占比
    public static void orderPerHour(JSONObject self, JSONObject result) {
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
    }

    //账单中出现某字段的订单次数
    //账单中出现某字段的订单金额
    //配偶_账单中出现某字段的订单次数
    //配偶_账单中出现某字段的订单金额
    public static void orderCntAmount(JSONObject self, JSONObject result,String[] fields,String str) {

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
    }
}
