// Generated by xsd compiler for android/java
// DO NOT CHANGE!
package com.bo.salesdemo.ebay.finding.api;

import java.io.Serializable;
import com.leansoft.nano.annotation.*;
import java.util.List;

/**
 * 
 * Statistical (histogram) information about categories that contain items that
 * match the query, if any. For categories associated with specific items, see
 * items returned in each search result. Shows the distribution of items across
 * each category. Not returned if there is no match.
 * 
 */
public class CategoryHistogram extends Category implements Serializable {

    private static final long serialVersionUID = -1L;

	@Element
	@Order(value=0)
	public Long count;	
	
	@Element
	@Order(value=1)
	public List<CategoryHistogram> childCategoryHistogram;	
	
	@Element
	@Order(value=2)
	public String delimiter;	
	
	@AnyElement
	@Order(value=3)
	public List<Object> any;	
	
    
}