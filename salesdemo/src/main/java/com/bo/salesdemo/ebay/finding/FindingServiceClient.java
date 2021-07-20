package com.bo.salesdemo.ebay.finding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bo.salesdemo.ebay.finding.api.client.FindingServicePortType_SOAPClient;

import com.leansoft.nano.ws.SOAPVersion;

public class FindingServiceClient {
	
	
	// production
	@NonNull
    public static String eBayFindingServiceURLString = "http://svcs.ebay.com/services/search/FindingService/v1";
	// sandbox
	//public static String eBayFindingServiceURLString = "https://svcs.sandbox.ebay.com/services/search/FindingService/v1";
	@NonNull
    public static String eBayAppId = "Impetus06-f40e-4ba2-b112-1c3659ce305";
	
	@Nullable
    private static volatile FindingServicePortType_SOAPClient client = null;
	
	@Nullable
    public static FindingServicePortType_SOAPClient getSharedClient() {
		if (client == null) {
			synchronized(FindingServiceClient.class) {
				if (client == null) {
					client = new FindingServicePortType_SOAPClient();
					client.setEndpointUrl(eBayFindingServiceURLString);
					client.setSoapVersion(SOAPVersion.SOAP12); // ebay finding service supports SOAP 1.2
					client.setContentType("application/soap+xml");
					client.getAsyncHttpClient().addHeader("Accept", "application/soap+xml");
					client.getAsyncHttpClient().addHeader("X-EBAY-SOA-SECURITY-APPNAME", eBayAppId);
					client.getAsyncHttpClient().addHeader("X-EBAY-SOA-MESSAGE-PROTOCOL", "SOAP12");
					client.getAsyncHttpClient().addHeader("X-EBAY-SOA-REQUEST-DATA-FORMAT", "SOAP");
				}
			}
		}
		
		return client;
	}
	
}
