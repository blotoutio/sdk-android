// Generated by xsd compiler for android/java
// DO NOT CHANGE!
package com.bo.salesdemo.ebay.finding.api;

import java.io.Serializable;
import com.leansoft.nano.annotation.*;

/**
 * 
 * Monetary amount.
 * 
 */
public class Amount implements Serializable {

    private static final long serialVersionUID = -1L;

	@Value
	@Order(value=0)
	public double value;	
	
	@Attribute  
	@Order(value=1)
	public String currencyId;	
	
    
}