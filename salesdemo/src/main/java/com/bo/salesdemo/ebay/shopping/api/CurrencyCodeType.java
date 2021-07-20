// Generated by xsd compiler for android/java
// DO NOT CHANGE!
package com.bo.salesdemo.ebay.shopping.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 
 * Defines the standard 3-letter ISO 4217 currency code set.
 * However, only certain currency codes are currently valid for use on eBay.
 * The valid codes are documented below with the notation "(in/out)".
 * Other codes in this list are for future use.
 * The documentation below specifies English names for each currency.
 * Alternatively, use GeteBayDetails to retrieve the names programmatically.
 * A reference: http://www.xe.com/iso4217.htm
 * 
 */
public enum CurrencyCodeType {

    AFA("AFA"),
  

    ALL("ALL"),
  

    DZD("DZD"),
  

    ADP("ADP"),
  

    AOA("AOA"),
  

    ARS("ARS"),
  

    AMD("AMD"),
  

    AWG("AWG"),
  

    AZM("AZM"),
  

    BSD("BSD"),
  

    BHD("BHD"),
  

    BDT("BDT"),
  

    BBD("BBD"),
  

    BYR("BYR"),
  

    BZD("BZD"),
  

    BMD("BMD"),
  

    BTN("BTN"),
  

    /**
     * 
   * Indian Rupee.
   * Applicable to listings on the India site (site ID 203).
   * 
     */
    INR("INR"),
  

    BOV("BOV"),
  

    BOB("BOB"),
  

    BAM("BAM"),
  

    BWP("BWP"),
  

    BRL("BRL"),
  

    BND("BND"),
  

    BGL("BGL"),
  

    BGN("BGN"),
  

    BIF("BIF"),
  

    KHR("KHR"),
  

    /**
     * 
   * Canadian Dollar.
   * Applicable to listings on the Canada site (site ID 2)
   * (Items listed on the Canada site can also specify USD.)
   * 
     */
    CAD("CAD"),
  

    CVE("CVE"),
  

    KYD("KYD"),
  

    XAF("XAF"),
  

    CLF("CLF"),
  

    CLP("CLP"),
  

    /**
     * 
   * Chinese Yuan Renminbi.
   * 
     */
    CNY("CNY"),
  

    COP("COP"),
  

    KMF("KMF"),
  

    CDF("CDF"),
  

    CRC("CRC"),
  

    HRK("HRK"),
  

    CUP("CUP"),
  

    CYP("CYP"),
  

    CZK("CZK"),
  

    DKK("DKK"),
  

    DJF("DJF"),
  

    DOP("DOP"),
  

    TPE("TPE"),
  

    ECV("ECV"),
  

    ECS("ECS"),
  

    EGP("EGP"),
  

    SVC("SVC"),
  

    ERN("ERN"),
  

    EEK("EEK"),
  

    ETB("ETB"),
  

    FKP("FKP"),
  

    FJD("FJD"),
  

    GMD("GMD"),
  

    GEL("GEL"),
  

    GHC("GHC"),
  

    GIP("GIP"),
  

    GTQ("GTQ"),
  

    GNF("GNF"),
  

    GWP("GWP"),
  

    GYD("GYD"),
  

    HTG("HTG"),
  

    HNL("HNL"),
  

    /**
     * 
   * Hong Kong Dollar.
   * Applicable to listings on the Hong Kong site (site ID 201).
   * 
     */
    HKD("HKD"),
  

    HUF("HUF"),
  

    ISK("ISK"),
  

    IDR("IDR"),
  

    IRR("IRR"),
  

    IQD("IQD"),
  

    ILS("ILS"),
  

    JMD("JMD"),
  

    JPY("JPY"),
  

    JOD("JOD"),
  

    KZT("KZT"),
  

    KES("KES"),
  

    /**
     * 
   * Australian Dollar.
   * Applicable to listings on the Australia site (site ID 15).
   * 
     */
    AUD("AUD"),
  

    KPW("KPW"),
  

    KRW("KRW"),
  

    KWD("KWD"),
  

    KGS("KGS"),
  

    LAK("LAK"),
  

    LVL("LVL"),
  

    LBP("LBP"),
  

    LSL("LSL"),
  

    LRD("LRD"),
  

    LYD("LYD"),
  

    /**
     * 
   * Swiss Franc.
   * Applicable to listings on the Switzerland site (site ID 193).
   * 
     */
    CHF("CHF"),
  

    LTL("LTL"),
  

    MOP("MOP"),
  

    MKD("MKD"),
  

    MGF("MGF"),
  

    MWK("MWK"),
  

    /**
     * 
   * Malaysian Ringgit.
   * Applicable to listings on the Malaysia site (site ID 207).
   * 
     */
    MYR("MYR"),
  

    MVR("MVR"),
  

    MTL("MTL"),
  

    /**
     * 
   * Euro.
   * Applicable to listings on these site:
   * Austria (site 16), Belgium_French (site 23),
   * France (site 71), Germany (site 77), Italy (site 101), Belgium_Dutch (site 123),
   * Netherlands (site 146), Spain (site 186), Ireland (site 205).
   * 
     */
    EUR("EUR"),
  

    MRO("MRO"),
  

    MUR("MUR"),
  

    MXN("MXN"),
  

    MXV("MXV"),
  

    MDL("MDL"),
  

    MNT("MNT"),
  

    XCD("XCD"),
  

    MZM("MZM"),
  

    MMK("MMK"),
  

    ZAR("ZAR"),
  

    NAD("NAD"),
  

    NPR("NPR"),
  

    ANG("ANG"),
  

    XPF("XPF"),
  

    NZD("NZD"),
  

    NIO("NIO"),
  

    NGN("NGN"),
  

    NOK("NOK"),
  

    OMR("OMR"),
  

    PKR("PKR"),
  

    PAB("PAB"),
  

    PGK("PGK"),
  

    PYG("PYG"),
  

    PEN("PEN"),
  

    /**
     * 
   * Philippines Peso.
   * Applicable to listings on the Philippines site (site ID 211).
   * 
     */
    PHP("PHP"),
  

    /**
     * 
   * Poland, Zloty.
   * Applicable to listings on the Poland site (site ID 212).
   * 
     */
    PLN("PLN"),
  

    /**
     * 
   * US Dollar.
   * Applicable to listings on the US (site ID 0), eBayMotors (site 100), and Canada (site 2) sites.
   * 
     */
    USD("USD"),
  

    QAR("QAR"),
  

    ROL("ROL"),
  

    RUB("RUB"),
  

    RUR("RUR"),
  

    RWF("RWF"),
  

    SHP("SHP"),
  

    WST("WST"),
  

    STD("STD"),
  

    SAR("SAR"),
  

    SCR("SCR"),
  

    SLL("SLL"),
  

    /**
     * 
   * Singapore Dollar.
   * Applicable to listings on the Singapore site (site 216).
   * 
     */
    SGD("SGD"),
  

    SKK("SKK"),
  

    SIT("SIT"),
  

    SBD("SBD"),
  

    SOS("SOS"),
  

    LKR("LKR"),
  

    SDD("SDD"),
  

    SRG("SRG"),
  

    SZL("SZL"),
  

    /**
     * 
   *  Swedish Krona.
   *  Applicable to listings on the Sweden site (site 218).
   * 
     */
    SEK("SEK"),
  

    SYP("SYP"),
  

    /**
     * 
   * New Taiwan Dollar.
   * 
     */
    TWD("TWD"),
  

    TJS("TJS"),
  

    TZS("TZS"),
  

    THB("THB"),
  

    XOF("XOF"),
  

    TOP("TOP"),
  

    TTD("TTD"),
  

    TND("TND"),
  

    TRL("TRL"),
  

    TMM("TMM"),
  

    UGX("UGX"),
  

    UAH("UAH"),
  

    AED("AED"),
  

    /**
     * 
   * Pound Sterling.
   * Applicable to listings on the UK site (site ID 3).
   * 
     */
    GBP("GBP"),
  

    USS("USS"),
  

    USN("USN"),
  

    UYU("UYU"),
  

    UZS("UZS"),
  

    VUV("VUV"),
  

    VEB("VEB"),
  

    VND("VND"),
  

    MAD("MAD"),
  

    YER("YER"),
  

    YUM("YUM"),
  

    ZMK("ZMK"),
  

    ZWD("ZWD"),
  

    ATS("ATS"),
  

    /**
     * 
   * Placeholder value. See
   * <a href="types/simpleTypes.html#token">token</a>.
   * 
     */
    CUSTOM_CODE("CustomCode");
  
  
    private final String value;
  
    CurrencyCodeType(String v) {
        value = v;
    }
    
    public String value() {
        return value;
    }
    
    @NonNull
    public static CurrencyCodeType fromValue(@Nullable String v) {
        if (v != null) {
            for (CurrencyCodeType c: CurrencyCodeType.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
        }
        throw new IllegalArgumentException(v);
    }
}