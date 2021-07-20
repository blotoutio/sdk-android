package com.blotout.Controllers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blotout.analytics.BOHandlerMessage;
import com.blotout.eventsExecutor.BONetworkSegmentExecutorHelper;
import com.blotout.analytics.BOSharedManager;
import com.blotout.analytics.BlotoutAnalytics_Internal;
import com.blotout.constants.BOCommonConstants;
import com.blotout.events.BOAppSessionEvents;
import com.blotout.events.BOCommonEvents;
import com.blotout.model.Segments.SegmentsPayload.BOSegmentsResGeo;
import com.blotout.model.Segments.SegmentsPayload.BOSegmentsResMeta;
import com.blotout.model.Segments.SegmentsPayload.BOSegmentsResSegment;
import com.blotout.model.Segments.SegmentsPayload.BOSegmentsResSegmentsPayload;
import com.blotout.model.Segments.SegmentsReceivedModel.BORule;
import com.blotout.model.Segments.SegmentsReceivedModel.BOSegment;
import com.blotout.model.Segments.SegmentsReceivedModel.BOSegmentEvents;
import com.blotout.model.session.BOAppSessionDataModel;
import com.blotout.network.api.BOBaseAPI;
import com.blotout.network.api.BOSegmentAPI;
import com.blotout.storage.BOFileSystemManager;
import com.blotout.storage.BOSharedPreferenceImpl;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.BODateTimeUtils;
import com.blotout.utilities.BOServerDataConverter;
import com.blotout.utilities.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Blotout on 04,January,2020
 */
public class BOSegmentsSyncController {

    private static String BO_STRING_SEPERATOR_FOR_FILE_NAME = "-";
    private static final String TAG = "BOSegmentsSyncController";
    private static volatile BOSegmentsSyncController instance;
    private boolean isPrepareSegmentsSyncCalled;
    private List<String> qualifiedSegments;
    private List<String> qualifiedSyncCompleteSegments;
    private boolean requestInProgress = false;
    private static final String boSegmentsRecursiveDownloadHandlerKey = "boSegmentsRecursiveDownloadHandlerKey";
    private static final String boSegmentsUserQualifyHandlerKey = "boSegmentsUserQualifyHandlerKey";
    private HashMap<String, Object> metaData;
    private HashMap<String, Object> geoData;
    private HashMap<String,String> eventMap;

    public static BOSegmentsSyncController getInstance() {

        if (instance == null) { //if there is no instance available... create new one
            synchronized (BOSegmentsSyncController.class) {
                if (instance == null) {
                    instance = new BOSegmentsSyncController();
                }
            }

        }
        return instance;
    }

    private BOSegmentsSyncController() {
        this.qualifiedSegments = new ArrayList<>();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        requestInProgress = false;
    }

    private List subStringsFromString(String completeString, String separator) {
        return Arrays.asList(completeString.split(separator));
    }

    private String subStringFromStringBeforeSeparator(String completeString, String separator) {
        try {
            String[] splittedString = completeString.split(separator);
            if (splittedString.length > 0) {
                return splittedString[0];
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    private String subStringFromStringAfterSeparator(String completeString, String separator) {
        String[] splittedString = completeString.split(separator);
        if (splittedString.length > 0) {
            return splittedString[splittedString.length - 1];
        }
        return null;
    }

    private List<String> getListOfSegmentAlreadyQulified() {
        //Not considering offline case, in that date wise folder has to be created.
        //In current scenario, will create one folder, files name as segment id and daily sync will sync these and move to synced files
        try {
            String segmentsSyncPendingDirPath = BOFileSystemManager.getInstance().getSessionBasedSegmentsEventsSyncPendingDirectoryPath();
            String fileExtention = "txt";
            List<String> allFiles = BOFileSystemManager.getInstance().getAllFilesWithExtension(segmentsSyncPendingDirPath, fileExtention);
            List<String> segmentIDs = (allFiles.size() > 0) ? new ArrayList<>() : null;
            for (String filePath : allFiles) {
                String completeFileName = BOFileSystemManager.getInstance().getLastPathComponent(filePath);
                String segmentID = this.subStringFromStringBeforeSeparator(completeFileName, BO_STRING_SEPERATOR_FOR_FILE_NAME);
                segmentIDs.add(segmentID);
            }
            if (segmentIDs != null && segmentIDs.size() > 0) {
                this.qualifiedSegments = segmentIDs;
            }
            return segmentIDs;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return null;
        }
    }

    private List<String> getListOfSegmentAlreadyQulifiedAndSyncedWithServer() {
        //Not considering offline case, in that date wise folder has to be created.
        //In current scenario, will create one folder, files name as segment id and daily sync will sync these and move to synced files
        try {
            String segmentsSyncCompleteDirPath = BOFileSystemManager.getInstance().getSessionBasedSegmentsEventsSyncCompleteDirectoryPath();
            String fileExtention = "txt";
            List<String> allDateFolders = BOFileSystemManager.getInstance().getAllDirectories(segmentsSyncCompleteDirPath);
            List<String> allSyncedSegmentFiles = BOFileSystemManager.getInstance().getAllFilesWithExtension(segmentsSyncCompleteDirPath, fileExtention);
            List<String> segmentIDRootArrs = new ArrayList<>();

            for (String singleDateDir : allDateFolders) {
                List<String> allFiles = BOFileSystemManager.getInstance().getAllFilesWithExtension(singleDateDir, fileExtention);
                //NSMutableArray <NSString*>*segmentIDs = (allFiles.count > 0) ? [NSMutableArray array] : nil;
                for (String filePath : allFiles) {
                    String completeFileName = BOFileSystemManager.getInstance().getLastPathComponent(filePath);
                    String segmentID = this.subStringFromStringBeforeSeparator(completeFileName, BO_STRING_SEPERATOR_FOR_FILE_NAME);
                    segmentIDRootArrs.add(segmentID);
                }
            }
            this.qualifiedSyncCompleteSegments = segmentIDRootArrs;

            for (String oneFilePath : allSyncedSegmentFiles) {
                //NSString *segmentID = [[oneFilePath stringByDeletingPathExtension] lastPathComponent];
                String completeFileName = BOFileSystemManager.getInstance().getLastPathComponent(oneFilePath);
                String segmentID = subStringFromStringBeforeSeparator(completeFileName, BO_STRING_SEPERATOR_FOR_FILE_NAME);
                segmentIDRootArrs.add(segmentID);
                qualifiedSyncCompleteSegments.add(segmentID);
            }

            return (segmentIDRootArrs != null && segmentIDRootArrs.size() > 0) ? segmentIDRootArrs : null;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return null;
        }
    }

    public void prepareSegmentsSyncAndAnalyser() {
        try {
            BOSegmentEvents segmentEvents = null;
            if (!isPrepareSegmentsSyncCalled) {
                segmentEvents = loadAllActiveSegments();
                if (segmentEvents != null) {
                    isPrepareSegmentsSyncCalled = true;
                }
            }

            loadSegmentsNetworkScheduler();

            serverSyncQualifiedSegment();

            startQualifyingAvailableSegment(segmentEvents,null);

        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private void startQualifyingAvailableSegment(BOSegmentEvents segmentEvents,BOAppSessionDataModel sessionDataModel) {
        if (segmentEvents != null && segmentEvents.getSegments() != null && segmentEvents.getSegments().size() > 0) {
            for (int indx = 0; indx < segmentEvents.getSegments().size(); indx++) {
                BOSegment oneSegment = segmentEvents.getSegments().get(indx);
                //Don't check for prequalified segments
                List<String> alreadyQualifiedSegs = this.getListOfSegmentAlreadyQulified();
                if (alreadyQualifiedSegs != null && alreadyQualifiedSegs.contains("" + oneSegment.getIdentifier())) {
                    continue;
                }
                boolean isNewSegmentToLoad = true;
                for (BOSegment fTestIDEvent : segmentEvents.getSegments()) {
                    if (fTestIDEvent.getIdentifier().equals(oneSegment.getIdentifier())) {
                        isNewSegmentToLoad = false;
                        break;
                    }
                }
                boolean doesQualify = false;
                //Load segment qualifier and test against qualification, check for already qualified segments and do not repeat
                doesQualify = doesUserQualifiesForSegment(oneSegment,sessionDataModel, new BOHandlerMessage() {
                    @Override
                    public void handleMessage(BOHandlerMessage.BOMessage msg) {
                        if (msg.what == 1) {
                            userQualifiedForTheSegment((BOSegment) msg.obj);
                        }
                    }
                });

                if (doesQualify) {
                    userQualifiedForTheSegment(oneSegment);
                }
                //TODO:
                //Generate server response and save against segment ID... once qualified will not be tested on old data
                //If not qualified then next weekly data will be considered from the date of last test not qualified
            }
        }
    }

    private void userQualifiedForTheSegment(BOSegment segmentInfo) {
        try {
            if (!this.qualifiedSegments.contains("" + segmentInfo.getIdentifier())) {
                String segmentsSyncPendingDirPath = BOFileSystemManager.getInstance().getSessionBasedSegmentsEventsSyncPendingDirectoryPath();
                String fileExtention = "txt";
                long timeStamp = BODateTimeUtils.get13DigitNumberObjTimeStamp();

                String segmentIDSyncPendingFilePath = "" + segmentsSyncPendingDirPath + "/" + segmentInfo.getIdentifier() + BO_STRING_SEPERATOR_FOR_FILE_NAME + timeStamp + "." + fileExtention;
                String segName = segmentInfo.getName().replace(" ", "");
                HashMap<String, Object> serverResponse = new HashMap<>();
                serverResponse.put("id", segmentInfo.getIdentifier());
                serverResponse.put("event_time", segmentInfo.getIdentifier());
                serverResponse.put("message_id", BOCommonUtils.getMessageIDForEventWithEventId("Seg" + segName + segmentInfo.getCreatedTime(), segmentInfo.getIdentifier()));

                BOSegmentsResSegmentsPayload resPayload = new BOSegmentsResSegmentsPayload();
                resPayload.setMeta(prepareMetaData(null));
                resPayload.setGeo(prepareGeoData());
                BOSegmentsResSegment segmentRes = BOSegmentsResSegment.fromJsonDictionary(serverResponse);
                List<BOSegmentsResSegment> list = new ArrayList<>();
                list.add(segmentRes);
                resPayload.setSegments(list);

                String serverResStr = BOSegmentsResSegmentsPayload.toJsonString(resPayload);
                //else file write operation and prapare new object
                BOFileSystemManager.getInstance().writeToFile(segmentIDSyncPendingFilePath, serverResStr);

                this.qualifiedSegments.add("" + segmentInfo.getIdentifier());
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private void serverSyncQualifiedSegment() {
        try {
            String segmentsSyncPendingDirPath = BOFileSystemManager.getInstance().getSessionBasedSegmentsEventsSyncPendingDirectoryPath();
            String fileExtention = "txt";
            List<String> allFiles = BOFileSystemManager.getInstance().getAllFilesWithExtension(segmentsSyncPendingDirPath, fileExtention);

            for (String oneFile : allFiles) {
                try {
                    String jsonString = BOFileSystemManager.getInstance().readContentOfFileAtPath(oneFile);
                    HashMap<String, Object> dailyDataDict = BOCommonUtils.getHashmapFromJsonString(jsonString);
                    this.serverSyncSegmentsAtURLRequest(dailyDataDict, new BOHandlerMessage() {
                        @Override
                        public void handleMessage(@NonNull BOMessage msg) {
                            if (msg.what == 1) {
                                String completeFileName = BOFileSystemManager.getInstance().getFilePathAfterDeletingPathExtention(BOFileSystemManager.getInstance().getLastPathComponent(oneFile));
                                String dateStampString = subStringFromStringAfterSeparator(completeFileName, BO_STRING_SEPERATOR_FOR_FILE_NAME);
                                String fileCreationDateStr = dateStampString != null ? BODateTimeUtils.getDateStringFromString(dateStampString, BOCommonConstants.BO_DATE_FORMAT) : "default";
                                if (fileCreationDateStr != null && fileCreationDateStr.equals("default")) {
                                    Date fileCreationDate = BOFileSystemManager.getInstance().getCreationDateOfItemAtPath(oneFile);
                                    fileCreationDateStr = fileCreationDate != null ? BODateTimeUtils.getStringFromDate(fileCreationDate, BOCommonConstants.BO_DATE_FORMAT) : "default";
                                }
                                String syncedPathDir = BOFileSystemManager.getInstance().getSessionBasedSegmentsEventsSyncCompleteDirectoryPath();
                                String syncedPathDateDir = fileCreationDateStr != null ? BOFileSystemManager.getInstance().createDirectoryIfRequiredAndReturnPath(syncedPathDir + "/" + fileCreationDateStr) : null;
                                if (syncedPathDateDir != null) {
                                    BOFileSystemManager.getInstance().moveFile(oneFile, syncedPathDateDir);
                                } else if (syncedPathDir != null) {
                                    BOFileSystemManager.getInstance().moveFile(oneFile, syncedPathDir);
                                }
                            } else {

                            }
                        }
                    });
                } catch (Exception e) {
                    Logger.INSTANCE.e(TAG, e.toString());
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private void serverSyncSegmentsAtURLRequest(
            @Nullable HashMap<String, Object> jsonDict, @NonNull BOHandlerMessage callback) {

        if (!BlotoutAnalytics_Internal.getInstance().isSDKEnabled && (jsonDict == null || !BlotoutAnalytics_Internal.getInstance().isSegmentEventsEnabled)) {
            //As no request was shared, will create neutral error case later
            callback.handleMessage(new BOHandlerMessage.BOMessage(0, null));
            return;
        }
        try {
            BOSegmentAPI api = BOSharedManager.getInstance().getAPIFactory().segmentAPI;
            Call<Object> call = api.postSegmentData(BOBaseAPI.getInstance().getSegmentFeedback(), jsonDict);
            Response response = call.execute();
            if (response.isSuccessful()) {
                BOCommonEvents.getInstance().recordSegmentTriggered();
                Logger.INSTANCE.e(TAG, "segment upload succeed" + jsonDict.toString());
                callback.handleMessage(new BOHandlerMessage.BOMessage(1, null));
            } else {
                callback.handleMessage(new BOHandlerMessage.BOMessage(0, null));
            }

        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            callback.handleMessage(new BOHandlerMessage.BOMessage(0, null));
        }
    }

    private void loadSegmentsNetworkScheduler() {
        try {
            long lastSyncTimeStamp = BOSharedPreferenceImpl.getInstance().getLong(BOCommonConstants.BO_ANALYTICS_SEGMENT_LAST_SYNC_TIME_DEFAULTS_KEY);
            if (lastSyncTimeStamp > 0) {
                long updatedSyncTime = BODateTimeUtils.get13DigitNumberObjTimeStamp() - lastSyncTimeStamp;
                long networkSyncTime = BOSDKManifestController.delayInterval();
                long delayNow = ((networkSyncTime - updatedSyncTime) > 0) ? (networkSyncTime - updatedSyncTime) : 0;
                recursivelyDownloadSegmentsAfterDelay(delayNow, "");
            } else {
                recursivelyDownloadSegmentsAfterDelay(0, "");
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    @Nullable
    private HashMap<String, Object> prepareMetaDataDict() {
        try {
            if (this.metaData == null) {
                HashMap<String, Object> metaDatas = BOServerDataConverter.prepareMetaData();
                if (metaDatas != null) {
                    this.metaData = metaDatas;
                    return new HashMap<>(metaDatas);
                } else {
                    return null;
                }
            } else {
                return new HashMap<>(this.metaData);
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return null;
        }
    }

    @Nullable
    private BOSegmentsResMeta prepareMetaData(BOAppSessionDataModel sessionData) {
        try {
            HashMap<String, Object> metaDataDict = prepareMetaDataDict();
            return BOSegmentsResMeta.fromJsonDictionary(metaDataDict);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return null;
        }
    }

    @Nullable
    private BOSegmentsResGeo prepareGeoData() {
        try {
            HashMap<String, Object> metaDataDict = prepareGeoDataDict();
            return BOSegmentsResGeo.fromJsonDictionary(metaDataDict);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return null;
        }
    }

    @Nullable
    private HashMap<String, Object> prepareGeoDataDict() {
        try {
            if (this.geoData == null) {
                HashMap<String, Object> geoDatas = BOServerDataConverter.prepareGeoData();
                if (geoDatas != null) {
                    this.geoData = geoDatas;
                    return new HashMap<>(geoDatas);
                } else {
                    return new HashMap<>();
                }
            } else {
                return new HashMap<>(this.geoData);
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return new HashMap<>();
        }
    }

    @Nullable
    private BOSegmentEvents loadAllActiveSegments() {
        try {
            String fileExtention = "txt";

            String allSegmentsDirPath = BOFileSystemManager.getInstance().getAllSegmentsToAnalyseDirectoryPath();
            String allSegmentsFilePath = allSegmentsDirPath + "/" + "AllSegments" + "." + fileExtention;

            String jsonString = null;
            try {
                jsonString = BOFileSystemManager.getInstance().readContentOfFileAtPath(allSegmentsFilePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            BOSegmentEvents segments = null;
            if (jsonString != null && !jsonString.equals("")) {
                HashMap<String, Object> mapJson = BOCommonUtils.getHashmapFromJsonString(jsonString);
                segments = BOSegmentEvents.fromJsonDictionary(mapJson);
            }
            return segments;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return null;
        }
    }

    boolean isSegmentsNewDataValid(@NonNull BOSegmentEvents sessionSegmentsNewData) {
        boolean isAllSegmentValid = false;
        try {
            if (sessionSegmentsNewData.getSegments() != null && sessionSegmentsNewData.getSegments().size() > 0) {
                for (BOSegment eventSegment : sessionSegmentsNewData.getSegments()) {
                    if (eventSegment.getRuleset().getRules().size() >= 1) {
                        isAllSegmentValid = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return isAllSegmentValid;
    }

    //This methos only filter funnel events and modify objects with only valid funnels
    @NonNull
    private BOSegmentEvents validNewDataSegments(@NonNull BOSegmentEvents sessionSegmentsNewData) {
        BOSegmentEvents segmentEvents = new BOSegmentEvents();
        try {
            List<BOSegment> segmentsArr = new ArrayList<>();
            //NSMutableArray<BOAEventsCodified *> *eventCodifiedArr = [NSMutableArray array];
            if (sessionSegmentsNewData.getSegments() != null && sessionSegmentsNewData.getSegments().size() > 0) {
                for (BOSegment eventSegment : sessionSegmentsNewData.getSegments()) {
                    if (eventSegment.getRuleset().getRules().size() >= 1) {
                        segmentsArr.add(eventSegment);
                        //[eventCodifiedArr addObject:<#(nonnull BOAEventsCodified *)#>]
                    }
                }
            }
            segmentEvents.setGeo(sessionSegmentsNewData.getGeo());
            segmentEvents.setSegments(segmentsArr);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());

        }
        return segmentEvents;
    }

    private void storeSessionSegmentsNewData(@Nullable BOSegmentEvents sessionSegmentsNewData) {
        if (sessionSegmentsNewData == null) {
            return;
        }
        try {
            boolean newSegmentsAdded = false;
            BOSegmentEvents oldSegmentsEvent = loadAllActiveSegments();
            if (oldSegmentsEvent != null && oldSegmentsEvent.getSegments() != null && oldSegmentsEvent.getSegments().size() > 0) {

                List<BOSegment> oldAndNewEvents = new ArrayList<>(oldSegmentsEvent.getSegments());

                //TODO: Directly adding from array can lead to duplicate funnel object if server makes a mistake.
                //TODO: Using above incomplete for loop mechanism, we can filter but will see the need & do

                //Once model is updated then implement logic for deleting old one as well
                for (BOSegment newSegment : sessionSegmentsNewData.getSegments()) {
                    boolean isSameFEvent = false;
                    for (BOSegment oldSegment : oldAndNewEvents) {
                        if (newSegment.getIdentifier().equals(oldSegment.getIdentifier())) {
                            isSameFEvent = true;
                            break;
                        }
                    }
                    if (!isSameFEvent) {
                        oldAndNewEvents.add(newSegment);
                        newSegmentsAdded = true;
                    }
                }
                //Below was duplicating funnel events
                //[oldAndNewEvents addObjectsFromArray:sessionFunnelsNewData.eventsFunnel];
                oldSegmentsEvent.setSegments(oldAndNewEvents);
            } else {
                oldSegmentsEvent = sessionSegmentsNewData;
                newSegmentsAdded = true;
            }

            if (newSegmentsAdded) {
                BOCommonEvents.getInstance().recordSegmentReceived();
            }

            String allFunnelsDataStr = BOSegmentEvents.toJsonString(oldSegmentsEvent);

            String fileExtention = "txt";
            String allSegmentsDirPath = BOFileSystemManager.getInstance().getAllSegmentsToAnalyseDirectoryPath();
            String allSegmentsFilePath = allSegmentsDirPath + "/" + "AllSegments" + "." + fileExtention;
            //else file write operation and prapare new object
            BOFileSystemManager.getInstance().writeToFile(allSegmentsFilePath, allFunnelsDataStr);

            String sessionSegmentsNewDataStr = BOSegmentEvents.toJsonString(sessionSegmentsNewData);

            String dateString = BODateTimeUtils.getStringFromDate(BODateTimeUtils.getCurrentDate(), BOCommonConstants.BO_DATE_FORMAT);
            String segmentDownloadLogsDir = BOFileSystemManager.getInstance().getLogLevelDirAllFunnelsToAnalyseDirectoryPath();
            String segmentDownloadLogsFile = segmentDownloadLogsDir + "/" + dateString + "-" + BODateTimeUtils.get13DigitNumberObjTimeStamp() + ".txt";

            //else file write operation and prapare new object
            BOFileSystemManager.getInstance().writeToFile(segmentDownloadLogsFile, sessionSegmentsNewDataStr);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }


    public void pauseSegmentsSyncAndAnalyser() {

    }

    public boolean isSegmentAvailable() {
        try {
            BOSegmentEvents segEvents = loadAllActiveSegments();
            if (segEvents != null && segEvents.getSegments() != null && (segEvents.getSegments().size() > 0)) {
                return true;
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return false;
    }

    //Timer logic to run funnel task under fixed frequency
    private void recursivelyDownloadSegmentsUsingURLRequest(String networkRequest) {
        //write job to fetch and send data as frequency
        //Also save files in directory, expired under history/expired, live under live and then unsynced + sync
        //Create direcotry structure


        try {
            this.segmentsUsingURLRequest(networkRequest, new BOHandlerMessage() {
                @Override
                public void handleMessage(@NonNull BOMessage msg) {
                    if (msg.what == 1) {
                        BOSegmentEvents allFunnels = (BOSegmentEvents) msg.obj;
                        if (isSegmentsNewDataValid(allFunnels)) {
                            BOSegmentEvents validEvents = validNewDataSegments(allFunnels);
                            storeSessionSegmentsNewData(validEvents);
                            if (!BOSegmentsSyncController.this.isPrepareSegmentsSyncCalled) {
                                prepareSegmentsSyncAndAnalyser();
                            }
                        }
                    } else {

                    }
                }
            });

            BOSharedPreferenceImpl.getInstance().saveLong(BOCommonConstants.BO_ANALYTICS_SEGMENT_LAST_SYNC_TIME_DEFAULTS_KEY, BODateTimeUtils.get13DigitNumberObjTimeStamp());
            recursivelyDownloadSegmentsAfterDelay(BOSDKManifestController.delayInterval(), "");
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());

        }
    }

    private void recursivelyDownloadSegmentsAfterDelay(long milliSeconds, String request) {
        Runnable recursiveDownloadRunnable = new Runnable() {
            @Override
            public void run() {
                recursivelyDownloadSegmentsUsingURLRequest(request);
            }
        };
        BONetworkSegmentExecutorHelper.getInstance().postDelayedWithKey(boSegmentsRecursiveDownloadHandlerKey, recursiveDownloadRunnable, milliSeconds);
    }

    public HashMap<String, Object> segmentNetworkRequestDict() {
        HashMap<String, Object> funnelPullPayload = new HashMap<>();

        try {
            long lastUpdateTime = BOSharedPreferenceImpl.getInstance().getLong(BOCommonConstants.BO_ANALYTICS_SEGMENT_LAST_SYNC_TIME_DEFAULTS_KEY);

            BOAppSessionEvents sessionEvent = BOAppSessionEvents.getInstance();
            HashMap<String, Object> locationDict = sessionEvent.getGeoIPAndPublish(false);

            HashMap<String, Object> metaInfo = this.prepareMetaDataDict();
            HashMap<String, Object> funnelSyncGeo = new HashMap<>();
            if (locationDict != null && locationDict.keySet().size() > 0) {
                funnelSyncGeo.put("city", locationDict.get("city"));
                funnelSyncGeo.put("reg", locationDict.get("state"));
                funnelSyncGeo.put("couc", locationDict.get("country"));
                funnelSyncGeo.put("zip", locationDict.get("zip"));
                funnelSyncGeo.put("conc", locationDict.get("continentCode"));
            }

            HashMap<String, Object> events = new HashMap<>();
            funnelSyncGeo.put(BOCommonConstants.BO_LAST_UPDATED_TIME, lastUpdateTime);

            funnelPullPayload.put("geo", funnelSyncGeo);
            funnelPullPayload.put("meta", metaInfo);
            funnelPullPayload.put("events", events);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return funnelPullPayload;
    }

    private void segmentsUsingURLRequest(@Nullable String segmentsURLReq, @NonNull BOHandlerMessage callback) {

        if (!BlotoutAnalytics_Internal.getInstance().isSDKEnabled || requestInProgress) {
            return;
        }

        try {
            BOSegmentAPI api = BOSharedManager.getInstance().getAPIFactory().segmentAPI;
            Call<Object> call = api.getSegmentData(BOBaseAPI.getInstance().getSegmentPath(), this.segmentNetworkRequestDict());
            requestInProgress = true;
            Response response = call.execute();
            if (response.isSuccessful()) {
                //TODO: resp handling
                BOSegmentEvents segmentEvents = BOSegmentEvents.fromJsonDictionary((HashMap<String, Object>) response.body());
                if (segmentEvents.getSegments() != null && segmentEvents.getSegments().size() > 0) {
                    callback.handleMessage(new BOHandlerMessage.BOMessage(1, segmentEvents));
                } else {
                    callback.handleMessage(new BOHandlerMessage.BOMessage(0, null));
                }
            } else {
                callback.handleMessage(new BOHandlerMessage.BOMessage(0, null));
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            callback.handleMessage(new BOHandlerMessage.BOMessage(0, null));
        }
    }

    @NonNull
    public List<String> allRuleKeysToCheckForFromSegment(@NonNull List<BORule> rulesObj) {
        List<String> importantKeys = new ArrayList<>();
        try {
            for (BORule ruleX : rulesObj) {
                if (ruleX.getRules() != null && ruleX.getRules().size() > 0) {
                    List<String> keysFromInner = allRuleKeysToCheckForFromSegment(ruleX.getRules());
                    importantKeys.addAll(keysFromInner);
                } else {
                    if (ruleX.getKey() != null && !importantKeys.contains(ruleX.getKey())) {
                        importantKeys.add(ruleX.getKey());
                    }
                    if (ruleX.getEventName() != null && !importantKeys.contains(ruleX.getEventName())) {
                        importantKeys.add(mappedSystemEventName(ruleX.getEventName()));
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return importantKeys;
    }

    private boolean checkForFromSegmentRulesWithCondition(
            @NonNull List<BORule> rulesObj, @NonNull String condition, @NonNull HashMap<String, Object> jsonDict) {
        try {
            BOSegmentsExecutorHelper execHelper = BOSegmentsExecutorHelper.getInstance();

            List<Boolean> conditionalValue = new ArrayList<>();

            for (BORule rule : rulesObj) {
                if (rule.getSegmentID() != null && rule.getSegmentID().intValue() > 0) {
                    //find stored segment by segment ID and check for qualification
                    //[self doesUser:nil qualifiesForSegment:nil];
                } else if (rule.getRules() != null) {
                    boolean condResults = checkForFromSegmentRulesWithCondition(rule.getRules(), rule.getCondition(), jsonDict);
                    conditionalValue.add(condResults);

                } else if (rule.getKey() != null && !rule.getKey().equals("") && rule.getValue() != null && rule.getValue().size() > 0) {
                    ArrayList<Object> objectList = new ArrayList<Object>(rule.getValue());
                    boolean isTrue = execHelper.doesKeyConatainsValues(rule.getKey(), objectList, rule.getOperatorKey(), jsonDict, null);
                    execHelper.resetSettings();
                    conditionalValue.add(isTrue);
                } else if (rule.getEventName() != null && !rule.getEventName().equals("")) {
                    ArrayList<Object> objectList = null;
                    if (rule.getValue() != null) {
                        objectList = new ArrayList<Object>(rule.getValue());
                    }
                    long operatorKey = 0;
                    if (rule.getOperatorKey() != null) {
                        operatorKey = rule.getOperatorKey();
                    }

                    boolean isTrue = execHelper.doesKeyConatainsValues(rule.getKey(), objectList, operatorKey, jsonDict, mappedSystemEventName(rule.getEventName()));
                    execHelper.resetSettings();
                    conditionalValue.add(isTrue);
                }
            }

            boolean bitwiseResult = false;
            if (conditionalValue.size() > 1) {
                boolean bitwiseResult1 = conditionalValue.get(0);
                boolean bitwiseResult2 = conditionalValue.get(1);
                bitwiseResult = execHelper.resultsOfBitwiseOperator(condition, bitwiseResult1, bitwiseResult2);
                for (int ind = 2; ind < conditionalValue.size(); ind++) {
                    //Need parent object "condition":"AND" to pass below and test
                    bitwiseResult = execHelper.resultsOfBitwiseOperator(condition, bitwiseResult, conditionalValue.get(ind));
                }
            } else {
                if (conditionalValue.size() > 0)
                    bitwiseResult = conditionalValue.get(conditionalValue.size() - 1);
            }
            return bitwiseResult;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return false;
    }

    //UserID is symbolic reprentation for any user qualification, for now pass nil
    public boolean doesUserQualifiesForSegment(@NonNull BOSegment segmentInfo,BOAppSessionDataModel sessionDataModel, BOHandlerMessage callback) {

        try {
            boolean qualificationResult = false;
            boolean userID = false;
            if (userID) {
                //Fetch user data for qualification
            } else {

                HashMap<String, Object> syncedLFTFilesResults = performTestOnSynedLifeTimeFilesForSegment(segmentInfo,sessionDataModel);
                HashMap<String, Object> nonSyncedLFTFilesResults = performTestOnNonSynedLifeTimeFilesForSegment(segmentInfo,sessionDataModel);
                HashMap<String, Object> syncedSessFilesResults = performTestOnSynedSessionFilesForSegment(segmentInfo,sessionDataModel);
                HashMap<String, Object> nonSyncedSessFilesResults = performTestOnNonSynedSessionFilesForSegment(segmentInfo,sessionDataModel);
                HashMap<String, Object> currentDaySessionData = performTestOnCurrentDaySessionDataForSegment(segmentInfo,sessionDataModel);

                List<String> allImpKeys = allRuleKeysToCheckForFromSegment(segmentInfo.getRuleset().getRules());

                boolean syncedLFTCheck1 = false;
                if (syncedLFTFilesResults != null && syncedLFTFilesResults.keySet().size() == allImpKeys.size()) {
                    for (String keyName : syncedLFTFilesResults.keySet()) {
                        //Don't loop for every key and it's JSON objects. For Segment to qualify all keys should be present in single JSON
                        HashMap<String, Object> foundJSONForKey = (HashMap<String, Object>) syncedLFTFilesResults.get(keyName);
                        List<HashMap<String, Object>> allJSONsContsKey = (List<HashMap<String, Object>>) foundJSONForKey.get("inDict");

                        //Build logic to check for segment qualification using partial JSON, what that mean is, scenario below:
                        // 1: key1 found in JSON 1, key2 Found in JSON 2, Key3 found in JOSN 3 ans Key4 found in JSON 4
                        // In current logic it won't quality as it will check all keys logic in sinle JSON, that's why current loop is waste of processing.
                        // For all keys in single JSON, find common JSONs and test the logic.
                        // For partial JSON, build logic to combine from partial data, not yet in place.

                        //Corretion one1: Looping logic correction for performance improvement
                        //Corretion two2: Partial keys in single JSON and combine result for qualification.
                        //difficulty is find relevent keys in single JSON. like key1, key2, key3, key4, key5,key6... now key1 and key 2 has to be present in single JSON and key3 and key4 in single, if this combination is not found then not quaified or find alternate again

                        for (HashMap<String, Object> jsonDict : allJSONsContsKey) {
                            //This logic is good when all keys are found in single JSON object then we don't need to loop as per above logic
                            //If keys are found in distributed set of JSON, one in this week data and another in another day or week JSON then below check will fail.
                            //Need to find solution
                            //Also build logic to store segment ID based JSON store, so that we don't loop again and again, check for fresh data only
                            syncedLFTCheck1 = checkForFromSegmentRulesWithCondition(segmentInfo.getRuleset().getRules(), segmentInfo.getRuleset().getCondition(), jsonDict);
                            if (syncedLFTCheck1) {
                                break;
                            }
                        }
                        if (syncedLFTCheck1) {
                            break;
                        }
                    }
                }
                boolean syncedLFTCheck2 = false;
                if (nonSyncedLFTFilesResults != null && nonSyncedLFTFilesResults.keySet().size() == allImpKeys.size()) {
                    for (String keyName : nonSyncedLFTFilesResults.keySet()) {
                        HashMap<String, Object> foundJSONForKey = (HashMap<String, Object>) nonSyncedLFTFilesResults.get(keyName);
                        List<HashMap<String, Object>> allJSONsContsKey = (List<HashMap<String, Object>>) foundJSONForKey.get("inDict");

                        for (HashMap<String, Object> jsonDict : allJSONsContsKey) {
                            syncedLFTCheck2 = checkForFromSegmentRulesWithCondition(segmentInfo.getRuleset().getRules(), segmentInfo.getRuleset().getCondition(), jsonDict);
                            if (syncedLFTCheck2) {
                                break;
                            }
                        }
                        if (syncedLFTCheck2) {
                            break;
                        }
                    }
                }

                boolean syncedLFTCheck3 = false;
                if (syncedSessFilesResults != null && syncedSessFilesResults.keySet().size() == allImpKeys.size()) {
                    for (String keyName : syncedSessFilesResults.keySet()) {
                        HashMap<String, Object> foundJSONForKey = (HashMap<String, Object>) syncedSessFilesResults.get(keyName);
                        List<HashMap<String, Object>> allJSONsContsKey = (List<HashMap<String, Object>>) foundJSONForKey.get("inDict");

                        for (HashMap<String, Object> jsonDict : allJSONsContsKey) {
                            syncedLFTCheck3 = checkForFromSegmentRulesWithCondition(segmentInfo.getRuleset().getRules(), segmentInfo.getRuleset().getCondition(), jsonDict);
                            if (syncedLFTCheck3) {
                                break;
                            }
                        }
                        if (syncedLFTCheck3) {
                            break;
                        }
                    }
                }
                boolean syncedLFTCheck4 = false;
                if (nonSyncedSessFilesResults != null && nonSyncedSessFilesResults.keySet().size() == allImpKeys.size()) {
                    for (String keyName : nonSyncedSessFilesResults.keySet()) {
                        HashMap<String, Object> foundJSONForKey = (HashMap<String, Object>) nonSyncedSessFilesResults.get(keyName);
                        List<HashMap<String, Object>> allJSONsContsKey = (List<HashMap<String, Object>>) foundJSONForKey.get("inDict");
                        for (HashMap<String, Object> jsonDict : allJSONsContsKey) {
                            syncedLFTCheck4 = checkForFromSegmentRulesWithCondition(segmentInfo.getRuleset().getRules(), segmentInfo.getRuleset().getCondition(), jsonDict);
                            if (syncedLFTCheck4) {
                                break;
                            }
                        }
                        if (syncedLFTCheck4) {
                            break;
                        }
                    }
                }
                boolean syncedLFTCheck5 = false;
                if (currentDaySessionData != null && currentDaySessionData.keySet().size() == allImpKeys.size()) {
                    for (String keyName : currentDaySessionData.keySet()) {
                        HashMap<String, Object> foundJSONForKey = (HashMap<String, Object>) currentDaySessionData.get(keyName);
                        List<HashMap<String, Object>> allJSONsContsKey = (List<HashMap<String, Object>>) foundJSONForKey.get("inDict");
                        for (HashMap<String, Object> jsonDict : allJSONsContsKey) {
                            syncedLFTCheck5 = checkForFromSegmentRulesWithCondition(segmentInfo.getRuleset().getRules(), segmentInfo.getRuleset().getCondition(), jsonDict);
                            if (syncedLFTCheck5) {
                                break;
                            }
                        }
                        if (syncedLFTCheck5) {
                            break;
                        }
                    }
                }

                if (syncedLFTCheck1 || syncedLFTCheck2 || syncedLFTCheck3 || syncedLFTCheck4 || syncedLFTCheck5) {
                    qualificationResult = true;
                }
            }

            if (!qualificationResult) {

                Runnable userQualifyHandlerRunnable = new Runnable() {
                    @Override
                    public void run() {
                        boolean doesQualify = doesUserQualifiesForSegment(segmentInfo,sessionDataModel, callback);
                        if (doesQualify) {
                            callback.handleMessage(new BOHandlerMessage.BOMessage(1, segmentInfo));
                            BONetworkSegmentExecutorHelper.getInstance().removeCallbackForKey(boSegmentsUserQualifyHandlerKey);
                        }
                    }
                };
                BONetworkSegmentExecutorHelper.getInstance().postDelayedWithKey(boSegmentsUserQualifyHandlerKey, userQualifyHandlerRunnable, 10);
            }
            return qualificationResult;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            callback.handleMessage(new BOHandlerMessage.BOMessage(0, null));
        }
        return false;
    }

    @NonNull
    private HashMap<String, Object> performTestOnNonSynedSessionFilesForSegment(@NonNull BOSegment segmentInfo, BOAppSessionDataModel sessionDataModel) {
        HashMap<String, Object> keyCheckedResult = new HashMap<String, Object>();

        try {
            String notSyncedPathSessionTime = BOFileSystemManager.getInstance().getNotSyncedFilesSessionTimeEventsDirectoryPath();
            List<String> allNotSyncedFilesSessionTime = BOFileSystemManager.getInstance().getAllFilesWithExtension(notSyncedPathSessionTime, "txt");

            BOSegmentsExecutorHelper execHelper = BOSegmentsExecutorHelper.getInstance();
            List<String> allImportantKeys = allRuleKeysToCheckForFromSegment(segmentInfo.getRuleset().getRules());

            //NSMutableDictionary *keyInDictRelation = [NSMutableDictionary dictionary];

            for (String filePath : allNotSyncedFilesSessionTime) {
                //NSString *jsonString = [NSString stringWithContentsOfFile:filePath encoding:NSUnicodeStringEncoding error:&fileReadError]
                String jsonString = null;
                HashMap<String, Object> fileJSONDict = null;
                try {
                    jsonString = BOFileSystemManager.getInstance().readContentOfFileAtPath(filePath);
                } catch (Exception e) {
                }
                if (jsonString != null) {
                    fileJSONDict = BOCommonUtils.getHashmapFromJsonString(jsonString);

                    for (String keyName : allImportantKeys) {
                        boolean isFound = execHelper.isKeyFoundIn(keyName, fileJSONDict);
                        if (isFound) {
                            if (keyCheckedResult.keySet().contains(keyName)) {
                                HashMap<String, Object> existingDict = (HashMap<String, Object>) keyCheckedResult.get(keyName);

                                List<Object> exsitingDictArr = (List<Object>) existingDict.get("inDict");
                                exsitingDictArr.add(fileJSONDict);
                                existingDict.put("inDict", exsitingDictArr);

                                List<String> exsitingPathArr = (List<String>) existingDict.get("filePath");
                                exsitingPathArr.add(filePath);
                                existingDict.put("filePath", exsitingPathArr);

                                keyCheckedResult.put(keyName, existingDict);

                            } else {
                                HashMap<String, Object> keyResultDict = new HashMap<>();

                                keyResultDict.put("isFound", isFound);

                                List<HashMap<String, Object>> jsonList = new ArrayList<>();
                                jsonList.add(fileJSONDict);
                                keyResultDict.put("inDict", jsonList);

                                List<String> fileList = new ArrayList<String>();
                                fileList.add(filePath);
                                keyResultDict.put("filePath", fileList);

                                keyCheckedResult.put(keyName, keyResultDict);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return keyCheckedResult;
    }

    @NonNull
    private HashMap<String, Object> performTestOnSynedSessionFilesForSegment(@NonNull BOSegment segmentInfo, BOAppSessionDataModel sessionDataModel) {
        HashMap<String, Object> keyCheckedResult = new HashMap<String, Object>();

        try {
            String syncedPathSessionTime = BOFileSystemManager.getInstance().getSyncedFilesSessionTimeEventsDirectoryPath();
            List<String> allSyncedFilesSessionTime = BOFileSystemManager.getInstance().getAllFilesWithExtension(syncedPathSessionTime, "txt");

            BOSegmentsExecutorHelper execHelper = BOSegmentsExecutorHelper.getInstance();
            List<String> allImportantKeys = allRuleKeysToCheckForFromSegment(segmentInfo.getRuleset().getRules());

            //NSMutableDictionary *keyInDictRelation = [NSMutableDictionary dictionary];

            for (String filePath : allSyncedFilesSessionTime) {
                //NSString *jsonString = [NSString stringWithContentsOfFile:filePath encoding:NSUnicodeStringEncoding error:&fileReadError]
                String jsonString = null;
                HashMap<String, Object> fileJSONDict = null;
                try {
                    jsonString = BOFileSystemManager.getInstance().readContentOfFileAtPath(filePath);
                } catch (Exception e) {
                }
                if (jsonString != null) {
                    fileJSONDict = BOCommonUtils.getHashmapFromJsonString(jsonString);

                    for (String keyName : allImportantKeys) {
                        boolean isFound = execHelper.isKeyFoundIn(keyName, fileJSONDict);
                        if (isFound) {
                            if (keyCheckedResult.keySet().contains(keyName)) {
                                HashMap<String, Object> existingDict = (HashMap<String, Object>) keyCheckedResult.get(keyName);

                                List<Object> exsitingDictArr = (List<Object>) existingDict.get("inDict");
                                exsitingDictArr.add(fileJSONDict);
                                existingDict.put("inDict", exsitingDictArr);

                                List<String> exsitingPathArr = (List<String>) existingDict.get("filePath");
                                exsitingPathArr.add(filePath);
                                existingDict.put("filePath", exsitingPathArr);

                                keyCheckedResult.put(keyName, existingDict);

                            } else {
                                HashMap<String, Object> keyResultDict = new HashMap<>();

                                keyResultDict.put("isFound", isFound);

                                List<HashMap<String, Object>> jsonList = new ArrayList<>();
                                jsonList.add(fileJSONDict);
                                keyResultDict.put("inDict", jsonList);

                                List<String> fileList = new ArrayList<String>();
                                fileList.add(filePath);
                                keyResultDict.put("filePath", fileList);

                                keyCheckedResult.put(keyName, keyResultDict);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return keyCheckedResult;
    }

    @NonNull
    private HashMap<String, Object> performTestOnNonSynedLifeTimeFilesForSegment(@NonNull BOSegment segmentInfo, BOAppSessionDataModel sessionDataModel) {
        HashMap<String, Object> keyCheckedResult = new HashMap<String, Object>();

        try {
            String notSyncedPathLifeTime = BOFileSystemManager.getInstance().getNotSyncedFilesLifeTimeEventsDirectoryPath();
            List<String> allNotSyncedFilesLifeTime = BOFileSystemManager.getInstance().getAllFilesWithExtension(notSyncedPathLifeTime, "txt");

            BOSegmentsExecutorHelper execHelper = BOSegmentsExecutorHelper.getInstance();
            List<String> allImportantKeys = allRuleKeysToCheckForFromSegment(segmentInfo.getRuleset().getRules());

            for (String filePath : allNotSyncedFilesLifeTime) {
                //NSString *jsonString = [NSString stringWithContentsOfFile:filePath encoding:NSUnicodeStringEncoding error:&fileReadError]
                String jsonString = null;
                HashMap<String, Object> fileJSONDict = null;
                try {
                    jsonString = BOFileSystemManager.getInstance().readContentOfFileAtPath(filePath);
                } catch (Exception e) {
                }
                if (jsonString != null) {
                    fileJSONDict = BOCommonUtils.getHashmapFromJsonString(jsonString);

                    for (String keyName : allImportantKeys) {
                        boolean isFound = execHelper.isKeyFoundIn(keyName, fileJSONDict);
                        if (isFound) {
                            if (keyCheckedResult.keySet().contains(keyName)) {
                                HashMap<String, Object> existingDict = (HashMap<String, Object>) keyCheckedResult.get(keyName);

                                List<Object> exsitingDictArr = (List<Object>) existingDict.get("inDict");
                                exsitingDictArr.add(fileJSONDict);
                                existingDict.put("inDict", exsitingDictArr);

                                List<String> exsitingPathArr = (List<String>) existingDict.get("filePath");
                                exsitingPathArr.add(filePath);
                                existingDict.put("filePath", exsitingPathArr);

                                keyCheckedResult.put(keyName, existingDict);

                            } else {
                                HashMap<String, Object> keyResultDict = new HashMap<>();

                                keyResultDict.put("isFound", isFound);

                                List<HashMap<String, Object>> jsonList = new ArrayList<>();
                                jsonList.add(fileJSONDict);
                                keyResultDict.put("inDict", jsonList);

                                List<String> fileList = new ArrayList<String>();
                                fileList.add(filePath);
                                keyResultDict.put("filePath", fileList);

                                keyCheckedResult.put(keyName, keyResultDict);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return keyCheckedResult;
    }

    @NonNull
    private HashMap<String, Object> performTestOnSynedLifeTimeFilesForSegment(@NonNull BOSegment segmentInfo, BOAppSessionDataModel sessionDataModel) {
        HashMap<String, Object> keyCheckedResult = new HashMap<String, Object>();
        try {
            String syncedPathLifeTime = BOFileSystemManager.getInstance().getSyncedFilesLifeTimeEventsDirectoryPath();
            List<String> allSyncedFilesLifeTime = BOFileSystemManager.getInstance().getAllFilesWithExtension(syncedPathLifeTime, "txt");

            BOSegmentsExecutorHelper execHelper = BOSegmentsExecutorHelper.getInstance();
            List<String> allImportantKeys = allRuleKeysToCheckForFromSegment(segmentInfo.getRuleset().getRules());

            for (String filePath : allSyncedFilesLifeTime) {
                //NSString *jsonString = [NSString stringWithContentsOfFile:filePath encoding:NSUnicodeStringEncoding error:&fileReadError]
                String jsonString = null;
                HashMap<String, Object> fileJSONDict = null;
                try {
                    jsonString = BOFileSystemManager.getInstance().readContentOfFileAtPath(filePath);
                } catch (Exception e) {
                }
                if (jsonString != null) {
                    fileJSONDict = BOCommonUtils.getHashmapFromJsonString(jsonString);

                    for (String keyName : allImportantKeys) {
                        boolean isFound = execHelper.isKeyFoundIn(keyName, fileJSONDict);
                        boolean isEventNameFound = execHelper.isKeyFoundIn(keyName, fileJSONDict);
                        if (isFound) {
                            if (keyCheckedResult.keySet().contains(keyName)) {
                                HashMap<String, Object> existingDict = (HashMap<String, Object>) keyCheckedResult.get(keyName);

                                List<Object> exsitingDictArr = (List<Object>) existingDict.get("inDict");
                                exsitingDictArr.add(fileJSONDict);
                                existingDict.put("inDict", exsitingDictArr);

                                List<String> exsitingPathArr = (List<String>) existingDict.get("filePath");
                                exsitingPathArr.add(filePath);
                                existingDict.put("filePath", exsitingPathArr);

                                keyCheckedResult.put(keyName, existingDict);

                            } else {
                                HashMap<String, Object> keyResultDict = new HashMap<>();

                                keyResultDict.put("isFound", isFound);

                                List<HashMap<String, Object>> jsonList = new ArrayList<>();
                                jsonList.add(fileJSONDict);
                                keyResultDict.put("inDict", jsonList);

                                List<String> fileList = new ArrayList<String>();
                                fileList.add(filePath);
                                keyResultDict.put("filePath", fileList);

                                keyCheckedResult.put(keyName, keyResultDict);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return keyCheckedResult;
    }

    private HashMap<String, Object> performTestOnCurrentDaySessionDataForSegment(@NonNull BOSegment segmentInfo, BOAppSessionDataModel sessionDataModel) {
        HashMap<String, Object> keyCheckedResult = new HashMap<>();

        try {
            BOAppSessionDataModel currentDaySessionObj = null;
            if(sessionDataModel == null) {
                currentDaySessionObj = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null);
            } else {
                currentDaySessionObj = sessionDataModel;
            }

            HashMap<String, Object> currentDaySessionDictObj = BOCommonUtils.getHashmapFromJsonString(BOAppSessionDataModel.toJsonString(currentDaySessionObj));

            BOSegmentsExecutorHelper execHelper = BOSegmentsExecutorHelper.getInstance();
            List<String> allImportantKeys = allRuleKeysToCheckForFromSegment(segmentInfo.getRuleset().getRules());

            //NSMutableDictionary *keyInDictRelation = [NSMutableDictionary dictionary];

            //    NSString *jsonString = [BOAUtilities jsonStringFrom:currentDaySessionDictObj withPrettyPrint:YES];
            //
            ////    NSString *jsonString = [BOFFileSystemManager contentOfFileAtPath:filePath withEncoding:NSUnicodeStringEncoding andError:&fileReadError];
            //    NSDictionary *fileJSONDict = jsonString ? [BOAUtilities jsonObjectFromString:jsonString] : nil;

            for (String keyName : allImportantKeys) {
                boolean isFound = execHelper.isKeyFoundIn(keyName, currentDaySessionDictObj);
                if (isFound) {
                    if (keyCheckedResult.keySet().contains(keyName)) {
                        HashMap<String, Object> existingDict = (HashMap<String, Object>) keyCheckedResult.get(keyName);

                        List<Object> exsitingDictArr = (List<Object>) existingDict.get("inDict");
                        exsitingDictArr.add(currentDaySessionDictObj);
                        existingDict.put("inDict", exsitingDictArr);

                        //                NSMutableArray *exsitingPathArr = [existingDict objectForKey:@"filePath"];
                        //                [exsitingPathArr addObject:filePath];
                        //                [existingDict setObject:exsitingPathArr forKey:@"filePath"];

                        keyCheckedResult.put(keyName, existingDict);
                    } else {
                        HashMap<String, Object> keyResultDict = new HashMap<>();

                        keyResultDict.put("isFound", isFound);

                        List<HashMap<String, Object>> jsonList = new ArrayList<>();
                        jsonList.add(currentDaySessionDictObj);
                        keyResultDict.put("inDict", jsonList);

                        List<String> fileList = new ArrayList<String>();
                        keyResultDict.put("filePath", fileList);

                        keyCheckedResult.put(keyName, keyResultDict);
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return keyCheckedResult;
    }

    public void recordDevEventSubCodeWithDetail(String eventName, long eventSubCode, HashMap eventDetails) {

    }

    public void recordNavigationEventFrom(String fromVC, String toVC, HashMap<String, Object> eventDetails) {

    }


    public void appLaunchedWithInfo(HashMap<String, Object> launchInfo) {

    }

    public void appInBackgroundWithInfo(HashMap<String, Object> backgroudInfo) {

    }

    public void appWillTerminatWithInfo(HashMap<String, Object> terminationInfo) {

    }

    public String mappedSystemEventName(String key) {

        HashMap<String,String> eventMap = systemEventsMapping();
        if(eventMap.containsKey(key)) {
          return eventMap.get(key);
        }
        return key;

    }

    public HashMap<String,String> systemEventsMapping() {

        if(eventMap == null) {
            eventMap = new HashMap<String,String>();

            eventMap.put("App Launched", "appLaunched");
            eventMap.put("App Terminate", "appResignActive");
            eventMap.put("Session Start", "appLaunched");
            eventMap.put("Session End", "appResignActive");

            eventMap.put("App Background", "appInBackground");
            eventMap.put("App Foreground", "appInForeground");
            eventMap.put("Notification Received", "appNotificationReceived");
            eventMap.put("Notification Viewed", "appNotificationViewed");
            eventMap.put("Notification Clicked", "appNotificationClicked");
            eventMap.put("Portrait Orientation", "appOrientationPortrait");
            eventMap.put("Landscape Orientation", "appOrientationLandscape");

            eventMap.put("App Installed", "");
            eventMap.put("App Uninstalled", "");
            eventMap.put("Click/Tap", "");
            eventMap.put("Double Click/Double Tap", "");
            eventMap.put("View", "");
            eventMap.put("AppInstall Referrer", "");
        }
        return eventMap;
    }
}
