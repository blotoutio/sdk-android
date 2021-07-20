// Generated by xsd compiler for android/java
// DO NOT CHANGE!
package com.bo.salesdemo.ebay.finding.api;

import java.io.Serializable;
import com.leansoft.nano.annotation.*;
import java.util.List;

/**
 * 
 * Response container for the spelling check and correction for keywords.
 * 
 */
@RootElement(name = "getSearchKeywordsRecommendationResponse", namespace = "http://www.ebay.com/marketplace/search/v1/services")
public class GetSearchKeywordsRecommendationResponse extends BaseServiceResponse implements Serializable {

    private static final long serialVersionUID = -1L;

	@Element
	@Order(value=0)
	public String keywords;	
	
	@Element
	@Order(value=1)
	public List<ExtensionType> extension;	
	
    
}