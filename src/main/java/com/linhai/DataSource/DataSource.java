package com.linhai.DataSource;

import com.linhai.comm.Util;

import java.io.File;

/**
 * Created by linhai on 2018/11/25.
 */
public class DataSource {

    public static String dataSource(){
        return Util.readTxt(System.getProperty("user.dir")+"/file/datasource.txt");
    }

    //报表数据源
    public static String dataSourceForReport(){
        return Util.readTxt(System.getProperty("user.dir")+"/file/report.txt");
    }

    //淘宝自己的数据
    public static String taoBaoSelf(){
        return Util.readTxt(System.getProperty("user.dir")+"/file/taoBaoSelf.txt");
    }

    //淘宝配偶的数据
    public static String taoBaoPartner(){
        return Util.readTxt(System.getProperty("user.dir")+"/file/taoBaoPartner.txt");
    }

    //申请人信息（包含申请地、户籍等）
    public static String applicant(){
        return Util.readTxt(System.getProperty("user.dir")+"/file/applyInfo.txt");
    }
}
