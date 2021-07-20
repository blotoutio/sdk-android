package com.blotout.network.jobs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.blotout.Controllers.BOSDKManifestController;
import com.blotout.analytics.BOSdkToServerFormat;
import com.blotout.analytics.BOSharedManager;
import com.blotout.constants.BOCommonConstants;
import com.blotout.constants.BOManifestConstants;
import com.blotout.constants.BONetworkConstants;
import com.blotout.events.BOAEvents;
import com.blotout.model.lifetime.BOAppLifetimeData;
import com.blotout.model.session.BOAppSessionDataModel;
import com.blotout.network.BOAPIFactory;
import com.blotout.network.api.BOBaseAPI;
import com.blotout.network.api.BOEventPostAPI;
import com.blotout.network.api.BORetentionEventPostAPI;
import com.blotout.storage.BOFileSystemManager;
import com.blotout.storage.BOSharedPreferenceImpl;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.BODateTimeUtils;
import com.blotout.utilities.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BOAPostEventsDataJob extends BOBaseJob {
    private static final String TAG = BOCommonConstants.TAG_PREFIX + "BOAPostEventsDataJob";
    private static final int BO_SESSION_DATA_TAG = 0;
    private static final int BO_SESSION_RETENTION_DATA_TAG = 1;
    private static final int BO_SESSION_PII_PHI_TAG = 2;
    private static final int BO_LIFETIME_DATA_TAG = 3;

    private static final String FileSentToServerLifeTime = "fileNameDataSentToServerLifeTime";
    private static final String FileSentToServer = "fileNameDataSentToServer";
    public volatile BOAppSessionDataModel sessionObject;
    public String filePath;
    public volatile BOAppLifetimeData appLifetimeData;
    public String filePathLifetimeData;

    public BOAPostEventsDataJob() {
        super(new Params(Priority.LOW).requireNetwork().groupBy("BOA_PostEvents_Data_Job"));
    }

    @Override
    public void onJobRun() throws Throwable {
        if (this.filePath != null) {
            sendEventsUsingRandomizer();
        } else if (this.sessionObject != null) {
            sendEventsUsingSessionObject();
        } else if (this.filePathLifetimeData != null) {
            sendEventsFromLifeTimeModelFileWithRandomiser();
        } else if (this.appLifetimeData != null) {
            sendEventsUsingLifeTimeSessionObjectWithRandomiser();
        }
    }

    /**
     * Map Session file that is transferred to synced file
     *
     * @param fileName String
     */
    private void saveFileNameSentToServer(String fileName, String prefKey) {
        List<Object> finalFileNames = BOSharedPreferenceImpl.getInstance().getSavedListFromPref(prefKey);
        if (finalFileNames != null && !finalFileNames.contains(fileName)) {
            finalFileNames.add(fileName);
            BOSharedPreferenceImpl.getInstance().saveListInPref(prefKey, finalFileNames);
        } else if (finalFileNames == null) {
            finalFileNames = new ArrayList<>();
            finalFileNames.add(fileName);
            BOSharedPreferenceImpl.getInstance().saveListInPref(prefKey, finalFileNames);
        }
    }

    private void moveFileToSyncedFolder(String filePath, String dirPath) {
        //Moved file to synced dir
        try {
            String appSessionString = BOFileSystemManager.getInstance().readContentOfFileAtPath(this.filePath);
            String destFilename = BOCommonUtils.lastPathComponent(filePath);
            String destPath = dirPath + "/" + destFilename;
            boolean fileTransfer = BOFileSystemManager.getInstance().moveFile(filePath, destPath);

            if (!fileTransfer) {
                boolean success = BOFileSystemManager.getInstance().writeToFile(destPath, appSessionString);
                if (success) {
                    BOFileSystemManager.getInstance().deleteFilesAndDir(BOAPostEventsDataJob.this.filePath);
                }
            }
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    /**
     * Send all session events that is not sent to server
     */
    private void sendEvents() {
        try {
            List<Object> fileNames = BOSharedPreferenceImpl.getInstance().getSavedListFromPref(FileSentToServer);
            final String fileName = BOCommonUtils.lastPathComponent(this.filePath);
            if (fileNames != null && fileNames.contains(fileName)) {
                // data already send to server
                moveFileToSyncedFolder(this.filePath,BOAEvents.getSyncedDirectoryPath());
            } else {
                String jsonString = BOFileSystemManager.getInstance().readContentOfFileAtPath(this.filePath);
                final BOAppSessionDataModel appSessionData = BOAppSessionDataModel.fromJsonString(jsonString);
                BOSdkToServerFormat sdkToServerEvent = BOSdkToServerFormat.getInstance();
                HashMap<String, Object> otherEventData = sdkToServerEvent.serverFormatEventsJSONFrom(appSessionData);
                HashMap<String, Object> retentionEvent = sdkToServerEvent.serverFormatRetentionEventsFrom(appSessionData);
                HashMap<String, Object> piiPhiEventsData = BOSdkToServerFormat.getInstance().serverFormatPIIPHIEventsJSONFrom(appSessionData);

                //no event to update
                if(otherEventData == null && retentionEvent == null && piiPhiEventsData == null) {
                    saveFileNameSentToServer(fileName,BOAPostEventsDataJob.FileSentToServer);
                    //Moved file to synced dir
                    moveFileToSyncedFolder(this.filePath,BOAEvents.getSyncedDirectoryPath());
                    return;
                }


                for (int eIndex = 0; eIndex < 3; eIndex++) {
                    HashMap<String, Object> allEventsData = otherEventData;
                    if (eIndex == BO_SESSION_DATA_TAG) {
                        allEventsData = otherEventData;
                    } else if(eIndex == BO_SESSION_RETENTION_DATA_TAG) {
                        allEventsData = retentionEvent;
                    } else if(eIndex == BO_SESSION_PII_PHI_TAG)  {
                        allEventsData = this.encryptedPIIPHIEventsMapData(piiPhiEventsData);
                    }

                    if(allEventsData == null || allEventsData.keySet().size() <= 0) {
                        continue;
                    }

                    Call<Object> call = null;
                    if (eIndex == BO_SESSION_RETENTION_DATA_TAG) {
                        BORetentionEventPostAPI api = BOSharedManager.getInstance().getAPIFactory().retentionAPI;
                        call = api.postJson(BOBaseAPI.getInstance().getRetentionPublish(), allEventsData);
                    } else {
                        BOEventPostAPI eventPostAPI = BOSharedManager.getInstance().getAPIFactory().eventPostAPI;
                        call = eventPostAPI.postJson(BOBaseAPI.getInstance().getEventPost(), allEventsData);
                    }

                    Response resp = call.execute();
                    if (resp.isSuccessful()) {
                        appSessionData.getSingleDaySessions().setLastServerSyncTimeStamp(BODateTimeUtils.get13DigitNumberObjTimeStamp());
                        appSessionData.getSingleDaySessions().setAllEventsSyncTimeStamp(BODateTimeUtils.get13DigitNumberObjTimeStamp());
                        String appSessionString = BOAppSessionDataModel.toJsonString(appSessionData);
                        boolean successs = BOFileSystemManager.getInstance().writeToFile(BOAPostEventsDataJob.this.filePath, appSessionString);

                        saveFileNameSentToServer(fileName,BOAPostEventsDataJob.FileSentToServer);
                        //Moved file to synced dir
                        moveFileToSyncedFolder(this.filePath,BOAEvents.getSyncedDirectoryPath());
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    /**
     * Send All Events Data
     *
     * @param event            Hashmap of all events that we have to sent
     * @param dataType to check for api
     */
    private void sendAllEventsData(HashMap<String, Object> event, int dataType) {
        if (event != null && event.keySet().size() > 0) {
            try {
                BOAPIFactory api = BOSharedManager.getInstance().getAPIFactory();
                Call<Object> call;
                if (dataType == BO_SESSION_RETENTION_DATA_TAG) {
                    call = api.retentionAPI.postJson(BOBaseAPI.getInstance().getRetentionPublish(), event);
                }else if(dataType == BO_SESSION_PII_PHI_TAG) {
                    HashMap<String, Object> encryptedEventData = this.encryptedPIIPHIEventsMapData(event);
                    call = api.eventPostAPI.postJson(BOBaseAPI.getInstance().getEventPost(), encryptedEventData);
                } else {
                    call = api.eventPostAPI.postJson(BOBaseAPI.getInstance().getEventPost(), event);
                }

                Response resp = call.execute();
                if (resp.isSuccessful()) {
                    eventsAfterSyncProcessingFor(event,this.sessionObject,null,dataType);
                    BOAPostEventsDataJob.this.sessionObject.getSingleDaySessions().setLastServerSyncTimeStamp(BODateTimeUtils.get13DigitNumberObjTimeStamp());
                }
            } catch (IOException e) {
                Logger.INSTANCE.e(TAG, e.toString());
            }
        }
    }

    /**
     * Send Event using Session Object based to time interval set on server
     */
    public void sendEventsUsingSessionObject() {
        try {
            BOAppSessionDataModel appSessionData = this.sessionObject;
            BOSdkToServerFormat sdkToServerEvent = BOSdkToServerFormat.getInstance();
            boolean isGroupingEnabled = true;
            int groupingSize = getGroupingSize(); //2; //-1 will make isGroupingSizeAll to YES
            boolean isGroupingSizeAll = getGroupingSize() == -1; //when size value will be -1 then this is true
            if (isGroupingEnabled && isGroupingSizeAll) {
                HashMap<String, Object> event = sdkToServerEvent.serverFormatEventsJSONFrom(appSessionData);
                this.sendAllEventsData(event, BO_SESSION_DATA_TAG);
                HashMap<String, Object> retentionEvent = sdkToServerEvent.serverFormatRetentionEventsFrom(appSessionData);
                this.sendAllEventsData(retentionEvent, BO_SESSION_RETENTION_DATA_TAG);
                HashMap<String, Object> piiPhiEvent = sdkToServerEvent.serverFormatPIIPHIEventsJSONFrom(appSessionData);
                this.sendAllEventsData(piiPhiEvent, BO_SESSION_PII_PHI_TAG);
                return;
            }

            HashMap<String, Object> serverEvents = BOSdkToServerFormat.getInstance().serverFormatEventsJSONFrom(appSessionData);
            HashMap<String, Object> serverRetentionEvents = BOSdkToServerFormat.getInstance().serverFormatRetentionEventsFrom(appSessionData);
            HashMap<String, Object> piiPhiEventsData = sdkToServerEvent.serverFormatPIIPHIEventsJSONFrom(appSessionData);

            for (int eIndex = 0; eIndex < 3; eIndex++) {

                List<HashMap<String, Object>> randomGroupedServerEvents = new ArrayList<>();
                if (eIndex == BO_SESSION_DATA_TAG) {
                    if(serverEvents != null && serverEvents.keySet().size() >0) {
                        randomGroupedServerEvents = this.randomisedEventsDataWithGroupSize(serverEvents, groupingSize, false);
                    } else {
                        continue;
                    }
                } else if(eIndex == BO_SESSION_RETENTION_DATA_TAG) {
                    if(serverRetentionEvents != null && serverRetentionEvents.keySet().size() > 0) {
                        randomGroupedServerEvents = this.randomisedEventsDataWithGroupSize(serverRetentionEvents, groupingSize, true);
                    } else {
                        continue;
                    }
                } else if(eIndex == BO_SESSION_PII_PHI_TAG)  {

                    if (piiPhiEventsData != null && piiPhiEventsData.size() > 0) {
                        randomGroupedServerEvents = this.encryptPIIPHIEventsListData(piiPhiEventsData);
                    } else {
                        continue;
                    }
                }

                for (HashMap<String, Object> singleGroup : randomGroupedServerEvents) {

                    if (singleGroup != null && singleGroup.keySet().size() > 0) {

                        Call<Object> call;

                        if (eIndex == BO_SESSION_RETENTION_DATA_TAG) {
                            BORetentionEventPostAPI api = BOSharedManager.getInstance().getAPIFactory().retentionAPI;
                            call = api.postJson(BOBaseAPI.getInstance().getRetentionPublish(), singleGroup);
                        } else  {
                            BOEventPostAPI eventPostAPI = BOSharedManager.getInstance().getAPIFactory().eventPostAPI;
                            call = eventPostAPI.postJson(BOBaseAPI.getInstance().getEventPost(), singleGroup);
                        }
                        Response resp = call.execute();
                        if (resp.isSuccessful()) {
                            Logger.INSTANCE.d(TAG, "PostDataSuccessful");
                            appSessionData.getSingleDaySessions().setLastServerSyncTimeStamp(BODateTimeUtils.get13DigitNumberObjTimeStamp());
                            if(eIndex == BO_SESSION_PII_PHI_TAG) {
                                eventsAfterSyncProcessingFor(piiPhiEventsData,this.sessionObject,null,eIndex);
                            } else {
                                eventsAfterSyncProcessingFor(singleGroup,this.sessionObject,null,eIndex);
                            }
                        } else {
                            Logger.INSTANCE.d(TAG, resp.errorBody() != null ? resp.errorBody().toString() : "API error");
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    /**
     * Send Random Event using Session Object based to time interval set on server
     */
    public void sendEventsUsingRandomizer() {
        try {

            List<Object> fileNames = BOSharedPreferenceImpl.getInstance().getSavedListFromPref(FileSentToServer);

            final String fileName = BOCommonUtils.lastPathComponent(this.filePath);
            if (fileNames != null && fileNames.contains(fileName)) {
                // data already send to server
            } else {
                String jsonString = BOFileSystemManager.getInstance().readContentOfFileAtPath(this.filePath);
                final BOAppSessionDataModel appSessionData = BOAppSessionDataModel.fromJsonString(jsonString);
                boolean isGroupingEnabled = true;
                int groupingSize = getGroupingSize(); //2; //-1 will make isGroupingSizeAll to YES
                boolean isGroupingSizeAll = getGroupingSize() == -1; //when size value will be -1 then this is true
                if (isGroupingEnabled && isGroupingSizeAll) {
                    this.sendEvents();
                    return;
                }

                HashMap<String, Object> serverEvents = BOSdkToServerFormat.getInstance().serverFormatEventsJSONFrom(appSessionData);
                HashMap<String, Object> serverRetentionEvents = BOSdkToServerFormat.getInstance().serverFormatRetentionEventsFrom(appSessionData);
                HashMap<String, Object> piiPhiEventsData = BOSdkToServerFormat.getInstance().serverFormatPIIPHIEventsJSONFrom(appSessionData);

                //no event to update
                if(serverEvents == null && serverRetentionEvents == null && piiPhiEventsData == null) {
                    saveFileNameSentToServer(fileName,BOAPostEventsDataJob.FileSentToServer);
                    //Moved file to synced dir
                    moveFileToSyncedFolder(this.filePath,BOAEvents.getSyncedDirectoryPath());
                    return;
                }

                int totalEventCount = 0;
                if(serverEvents != null && serverEvents.keySet().size()>0) {
                    List<HashMap<String, Object>> eventsData = (List<HashMap<String, Object>>) serverEvents.get(BONetworkConstants.BO_EVENTS);
                    totalEventCount = eventsData != null ? eventsData.size() : 0;
                }

                if(serverRetentionEvents != null && serverRetentionEvents.keySet().size() >0 ){
                    List<HashMap<String, Object>> retentionEventsData = (List<HashMap<String, Object>>) serverRetentionEvents.get(BONetworkConstants.BO_EVENTS);
                    int retentionDataCount = retentionEventsData != null ? retentionEventsData.size() : 0;
                    totalEventCount = totalEventCount + retentionDataCount;
                }

                //add 1 event for PII & PHI Data
                if(piiPhiEventsData != null && piiPhiEventsData.keySet().size() >0) {
                    totalEventCount = totalEventCount + 1;
                }


                for (int eIndex = 0; eIndex < 3; eIndex++) {

                    List<HashMap<String, Object>> randomGroupedServerEvents = new ArrayList<>();
                    if (eIndex == BO_SESSION_DATA_TAG) {
                        if (serverEvents != null && serverEvents.keySet().size() > 0) {
                            randomGroupedServerEvents = this.randomisedEventsDataWithGroupSize(serverEvents, groupingSize, false);
                        } else {
                            continue;
                        }
                    } else if(eIndex == BO_SESSION_RETENTION_DATA_TAG) {
                        if (serverRetentionEvents != null && serverRetentionEvents.keySet().size() > 0) {
                            randomGroupedServerEvents = this.randomisedEventsDataWithGroupSize(serverRetentionEvents, groupingSize, true);
                        } else {
                            continue;
                        }
                    } else if(eIndex == BO_SESSION_PII_PHI_TAG)  {

                        if (piiPhiEventsData != null && piiPhiEventsData.size() > 0) {
                            randomGroupedServerEvents = this.encryptPIIPHIEventsListData(piiPhiEventsData);
                        } else  {
                            continue;
                        }
                    }

                    for (HashMap<String, Object> singleGroup : randomGroupedServerEvents) {
                        List<HashMap<String, Object>> singleGroupEvents = (List<HashMap<String, Object>>) singleGroup.get(BONetworkConstants.BO_EVENTS);

                        if (singleGroup != null && singleGroup.keySet().size() > 0) {

                            Call<Object> call = null;

                            if (eIndex == BO_SESSION_RETENTION_DATA_TAG) {
                                BORetentionEventPostAPI api = BOSharedManager.getInstance().getAPIFactory().retentionAPI;
                                call = api.postJson(BOBaseAPI.getInstance().getRetentionPublish(), singleGroup);
                            } else {
                                BOEventPostAPI eventPostAPI = BOSharedManager.getInstance().getAPIFactory().eventPostAPI;
                                call = eventPostAPI.postJson(BOBaseAPI.getInstance().getEventPost(), singleGroup);
                            }
                            Response resp = call.execute();
                            if (resp.isSuccessful()) {

                                appSessionData.getSingleDaySessions().setLastServerSyncTimeStamp(BODateTimeUtils.get13DigitNumberObjTimeStamp());
                                appSessionData.getSingleDaySessions().setAllEventsSyncTimeStamp(BODateTimeUtils.get13DigitNumberObjTimeStamp());
                                if(eIndex == BO_SESSION_PII_PHI_TAG) {
                                    eventsAfterSyncProcessingFor(piiPhiEventsData,appSessionData,null, eIndex);
                                    totalEventCount = totalEventCount - 1;
                                } else {
                                    eventsAfterSyncProcessingFor(singleGroup,appSessionData,null, eIndex);
                                    totalEventCount = totalEventCount - singleGroupEvents.size();
                                }

                                String appSessionString = BOAppSessionDataModel.toJsonString(appSessionData);
                                boolean successs = BOFileSystemManager.getInstance().writeToFile(BOAPostEventsDataJob.this.filePath, appSessionString);

                                //Moved file to synced dir
                                if (totalEventCount == 0) {

                                    saveFileNameSentToServer(fileName,BOAPostEventsDataJob.FileSentToServer);
                                    moveFileToSyncedFolder(this.filePath,BOAEvents.getSyncedDirectoryPath());

                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    /**
     * Send Lifetime Event using Lifetime file based to time interval set on server
     */
    public void sendEventsFromLifeTimeModelFileWithRandomiser() {
        try {
            List<Object> fileNames = BOSharedPreferenceImpl.getInstance().getSavedListFromPref(FileSentToServerLifeTime);
            final String fileName = BOCommonUtils.lastPathComponent(this.filePathLifetimeData);
            if (fileNames != null && fileNames.contains(fileName)) {
                // data already send to server
            } else {

                String jsonString = BOFileSystemManager.getInstance().readContentOfFileAtPath(this.filePathLifetimeData);
                final BOAppLifetimeData appLifeTimeData = BOAppLifetimeData.fromJsonString(jsonString);
                BOSdkToServerFormat sdkToServerEvent = BOSdkToServerFormat.getInstance();

                boolean isGroupingEnabled = true;
                int groupingSize = getGroupingSize(); //2; //-1 will make isGroupingSizeAll to YES
                boolean isGroupingSizeAll = getGroupingSize() == -1; //when size value will be -1 then this is true
                if (isGroupingEnabled && isGroupingSizeAll) {
                    sendEventsFromLifeTimeModelFile();
                    return;
                }

                HashMap<String, Object> serverEvents = sdkToServerEvent.serverFormatLifeTimeEventsJSONFrom(appLifeTimeData);
                HashMap<String, Object> serverRetentionEvents = sdkToServerEvent.serverFormatLifeTimeRetentionEventsJSONFrom(appLifeTimeData);

                //no event to update
                if(serverEvents == null && serverRetentionEvents == null ) {
                    saveFileNameSentToServer(fileName,BOAPostEventsDataJob.FileSentToServerLifeTime);
                    //Moved file to synced dir
                    moveFileToSyncedFolder(this.filePathLifetimeData,BOAEvents.getLifeTimeDataSyncedDirectoryPath());
                    return;
                }


                int totalEventCount = 0;
                if(serverEvents != null && serverEvents.keySet().size() > 0) {
                    List<HashMap<String, Object>> eventsData = (List<HashMap<String, Object>>) serverEvents.get(BONetworkConstants.BO_EVENTS);
                    totalEventCount = eventsData != null ? eventsData.size() : 0;
                }

                if(serverRetentionEvents != null && serverRetentionEvents.keySet().size() > 0) {
                    List<HashMap<String, Object>> retentionEventsData = (List<HashMap<String, Object>>) serverRetentionEvents.get(BONetworkConstants.BO_EVENTS);
                    int retentionDataCount = retentionEventsData != null ? retentionEventsData.size() : 0;
                     totalEventCount = totalEventCount + retentionDataCount;
                }


                for (int eIndex = 0; eIndex < 2; eIndex++) {
                    List<HashMap<String, Object>> randomGroupedServerEvents = new ArrayList<>();
                    if (eIndex == 0) {
                        if (serverEvents != null && serverEvents.keySet().size() > 0) {
                            randomGroupedServerEvents = this.randomisedEventsDataWithGroupSize(serverEvents, groupingSize, false);
                        } else {
                            continue;
                        }
                    } else {
                        if (serverRetentionEvents != null && serverRetentionEvents.keySet().size() > 0) {
                            randomGroupedServerEvents = this.randomisedEventsDataWithGroupSize(serverRetentionEvents, groupingSize, true);
                        } else {
                            continue;
                        }
                    }

                    for (HashMap<String, Object> singleGroup : randomGroupedServerEvents) {
                        List<HashMap<String, Object>> singleGroupEvents = (List<HashMap<String, Object>>) singleGroup.get(BONetworkConstants.BO_EVENTS);
                        if (singleGroupEvents != null && singleGroupEvents.size() > 0) {

                            Call<Object> call;

                            if (eIndex == BO_SESSION_RETENTION_DATA_TAG) {
                                BORetentionEventPostAPI api = BOSharedManager.getInstance().getAPIFactory().retentionAPI;
                                call = api.postJson(BOBaseAPI.getInstance().getRetentionPublish(), singleGroup);
                            } else {
                                BOEventPostAPI eventPostAPI = BOSharedManager.getInstance().getAPIFactory().eventPostAPI;
                                call = eventPostAPI.postJson(BOBaseAPI.getInstance().getEventPost(), singleGroup);
                            }

                            Response resp = call.execute();
                            if (resp.isSuccessful()) {

                                appLifeTimeData.setLastServerSyncTimeStamp(BODateTimeUtils.get13DigitNumberObjTimeStamp());
                                appLifeTimeData.setAllEventsSyncTimeStamp(BODateTimeUtils.get13DigitNumberObjTimeStamp());
                                eventsAfterSyncProcessingFor(singleGroup, null,appLifeTimeData,BO_LIFETIME_DATA_TAG);
                                totalEventCount = totalEventCount - singleGroupEvents.size();

                                String appSessionString = BOAppLifetimeData.toJsonString(appLifeTimeData);
                                boolean successs = BOFileSystemManager.getInstance().writeToFile(BOAPostEventsDataJob.this.filePathLifetimeData, appSessionString);


                                if (totalEventCount == 0) {
                                    String date = appLifeTimeData.getDate();
                                    boolean isSameMonth = BODateTimeUtils.isMonthAndYearSameOfDate(BODateTimeUtils.getCurrentDate(), date, BOCommonConstants.BO_DATE_FORMAT);
                                    if (!isSameMonth) {
                                        // display AccountInfo on UI
                                        saveFileNameSentToServer(fileName,BOAPostEventsDataJob.FileSentToServerLifeTime);
                                        moveFileToSyncedFolder(this.filePathLifetimeData,BOAEvents.getLifeTimeDataSyncedDirectoryPath());
                                    }
                                }
                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    /**
     * Send Event using Lifetime Session file
     */
    private void sendEventsFromLifeTimeModelFile() {
        try {
            List<Object> fileNames = BOSharedPreferenceImpl.getInstance().getSavedListFromPref(FileSentToServerLifeTime);
            final String fileName = BOCommonUtils.lastPathComponent(this.filePathLifetimeData);
            if (fileNames != null && fileNames.contains(fileName)) {
                // data already send to server
                moveFileToSyncedFolder(this.filePathLifetimeData,BOAEvents.getLifeTimeDataSyncedDirectoryPath());
            } else {
                String jsonString = BOFileSystemManager.getInstance().readContentOfFileAtPath(this.filePathLifetimeData);
                final BOAppLifetimeData appLifeTimeData = BOAppLifetimeData.fromJsonString(jsonString);
                BOSdkToServerFormat sdkToServerEvent = BOSdkToServerFormat.getInstance();
                HashMap<String, Object> otherEventData = sdkToServerEvent.serverFormatLifeTimeEventsJSONFrom(appLifeTimeData);
                HashMap<String, Object> retentionEvent = sdkToServerEvent.serverFormatLifeTimeRetentionEventsJSONFrom(appLifeTimeData);

                //no event to update
                if(otherEventData == null && retentionEvent == null ) {
                    saveFileNameSentToServer(fileName,BOAPostEventsDataJob.FileSentToServerLifeTime);
                    //Moved file to synced dir
                    moveFileToSyncedFolder(this.filePathLifetimeData,BOAEvents.getLifeTimeDataSyncedDirectoryPath());
                    return;
                }

                for (int eIndex = 0; eIndex < 2; eIndex++) {
                    HashMap<String, Object> allEventsData = otherEventData;
                    if (eIndex == BO_SESSION_RETENTION_DATA_TAG) {
                        allEventsData = retentionEvent;
                    }

                    if(allEventsData == null || allEventsData.keySet().size() <= 0) {
                        continue;
                    }

                    Call<Object> call = null;
                    if (eIndex == BO_SESSION_RETENTION_DATA_TAG) {
                        BORetentionEventPostAPI api = BOSharedManager.getInstance().getAPIFactory().retentionAPI;
                        call = api.postJson(BOBaseAPI.getInstance().getRetentionPublish(), allEventsData);
                    } else {
                        BOEventPostAPI eventPostAPI = BOSharedManager.getInstance().getAPIFactory().eventPostAPI;
                        call = eventPostAPI.postJson(BOBaseAPI.getInstance().getEventPost(), allEventsData);
                    }

                    Response resp = call.execute();
                    if (resp.isSuccessful()) {
                        appLifeTimeData.setLastServerSyncTimeStamp(BODateTimeUtils.get13DigitNumberObjTimeStamp());
                        appLifeTimeData.setAllEventsSyncTimeStamp(BODateTimeUtils.get13DigitNumberObjTimeStamp());
                        eventsAfterSyncProcessingFor(allEventsData,null,appLifeTimeData,BO_LIFETIME_DATA_TAG);

                        String appSessionString = BOAppLifetimeData.toJsonString(appLifeTimeData);
                        boolean successs = BOFileSystemManager.getInstance().writeToFile(BOAPostEventsDataJob.this.filePathLifetimeData, appSessionString);
                        String date = appLifeTimeData.getDate();
                        boolean isSameMonth = BODateTimeUtils.isMonthAndYearSameOfDate(BODateTimeUtils.getCurrentDate(), date, BOCommonConstants.BO_DATE_FORMAT);
                        if (!isSameMonth) {
                            // display AccountInfo on UI
                            saveFileNameSentToServer(fileName,BOAPostEventsDataJob.FileSentToServerLifeTime);
                            //Moved file to synced dir
                            moveFileToSyncedFolder(this.filePathLifetimeData,BOAEvents.getLifeTimeDataSyncedDirectoryPath());
                        }
                    }
                }

            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    /**
     * Send Lifetime Random Event using Lifetime Object based to time interval set on server
     */
    public void sendEventsUsingLifeTimeSessionObjectWithRandomiser() {
        try {
            BOAppLifetimeData appSessionData = this.appLifetimeData;
            BOSdkToServerFormat sdkToServerEvent = BOSdkToServerFormat.getInstance();
            boolean isGroupingEnabled = true;
            int groupingSize = getGroupingSize(); //2; //-1 will make isGroupingSizeAll to YES
            boolean isGroupingSizeAll = getGroupingSize() == -1; //when size value will be -1 then this is true
            if (isGroupingEnabled && isGroupingSizeAll) {
                HashMap<String, Object> event = sdkToServerEvent.serverFormatLifeTimeEventsJSONFrom(appSessionData);
                this.sendAllLifeTimeEventsData(event, false);
                HashMap<String, Object> retentionEvent = sdkToServerEvent.serverFormatLifeTimeRetentionEventsJSONFrom(appSessionData);
                this.sendAllLifeTimeEventsData(retentionEvent, true);
                return;
            }

            HashMap<String, Object> serverEvents = BOSdkToServerFormat.getInstance().serverFormatLifeTimeEventsJSONFrom(appSessionData);
            HashMap<String, Object> serverRetentionEvents = BOSdkToServerFormat.getInstance().serverFormatLifeTimeRetentionEventsJSONFrom(appSessionData);

            for (int eIndex = 0; eIndex < 2; eIndex++) {
                List<HashMap<String, Object>> randomGroupedServerEvents = new ArrayList<>();
                if (eIndex == 0) {
                    if (serverEvents != null && serverEvents.keySet().size() > 0) {
                        randomGroupedServerEvents = this.randomisedEventsDataWithGroupSize(serverEvents, groupingSize, false);
                    } else {
                        continue;
                    }
                } else {
                    if (serverRetentionEvents != null && serverRetentionEvents.keySet().size() > 0) {
                        randomGroupedServerEvents = this.randomisedEventsDataWithGroupSize(serverRetentionEvents, groupingSize, true);
                    } else {
                        continue;
                    }
                }
                for (HashMap<String, Object> singleGroup : randomGroupedServerEvents) {
                    List<HashMap<String, Object>> singleGroupEventsData = (List<HashMap<String, Object>>) singleGroup.get(BONetworkConstants.BO_EVENTS);
                    if (singleGroupEventsData != null && singleGroupEventsData.size() > 0) {

                        Call<Object> call;

                        if (eIndex == BO_SESSION_RETENTION_DATA_TAG) {
                            BORetentionEventPostAPI api = BOSharedManager.getInstance().getAPIFactory().retentionAPI;
                            call = api.postJson(BOBaseAPI.getInstance().getRetentionPublish(), singleGroup);
                        } else {
                            BOEventPostAPI eventPostAPI = BOSharedManager.getInstance().getAPIFactory().eventPostAPI;
                            call = eventPostAPI.postJson(BOBaseAPI.getInstance().getEventPost(), singleGroup);
                        }
                        Response resp = call.execute();
                        if (resp.isSuccessful()) {
                            Logger.INSTANCE.d(TAG, "PostDataSuccessful");
                            appSessionData.setLastServerSyncTimeStamp(BODateTimeUtils.get13DigitNumberObjTimeStamp());
                            eventsAfterSyncProcessingFor(singleGroup,null,this.appLifetimeData,BO_LIFETIME_DATA_TAG);
                        } else {
                            Logger.INSTANCE.d(TAG, resp.errorBody() != null ? resp.errorBody().toString() : "API error");
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private void sendAllLifeTimeEventsData(HashMap<String, Object> event, boolean isRetentionEvent) {
        if (event != null && event.keySet().size() > 0) {
            try {
                BOAPIFactory api = BOSharedManager.getInstance().getAPIFactory();
                Call<Object> call;
                if (isRetentionEvent) {
                    call = api.retentionAPI.postJson(BOBaseAPI.getInstance().getRetentionPublish(), event);
                } else {
                    call = api.eventPostAPI.postJson(BOBaseAPI.getInstance().getEventPost(), event);
                }
                Response resp = call.execute();
                if (resp.isSuccessful()) {
                    eventsAfterSyncProcessingFor(event, null,this.appLifetimeData,BO_LIFETIME_DATA_TAG);
                    BOAPostEventsDataJob.this.appLifetimeData.setLastServerSyncTimeStamp(BODateTimeUtils.get13DigitNumberObjTimeStamp());
                }
            } catch (Exception e) {
                Logger.INSTANCE.e(TAG, e.toString());
            }
        }
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Nullable
    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }

    /**
     * Event Randomize
     *
     * @param serverEvents     Event Dara
     * @param groupSize        Grouping Size
     * @param isRetentionEvent EventType
     * @return Hashmap
     */
    @NonNull
    private List<HashMap<String, Object>> randomisedEventsDataWithGroupSize(@NonNull HashMap<String, Object> serverEvents, int groupSize, boolean isRetentionEvent) {

        List<HashMap<String, Object>> serverEventResized = new ArrayList<>();
        try {
            List<HashMap<String, Object>> allEvents = (List<HashMap<String, Object>>) serverEvents.get(BONetworkConstants.BO_EVENTS);
            int groupingSize = groupSize; //2;
            //int loopCounter = (int)ceil(serverEvents.events.count / groupingSize);
            do {
                List<HashMap<String, Object>> groupedSizedEvents = new ArrayList<>();
                if (allEvents != null && allEvents.size() > groupingSize) {
                    for (int i = 0; i < groupingSize; i++) {
                        int lowerBound = 0;
                        int upperBound = (int) allEvents.size();
                        int rndValue = (int) (lowerBound + Math.random() % (upperBound - lowerBound));
                        groupedSizedEvents.add(allEvents.get(rndValue));
                        allEvents.remove(rndValue);
                    }
                } else {
                    groupedSizedEvents.addAll(allEvents);
                    allEvents.clear();
                }

                HashMap<String, Object> serverEventsL = new HashMap<>();
                serverEventsL.put(BONetworkConstants.BO_META, serverEvents.get(BONetworkConstants.BO_META));
                serverEventsL.put(BONetworkConstants.BO_GEO, serverEvents.get(BONetworkConstants.BO_GEO));
                serverEventsL.put(BONetworkConstants.BO_PMETA, isRetentionEvent ? serverEvents.get(BONetworkConstants.BO_PMETA) : null);
                serverEventsL.put(BONetworkConstants.BO_EVENTS, groupedSizedEvents);

                serverEventResized.add(serverEventsL);

            } while (allEvents != null && (allEvents.size() > 0));
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return serverEventResized;
    }

    /**
     * Event Randomize
     *
     * @param serverEvents PII and PHI Events
     * @return Hashmap
     */
    @NonNull
    private List<HashMap<String, Object>> encryptPIIPHIEventsListData(@NonNull HashMap<String, Object> serverEvents) {

        List<HashMap<String, Object>> serverEventResized = new ArrayList<>();

        if(serverEvents != null && serverEvents.keySet().size() > 0) {
            try {
                HashMap<String, Object> piiEvents = (HashMap<String, Object>) serverEvents.get(BONetworkConstants.BO_PII);
                HashMap<String, Object> phiEvents = (HashMap<String, Object>) serverEvents.get(BONetworkConstants.BO_PHI);

                HashMap<String, Object> serverEventsL = new HashMap<>();
                serverEventsL.put(BONetworkConstants.BO_META, serverEvents.get(BONetworkConstants.BO_META));
                serverEventsL.put(BONetworkConstants.BO_GEO, serverEvents.get(BONetworkConstants.BO_GEO));
                serverEventsL.put(BONetworkConstants.BO_PII, piiEvents);
                serverEventsL.put(BONetworkConstants.BO_PHI, phiEvents);

                serverEventResized.add(serverEventsL);

            } catch (Exception e) {
                Logger.INSTANCE.e(TAG, e.toString());
            }
            return serverEventResized;
        }

        return serverEventResized;
    }

    /**
     * Event Randomize
     *
     * @param serverEvents PII and PHI Events
     * @return Hashmap
     */
    @NonNull
    private HashMap<String, Object> encryptedPIIPHIEventsMapData(@NonNull HashMap<String, Object> serverEvents) {

        try {
            if(serverEvents != null && serverEvents.keySet().size() > 0) {

                HashMap<String, Object> piiEvents = (HashMap<String, Object>) serverEvents.get(BONetworkConstants.BO_PII);
                HashMap<String, Object> phiEvents = (HashMap<String, Object>) serverEvents.get(BONetworkConstants.BO_PHI);

                HashMap<String, Object> serverEventMap = new HashMap<>();

                serverEventMap.put(BONetworkConstants.BO_META, serverEvents.get(BONetworkConstants.BO_META));
                serverEventMap.put(BONetworkConstants.BO_GEO, serverEvents.get(BONetworkConstants.BO_GEO));
                serverEventMap.put(BONetworkConstants.BO_PII, piiEvents);
                serverEventMap.put(BONetworkConstants.BO_PHI, phiEvents);
                return serverEventMap;
            } else {
                return null;
            }

        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return null;
        }
    }

    private int getGroupingSize() {
        int mergeCounter = BOSDKManifestController.getInstance().eventCodifiedMergeCounter;
        return mergeCounter > 0 ? mergeCounter : 1;
    }

    public void eventsAfterSyncProcessingFor(HashMap<String, Object> eventsGroup, BOAppSessionDataModel sessionObject, BOAppLifetimeData lifetimeData, int dataType) {
        try {
            if (dataType == BO_SESSION_DATA_TAG || dataType == BO_SESSION_RETENTION_DATA_TAG) {
                BOSDKServerPostSyncEventConfiguration.getInstance().sessionObject = sessionObject;
                BOSDKServerPostSyncEventConfiguration.getInstance().updateSentToServerForSessionEvents(eventsGroup);
            } else if(dataType == BO_LIFETIME_DATA_TAG) {
                BOSDKServerPostSyncEventConfiguration.getInstance().lifetimeDataObject = lifetimeData;
                BOSDKServerPostSyncEventConfiguration.getInstance().updateSentToServerForLifeTimeEvents(eventsGroup);
            } else if(dataType == BO_SESSION_PII_PHI_TAG) {
                BOSDKServerPostSyncEventConfiguration.getInstance().sessionObject = sessionObject;
                BOSDKServerPostSyncEventConfiguration.getInstance().updatePIIPHIEvents(eventsGroup,sessionObject);
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }
}
