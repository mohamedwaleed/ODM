package com.odm.utility;


/**
 * Created by mohamed on 6/11/16.
 */
public class Utility {
    public static String getLocalString(String key){
        String localString = "";
        localString = OdmLocal.getResourceBundle().getString(key);
        return localString;
    }
}
