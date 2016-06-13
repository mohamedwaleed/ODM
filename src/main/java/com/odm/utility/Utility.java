package com.odm.utility;


import java.io.UnsupportedEncodingException;

/**
 * Created by mohamed on 6/11/16.
 */
public class Utility {
    public static String getLocalString(String key){
        String localString = "";
        try {
            localString = new String(OdmLocal.getResourceBundle().getString(key).getBytes("ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return localString;
    }
}
