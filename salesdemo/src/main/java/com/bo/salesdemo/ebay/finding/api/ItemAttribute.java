// Generated by xsd compiler for android/java
// DO NOT CHANGE!
package com.bo.salesdemo.ebay.finding.api;

import java.io.Serializable;
import com.leansoft.nano.annotation.*;

/**
 * 
 * This type contains the item attribute name and value.
 * 
 */
public class ItemAttribute implements Serializable {

    private static final long serialVersionUID = -1L;

	@Element
	@Order(value=0)
	public String name;	
	
	@Element
	@Order(value=1)
	public String value;	
	
    
}