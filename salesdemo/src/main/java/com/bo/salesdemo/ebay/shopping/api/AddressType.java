// Generated by xsd compiler for android/java
// DO NOT CHANGE!
package com.bo.salesdemo.ebay.shopping.api;

import java.io.Serializable;
import com.leansoft.nano.annotation.*;
import java.util.List;

/**
 * 
 * Contains the data for one user address. This is the base type for a
 * number of user addresses, including seller payment address, buyer
 * shipping address and buyer and seller registration address.
 * 
 */
public class AddressType implements Serializable {

    private static final long serialVersionUID = -1L;

	@Element(name = "Name")
	@Order(value=0)
	public String name;	
	
	@Element(name = "Street")
	@Order(value=1)
	public String street;	
	
	@Element(name = "Street1")
	@Order(value=2)
	public String street1;	
	
	@Element(name = "Street2")
	@Order(value=3)
	public String street2;	
	
	@Element(name = "CityName")
	@Order(value=4)
	public String cityName;	
	
	@Element(name = "County")
	@Order(value=5)
	public String county;	
	
	@Element(name = "StateOrProvince")
	@Order(value=6)
	public String stateOrProvince;	
	
	@Element(name = "CountryName")
	@Order(value=7)
	public String countryName;	
	
	@Element(name = "Phone")
	@Order(value=8)
	public String phone;	
	
	@Element(name = "PhoneCountryPrefix")
	@Order(value=9)
	public String phoneCountryPrefix;	
	
	@Element(name = "PhoneAreaOrCityCode")
	@Order(value=10)
	public String phoneAreaOrCityCode;	
	
	@Element(name = "PhoneLocalNumber")
	@Order(value=11)
	public String phoneLocalNumber;	
	
	@Element(name = "Phone2CountryPrefix")
	@Order(value=12)
	public String phone2CountryPrefix;	
	
	@Element(name = "Phone2AreaOrCityCode")
	@Order(value=13)
	public String phone2AreaOrCityCode;	
	
	@Element(name = "Phone2LocalNumber")
	@Order(value=14)
	public String phone2LocalNumber;	
	
	@Element(name = "PostalCode")
	@Order(value=15)
	public String postalCode;	
	
	@Element(name = "AddressID")
	@Order(value=16)
	public String addressID;	
	
	@Element(name = "ExternalAddressID")
	@Order(value=17)
	public String externalAddressID;	
	
	@Element(name = "InternationalName")
	@Order(value=18)
	public String internationalName;	
	
	@Element(name = "InternationalStateAndCity")
	@Order(value=19)
	public String internationalStateAndCity;	
	
	@Element(name = "InternationalStreet")
	@Order(value=20)
	public String internationalStreet;	
	
	@Element(name = "CompanyName")
	@Order(value=21)
	public String companyName;	
	
	@Element(name = "FirstName")
	@Order(value=22)
	public String firstName;	
	
	@Element(name = "LastName")
	@Order(value=23)
	public String lastName;	
	
	@AnyElement
	@Order(value=24)
	public List<Object> any;	
	
    
}