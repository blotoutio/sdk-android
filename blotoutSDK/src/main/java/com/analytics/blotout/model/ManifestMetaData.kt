package com.analytics.blotout.model

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
        val isEditable:Boolean,
        @SerializedName("variable_options")
        val variableOptions:List<VariableOption>

//{"variables":[{"variableId":5023,"value":"0","variableDataType":1,"variableName":"SDK_Push_System_Events"},
        // {"variableId":5998,"value":"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDAnUPi9CXJ3Mo4HwS71xUnPmJjlTFHnd71Zjig0bfDyKQTaLa6gAOXzaM12MFBK4p4Sru5GNxRnbSnJpAph1QraDxVDVgPf4F2YjumKJtQW+2hKZs30xit2TQ8/747JaHxNHajiDGY2Nz1Xz4xclvtsGb6C+Pf0BcqedHzDhKa4wIDAQAB","variableDataType":6,"variableName":"PII_Public_Key"},
        // {"variableId":5997,"value":"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC17C+l8MByxiS9VDCsWkSdIAJFZlQxpIDYmHQ7Cc72vGtFCx7GgjmfFOzSRxKPhJNq9WqatCEW+w9gX6CQwqgTTyp+32T4dZ7pS3rOVuotjWS3gLdUjFhsdDAWLHGSV+mVC6hAT0IzLHPY27zfchWTF6uAWhpZiM0JI2x0D9dCowIDAQAB","variableDataType":6,"variableName":"PHI_Public_Key"}]}
)
