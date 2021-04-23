package com.blotout.repository.impl

import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.blotout.DependencyInjectorImpl
import com.blotout.repository.data.FileManager
import com.blotout.util.Constant
import com.blotout.util.sizeFormatter
import java.io.*

@RequiresApi(Build.VERSION_CODES.KITKAT)
class FileManagerImpl(context: Context) : FileManager{


    private var mContext= context

    init {
        createRootDirectory()
    }

    override fun isSDCardAvailable(): Boolean {
        var isRemovableSDCardAvailble  = ContextCompat.getExternalFilesDirs(mContext, null).size >= 2
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED && isRemovableSDCardAvailble
    }

    override fun getAvailableInternalMemory(): String {
        return mContext.getFilesDir().getFreeSpace().sizeFormatter()
    }

    override fun getTotalInternalMemory(): String {
        return mContext.getFilesDir().getTotalSpace().sizeFormatter()
    }


    override fun getAvailableExternalMemory(): String {
        if(isSDCardAvailable()) {
            var externalBytesAvailable = File(
                    mContext.getExternalFilesDirs(null).get(1).getPath()).getUsableSpace()
            return externalBytesAvailable.sizeFormatter()
        }
        return ""
    }


    override fun getTotalExternalMemory(): String {
        if (isSDCardAvailable()) {
            var totalExternalBytes = File(
                    mContext.getExternalFilesDirs(null).get(1).getPath()).getTotalSpace()
            return totalExternalBytes.sizeFormatter()
        }
        return ""
    }


    override fun createRootDirectory() {
        try {
            if (mContext != null) {
                val file: File?
                val filesDirs: Array<File> = mContext.getExternalFilesDirs(null)
                //Gets the file path for the directory created
                file = File(filesDirs.get(0).getAbsolutePath() + File.separator + Constant.BO_SDK_ROOT_DIRECTORY_NAME)
                DependencyInjectorImpl.getInstance().getSecureStorageService().storeString(Constant.BO_SDK_ROOT_DIRECTORY_NAME, file!!.absolutePath)

                if (null != file) {
                    if (!file.exists()) {
                        file.mkdirs()
                        //Log.d(BOFileSystemManager.TAG, BOCommonConstants.DIRECTORY_NEWLY_CREATED)
                    } else {
                        //Log.d(BOFileSystemManager.TAG, BOCommonConstants.DIRECTORY_EXIST)
                    }
                }
            } else {
                //Log.d(BOFileSystemManager.TAG, BOCommonConstants.EMPTY_CONTEXT)
            }
        } catch (e: Exception) {
            //Log.d(BOFileSystemManager.TAG, " " + e)
        }
    }

    override fun writeToFile(fileName: String?, content: String?): Boolean {
        try {
                File(fileName).writeText(content!!)
                Log.d("", "")
        } catch (e: java.lang.Exception) {
            Log.d("", "")
        }
        return false
    }

    override fun writeToFile(fileName: String?, content: ByteArray?): Boolean {

        try {
            //if (BlotoutAnalytics_Internal.getInstance().isDataCollectionEnabled && BlotoutAnalytics_Internal.getInstance().isSDKEnabled) {
                File(fileName).writeBytes(content!!)

        } catch (e: java.lang.Exception) {
            //com.blotout.utilities.Logger.INSTANCE.e(BOFileSystemManager.TAG, e.toString())
        }
        return false
    }

    override fun deleteFilesAndDir(path: String?) {
        try {
            val file = File(path)
            if (file.isDirectory) {
                for (nestedChild in file.listFiles()) {
                    deleteFilesAndDir(nestedChild.toString())
                }
            } else if (file.isFile) {
                file.delete()
            }
        } catch (e: java.lang.Exception) {
            //com.blotout.utilities.Logger.INSTANCE.e(BOFileSystemManager.TAG, e.toString())
        }
    }

    override fun getBOSDKRootDirectory(): String? {
        return DependencyInjectorImpl.getInstance().getSecureStorageService().fetchString(Constant.BO_SDK_ROOT_DIRECTORY_NAME)
    }

    override fun checkFileExist(path: String?): Boolean {
        val file = File(path)
        return file.exists()
    }

    override fun getSDKManifestDirectoryPath(): String? {
        val eventsRootDir = getEventsRootDirectoryPath()
        return createDirectoryIfRequiredAndReturnPath("$eventsRootDir/SDKManifestData")

    }

    override fun getEventsRootDirectoryPath(): String? {
        val sdkRootDirectory: String = getBOSDKRootDirectory()!!
        return createDirectoryIfRequiredAndReturnPath("$sdkRootDirectory/Events")
    }

    override fun createDirectoryIfRequiredAndReturnPath(path: String?): String? {
        val directory = File(path)
        if (null != directory) {
            if (!directory.exists()) {
                directory.mkdirs()
                //Logger.INSTANCE.d(TAG, "Created " + path + "Directory");
            }
            return directory.absolutePath
        }
        return null
    }

    @Throws(java.lang.Exception::class)
    override fun readContentOfFileAtPath(filePath: String?): String? {
        if (checkFileExist(filePath)) return null
        try {
            val fl = File(filePath)
            val fin = FileInputStream(fl)
            var ret: String = convertStreamToString(fin)
            fin.close()
            return ret
        } catch (e: java.lang.Exception) {
        }
        return null
    }

    @Throws(java.lang.Exception::class)
    private fun convertStreamToString(`is`: InputStream): String {
        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            sb.append(line).append("\n")
        }
        reader.close()
        return sb.toString()
    }

}