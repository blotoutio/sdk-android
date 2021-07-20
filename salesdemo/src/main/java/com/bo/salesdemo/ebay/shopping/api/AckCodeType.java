// Generated by xsd compiler for android/java
// DO NOT CHANGE!
package com.bo.salesdemo.ebay.shopping.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 
 * AckCodeType - Type declaration to be used by other schema. This code identifies
 * the acknowledgement code types that eBay could use to communicate the status of
 * processing a (request) message to an application. This code would be used as part
 * of a response message that contains an application-level acknowledgement element.
 * 
 */
public enum AckCodeType {

    /**
     * 
   * (out) Request processing succeeded
   * 
     */
    SUCCESS("Success"),
  

    /**
     * 
   * (out) Request processing failed
   * 
     */
    FAILURE("Failure"),
  

    /**
     * 
   * (out) Request processing completed with warning information
   * being included in the response message
   *  
     */
    WARNING("Warning"),
  

    /**
     * 
   * (out) Request processing completed with some failures.
   * See the Errors data to determine which portions of the request failed.
   *  
     */
    PARTIAL_FAILURE("PartialFailure"),
  

    /**
     * 
   * (out) Reserved for internal or future use.
   * 
     */
    CUSTOM_CODE("CustomCode");
  
  
    private final String value;
  
    AckCodeType(String v) {
        value = v;
    }
    
    public String value() {
        return value;
    }
    
    @NonNull
    public static AckCodeType fromValue(@Nullable String v) {
        if (v != null) {
            for (AckCodeType c: AckCodeType.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
        }
        throw new IllegalArgumentException(v);
    }
}