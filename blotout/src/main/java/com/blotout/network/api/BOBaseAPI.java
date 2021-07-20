package com.blotout.network.api;
import com.blotout.Controllers.BOSDKManifestController;
import com.blotout.analytics.BlotoutAnalytics_Internal;
import com.blotout.constants.BONetworkConstants;

/**
 * Created by Blotout on 09,May,2020
 */
public class BOBaseAPI {

    private static final String TAG = "BOBaseAPI";

    private static volatile BOBaseAPI instance;

    public static BOBaseAPI getInstance() {

        if (instance == null) { //if there is no instance available... create new one
            synchronized (BOBaseAPI.class) {
                if (instance == null) {
                    instance = new BOBaseAPI();
                }
            }
        }
        return instance;
    }

    public String validateAndReturnServerEndPoint(String endPoint) {
        if(endPoint != null && endPoint.length() > 0) {
            if(endPoint.contains("http://") || endPoint.contains("https://")) {
                String lastChar = endPoint.substring(endPoint.length()-1);
                if(lastChar.equals("/")) {
                    return endPoint +  "sdk/" ;
                } else {
                    return endPoint + "/" + "sdk/" ;
                }
            } else {
                return null;
            }
        }

        return null;
    }

    public String getBaseAPI() {
        if(!BlotoutAnalytics_Internal.getInstance().isDevModeEnabled) {
            if (BlotoutAnalytics_Internal.getInstance().isProductionMode) {
                if (BOSDKManifestController.getInstance().serverBaseURL != null && BOSDKManifestController.getInstance().serverBaseURL.length() > 0) {
                    return BOSDKManifestController.getInstance().serverBaseURL + "/";
                } else {
                    String validatedUrl = this.validateAndReturnServerEndPoint(BlotoutAnalytics_Internal.getInstance().externalServerEndPointUrl);
                    if(validatedUrl != null && validatedUrl.length() > 0) {
                        return validatedUrl;
                    } else {
                        return BONetworkConstants.BASE_URL;
                    }
                }
            } else {
                if (BOSDKManifestController.getInstance().serverBaseURL != null && BOSDKManifestController.getInstance().serverBaseURL.length() > 0) {
                    return BOSDKManifestController.getInstance().serverBaseURL + "/";
                } else {
                    String validatedUrl = this.validateAndReturnServerEndPoint(BlotoutAnalytics_Internal.getInstance().externalServerEndPointUrl);
                    if(validatedUrl != null && validatedUrl.length() > 0) {
                        return validatedUrl;
                    } else {
                        return BONetworkConstants.BASE_URL_STAGE;
                    }
                }
            }
        } else {
            if (BOSDKManifestController.getInstance().serverBaseURL != null && BOSDKManifestController.getInstance().serverBaseURL.length() > 0) {
                return BOSDKManifestController.getInstance().serverBaseURL + "/";
            } else {
                String validatedUrl = this.validateAndReturnServerEndPoint(BlotoutAnalytics_Internal.getInstance().externalServerEndPointUrl);
                if(validatedUrl != null && validatedUrl.length() > 0) {
                    return validatedUrl;
                } else {
                    return BONetworkConstants.BASE_URL_DEVELOPMENT;
                }
            }
        }
    }

    public String getManifestPath() {
        if(BOSDKManifestController.getInstance().manifestPath != null && BOSDKManifestController.getInstance().manifestPath.length()>0) {
            return BOSDKManifestController.getInstance().manifestPath;
        } else {
            return "v1/manifest/pull";
        }
    }

    public String getEventPost() {
        if(BOSDKManifestController.getInstance().eventPath != null && BOSDKManifestController.getInstance().eventPath.length()>0) {
            return BOSDKManifestController.getInstance().eventPath;
        } else {
            return "v1/events/publish";
        }
    }

    public String getSegmentFeedback() {
        if(BOSDKManifestController.getInstance().segmentPathFeedback != null && BOSDKManifestController.getInstance().segmentPathFeedback.length()>0) {
            return BOSDKManifestController.getInstance().segmentPathFeedback;
        } else {
            return "v1/segment/custom/feedback";
        }
    }

    public String getSegmentPath() {
        if(BOSDKManifestController.getInstance().segmentPath != null && BOSDKManifestController.getInstance().segmentPath.length()>0) {
            return BOSDKManifestController.getInstance().segmentPath;
        } else {
            return "v1/segment/pull";
        }
    }

    public String getGeoPath() {
        if(BOSDKManifestController.getInstance().geoIPPath != null && BOSDKManifestController.getInstance().geoIPPath.length()>0) {
            return BOSDKManifestController.getInstance().geoIPPath;
        } else {
            return "v1/geo/city";
        }
    }

    public String getRetentionPublish() {
        if(BOSDKManifestController.getInstance().eventRetentionPath != null && BOSDKManifestController.getInstance().eventRetentionPath.length()>0) {
            return BOSDKManifestController.getInstance().eventRetentionPath;
        } else {
            return "v1/events/retention/publish";
        }
    }

    public String getFunnelFeedback() {
        if(BOSDKManifestController.getInstance().eventFunnelPathsFeedback != null && BOSDKManifestController.getInstance().eventFunnelPathsFeedback.length()>0) {
            return BOSDKManifestController.getInstance().eventFunnelPathsFeedback;
        } else {
            return "v1/funnel/feedback";
        }
    }

    public String getFunnelPath() {
        if(BOSDKManifestController.getInstance().eventFunnelPath != null && BOSDKManifestController.getInstance().eventFunnelPath.length()>0) {
            return BOSDKManifestController.getInstance().eventFunnelPath;
        } else {
            return "v1/funnel/pull";
        }
    }
}
