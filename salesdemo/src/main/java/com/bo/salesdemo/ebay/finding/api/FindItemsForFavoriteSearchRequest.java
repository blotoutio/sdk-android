// Generated by xsd compiler for android/java
// DO NOT CHANGE!
package com.bo.salesdemo.ebay.finding.api;

import java.io.Serializable;
import com.leansoft.nano.annotation.*;
import java.util.Date;

/**
 * 
 * Reserved for future use.
 * 
 */
@RootElement(name = "findItemsForFavoriteSearchRequest", namespace = "http://www.ebay.com/marketplace/search/v1/services")
public class FindItemsForFavoriteSearchRequest extends BaseServiceRequest implements Serializable {

    private static final long serialVersionUID = -1L;

	@Element
	@Order(value=0)
	public Long searchId;	
	
	@Element
	@Order(value=1)
	public String searchName;	
	
	@Element
	@Order(value=2)
	public Date startTimeFrom;	
	
	@Element
	@Order(value=3)
	public PaginationInput paginationInput;	
	
    
}