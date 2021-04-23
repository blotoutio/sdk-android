package com.blotout.model

import com.google.gson.annotations.SerializedName

data class ManifestMetaData(
        @SerializedName("variableId")
        var variableId: Long = 0,
        @SerializedName("value")
        val value: String?,
        @SerializedName("variableDataType")
        val variableDataType: Int,
        @SerializedName("variableName")
        val variableName: String?,
        @SerializedName("isEditable")
        val isEditable:Boolean
)
