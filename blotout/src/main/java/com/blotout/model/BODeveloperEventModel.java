package com.blotout.model;

import androidx.annotation.Nullable;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Blotout on 07,November,2019
 */
public class BODeveloperEventModel {

    public   String eventID;
    public   String eventName;

    public   long eventStartTimeReference;
    public   long eventEndTimeReference;
    public   Date eventStartDate;
    public   Date eventEndDate;
    public   long eventDuration;


    public BODeveloperEventModel(String eventName, HashMap<String, Object> startEventInfo) {

    }

    @Nullable
    public HashMap<String, Object> eventInfoForStorage () {

        return null;
    }
}
