package com.analytics.blotout.repository.data

interface FileManager {

    fun isSDCardAvailable(): Boolean

    fun getAvailableInternalMemory(): String

    fun getTotalInternalMemory(): String

    fun getAvailableExternalMemory(): String

    fun getTotalExternalMemory(): String

    fun createRootDirectory()

    fun writeToFile(fileName: String?, content: String?): Boolean

    fun writeToFile(fileName: String?, content: ByteArray?): Boolean

    fun deleteFilesAndDir(path: String?)

    fun getBOSDKRootDirectory(): String?

    fun checkFileExist(path: String?): Boolean

    fun getSDKManifestDirectoryPath(): String?

    fun getEventsRootDirectoryPath(): String?

    fun createDirectoryIfRequiredAndReturnPath(path: String?): String?

    fun readContentOfFileAtPath(path: String?) :String?

}