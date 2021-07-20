// Generated by xsd compiler for android/java
// DO NOT CHANGE!
package com.bo.salesdemo.ebay.shopping.api;

import java.io.Serializable;
import com.leansoft.nano.annotation.*;
import java.util.List;

/**
 * 
 * Information about zero or more buying guides and the site's buying guide hub.
 * Buying guides contain content about particular product areas, categories, or subjects
 * to help buyers decide which type of item to purchase based on their particular interests.
 * Multiple buying guides can be returned. See the eBay Web Services Guide for additional information.
 * 
 */
public class BuyingGuideDetailsType implements Serializable {

    private static final long serialVersionUID = -1L;

	@Element(name = "BuyingGuide")
	@Order(value=0)
	public List<BuyingGuideType> buyingGuide;	
	
	@Element(name = "BuyingGuideHub")
	@Order(value=1)
	public String buyingGuideHub;	
	
	@AnyElement
	@Order(value=2)
	public List<Object> any;	
	
    
}