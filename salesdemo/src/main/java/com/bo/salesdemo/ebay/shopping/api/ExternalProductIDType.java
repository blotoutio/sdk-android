// Generated by xsd compiler for android/java
// DO NOT CHANGE!
package com.bo.salesdemo.ebay.shopping.api;

import java.io.Serializable;
import com.leansoft.nano.annotation.*;
import java.util.List;

/**
 * 
 * Contains an ISBN value, EAN value, UPC value, ticket keywords, or eBay
 * catalog product ID, plus other meta-data. For event tickets, this type
 * can contain a set of keywords that uniquely identify the product. Only
 * applicable to certain categories that support Pre-filled Item
 * Information.
 * 
 */
public class ExternalProductIDType implements Serializable {

    private static final long serialVersionUID = -1L;

	@Element(name = "Value")
	@Order(value=0)
	public String value;	
	
	@Element(name = "ReturnSearchResultOnDuplicates")
	@Order(value=1)
	public Boolean returnSearchResultOnDuplicates;	
	
	@Element(name = "Type")
	@Order(value=2)
	public ExternalProductCodeType type;	
	
	@Element(name = "AlternateValue")
	@Order(value=3)
	public List<String> alternateValue;	
	
	@AnyElement
	@Order(value=4)
	public List<Object> any;	
	
    
}