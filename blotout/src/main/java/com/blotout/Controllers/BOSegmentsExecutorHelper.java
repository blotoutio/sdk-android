package com.blotout.Controllers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blotout.analytics.BODataRuleEngine;
import com.blotout.constants.BONetworkConstants;
import com.blotout.utilities.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Blotout on 12,January,2020
 */
public class BOSegmentsExecutorHelper {

    private static final String TAG = "BOSegmentsExecutorHelper";
    private static volatile BOSegmentsExecutorHelper instance;
    private boolean isLowerCaseHandled;
    private boolean isUpperCaseHandled;

    public static BOSegmentsExecutorHelper getInstance() {

        if (instance == null) { //if there is no instance available... create new one
            synchronized (BOSegmentsExecutorHelper.class) {
                if (instance == null) {
                    instance = new BOSegmentsExecutorHelper();
                }
            }
        }
        return instance;
    }

    private BOSegmentsExecutorHelper() {
        resetSettings();
    }

    public boolean isKeyFoundIn(@NonNull String key, HashMap<String, Object> jsonDict) {
        return BODataRuleEngine.isKeyAvailableIn(key, jsonDict);
    }

    public boolean isValueFoundIn(@NonNull String value, HashMap<String, Object> jsonDict) {
        return BODataRuleEngine.isValueAvailableIn(value, jsonDict);
    }

    public int operatorIntForString(@NonNull String operatorStr) {
        int optIntVal = -1;
        if (operatorStr.equals("AND")) {
            optIntVal = 1;
        } else if (operatorStr.equals("OR")) {
            optIntVal = 2;
        }
        return optIntVal;
    }

    public boolean doesKeyConatainsValues(String key, @Nullable List<Object> values, long operator, @NonNull HashMap<String, Object> jsonDict) {
        int operatorInt = (int) operator;
        Object result;
        boolean boolResult = false;
        long lastObject = 0;
        long firstObject = 0;
        try {

            switch (operatorInt) {
                case BONetworkConstants.BO_GREATER_THAN://GREATER_THAN
                    if (values != null && values.size() > 0) {
                        lastObject = (long) values.get(values.size() - 1);
                    }
                    result = BODataRuleEngine.valueForKeyInDictwhereItIsGreaterThan(key, jsonDict, lastObject);
                    boolResult = result != null;
                    break;
                case BONetworkConstants.BO_GREATER_THAN_EQUALTO: //GREATER_THAN_EQUALTO
                    if (values != null && values.size() > 0) {
                        lastObject = (long) values.get(values.size() - 1);
                    }
                    result = BODataRuleEngine.valueForKeyInDictwhereItIsGreaterThanOrEqualTo(key, jsonDict, lastObject);
                    boolResult = result != null;
                    break;
                case BONetworkConstants.BO_LESS_THAN: //LESS_THAN
                    if (values != null && values.size() > 0) {
                        lastObject = (long) values.get(values.size() - 1);
                    }
                    result = BODataRuleEngine.valueForKeyInDictwhereItIsLessThan(key, jsonDict, lastObject);
                    boolResult = result != null;
                    break;
                case BONetworkConstants.BO_LESS_THAN_EQUALTO: //LESS_THAN_EQUALTO
                    if (values != null && values.size() > 0) {
                        lastObject = (long) values.get(values.size() - 1);
                    }
                    result = BODataRuleEngine.valueForKeyInDictwhereItIsLessThanOrEqualTo(key, jsonDict, lastObject);
                    boolResult = result != null;
                    break;
                case BONetworkConstants.BO_EQUAL: //EQUAL
                    if (values != null && values.size() > 0) {
                        lastObject = (long) values.get(values.size() - 1);
                    }
                    result = BODataRuleEngine.valueForKeyInDictwhereItIsEqualTo(key, jsonDict, lastObject);
                    boolResult = result != null;
                    break;
                case BONetworkConstants.BO_NOT_EQUAL: //NOT_EQUAL
                    if (values != null && values.size() > 0) {
                        lastObject = (long) values.get(values.size() - 1);
                    }
                    result = BODataRuleEngine.valueForKeyInDictwhereItIsNotEqualTo(key, jsonDict, lastObject);
                    boolResult = result != null;
                    break;
                case BONetworkConstants.BO_IN_RANGE: //IN_RANGE
                    if (values != null && values.size() > 0) {
                        lastObject = (long) values.get(values.size() - 1);
                        firstObject = (long) values.get(0);
                    }
                    result = BODataRuleEngine.valueForKeyInDictwhereItIsGreaterThanANDLessThan(key, jsonDict, firstObject, lastObject);
                    boolResult = result != null;
                    break;
                case BONetworkConstants.BO_NOT_IN_RANGE: //NOT_IN_RANGE
                    if (values != null && values.size() > 0) {
                        lastObject = (long) values.get(values.size() - 1);
                        firstObject = (long) values.get(0);
                    }
                    result = BODataRuleEngine.valueForKeyInDictwhereItIsGreaterThanANDLessThan(key, jsonDict, firstObject, lastObject);
                    boolResult = result == null;
                    break;
                case BONetworkConstants.BO_IN: //IN
                    boolResult = values.contains(BODataRuleEngine.valueForKeyInDict(key, jsonDict));
                    break;
                case BONetworkConstants.BO_NOT_IN: //NOT_IN
                    boolResult = !values.contains(BODataRuleEngine.valueForKeyInDict(key, jsonDict));
                    break;
                case BONetworkConstants.BO_CONTAIN: //CONTAIN
                    if (values != null && values.size() > 0) {
                        lastObject = (long) values.get(values.size() - 1);
                    }
                    result = BODataRuleEngine.valueForKeyInDictwhereItContains(key, jsonDict, String.valueOf(lastObject));
                    boolResult = result != null;
                    break;
                case BONetworkConstants.BO_NOT_CONTAIN: //NOT_CONTAIN
                    if (values != null && values.size() > 0) {
                        lastObject = (long) values.get(values.size() - 1);
                    }
                    result = BODataRuleEngine.valueForKeyInDictwhereItContains(key, jsonDict, String.valueOf(lastObject));
                    boolResult = result == null;
                    break;
                case BONetworkConstants.BO_ANY: //ANY
                    //Find Difference between In and Any case
                    boolResult = values.contains(BODataRuleEngine.valueForKeyInDict(key, jsonDict));
                    break;
                case BONetworkConstants.BO_ALL: //ALL
                {
                    Object valuesObj = BODataRuleEngine.valueForKeyInDict(key, jsonDict);
                    if (valuesObj instanceof List) {
                        boolResult = values.equals(valuesObj);
                        break;
                    }
                }
                break;
                case BONetworkConstants.BO_NONE: //NONE
                    //Find Difference between No In and NONE case
                    boolResult = !values.contains(BODataRuleEngine.valueForKeyInDict(key, jsonDict));
                    break;
                default:
                    Logger.INSTANCE.e(TAG, "DEFAULT CASE= values = %@ and internal value type=%@ \n--------\n dict value if found = %@ and Type = %@");
                    break;
            }
        } catch (NullPointerException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }

        if (!boolResult && !isLowerCaseHandled) {
            isLowerCaseHandled = true;
            boolResult = this.doesKeyConatainsValues(key.toLowerCase(), values, operator, jsonDict);
        }
        if (!boolResult && !isUpperCaseHandled) {
            isUpperCaseHandled = true;
            boolResult = this.doesKeyConatainsValues(key.toUpperCase(), values, operator, jsonDict);
        }

        return boolResult;
    }

    public boolean doesKeyConatainsValues(String key, @Nullable List<Object> values, long operator, @NonNull HashMap<String, Object> jsonDict, String eventName) {

        int operatorInt = (int) operator;
        Object result;
        boolean boolResult = false;
        long lastObject = 0;
        long firstObject = 0;
        boolean keyValComboNotGood = false;
        boolean jsonHasEventName = false;
        boolean eventNameNotGood = false;

        boolean finalResult = false;

        try {

            String tempEventName = eventName != null ? eventName.replaceAll(" ", "") : null;

            List<HashMap<String, Object>> eventNameDicts = null;
            if (eventName != null && !tempEventName.equals("")) {
                if (isValueFoundIn(eventName, jsonDict)) {
                    jsonHasEventName = true;
                    //NSDictionary *eventNameDict = [BOADataRuleEngine dictContains:eventName fromRootDict:jsonDic];
                    eventNameDicts = BODataRuleEngine.allDictContainsValueFromRootDict(eventName, jsonDict);
                }
            } else {
                jsonHasEventName = true;
                eventNameNotGood = true;
                eventNameDicts = new ArrayList<>();
                eventNameDicts.add(jsonDict);
            }
            for (HashMap<String, Object> jsonDictName : eventNameDicts) {
                String tempKeyName = key != null ? key.replaceAll(" ", "") : "";
                if (key != null && !tempKeyName.equals("") && values != null && values.size() > 0) {
                    switch (operatorInt) {
                        case BONetworkConstants.BO_GREATER_THAN://GREATER_THAN
                            if (values != null && values.size() > 0) {
                                lastObject = (long) values.get(values.size() - 1);
                            }
                            result = BODataRuleEngine.valueForKeyInDictwhereItIsGreaterThan(key, jsonDict, lastObject);
                            boolResult = result != null;
                            break;
                        case BONetworkConstants.BO_GREATER_THAN_EQUALTO: //GREATER_THAN_EQUALTO
                            if (values != null && values.size() > 0) {
                                lastObject = (long) values.get(values.size() - 1);
                            }
                            result = BODataRuleEngine.valueForKeyInDictwhereItIsGreaterThanOrEqualTo(key, jsonDict, lastObject);
                            boolResult = result != null;
                            break;
                        case BONetworkConstants.BO_LESS_THAN: //LESS_THAN
                            if (values != null && values.size() > 0) {
                                lastObject = (long) values.get(values.size() - 1);
                            }
                            result = BODataRuleEngine.valueForKeyInDictwhereItIsLessThan(key, jsonDict, lastObject);
                            boolResult = result != null;
                            break;
                        case BONetworkConstants.BO_LESS_THAN_EQUALTO: //LESS_THAN_EQUALTO
                            if (values != null && values.size() > 0) {
                                lastObject = (long) values.get(values.size() - 1);
                            }
                            result = BODataRuleEngine.valueForKeyInDictwhereItIsLessThanOrEqualTo(key, jsonDict, lastObject);
                            boolResult = result != null;
                            break;
                        case BONetworkConstants.BO_EQUAL: //EQUAL
                            if (values != null && values.size() > 0) {
                                lastObject = (long) values.get(values.size() - 1);
                            }
                            result = BODataRuleEngine.valueForKeyInDictwhereItIsEqualTo(key, jsonDict, lastObject);
                            boolResult = result != null;
                            break;
                        case BONetworkConstants.BO_NOT_EQUAL: //NOT_EQUAL
                            if (values != null && values.size() > 0) {
                                lastObject = (long) values.get(values.size() - 1);
                            }
                            result = BODataRuleEngine.valueForKeyInDictwhereItIsNotEqualTo(key, jsonDict, lastObject);
                            boolResult = result != null;
                            break;
                        case BONetworkConstants.BO_IN_RANGE: //IN_RANGE
                            if (values != null && values.size() > 0) {
                                lastObject = (long) values.get(values.size() - 1);
                                firstObject = (long) values.get(0);
                            }
                            result = BODataRuleEngine.valueForKeyInDictwhereItIsGreaterThanANDLessThan(key, jsonDict, firstObject, lastObject);
                            boolResult = result != null;
                            break;
                        case BONetworkConstants.BO_NOT_IN_RANGE: //NOT_IN_RANGE
                            if (values != null && values.size() > 0) {
                                lastObject = (long) values.get(values.size() - 1);
                                firstObject = (long) values.get(0);
                            }
                            result = BODataRuleEngine.valueForKeyInDictwhereItIsGreaterThanANDLessThan(key, jsonDict, firstObject, lastObject);
                            boolResult = result == null;
                            break;
                        case BONetworkConstants.BO_IN: //IN
                            boolResult = values.contains(BODataRuleEngine.valueForKeyInDict(key, jsonDict));
                            break;
                        case BONetworkConstants.BO_NOT_IN: //NOT_IN
                            boolResult = !values.contains(BODataRuleEngine.valueForKeyInDict(key, jsonDict));
                            break;
                        case BONetworkConstants.BO_CONTAIN: //CONTAIN
                            if (values != null && values.size() > 0) {
                                lastObject = (long) values.get(values.size() - 1);
                            }
                            result = BODataRuleEngine.valueForKeyInDictwhereItContains(key, jsonDict, String.valueOf(lastObject));
                            boolResult = result != null;
                            break;
                        case BONetworkConstants.BO_NOT_CONTAIN: //NOT_CONTAIN
                            if (values != null && values.size() > 0) {
                                lastObject = (long) values.get(values.size() - 1);
                            }
                            result = BODataRuleEngine.valueForKeyInDictwhereItContains(key, jsonDict, String.valueOf(lastObject));
                            boolResult = result == null;
                            break;
                        case BONetworkConstants.BO_ANY: //ANY
                            //Find Difference between In and Any case
                            boolResult = values.contains(BODataRuleEngine.valueForKeyInDict(key, jsonDict));
                            break;
                        case BONetworkConstants.BO_ALL: //ALL
                        {
                            Object valuesObj = BODataRuleEngine.valueForKeyInDict(key, jsonDict);
                            if (valuesObj instanceof List) {
                                boolResult = values.equals(valuesObj);
                                break;
                            }
                        }
                        break;
                        case BONetworkConstants.BO_NONE: //NONE
                            //Find Difference between No In and NONE case
                            boolResult = !values.contains(BODataRuleEngine.valueForKeyInDict(key, jsonDict));
                            break;
                        default:
                            Logger.INSTANCE.e(TAG, "DEFAULT CASE= values = %@ and internal value type=%@ \n--------\n dict value if found = %@ and Type = %@");
                            break;
                    }
                    if (boolResult) {
                        break;
                    }
                } else {
                    boolResult = true;
                    keyValComboNotGood = true;
                    break;
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }

        if (!boolResult && !isLowerCaseHandled) {
            isLowerCaseHandled = true;
            String tempKey = key != null ? key.toLowerCase() : null;
            boolResult = this.doesKeyConatainsValues(tempKey, values, operator, jsonDict, eventName);
        }
        if (!boolResult && !isUpperCaseHandled) {
            isUpperCaseHandled = true;
            String tempKey = key != null ? key.toUpperCase() : null;
            boolResult = this.doesKeyConatainsValues(tempKey, values, operator, jsonDict, eventName);
        }

        //This is ensuring that atleast one required param was present, either eventName or Key value, both present is welcome
        if (!(eventNameNotGood && keyValComboNotGood)) {
            finalResult = boolResult && jsonHasEventName;
        }

        return finalResult;
    }


    public boolean conditionalResultForCondition(@NonNull String condition, @Nullable String values1, @Nullable String values2) {
        boolean result = false;
        if (condition.equals("AND")) {
            result = values1 != null && values2 != null;
        } else if (condition.equals("OR")) {
            result = values1 != null || values2 != null;
        }
        return result;

    }

    public boolean resultsOfBitwiseOperator(@NonNull String bitOperator, boolean result1, boolean result2) {
        boolean result = false;
        if (bitOperator.equals("AND")) {
            result = result1 && result2;
        } else if (bitOperator.equals("OR")) {
            result = result1 || result2;
        }
        return result;
    }

    public void resetSettings() {
        isLowerCaseHandled = false;
        isUpperCaseHandled = false;
    }

}
