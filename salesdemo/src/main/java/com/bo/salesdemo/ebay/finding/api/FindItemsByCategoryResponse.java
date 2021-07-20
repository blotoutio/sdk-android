// Generated by xsd compiler for android/java
// DO NOT CHANGE!
package com.bo.salesdemo.ebay.finding.api;

import java.io.Serializable;
import com.leansoft.nano.annotation.*;

/**
 * 
 * Response container for the findItemsByKeywords call.
 * 
 */
@RootElement(name = "findItemsByCategoryResponse", namespace = "http://www.ebay.com/marketplace/search/v1/services")
public class FindItemsByCategoryResponse extends BaseFindingServiceResponse implements Serializable {

    private static final long serialVersionUID = -1L;

	@Element
	@Order(value=0)
	public CategoryHistogramContainer categoryHistogramContainer;	
	
	@Element
	@Order(value=1)
	public AspectHistogramContainer aspectHistogramContainer;	
	
	@Element
	@Order(value=2)
	public ConditionHistogramContainer conditionHistogramContainer;	
	
    
}