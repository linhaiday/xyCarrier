package com.linhai.comm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static javax.swing.UIManager.getInt;

/**
 * Created by linhai on 2018/11/27.
 */
public class CarrierDateUtil {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 读取txt文件的内容
     * @param path 想要读取的文件对象
     * @return 返回文件内容
     */
    public static String readTxt(String path){

        File file;
        StringBuilder result = new StringBuilder();
        try{
            file = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(System.lineSeparator()+s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * 过去N个月的时间点
     * @param month
     * @return
     */
    public static String monthsBefore(int month,String date){


        Calendar c = Calendar.getInstance();
        c.setTime(toDate(date));
        c.add(Calendar.MONTH, -month);
        Date m = c.getTime();

        return format.format(m);
    }

    /**
     * 未来N个月的时间点
     * @param month
     * @return
     */
    public static String monthsAfter(int month,String date){


        Calendar c = Calendar.getInstance();
        c.setTime(toDate(date));
        c.add(Calendar.MONTH, month);
        Date m = c.getTime();

        return format.format(m);
    }

    /**
     * 过去N天的时间点
     * @param days
     * @return
     */
    public static String daysBefore(int days,String date){


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(toDate(date));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        calendar.add(Calendar.DATE, -days);

        return format.format(calendar.getTime());
    }

    /**
     * String型日期转换成Date
     * @param date
     * @return
     */
    public static Date toDate(String date){
        Date d = null;
        try {
            d=format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * 年
     * @param date
     * @return
     */
    public static String year(String date){
        return String.format("%tY",CarrierDateUtil.toDate(date));
    }

    /**
     * 月
     * @param date
     * @return
     */
    public static String month(String date){
        return String.format("%tm",CarrierDateUtil.toDate(date));
    }

    /**
     * YYYY-MM
     * @param date
     * @return
     */
    public static String yearMonth(String date){
        return String.format("%tY",CarrierDateUtil.toDate(date))+"-"+String.format("%tm",CarrierDateUtil.toDate(date));
    }

    /**
     * YYYY-MM-DD
     * @param date
     * @return
     */
    public static String yearMonthDay(String date){
        return String.format("%tY",CarrierDateUtil.toDate(date))+"-"+String.format("%tm",CarrierDateUtil.toDate(date))+"-"+String.format("%td",CarrierDateUtil.toDate(date));
    }

    /**
     * 日期比较大小
     * @param date1
     * @param date2
     */
    public static int compareDate(String date1, String date2,String formatString) {
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        try {
            Date date3 = format.parse(date1);
            Date date4 = format.parse(date2);
            if (date3.getTime() > date4.getTime()) return 1;
            else if (date3.getTime() < date4.getTime()) return -1;
            else return 0;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 字符串日期格式的计算
     * @param smdate
     * @param bdate
     * @return
     * @throws ParseException
     */
    public static int daysBetween(String smdate,String bdate){

        long between_days=0;
        long time1;
        long time2;
        try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(smdate));
            time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            time2 = cal.getTimeInMillis();
            between_days=(time2-time1)/(1000*3600*24);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Integer.parseInt(String.valueOf(between_days));
    }

    public static Map<String, String> sortMapByValue(Map<String, String> oriMap) {
        Map<String, String> sortedMap = new LinkedHashMap<>();
        if (oriMap != null && !oriMap.isEmpty()) {
            List<Map.Entry<String, String>> entryList = new ArrayList<>(oriMap.entrySet());
            Collections.sort(entryList,
                    new Comparator<Map.Entry<String, String>>() {
                        public int compare(Map.Entry<String, String> entry1,
                                           Map.Entry<String, String> entry2) {
                            int value1 = 0, value2 = 0;
                            try {
                                value1 = getInt(entry1.getValue());
                                value2 = getInt(entry2.getValue());
                            } catch (NumberFormatException e) {
                                value1 = 0;
                                value2 = 0;
                            }
                            return value2 - value1;
                        }
                    });
            Iterator<Map.Entry<String, String>> iter = entryList.iterator();
            Map.Entry<String, String> tmpEntry;
            while (iter.hasNext()) {
                tmpEntry = iter.next();
                sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
            }
        }
        return sortedMap;
    }

    //降序排序
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDescending(Map<K, V> map)
    {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>()
        {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2)
            {
                int compare = (o1.getValue()).compareTo(o2.getValue());
                return -compare;
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    //获取当前日期
    public static String getNowDate(){
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(dt);
    }

    /**
     * 判断日期是否在范围内
     * @param maxMonth
     * @param minMonth
     * @param month
     * @return
     */
    public static boolean dateScope(String maxMonth,String minMonth,String month,String day){

        //不再范围内
        if(CarrierDateUtil.compareDate(minMonth+day,month+day,"yyyy-MM-dd")==1 ||
                CarrierDateUtil.compareDate(maxMonth+day,month+day,"yyyy-MM-dd")==-1) return false;

        return true;
    }

    /**
     * 过去N天的时间点
     * @param dateStr
     * @param num
     * @return
     */
    public static String getBeforeOrAfterDate(String dateStr, int num) {

        Date date = toDate(dateStr);
        Calendar calendar = Calendar.getInstance();//获取日历
        calendar.setTime(date);//当date的值是当前时间，则可以不用写这段代码。
        calendar.add(Calendar.DATE, -num);
        Date d = calendar.getTime();//把日历转换为Date
        return format.format(d);
    }

    /**
     *
     * @param minDate <String>
     * @param maxDate <String>
     * @return int
     * @throws ParseException
     */
    public static List<String> getMonthBetween(String minDate, String maxDate) throws ParseException {
        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(sdf.parse(minDate));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(sdf.parse(maxDate));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }

        return result;
    }

    /**
     * date2比date1多的天数
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1,Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2)   //同一年
        {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++)
            {
                if(i%4==0 && i%100!=0 || i%400==0)    //闰年
                {
                    timeDistance += 366;
                }
                else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2-day1) ;
        }
        else    //不同年
        {
            System.out.println("判断day2 - day1 : " + (day2-day1));
            return day2-day1;
        }
    }

    //比较a、b值的大小（m = 1：小于；2：小于等于；0：等于；3：大于；4：大于等于）
    public static int number(int a,int m,int b){

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
                    //Object o = orderDay(obj);
                    jsonArray.set(jsonArray.size(),obj);
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
    public static Object orderDay(Object obj){

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

    public static String[] ListSort(Object[] arr) {
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
