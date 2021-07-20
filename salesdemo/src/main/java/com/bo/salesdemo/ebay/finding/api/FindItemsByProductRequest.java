// Generated by xsd compiler for android/java
// DO NOT CHANGE!
package com.bo.salesdemo.ebay.finding.api;

import java.io.Serializable;
import com.leansoft.nano.annotation.*;
import java.util.List;

/**
 * 
 * You can use product IDs (such as an ISBN, UPC, EAN, or eBay Product Reference
 * ID) to find associated items listed on eBay.
 * 
 */
@RootElement(name = "findItemsByProductRequest", namespace = "http://www.ebay.com/marketplace/search/v1/services")
public class FindItemsByProductRequest extends BaseFindingServiceRequest implements Serializable {

    private static final long serialVersionUID = -1L;

	@Element
	@Order(value=0)
	public ProductId productId;	
	
	@Element
	@Order(value=1)
	public List<ItemFilter> itemFilter;	
	
	@Element
	@Order(value=2)
	public List<OutputSelectorType> outputSelector;	
	
    
}