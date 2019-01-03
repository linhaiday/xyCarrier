package com.linhai.taobao;

import com.alibaba.fastjson.JSONObject;
import com.linhai.DataSource.DataSource;
import com.linhai.comm.CarrierDateUtil;

import java.text.SimpleDateFormat;

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
            //现居住地址送货次数	tb_place_cnt	配偶_申请人现居住地址送货次数	tb_partner_place_cnt	int
            TaobaoAlgorithm.placeCnt(self,partner,application,result);
            //现居住地址送货首次使用距离当前天数	tb_place_first_interval	配偶_申请人现居住地址送货首次使用距离当前天数	tb_partner_place_first_interval	int
            //现居住地址送货最后一次使用距离当前天数	tb_place_last_interval	配偶_申请人现居住地址送货最后一次使用距离当前天数	tb_partner_place_last_interval	int
            TaobaoAlgorithm.placeInterval(self,partner,application,result);
            //1点-7点下订单的笔数占比	tb_order_per_1_7_hour	配偶_1点-7点下订单的笔数占比	tb_partner_order_per_1_7_hour	double
            TaobaoAlgorithm.orderPerHour(self,partner,result);
            //账单中出现婴儿字段的订单次数	tb_baby_order_cnt	配偶_账单中出现婴儿字段的订单次数	tb_partner_baby_order_cnt	int	关键词：婴儿尿不湿、婴儿奶粉
            //账单中出现婴儿字段的订单金额	tb_baby_order_amount	配偶_账单中出现婴儿字段的订单金额	tb_partner_baby_order_amount	double
            TaobaoAlgorithm.orderCntAmount(self,partner,result,new String[]{"婴儿尿不湿","婴儿奶粉"},"baby");
            //账单中出现儿童字段的订单次数	tb_child_order_cnt	配偶_账单中出现儿童字段的订单次数	tb_partner_child_order_cnt	int	关键词：童装
            //账单中出现儿童字段的订单金额	tb_child_order_amount	配偶_账单中出现儿童字段的订单金额	tb_partner_child_order_amount	double
            TaobaoAlgorithm.orderCntAmount(self,partner,result,new String[]{"童装"},"child");
            //账单中生活用品类字段的订单次数	tb_daily_order_cnt	配偶_账单中生活用品类字段的订单次数	tb_partner_daily_order_cnt	int	关键词：洗发水、洗衣液、卫生纸、洗洁精
            //账单中生活用品类字段订单金额	tb_daily_order_amount	配偶_账单中生活用品类字段订单金额	tb_partner_daily_order_amount	double
            TaobaoAlgorithm.orderCntAmount(self,partner,result,new String[]{"洗发水","洗衣液","卫生纸","洗洁精"},"daily");
            //近一年内收货地址中是否有和配偶相同的收货地址	tb_same_address_year	配偶_近一年内收货地址中是否有和申请人相同的收货地址	tb_partner_same_address_year	int(否：0，是：1)
            TaobaoAlgorithm.sameAddressYear(self,partner,application,result,365);
            //淘宝收货号码是否含配偶的电话号码	tb_same_phone	配偶_淘宝收货号码是否含申请人的电话号码	tb_partner_same_phone	int(否：0，是：1)
            //淘宝收货姓名是否含配偶的姓名	tb_same_name	配偶_淘宝收货姓名是否含申请人的姓名	tb_partner_same_name	int(否：0，是：1)
            TaobaoAlgorithm.samePhone(self,partner,application,result);
            //只有最近一个月的订单包含配偶，而之前没有	tb_spouse_only_recently	配偶_只有最近一个月的订单包含申请人，而之前没有	tb_partner_spouse_only_recently	int(否：0，是：1)
            TaobaoAlgorithm.spouseOnlyRecently(self,partner,application,result,1);
    }

    //申请人变量清洗
    public static void selfClean(JSONObject self, JSONObject applicant,JSONObject result) {
        TaobaoSelfAlgorithm.zmf(self,result);
        //淘宝认证姓名是否与申请填写时姓名一致	tb_name_consistency	int(否：0，是：1)
        TaobaoSelfAlgorithm.consistencyName(self,applicant,result);
        //申请时填写的亲密联系人是否出现在订单中	tb_contact	int(否：0，是：1)
        TaobaoSelfAlgorithm.contact(self,applicant,result);
        //支付宝账号	tb_zfb	string
        TaobaoSelfAlgorithm.zfb(self,result);
        //实名认证姓名	tb_real_name	string
        TaobaoSelfAlgorithm.realName(self,result);
        //实名认证身份证号	tb_real_id	string
        TaobaoSelfAlgorithm.realId(self,result);
        //实名认证状态	tb_real_status	string
        TaobaoSelfAlgorithm.realStatus(self,result);
        //账户余额	tb_account	double
        TaobaoSelfAlgorithm.account(self,result);
        //花呗可用额度	tb_usable_limit_hb	double
        TaobaoSelfAlgorithm.usable(self,result);
        //花呗总额度	tb_total_limit_hb	double
        TaobaoSelfAlgorithm.total(self,result);
        //余额宝	tb_balance	double

        //有效订单总次数	tb_valid_order_cnt	int
        //有效订单总金额	tb_valid_order_amount	double
        TaobaoSelfAlgorithm.validOrder(self,result);
        //最近7天有效订单次数	tb_valid_order_cnt_7d	int
        //最近7天有效订单金额	tb_valid_order_amount_7d	double
        //最近7天居住地有效订单次数	tb_place_valid_order_amount_7d	int
        TaobaoSelfAlgorithm.validOrderDay(self,applicant,result,7);
        //最近15天有效订单次数	tb_valid_order_cnt_15d	int
        //最近15天有效订单金额	tb_valid_order_amount_15d	double
        //最近15天居住地有效订单次数	tb_place_valid_order_amount_15d     int
        TaobaoSelfAlgorithm.validOrderDay(self,applicant,result,15);
        //最近30天有效订单次数	tb_valid_order_cnt_30d	int
        //最近30天有效订单金额	tb_valid_order_amount_30d	double
        //最近30天居住地有效订单次数	tb_place_valid_order_amount_30d	int
        TaobaoSelfAlgorithm.validOrderDay(self,applicant,result,30);
        //最近60天有效订单次数	tb_valid_order_cnt_60d	int
        //最近60天有效订单金额	tb_valid_order_amount_60d	double
        //最近60天居住地有效订单次数	tb_place_valid_order_amount_60d	int
        TaobaoSelfAlgorithm.validOrderDay(self,applicant,result,60);
        //最近90天有效订单次数	tb_valid_order_cnt_90d	int
        //最近90天有效订单金额	tb_valid_order_amount_90d	double
        //最近90天居住地有效订单次数	tb_place_valid_order_amount_90d	int
        TaobaoSelfAlgorithm.validOrderDay(self,applicant,result,90);
        //每个月的有效订单次数	tb_each_valid_order_cnt	int
        //每个月的有效订单金额	tb_each_valid_order_amount	double
        TaobaoSelfAlgorithm.eachValidOrder(self,result);
        //月平均有效订单次数	tb_avg_valid_order_cnt	double
        //月平均有效订单金额	tb_avg_valid_order_amount	double
        TaobaoSelfAlgorithm.avgValidOrder(result);
        //大于等于500元的有效订单数量	    tb_up_500_valid_order_cnt	int
        TaobaoSelfAlgorithm.upValidOrder(self,result,500,"tb_up_500_valid_order_cnt");
        //大于等于1000元的有效订单数量	tb_up_1000_valid_order_cnt	int
        TaobaoSelfAlgorithm.upValidOrder(self,result,1000,"tb_up_1000_valid_order_cnt");
        //大于等于2000元的有效订单数量	tb_up_2000_valid_order_cnt	int
        TaobaoSelfAlgorithm.upValidOrder(self,result,2000,"tb_up_2000_valid_order_cnt");
        //最近30天除本人外的收货号码数	tb_exc_self_phone_cnt_30d	int
        TaobaoSelfAlgorithm.excSelfPhoneCnt(self,applicant,result,30,"tb_exc_self_phone_cnt_30d");
        //最近60天除本人外的收货号码数	tb_exc_self_phone_cnt_60d	int
        TaobaoSelfAlgorithm.excSelfPhoneCnt(self,applicant,result,60,"tb_exc_self_phone_cnt_60d");
        //最近90天除本人外的收货号码数	tb_exc_self_phone_cnt_90d	int
        TaobaoSelfAlgorithm.excSelfPhoneCnt(self,applicant,result,90,"tb_exc_self_phone_cnt_90d");
        //无购物月数	tb_no_shop_month	int
        TaobaoSelfAlgorithm.noShopMonth(self,applicant,result);
        //收件地址总数量	tb_address	int
        //收件地址中收件人为本人的城市个数	tb_self_address	int
        //收件地址中是否有申请人户籍所在地	tb_register	int(否：0，是：1)
        //收货地址为申请城市的数量	tb_apply_city	int
        //收件人为非本人的地址数量	tb_exc_self_address	int
        TaobaoSelfAlgorithm.address(self,applicant,result);
        //近30天有效订单收件人为本人的地址数量	tb_valid_order_self_address_30d	int
        //近30天有效订单收件人为配偶的地址数量	tb_valid_order_spouse_address_30d	int
        TaobaoSelfAlgorithm.validOrderSelfAddress(self,applicant,result,30);
        //近90天有效订单收件人为本人的地址数量	tb_valid_order_self_address_90d	int
        //近90天有效订单收件人为配偶的地址数量	tb_valid_order_spouse_address_90d	int
        TaobaoSelfAlgorithm.validOrderSelfAddress(self,applicant,result,90);
        //近180天有效订单收件人为本人的地址数量	tb_valid_order_self_address_180d	int
        //近180天有效订单收件人为配偶的地址数量	tb_valid_order_spouse_address_180d	int
        TaobaoSelfAlgorithm.validOrderSelfAddress(self,applicant,result,180);
        //近一年内有效订单收件人为本人的地址数量	tb_valid_order_self_address_year	int
        //近一年内有效订单收件人为配偶的地址数量	tb_valid_order_spouse_address_year	int
        TaobaoSelfAlgorithm.validOrderSelfAddress(self,applicant,result,360);
        //现居住地址送货次数	tb_place_cnt	int
        TaobaoSelfAlgorithm.placeCnt(self,applicant,result);
        //现居住地址送货首次使用距离当前天数	tb_place_first_interval	int
        //现居住地址送货最后一次使用距离当前天数	tb_place_last_interval	int
        TaobaoSelfAlgorithm.placeInterval(self,applicant,result);
        //1点-7点下订单的笔数占比	tb_order_per_1_7_hour	double
        TaobaoSelfAlgorithm.orderPerHour(self,result);
        //账单中出现婴儿字段的订单次数	tb_baby_order_cnt	int	关键词：婴儿尿不湿、婴儿奶粉
        //账单中出现婴儿字段的订单金额	tb_baby_order_amount	double
        TaobaoSelfAlgorithm.orderCntAmount(self,result,new String[]{"婴儿尿不湿","婴儿奶粉"},"baby");
        //账单中出现儿童字段的订单次数	tb_child_order_cnt	int	关键词：童装
        //账单中出现儿童字段的订单金额	tb_child_order_amount	double
        TaobaoSelfAlgorithm.orderCntAmount(self,result,new String[]{"童装"},"child");
        //账单中生活用品类字段的订单次数	tb_daily_order_cnt	int	关键词：洗发水、洗衣液、卫生纸、洗洁精
        //账单中生活用品类字段订单金额	tb_daily_order_amount	double
        TaobaoSelfAlgorithm.orderCntAmount(self,result,new String[]{"洗发水","洗衣液","卫生纸","洗洁精"},"daily");
    }

    //申请人配偶变量清洗
    public static void partnerClean(JSONObject partner, JSONObject applicant, JSONObject result) {
        TaobaoPartnerAlgorithm.zmf(partner,result);
        //配偶_淘宝认证姓名是否与申请填写时姓名一致	tb_partner_name_consistency	int(否：0，是：1)
        TaobaoPartnerAlgorithm.consistencyName(partner,applicant,result);
        //配偶_申请人申请时填写的亲密联系人是否出现在订单中	tb_partner_contact	int(否：0，是：1)
        TaobaoPartnerAlgorithm.contact(partner,applicant,result);
        //配偶_支付宝账号	tb_partner_zfb	string
        TaobaoPartnerAlgorithm.zfb(partner,result);
        //配偶_实名认证姓名	tb_partner_real_name	string
        TaobaoPartnerAlgorithm.realName(partner,result);
        //配偶_实名认证身份证号	tb_partner_real_id	string
        TaobaoPartnerAlgorithm.realId(partner,result);
        //配偶_实名认证状态	tb_partner_real_status	string
        TaobaoPartnerAlgorithm.realStatus(partner,result);
        //配偶_账户余额	tb_partner_account	double
        TaobaoPartnerAlgorithm.account(partner,result);
        //配偶_花呗可用额度	tb_partner_usable_limit_hb	double
        TaobaoPartnerAlgorithm.usable(partner,result);
        //配偶_花呗总额度	tb_partner_total_limit_hb	double
        TaobaoPartnerAlgorithm.total(partner,result);
        //配偶_余额宝	tb_partner_balance	double

        //配偶_有效订单总次数	tb_partner_valid_order_cnt	int
        //配偶_有效订单总金额	tb_partner_valid_order_amount	double
        TaobaoPartnerAlgorithm.validOrder(partner,result);
        //配偶_最近7天有效订单次数	tb_partner_valid_order_cnt_7d	int
        //配偶_最近7天有效订单金额	tb_partner_valid_order_amount_7d	double
        //配偶_最近7天居住地有效订单次数	tb_partner_place_valid_order_amount_7d	int
        TaobaoPartnerAlgorithm.validOrderDay(partner,applicant,result,7);
        //配偶_最近15天有效订单次数	tb_partner_valid_order_cnt_15d	int
        //配偶_最近15天有效订单金额	tb_partner_valid_order_amount_15d	double
        //配偶_最近15天居住地有效订单次数	tb_partner_place_valid_order_amount_15d	int
        TaobaoPartnerAlgorithm.validOrderDay(partner,applicant,result,15);
        //配偶_最近30天有效订单次数	tb_partner_valid_order_cnt_30d	int
        //配偶_最近30天有效订单金额	tb_partner_valid_order_amount_30d	double
        //配偶_最近30天居住地有效订单次数	tb_partner_place_valid_order_amount_30d	int
        TaobaoPartnerAlgorithm.validOrderDay(partner,applicant,result,30);
        //配偶_最近60天有效订单次数	tb_partner_valid_order_cnt_60d	int
        //配偶_最近60天有效订单金额	tb_partner_valid_order_amount_60d	double
        //配偶_最近60天居住地有效订单次数	tb_partner_place_valid_order_amount_60d	int
        TaobaoPartnerAlgorithm.validOrderDay(partner,applicant,result,60);
        //配偶_最近90天有效订单次数	tb_partner_valid_order_cnt_90d	int
        //配偶_最近90天有效订单金额	tb_partner_valid_order_amount_90d	double
        //配偶_最近90天居住地有效订单次数	tb_partner_place_valid_order_amount_90d	int
        TaobaoPartnerAlgorithm.validOrderDay(partner,applicant,result,90);
        //配偶_每个月的有效订单次数	tb_partner_each_valid_order_cnt	int
        //配偶_每个月的有效订单金额	tb_partner_each_valid_order_amount	double
        TaobaoPartnerAlgorithm.eachValidOrder(partner,result);
        //配偶_月平均有效订单次数	tb_partner_avg_valid_order_cnt	double
        //配偶_月平均有效订单金额	tb_partner_avg_valid_order_amount	double
        TaobaoPartnerAlgorithm.avgValidOrder(result);
        //配偶_大于等于500元的有效订单数量	tb_partner_up_500_valid_order_cnt	int
        TaobaoPartnerAlgorithm.upValidOrder(partner,result,500,"tb_partner_up_500_valid_order_cnt");
        //配偶_大于等于1000元的有效订单数量	tb_partner_up_1000_valid_order_cnt	int
        TaobaoPartnerAlgorithm.upValidOrder(partner,result,1000,"tb_partner_up_1000_valid_order_cnt");
        //配偶_大于等于2000元的有效订单数量	tb_partner_up_2000_valid_order_cnt	int
        TaobaoPartnerAlgorithm.upValidOrder(partner,result,2000,"tb_partner_up_2000_valid_order_cnt");
        //配偶_最近30天除本人外的收货号码数	tb_partner_exc_self_phone_cnt_30d	int
        TaobaoPartnerAlgorithm.excSelfPhoneCnt(partner,applicant,result,30,"tb_partner_exc_self_phone_cnt_30d");
        //配偶_最近60天除本人外的收货号码数	tb_partner_exc_self_phone_cnt_60d	int
        TaobaoPartnerAlgorithm.excSelfPhoneCnt(partner,applicant,result,60,"tb_partner_exc_self_phone_cnt_60d");
        //配偶_最近90天除本人外的收货号码数	tb_partner_exc_self_phone_cnt_90d	int
        TaobaoPartnerAlgorithm.excSelfPhoneCnt(partner,applicant,result,90,"tb_partner_exc_self_phone_cnt_90d");
        //配偶_无购物月数	tb_partner_no_shop_month	int
        TaobaoPartnerAlgorithm.noShopMonth(partner,applicant,result);
        //配偶_收件地址总数量	tb_partner_address	int
        //配偶_收件地址中收件人为本人的城市个数	tb_partner_self_address	int
        //配偶_收件地址中是否有申请人户籍所在地	tb_partner_register	int(否：0，是：1)
        //配偶_收货地址为申请城市的数量	tb_partner_apply_city	int
        //配偶_收件人为非本人的地址数量	tb_partner_exc_self_address	int
        TaobaoPartnerAlgorithm.address(partner,applicant,result);
        //配偶_近一个月有效订单收件人为本人的地址数量	tb_partner_valid_order_self_address_30d	int
        //配偶_近一个月有效订单收件人为申请人的地址数量	tb_partner_valid_order_spouse_address_30d	int
        TaobaoPartnerAlgorithm.validOrderSelfAddress(partner,applicant,result,30);
        //配偶_近三个月有效订单收件人为本人的地址数量	tb_partner_valid_order_self_address_90d	int
        //配偶_近三个月有效订单收件人为申请人的地址数量	tb_partner_valid_order_spouse_address_90d	int
        TaobaoPartnerAlgorithm.validOrderSelfAddress(partner,applicant,result,90);
        //配偶_近六个月有效订单收件人为本人的地址数量	tb_partner_valid_order_self_address_180d	int
        //配偶_近六个月有效订单收件人为申请人的地址数量	tb_partner_valid_order_spouse_address_180d	int
        TaobaoPartnerAlgorithm.validOrderSelfAddress(partner,applicant,result,180);
        //配偶_近一年内有效订单收件人为本人的地址数量	tb_partner_valid_order_self_address_year	int
        //配偶_近一年内有效订单收件人为申请人的地址数量	tb_partner_valid_order_spouse_address_year	int
        TaobaoPartnerAlgorithm.validOrderSelfAddress(partner,applicant,result,360);
        //配偶_申请人现居住地址送货次数	tb_partner_place_cnt	int
        TaobaoPartnerAlgorithm.placeCnt(partner,applicant,result);
        //配偶_申请人现居住地址送货首次使用距离当前天数	tb_partner_place_first_interval	int
        //配偶_申请人现居住地址送货最后一次使用距离当前天数	tb_partner_place_last_interval	int
        TaobaoPartnerAlgorithm.placeInterval(partner,applicant,result);
        //配偶_1点-7点下订单的笔数占比	tb_partner_order_per_1_7_hour	double
        TaobaoPartnerAlgorithm.orderPerHour(partner,result);
        //配偶_账单中出现婴儿字段的订单次数	tb_partner_baby_order_cnt	int	关键词：婴儿尿不湿、婴儿奶粉
        //配偶_账单中出现婴儿字段的订单金额	tb_partner_baby_order_amount	double
        TaobaoPartnerAlgorithm.orderCntAmount(partner,result,new String[]{"婴儿尿不湿","婴儿奶粉"},"baby");
        //配偶_账单中出现儿童字段的订单次数	tb_partner_child_order_cnt	int	关键词：童装
        //配偶_账单中出现儿童字段的订单金额	tb_partner_child_order_amount	double
        TaobaoPartnerAlgorithm.orderCntAmount(partner,result,new String[]{"童装"},"child");
        //配偶_账单中生活用品类字段的订单次数	tb_partner_daily_order_cnt	int	关键词：洗发水、洗衣液、卫生纸、洗洁精
        //配偶_账单中生活用品类字段订单金额	tb_partner_daily_order_amount	double
        TaobaoPartnerAlgorithm.orderCntAmount(partner,result,new String[]{"洗发水","洗衣液","卫生纸","洗洁精"},"daily");
    }

    //交叉变量清洗
    public static void crossClean(JSONObject self, JSONObject partner, JSONObject applicant, JSONObject result){
        //近30天有效订单收件人为本人的地址数量	tb_valid_order_self_address_30d	配偶_近一个月有效订单收件人为本人的地址数量	tb_partner_valid_order_self_address_30d	int
        //近30天有效订单收件人为配偶的地址数量	tb_valid_order_spouse_address_30d	配偶_近一个月有效订单收件人为申请人的地址数量	tb_partner_valid_order_spouse_address_30d	int
        TaobaoCrossAlgorithm.validOrderSelfAddress(self,partner,applicant,result,30);
        //近90天有效订单收件人为本人的地址数量	tb_valid_order_self_address_90d	配偶_近三个月有效订单收件人为本人的地址数量	tb_partner_valid_order_self_address_90d	int
        //近90天有效订单收件人为配偶的地址数量	tb_valid_order_spouse_address_90d	配偶_近三个月有效订单收件人为申请人的地址数量	tb_partner_valid_order_spouse_address_90d	int
        TaobaoCrossAlgorithm.validOrderSelfAddress(self,partner,applicant,result,90);
        //近180天有效订单收件人为本人的地址数量	tb_valid_order_self_address_180d	配偶_近六个月有效订单收件人为本人的地址数量	tb_partner_valid_order_self_address_180d	int
        //近180天有效订单收件人为配偶的地址数量	tb_valid_order_spouse_address_180d	配偶_近六个月有效订单收件人为申请人的地址数量	tb_partner_valid_order_spouse_address_180d	int
        TaobaoCrossAlgorithm.validOrderSelfAddress(self,partner,applicant,result,180);
        //近一年内有效订单收件人为本人的地址数量	tb_valid_order_self_address_year	配偶_近一年内有效订单收件人为本人的地址数量	tb_partner_valid_order_self_address_year	int
        //近一年内有效订单收件人为配偶的地址数量	tb_valid_order_spouse_address_year	配偶_近一年内有效订单收件人为申请人的地址数量	tb_partner_valid_order_spouse_address_year	int
        TaobaoAlgorithm.validOrderSelfAddress(self,partner,applicant,result,360);
        //近一年内收货地址中是否有和配偶相同的收货地址	tb_same_address_year	配偶_近一年内收货地址中是否有和申请人相同的收货地址	tb_partner_same_address_year	int(否：0，是：1)
        TaobaoCrossAlgorithm.sameAddressYear(self,partner,applicant,result,365);
        //淘宝收货号码是否含配偶的电话号码	tb_same_phone	配偶_淘宝收货号码是否含申请人的电话号码	tb_partner_same_phone	int(否：0，是：1)
        //淘宝收货姓名是否含配偶的姓名	tb_same_name	配偶_淘宝收货姓名是否含申请人的姓名	tb_partner_same_name	int(否：0，是：1)
        TaobaoCrossAlgorithm.samePhone(self,partner,applicant,result);
        //只有最近一个月的订单包含配偶，而之前没有	tb_spouse_only_recently	配偶_只有最近一个月的订单包含申请人，而之前没有	tb_partner_spouse_only_recently	int(否：0，是：1)
        TaobaoCrossAlgorithm.spouseOnlyRecently(self,partner,applicant,result,1);
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
            if(!self.isEmpty()) selfClean(self.getJSONObject("data"),applicant.getJSONObject("userInfo"),result);
            if(!partner.isEmpty()) partnerClean(partner.getJSONObject("data"),applicant.getJSONObject("userInfo"),result);
            if(!self.isEmpty() && !partner.isEmpty()) crossClean(self.getJSONObject("data"),partner.getJSONObject("data"),applicant.getJSONObject("userInfo"),result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //遍历结果集
        System.out.println(result.toJSONString());
    }
}
