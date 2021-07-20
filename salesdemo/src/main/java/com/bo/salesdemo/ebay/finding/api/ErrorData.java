// Generated by xsd compiler for android/java
// DO NOT CHANGE!
package com.bo.salesdemo.ebay.finding.api;

import java.io.Serializable;
import com.leansoft.nano.annotation.*;
import java.util.List;

/**
 * 
 * A container for error details.
 * 
 */
public class ErrorData implements Serializable {

    private static final long serialVersionUID = -1L;

	@Element
	@Order(value=0)
	public long errorId;	
	
	@Element
	@Order(value=1)
	public String domain;	
	
	@Element
	@Order(value=2)
	public ErrorSeverity severity;	
	
	@Element
	@Order(value=3)
	public ErrorCategory category;	
	
	@Element
	@Order(value=4)
	public String message;	
	
	@Element
	@Order(value=5)
	public String subdomain;	
	
	@Element
	@Order(value=6)
	public String exceptionId;	
	
	@Element
	@Order(value=7)
	public List<ErrorParameter> parameter;	
	
    
}