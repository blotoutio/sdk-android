package com.blotout.analytics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blotout.constants.BOCommonConstants;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.Logger;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Blotout on 30,November,2019
 */
public class BODataRuleEngine {

    private static final String TAG = BOCommonConstants.TAG_PREFIX + "BODataRuleEngine";

    public static boolean doesObjectContains(Object object, String str){
        boolean isValidValue = false;
        try {
            if (object instanceof String) {
                isValidValue = ((String) ((String) object).toLowerCase()).contains(str);
            }
            if (object instanceof List) {
                isValidValue = ((List) object).contains(str);
            }
            if (object instanceof HashMap) {
                for (Object obj : ((HashMap) object).values()) {
                    isValidValue = doesObjectContains(obj, str);
                    if (isValidValue) {
                        break;
                    }
                }
            }
        }catch(Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }
        return isValidValue;
    }

public static boolean isKeyAvailableIn(@NonNull String key, HashMap<String,Object> jsonDict) {
    String jsonStr = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
    return BOJSONQueryEngine.isKeyAvailableInJSON(key,jsonStr);
}

    public static boolean isValueAvailableIn(@NonNull String value, HashMap<String,Object> jsonDict) {
        String jsonStr = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
        return BOJSONQueryEngine.isValueAvailableInJSON(value,jsonStr);
    }

    @Nullable
    public static HashMap<String,Object> dictContainsKeyFromRootDict(String key, HashMap<String,Object> jsonDict){
        return BOJSONQueryEngine.dictContainsKeyFromRootDict(key,jsonDict);
    }

    public static List<HashMap<String,Object>> allDictContainsValueFromRootDict(Object value, HashMap<String,Object> jsonDict){
        return BOJSONQueryEngine.allDictContainsValueFromRootDict(value,jsonDict);
    }

    @Nullable
    public static HashMap<String,Object> dictContainsValueFromRootDict(String value, HashMap<String,Object> jsonDict){

        return null;
    }

    @Nullable
    public static Object dictContains(Object value, HashMap<String,Object> jsonDict){

        return null;
    }

    @Nullable
    public static Object valueForKeyInDict(String key, HashMap<String,Object> jsonDict){
        Object valueForKey = BOJSONQueryEngine.valueForKeyInNestedDict(key,jsonDict);
        return valueForKey;
    }

    @Nullable
    public static Object valueForKeyInDictwhereItContains(String key, HashMap<String,Object> jsonDict, String contain){
      try {
        Object valueForKey = BOJSONQueryEngine.valueForKeyInNestedDict(key,jsonDict);
        boolean isContained = BODataRuleEngine.doesObjectContains(valueForKey,contain);
        return isContained ? valueForKey : null;
      }catch(Exception e) {
          Logger.INSTANCE.e(TAG,e.toString());
      }
      return null;
    }

    @Nullable
    public static Object valueForKeyInDictwhereItIsGreaterThan(String key, @NonNull HashMap<String,Object> jsonDict, long conditionalvalue){

        try {
            Object valueForKey = BOJSONQueryEngine.valueForKeyInNestedDict(key,jsonDict);
            boolean isValidValue = false;
            if (valueForKey != null && valueForKey instanceof Long) {
                isValidValue = ((Long) valueForKey).doubleValue() > conditionalvalue;
            }
            return isValidValue ? valueForKey : null;
        }catch(Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }

        return null;
    }

    @Nullable
    public static Object valueForKeyInDictwhereItIsGreaterThanOrEqualTo(String key, @NonNull HashMap<String,Object> jsonDict, long conditionalvalue){
        Object valueForKey = BOJSONQueryEngine.valueForKeyInNestedDict(key,jsonDict);
        boolean isValidValue = false;
        if (valueForKey != null && valueForKey instanceof Long) {
            isValidValue = ((Long) valueForKey).doubleValue() >= conditionalvalue;
        }
        return isValidValue ? valueForKey : null;
    }

    @Nullable
    public static Object valueForKeyInDictwhereItIsLessThan(String key, @NonNull HashMap<String,Object> jsonDict, long conditionalvalue){

        Object valueForKey = BOJSONQueryEngine.valueForKeyInNestedDict(key,jsonDict);
        boolean isValidValue = false;
        if (valueForKey != null && valueForKey instanceof Long) {
            isValidValue = ((Long) valueForKey).doubleValue() < conditionalvalue;
        }
        return isValidValue ? valueForKey : null;
    }

    @Nullable
    public static Object valueForKeyInDictwhereItIsLessThanOrEqualTo(String key, @NonNull HashMap<String,Object> jsonDict, long conditionalvalue){

        Object valueForKey = BOJSONQueryEngine.valueForKeyInNestedDict(key,jsonDict);
        boolean isValidValue = false;
        if (valueForKey != null && valueForKey instanceof Long) {
            isValidValue = ((Long) valueForKey).doubleValue() <= conditionalvalue;
        }
        return isValidValue ? valueForKey : null;
    }

    @Nullable
    public static Object valueForKeyInDictwhereItIsEqualTo(String key, @NonNull HashMap<String,Object> jsonDict, long conditionalvalue){
        Object valueForKey = BOJSONQueryEngine.valueForKeyInNestedDict(key,jsonDict);
        boolean isValidValue = false;
        if (valueForKey != null && valueForKey instanceof Long) {
            isValidValue = ((Long) valueForKey).doubleValue() == conditionalvalue;
        }
        return isValidValue ? valueForKey : null;

    }

    @Nullable
    public static Object valueForKeyInDictwhereItIsNotEqualTo(String key, @NonNull HashMap<String,Object> jsonDict, long conditionalvalue){
        Object valueForKey = BOJSONQueryEngine.valueForKeyInNestedDict(key,jsonDict);
        boolean isValidValue = false;
        if (valueForKey != null && valueForKey instanceof Long) {
            isValidValue = ((Long) valueForKey).doubleValue() != conditionalvalue;
        }
        return isValidValue ? valueForKey : null;
    }

    @Nullable
    public static Object valueForKeyInDictwhereItIsGreaterThanANDLessThan(String key, @NonNull HashMap<String,Object> jsonDict, long value1, long value2){

        Object valueForKey = BOJSONQueryEngine.valueForKeyInNestedDict(key,jsonDict);
        boolean isValidValue = false;
        if (valueForKey != null && valueForKey instanceof Long) {
            double doubleValue = ((Long) valueForKey).doubleValue();
            if ((doubleValue > value1) && (doubleValue < value2)) {
                isValidValue = true;
            }
        }
        return isValidValue ? valueForKey : null;
    }

    @Nullable
    public static Object valueForKeyInDictwhereItIsGreaterThanEqualToANDLessThan(String key, @NonNull HashMap<String,Object> jsonDict, long value1, long value2){

        Object valueForKey = BOJSONQueryEngine.valueForKeyInNestedDict(key,jsonDict);
        boolean isValidValue = false;
        if (valueForKey != null && valueForKey instanceof Long) {
            double doubleValue = ((Long) valueForKey).doubleValue();
            if ((doubleValue >= value1) && (doubleValue < value2)) {
                isValidValue = true;
            }
        }
        return isValidValue ? valueForKey : null;
    }

    @Nullable
    public static Object valueForKeyInDictwhereItIsGreaterThanANDLessThanEqualTo(String key, @NonNull HashMap<String,Object> jsonDict, long value1, long value2){

        Object valueForKey = BOJSONQueryEngine.valueForKeyInNestedDict(key,jsonDict);
        boolean isValidValue = false;
        if (valueForKey != null && valueForKey instanceof Long) {
            double doubleValue = ((Long) valueForKey).doubleValue();
            if ((doubleValue > value1) && (doubleValue <= value2)) {
                isValidValue = true;
            }
        }
        return isValidValue ? valueForKey : null;
    }

    @Nullable
    public static Object valueForKeyInDictwhereItIsGreaterThanEqualToANDLessThanEqualTo(String key, @NonNull HashMap<String,Object> jsonDict, long value1, long value2){
        Object valueForKey = BOJSONQueryEngine.valueForKeyInNestedDict(key,jsonDict);
        boolean isValidValue = false;
        if (valueForKey != null && valueForKey instanceof Long) {
            double doubleValue = ((Long) valueForKey).doubleValue();
            if ((doubleValue >= value1) && (doubleValue <= value2)) {
                isValidValue = true;
            }
        }
        return isValidValue ? valueForKey : null;
    }

    @Nullable
    public static Object valueForKeyInDictwhereItIsGreaterThanORLessThan(String key, @NonNull HashMap<String,Object> jsonDict, long value1, long value2){
        Object valueForKey = BOJSONQueryEngine.valueForKeyInNestedDict(key,jsonDict);
        boolean isValidValue = false;
        if (valueForKey != null && valueForKey instanceof Long) {
            double doubleValue = ((Long) valueForKey).doubleValue();
            if ((doubleValue > value1) || (doubleValue < value2)) {
                isValidValue = true;
            }
        }
        return isValidValue ? valueForKey : null;
    }

    @Nullable
    public static Object valueForKeyInDictwhereItIsGreaterThanEqualToORLessThan(String key, @NonNull HashMap<String,Object> jsonDict, long value1, long value2){
        Object valueForKey = BOJSONQueryEngine.valueForKeyInNestedDict(key,jsonDict);
        boolean isValidValue = false;
        if (valueForKey != null && valueForKey instanceof Long) {
            double doubleValue = ((Long) valueForKey).doubleValue();
            if ((doubleValue >= value1) || (doubleValue < value2)) {
                isValidValue = true;
            }
        }
        return isValidValue ? valueForKey : null;
    }

    @Nullable
    public static Object valueForKeyInDictwhereItIsGreaterThanORLessThanEqualTo(String key, @NonNull HashMap<String,Object> jsonDict, long value1, long value2){

        Object valueForKey = BOJSONQueryEngine.valueForKeyInNestedDict(key,jsonDict);
        boolean isValidValue = false;
        if (valueForKey != null && valueForKey instanceof Long) {
            double doubleValue = ((Long) valueForKey).doubleValue();
            if ((doubleValue > value1) || (doubleValue <= value2)) {
                isValidValue = true;
            }
        }
        return isValidValue ? valueForKey : null;
    }

    @Nullable
    public static Object valueForKeyInDictwhereItIsGreaterThanEqualToORLessThanEqualTo(String key, @NonNull HashMap<String,Object> jsonDict, long value1, long value2){

        Object valueForKey = BOJSONQueryEngine.valueForKeyInNestedDict(key,jsonDict);
        boolean isValidValue = false;
        if (valueForKey != null && valueForKey instanceof Long) {
            double doubleValue = ((Long) valueForKey).doubleValue();
            if ((doubleValue >= value1) || (doubleValue <= value2)) {
                isValidValue = true;
            }
        }
        return isValidValue ? valueForKey : null;
    }

}
