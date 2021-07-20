package com.blotout.storage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.util.List;

public interface IBOFileSystemManager {

    boolean isSDCardAvailable();

    @NonNull
    String getAvailableInternalMemory();

    @NonNull
    String getTotalInternalMemory();

    @NonNull
    String getAvailableExternalMemory();

    @NonNull
    String getTotalExternalMemory();

    void createRootDirectory();

    boolean downloadContent(String url, File file);

    boolean writeToFile(String fileName, String content);

    boolean writeToFile(String fileName, byte[] content);
    void deleteFilesAndDir(String path);

    boolean checkFileExist(String path);

    boolean isMediaRemoved(String path);

    @Nullable
    String getBOSDKRootDirectory();

    @Nullable
    String getExternalDirectoryPath();

    @NonNull
    List<String> getAllDirectories(String path);

    @Nullable
    String getFilePathExtention(String fileName);

    @NonNull
    List<String> getAllFileAndDirectories(String path);
    @NonNull
    String getFileName(String filePath);
    @NonNull
    List<String> getAllFilesWithExtension(String path, String fileExtension);
    @NonNull
    List<File> getAllFilesWithoutExtension(String path);
    @Nullable
    String createDirectoryIfRequiredAndReturnPath(String path);

    @NonNull
    String readContentOfFileAtPath(String filePath)throws Exception;
    boolean moveFile(String sourceFilePath, String destFilePath);
    boolean moveFile(File srcFile, File destFile);
    boolean copyFile(String sourceFilePath, String destFilePath);

    boolean isFirstLaunchBOSDKFileSystemCheck();
    boolean isAppFirstLaunchFileSystemChecks();

    @Nullable
    String getFilePathAfterDeletingPathExtention(@NonNull String fileName);

    @Nullable
    String getLastPathComponent(String fileName);

    @NonNull
    String stringByDeletingLastPathComponent(String path);

    @Nullable
    String getBOFNetworkDownloadsDirectoryPath();

    @Nullable
    String getBOFNetworkDownloadsDirectoryPossibleExistancePath();

    @Nullable
    String getBOSDKVolatileRootDirectoryPossibleExistancePath();
    @Nullable
    String getBOSDKVolatileRootDirectoryPath();
    @Nullable
    String getBOSDKNonVolatileRootDirectoryPossibleExistancePath();
    @Nullable
    String getBOSDKNonVolatileRootDirectoryPath();
    @Nullable
    String getFunnelRootDirectoryPath();
    @Nullable
    String getEventsRootDirectoryPath();

    @Nullable
    String getSegmentsRootDirectoryPath();

    @Nullable
    String getCampaignsRootDirectoryPath();
    @Nullable
    String getNetworkDownloadsFunnelDirectoryPath();

    @Nullable
    String getNetworkDownloadsSegmentsDirectoryPath();

    @Nullable
    String getArchivedFunnelsDirectoryPath();
    @Nullable
    String getLifeTimeDataEventsDirectoryPath();
    @Nullable
    String getSessionDataEventsDirectoryPath();
    @Nullable
    String getActiveFunnelsDirectoryPath();
    @Nullable
    String getExpiredFunnelsDirectoryPath();
    @Nullable
    String getInActiveFunnelsDirectoryPath();

    @Nullable
    String getActiveSegmentsDirectoryPath();

    @Nullable
    String getExpiredSegmentsDirectoryPath();

    @Nullable
    String getInActiveSegmentsDirectoryPath();

    @Nullable
    String getSyncedFilesLifeTimeEventsDirectoryPath();
    @Nullable
    String getNotSyncedFilesLifeTimeEventsDirectoryPath();

    @Nullable
    String getSyncedFilesSessionTimeEventsDirectoryPath();

    @Nullable
    String getNotSyncedFilesSessionTimeEventsDirectoryPath();

    @Nullable
    String getAllFunnelsToAnalyseDirectoryPath();

    @Nullable
    String getServerSyncCompleteFunnelEventsDirectoryPath();

    @Nullable
    String getServerSyncPendingFunnelEventsDirectoryPath();

    @Nullable
    String getAllSegmentsToAnalyseDirectoryPath();

    @Nullable
    String getServerSyncCompleteSegmentsEventsDirectoryPath();

    @Nullable
    String getServerSyncPendingSegmentsEventsDirectoryPath();

    @Nullable
    String getLogLevelDirAllFunnelsToAnalyseDirectoryPath();

    @Nullable
    String getSessionBasedFunnelEventsSyncPendingDirectoryPath();

    @Nullable
    String getDailyAggregatedFunnelEventsSyncPendingDirectoryPath();

    @Nullable
    String getSessionBasedFunnelEventsSyncCompleteDirectoryPath();

    @Nullable
    String getDailyAggregatedFunnelEventsSyncCompleteDirectoryPath();

    @Nullable
    String getSyncPendingSessionFunnelMetaInfoDirectoryPath();

    @Nullable
    String getSyncPendingSessionFunnelInfoDirectoryPath();

    @Nullable
    String getSyncCompleteSessionFunnelMetaInfoDirectoryPath();
    @Nullable
    String getSyncCompleteSessionFunnelInfoDirectoryPath();

    @Nullable
    String getSyncPendingSessionFunnelMetaInfoDirectoryPathForDate(String dateString);

    @Nullable
    String getSyncPendingSessionFunnelInfoDirectoryPathForFunnelID(String funnelID);

    @Nullable
    String getSyncCompleteSessionFunnelMetaInfoDirectoryPathForDate(String dateString);

    @Nullable
    String getSyncCompleteSessionFunnelInfoDirectoryPathForFunnelID(String funnelID);

    @Nullable
    String getSyncPendingSessionFunnelInfoDirectoryPathForDate(String dateString, String funnelID);

    @Nullable
    String getSyncCompleteSessionFunnelInfoDirectoryPathForDate(String dateString, String funnelID);

    @Nullable
    String getSDKManifestDirectoryPath();

    @Nullable
    public String getLogLevelDirAllSegmentsToAnalyseDirectoryPath();

    @Nullable
    public String getSessionBasedSegmentsEventsSyncPendingDirectoryPath();

    @Nullable
    public String getDailyAggregatedSegmentsEventsSyncPendingDirectoryPath();

    @Nullable
    public String getSessionBasedSegmentsEventsSyncCompleteDirectoryPath();

    @Nullable
    public String getDailyAggregatedSegmentsEventsSyncCompleteDirectoryPath();

    @Nullable
    public String getSyncPendingSessionSegmentsMetaInfoDirectoryPath();

    @Nullable
    public String getSyncPendingSessionSegmentsInfoDirectoryPath();

    @Nullable
    public String getSyncCompleteSessionSegmentsMetaInfoDirectoryPath();

    @Nullable
    public String getSyncCompleteSessionSegmentsInfoDirectoryPath();


}
