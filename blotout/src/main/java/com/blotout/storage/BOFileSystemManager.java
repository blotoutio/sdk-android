package com.blotout.storage;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import com.blotout.analytics.BOSharedManager;
import com.blotout.analytics.BlotoutAnalytics_Internal;
import com.blotout.constants.BOCommonConstants;
import com.blotout.constants.BODateTimeConstants;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.BODateTimeUtils;
import com.blotout.utilities.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BOFileSystemManager implements IBOFileSystemManager {

    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOFileSystemManager";


    private static BOFileSystemManager instance;
    private Context context;

    private long internalBytesAvailable;
    private long totalInternalBytes;
    private long externalBytesAvailable;
    private long totalExternalBytes;
    public boolean neverDeleteSDKData; // not using for now but if needed then check before delete in all direcotry or file delete methods

    private static final int CHUNK_SIZE = BOCommonConstants.KILOBYTE * 64;
    private static final int READ_TIMEOUT_SECONDS = BODateTimeConstants.SECONDS * 10;
    private static final int CONNECT_TIMEOUT_SECONDS = BODateTimeConstants.SECONDS * 20;
    private final static String FILE_EXTENSION_SEPARATOR = ".";

    public static BOFileSystemManager getInstance() {

        if (instance != null) {
            return instance;
        }
        //throw new IllegalArgumentException("Please use getInstance(context) for atleast once.");
        return instance;
    }

    public static BOFileSystemManager getInstance(Context context) {

        if (instance == null) { //if there is no instance available... create new one
            instance = new BOFileSystemManager(context);
        }
        return instance;
    }

    public BOFileSystemManager(Context context) {
        this.context = context;
    }

    @Override
    public boolean isSDCardAvailable() {

        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                && isRemovableSDCardAvailble();
    }

    @NonNull
    @Override
    public String getAvailableInternalMemory() {
        try {
            internalBytesAvailable = getFreeInternal();
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return BOCommonUtils.sizeFormatter(internalBytesAvailable);
    }

    @NonNull
    @Override
    public String getTotalInternalMemory() {
        try {
            totalInternalBytes = getTotalInternal();
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return BOCommonUtils.sizeFormatter(totalInternalBytes);
    }

    @NonNull
    @Override
    public String getAvailableExternalMemory() {
        try {
            externalBytesAvailable = getFreeExternal();
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return BOCommonUtils.sizeFormatter(externalBytesAvailable);
    }

    @NonNull
    @Override
    public String getTotalExternalMemory() {
        try {
            totalExternalBytes = getTotalExternal();
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return BOCommonUtils.sizeFormatter(totalExternalBytes);
    }


    @Override
    public void createRootDirectory() {

        try {
            if (context != null) {
                File file;
                File[] filesDirs = context.getExternalFilesDirs(null);
                //Gets the file path for the directory created
                file = getFileDirectoryPath(filesDirs);

                if (null != file) {
                    if (!file.exists()) {
                        file.mkdirs();
                        Logger.INSTANCE.d(TAG, BOCommonConstants.DIRECTORY_NEWLY_CREATED);
                    } else {
                        Logger.INSTANCE.d(TAG, BOCommonConstants.DIRECTORY_EXIST);
                    }
                }
            } else {
                Logger.INSTANCE.d(TAG, BOCommonConstants.EMPTY_CONTEXT);
            }
        } catch (Exception e) {
            Logger.INSTANCE.methodLog(TAG + " " + e);
        }
    }

    @Nullable
    @Override
    public String createDirectoryIfRequiredAndReturnPath(String path) {
        if (path == null) {
            return null;
        }
        File directory = new File(path);
        //Checks if directory already exist
        if (null != directory) {
            if (directory.exists()) {
                //Logger.INSTANCE.d(TAG,
                        //"Directory '" + directory.getAbsolutePath() + "' already exists");

            } else {
                directory.mkdirs();
                //Logger.INSTANCE.d(TAG, "Created " + path + "Directory");
            }
            return directory.getAbsolutePath();
        }
        return null;
    }

    @NonNull
    @Override
    public String readContentOfFileAtPath(String filePath) throws Exception {
        if(!BOFileSystemManager.getInstance().checkFileExist(filePath))
            return null;
        try {
            File fl = new File(filePath);
            FileInputStream fin = new FileInputStream(fl);
            String ret = convertStreamToString(fin);

            //data decryption
            if(BlotoutAnalytics_Internal.getInstance().isDataEncryptionEnabled) {
                ret = BOSharedManager.getInstance().getEncryptionManager().decryptString(ret);
            }

            //Make sure we close all streams.
            fin.close();
            return ret;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }


    @NonNull
    private static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }


    /**
     * Gets internal file/directory path
     *
     * @param files: List of files
     * @return: file path
     */
    @Nullable
    private File getFileDirectoryPath(@Nullable File[] files) {
        File file = null;
        if (files != null) {

            //Created directory in internal memory
            if(BlotoutAnalytics_Internal.getInstance().isProductionMode) {
                file = new File(
                        files[0].getAbsolutePath() + File.separator + BOCommonConstants.BO_SDK_ROOT_DIRECTORY_NAME);
                BOSharedPreferenceImpl.getInstance().saveString(BOCommonConstants.GET_ROOT_DIR,
                        file.getAbsolutePath());
            } else {
                file = new File(
                        files[0].getAbsolutePath() + File.separator + BOCommonConstants.BO_SDK_ROOT_DIRECTORY_NAME_STAGE);
                BOSharedPreferenceImpl.getInstance().saveString(BOCommonConstants.GET_ROOT_DIR_STAGE,
                        file.getAbsolutePath());
            }

        }
        return file;
    }

    @Nullable
    @Override
    public String getBOSDKRootDirectory() {
        String directoryPath = "";

        try {
            if (context != null) {
                if(BlotoutAnalytics_Internal.getInstance().isProductionMode) {
                    directoryPath = BOSharedPreferenceImpl.getInstance().getString(
                            BOCommonConstants.GET_ROOT_DIR, "");
                } else {
                    directoryPath = BOSharedPreferenceImpl.getInstance().getString(
                            BOCommonConstants.GET_ROOT_DIR_STAGE, "");
                }
            } else {
                Logger.INSTANCE.d(TAG, BOCommonConstants.EMPTY_CONTEXT);
            }
        } catch (Exception e) {
            Logger.INSTANCE.methodLog(TAG + " " + e);
        }
        return directoryPath;
    }

    @Nullable
    @Override
    public String getExternalDirectoryPath() {
        String directoryPath = "";

        try {
            if (context != null) {
                directoryPath = BOSharedPreferenceImpl.getInstance().getString(
                        BOCommonConstants.GET_EXTERNAL_DIR, "");
            } else {
                Logger.INSTANCE.d(TAG, BOCommonConstants.EMPTY_CONTEXT);
            }
        } catch (Exception e) {
            Logger.INSTANCE.methodLog(TAG + " " + e);
        }
        return directoryPath;
    }


    @Override
    public boolean downloadContent(String url, @NonNull File file) {

        int count;
        byte[] buffer = new byte[CHUNK_SIZE];
        boolean isComplete;

        file.getParentFile().mkdirs();
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            Logger.INSTANCE.i(TAG, BOCommonConstants.DOWNLOAD_START);
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.setReadTimeout(READ_TIMEOUT_SECONDS);
            httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT_SECONDS);

            InputStream inputStream = httpURLConnection.getInputStream();

            BufferedInputStream inputBuffer = new BufferedInputStream(inputStream);

            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("Response code 200 expected.");
            }

            while ((count = inputBuffer.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, count);
            }

            inputBuffer.close();
            inputStream.close();
            isComplete = true;

            Logger.INSTANCE.i(TAG, BOCommonConstants.DOWNLOAD_DONE);

        } catch (IOException e) {
            Logger.INSTANCE.e(TAG, BOCommonConstants.DOWNLOAD_ERROR + " " + e);
            isComplete = false;

        }
        return isComplete;
    }


    @Override
    public boolean writeToFile(String fileName, String content) {
      try {
          if (BlotoutAnalytics_Internal.getInstance().isDataCollectionEnabled && BlotoutAnalytics_Internal.getInstance().isSDKEnabled) {
              File file = new File(fileName);
              if (!file.exists()) {
                  //apply data encryption
                  if(BlotoutAnalytics_Internal.getInstance().isDataEncryptionEnabled) {
                      content = BOSharedManager.getInstance().getEncryptionManager().encrypt(content);
                  }

                  try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true))) {
                      bufferedWriter.write(content);
                      bufferedWriter.flush();
                      return true;
                  } catch (IOException e) {
                      Logger.INSTANCE.e(TAG, e.toString());
                      return false;
                  }
              }
          }
      }catch (Exception e) {
          Logger.INSTANCE.e(TAG,  e.toString());
      }
      return false;
    }

    @Override
    public boolean writeToFile(String filename, byte[] content) {
        try {
            if (BlotoutAnalytics_Internal.getInstance().isDataCollectionEnabled && BlotoutAnalytics_Internal.getInstance().isSDKEnabled) {
                File file = new File(filename);

                if (!file.exists()) {
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(content);
                        return true;
                    } catch (IOException e) {
                        Logger.INSTANCE.e(TAG, e.toString());
                        return false;
                    }
                } else {
                    //overrite file
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(content);
                        return true;
                    } catch (IOException e) {
                        Logger.INSTANCE.e(TAG, e.toString());
                        return false;
                    }
                }
            }
        }catch (Exception e) {
                Logger.INSTANCE.e(TAG,  e.toString());
        }
        return false;
    }

    /* @param filePath
     * @return file name from path, include suffix
     */
    @NonNull
    @Override
    public String getFileName(@NonNull String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }


    /**
     * Moves file from one location to another location
     *
     * @param sourceFilePath
     * @param destFilePath
     */
    @Override
    public boolean moveFile(String sourceFilePath, String destFilePath) {
        if (TextUtils.isEmpty(sourceFilePath) || TextUtils.isEmpty(destFilePath)) {
           return false;
        }
        return moveFile(new File(sourceFilePath), new File(destFilePath));
    }

    /**
     * Moves files from source location to destination location
     *
     * @param srcFile
     * @param destFile
     */
    @Override
    public boolean moveFile(@NonNull File srcFile, @NonNull File destFile) {
    try {
            boolean rename = srcFile.renameTo(destFile);

            if (!rename) {
                boolean isFileCopied = copyFile(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
                if (isFileCopied) deleteFilesAndDir(srcFile.getAbsolutePath());
                return isFileCopied;
            } else  {
                if(srcFile.exists()) {
                    rename = srcFile.delete();
                }
            }

            return rename;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
            return false;
        }
    }

    /**
     * Copies file
     *
     * @param sourceFilePath
     * @param destFilePath
     * @return
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    @Override
    public boolean copyFile(@NonNull String sourceFilePath, String destFilePath) {
        boolean isFileWriteDone = false;
        try {
            String destWithFileName;
            InputStream inputStream;
            String srcFileName = getFileName(sourceFilePath);
            //we need to append file name with the destination path
            destWithFileName = destFilePath + "/" + srcFileName;
            inputStream = new FileInputStream(sourceFilePath);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            isFileWriteDone = writeToFile(destWithFileName, buffer);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return isFileWriteDone;
    }

    @Override
    public boolean isFirstLaunchBOSDKFileSystemCheck() {

        boolean isSDKFirstLaunch = true;

        String sdkRootDir = getBOSDKRootDirectory();
        boolean isRootDirCreated = checkFileExist(sdkRootDir);
        if (isRootDirCreated) {
            String sdkLaunchTest = sdkRootDir + BOCommonConstants.BO_SDK_Launch_Test_DirectoryName;

            boolean isSDKLaunchTestCreated = checkFileExist(sdkLaunchTest);
            if (isSDKLaunchTestCreated) {
                isSDKFirstLaunch = !isSDKFirstLaunch;
            } else {
                //[self getChildDirectory:kBOSDKLaunchTestDirectoryName byCreatingInParent:[self getBOSDKRootDirecoty]];
            }
        } else {
            //NSString *sdkRootDir = [self getBOSDKRootDirecoty];
            //[self getChildDirectory:kBOSDKLaunchTestDirectoryName byCreatingInParent:sdkRootDir];
        }
        return isSDKFirstLaunch;
        //TODO: need to implement
    }

    @Override
    public boolean isAppFirstLaunchFileSystemChecks() {
        return true;
        //TODO: need to implement
    }

    @Override
    public void deleteFilesAndDir(String path) {
        try {
            File file = new File(path);
            if (file.isDirectory()) {
                for (File nestedChild : file.listFiles()) {
                    deleteFilesAndDir(nestedChild.toString());
                }
            } else if (file.isFile()) {
                file.delete();
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    public boolean deleteFile(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
                return true;
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return false;
    }


    @Override
    public boolean checkFileExist(@Nullable String path) {
        if (path == null) {
            return false;
        }
        File file = new File(path);
        return file.exists();
    }


    /**
     * Called in case when the file cannot be found.
     *
     * @param path Movie file path
     * @return True if file path points to a directory that is not available because storage was
     * removed.
     */
    @Override
    public boolean isMediaRemoved(@NonNull String path) {
        boolean isPathRemoved;
        if (isSDCardAvailable()) {
            String externalStoragePath =
                    Environment.getExternalStorageDirectory().getAbsolutePath();
            isPathRemoved = !path.startsWith(externalStoragePath);
        } else {
            isPathRemoved = true;
        }
        return isPathRemoved;
    }


    /**
     * Checks if SD card there or not
     *
     * @return: true if SD card available, else false.
     */
    private boolean isRemovableSDCardAvailble() {
        return ContextCompat.getExternalFilesDirs(context, null).length >= 2;

    }
    @NonNull
    @Override
    public List<String> getAllDirectories(String path) {
        File file = new File(path);
        List<String> out = new ArrayList<>();
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files == null) {
                return null;
            } else {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        out.add(path + "/" + files[i].getName());
                    }
                }
            }
        }
        return out;
    }

    @NonNull
    @Override
    public List<String> getAllFileAndDirectories(String path) {
        File file = new File(path);
        List<String> out = new ArrayList<>();
        getDirectoryFilesImpl(file, out);
        return out;
    }

    @NonNull
    @Override
    public List<String> getAllFilesWithExtension(String path, @NonNull String fileExtension) {
        List<String> out = new ArrayList<>();
        try {
           File file = new File(path);
           getDirectoryFilesWithExtensionImpl(file, fileExtension, out);
           return out;
       }catch(Exception e) {
               Logger.INSTANCE.d(TAG,e.toString());
           }
       return out;
    }

    @NonNull
    @Override
    public List<File> getAllFilesWithoutExtension(String path) {
        List<File> out = new ArrayList<>();

        try {
            File file = new File(path);
            getDirectoryFilesWithoutExtensionImpl(file, out);
        }catch(Exception e) {
                Logger.INSTANCE.d(TAG,e.toString());
            }
        return out;
    }

    /**
     * Gets all files under the directory
     */

    private void getDirectoryFilesImpl(@NonNull File directory, @NonNull List<String> out) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files == null) {
                return;
            } else {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        getDirectoryFilesImpl(files[i], out);
                    } else {
                        out.add(directory.getAbsolutePath()+"/" + files[i].getName());
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public String getFilePathExtention(@NonNull String fileName) {
        String extension = null;
        try {
            if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
                extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            }
        }catch(Exception e) {
            Logger.INSTANCE.d(TAG,e.toString());
        }
        return extension;
    }

    @Nullable
    @Override
    public String getFilePathAfterDeletingPathExtention(@NonNull String fileName) {
        String extension = null;
        try {
            if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
                extension = fileName.substring(0, fileName.lastIndexOf("."));
            }
        }catch(Exception e) {
            Logger.INSTANCE.d(TAG,e.toString());
        }
        return extension;
    }

    @Nullable
    @Override
    public String getLastPathComponent(@NonNull String fileName) {
        String lastPathComponent = null;
        try {
            if(fileName.lastIndexOf("/") != -1 && fileName.lastIndexOf("/") != 0) {
                lastPathComponent = fileName.substring(fileName.lastIndexOf("/") + 1);
            }
        }catch(Exception e) {
            Logger.INSTANCE.d(TAG,e.toString());
        }
        return lastPathComponent;

    }

    @NonNull
    @Override
    public String stringByDeletingLastPathComponent(@NonNull String path) {
        String dest = path.substring(0, path.lastIndexOf('/'));
        return dest;
    }



    /**
     * Gets all files under the directory
     */

    private void getDirectoryFilesWithExtensionImpl(@NonNull File directory,@NonNull String fileExtension, @NonNull List<String> out) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files == null) {
                return;
            } else {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isFile()) {
                        String fileName = files[i].getName();
                        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
                           String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
                           if(extension.equals(fileExtension)) {
                               out.add(directory.getAbsolutePath()+"/"+fileName);
                           }
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets all files under the directory
     */

    private void getDirectoryFilesWithoutExtensionImpl(@NonNull File directory, @NonNull List<File> out) {
        //TODO: need to revisit
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files == null) {
                return;
            } else {
                String fileName;
                int lastPeriodPos;
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isFile()) {
                        fileName = files[i].getName();
                        lastPeriodPos = fileName.lastIndexOf('.');
                        if (lastPeriodPos > 0) {
                            fileName = fileName.substring(0, lastPeriodPos);
                        }
                        Logger.INSTANCE.v(TAG,"File name is " + fileName);
                        out.add(new File(fileName));
                    }
                }
            }
        }
    }

    private long getFreeInternal() {
        internalBytesAvailable = context.getFilesDir().getFreeSpace();
        return internalBytesAvailable;
    }

    private long getTotalInternal() {
        totalInternalBytes = context.getFilesDir().getTotalSpace();
        return totalInternalBytes;

    }

    private long getFreeExternal() {
        if (isSDCardAvailable()) {
            externalBytesAvailable = new File(
                    context.getExternalFilesDirs(null)[1].getPath()).getUsableSpace();
        } else {
            Logger.INSTANCE.d(TAG, BOCommonConstants.NO_EXTERNAL_MEMORY);
        }
        return externalBytesAvailable;
    }

    private long getTotalExternal() {
        if (isSDCardAvailable()) {
            totalExternalBytes = new File(
                    context.getExternalFilesDirs(null)[1].getPath()).getTotalSpace();
        } else {
            Logger.INSTANCE.d(TAG, BOCommonConstants.NO_EXTERNAL_MEMORY);
        }
        return totalExternalBytes;
    }


    //pragma mark Directory specific functions
    //pragma mark Network Downloads Directory
    @Nullable
    @Override
    public String getBOFNetworkDownloadsDirectoryPath() {
        String BOFNetworkDirectory = this.createDirectoryIfRequiredAndReturnPath(this.getBOFNetworkDownloadsDirectoryPossibleExistancePath());
        return BOFNetworkDirectory;
    }

    @Nullable
    @Override
    public String getBOFNetworkDownloadsDirectoryPossibleExistancePath() {
        String BOFSDKRootDir = getBOSDKRootDirectory();
        String BOFNetworkDirectory = BOFSDKRootDir + BOCommonConstants.BO_Network_Promise_Download_Directory_Name;
        return BOFNetworkDirectory;
    }

    //pragma mark Volatile Directory
    @Nullable
    @Override
    public String getBOSDKVolatileRootDirectoryPossibleExistancePath() {
        String BOFSDKRootDir = getBOSDKRootDirectory();
        String BOFNetworkDirectory = BOFSDKRootDir + BOCommonConstants.BO_SDK_Volatile_ROOT_DIRECTORY_NAME;
        return BOFNetworkDirectory;
    }

    @Nullable
    @Override
    public String getBOSDKVolatileRootDirectoryPath() {
        String BOFNetworkDirectory = this.createDirectoryIfRequiredAndReturnPath(this.getBOSDKVolatileRootDirectoryPossibleExistancePath());
        return BOFNetworkDirectory;
    }

    @Nullable
    @Override
    public String getBOSDKNonVolatileRootDirectoryPossibleExistancePath() {
        String BOFSDKRootDir = getBOSDKRootDirectory();
        String BOFNetworkDirectory = BOFSDKRootDir + BOCommonConstants.BO_SDK_NonVolatile_ROOT_DIRECTORY_NAME;
        return BOFNetworkDirectory;
    }

    @Nullable
    @Override
    public String getBOSDKNonVolatileRootDirectoryPath() {
        String BOFNetworkDirectory = this.createDirectoryIfRequiredAndReturnPath(this.getBOSDKNonVolatileRootDirectoryPossibleExistancePath());
        return BOFNetworkDirectory;
    }

    //BOSDK related dir structure
//===========================================Level 1================================================
//Level 1 Dir
//Funnel Root
    @Nullable
    @Override
    public String getFunnelRootDirectoryPath() {
        String sdkRootDirectory = this.getBOSDKRootDirectory();
        String funnelsRootDir = this.createDirectoryIfRequiredAndReturnPath(sdkRootDirectory +
                "/Funnels");
        return funnelsRootDir;
    }

    @Nullable
    @Override
    public String getEventsRootDirectoryPath() {
        String sdkRootDirectory = this.getBOSDKRootDirectory();
        String funnelsRootDir = this.createDirectoryIfRequiredAndReturnPath(sdkRootDirectory + "/Events");
        return funnelsRootDir;
    }

    @Nullable
    @Override
    public String getSegmentsRootDirectoryPath() {
        String sdkRootDirectory = this.getBOSDKRootDirectory();
        String segmentsRootDir = this.createDirectoryIfRequiredAndReturnPath(sdkRootDirectory + "/Segments");
        return segmentsRootDir;
    }

    @Nullable
    @Override
    public String getCampaignsRootDirectoryPath() {
        String sdkRootDirectory = this.getBOSDKRootDirectory();
        String funnelsRootDir = this.createDirectoryIfRequiredAndReturnPath(sdkRootDirectory + "/Campaigns");
        return funnelsRootDir;
    }

    //===========================================Level 2================================================
//Level 2 Dir
//Funnel Network Downloads
    @Nullable
    @Override
    public String getNetworkDownloadsFunnelDirectoryPath() {
        String funnelRootDir = this.getFunnelRootDirectoryPath();
        String networkDownloads = this.createDirectoryIfRequiredAndReturnPath(funnelRootDir + "/NetworkDownloads");
        return networkDownloads;
    }

    @Nullable
    @Override
    public String getNetworkDownloadsSegmentsDirectoryPath() {
        String funnelRootDir = this.getSegmentsRootDirectoryPath();
        String networkDownloads = this.createDirectoryIfRequiredAndReturnPath(funnelRootDir + "/NetworkDownloads");
        return networkDownloads;
    }

    @Nullable
    @Override
    public String getArchivedFunnelsDirectoryPath() {
        String funnelRootDir = this.getFunnelRootDirectoryPath();
        String archivedFunnels = this.createDirectoryIfRequiredAndReturnPath(funnelRootDir + "/ArchivedFunnels");
        return archivedFunnels;
    }

    //Level 2 Dir
//Events LifeTime Data
    @Nullable
    @Override
    public String getLifeTimeDataEventsDirectoryPath() {
        String eventsRootDir = this.getEventsRootDirectoryPath();
        String lifeTimeEvents = this.createDirectoryIfRequiredAndReturnPath(eventsRootDir +
                "/LifeTimeDataEvents");
        return lifeTimeEvents;
    }

    //Level 2 Dir
//Events Session Data
    @Nullable
    @Override
    public String getSessionDataEventsDirectoryPath() {
        String eventsRootDir = this.getEventsRootDirectoryPath();
        String sessionDataEvents = this.createDirectoryIfRequiredAndReturnPath(eventsRootDir + "/SessionDataEvents");
        return sessionDataEvents;
    }

    @Nullable
    @Override
    public String getSDKManifestDirectoryPath() {
        String eventsRootDir = this.getEventsRootDirectoryPath();
        String sdkManifestData = this.createDirectoryIfRequiredAndReturnPath(eventsRootDir + "/SDKManifestData");
        return sdkManifestData;
    }

    //===========================================Level 3================================================
//Level 3 Dir
//Funnel Active Funnels
    @Nullable
    @Override
    public String getActiveFunnelsDirectoryPath() {
        String networkDownloads = this.getNetworkDownloadsFunnelDirectoryPath();
        String activeFunnels = this.createDirectoryIfRequiredAndReturnPath(networkDownloads + "/ActiveFunnels");
        return activeFunnels;
    }

    @Nullable
    @Override
    public String getExpiredFunnelsDirectoryPath() {
        String networkDownloads = this.getNetworkDownloadsFunnelDirectoryPath();
        String expiredFunnels = this.createDirectoryIfRequiredAndReturnPath(networkDownloads + "/ExpiredFunnels");
        return expiredFunnels;
    }

    @Nullable
    @Override
    public String getInActiveFunnelsDirectoryPath() {
        String networkDownloads = this.getNetworkDownloadsFunnelDirectoryPath();
        String inActiveFunnels = this.createDirectoryIfRequiredAndReturnPath(networkDownloads + "/InActiveFunnels");
        return inActiveFunnels;
    }

    @Nullable
    @Override
    public String getActiveSegmentsDirectoryPath() {
        String networkDownloads = this.getNetworkDownloadsSegmentsDirectoryPath();
        String activeSegments = this.createDirectoryIfRequiredAndReturnPath(networkDownloads + "/ActiveSegments");
        return activeSegments;
    }

    @Nullable
    @Override
    public String getExpiredSegmentsDirectoryPath() {
        String networkDownloads = this.getNetworkDownloadsSegmentsDirectoryPath();
        String expiredSegments = this.createDirectoryIfRequiredAndReturnPath(networkDownloads + "/ExpiredSegments");
        return expiredSegments;
    }

    @Nullable
    @Override
    public String getInActiveSegmentsDirectoryPath() {
        String networkDownloads = this.getNetworkDownloadsSegmentsDirectoryPath();
        String inActiveSegments = this.createDirectoryIfRequiredAndReturnPath(networkDownloads + "/InActiveSegments");
        return inActiveSegments;
    }

    //Level 3 Dir
//SyncedFilesEvents LifeTime Data
    @Nullable
    @Override
    public String getSyncedFilesLifeTimeEventsDirectoryPath() {
        String lifeTimeEvents = this.getLifeTimeDataEventsDirectoryPath();
        String syncedFilesEventsDir = this.createDirectoryIfRequiredAndReturnPath(lifeTimeEvents + "/SyncedFilesEvents");
        return syncedFilesEventsDir;
    }

    //Level 3 Dir
//NotSyncedFilesEvents LifeTime Data
    @Nullable
    @Override
    public String getNotSyncedFilesLifeTimeEventsDirectoryPath() {
        String lifeTimeEvents = this.getLifeTimeDataEventsDirectoryPath();
        String notSyncedFilesEventsDir = this.createDirectoryIfRequiredAndReturnPath(lifeTimeEvents + "/NotSyncedFilesEvents");
        return notSyncedFilesEventsDir;
    }

    //Level 3 Dir
//SyncedFilesEvents Session Data
    @Nullable
    @Override
    public String getSyncedFilesSessionTimeEventsDirectoryPath() {
        String sessionDataEvents = this.getSessionDataEventsDirectoryPath();
        String syncedFilesEventsDir = this.createDirectoryIfRequiredAndReturnPath(sessionDataEvents + "/SyncedFilesEvents");
        return syncedFilesEventsDir;
    }

    //Level 3 Dir
//NotSyncedFilesEvents Session Data
    @Nullable
    @Override
    public String getNotSyncedFilesSessionTimeEventsDirectoryPath() {
        String sessionDataEvents = this.getSessionDataEventsDirectoryPath();
        String notSyncedFilesEventsDir = this.createDirectoryIfRequiredAndReturnPath(sessionDataEvents + "/NotSyncedFilesEvents");
        return notSyncedFilesEventsDir;
    }

    //===========================================Level 4================================================
//Level 4 Dir
//Funnel All Funnels to Analyse
    @Nullable
    @Override
    public String getAllFunnelsToAnalyseDirectoryPath() {
        String activeFunnels = this.getActiveFunnelsDirectoryPath();
        String allFunnelsToAnalyse = this.createDirectoryIfRequiredAndReturnPath(activeFunnels + "/AllFunnelsToAnalyse");
        return allFunnelsToAnalyse;
    }

    @Nullable
    @Override
    public String getServerSyncCompleteFunnelEventsDirectoryPath() {
        String activeFunnels = this.getActiveFunnelsDirectoryPath();
        String serverSyncComplete = this.createDirectoryIfRequiredAndReturnPath(activeFunnels + "/ServerSyncCompleteFunnelEvents");
        return serverSyncComplete;
    }

    @Nullable
    @Override
    public String getServerSyncPendingFunnelEventsDirectoryPath() {
        String activeFunnels = this.getActiveFunnelsDirectoryPath();
        String serverSyncPending = this.createDirectoryIfRequiredAndReturnPath(activeFunnels + "/ServerSyncPendingFunnelEvents");
        return serverSyncPending;
    }

    @Nullable
    @Override
    public String getAllSegmentsToAnalyseDirectoryPath() {
        String activeSegments = this.getActiveSegmentsDirectoryPath();
        String allSegmentsToAnalyse = this.createDirectoryIfRequiredAndReturnPath(activeSegments + "/AllSegmentsToAnalyse");
        return allSegmentsToAnalyse;
    }

    @Nullable
    @Override
    public String getServerSyncCompleteSegmentsEventsDirectoryPath() {
        String activeSegments = this.getActiveSegmentsDirectoryPath();
        String serverSyncComplete = this.createDirectoryIfRequiredAndReturnPath(activeSegments + "/ServerSyncCompleteSegmentsEvents");
        return serverSyncComplete;
    }

    @Nullable
    @Override
    public String getServerSyncPendingSegmentsEventsDirectoryPath() {
        String activeSegments = this.getActiveSegmentsDirectoryPath();
        String serverSyncPending = this.createDirectoryIfRequiredAndReturnPath(activeSegments + "/ServerSyncPendingSegmentsEvents");
        return serverSyncPending;
    }
    //===========================================Level 5================================================
//Level 5 Dir
//Funnel Log Level Files
    @Nullable
    @Override
    public String getLogLevelDirAllFunnelsToAnalyseDirectoryPath() {
        String allFunnelsToAnalyse = this.getAllFunnelsToAnalyseDirectoryPath();
        String logLevelFiles = this.createDirectoryIfRequiredAndReturnPath(allFunnelsToAnalyse + "/LogLevelFiles");
        return logLevelFiles;
    }

    @Nullable
    @Override
    public String getSessionBasedFunnelEventsSyncPendingDirectoryPath() {
        String syncPending = this.getServerSyncPendingFunnelEventsDirectoryPath();
        String sessionSyncPending = this.createDirectoryIfRequiredAndReturnPath(syncPending + "/SessionBasedFunnelEvents");
        return sessionSyncPending;
    }

    @Nullable
    @Override
    public String getDailyAggregatedFunnelEventsSyncPendingDirectoryPath() {
        String syncPending = this.getServerSyncPendingFunnelEventsDirectoryPath();
        String dailySyncPending = this.createDirectoryIfRequiredAndReturnPath(syncPending + "/DailyAggregatedFunnelEvents");
        return dailySyncPending;
    }

    @Nullable
    @Override
    public String getSessionBasedFunnelEventsSyncCompleteDirectoryPath() {
        String syncComplete = this.getServerSyncCompleteFunnelEventsDirectoryPath();
        String sessionSyncComplete = this.createDirectoryIfRequiredAndReturnPath(syncComplete + "/SessionBasedFunnelEvents");
        return sessionSyncComplete;
    }

    @Nullable
    @Override
    public String getDailyAggregatedFunnelEventsSyncCompleteDirectoryPath() {
        String syncComplete = this.getServerSyncCompleteFunnelEventsDirectoryPath();
        String dailySyncComplete = this.createDirectoryIfRequiredAndReturnPath(syncComplete + "/DailyAggregatedFunnelEvents");
        return dailySyncComplete;
    }

    //Segments
    //Funnel Log Level Files
    @Nullable
    @Override
    public String getLogLevelDirAllSegmentsToAnalyseDirectoryPath() {
        String allSegmentsToAnalyse = this.getAllSegmentsToAnalyseDirectoryPath();
        String logLevelFiles = this.createDirectoryIfRequiredAndReturnPath(allSegmentsToAnalyse + "/LogLevelFiles");
        return logLevelFiles;
    }

    @Nullable
    @Override
    public String getSessionBasedSegmentsEventsSyncPendingDirectoryPath() {
        String syncPending = this.getServerSyncPendingSegmentsEventsDirectoryPath();
        String sessionSyncPending = this.createDirectoryIfRequiredAndReturnPath(syncPending + "/SessionBasedSegmentsEvents");
        return sessionSyncPending;
    }

    @Nullable
    @Override
    public String getDailyAggregatedSegmentsEventsSyncPendingDirectoryPath() {
        String syncPending = this.getServerSyncPendingSegmentsEventsDirectoryPath();
        String dailySyncPending = this.createDirectoryIfRequiredAndReturnPath(syncPending + "/DailyAggregatedSegmentsEvents");
        return dailySyncPending;
    }

    @Nullable
    @Override
    public String getSessionBasedSegmentsEventsSyncCompleteDirectoryPath() {
        String syncComplete = this.getServerSyncCompleteSegmentsEventsDirectoryPath();
        String sessionSyncComplete = this.createDirectoryIfRequiredAndReturnPath(syncComplete + "/SessionBasedSegmentsEvents");
        return sessionSyncComplete;
    }

    @Nullable
    @Override
    public String getDailyAggregatedSegmentsEventsSyncCompleteDirectoryPath() {
        String syncComplete = this.getServerSyncCompleteSegmentsEventsDirectoryPath();
        String dailySyncComplete = this.createDirectoryIfRequiredAndReturnPath(syncComplete + "/DailyAggregatedSegmentsEvents");
        return dailySyncComplete;
    }

    //===========================================Level 6================================================
    @Nullable
    @Override
    public String getSyncPendingSessionFunnelMetaInfoDirectoryPath() {
        String sessionSyncPending = this.getSessionBasedFunnelEventsSyncPendingDirectoryPath();
        String sessionFunnelsMetaInfo = this.createDirectoryIfRequiredAndReturnPath(sessionSyncPending + "/SessionFunnelsMetaInfo");
        return sessionFunnelsMetaInfo;
    }

    @Nullable
    @Override
    public String getSyncPendingSessionFunnelInfoDirectoryPath() {
        String sessionSyncPending = this.getSessionBasedFunnelEventsSyncPendingDirectoryPath();
        String sessionFunnelsInfo = this.createDirectoryIfRequiredAndReturnPath(sessionSyncPending + "/SessionFunnelsInfo");
        return sessionFunnelsInfo;
    }

    @Nullable
    @Override
    public String getSyncCompleteSessionFunnelMetaInfoDirectoryPath() {
        String sessionSyncComplete = this.getSessionBasedFunnelEventsSyncCompleteDirectoryPath();
        String sessionFunnelsMetaInfo = this.createDirectoryIfRequiredAndReturnPath(sessionSyncComplete + "/SessionFunnelsMetaInfo");
        return sessionFunnelsMetaInfo;
    }

    @Nullable
    @Override
    public String getSyncCompleteSessionFunnelInfoDirectoryPath() {
        String sessionSyncComplete = this.getSessionBasedFunnelEventsSyncCompleteDirectoryPath();
        String sessionFunnelsInfo = this.createDirectoryIfRequiredAndReturnPath(sessionSyncComplete + "/SessionFunnelsInfo");
        return sessionFunnelsInfo;
    }

    @Nullable
    @Override
    public String getSyncPendingSessionSegmentsMetaInfoDirectoryPath() {
        String sessionSyncPending = this.getSessionBasedSegmentsEventsSyncPendingDirectoryPath();
        String sessionSegmentsMetaInfo = this.createDirectoryIfRequiredAndReturnPath(sessionSyncPending + "/SessionSegmentsMetaInfo");
        return sessionSegmentsMetaInfo;
    }

    @Nullable
    @Override
    public String getSyncPendingSessionSegmentsInfoDirectoryPath() {
        String sessionSyncPending = this.getSessionBasedSegmentsEventsSyncPendingDirectoryPath();
        String sessionSegmentsInfo = this.createDirectoryIfRequiredAndReturnPath(sessionSyncPending + "/SessionSegmentsInfo");
        return sessionSegmentsInfo;
    }

    @Nullable
    @Override
    public String getSyncCompleteSessionSegmentsMetaInfoDirectoryPath() {
        String sessionSyncComplete = this.getSessionBasedSegmentsEventsSyncCompleteDirectoryPath();
        String sessionSegmentsMetaInfo = this.createDirectoryIfRequiredAndReturnPath(sessionSyncComplete + "/SessionSegmentsMetaInfo");
        return sessionSegmentsMetaInfo;
    }

    @Nullable
    @Override
    public String getSyncCompleteSessionSegmentsInfoDirectoryPath() {
        String sessionSyncComplete = this.getSessionBasedSegmentsEventsSyncCompleteDirectoryPath();
        String sessionSegmentsInfo = this.createDirectoryIfRequiredAndReturnPath(sessionSyncComplete + "/SessionSegmentsInfo");
        return sessionSegmentsInfo;
    }

    //===========================================Level 7================================================
    @Nullable
    @Override
    public String getSyncPendingSessionFunnelMetaInfoDirectoryPathForDate(String dateString) {
        String sessionFunnelsMetaInfo = this.getSyncPendingSessionFunnelMetaInfoDirectoryPath();
        String sessionFunnelsMetaDate = this.createDirectoryIfRequiredAndReturnPath(sessionFunnelsMetaInfo + "/"+dateString);
        return sessionFunnelsMetaDate;
    }

    @Nullable
    @Override
    public String getSyncPendingSessionFunnelInfoDirectoryPathForFunnelID(String funnelID) {
        String sessionFunnelsInfo = this.getSyncPendingSessionFunnelInfoDirectoryPath();
        String sessionFunnelIDdir = this.createDirectoryIfRequiredAndReturnPath(sessionFunnelsInfo +"/" +funnelID);
        return sessionFunnelIDdir;
    }

    @Nullable
    @Override
    public String getSyncCompleteSessionFunnelMetaInfoDirectoryPathForDate(String dateString) {
        String sessionFunnelsMetaInfo = this.getSyncCompleteSessionFunnelMetaInfoDirectoryPath();
        String sessionFunnelsMetaDate = this.createDirectoryIfRequiredAndReturnPath(sessionFunnelsMetaInfo+"/" + dateString);
        return sessionFunnelsMetaDate;
    }

    @Nullable
    @Override
    public String getSyncCompleteSessionFunnelInfoDirectoryPathForFunnelID(String funnelID) {
        String sessionFunnelsInfo = this.getSyncCompleteSessionFunnelInfoDirectoryPath();
        String sessionFunnelIDdir = this.createDirectoryIfRequiredAndReturnPath(sessionFunnelsInfo + "/"+funnelID);
        return sessionFunnelIDdir;
    }

    //===========================================Level 8================================================
    @Nullable
    @Override
    public String getSyncPendingSessionFunnelInfoDirectoryPathForDate(String dateString, String funnelID) {
        String sessionFunnelIDDir = this.getSyncPendingSessionFunnelInfoDirectoryPathForFunnelID(funnelID);
        String sessionFunnelIDDatedir = this.createDirectoryIfRequiredAndReturnPath(sessionFunnelIDDir+"/" + dateString);
        return sessionFunnelIDDatedir;
    }

    @Nullable
    @Override
    public String getSyncCompleteSessionFunnelInfoDirectoryPathForDate(String dateString, String funnelID) {
        String sessionFunnelIDDir = this.getSyncCompleteSessionFunnelInfoDirectoryPathForFunnelID(funnelID);
        String sessionFunnelIDDatedir = this.createDirectoryIfRequiredAndReturnPath(sessionFunnelIDDir+"/" + dateString);
        return sessionFunnelIDDatedir;
    }

//Need to implement
    public Date getCreationDateOfItemAtPath(String itemPath) {
        try {
            File file = new File(itemPath);
            Long lastmodified = file.lastModified();
            Date creationDate =  new Date(lastmodified);
            return creationDate;
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }

        return null;
       /* Supports API 26 and above
       File file = new File(itemPath);
        Path filePath = file.toPath();

        BasicFileAttributes attributes = null;
        try
        {
            attributes =
                    Files.readAttributes(filePath, BasicFileAttributes.class);
        }
        catch (IOException exception)
        {
            System.out.println("Exception handled when trying to get file " +
                    "attributes: " + exception.getMessage());
        }
        long milliseconds = attributes.creationTime().to(TimeUnit.MILLISECONDS);
        if((milliseconds > Long.MIN_VALUE) && (milliseconds < Long.MAX_VALUE))
        {
            Date creationDate =
                    new Date(attributes.creationTime().to(TimeUnit.MILLISECONDS));

            System.out.println("File " + filePath.toString() + " created " +
                    creationDate.getDate() + "/" +
                    (creationDate.getMonth() + 1) + "/" +
                    (creationDate.getYear() + 1900));
         } */
    }

    public Date getModificationDateOfItemAtPath(String itemPath) {
        try {
            File file = new File(itemPath);
            Long lastmodified = file.lastModified();
            Date creationDate = new Date(lastmodified);
            return creationDate;
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }

        return null;
    }

    /* Delete Files */
    public boolean deleteFilesRecursively(boolean isRecursively, int olderThanDays, String underRootDirPath) {
        try {
        boolean isAllFiledDeleted = true;

        underRootDirPath = (underRootDirPath != null &&  !underRootDirPath.equals("")) ? underRootDirPath : getBOSDKRootDirectory();
        olderThanDays = olderThanDays <=0 ? olderThanDays : 180;
        List<String> allContent = getAllFileAndDirectories(underRootDirPath);

        for (String oneContent : allContent) {
            boolean isDir = false;
            File file = new File(oneContent);
            boolean isDirExist = file.exists();
            isDir = file.isDirectory();
            if (isDirExist && isDir && isRecursively) {
                isAllFiledDeleted = deleteFilesRecursively(isRecursively, olderThanDays, oneContent);
            }else if (isDirExist && !isDir){
                //NSDate *fileCreationDate = [self getCreationDateOfItemAtPath:oneContent];
                Date fileModificationDate = getModificationDateOfItemAtPath(oneContent);

                long expiryInterval = olderThanDays * 24 * 60 * 60;
                Date expiryDate = BODateTimeUtils.dateByAddingTimeInterval(fileModificationDate,expiryInterval);
                Date todaysDate = BODateTimeUtils.getCurrentDate();

                //Greater is checked with the logic as 7th April 2020 is greater than 6th April 2020
                // so 7th is today and 6th is expiry, working with seconds accuracy
                if (BODateTimeUtils.isDateGreaterThan(todaysDate,expiryDate)){
                    isAllFiledDeleted = isAllFiledDeleted && BOFileSystemManager.getInstance().deleteFile(oneContent);
                }
            }
        }
        return isAllFiledDeleted;
    }catch (Exception e) {
        Logger.INSTANCE.e(TAG,e.toString());
    }

     return false;
    }

    public boolean deleteFilesRecursively(boolean isRecursively, Date olderThan, String underRootDirPath) {
    try {
        boolean isAllFiledDeleted = true;

        underRootDirPath = (underRootDirPath != null &&  !underRootDirPath.equals("")) ? underRootDirPath : getBOSDKRootDirectory();
        if (olderThan != null) {
            return false;
        }

        List<String> allContent = getAllFileAndDirectories(underRootDirPath);

        for (String oneContent : allContent) {
            boolean isDir = false;
            File file = new File(oneContent);
            boolean isDirExist = file.exists();
            isDir = file.isDirectory();
            if (isDirExist && isDir && isRecursively) {
                isAllFiledDeleted = deleteFilesRecursively(isRecursively, olderThan, oneContent);
            }else if (isDirExist && !isDir){
                //NSDate *fileCreationDate = [self getCreationDateOfItemAtPath:oneContent];
                Date fileModificationDate = getModificationDateOfItemAtPath(oneContent);

                Date expiryInterval = olderThan;
                Date expiryDate = BODateTimeUtils.dateByAddingTimeInterval(fileModificationDate,expiryInterval.getTime());
                Date todaysDate = BODateTimeUtils.getCurrentDate();

                //Greater is checked with the logic as 7th April 2020 is greater than 6th April 2020
                // so 7th is today and 6th is expiry, working with seconds accuracy
                if (BODateTimeUtils.isDateGreaterThan(todaysDate,expiryDate)){
                    isAllFiledDeleted = isAllFiledDeleted && BOFileSystemManager.getInstance().deleteFile(oneContent);
                }
            }
        }
        return isAllFiledDeleted;
    }catch (Exception e) {
        Logger.INSTANCE.e(TAG,e.toString());
    }

     return false;
    }

    public boolean deleteFilesAndDirectoryRecursively(boolean isRecursively, int olderThanDays, String underRootDirPath) {
     try {
         boolean isAllFiledDeleted = true;

         underRootDirPath = (underRootDirPath != null && !underRootDirPath.equals("")) ? underRootDirPath : getBOSDKRootDirectory();
         List<String> allContent = getAllFileAndDirectories(underRootDirPath);

         for (String oneContent : allContent) {
             boolean isDir = false;
             File file = new File(oneContent);
             //stop removing sdkManifest file
             if(file.getName().equals("sdkManifest.txt"))
                 continue;

             boolean isDirExist = file.exists();
             isDir = file.isDirectory();
             if (isDirExist && isDir && isRecursively) {
                 isAllFiledDeleted = deleteFilesRecursively(isRecursively, olderThanDays, oneContent);
             } else if (isDirExist) {
                 //NSDate *fileCreationDate = [self getCreationDateOfItemAtPath:oneContent];
                 Date fileModificationDate = getModificationDateOfItemAtPath(oneContent);

                 long expiryInterval = olderThanDays * 24 * 60 * 60;
                 Date expiryDate = BODateTimeUtils.dateByAddingTimeInterval(fileModificationDate, expiryInterval);
                 Date todaysDate = BODateTimeUtils.getCurrentDate();

                 //Greater is checked with the logic as 7th April 2020 is greater than 6th April 2020
                 // so 7th is today and 6th is expiry, working with seconds accuracy
                 if (BODateTimeUtils.isDateGreaterThan(todaysDate, expiryDate)) {
                     isAllFiledDeleted = isAllFiledDeleted && BOFileSystemManager.getInstance().deleteFile(oneContent);
                 }
             }
         }
         return isAllFiledDeleted;
     }catch (Exception e) {
         Logger.INSTANCE.e(TAG,e.toString());
     }

     return false;
    }

    public boolean deleteFilesAndDirectoryRecursively(boolean isRecursively, Date olderThan, String underRootDirPath) {

        try{
        boolean isAllFiledDeleted = true;

        underRootDirPath = (underRootDirPath != null &&  !underRootDirPath.equals("")) ? underRootDirPath : getBOSDKRootDirectory();
        if (olderThan != null) {
            return false;
        }

        List<String> allContent = getAllFileAndDirectories(underRootDirPath);

        for (String oneContent : allContent) {
            boolean isDir = false;
            File file = new File(oneContent);
            boolean isDirExist = file.exists();
            isDir = file.isDirectory();
            if (isDirExist && isDir && isRecursively) {
                isAllFiledDeleted = deleteFilesRecursively(isRecursively, olderThan, oneContent);
            }else if (isDirExist ){
                //NSDate *fileCreationDate = [self getCreationDateOfItemAtPath:oneContent];
                Date fileModificationDate = getModificationDateOfItemAtPath(oneContent);

                Date expiryInterval = olderThan;
                Date expiryDate = BODateTimeUtils.dateByAddingTimeInterval(fileModificationDate,expiryInterval.getTime());
                Date todaysDate = BODateTimeUtils.getCurrentDate();

                //Greater is checked with the logic as 7th April 2020 is greater than 6th April 2020
                // so 7th is today and 6th is expiry, working with seconds accuracy
                if (BODateTimeUtils.isDateGreaterThan(todaysDate,expiryDate)){
                    isAllFiledDeleted = isAllFiledDeleted && BOFileSystemManager.getInstance().deleteFile(oneContent);
                }
            }
        }
        return isAllFiledDeleted;
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }

        return false;
    }
}
