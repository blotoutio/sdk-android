package com.bo.salesdemo.ebay.shopping.api;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bo.salesdemo.ebay.shopping.api.client.ShoppingInterface_XMLClient;

public class ShoppingServiceClient {
	
	// production
	@NonNull
    public static String eBayShoppingServiceURLString = "https://open.api.ebay.com/shopping?";
	//http://open.api.ebay.com/shopping?
	// sandbox
	//public static final String eBayShoppingServiceURLString = "http://open.api.sandbox.ebay.com/shopping";
	@NonNull
    public static String eBayAppId = "Impetus06-f40e-4ba2-b112-1c3659ce305";
	@NonNull
    public static String targetAPIVersion = "685";
	/**
	for site id list, see http://developer.ebay.com/DevZone/shopping/docs/CallRef/types/SiteCodeType.html
	*/
	@NonNull
    public static String targetSiteid = "0";
	
	@Nullable
    private static volatile ShoppingInterface_XMLClient client = null;
	
	@Nullable
    public static ShoppingInterface_XMLClient getSharedClient() {
		if (client == null) {
			synchronized(ShoppingServiceClient.class) {
				if (client == null) {
					client = new ShoppingInterface_XMLClient();
					client.setEndpointUrl(eBayShoppingServiceURLString);
					client.getAsyncHttpClient().addHeader("X-EBAY-API-APP-ID", eBayAppId);
					client.getAsyncHttpClient().addHeader("X-EBAY-API-REQUEST-ENCODING", "XML");
					client.getAsyncHttpClient().addHeader("X-EBAY-API-VERSION", targetAPIVersion);
					client.getAsyncHttpClient().addHeader("X-EBAY-API-SITE-ID", targetSiteid);
				}
			}
		}
		
		return client;
	}

}
