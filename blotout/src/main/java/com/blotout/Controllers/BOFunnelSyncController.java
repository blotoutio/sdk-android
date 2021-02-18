package com.blotout.Controllers;

import com.blotout.analytics.BOHandlerMessage;
import com.blotout.eventsExecutor.BONetworkFunnelExecutorHelper;
import com.blotout.analytics.BOSharedManager;
import com.blotout.analytics.BlotoutAnalytics_Internal;
import com.blotout.constants.BOCommonConstants;
import com.blotout.events.BOAppSessionEvents;
import com.blotout.events.BOCommonEvents;
import com.blotout.model.FunnelAndCodifiedEvents.FunnelAndCodifiedEventsGetRes.BOEventList;
import com.blotout.model.FunnelAndCodifiedEvents.FunnelAndCodifiedEventsGetRes.BOEventsFunnel;
import com.blotout.model.FunnelAndCodifiedEvents.FunnelAndCodifiedEventsGetRes.BOFunnelAndCodifiedEvents;
import com.blotout.model.FunnelAndCodifiedEvents.FunnelPayload.BOFunnelEvent;
import com.blotout.model.FunnelAndCodifiedEvents.FunnelPayload.BOFunnelGeo;
import com.blotout.model.FunnelAndCodifiedEvents.FunnelPayload.BOFunnelMeta;
import com.blotout.model.FunnelAndCodifiedEvents.FunnelPayload.BOFunnelPayload;
import com.blotout.model.session.BOAppSessionDataModel;
import com.blotout.network.api.BOBaseAPI;
import com.blotout.network.api.BOFunnelAPI;
import com.blotout.storage.BOFileSystemManager;
import com.blotout.storage.BOSharedPreferenceImpl;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.BODateTimeUtils;
import com.blotout.utilities.BOServerDataConverter;
import com.blotout.utilities.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Response;

import static com.blotout.constants.BOCommonConstants.BO_ANALYTICS_FUNNEL_LAST_SYNC_TIME_DEFAULTS_KEY;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Blotout on 07,December,2019
 */


public class BOFunnelSyncController {

    private static final String TAG = "BOFunnelSyncController";
    private static boolean isAggregate = true;

    @Nullable
    private BOFunnelAndCodifiedEvents funnelsAndCodifiedEventsInstance;
    private List<BOFunnelEvent> payloadEvents;
    private static final String boFunnelRecursiveDownloadHandlerKey = "boFunnelRecursiveDownloadHandlerKey";

    private long launchTimeStamp;
    private long terminationTimeStamp;
    private List<String> eventSequenceOrder;
    private List<Long> eventSubCodeSequenceOrder;

    private HashMap<String, List<Long>> eventsInfo;
    private HashMap<String, Long> preEventsInfo;
    private HashMap<String, Long> eventsSubCode;
    private HashMap<String, List<Long>> eventsDuration;

    private boolean requestInProgress = false;

    private static volatile BOFunnelSyncController instance;

    private BOFunnelSyncController() {
        this.eventsInfo = new HashMap<>();
        this.preEventsInfo = new HashMap<>();
        this.eventsSubCode = new HashMap<>();
        this.eventsDuration = new HashMap<>();
        this.eventSequenceOrder = new ArrayList<>();
        this.eventSubCodeSequenceOrder = new ArrayList<>();

        funnelsAndCodifiedEventsInstance = null;

        payloadEvents = new ArrayList<>();

    }

    public static BOFunnelSyncController getInstance() {

        if (instance == null) { //if there is no instance available... create new one
            synchronized (BOFunnelSyncController.class) {
                if (instance == null) {
                    instance = new BOFunnelSyncController();
                }
            }
        }
        return instance;
    }

    public void prepareFunnelSyncAndAnalyser() {
        try {

            if(!BlotoutAnalytics_Internal.getInstance().isFunnelEventsEnabled)
                return;

            if (funnelsAndCodifiedEventsInstance == null) {
                funnelsAndCodifiedEventsInstance = this.loadAllActiveFunnels();
            }

            loadFunnelNetworkScheduler();

            //TODO: Check logic and verify, only concern atm is, network is aync and this won't wait for new funnel rather move to load existing one.
            if (funnelsAndCodifiedEventsInstance != null && funnelsAndCodifiedEventsInstance.getEventsFunnel() != null) {
                for (int indx = 0; indx < funnelsAndCodifiedEventsInstance.getEventsFunnel().size(); indx++) {
                    BOEventsFunnel funnelEvent = funnelsAndCodifiedEventsInstance.getEventsFunnel().get(indx);

                    boolean isNewFunnelToLoad = true;
                    for (BOFunnelEvent fTestIDEvent : payloadEvents) {
                        if (fTestIDEvent.getIdentifier().equals(funnelEvent.getIdentifier())) {
                            isNewFunnelToLoad = false;
                            break;
                        }
                    }

                    if (isNewFunnelToLoad) {

                        BOFunnelEvent funnelPayloadEvent = new BOFunnelEvent();

                        funnelPayloadEvent.setIdentifier(funnelEvent.getIdentifier());
                        funnelPayloadEvent.setVersion(funnelEvent.getVersion());
                        funnelPayloadEvent.setName(funnelEvent.getName());
                        funnelPayloadEvent.setEventTime(BODateTimeUtils.get13DigitNumberObjTimeStamp());
                        funnelPayloadEvent.setDayOfAnalysis(BODateTimeUtils.getStringFromDate(new Date(), BOCommonConstants.BO_DATE_FORMAT));
                        funnelPayloadEvent.setDaySessionCount(1);
                        //Setting here will make timeStamp worth else no meaning like recording and updating at when app goes in background
                        funnelPayloadEvent.setMessageID(BOCommonUtils.generateMessageIDForEvent(funnelEvent.getName(), funnelEvent.getIdentifier(), BODateTimeUtils.get13DigitNumberObjTimeStamp()));
                        funnelPayloadEvent.setISADayEvent(false);
                        funnelPayloadEvent.setIsTraversed(false);
                        funnelPayloadEvent.setDayTraversedCount(0);

                        //Just to make sure array index beyond bound never happen when values are filled
                        List<Long> fVisits = new ArrayList();
                        for (int initIndx = 0; initIndx < funnelEvent.getEventList().size(); initIndx++) {
                            fVisits.add(Long.valueOf(0));
                        }
                        funnelPayloadEvent.setVisits(fVisits);

                        //Just to make sure array index beyond bound never happen when values are filled
                        List<Long> fNavigationTime = new ArrayList<>();
                        for (int initIndx = 0; initIndx < funnelEvent.getEventList().size(); initIndx++) {
                            fNavigationTime.add(Long.valueOf(0));
                        }
                        funnelPayloadEvent.setNavigationTime(fNavigationTime);

                        funnelPayloadEvent.setUserReferral(false);
                        funnelPayloadEvent.setUserTraversedCount(0);
                        funnelPayloadEvent.setPrevTraversalDay(null);

                        payloadEvents.add(funnelPayloadEvent);
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    @Nullable
    private BOFunnelAndCodifiedEvents loadAllActiveFunnels() {
        String fileExtention = "txt";
        BOFunnelAndCodifiedEvents funnelsAndCodifiedEvents = null;

        try {
            String allFunnelsDirPath = BOFileSystemManager.getInstance().getAllFunnelsToAnalyseDirectoryPath();
            String allFunnelsFilePath = allFunnelsDirPath + "/" + "AllFunnels" + "." + fileExtention;

            String jsonString = null;
            try {
                jsonString = BOFileSystemManager.getInstance().readContentOfFileAtPath(allFunnelsFilePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (jsonString != null && !jsonString.equals("")) {
                HashMap<String, Object> mapJson = BOCommonUtils.getHashmapFromJsonString(jsonString);
                funnelsAndCodifiedEvents = BOFunnelAndCodifiedEvents.fromJsonDictionary(mapJson);
            }
            return funnelsAndCodifiedEvents;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return funnelsAndCodifiedEvents;
    }

    @Nullable
    private HashMap<String, Object> prepareMetaDataDict(BOAppSessionDataModel sessionData) {
        try {
                HashMap<String, Object> metaDatas = BOServerDataConverter.prepareMetaData();
                if (metaDatas != null) {
                    return new HashMap<>(metaDatas);
                } else {
                    return new HashMap<>();
                }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return new HashMap<>();
        }
    }

    @Nullable
    private BOFunnelMeta prepareMetaData(BOAppSessionDataModel sessionData) {
        try {
            HashMap<String, Object> metaDataDict = this.prepareMetaDataDict(sessionData);
            return BOFunnelMeta.fromJsonDictionary(metaDataDict);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    @Nullable
    private BOFunnelGeo prepareGeoData() {
        try {
            HashMap<String, Object> metaDataDict = this.prepareGeoDataDict();
            return BOFunnelGeo.fromJsonDictionary(metaDataDict);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    @Nullable
    private HashMap<String, Object> prepareGeoDataDict() {
        try {
                HashMap<String, Object> geoDatas = BOServerDataConverter.prepareGeoData();
                if (geoDatas != null) {
                    return new HashMap<>(geoDatas);

                } else {
                    return new HashMap<>();
                }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return new HashMap<>();
        }
    }

    private void checkAndUpdateTraversalCompleteForFunnels() {
        try {
            if (funnelsAndCodifiedEventsInstance != null && funnelsAndCodifiedEventsInstance.getEventsFunnel() != null && funnelsAndCodifiedEventsInstance.getEventsFunnel().size() > 0) {
                for (int gIndx = 0; gIndx < funnelsAndCodifiedEventsInstance.getEventsFunnel().size(); gIndx++) {

                    BOEventsFunnel funnelEvent = funnelsAndCodifiedEventsInstance.getEventsFunnel().get(gIndx);
                    BOFunnelEvent funnelPayloadEvent = payloadEvents.get(gIndx);
                    if (funnelPayloadEvent != null) {
                        int funnelEventsCount = (int) funnelEvent.getEventList().size();

                        List<String> eventOccuredSoFar = this.eventSequenceOrder;
                        List<String> allFunnelEventsName = new ArrayList();

                        List<Long> eventSubCodeOccuredSoFar = this.eventSubCodeSequenceOrder;
                        List<Long> allFunnelEventsSubCode = new ArrayList();

                        boolean isTraversed = false;
                        int traversedCount = 0;

                        //V1.0 solution, discuss about improvements once featuers are ready
                        //funnel events, sequence example = [A,B,A,A,B,C,B,A,A,C,B,A,A,B,C,D,E,C,B,A];
                        //event A indices=[0,2,3,7,8,11,12,19]
                        //event B indices=[1,4,6,10,13,18]
                        //event C indices=[5,9,14,17]
                        //event D indices=[15]

                        //As funnel forward sequence must start with A, so create arrays with funnel events count startng indexes of A
                        //Test all array for name match with extaxt sequence and if yes then traversed else not
                        for (int indx = 0; indx < funnelEventsCount; indx++) {
                            BOEventList event = funnelEvent.getEventList().get(indx);
                            if (event != null && event.getEventName() != null && event.getEventCategorySubtype() != null) {
                                allFunnelEventsName.add(event.getEventName());
                                allFunnelEventsSubCode.add(Long.valueOf(event.getEventCategorySubtype()));
                            }
                        }

                        Set<String> allIndexesOfFirstEvent = new HashSet<>(eventOccuredSoFar);
                        allIndexesOfFirstEvent.retainAll(allFunnelEventsName);

                        Set<Long> allIndexesOfFirstSubCodeEvent = new HashSet<>(eventSubCodeOccuredSoFar);
                        allIndexesOfFirstSubCodeEvent.retainAll(allFunnelEventsSubCode);

                        List<List> eventsGroupArr = new ArrayList<>();

                        for (int idx = 0; idx < eventOccuredSoFar.size(); idx++) {
                            int fEventCount = funnelEventsCount;
                            // 2 3 4  3 2 1
                            List eventsGroupOfCount = new ArrayList();
                            for (int indx = idx; fEventCount >= 1; fEventCount--) {
                                eventsGroupOfCount.add(eventOccuredSoFar.get(indx));
                                indx++;
                                if (indx >= eventOccuredSoFar.size()) {
                                    break;
                                }
                            }
                            eventsGroupArr.add(eventsGroupOfCount);
                        }

                        for (int indxG = 0; indxG < eventsGroupArr.size(); indxG++) {
                            List eventsGrouped = eventsGroupArr.get(indxG);
                            int totalNameMatch = 0;
                            boolean traversalCheck = false;
                            //Test
                            if (eventsGrouped.size() == allFunnelEventsName.size()) {
                                for (int yndx = 0; yndx < eventsGrouped.size(); yndx++) {
                                    String eventNameOccured = (String) eventsGrouped.get(yndx);
                                    String eventFunnelName =  allFunnelEventsName.get(yndx);
                                    if (eventNameOccured != null && eventNameOccured.equals(eventFunnelName)) {
                                        totalNameMatch = totalNameMatch + 1;
                                        if (totalNameMatch == eventsGrouped.size()) {
                                            isTraversed = true;
                                            traversalCheck = true;
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            } else {
                                Logger.INSTANCE.e(TAG, "Funnel Test: Event group count not matching");
                            }

                            if (traversalCheck) {
                                traversedCount = traversedCount + 1;
                            }
                        }
                        funnelPayloadEvent.setIsTraversed(isTraversed);
                        funnelPayloadEvent.setDayTraversedCount(traversedCount);

                        //This is update only operation so cases like payloadEvents with 0 objects in it and first insert operation happening here must not be the case
                        payloadEvents.add(gIndx, funnelPayloadEvent);
                        //Remove the old one & as per behaviour mentione here
                        //https://developer.apple.com/documentation/foundation/nsmutablearray/1416682-insertobject?language=objc
                        //Objects gets shifted by 1
                        payloadEvents.remove(gIndx + 1);
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private void storeSessionFunnelInfoPayload(@Nullable BOFunnelPayload sessionFunnelPayload) {
        if (sessionFunnelPayload == null) {
            return;
        }
        try {
            BOFunnelPayload funnelPayload = sessionFunnelPayload;
            if (funnelPayload.getFevents() != null && funnelPayload.getFevents().size() > 0) {
                String funnelID = funnelPayload.getFevents().get(funnelPayload.getFevents().size() - 1).getIdentifier();
                String sessionInfoFunnelsString = BOFunnelPayload.toJsonString(funnelPayload);

                if (funnelID != null && !funnelID.equals("") && sessionInfoFunnelsString != null && !sessionInfoFunnelsString.equals("")) {

                    String dateString = BODateTimeUtils.getFormattedCurrentDateString(BOCommonConstants.BO_DATE_FORMAT);
                    String funnelInfoDatePath = BOFileSystemManager.getInstance().getSyncPendingSessionFunnelInfoDirectoryPathForDate(dateString, funnelID);
                    String funnelInfoFilePath = funnelInfoDatePath + "/" + BODateTimeUtils.get13DigitNumberObjTimeStamp() + ".txt";

                    //else file write operation and prapare new object
                    BOFileSystemManager.getInstance().writeToFile(funnelInfoFilePath, sessionInfoFunnelsString);
                }
            }
            //TODO: for testing only
            //        String funnelInfoDatePathTest = BOFileSystemManager.getInstance().getSyncPendingSessionFunnelInfoDirectoryPathForDate(dateString, funnelID);
            //        String funnelInfoFilePathTest = funnelInfoDatePathTest + "/" + BODateTimeUtils.get13DigitNumberObjTimeStamp() + ".txt";
            //        BOFileSystemManager.getInstance().writeToFile(funnelInfoFilePathTest, sessionInfoFunnelsString);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private void storeSessionFunnelMetaInfo() {
        //Write creating separate file and saving it under date folder, so all session for the day goes inside same directory
        //Alternatively we can do array below with timeStamp and same in a single file with date as file name.

        //As we are saving at App close and when App crosses day in a single session,
        //then using multiple files last file gets saved to separate folder as at end day has chnaged.

        //In the alternate approach, we will load data at the begining and save in the same file on day change so data of same file will have timeStamp of another day.
        //Another approach possible to create another file if day has changed, but going with multi file approach for now and will test any performance impact before switching to single file approach

        HashMap<String, Object> sessionMetaInfoFunnels = new HashMap<>();
        try {
            sessionMetaInfoFunnels.put("launchTimeStamp", this.launchTimeStamp); //platform Code, defined in event doc
            sessionMetaInfoFunnels.put("terminationTimeStamp", this.terminationTimeStamp);
            sessionMetaInfoFunnels.put("eventSequenceOrder", this.eventSequenceOrder);
            sessionMetaInfoFunnels.put("eventSubCodeSequenceOrder", this.eventSubCodeSequenceOrder);
            sessionMetaInfoFunnels.put("eventsInfo", this.eventsInfo);
            sessionMetaInfoFunnels.put("preEventsInfo", this.preEventsInfo);
            sessionMetaInfoFunnels.put("eventsSubCode", this.eventsSubCode);
            sessionMetaInfoFunnels.put("eventsDuration", this.eventsDuration);

            for (int i = 0; i < this.eventSequenceOrder.size(); i++) {
                Logger.INSTANCE.e(TAG, "");
            }
            String sessionMetaInfoFunnelsString = BOCommonUtils.getJsonStringFromHashMap(sessionMetaInfoFunnels);
            String dateString = BODateTimeUtils.getFormattedCurrentDateString(BOCommonConstants.BO_DATE_FORMAT);
            String metaInfoDatePath = BOFileSystemManager.getInstance().getSyncPendingSessionFunnelMetaInfoDirectoryPathForDate(dateString);
            String metaInfoFilePath = metaInfoDatePath + "/" + this.terminationTimeStamp + ".txt";
            BOFileSystemManager.getInstance().writeToFile(metaInfoFilePath, sessionMetaInfoFunnelsString);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private void recordEventDurationsFor(@Nullable String preEventNameRef, long changeRefTime) {

        if (preEventNameRef == null || preEventNameRef.equals("")) {
            return;
        }
        try {
            if (this.preEventsInfo != null && this.preEventsInfo.keySet().size() > 0) {
                //preEventNameRef are not activaly used
                Long changeRefTimeL = changeRefTime;
                String preEventName = (String) this.preEventsInfo.keySet().toArray()[this.preEventsInfo.keySet().size() - 1];
                Long preEventTimeStamp = this.preEventsInfo.get(preEventName);
                Long preEventCurrentDuration = changeRefTimeL - preEventTimeStamp;

                if (this.eventsDuration.containsKey(preEventName)) {
                    List<Long> preEventDurations = new ArrayList<>(this.eventsDuration.get(preEventName));
                    preEventDurations.add(preEventCurrentDuration);
                    this.eventsDuration.put(preEventName, preEventDurations);
                } else {
                    List<Long> array = new ArrayList<>();
                    array.add(preEventCurrentDuration);
                    this.eventsDuration.put(preEventName, array);
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    public void recordNavigationEventFrom(@Nullable String fromVC, @Nullable String toVC, HashMap<String, Object> eventDetails) {

    }

    public void recordDevEvent(@Nullable String eventName, Long eventSubCode, HashMap<String, Object> eventDetails) {
        try {

            if(!BlotoutAnalytics_Internal.getInstance().isFunnelEventsEnabled)
                return;

            if (eventName != null && !eventName.equals("")) {

                Long eventTimeStamp = BODateTimeUtils.get13DigitNumberObjTimeStamp();
                this.eventSequenceOrder.add(eventName);
                this.eventSubCodeSequenceOrder.add(eventSubCode);

                if (this.eventsInfo.containsKey(eventName)) {
                    List<Long> eventTimes = new ArrayList<>(this.eventsInfo.get(eventName));
                    eventTimes.add(eventTimeStamp);
                    this.eventsInfo.put(eventName, eventTimes);
                } else {
                    List<Long> array = new ArrayList<>();
                    array.add(eventTimeStamp);
                    this.eventsInfo.put(eventName, array);
                }

                String preEventName = null;
                if (this.preEventsInfo != null && this.preEventsInfo.keySet().size() > 0 && !this.preEventsInfo.keySet().toArray()[this.preEventsInfo.keySet().size() - 1].equals(eventName)) {
                    preEventName = (String) this.preEventsInfo.keySet().toArray()[this.preEventsInfo.keySet().size() - 1];
                    recordEventDurationsFor(preEventName, eventTimeStamp);
                }

                if (eventSubCode != 0) {
                    this.eventsSubCode.put(eventName, eventSubCode);
                }

                //Do it when app goes in background, make changes later, now doing it in background
                //[self analyseAndUpdateFunnelsPayloadForEvents:eventName eventCode:eventSubCode happenedAt:eventTimeStamp];

                if (this.preEventsInfo != null && this.preEventsInfo.keySet().size() > 0) {
                    this.preEventsInfo.clear();
                }
                this.preEventsInfo.put(eventName, eventTimeStamp);
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    @NonNull
    private List<String> getAllStoredSyncPendingSessionsFunnelPayloadFilesFor(String funnelID, String dateStr) {
        try {
            String funnelIDDateDir = BOFileSystemManager.getInstance().getSyncPendingSessionFunnelInfoDirectoryPathForDate(dateStr, funnelID);
            List<String> allFunnelIDFiles = BOFileSystemManager.getInstance().getAllFilesWithExtension(funnelIDDateDir, "txt");
            return allFunnelIDFiles;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }

        return null;
    }

    @NonNull
    private List<BOFunnelPayload> getAllStoredSyncPendingSessionsFunnelPayloadFor(String funnelID, String dateStr) {
        try {
            String funnelIDDateDir = BOFileSystemManager.getInstance().getSyncPendingSessionFunnelInfoDirectoryPathForDate(dateStr, funnelID);
            List<String> allFunnelIDFiles = BOFileSystemManager.getInstance().getAllFilesWithExtension(funnelIDDateDir, "txt");

            List<BOFunnelPayload> funnelPayloadsArr = new ArrayList<>();
            for (String funnelFiles : allFunnelIDFiles) {
                try {
                    String fileDataStr = BOFileSystemManager.getInstance().readContentOfFileAtPath(funnelFiles);
                    BOFunnelPayload funnelPayload = BOFunnelPayload.fromJsonString(fileDataStr);
                    funnelPayloadsArr.add(funnelPayload);
                } catch (Exception e) {

                }
            }
            return funnelPayloadsArr;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    @NonNull
    private List<String> getAllStoredSyncCompleteSessionsFunnelPayloadFilesFor(String funnelID, String dateStr) {
        try {
            String funnelIDDateDir = BOFileSystemManager.getInstance().getSyncCompleteSessionFunnelInfoDirectoryPathForDate(dateStr, funnelID);
            List<String> allFunnelIDFiles = BOFileSystemManager.getInstance().getAllFilesWithExtension(funnelIDDateDir, "txt");
            return allFunnelIDFiles;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    @NonNull
    private List<BOFunnelPayload> getAllStoredSyncCompleteSessionsFunnelPayloadFor(String funnelID, String dateStr) {
        try {
            String funnelIDDateDir = BOFileSystemManager.getInstance().getSyncCompleteSessionFunnelInfoDirectoryPathForDate(dateStr, funnelID);
            List<String> allFunnelIDFiles = BOFileSystemManager.getInstance().getAllFilesWithExtension(funnelIDDateDir, "txt");

            List<BOFunnelPayload> funnelPayloadsArr = new ArrayList<>();
            for (String funnelFiles : allFunnelIDFiles) {
                try {
                    String fileDataStr = BOFileSystemManager.getInstance().readContentOfFileAtPath(funnelFiles);
                    BOFunnelPayload funnelPayload = BOFunnelPayload.fromJsonString(fileDataStr);
                    funnelPayloadsArr.add(funnelPayload);
                } catch (Exception e) {

                }
            }
            return funnelPayloadsArr;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    private void analyseAndUpdateFunnelsPayloadForEventsOccuredSoFar() {

        if (funnelsAndCodifiedEventsInstance == null)
            return;
        try {
            List<String> uniqueEventNames = new ArrayList<>();
            uniqueEventNames.addAll(this.eventSequenceOrder);

            List uniqueEventSubcodes = new ArrayList<>(this.eventSubCodeSequenceOrder);

            for (int gIndexEvent = 0; gIndexEvent < uniqueEventNames.size(); gIndexEvent++) {

                String eventNameArg = uniqueEventNames.get(gIndexEvent);
                Long eventCodeArg = (Long) uniqueEventSubcodes.get(gIndexEvent);

                //TODO: improvements
                //Logic can be improved by knowing funnels which contains event name and process for those only
                //Let it work in 1.0 launch and then improve logic later
                if (funnelsAndCodifiedEventsInstance.getEventsFunnel() != null) {
                    for (int indx = 0; indx < funnelsAndCodifiedEventsInstance.getEventsFunnel().size(); indx++) {

                        BOEventsFunnel funnelEvent = funnelsAndCodifiedEventsInstance.getEventsFunnel().get(indx);
                        BOFunnelEvent funnelPayloadEvent = payloadEvents.get(indx);
                        //Making sure funnelPayloadEvent is not nil
                        if (funnelPayloadEvent != null) {
                            for (BOEventList event : funnelEvent.getEventList()) {
                                if (event.getEventName().equals(eventNameArg) && event.getEventCategorySubtype().equals("" + eventCodeArg)) {

                                    List<Long> visits = new ArrayList<>(funnelPayloadEvent.getVisits());
                                    List<Long> navigationTime = new ArrayList<>(funnelPayloadEvent.getNavigationTime());
                                    int eventMatchIndex = funnelEvent.getEventList().indexOf(event);

//                        NSString *preEventName = nil;//[[self.preEventsInfo allKeys] lastObject];
//                        NSUInteger eventsSoFarCount = self.eventSequenceOrder.count;
//                        if (eventsSoFarCount <= 1) {
//                            preEventName = [self.eventSequenceOrder lastObject];
//                        }else{
//                            preEventName = [self.eventSequenceOrder objectAtIndex:(eventsSoFarCount - 2)];
//                        }
                                    List<Long> eventDurations = eventNameArg != null ? this.eventsDuration.get(eventNameArg) : null;
                                    int totalVisitCount = eventDurations != null ? eventDurations.size() : 0;
                                    long totalEventDuration = 0;
                                    if (eventDurations != null) {
                                        for (Long value : eventDurations) {
                                            totalEventDuration = totalEventDuration + value;
                                        }
                                    }

                                    //After making changes in init prepare method, this if should never be true, still for safety let it be until 100% tested
                                    if (eventMatchIndex > (visits.size() - 1)) {
                                        for (int preFillIndx = visits.size() - 1; preFillIndx < eventMatchIndex; preFillIndx++) {
                                            visits.add((long) 0);
                                            navigationTime.add((long) 0);
                                        }
                                    }

                                    //NSNumber *existingVisitCount = [visits objectAtIndex:eventMatchIndex];
                                    //int newCount = ([existingVisitCount intValue] > 0) ? ([existingVisitCount intValue] + self.eventsDuration) : 1;
                                    visits.add(eventMatchIndex, (long) totalVisitCount);
                                    int oldObjectIndex = eventMatchIndex + 1;
                                    if ((visits.size() > 1) && oldObjectIndex < visits.size()) {
                                        visits.remove(oldObjectIndex);
                                    }

                                    navigationTime.add(eventMatchIndex, totalEventDuration);
                                    if ((navigationTime.size() > 1) && oldObjectIndex < navigationTime.size()) {
                                        navigationTime.remove(oldObjectIndex);
                                    }

                                    funnelPayloadEvent.setVisits(visits);
                                    funnelPayloadEvent.setNavigationTime(navigationTime);
                                }
                            }
                            //[payloadEvents removeObjectAtIndex:indx];

                            //This is update only operation so cases like payloadEvents with 0 objects in it and first insert operation happening here must not be the case
                            payloadEvents.add(indx, funnelPayloadEvent);
                            //Remove the old one & as per behaviour mentione here
                            //https://developer.apple.com/documentation/foundation/nsmutablearray/1416682-insertobject?language=objc
                            //Objects gets shifted by 1
                            int oldObjectNewIndex = indx + 1;
                            payloadEvents.remove(oldObjectNewIndex);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private void analyseAndUpdateFunnelsPayloadForEvents(@Nullable String eventNameArg, long eventCodeArg, long timeStamp) {

        if (eventNameArg == null || eventNameArg.equals("")) {
            return;
        }
        try {
            //NSNumber *eventTimeStamp = timeStamp;
            if (funnelsAndCodifiedEventsInstance == null) {
                //This is true, find way to make sure object init happen before this call
                return;
            }
            //TODO: improvements
            //Logic can be improved by knowing funnels which contains event name and process for those only
            //Let it work in 1.0 launch and then improve logic later
            for (int indx = 0; indx < funnelsAndCodifiedEventsInstance.getEventsFunnel().size(); indx++) {

                BOEventsFunnel funnelEvent = funnelsAndCodifiedEventsInstance.getEventsFunnel().get(indx);
                BOFunnelEvent funnelPayloadEvent = payloadEvents.get(indx);
                //Making sure funnelPayloadEvent is not nil
                if (funnelPayloadEvent != null) {
                    for (BOEventList event : funnelEvent.getEventList()) {

                        if (event.getEventName().equals(eventNameArg) && event.getEventCategorySubtype().equals("" + eventCodeArg)) {

                            List<Long> visits = new ArrayList<>(funnelPayloadEvent.getVisits());
                            List<Long> navigationTime = new ArrayList<>(funnelPayloadEvent.getNavigationTime());
                            int eventMatchIndex = funnelEvent.getEventList().indexOf(event);

                            String preEventName = null;//[[self.preEventsInfo allKeys] lastObject];
                            int eventsSoFarCount = this.eventSequenceOrder.size();
                            if (eventsSoFarCount <= 1) {
                                preEventName = this.eventSequenceOrder.get(eventsSoFarCount - 1);
                            } else {
                                preEventName = this.eventSequenceOrder.get(eventsSoFarCount - 2);
                            }

                            List<Long> eventDurations = preEventName != null ? this.eventsDuration.get(preEventName) : null;
                            int totalVisitCount = eventDurations != null ? eventDurations.size() : 0;
                            long totalEventDuration = 0;

                            if (eventDurations != null) {
                                for (Long value : eventDurations) {
                                    totalEventDuration = totalEventDuration + value;
                                }
                            }
                            //NSNumber *existingVisitCount = [visits objectAtIndex:eventMatchIndex];
                            //int newCount = ([existingVisitCount intValue] > 0) ? ([existingVisitCount intValue] + self.eventsDuration) : 1;

                            //After making changes in init prepare method, this if should never be true, still for safety let it be until 100% tested
                            if (eventMatchIndex > (visits.size() - 1)) {

                                for (int preFillIndx = ((int) visits.size() - 1); preFillIndx < eventMatchIndex; preFillIndx++) {
                                    visits.add((long) 0);
                                    navigationTime.add((long) 0);
                                }
                            }

                            visits.add(eventMatchIndex, (long) totalVisitCount);
                            int oldObjectIndex = eventMatchIndex + 1;
                            if ((visits.size() > 1) && oldObjectIndex < visits.size()) {
                                visits.remove(oldObjectIndex);
                            }

                            navigationTime.add(eventMatchIndex, totalEventDuration);
                            if ((navigationTime.size() > 1) && oldObjectIndex < navigationTime.size()) {
                                navigationTime.remove(oldObjectIndex);
                            }

                            funnelPayloadEvent.setVisits(visits);
                            funnelPayloadEvent.setNavigationTime(navigationTime);
                        }
                    }
                    //[payloadEvents removeObjectAtIndex:indx];
                    //This is update only operation so cases like payloadEvents with 0 objects in it and first insert operation happening here must not be the case
                    payloadEvents.add(indx, funnelPayloadEvent);
                    //Remove the old one & as per behaviour mentione here
                    //https://developer.apple.com/documentation/foundation/nsmutablearray/1416682-insertobject?language=objc
                    //Objects gets shifted by 1
                    int oldObjectNewIndex = indx + 1;
                    payloadEvents.remove(oldObjectNewIndex);
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    //Sync shoud consider all previous days files, as if App is launched once is a day and then next day then session not sycned is previous day, similar for previous chain

    private void serverSynSessionFunnelPayloadOfDate(@Nullable String syncDateStr, @NonNull BOHandlerMessage callback) {
        try {
            //NSString *previousDateStr = [BOAUtilities getPreviousDayDateInFormat:@"yyyy-MM-dd" fromReferenceDate:[BOAUtilities getCurrentDate]];
            String todayDateStr = syncDateStr != null ? syncDateStr : BODateTimeUtils.getFormattedCurrentDateString(BOCommonConstants.BO_DATE_FORMAT);

            String allSessionFunnelsDir = BOFileSystemManager.getInstance().getSyncPendingSessionFunnelInfoDirectoryPath();
            List<String> allFunnelsDir = BOFileSystemManager.getInstance().getAllFileAndDirectories(allSessionFunnelsDir);

            //All stored session funnels IDs
            List<String> allStoredFunnelIDs = new ArrayList();

            for (String fDirPath : allFunnelsDir) {
                //TODO: check for dir name as funnel ID match
                String dirName = BOFileSystemManager.getInstance().getLastPathComponent(fDirPath);
                allStoredFunnelIDs.add(dirName);
            }

            //All today session Funnels files
            List<List<String>> allFunnelsSessionFunnels = new ArrayList<>();
            for (String funnelID : allStoredFunnelIDs) {
                List<String> allFunnelsFiles = this.getAllStoredSyncPendingSessionsFunnelPayloadFilesFor(funnelID, todayDateStr);
                allFunnelsSessionFunnels.add(allFunnelsDir);
            }

            boolean isSuccess = false;
            for (int loop1 = 0; loop1 < allFunnelsSessionFunnels.size(); loop1++) {
                List<String> allFunnelSessionFiles = allFunnelsSessionFunnels.get(loop1);
                for (int loop2 = 0; loop2 < allFunnelSessionFiles.size(); loop2++) {
                    String sessionFunnelFile = allFunnelSessionFiles.get(loop2);
                    String fileData = null;
                    try {
                        fileData = BOFileSystemManager.getInstance().readContentOfFileAtPath(sessionFunnelFile);//            NSString *funnelDateStr = [[funnelFilePathCopy2 stringByDeletingLastPathComponent] lastPathComponent];
                    } catch (Exception e) {

                    }
                    HashMap<String, Object> jsonDict = BOCommonUtils.getHashmapFromJsonString(fileData);

                    String funnelIDStr = BOFileSystemManager.getInstance().getLastPathComponent(BOFileSystemManager.getInstance().stringByDeletingLastPathComponent(BOFileSystemManager.getInstance().stringByDeletingLastPathComponent(sessionFunnelFile)));

                    this.serverSyncFunnelAtURLRequest(jsonDict, new BOHandlerMessage() {
                        @Override
                        public void handleMessage(@NonNull BOMessage msg) {
                            if (msg.what == 1) {
                                String funnelIDnDateDirInsideComplete = BOFileSystemManager.getInstance().getSyncCompleteSessionFunnelInfoDirectoryPathForDate(todayDateStr, funnelIDStr);
                                //Because file has already been written to pending directory, so moving from pending to complete
                                //Already written becasue of uncertain user behaviour and app may get closed just after launch
                                boolean isSuccess = BOFileSystemManager.getInstance().moveFile(sessionFunnelFile, funnelIDnDateDirInsideComplete);
                                if (isSuccess) {
                                    callback.handleMessage(new BOHandlerMessage.BOMessage(1, null));
                                } else {
                                    //TODO: fix this using error class framework
                                    callback.handleMessage(new BOHandlerMessage.BOMessage(0, null));
                                }
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            callback.handleMessage(new BOHandlerMessage.BOMessage(0, null));
        }
    }

    //Do this once only at App launch using previous day sesssion events, prepare daily aggregation, save it and send it to server as well
//If any major issue found during testing then you can preapre this object along with funnel and sync with server on date change, but for now seems ok
    private void preapreDailyAggregatedFunnelEventAndSaveToDiskWithServerSyncForDate(
            @Nullable String dateString) {
        try {
            //Make sure to take care of session movement
            String previousDateStr = dateString != null ? dateString : BODateTimeUtils.getPreviousDayDateInFormat(BODateTimeUtils.getCurrentDate(), BOCommonConstants.BO_DATE_FORMAT);
            String allSessionFunnelsDir = BOFileSystemManager.getInstance().getSyncCompleteSessionFunnelInfoDirectoryPath();
            List<String> allFunnelsDir = BOFileSystemManager.getInstance().getAllDirectories(allSessionFunnelsDir);

            //All stored session funnels IDs
            List<String> allStoredFunnelIDs = new ArrayList();
            for (String fDirPath : allFunnelsDir) {
                //TODO: check for dir name as funnel ID match
                String dirName = BOFileSystemManager.getInstance().getLastPathComponent(fDirPath);
                allStoredFunnelIDs.add(dirName);
            }
            //All previous day session Funnels objects
            List<List<BOFunnelPayload>> allPreDaySessionFunnels = new ArrayList<>();
            for (String funnelID : allStoredFunnelIDs) {
                List<BOFunnelPayload> allFunnelsFiles = this.getAllStoredSyncCompleteSessionsFunnelPayloadFor(funnelID, previousDateStr);
                allPreDaySessionFunnels.add(allFunnelsFiles);
            }

            //BOAFunnelAndCodifiedEvents *funnelsAndCodifiedEvents = [self loadAllActiveFunnels];
            for (List<BOFunnelPayload> funnelSessionPayloadArr : allPreDaySessionFunnels) {

                BOFunnelPayload dailyFunnelEventPay = new BOFunnelPayload();
                BOFunnelEvent dailyEvent = new BOFunnelEvent();

                BOFunnelPayload funnelSessionP = funnelSessionPayloadArr.size() > 0 ? funnelSessionPayloadArr.get(funnelSessionPayloadArr.size() - 1) : null;
                //last object or first object both should be fine as we are maintaining single funnel event per file
                if (funnelSessionP != null && funnelSessionP.getFevents() != null) {
                    BOFunnelEvent funnelPayloadEvent = funnelSessionP.getFevents().size() > 0 ? funnelSessionP.getFevents().get(funnelSessionP.getFevents().size() - 1) : null;
                    if (funnelPayloadEvent != null) {
                        dailyEvent.setIdentifier(funnelPayloadEvent.getIdentifier());
                        dailyEvent.setVersion(funnelPayloadEvent.getVersion());
                        dailyEvent.setName(funnelPayloadEvent.getName());
                        dailyEvent.setEventTime(funnelPayloadEvent.getEventTime());//eventTimeStamp;
                        dailyEvent.setDayOfAnalysis(previousDateStr);
                        dailyEvent.setDaySessionCount(funnelSessionPayloadArr.size());
                        dailyEvent.setMessageID(BOCommonUtils.generateMessageIDForEvent(funnelPayloadEvent.getName(), funnelPayloadEvent.getIdentifier(), funnelPayloadEvent.getEventTime()));
                        dailyEvent.setISADayEvent(true);
                        boolean isTraversed = false;
                        int traversedCount = 0;
                        for (BOFunnelPayload funnelSessionPayLoop : funnelSessionPayloadArr) {
                            if (funnelSessionPayLoop != null
                                    && funnelSessionPayLoop.getFevents() != null && funnelSessionPayLoop.getFevents().size() > 0) {
                                BOFunnelEvent eve = funnelSessionPayLoop.getFevents().get(funnelSessionPayLoop.getFevents().size() - 1);
                                if (eve.getIsTraversed()) {
                                    isTraversed = true;
                                    traversedCount = traversedCount + 1;
                                }
                            }
                        }
                        long todaysCount = traversedCount;
                        dailyEvent.setIsTraversed(isTraversed);
                        dailyEvent.setDayTraversedCount(todaysCount);

                        List<Long> dailyVisits = new ArrayList<>();
                        List<Long> dailyEventNavigation = new ArrayList<>();
                        for (int indx = 0; indx < funnelPayloadEvent.getVisits().size(); indx++) {
                            long visitSum = 0;
                            long navigationSum = 0;
                            for (int yndx = 0; yndx < funnelSessionPayloadArr.size(); yndx++) {
                                //last object because in payload store file, we are storing single session data and single funnel data per file
                                List<BOFunnelEvent> funnelEvents = funnelSessionPayloadArr.get(yndx).getFevents();
                                if (funnelEvents != null && funnelEvents.size() > 0) {
                                    BOFunnelEvent fEvent = funnelEvents.get(funnelEvents.size() - 1);
                                    //BOAFunnelEvent *visitTemp = [funnelSessionP.funnelEvents objectAtIndex:yndx];
                                    if (fEvent != null) {
                                        if (fEvent.getVisits() != null && fEvent.getVisits().size() < indx) {
                                            long firstEventFirstVisit = fEvent.getVisits().get(indx);
                                            visitSum = visitSum + firstEventFirstVisit;
                                        }
                                        //Becasue visits & navigation counter is same, so doing both job in the same loop
                                        if (fEvent.getNavigationTime() != null && fEvent.getNavigationTime().size() < indx) {
                                            long firstEventFirstNavigation = fEvent.getNavigationTime().get(indx);
                                            navigationSum = navigationSum + firstEventFirstNavigation;
                                        }
                                    }
                                }
                            }

                            //No delete needed as insertion is happening on fresh object being created at line 689 & 690
                            //TODO: Check whether insert at index is working properly
                            dailyVisits.add(indx, visitSum);
                            dailyEventNavigation.add(indx, navigationSum);
                        }

                        //        NSMutableArray<NSNumber*> *dailyEventNavigation = [NSMutableArray array];
                        //        for (int indx1=0; indx1<funnelPayloadEvent.navigationTime.count; indx1++) {
                        //            long navigationSum = 0;
                        //            for (int yndx1=0; yndx1<funnelSessionPayloadArr.count; yndx1++) {
                        //                //last object because in payload store file, we are storing single session data and single funnel data per file
                        //                BOAFunnelEvent *fEvent = [[funnelSessionPayloadArr objectAtIndex:yndx1].funnelEvents lastObject];
                        //                NSNumber *firstEventFirstNavigation = [fEvent.navigationTime objectAtIndex:indx1];
                        //                navigationSum = navigationSum + [firstEventFirstNavigation longValue];
                        //            }
                        //            [dailyEventNavigation insertObject:[NSNumber numberWithLong:navigationSum] atIndex:indx1];
                        //        }

                        dailyEvent.setVisits(dailyVisits);
                        dailyEvent.setNavigationTime(dailyEventNavigation);
                        dailyEvent.setUserReferral(false);//TODO: need to work on this
                        dailyEvent.setUserTraversedCount(this.getAndUpdateUserLevelFunnelCountForID(funnelPayloadEvent.getIdentifier(), todaysCount));
                        dailyEvent.setPrevTraversalDay(this.getAndUpdateFunnelPrevTraversalDateForID(funnelPayloadEvent.getIdentifier(), isTraversed));

                        BOFunnelMeta dailyMetaInfo = funnelSessionP.getMeta();
                        BOFunnelGeo dailyGeoInfo = funnelSessionP.getGeo();
                        List<BOFunnelEvent> dailyFunnelEvents = new ArrayList<BOFunnelEvent>();
                        dailyFunnelEvents.add(dailyEvent);

                        dailyFunnelEventPay.setMeta(dailyMetaInfo);
                        dailyFunnelEventPay.setGeo(dailyGeoInfo);
                        dailyFunnelEventPay.setFevents(dailyFunnelEvents);
                        //work on looping part for other funnel ID as above just make ready for one
                        this.storeDailyAggregatedFunnelPayload(dailyFunnelEventPay, previousDateStr, funnelPayloadEvent.getIdentifier());
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private long getAndUpdateUserLevelFunnelCountForID(String funnelID, long todaysCount) {
        try {
            String userFunnelString = BOSharedPreferenceImpl.getInstance().getString(BOCommonConstants.BO_ANALYTICS_FUNNEL_USER_TRAVERSAL_COUNT_DEFAULTS_KEY);
            HashMap<String, Object> userFunnelCount = BOCommonUtils.getHashmapFromJsonString(userFunnelString);
            Long prevCount = (Long) userFunnelCount.get(funnelID);
            Long newCount = prevCount + todaysCount;
            userFunnelCount.put(funnelID, newCount);
            BOSharedPreferenceImpl.getInstance().saveString(BOCommonUtils.getJsonStringFromHashMap(userFunnelCount), BOCommonConstants.BO_ANALYTICS_FUNNEL_USER_TRAVERSAL_COUNT_DEFAULTS_KEY);
            return newCount;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }

        return 0;
    }

    @Nullable
    private String getAndUpdateFunnelPrevTraversalDateForID(String funnelID, boolean todayTraversed) {
        try {
            String userFunnelVisitDayString = BOSharedPreferenceImpl.getInstance().getString(BOCommonConstants.BO_ANALYTICS_FUNNEL_USER_TRAVERSAL_PREV_DAY_DEFAULTS_KEY);
            HashMap<String, Object> userFunnelVisitDay = BOCommonUtils.getHashmapFromJsonString(userFunnelVisitDayString);
            String prevDay = (String) userFunnelVisitDay.get(funnelID);
            if (todayTraversed) {
                String newDate = BODateTimeUtils.getFormattedCurrentDateString(BOCommonConstants.BO_DATE_FORMAT);
                userFunnelVisitDay.put(funnelID, newDate);
            }
            BOSharedPreferenceImpl.getInstance().saveString(BOCommonUtils.getJsonStringFromHashMap(userFunnelVisitDay), BOCommonConstants.BO_ANALYTICS_FUNNEL_USER_TRAVERSAL_PREV_DAY_DEFAULTS_KEY);
            return prevDay;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return null;
        }
    }

    private void storeDailyAggregatedFunnelPayload(BOFunnelPayload dailyAggregatedPayload, @Nullable String dateStr, String funnelID) {
        try {
            String previousDateStr = dateStr != null ? dateStr : BODateTimeUtils.getPreviousDayDateInFormat(BODateTimeUtils.getCurrentDate(), BOCommonConstants.BO_DATE_FORMAT);

            String dailyAggregatedEventStr = BOFunnelPayload.toJsonString(dailyAggregatedPayload);

            String fileExtention = "txt";
            String dailyAggregatedPendingDir = BOFileSystemManager.getInstance().getDailyAggregatedFunnelEventsSyncPendingDirectoryPath();
            String dateDirInsidePending = BOFileSystemManager.getInstance().createDirectoryIfRequiredAndReturnPath(dailyAggregatedPendingDir + "/" + previousDateStr);
            String dailyAggregatedFunnelIDFile = dateDirInsidePending + "/" + funnelID + "." + fileExtention;
            //else file write operation and prapare new object

            BOFileSystemManager.getInstance().writeToFile(dailyAggregatedFunnelIDFile, dailyAggregatedEventStr);
            this.serverSynDailyAggregatedFunnelPayload(dailyAggregatedPayload, previousDateStr, funnelID);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private void serverSynDailyAggregatedFunnelPayload(@Nullable BOFunnelPayload dailyAggregatedEvent, @Nullable String dateStr, String funnelID) {
        try {
            String previousDateStr = dateStr != null ? dateStr : BODateTimeUtils.getPreviousDayDateInFormat(BODateTimeUtils.getCurrentDate(), BOCommonConstants.BO_DATE_FORMAT);
            String fileExtention = "txt";

            if (dailyAggregatedEvent != null) {

                String dailyAggregatedData = BOFunnelPayload.toJsonString(dailyAggregatedEvent);
                HashMap<String, Object> dailyDataDict = BOCommonUtils.getHashmapFromJsonString(dailyAggregatedData);
                this.serverSyncFunnelAtURLRequest(dailyDataDict, new BOHandlerMessage() {
                    @Override
                    public void handleMessage(@NonNull BOMessage msg) {
                        if (msg.what == 1) {
                            String dailyAggregatedPendingDir = BOFileSystemManager.getInstance().getDailyAggregatedFunnelEventsSyncPendingDirectoryPath();
                            String dateDirInsidePending = BOFileSystemManager.getInstance().createDirectoryIfRequiredAndReturnPath(dailyAggregatedPendingDir + "/" + previousDateStr);

                            String dailyAggregatedCompleteDir = BOFileSystemManager.getInstance().getDailyAggregatedFunnelEventsSyncCompleteDirectoryPath();
                            String dateDirInsideComplete = BOFileSystemManager.getInstance().createDirectoryIfRequiredAndReturnPath(dailyAggregatedCompleteDir + "/" + previousDateStr);

                            String funnleFilePath = dateDirInsidePending + "/" + funnelID + "." + fileExtention;

                            //Because file has already been written to pending directory, so moving from pending to complete
                            //Already written becasue of uncertain user behaviour and app may get closed just after launch
                            BOFileSystemManager.getInstance().moveFile(funnleFilePath, dateDirInsideComplete);
                        }
                    }
                });
            } else {
                String dailyAggregatedPendingDir = BOFileSystemManager.getInstance().getDailyAggregatedFunnelEventsSyncPendingDirectoryPath();
                String dateDirInsidePending = BOFileSystemManager.getInstance().createDirectoryIfRequiredAndReturnPath(dailyAggregatedPendingDir + "/" + previousDateStr);

                List<String> allFunnelPayloadFiles = BOFileSystemManager.getInstance().getAllFilesWithExtension(dateDirInsidePending, fileExtention);

                for (String singleFilePath : allFunnelPayloadFiles) {

                    try {
                        String jsonString = BOFileSystemManager.getInstance().readContentOfFileAtPath(singleFilePath);
                        HashMap<String, Object> dailyDataDict = BOCommonUtils.getHashmapFromJsonString(jsonString);
                        this.serverSyncFunnelAtURLRequest(dailyDataDict, new BOHandlerMessage() {
                            @Override
                            public void handleMessage(@NonNull BOMessage msg) {
                                if (msg.what == 1) {

                                    String dailyAggregatedCompleteDir = BOFileSystemManager.getInstance().getDailyAggregatedFunnelEventsSyncCompleteDirectoryPath();
                                    String dateDirInsideComplete = BOFileSystemManager.getInstance().createDirectoryIfRequiredAndReturnPath(dailyAggregatedCompleteDir + "/" + previousDateStr);
                                    //Because file has already been written to pending directory, so moving from pending to complete
                                    //Already written becasue of uncertain user behaviour and app may get closed just after launch
                                    BOFileSystemManager.getInstance().moveFile(singleFilePath, dateDirInsideComplete);
                                }
                            }
                        });
                    } catch (Exception e) {
                        Logger.INSTANCE.e(TAG, e.toString());
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private void storeSessionFunnelsNewData(@Nullable BOFunnelAndCodifiedEvents sessionFunnelsNewData) {
        if (sessionFunnelsNewData == null) {
            return;
        }
        try {
            boolean newFunnelsAdded = false;
            BOFunnelAndCodifiedEvents oldFunnelsEvent = this.loadAllActiveFunnels();
            if (oldFunnelsEvent != null) {
                List<BOEventsFunnel> oldAndNewEvents = new ArrayList<>(oldFunnelsEvent.getEventsFunnel());

                //TODO: Directly adding from array can lead to duplicate funnel object if server makes a mistake.
                //TODO: Using above incomplete for loop mechanism, we can filter but will see the need & do

                //Once model is updated then implement logic for deleting old one as well
                for (BOEventsFunnel newEFunnel : sessionFunnelsNewData.getEventsFunnel()) {
                    boolean isSameFEvent = false;
                    for (BOEventsFunnel oldEFunnel : oldAndNewEvents) {
                        if (newEFunnel.getIdentifier().equals(oldEFunnel.getIdentifier())) {
                            isSameFEvent = true;
                            break;
                        }
                    }
                    if (!isSameFEvent) {
                        oldAndNewEvents.add(newEFunnel);
                        newFunnelsAdded = true;
                    }
                }
                //Below was duplicating funnel events
                //[oldAndNewEvents addObjectsFromArray:sessionFunnelsNewData.eventsFunnel];
                oldFunnelsEvent.setEventsFunnel(oldAndNewEvents);
            } else {
                oldFunnelsEvent = sessionFunnelsNewData;
                newFunnelsAdded = true;
            }

            if (newFunnelsAdded) {
                BOCommonEvents.getInstance().recordFunnelReceived();
            }
            String allFunnelsDataStr = BOFunnelAndCodifiedEvents.toJsonString(oldFunnelsEvent);

            String fileExtention = "txt";
            String allFunnelsDirPath = BOFileSystemManager.getInstance().getAllFunnelsToAnalyseDirectoryPath();
            String allFunnelsFilePath = allFunnelsDirPath + "/" + "AllFunnels" + "." + fileExtention;
            //else file write operation and prapare new object
            BOFileSystemManager.getInstance().writeToFile(allFunnelsFilePath, allFunnelsDataStr);

            String sessionFunnelsNewDataStr = BOFunnelAndCodifiedEvents.toJsonString(sessionFunnelsNewData);

            String dateString = BODateTimeUtils.getStringFromDate(BODateTimeUtils.getCurrentDate(), BOCommonConstants.BO_DATE_FORMAT);
            String funnelDownloadLogsDir = BOFileSystemManager.getInstance().getLogLevelDirAllFunnelsToAnalyseDirectoryPath();
            String funnelDownloadLogsFile = funnelDownloadLogsDir + "/" + dateString + "-" + BODateTimeUtils.get13DigitNumberObjTimeStamp() + ".txt";

            //else file write operation and prapare new object
            BOFileSystemManager.getInstance().writeToFile(funnelDownloadLogsFile, sessionFunnelsNewDataStr);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    //Timer logic to run funnel task under fixed frequency
    private void recursivelyDownloadFunnelsUsingURLRequest(String networkRequest) {
        //write job to fetch and send data as frequency
        //Also save files in directory, expired under history/expired, live under live and then unsynced + sync
        //Create direcotry structure
        try {
            this.codifiedAndFunnelsUsingURLRequest(networkRequest, new BOHandlerMessage() {
                @Override
                public void handleMessage(@NonNull BOMessage msg) {
                    if (msg.what == 1) {
                        BOFunnelAndCodifiedEvents allFunnels = (BOFunnelAndCodifiedEvents) msg.obj;

                        if (allFunnels != null && isFunnelsNewDataValid(allFunnels)) {
                            BOFunnelAndCodifiedEvents validEvents = validNewDataFunnels(allFunnels);
                            storeSessionFunnelsNewData(validEvents);
                            BOSharedPreferenceImpl.getInstance().saveLong(BOCommonConstants.BO_ANALYTICS_FUNNEL_LAST_UPDATE_TIME_DEFAULTS_KEY, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                            if (BOFunnelSyncController.this.funnelsAndCodifiedEventsInstance == null) {
                                prepareFunnelSyncAndAnalyser();
                            }
                        }
                    } else {

                    }
                }
            });

            BOSharedPreferenceImpl.getInstance().saveLong(BOCommonConstants.BO_ANALYTICS_FUNNEL_LAST_SYNC_TIME_DEFAULTS_KEY, BODateTimeUtils.get13DigitNumberObjTimeStamp());
            recursivelyDownloadFunnelsAfterDelay(BOSDKManifestController.delayInterval(), "");
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private void recursivelyDownloadFunnelsAfterDelay(long milliSeconds, String request) {

        Runnable recursivelyDownloadRunnable = new Runnable() {
            @Override
            public void run() {
                recursivelyDownloadFunnelsUsingURLRequest(request);
            }
        };
        BONetworkFunnelExecutorHelper.getInstance().postDelayedWithKey(boFunnelRecursiveDownloadHandlerKey, recursivelyDownloadRunnable, milliSeconds);
    }

    private void loadFunnelNetworkScheduler() {
        try {
            long lastSyncTimeStamp = BOSharedPreferenceImpl.getInstance().getLong(BO_ANALYTICS_FUNNEL_LAST_SYNC_TIME_DEFAULTS_KEY);
            if (lastSyncTimeStamp > 0) {
                long updatedSyncTime = BODateTimeUtils.get13DigitNumberObjTimeStamp() - lastSyncTimeStamp;
                long networkSyncTime = BOSDKManifestController.delayInterval();
                long delayNow = ((networkSyncTime - updatedSyncTime) > 0) ? (networkSyncTime - updatedSyncTime) : 0;
                recursivelyDownloadFunnelsAfterDelay(delayNow, "");
            } else {
                recursivelyDownloadFunnelsAfterDelay(0, "");
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private boolean isFunnelsNewDataValid(@NonNull BOFunnelAndCodifiedEvents sessionFunnelsNewData) {
        boolean isAllFunnelValid = false;
        try {
            if (sessionFunnelsNewData.getEventsFunnel() != null && sessionFunnelsNewData.getEventsFunnel().size() > 0) {
                for (BOEventsFunnel eventFunnel : sessionFunnelsNewData.getEventsFunnel()) {
                    if (eventFunnel.getEventList().size() >= 2) {
                        isAllFunnelValid = true;
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return isAllFunnelValid;
    }

    //This methos only filter funnel events and modify objects with only valid funnels
    @NonNull
    private BOFunnelAndCodifiedEvents validNewDataFunnels(@NonNull BOFunnelAndCodifiedEvents sessionFunnelsNewData) {
        BOFunnelAndCodifiedEvents funnelsAndCodifiedEvents = new BOFunnelAndCodifiedEvents();
        try {
            List<BOEventsFunnel> eventFunnelsArr = new ArrayList<>();
            //NSMutableArray<BOAEventsCodified *> *eventCodifiedArr = [NSMutableArray array];
            if (sessionFunnelsNewData.getEventsFunnel() != null && sessionFunnelsNewData.getEventsFunnel().size() > 0) {
                for (BOEventsFunnel eventFunnel : sessionFunnelsNewData.getEventsFunnel()) {
                    if (eventFunnel.getEventList().size() >= 2) {
                        eventFunnelsArr.add(eventFunnel);
                        //[eventCodifiedArr addObject:<#(nonnull BOAEventsCodified *)#>]
                    }
                }
            }
            funnelsAndCodifiedEvents.setEventsCodified(sessionFunnelsNewData.getEventsCodified());
            funnelsAndCodifiedEvents.setEventsFunnel(eventFunnelsArr);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return funnelsAndCodifiedEvents;
    }

    private boolean isFunnnelAvailable() {
        try {
            BOFunnelAndCodifiedEvents fcEvents = this.loadAllActiveFunnels();
            if (fcEvents != null && fcEvents.getEventsFunnel() != null && fcEvents.getEventsFunnel().size() > 0) {
                return true;
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return false;
    }

    private boolean isFunnelContainsValidEvents(BOEventsFunnel funnel) {
        //Check for events count should be greater than 1
        //Check for two consecutive events, should not be same
        //Do not implement now, get confirmation for v2.0 but possible & check for event duplicate orrurance, should not contain duplicate event
        return true;
    }

    private void serverSyncFunnelAtURLRequest(
            @Nullable HashMap<String, Object> jsonDict, @NonNull BOHandlerMessage callback) {

        if (!BlotoutAnalytics_Internal.getInstance().isSDKEnabled && (jsonDict == null || !BlotoutAnalytics_Internal.getInstance().isFunnelEventsEnabled)) {
            //As no request was shared, will create neutral error case later
            callback.handleMessage(new BOHandlerMessage.BOMessage(0, null));
            return;
        }
        try {
            BOFunnelAPI api = BOSharedManager.getInstance().getAPIFactory().funnelAPI;
            Call<Object> call = api.postFunnelData(BOBaseAPI.getInstance().getFunnelFeedback(), jsonDict);
            Response response = call.execute();
            if (response.isSuccessful()) {
                BOCommonEvents.getInstance().recordFunnelTriggered();
                Logger.INSTANCE.e(TAG, "Funnel upload succeed" + jsonDict.toString());
                callback.handleMessage(new BOHandlerMessage.BOMessage(1, null));
            } else {
                callback.handleMessage(new BOHandlerMessage.BOMessage(0, null));
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            callback.handleMessage(new BOHandlerMessage.BOMessage(0, null));
        }
    }

    public HashMap<String, Object> funnelNetworkRequestDict() {
        try {
            long lastUpdateTime = BOSharedPreferenceImpl.getInstance().getLong(BOCommonConstants.BO_ANALYTICS_FUNNEL_LAST_UPDATE_TIME_DEFAULTS_KEY);

            BOAppSessionEvents sessionEvent = BOAppSessionEvents.getInstance();
            HashMap<String, Object> locationDict = sessionEvent.getGeoIPAndPublish(false);

            HashMap<String, Object> metaInfo = this.prepareMetaDataDict(null);
            HashMap<String, Object> funnelSyncGeo = new HashMap<>();
            if (locationDict != null && locationDict.keySet().size() > 0) {
                funnelSyncGeo.put("city", locationDict.get("city"));
                funnelSyncGeo.put("reg", locationDict.get("state"));
                funnelSyncGeo.put("couc", locationDict.get("country"));
                funnelSyncGeo.put("zip", locationDict.get("zip"));
                funnelSyncGeo.put("conc", locationDict.get("continentCode"));
            }

            HashMap<String, Object> events = new HashMap<>();
            events.put(BOCommonConstants.BO_LAST_UPDATED_TIME, 0);  //lastUpdateTime

            HashMap<String, Object> funnelPullPayload = new HashMap<>();
            funnelPullPayload.put("geo", funnelSyncGeo);
            funnelPullPayload.put("meta", metaInfo);
            funnelPullPayload.put("events", events);

            return funnelPullPayload;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return null;
        }
    }

    private void codifiedAndFunnelsUsingURLRequest(@Nullable String funnelsURLReq, @NonNull BOHandlerMessage callback) {

        if (!BlotoutAnalytics_Internal.getInstance().isSDKEnabled || requestInProgress) {
            return;
        }

        try {
            BOFunnelAPI api = BOSharedManager.getInstance().getAPIFactory().funnelAPI;
            Logger.INSTANCE.e(TAG, this.funnelNetworkRequestDict().toString());
            Call<Object> call = api.getFunnelData(BOBaseAPI.getInstance().getFunnelPath(), this.funnelNetworkRequestDict());
            requestInProgress = true;
            Response response = call.execute();
            if (response.isSuccessful()) {
                Logger.INSTANCE.d(TAG, response.body().toString());
                BOFunnelAndCodifiedEvents codifiedAndFunnel = BOFunnelAndCodifiedEvents.fromJsonDictionary((HashMap<String, Object>) response.body());
                if (codifiedAndFunnel != null && codifiedAndFunnel.getEventsFunnel() != null && codifiedAndFunnel.getEventsFunnel().size() > 0) {
                    callback.handleMessage(new BOHandlerMessage.BOMessage(1, codifiedAndFunnel));
                } else {
                    callback.handleMessage(new BOHandlerMessage.BOMessage(0, null));
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            callback.handleMessage(new BOHandlerMessage.BOMessage(0, null));
        }
    }

    public void appLaunchedWithInfo(HashMap<String, Object> launchInfo) {
        try {
            if (BlotoutAnalytics_Internal.getInstance().isFunnelEventsEnabled) {
                this.launchTimeStamp = BODateTimeUtils.get13DigitNumberObjTimeStamp();

                Date lastLaunchDate = BODateTimeUtils.getDateFromString(BOSharedPreferenceImpl.getInstance().getString(BOCommonConstants.BO_ANALYTICS_FUNNEL_APP_LAUNCH_PREV_DAY_DEFAULTS_KEY), BOCommonConstants.BO_DATE_FORMAT);
                String lastLauchDateStr = BODateTimeUtils.getStringFromDate(lastLaunchDate, BOCommonConstants.BO_DATE_FORMAT);

                Date curretDate = BODateTimeUtils.getCurrentDate();
                boolean isDaySame = BODateTimeUtils.isDayMonthAndYearSameOfDate(lastLaunchDate, curretDate);
                boolean isLesserDate = BODateTimeUtils.isDateLessThan(lastLaunchDate, curretDate);
                boolean isConfirmedPreviousDate = !isDaySame && isLesserDate;
                //TODO: comment it once ready for integration, For testing purpose only until ready for Beta testing
                if (isAggregate && lastLauchDateStr != null && isConfirmedPreviousDate) {
                    preapreDailyAggregatedFunnelEventAndSaveToDiskWithServerSyncForDate(lastLauchDateStr);
                }

                //Do for all previous day
                if (lastLauchDateStr != null) {
                    serverSynSessionFunnelPayloadOfDate(null, new BOHandlerMessage() {
                        @Override
                        public void handleMessage(@NonNull BOMessage msg) {
                            if (msg.what == 1) {
                                //Do this once only at App launch using previous day sesssion events, prepare daily aggregation, save it and send it to server as well
                                //We can do after session funnel event sync as after that only session event move to complete
                                //[self preapreDailyAggregatedFunnelEventAndSaveToDiskWithServerSyncForDate:nil];
                                if (isAggregate && lastLauchDateStr != null && isConfirmedPreviousDate) {
                                    preapreDailyAggregatedFunnelEventAndSaveToDiskWithServerSyncForDate(lastLauchDateStr);
                                }
                                serverSynDailyAggregatedFunnelPayload(null, null, null); //check for error & test on all IOS versions
                            } else {
                                //try in next launch or in sometime and if fails again then try alternate file movement
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    public void appInBackgroundWithInfo(HashMap<String, Object> backgroundInfo) {
        if (!BlotoutAnalytics_Internal.getInstance().isFunnelEventsEnabled)
            return;
        //Do it when app goes in background, make changes later
        try {
            analyseAndUpdateFunnelsPayloadForEventsOccuredSoFar();
            checkAndUpdateTraversalCompleteForFunnels();
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    public void appWillTerminatWithInfo(HashMap<String, Object> terminationInfo) {
        try {
            if (!BlotoutAnalytics_Internal.getInstance().isFunnelEventsEnabled)
                return;

            if (!(this.preEventsInfo != null && this.preEventsInfo.keySet().size() > 0))
                return;

            long appTerminationTimeStamp = BODateTimeUtils.get13DigitNumberObjTimeStamp();
            List<String> allKeys = new ArrayList<>(this.preEventsInfo.keySet());
            String preEventName = allKeys.get(allKeys.size() - 1);
            recordEventDurationsFor(preEventName, appTerminationTimeStamp);

            this.terminationTimeStamp = appTerminationTimeStamp;

            String currentDateStr = BODateTimeUtils.getFormattedCurrentDateString(BOCommonConstants.BO_DATE_FORMAT);
            BOSharedPreferenceImpl.getInstance().saveString(BOCommonConstants.BO_ANALYTICS_FUNNEL_APP_LAUNCH_PREV_DAY_DEFAULTS_KEY, currentDateStr);

            storeSessionFunnelMetaInfo();

            //Test this logic of timegap
            long currentTime = BODateTimeUtils.get13DigitNumberObjTimeStamp();
            //Meta info and geo info within single session remains same. practically different geo locations are possible but v1.0 doing this
            HashMap<String, Object> metaInfo = prepareMetaDataDict(BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null));
            HashMap<String, Object> geoInfo = prepareGeoDataDict();

            HashMap<String, Object> payloadFunnelDict = new HashMap<>();
            payloadFunnelDict.put("meta", metaInfo);
            payloadFunnelDict.put("geo", geoInfo);
            payloadFunnelDict.put("fevents", payloadEvents);

            BOFunnelPayload completePayload = BOFunnelPayload.fromJsonDictionary(payloadFunnelDict);
            storeSessionFunnelInfoPayload(completePayload);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }
}
