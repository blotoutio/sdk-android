package com.blotout.analytics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blotout.constants.BOCommonConstants;
import com.blotout.utilities.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Blotout on 30,November,2019
 */
public class BOJSONQueryEngine {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOJSONQueryEngine";

    public static boolean isKeyAvailableInJSON(@NonNull String key, @NonNull String jsonStr){
        try {
            if(jsonStr == null) {
                return false;
            } else if(jsonStr.equals("")) {
                return false;
            } else {
                return jsonStr.toLowerCase().contains(key.toLowerCase());
            }
        }catch(Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }
        return false;
    }

    public static boolean isValueAvailableInJSON(@NonNull String value, @NonNull String jsonStr){
        try {
        if(jsonStr == null) {
            return false;
        } else if(jsonStr.equals("")) {
            return false;
        } else {
            return jsonStr.contains(value);
        }
        }catch(Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }
        return false;
    }

    @Nullable
    public static Object valueForKeyInNestedDict(String key, @NonNull HashMap<String,Object> jsonDict){
        Object valueForKey = null;
    try {
        List<String> allKeys = new ArrayList<>(jsonDict.keySet());
        if (allKeys.contains(key)) {
            valueForKey = jsonDict.get(key);
            Logger.INSTANCE.v("found Key and Object 105 %@",jsonDict.toString());
        }else{
            for (String singleKey : allKeys) {
                if (valueForKey != null) {
                    Logger.INSTANCE.v("found Key and Object 109 %@",jsonDict.toString());
                    break;
                }

                Object objectForSingleKey = jsonDict.get(singleKey);
                if (objectForSingleKey instanceof HashMap ) {
                    valueForKey = valueForKeyInNestedDict(key, (HashMap<String, Object>) objectForSingleKey);
                    if (valueForKey != null) {
                        //NSLog(@"found Key and Object 116 singleKey %@ object %@",singleKey, jsonDict);
                        break;
                    }
                }else if(objectForSingleKey instanceof ArrayList){
                    for (Object arraySingleObj : (ArrayList)objectForSingleKey) {
                        if (arraySingleObj instanceof HashMap) {
                            valueForKey = valueForKeyInNestedDict(key, (HashMap<String, Object>) arraySingleObj);
                            if (valueForKey != null) {
                                //NSLog(@"found Key and Object 124 arrarSingleObj %@ object %@",arraySingleObj, jsonDict);
                                break;
                            }
                        }else{
                            break;
                        }
                    }
                }
            }
        }
        return valueForKey;
    }catch(Exception e) {
        Logger.INSTANCE.e(TAG,e.toString());
    }
        return valueForKey;
    }

    public  static HashMap<String,Object> dictContainsKeyFromRootDict(String key, @NonNull HashMap<String,Object> jsonDict) {

        if(jsonDict == null) {
            return null;
        }

        HashMap<String,Object> dictContainsKey = null;
        try {
            Set<String> allKeys = jsonDict.keySet();
            if (allKeys.contains(key)) {
                dictContainsKey = new HashMap<>(jsonDict);
            }else{
                for (String singleKey : allKeys) {
                    if (dictContainsKey !=null) {
                        break;
                    }
                    Object objectForSingleKey = jsonDict.get(singleKey);
                    if (objectForSingleKey instanceof HashMap) {
                        dictContainsKey =  dictContainsKeyFromRootDict(key, (HashMap<String, Object>) objectForSingleKey);
                        if (dictContainsKey != null) {
                            break;
                        }
                    }else if(objectForSingleKey instanceof List){
                        for (Object arraySingleObj : (List)objectForSingleKey) {
                            if (arraySingleObj instanceof HashMap) {
                                dictContainsKey = dictContainsKeyFromRootDict(key, (HashMap<String, Object>) arraySingleObj);
                                if (dictContainsKey != null) {

                                    break;
                                }
                            }else{
                                break;
                            }
                        }
                    }
                }
            }
            return dictContainsKey;
        }catch(Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }
        return dictContainsKey;
    }

    public  static List<HashMap<String,Object>> allDictContainsValueFromRootDict(Object value, @NonNull HashMap<String,Object> jsonDict) {
        List<HashMap<String,Object>> allDictContaingValue = new ArrayList<>();

        List<String> allKeys = new ArrayList<>(jsonDict.keySet());
        List<Object> allValues = new ArrayList<>(jsonDict.values());
        if (allValues.contains(value)) {
            for (Object oneValue : allValues) {
                if (isValueEqualToObject(value,oneValue)) {
                    HashMap<String,Object> dictContainsValue = new HashMap<>(jsonDict);
                    allDictContaingValue.add(dictContainsValue);
                    break;
                }
            }
        }else{
            for (String singleKey : allKeys) {
                Object objectForSingleKey = jsonDict.get(singleKey);
                if (objectForSingleKey instanceof  HashMap) {
                    List<HashMap<String,Object>> dictContaingVal1 = allDictContainsValueFromRootDict(value, (HashMap<String, Object>) objectForSingleKey);
                     allDictContaingValue.addAll(dictContaingVal1);
                }else if(objectForSingleKey instanceof List){
                    for (Object arraySingleObj : (List<HashMap<String,Object>>)objectForSingleKey) {
                        if (arraySingleObj instanceof HashMap) {
                            List<HashMap<String,Object>> dictContaingVal2 = allDictContainsValueFromRootDict(value, (HashMap<String, Object>) arraySingleObj);
                            allDictContaingValue.addAll(dictContaingVal2);
                        }else{
                            //possibly not needed as first if condition and loop will solve the purpose
                            if (isValueEqualToObject(value,arraySingleObj)) {
                                HashMap<String,Object> dictContainsVal = new HashMap<>(jsonDict);
                                 allDictContaingValue.add(dictContainsVal);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return allDictContaingValue;
    }

    public static boolean isValueEqualToObject(Object value, Object object){
        boolean isEqual = false;
        if(value != null && object != null) {
            isEqual = value.equals(object);
        }
        return isEqual;
    }
}
