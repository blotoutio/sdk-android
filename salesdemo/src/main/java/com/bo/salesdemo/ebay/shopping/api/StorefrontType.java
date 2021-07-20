// Generated by xsd compiler for android/java
// DO NOT CHANGE!
package com.bo.salesdemo.ebay.shopping.api;

import java.io.Serializable;
import com.leansoft.nano.annotation.*;
import java.util.List;

/**
 * 
 * Contains information related to the item in the context of
 * a seller's eBay Store. Applicable for auction format, Basic Fixed Price,
 * and Store Inventory format items listed by eBay Stores sellers.
 *  
 */
public class StorefrontType implements Serializable {

    private static final long serialVersionUID = -1L;

	@Element(name = "StoreURL")
	@Order(value=0)
	public String storeURL;	
	
	@Element(name = "StoreName")
	@Order(value=1)
	public String storeName;	
	
	@AnyElement
	@Order(value=2)
	public List<Object> any;	
	
    
}