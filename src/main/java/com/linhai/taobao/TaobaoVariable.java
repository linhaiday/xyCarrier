package com.linhai.taobao;

import com.alibaba.fastjson.JSONObject;
import com.linhai.DataSource.DataSource;

/**
 * Created by linhai on 2018/12/15.
 */
public class TaobaoVariable {

    public static void clean(JSONObject self,JSONObject partner,JSONObject application,JSONObject result){
            TaobaoAlgorithm.zmf(self,partner,result);
            //淘宝认证姓名是否与申请填写时姓名一致	tb_name_consistency	配偶_淘宝认证姓名是否与申请填写时姓名一致	tb_partner_name_consistency	int(否：0，是：1)
            TaobaoAlgorithm.consistencyName(self,partner,application,result);
            //申请时填写的亲密联系人是否出现在订单中	tb_contact	配偶_申请人申请时填写的亲密联系人是否出现在订单中	tb_partner_contact	int(否：0，是：1)
            TaobaoAlgorithm.contact(self,partner,application,result);
            //支付宝账号	tb_zfb	配偶_支付宝账号	tb_partner_zfb	string
            TaobaoAlgorithm.zfb(self,partner,result);
            //实名认证姓名	tb_real_name	配偶_实名认证姓名	tb_partner_real_name	string
            TaobaoAlgorithm.realName(self,partner,result);
            //实名认证身份证号	tb_real_id	配偶_实名认证身份证号	tb_partner_real_id	string
            TaobaoAlgorithm.realId(self,partner,result);
            //实名认证状态	tb_real_status	配偶_实名认证状态	tb_partner_real_status	string
            TaobaoAlgorithm.realStatus(self,partner,result);
            //账户余额	tb_account	配偶_账户余额	tb_partner_account	double
            TaobaoAlgorithm.account(self,partner,result);
            //花呗可用额度	tb_usable_limit_hb	配偶_花呗可用额度	tb_partner_usable_limit_hb	double
            TaobaoAlgorithm.usable(self,partner,result);
            //花呗总额度	tb_total_limit_hb	配偶_花呗总额度	tb_partner_total_limit_hb	double
            TaobaoAlgorithm.total(self,partner,result);
            //余额宝	tb_balance	配偶_余额宝	tb_partner_balance	double

            //有效订单总次数	tb_valid_order_cnt	配偶_有效订单总次数	tb_partner_valid_order_cnt	int
            //有效订单总金额	tb_valid_order_amount	配偶_有效订单总金额	tb_partner_valid_order_amount	double
            TaobaoAlgorithm.validOrder(self,partner,result);
            //最近7天有效订单次数	tb_valid_order_cnt_7d	配偶_最近7天有效订单次数	tb_partner_valid_order_cnt_7d	int
            //最近7天有效订单金额	tb_valid_order_amount_7d	配偶_最近7天有效订单金额	tb_partner_valid_order_amount_7d	double
            //最近7天居住地有效订单次数	tb_place_valid_order_amount_7d	配偶_最近7天居住地有效订单次数	tb_partner_place_valid_order_amount_7d	int
            TaobaoAlgorithm.validOrderDay(self,partner,application,result,7);
            //最近15天有效订单次数	tb_valid_order_cnt_15d	配偶_最近15天有效订单次数	tb_partner_valid_order_cnt_15d	int
            //最近15天有效订单金额	tb_valid_order_amount_15d	配偶_最近15天有效订单金额	tb_partner_valid_order_amount_15d	double
            //最近15天居住地有效订单次数	tb_place_valid_order_amount_15d	配偶_最近15天居住地有效订单次数	tb_partner_place_valid_order_amount_15d	int
            TaobaoAlgorithm.validOrderDay(self,partner,application,result,15);
            //最近30天有效订单次数	tb_valid_order_cnt_30d	配偶_最近30天有效订单次数	tb_partner_valid_order_cnt_30d	int
            //最近30天有效订单金额	tb_valid_order_amount_30d	配偶_最近30天有效订单金额	tb_partner_valid_order_amount_30d	double
            //最近30天居住地有效订单次数	tb_place_valid_order_amount_30d	配偶_最近30天居住地有效订单次数	tb_partner_place_valid_order_amount_30d	int
            TaobaoAlgorithm.validOrderDay(self,partner,application,result,30);
            //最近60天有效订单次数	tb_valid_order_cnt_60d	配偶_最近60天有效订单次数	tb_partner_valid_order_cnt_60d	int
            //最近60天有效订单金额	tb_valid_order_amount_60d	配偶_最近60天有效订单金额	tb_partner_valid_order_amount_60d	double
            //最近60天居住地有效订单次数	tb_place_valid_order_amount_60d	配偶_最近60天居住地有效订单次数	tb_partner_place_valid_order_amount_60d	int
            TaobaoAlgorithm.validOrderDay(self,partner,application,result,60);
            //最近90天有效订单次数	tb_valid_order_cnt_90d	配偶_最近90天有效订单次数	tb_partner_valid_order_cnt_90d	int
            //最近90天有效订单金额	tb_valid_order_amount_90d	配偶_最近90天有效订单金额	tb_partner_valid_order_amount_90d	double
            //最近90天居住地有效订单次数	tb_place_valid_order_amount_90d	配偶_最近90天居住地有效订单次数	tb_partner_place_valid_order_amount_90d	int
            TaobaoAlgorithm.validOrderDay(self,partner,application,result,90);
            //每个月的有效订单次数	tb_each_valid_order_cnt	配偶_每个月的有效订单次数	tb_partner_each_valid_order_cnt	int
            //每个月的有效订单金额	tb_each_valid_order_amount	配偶_每个月的有效订单金额	tb_partner_each_valid_order_amount	double
            TaobaoAlgorithm.eachValidOrder(self,partner,result);
            //月平均有效订单次数	tb_avg_valid_order_cnt	配偶_月平均有效订单次数	tb_partner_avg_valid_order_cnt	double
            //月平均有效订单金额	tb_avg_valid_order_amount	配偶_月平均有效订单金额	tb_partner_avg_valid_order_amount	double
            TaobaoAlgorithm.avgValidOrder(result);
            //大于等于500元的有效订单数量	    tb_up_500_valid_order_cnt	配偶_大于等于500元的有效订单数量	tb_partner_up_500_valid_order_cnt	int
            TaobaoAlgorithm.upValidOrder(self,partner,result,500,"tb_up_500_valid_order_cnt","tb_partner_up_500_valid_order_cnt");
            //大于等于1000元的有效订单数量	tb_up_1000_valid_order_cnt	配偶_大于等于1000元的有效订单数量	tb_partner_up_1000_valid_order_cnt	int
            TaobaoAlgorithm.upValidOrder(self,partner,result,1000,"tb_up_1000_valid_order_cnt","tb_partner_up_1000_valid_order_cnt");
            //大于等于2000元的有效订单数量	tb_up_2000_valid_order_cnt	配偶_大于等于2000元的有效订单数量	tb_partner_up_2000_valid_order_cnt	int
            TaobaoAlgorithm.upValidOrder(self,partner,result,2000,"tb_up_2000_valid_order_cnt","tb_partner_up_2000_valid_order_cnt");
            //最近30天除本人外的收货号码数	tb_exc_self_phone_cnt_30d	配偶_最近30天除本人外的收货号码数	tb_partner_exc_self_phone_cnt_30d	int
            TaobaoAlgorithm.excSelfPhoneCnt(self,partner,application,result,30,"tb_exc_self_phone_cnt_30d","tb_partner_exc_self_phone_cnt_30d");
            //最近60天除本人外的收货号码数	tb_exc_self_phone_cnt_60d	配偶_最近60天除本人外的收货号码数	tb_partner_exc_self_phone_cnt_60d	int
            TaobaoAlgorithm.excSelfPhoneCnt(self,partner,application,result,60,"tb_exc_self_phone_cnt_60d","tb_partner_exc_self_phone_cnt_60d");
            //最近90天除本人外的收货号码数	tb_exc_self_phone_cnt_90d	配偶_最近90天除本人外的收货号码数	tb_partner_exc_self_phone_cnt_90d	int
            TaobaoAlgorithm.excSelfPhoneCnt(self,partner,application,result,90,"tb_exc_self_phone_cnt_90d","tb_partner_exc_self_phone_cnt_90d");
            //无购物月数	tb_no_shop_month	配偶_无购物月数	tb_partner_no_shop_month	int
            TaobaoAlgorithm.noShopMonth(self,partner,application,result);
            //收件地址总数量	tb_address	配偶_收件地址总数量	tb_partner_address	int
            //收件地址中收件人为本人的城市个数	tb_self_address	配偶_收件地址中收件人为本人的城市个数	tb_partner_self_address	int
            //收件地址中是否有申请人户籍所在地	tb_register	配偶_收件地址中是否有申请人户籍所在地	tb_partner_register	int(否：0，是：1)
            //收货地址为申请城市的数量	tb_apply_city	配偶_收货地址为申请城市的数量	tb_partner_apply_city	int
            //收件人为非本人的地址数量	tb_exc_self_address	配偶_收件人为非本人的地址数量	tb_partner_exc_self_address	int
            TaobaoAlgorithm.address(self,partner,application,result);
            //近30天有效订单收件人为本人的地址数量	tb_valid_order_self_address_30d	配偶_近一个月有效订单收件人为本人的地址数量	tb_partner_valid_order_self_address_30d	int
            //近30天有效订单收件人为配偶的地址数量	tb_valid_order_spouse_address_30d	配偶_近一个月有效订单收件人为申请人的地址数量	tb_partner_valid_order_spouse_address_30d	int
            TaobaoAlgorithm.validOrderSelfAddress(self,partner,application,result,30);
            //近90天有效订单收件人为本人的地址数量	tb_valid_order_self_address_90d	配偶_近三个月有效订单收件人为本人的地址数量	tb_partner_valid_order_self_address_90d	int
            //近90天有效订单收件人为配偶的地址数量	tb_valid_order_spouse_address_90d	配偶_近三个月有效订单收件人为申请人的地址数量	tb_partner_valid_order_spouse_address_90d	int
            TaobaoAlgorithm.validOrderSelfAddress(self,partner,application,result,90);
            //近180天有效订单收件人为本人的地址数量	tb_valid_order_self_address_180d	配偶_近六个月有效订单收件人为本人的地址数量	tb_partner_valid_order_self_address_180d	int
            //近180天有效订单收件人为配偶的地址数量	tb_valid_order_spouse_address_180d	配偶_近六个月有效订单收件人为申请人的地址数量	tb_partner_valid_order_spouse_address_180d	int
            TaobaoAlgorithm.validOrderSelfAddress(self,partner,application,result,180);
            //近一年内有效订单收件人为本人的地址数量	tb_valid_order_self_address_year	配偶_近一年内有效订单收件人为本人的地址数量	tb_partner_valid_order_self_address_year	int
            //近一年内有效订单收件人为配偶的地址数量	tb_valid_order_spouse_address_year	配偶_近一年内有效订单收件人为申请人的地址数量	tb_partner_valid_order_spouse_address_year	int
            TaobaoAlgorithm.validOrderSelfAddress(self,partner,application,result,360);


    }


    public static void main(String[] args) {

        JSONObject result = new JSONObject();
        //封装自己的数据源
        JSONObject self = JSONObject.parseObject(DataSource.taoBaoSelf());
        //封装配偶的数据源
        JSONObject partner = JSONObject.parseObject(DataSource.taoBaoPartner());
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
