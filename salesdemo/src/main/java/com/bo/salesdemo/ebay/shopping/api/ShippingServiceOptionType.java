// Generated by xsd compiler for android/java
// DO NOT CHANGE!
package com.bo.salesdemo.ebay.shopping.api;

import java.io.Serializable;
import com.leansoft.nano.annotation.*;
import java.util.List;
import java.util.Date;

/**
 * 
 * Shipping costs and options related to a domestic shipping service.
 * 
 */
public class ShippingServiceOptionType implements Serializable {

    private static final long serialVersionUID = -1L;

	@Element(name = "ShippingInsuranceCost")
	@Order(value=0)
	public AmountType shippingInsuranceCost;	
	
	@Element(name = "ShippingServiceName")
	@Order(value=1)
	public String shippingServiceName;	
	
	@Element(name = "ShippingServiceCost")
	@Order(value=2)
	public AmountType shippingServiceCost;	
	
	@Element(name = "ShippingServiceAdditionalCost")
	@Order(value=3)
	public AmountType shippingServiceAdditionalCost;	
	
	@Element(name = "ShippingServicePriority")
	@Order(value=4)
	public Integer shippingServicePriority;	
	
	@Element(name = "ExpeditedService")
	@Order(value=5)
	public Boolean expeditedService;	
	
	@Element(name = "ShippingTimeMin")
	@Order(value=6)
	public Integer shippingTimeMin;	
	
	@Element(name = "ShippingTimeMax")
	@Order(value=7)
	public Integer shippingTimeMax;	
	
	@Element(name = "ShippingSurcharge")
	@Order(value=8)
	public AmountType shippingSurcharge;	
	
	@Element(name = "ShipsTo")
	@Order(value=9)
	public List<String> shipsTo;	
	
	@Element(name = "EstimatedDeliveryMinTime")
	@Order(value=10)
	public Date estimatedDeliveryMinTime;	
	
	@Element(name = "EstimatedDeliveryMaxTime")
	@Order(value=11)
	public Date estimatedDeliveryMaxTime;	
	
	@Element(name = "FastAndFree")
	@Order(value=12)
	public Boolean fastAndFree;	
	
	@AnyElement
	@Order(value=13)
	public List<Object> any;	
	
    
}