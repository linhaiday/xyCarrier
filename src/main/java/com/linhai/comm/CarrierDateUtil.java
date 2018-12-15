package com.linhai.comm;

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
}
