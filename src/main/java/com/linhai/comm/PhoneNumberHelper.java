package com.linhai.comm;

import org.apache.commons.lang3.StringUtils;

/**
 * @author very
 */
public final class PhoneNumberHelper {

    public static boolean isMobilePhone(String phoneNumber){
        if(StringUtils.isBlank(phoneNumber)){
            return false;
        }
        if(StringUtils.length(phoneNumber) == 11){
            String first = StringUtils.substring(phoneNumber, 0, 1);
            return first.equals("1");
        }
        if(StringUtils.length(phoneNumber) == 13){
            String third = StringUtils.substring(phoneNumber, 2, 3);
            return third.equals("1");
        }
        return false;
    }
}
