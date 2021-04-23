package com.blotout.model

import com.google.gson.annotations.SerializedName

data class ManifestConfigurationResponse(
        @SerializedName("variables")
        var variables: List<ManifestMetaData?> = mutableListOf()
)