// Generated by xsd compiler for android/java
// DO NOT CHANGE!
package com.bo.salesdemo.ebay.shopping.api;

import java.io.Serializable;
import com.leansoft.nano.annotation.*;
import java.util.List;

/**
 * 
 * A name and corresponding value (a name/value pair).
 * 
 */
public class NameValueListType implements Serializable {

    private static final long serialVersionUID = -1L;

	@Element(name = "Name")
	@Order(value=0)
	public String name;	
	
	@Element(name = "Value")
	@Order(value=1)
	public List<String> value;	
	
	@AnyElement
	@Order(value=2)
	public List<Object> any;	
	
    
}